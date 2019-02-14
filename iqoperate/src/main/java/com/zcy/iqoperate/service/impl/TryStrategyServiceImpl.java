package com.zcy.iqoperate.service.impl;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.filter.StrategyContinuousDoubleFilter;
import com.zcy.iqoperate.filter.StrategyLongFilter;
import com.zcy.iqoperate.model.request.GetCandlesRequest;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.service.TryStrategyService;
import com.zcy.iqoperate.service.WebsocketService;
import com.zcy.iqoperate.strategy.StrategyContinuousLong;
import com.zcy.iqoperate.strategy.StrategyLong1m;
import com.zcy.iqoperate.strategy.StrategyLong30s;
import com.zcy.iqoperate.strategy.StrategyShadowLineEnough;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.FileUtil;
import com.zcy.iqoperate.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create date : 2019/1/7
 */
@Service
public class TryStrategyServiceImpl implements TryStrategyService {

    //条件
    private Object strategyFilterObject;

    //获取candles循环次数 = candleDays * 2
    private Integer candlesCycleSize;

    //请求蜡烛图的id集合
    private List<String> candlesRequestIds = new ArrayList<>();

    //request_id与蜡烛集合的map
    private Map<String, List<CandlesResponse.Candle>> candlesMap = new HashMap<>();

    @Autowired
    private WebsocketService websocketService;

/*    @Autowired
    StrategyContinuous strategyContinuous;

    @Autowired
    StrategyContinuousDouble strategyContinuousDouble;*/

/*    @Autowired
    StrategyContinuousOver strategyContinuousOver;*/

/*    @Autowired
    StrategyContinuousOverDelay strategyContinuousOverDelay;*/

/*    @Autowired
    StrategyLong1m strategyLong1m = new StrategyLong1m();*/
/*
    @Autowired
    StrategyLong30s strategyLong30s = new StrategyLong30s();*/

/*    @Autowired
    StrategyContinuousLong strategyContinuousLong = new StrategyContinuousLong();*/

    @Autowired
    StrategyShadowLineEnough strategyShadowLineEnough = new StrategyShadowLineEnough();

    /**
     * 执行
     *
     * @param strategyFilterObject
     */
    @Override
    public BtResult execute(Object strategyFilterObject) throws InterruptedException {

        //清空请求蜡烛图的id集合
        candlesRequestIds.clear();

        //清空request_id与蜡烛集合的map
        candlesMap.clear();

        //初始化传入参数
        this.strategyFilterObject = strategyFilterObject;

        StrategyLongFilter strategyFilter = JsonUtil.convertValue(strategyFilterObject, StrategyLongFilter.class);
        if (strategyFilter == null) {
            return BtResult.ERROR("解析参数失败");
        }

        //如果是从文件中读取candles
        if(strategyFilter.getReadCandlesFromFile()){

            //从文件读取蜡烛集合
            List<Map> fileCandlesMapList = (List) FileUtil.readFileToObject("D:/iq/candles_"+strategyFilter.getActiveId()+".json", List.class);

            //将读取的蜡烛类型转换
            List<CandlesResponse.Candle> allCandles = new ArrayList<>();
            for(Map map : fileCandlesMapList){
                CandlesResponse.Candle candle = JsonUtil.convertValue(map, CandlesResponse.Candle.class);
                allCandles.add(candle);
            }

            strategyShadowLineEnough.execute(allCandles, strategyFilterObject);
            return BtResult.OK();
        }

        //每个蜡烛图秒数
        Integer candleSize = strategyFilter.getCandleSize();

        //外汇id
        Integer activeId = strategyFilter.getActiveId();
        if (activeId == null) {
            return BtResult.ERROR("未传activeId");
        }

        //初始化查询天数
        Integer candleDays = strategyFilter.getCandleDays();
        if (candleDays == null) {
            return BtResult.ERROR("未传candleDays");
        }

        //Long currentId = IqUtil.getCurrentId();
        //Long currentId = 447397L;
        //Long currentId = 226255L;
        Long currentId = strategyFilter.getCurrentId();

        // 12*60
        //每天有的个数
        Integer dayIdSize = 24*60*60 / strategyFilter.getCandleSize();

        //id跳过个数
        //Integer skipIdSize = 12*60;
        Integer skipIdSize = 200;

        //获取candles循环次数 = candleDays * 2
        candlesCycleSize = candleDays * (dayIdSize / skipIdSize);

        //计算出第一个请求的id
        currentId = currentId - candlesCycleSize * skipIdSize;

        //将其进行赋值临时处理。因为这边接收candles那边candlesCycleSize会自减，造成数据冲突
        Integer candlesCycleSizeTemp = candlesCycleSize;

        for (int i = 0; i < candlesCycleSizeTemp; i++) {

            System.out.println("发送消息总次数 = " + candlesCycleSizeTemp + "   当前次数 = " + i);

            //每隔10次，睡眠2s
            if(i % 10 == 0){
                Thread.sleep(5000L);
            }

            String request_id = String.valueOf(System.currentTimeMillis() + i);
            candlesRequestIds.add(request_id);

            GetCandlesRequest getCandlesRequest = new GetCandlesRequest(
                    request_id,
                    activeId,
                    candleSize,
                    currentId - skipIdSize,
                    currentId);

            websocketService.sendMessage(getCandlesRequest);

            currentId = currentId + skipIdSize;
        }

        return BtResult.OK();
    }

    /**
     * 策略
     *
     * @param candlesResponse
     */
    @Override
    public void strategy(CandlesResponse candlesResponse) {

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

        //candles去除第一个，解决造成重复的问题
        candles.remove(0);

        candlesMap.put(candlesResponse.getRequest_id(), candles);

        //查询天数自减，
        candlesCycleSize --;

        //直到0时说明所有蜡烛信息收集完毕
        if (candlesCycleSize.equals(0)) {

            //汇总分批查询到的蜡烛图
            List<CandlesResponse.Candle> allCandles = new ArrayList<>();
            for (String requestId : candlesRequestIds) {
                allCandles.addAll(candlesMap.get(requestId));
            }

            //最后一个蜡烛图时间
            Long lastCandleFrom = allCandles.get(allCandles.size() - 1).getFrom();
            System.out.println("最后一个蜡烛图时间是："  + DateUtil.timeStampToDateString(lastCandleFrom * 1000));

            //System.out.println("所有蜡烛图信息：" + JsonUtil.ObjectToJsonString(allCandles));

            StrategyContinuousDoubleFilter strategyContinuousDoubleFilter
                    = JsonUtil.convertValue(strategyFilterObject, StrategyContinuousDoubleFilter.class);

            //创建蜡烛图集合文件
            if(strategyContinuousDoubleFilter.getCreateCandlesFile()){
                FileUtil.createJsonFile(JsonUtil.ObjectToJsonString(allCandles), "D:/iq","candles_" + strategyContinuousDoubleFilter.getActiveId() + ".json");
            }

            //执行策略
            strategyShadowLineEnough.execute(allCandles, strategyFilterObject);
        }
    }
}
