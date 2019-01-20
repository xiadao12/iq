package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.filter.StrategyContinuousFilter;
import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.FileUtil;
import com.zcy.iqoperate.util.JsonUtil;
import com.zcy.iqoperate.util.MapUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 连续个数
 * create date : 2019/1/19
 */
@Component
public class StrategyContinuous {

    public void execute(List<CandlesResponse.Candle> candles, Object strategyFilterObject) {

        StrategyContinuousFilter strategyContinuousFilter = JsonUtil.convertValue(strategyFilterObject, StrategyContinuousFilter.class);

        //因子最小值
        BigDecimal startFactor = strategyContinuousFilter.getStartFactor();

        //因子最大值
        BigDecimal endFactor = strategyContinuousFilter.getEndFactor();

        //因子从小到大间距
        BigDecimal factorDistance = strategyContinuousFilter.getFactorDistance();

        //几个蜡烛符合达到相应长度
        Integer conformNumber = strategyContinuousFilter.getConformNumber();

        //从第几个蜡烛图开始支付
        Integer payFromNumber = strategyContinuousFilter.getPayFromNumber();

        String content = "";

        //遍历因子
        //遍历起止因子
        for (BigDecimal factor = startFactor; factor.compareTo(endFactor) <= 0; factor = factor.add(factorDistance)) {

            System.out.println();
            System.out.println("计算因子为：" + factor);

            content = content + "计算因子为：" + factor;

            //上一个蜡烛图信息
            CandleMessage preCandleMessage = new CandleMessage();

            //统计连续数量和个数的map
            Map<Integer, Integer> sumMap = new HashMap<>();

            //统计连续数量和蜡烛时间的map
            Map<Integer, List<String>> sumListMap = new HashMap<>();

            //一个连续的蜡烛集合
            List<CandlesResponse.Candle> candlesResult = new ArrayList<>();

            //支付的连续的蜡烛集合
            List<CandlesResponse.Candle> payCandlesResult = new ArrayList<>();

            //符合长度的蜡烛图数量
            Integer conformCandleSum = 0;

            for (CandlesResponse.Candle candle : candles) {

/*                if(DateUtil.timeStampToDateString(candle.getTo() * 1000).equals("2018-12-11 16:17:00")){
                    System.out.println();
                }

                if (factor.compareTo(new BigDecimal(0.00010)) >= 0 && DateUtil.timeStampToDateString(candle.getTo() * 1000).equals("2018-12-11 16:17:00")) {
                    System.out.println();
                }*/
                //蜡烛信息
                CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);

                //当前涨跌
                Integer currentTrend = candleMessage.getTrend();

                //符合的实体长度
                BigDecimal conformEntity = factor.multiply(candle.getOpen());

                // 符合以下条件
                // 1 和上一个一样涨跌
                // 2 蜡烛图是平的而且连续结果集合数量是大于等于支付的开始蜡烛图
                if (currentTrend.equals(preCandleMessage.getTrend()) || (currentTrend.equals(0) && currentTrend.equals(0) && candlesResult.size() + 1 >= payFromNumber)) {

                    //记录第一个蜡烛图
                    if (candlesResult.size() == 1) {
                        //记录第一个蜡烛信息
                        CandleMessage firstCandleMessage = CandleMessage.getCandleMessage(candlesResult.get(0));

                        //如果第一个蜡烛图达到符合实体的长度
                        if (firstCandleMessage.getEntity().compareTo(conformEntity) >= 0) {
                            //符合实体的数量相加
                            conformCandleSum++;
                        }
                    }

                    //则将蜡烛放到集合中
                    candlesResult.add(candle);

                    //如果达到符合实体的长度
                    if (candleMessage.getEntity().compareTo(conformEntity) >= 0) {
                        //符合实体的数量相加
                        conformCandleSum++;
                    }

                    //如果符合实体的数量 大于几个蜡烛符合达到相应长度（conformNumber）
                    //而且符合开始支付的数量,注：candlesResult.size() + 1 >= payFromNumber，正常应该是向前面+1再进行判断的，但因为candlesResult提前把candle放进去了，所以抵消不用+1
                    if (conformCandleSum >= conformNumber && candlesResult.size() >= payFromNumber) {
                        //则将蜡烛放到集合中
                        payCandlesResult.add(candle);
                    }

                } else {
                    //如果和上一个蜡烛图不同
                    //排除蜡烛是平的，而且连续结果集合数量是否小于支付的开始蜡烛图，
                    if (!(currentTrend.equals(0) && candlesResult.size() + 1 < payFromNumber)) {

                        //把反转的蜡烛也放到支付集合里
                        if(conformCandleSum >= conformNumber && candlesResult.size() + 1 >= payFromNumber){
                            payCandlesResult.add(candle);
                        }

                        // 1 支付结果有数据
                        // 2 或者支付结果没数据，但正好是第五个蜡烛图反转
                        if ((payCandlesResult != null && payCandlesResult.size() > 0)) {
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
                        }
                    }

                    candlesResult.clear();
                    payCandlesResult.clear();
                    conformCandleSum = 0;

                    //清空集合后，放入蜡烛，是下一个集合的开始
                    candlesResult.add(candle);
                }

                preCandleMessage = candleMessage;

            }

            //按key排序
            sumMap = MapUtil.sortMapByKey(sumMap);

            System.out.println("连续数量对应 = " + JsonUtil.ObjectToJsonString(sumMap));
            System.out.println("连续数量时间 = " + JsonUtil.ObjectToJsonString(sumListMap));

            content = content + "连续数量对应 = " + JsonUtil.ObjectToJsonString(sumMap);
            content = content + "连续数量时间 = " + JsonUtil.ObjectToJsonString(sumListMap);

            //循环一个因子后清除数据
            //上一个涨与跌
            preCandleMessage = new CandleMessage();

            sumListMap = new HashMap<>();
            sumMap = new HashMap<>();

            candlesResult = new ArrayList<>();
        }

        //创建策略结果文件
        if(strategyContinuousFilter.getCreateResultFile()){
            FileUtil.createJsonFile(content, "D:/iq", "StrategyContinuous_"+strategyContinuousFilter.getActiveId()+".json");
        }
    }

}
