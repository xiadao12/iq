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
 * 连续蜡烛，直到结束，买3分钟以上蜡烛
 * create date : 2019/1/19
 */
@Component
public class StrategyContinuousOverDelay {

    public void execute(List<CandlesResponse.Candle> candles, Object strategyFilterObject) {

        StrategyContinuousDoubleFilter strategyContinuousDoubleFilter = JsonUtil.convertValue(strategyFilterObject, StrategyContinuousDoubleFilter.class);

        //上一个蜡烛图信息
        CandleMessage preCandleMessage = new CandleMessage();

        //一个连续的蜡烛集合
        List<CandlesResponse.Candle> candlesResult = new ArrayList<>();

        //连续蜡烛的涨跌方向
        Integer continuousTrend = null;

        //延迟几分钟购买
        for (int delayMinutes = 2; delayMinutes <= 5; delayMinutes++) {
            String content = "";

            //连续蜡烛图个数以上（包含）
            for (int minCandlenum = 8; minCandlenum <= 8; minCandlenum++) {

                //反向的蜡烛图长度因子
                for(BigDecimal factor = new BigDecimal(0.00001); factor.compareTo(new BigDecimal(0.0002)) <= 0; factor = factor.add(new BigDecimal(0.00001))){

                    System.out.println();
                    System.out.println("延迟购买分钟 = " + delayMinutes);
                    content = content + "延迟购买分钟  = " + delayMinutes;

                    System.out.println("最少的连续蜡烛数量 = " + minCandlenum);
                    content = content + "最少的连续蜡烛数量  = " + minCandlenum;

                    System.out.println("反向的蜡烛图长度因子 = " + factor);
                    content = content + "反向的蜡烛图长度因子  = " + factor;

                    //记录输赢的次数
                    Integer winNum = 0;
                    Integer lostNum = 0;

                    //记录赢输的时间点
                    List<String> winTimeList = new ArrayList<>();
                    List<String> lostTimeList = new ArrayList<>();

                    //遍历蜡烛
                    for (int k=0;k<candles.size() - delayMinutes;k++) {

                        CandlesResponse.Candle candle = candles.get(k);

                        //蜡烛信息
                        CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);

                        //当前涨跌
                        Integer currentTrend = candleMessage.getTrend();

                        if (continuousTrend == null) {
                            //和上一个一样涨跌或者是平的
                            if (currentTrend.equals(preCandleMessage.getTrend()) && !currentTrend.equals(0)) {
                                //则将蜡烛放到集合中
                                candlesResult.add(candle);
                            }
                            //如果和上一个蜡烛图不同
                            else {

                                //反向的蜡烛最大长度
                                BigDecimal candleMaxSize = candle.getOpen().multiply(factor);

                                if (candlesResult.size() < minCandlenum || candleMessage.getEntity().compareTo(candleMaxSize) > 0) {
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
                            BigDecimal buy = candle.getOpen();

                            CandlesResponse.Candle resultCandle = candles.get(k + delayMinutes);

                            if((continuousTrend > 0 && resultCandle.getClose().compareTo(buy) < 0) || (continuousTrend < 0 && resultCandle.getClose().compareTo(buy) > 0)){

                                winNum ++;
                                winTimeList.add(DateUtil.timeStampToDateString(candle.getTo() * 1000));

                                candlesResult.clear();
                                continuousTrend = null;

                                //清空集合后，放入蜡烛，是下一个集合的开始
                                candlesResult.add(candle);
                            }else{
                                lostNum ++;
                                lostTimeList.add(DateUtil.timeStampToDateString(candle.getTo() * 1000));

                                candlesResult.clear();
                                continuousTrend = null;

                                //清空集合后，放入蜡烛，是下一个集合的开始
                                candlesResult.add(candle);
                            }
                        }

                        preCandleMessage = candleMessage;

                    }

                    System.out.println("winNum = " + winNum);
                    System.out.println("lostNum = " + lostNum);
                    System.out.println("winTimeList = " + winTimeList);
                    System.out.println("lostTimeList = " + lostTimeList);

                    content = content + "\nwinNum = " + winNum + "\n";
                    content = content + "lostNum = " + lostNum + "\n";
                    content = content + "winTimeList = " + lostTimeList + "\n";
                    content = content + "lostTimeList = " + lostTimeList + "\n";
                }
            }

            //创建策略结果文件
            if (strategyContinuousDoubleFilter.getCreateResultFile()) {
                FileUtil.createFile(content, "D:/iq", "StrategyContinuousOverDelay_"+ delayMinutes + strategyContinuousDoubleFilter.getActiveId() + ".json");
            }
        }

    }

}
