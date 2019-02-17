package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.filter.StrategyContinuousDoubleFilter;
import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.FileUtil;
import com.zcy.iqoperate.util.JsonUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 连续蜡烛，直到结束
 * create date : 2019/1/19
 */
@Component
public class StrategyContinuousOver {

    public void execute(List<CandlesResponse.Candle> candles, Object strategyFilterObject) {

        StrategyContinuousDoubleFilter strategyContinuousDoubleFilter = JsonUtil.convertValue(strategyFilterObject, StrategyContinuousDoubleFilter.class);

        //上一个蜡烛图信息
        CandleMessage preCandleMessage = new CandleMessage();

        //一个连续的蜡烛集合
        List<CandlesResponse.Candle> candlesResult = new ArrayList<>();

        //连续蜡烛的涨跌方向
        Integer continuousTrend = null;

        //延迟几分钟购买
        for (int delayMinutes = 1; delayMinutes <= 2; delayMinutes++) {
            String content = "";

            //连续蜡烛图个数以上（包含）
            for (int minCandlenum = 0; minCandlenum <= 3; minCandlenum++) {

                //所有蜡烛图长度因子
                for (BigDecimal allFactor = new BigDecimal(0.0001);
                     allFactor.compareTo(new BigDecimal(0.0005)) <= 0;
                     allFactor = allFactor.add(new BigDecimal(0.0001))) {

                    //反向的蜡烛图长度因子
                    for (BigDecimal reverseFactor = new BigDecimal(0.00001);
                         reverseFactor.compareTo(new BigDecimal(0.00001)) <= 0;
                         reverseFactor = reverseFactor.add(new BigDecimal(0.00001))) {

                        System.out.println();
                        System.out.println("延迟购买分钟 = " + delayMinutes);
                        content = content + "延迟购买分钟  = " + delayMinutes;

                        System.out.println("最少的连续蜡烛数量 = " + minCandlenum);
                        content = content + "最少的连续蜡烛数量  = " + minCandlenum;

                        System.out.println("所有蜡烛图长度因子 = " + allFactor);
                        content = content + "所有蜡烛图长度因子  = " + allFactor;

                        System.out.println("反向的蜡烛图长度因子 = " + reverseFactor);
                        content = content + "反向的蜡烛图长度因子  = " + reverseFactor;

                        //记录赢输的时间点
                        List<String> winTimeList = new ArrayList<>();
                        List<String> lostTimeList = new ArrayList<>();

                        //赋值第一个蜡烛
                        candlesResult.add(candles.get(0));
                        preCandleMessage = CandleMessage.getCandleMessage(candles.get(0));

                        //遍历蜡烛
                        for (int k = 0; k < candles.size() - delayMinutes; k++) {

                            CandlesResponse.Candle candle = candles.get(k);

                            //蜡烛信息
                            CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);

                            //当前涨跌
                            Integer currentTrend = candleMessage.getTrend();

                            if (continuousTrend == null) {
                                //和上一个一样涨跌，且不是平的
                                if (currentTrend.equals(preCandleMessage.getTrend()) && !currentTrend.equals(0)) {
                                    //则将蜡烛放到集合中
                                    candlesResult.add(candle);
                                }
                                //如果和上一个蜡烛图不同
                                else {
                                    //反向的蜡烛最大长度
                                    BigDecimal candleMaxSize = candle.getOpen().multiply(reverseFactor);

                                    //符合连续蜡烛的最小长度
                                    BigDecimal allCandleMinSize = candle.getOpen().multiply(allFactor);

                                    //连续蜡烛中最后一个蜡烛的close
                                    BigDecimal lastCandleClose = candlesResult.get(candlesResult.size() - 1).getClose();

                                    //连续蜡烛中第一个蜡烛的close
                                    BigDecimal firstCandleOpen = candlesResult.get(0).getOpen();

                                    //获取总蜡烛长度
                                    BigDecimal allEntity = lastCandleClose.subtract(firstCandleOpen).abs();

                                    //判断连续个数,反向蜡烛实体长度,总蜡烛长度
                                    if (
                                            candlesResult.size() < minCandlenum
                                                    ||
                                                    candleMessage.getEntity().compareTo(candleMaxSize) > 0
                                                    ||
                                                    allEntity.compareTo(allCandleMinSize) < 0) {
                                        candlesResult.clear();
                                        continuousTrend = null;

                                        //清空集合后，放入蜡烛，是下一个集合的开始
                                        candlesResult.add(candle);
                                    } else {
                                        //获取连续蜡烛的涨跌
                                        continuousTrend = preCandleMessage.getTrend();
                                    }
                                }
                            } else {
                                //////////////////////////////////////
                                //反向蜡烛下一个蜡烛的开始
                                BigDecimal buy = candle.getOpen();

                                //结果蜡烛
                                CandlesResponse.Candle resultCandle = candles.get(k + delayMinutes - 1);

                                if ((continuousTrend > 0 && resultCandle.getClose().compareTo(buy) < 0) || (continuousTrend < 0 && resultCandle.getClose().compareTo(buy) > 0)) {

                                    winTimeList.add(DateUtil.timeStampToDateString(preCandleMessage.getTo() * 1000));

                                    candlesResult.clear();
                                    continuousTrend = null;

                                    //清空集合后，放入蜡烛，是下一个集合的开始
                                    candlesResult.add(candle);
                                } else {
                                    lostTimeList.add(DateUtil.timeStampToDateString(preCandleMessage.getTo() * 1000));

                                    candlesResult.clear();
                                    continuousTrend = null;

                                    //清空集合后，放入蜡烛，是下一个集合的开始
                                    candlesResult.add(candle);
                                }
                            }
                            preCandleMessage = candleMessage;
                        }

                        System.out.println("winNum = " + winTimeList.size());
                        System.out.println("lostNum = " + lostTimeList.size());
                        System.out.println("winTimeList = " + winTimeList);
                        System.out.println("lostTimeList = " + lostTimeList);

                        content = content + "\nwinNum = " + winTimeList.size() + "\n";
                        content = content + "lostNum = " + lostTimeList.size() + "\n";
                        content = content + "winTimeList = " + lostTimeList + "\n";
                        content = content + "lostTimeList = " + lostTimeList + "\n";
                    }
                }

            }

            //创建策略结果文件
            if (strategyContinuousDoubleFilter.getCreateResultFile()) {
                FileUtil.createFile(content, "D:/iq", "StrategyContinuousOverDelay_" + delayMinutes + strategyContinuousDoubleFilter.getActiveId() + ".json");
            }
        }

    }

}
