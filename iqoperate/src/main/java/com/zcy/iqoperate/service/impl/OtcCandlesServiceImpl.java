package com.zcy.iqoperate.service.impl;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.filter.CandlesFilter;
import com.zcy.iqoperate.model.request.GetCandlesRequest;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.service.OtcCandlesService;
import com.zcy.iqoperate.service.WebsocketService;
import com.zcy.iqoperate.strategy.StrategyContinuousOverOTC;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.FileUtil;
import com.zcy.iqoperate.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 获取otc蜡烛集合
 * create date : 2019/1/7
 */
@Service
public class OtcCandlesServiceImpl implements OtcCandlesService {

    @Autowired
    private WebsocketService websocketService;

    //请求的第一个id
    public static String firstRequestId;

    //请求的最后一个id
    public static String lastRequestId;

    //请求的第一个结束时间
    public static Long firstTo = null;

    //请求的最后一个结束时间
    public static Long lastTo = null;

    //每次查询个数
    public static Integer perSize;

    //蜡烛结束时间to与蜡烛集合的map，用于存放接收到的蜡烛对应关系
    public Map<Long, CandlesResponse.Candle> toCandleMap = new LinkedHashMap<>();

    /**
     * 发送请求获取蜡烛集合
     *
     * @param candlesFilter
     */
    @Override
    public BtResult sendGetCandles(CandlesFilter candlesFilter) throws Exception {

        //每个蜡烛图秒数
        Integer candleSize = candlesFilter.getCandleSize();

        //外汇id
        Integer activeId = candlesFilter.getActiveId();
        if (activeId == null) {
            return BtResult.ERROR("未传activeId");
        }

        //初始化查询天数
        Integer candleDays = candlesFilter.getCandleDays();
        if (candleDays == null) {
            return BtResult.ERROR("未传candleDays");
        }

        //请求的id
        Long currentId = candlesFilter.getCurrentId();
        if (currentId == null) {
            return BtResult.ERROR("未传请求的id");
        }

        //每次查询个数
        perSize = 50;

        //每天有的个数
        Integer dayIdSize = 24 * 60 * 60 / candleSize;

        //获取candles循环次数 = candleDays * 2
        Integer candlesCycleSize = candleDays * (dayIdSize / perSize);

        //计算出第一个请求的id
        currentId = currentId - candlesCycleSize * perSize;

        //将其进行赋值临时处理。因为这边接收candles那边candlesCycleSize会自减，造成数据冲突
        //Integer candlesCycleSizeTemp = candlesCycleSize;

        for (int i = 0; i < candlesCycleSize; i++) {

            System.out.println("发送消息总次数 = " + candlesCycleSize + "   当前次数 = " + i);

            //每隔10次，睡眠2s
            if (i % 10 == 0) {
                Thread.sleep(1000L);
            }

            String request_id = String.valueOf(System.currentTimeMillis() + i);
            if (i == 0) {
                firstRequestId = request_id;
            }
            if (i == candlesCycleSize - 1) {
                //最后一个请求id
                lastRequestId = request_id;
            }

            GetCandlesRequest getCandlesRequest = new GetCandlesRequest(
                    request_id,
                    activeId,
                    candleSize,
                    currentId - perSize,
                    currentId);

            websocketService.sendMessage(getCandlesRequest);

            currentId = currentId + perSize;
        }

        System.out.println("发送请求消息结束，正在收集所有信息.....");

        //存放一个周末的蜡烛集合
        List<CandlesResponse.Candle> weekCandles = new ArrayList<>();

        Long j = null;
        while (true) {
            if (firstTo == null) {
                Thread.sleep(2000);
                continue;
            }

            if (j == null) {
                j = firstTo;
            }

            if (lastTo != null && j > lastTo) {
                break;
            }

            //从map中获取蜡烛
            CandlesResponse.Candle candle = null;

            //获取蜡烛，允许重试5次
            //Integer retry = 5;
            //while(retry > 0){
            while (true) {
                candle = toCandleMap.get(j);
                if (candle != null) {
                    break;
                }

                Thread.sleep(2);

                //retry --;
            }

            //判断是否符合时间，如果不符合时间
            if (!StrategyContinuousOverOTC.judgeOTCTime(candle.getTo() * 1000, null, null)) {
                //将weekCandles集合中的蜡烛保存到文件中
                if (weekCandles != null && weekCandles.size() > 0) {

                    //判断是否是连续的
                    if(judgeCandlesContinue(weekCandles,candleSize)){
                        //获取周六的日期
                        String firstTime = DateUtil.timeStampToDateString(weekCandles.get(0).getTo() * 1000);
                        firstTime = firstTime.substring(0, firstTime.indexOf(" "));
                        String fileName = "otc_" + activeId + "_" + firstTime + ".json";
                        //创建蜡烛图集合文件
                        FileUtil.createJsonFile(JsonUtil.ObjectToJsonString(weekCandles), "D:/iq/otc/candles/", fileName);
                        System.out.println("生成" + firstTime);
                    }else {
                        System.out.println("蜡烛集合不连续，无法生成文件");
                    }
                }

                //清空集合
                weekCandles.clear();

                //如果不符合时间,则从map中移除
                toCandleMap.remove(j);

                j = j + candleSize;
                continue;
            }

            //判断第一个蜡烛是否是在周末，如果在，j=j+candleSize，直接略过三天
            if (j == firstTo) {
                j = j + 3 * dayIdSize;
                //j = 10L;
                System.out.println("第一个蜡烛是周末，跳过三天");
                continue;
            }

            //添加到集合中
            weekCandles.add(candle);
            System.out.println("将结束时间为" + candle.getTo() + "蜡烛加入到集合中");
            //则从map中移除
            toCandleMap.remove(j);

            j = j + candleSize;
        }

        System.out.println("-------------结束蜡烛集合收集-------------");

        System.out.println("第一个蜡烛时间是：" + DateUtil.timeStampToDateString(firstTo * 1000));
        System.out.println("最后一个蜡烛时间是：" + DateUtil.timeStampToDateString(lastTo * 1000));

        return BtResult.OK();
    }


    /**
     * 接收请求到的蜡烛集合
     *
     * @throws Exception
     */
    @Override
    public void receiveCandles(CandlesResponse candlesResponse) throws Exception {

        //判断传参
        if (candlesResponse == null) {
            return;
        }

        CandlesResponse.Msg msg = candlesResponse.getMsg();
        if (msg == null) {
            return;
        }

        List<CandlesResponse.Candle> candles = msg.getCandles();
        if (candles == null || candles.size() <= 0) {
            return;
        }

        String requestId = candlesResponse.getRequest_id();

        //获取请求的第一个结束时间
        if (requestId.equals(firstRequestId)) {
            firstTo = candles.get(0).getTo();
        } else {
            //candles去除第一个，解决造成重复的问题
            //candles.remove(0);
        }

        //请求的最后一个结束时间
        if (requestId.equals(lastRequestId)) {
            lastTo = candles.get(candles.size() - 1).getTo();
        }

        //Long i = firstCurrentId + 1; i <= lastCurrentId; i++

        for (CandlesResponse.Candle candle : candles) {
            toCandleMap.put(candle.getTo(), candle);
        }

    }

    /**
     * 判断蜡烛是否是连续的
     * @param candles
     * @param candleSize
     * @return
     */
    private Boolean judgeCandlesContinue(List<CandlesResponse.Candle> candles, Integer candleSize){

        if(candles == null || candles.size() <= 0){
            return false;
        }

        //记录上一个蜡烛的to
        Long preTo = null;
        for(CandlesResponse.Candle candle : candles){
            Long to = candle.getTo();

            if(preTo == null){
                preTo = to;
                continue;
            }

            if(!to.equals(preTo + candleSize)){
                return false;
            }

            preTo = to;

        }

        return true;
    }
}
