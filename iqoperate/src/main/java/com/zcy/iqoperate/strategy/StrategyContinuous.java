package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 连续个数
 * create date : 2019/1/19
 */
public class StrategyContinuous {

    public void execute(List<CandlesResponse.Candle> candles) {

        //上一个涨与跌
        Integer preTrend = 1;

        //记录最多连续蜡烛图个数
        Integer maxSum = 0;

        //记录最多连续蜡烛图的To
        Long maxSumTo = 0L;

        Map<Integer, List<String>> sumListMap = new HashMap<>();
        Map<Integer, Integer> sumMap = new HashMap<>();

        List<CandlesResponse.Candle> candlesResult = new ArrayList<>();

        for (CandlesResponse.Candle candle : candles) {
            CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);
            //当前涨跌
            Integer currentTrend = candleMessage.getTrend();

            //如果和上一个一样，获取是平的
            if (currentTrend.equals(0) || currentTrend.equals(preTrend)) {
                candlesResult.add(candle);

                if (candlesResult.size() > maxSum) {
                    maxSumTo = candle.getTo();
                    maxSum = candlesResult.size();
                }
            } else {

                if (sumMap.get(candlesResult.size()) == null) {
                    sumMap.put(candlesResult.size(), 1);
                } else {
                    sumMap.put(candlesResult.size(), sumMap.get(candlesResult.size()) + 1);
                }

                if (sumListMap.get(candlesResult.size()) == null) {
                    List<String> ss = new ArrayList<>();
                    ss.add(DateUtil.timeStampToDateString(candle.getTo() * 1000));
                    sumListMap.put(candlesResult.size(), ss);
                } else {
                    List<String> ss = sumListMap.get(candlesResult.size());
                    ss.add(DateUtil.timeStampToDateString(candle.getTo() * 1000));
                    sumListMap.put(candlesResult.size(), ss);
                }

                candlesResult.clear();
            }

            preTrend = currentTrend;
        }

        System.out.println("maxSum = " + maxSum);
        System.out.println("maxSumTo = " + DateUtil.timeStampToDateString(maxSumTo * 1000));
        System.out.println("maxSumTo = " + JsonUtil.ObjectToJsonString(sumMap));
        System.out.println("maxSumTo = " + JsonUtil.ObjectToJsonString(sumListMap));
    }

}
