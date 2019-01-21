package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.filter.StrategyContinuousDoubleFilter;
import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.FileUtil;
import com.zcy.iqoperate.util.JsonUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 连续蜡烛，结束后倍投
 * create date : 2019/1/19
 */
@Component
public class StrategyContinuousDouble {

    public void execute(List<CandlesResponse.Candle> candles, Object strategyFilterObject) {

        StrategyContinuousDoubleFilter strategyContinuousDoubleFilter = JsonUtil.convertValue(strategyFilterObject, StrategyContinuousDoubleFilter.class);

        //连续个数
        Integer continuousSize = strategyContinuousDoubleFilter.getContinuousSize();

        //支付的方向，true为相同，false为相反
        Boolean payDirect = strategyContinuousDoubleFilter.getPayDirect();

        //上一个蜡烛图信息
        CandleMessage preCandleMessage = new CandleMessage();

        //一个连续的蜡烛集合
        List<CandlesResponse.Candle> candlesResult = new ArrayList<>();

        //支付的连续的蜡烛集合
        List<CandlesResponse.Candle> payCandlesResult = new ArrayList<>();

        //连续蜡烛的涨跌
        Integer continuousTrend = null;

        //统计连续数量和个数的map
        Map<Integer, Integer> sumMap = new HashMap<>();

        //统计连续数量和蜡烛时间的map
        Map<Integer, List<String>> sumListMap = new HashMap<>();

        String content = "";

        for (CandlesResponse.Candle candle : candles) {

            //蜡烛信息
            CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);

            //当前涨跌
            Integer currentTrend = candleMessage.getTrend();

            if(continuousTrend == null){
                //和上一个一样涨跌或者是平的
                if (currentTrend.equals(preCandleMessage.getTrend()) && !currentTrend.equals(0)) {
                    //则将蜡烛放到集合中
                    candlesResult.add(candle);
                }
                //如果和上一个蜡烛图不同
                else {

                    if(candlesResult.size() < continuousSize){
                        candlesResult.clear();
                        payCandlesResult.clear();
                        continuousTrend = null;

                        //清空集合后，放入蜡烛，是下一个集合的开始
                        candlesResult.add(candle);
                    }else {
                        //获取连续蜡烛的涨跌
                        if((payCandlesResult == null || payCandlesResult.size() <=0) && continuousTrend == null){
                            continuousTrend = preCandleMessage.getTrend();
                        }
                    }
                }
            }else {
                //和上一个一样涨跌或者是平的
                if (currentTrend.equals(continuousTrend) || currentTrend.equals(0)) {
                    payCandlesResult.add(candle);
                }
                //如果和上一个蜡烛图不同
                else {
                    payCandlesResult.add(candle);

                    if (sumMap.get(payCandlesResult.size()) == null) {
                        sumMap.put(payCandlesResult.size(), 1);
                    } else {
                        sumMap.put(payCandlesResult.size(), sumMap.get(payCandlesResult.size()) + 1);
                    }

                    if (sumListMap.get(payCandlesResult.size()) == null) {
                        List<String> ss = new ArrayList<>();
                        ss.add(DateUtil.timeStampToDateString(payCandlesResult.get(0).getTo() * 1000));
                        sumListMap.put(payCandlesResult.size(), ss);
                    } else {
                        List<String> ss = sumListMap.get(payCandlesResult.size());
                        ss.add(DateUtil.timeStampToDateString(payCandlesResult.get(0).getTo() * 1000));
                        sumListMap.put(payCandlesResult.size(), ss);
                    }

                    candlesResult.clear();
                    payCandlesResult.clear();
                    continuousTrend = null;

                    //清空集合后，放入蜡烛，是下一个集合的开始
                    candlesResult.add(candle);

                }

            }

            preCandleMessage = candleMessage;

        }

        System.out.println("连续数量对应 = " + JsonUtil.ObjectToJsonString(sumMap));
        System.out.println("连续数量时间 = " + JsonUtil.ObjectToJsonString(sumListMap));

        content = content + "连续数量对应 = " + JsonUtil.ObjectToJsonString(sumMap);
        content = content + "连续数量时间 = " + JsonUtil.ObjectToJsonString(sumListMap);

        //创建策略结果文件
        if (strategyContinuousDoubleFilter.getCreateResultFile()) {
            FileUtil.createJsonFile(content, "D:/iq", "StrategyContinuousDouble_" + strategyContinuousDoubleFilter.getActiveId() + ".json");
        }
    }

}
