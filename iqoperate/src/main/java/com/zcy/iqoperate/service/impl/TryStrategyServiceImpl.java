package com.zcy.iqoperate.service.impl;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.filter.StrategyContinuousFilter;
import com.zcy.iqoperate.model.request.GetCandlesRequest;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.service.TryStrategyService;
import com.zcy.iqoperate.service.WebsocketService;
import com.zcy.iqoperate.strategy.StrategyContinuous;
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

    @Autowired
    StrategyContinuous strategyContinuous;

    /**
     * 执行
     *
     * @param strategyFilterObject
     */
    @Override
    public BtResult execute(Object strategyFilterObject) {

        //清空请求蜡烛图的id集合
        candlesRequestIds.clear();

        //清空request_id与蜡烛集合的map
        candlesMap.clear();

        //每个蜡烛图秒数
        Integer candleSize = 60;

        //初始化传入参数
        this.strategyFilterObject = strategyFilterObject;

        StrategyContinuousFilter strategyContinuousFilter = JsonUtil.convertValue(strategyFilterObject, StrategyContinuousFilter.class);
        if (strategyContinuousFilter == null) {
            return BtResult.ERROR("解析参数失败");
        }

        //如果是从文件中读取candles
        if(strategyContinuousFilter.getReadCandlesFromFile()){

            //从文件读取蜡烛集合
            List<Map> fileCandlesMapList = (List) FileUtil.readFileToObject("D:/iq/candles.json", List.class);

            //将读取的蜡烛类型转换
            List<CandlesResponse.Candle> allCandles = new ArrayList<>();
            for(Map map : fileCandlesMapList){
                CandlesResponse.Candle candle = JsonUtil.convertValue(map, CandlesResponse.Candle.class);
                allCandles.add(candle);
            }

            strategyContinuous.execute(allCandles, strategyFilterObject);
            return BtResult.OK();
        }

        //外汇id
        Integer activeId = strategyContinuousFilter.getActiveId();
        if (activeId == null) {
            return BtResult.ERROR("未传activeId");
        }

        //初始化查询天数
        Integer candleDays = strategyContinuousFilter.getCandleDays();
        if (candleDays == null) {
            return BtResult.ERROR("未传candleDays");
        }

        //Long currentId = IqUtil.getCurrentId();
        //Long currentId = 447397L;
        //Long currentId = 226255L;
        Long currentId = strategyContinuousFilter.getCurrentId();

        // 12*60
        Integer halfdayIdSize = 720;

        //id跳过个数
        Integer skipIdSize = halfdayIdSize;

        //获取candles循环次数 = candleDays * 2
        candlesCycleSize = candleDays * 2;

        //计算出第一个请求的id
        currentId = currentId - candlesCycleSize * halfdayIdSize;

        for (int i = 0; i < candlesCycleSize; i++) {

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
        candlesCycleSize--;

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

            System.out.println("所有蜡烛图信息：" + JsonUtil.ObjectToJsonString(candles));

            FileUtil.createJsonFile(JsonUtil.ObjectToJsonString(candles), "D:/iq","candles.json");
            strategyContinuous.execute(allCandles, strategyFilterObject);
        }
    }
}
