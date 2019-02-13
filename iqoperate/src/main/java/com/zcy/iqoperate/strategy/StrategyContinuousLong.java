package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DateUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 连续长蜡烛
 * create date : 2019/1/19
 */
@Component
public class StrategyContinuousLong {

    public void execute(List<CandlesResponse.Candle> candles, Object strategyFilterObject) {

        //StrategyContinuousFilter strategyContinuousFilter = JsonUtil.convertValue(strategyFilterObject, StrategyContinuousFilter.class);

        //几个蜡烛符合达到相应长度
        //Integer conformNumber = 2;

        //支付分钟数
        //Integer payMin = 2;

        //String content = "";

        //遍历符合蜡烛个数
        for (Integer conformNumber = 4; conformNumber <= 5; conformNumber++) {
            //遍历支付分钟数
            for (Integer payMin = 1; payMin <= 3; payMin++) {
                //遍历总长度因子
                for (BigDecimal sumFactor = new BigDecimal(0.0001);
                     sumFactor.compareTo(new BigDecimal(0.001)) <= 0;
                     sumFactor = sumFactor.add(new BigDecimal(0.0001))) {

                    //遍历单个蜡烛图长度因子
                    for (BigDecimal sigleFactor = new BigDecimal(0.0001);
                         sigleFactor.compareTo(new BigDecimal(0.0005)) <= 0;
                         sigleFactor = sigleFactor.add(new BigDecimal(0.0001))) {

                        System.out.println();
                        System.out.println("符合蜡烛个数为：" + conformNumber);
                        System.out.println("支付分钟数为：" + payMin);
                        System.out.println("总长度因子为：" + sumFactor);
                        System.out.println("单个长度因子为：" + sigleFactor);

                        //记录赢输的时间点
                        List winTimeList = new ArrayList<>();
                        List lostTimeList = new ArrayList<>();

                        first:
                        for (int i = 0 + conformNumber; i < candles.size() - 1 - payMin; i++) {
                            //定义总长度
                            BigDecimal sumLong = new BigDecimal(0);

                            //获取当前蜡烛
                            CandlesResponse.Candle currentCandle = candles.get(i);

/*                            if(DateUtil.timeStampToDateString(currentCandle.getTo().longValue() * 1000).equals("2018-12-07 21:51:00"))
                            {
                                System.out.println();
                            }*/
                            //获取当前蜡烛图信息
                            CandleMessage currentCandleMessage = CandleMessage.getCandleMessage(currentCandle);

                            //当前蜡烛，是否是涨,1为涨，-1为跌，0为平
                            Integer currentTrend = currentCandleMessage.getTrend();

                            BigDecimal currentOpen = currentCandle.getOpen();
                            //单个符合条件的长度
                            BigDecimal sigleConformEntity = currentOpen.multiply(sigleFactor);
                            //总共符合条件的长度
                            BigDecimal sumConformEntity = currentOpen.multiply(sumFactor);

                            //遍历当前及前面的蜡烛
                            for (int j = 0; j < conformNumber; j++) {
                                CandleMessage candleMessage = CandleMessage.getCandleMessage(candles.get(i - j));

                                //判断涨跌方向是否相同
                                if (candleMessage.getTrend().compareTo(currentTrend) != 0) {
                                    continue first;
                                }

                                //判断长度是否符合
                                if (candleMessage.getEntity().compareTo(sigleConformEntity) < 0) {
                                    continue first;
                                }

                                //统计总长度
                                sumLong = sumLong.add(candleMessage.getEntity());
                            }

                            //判断总长度是否达标
                            if (sumLong.compareTo(sumConformEntity) < 0) {
                                continue;
                            }

                            //获取购买时间
                            String payTimeString = DateUtil.timeStampToDateString(currentCandle.getTo() * 1000);

                            //判断盈亏
                            //获取结果方向
                            Integer resultTrend = candles.get(i + payMin).getClose().subtract(currentCandle.getClose()).compareTo(new BigDecimal(0));
                            if (resultTrend != 0 && !resultTrend.equals(currentTrend)) {
                                winTimeList.add(payTimeString);
                            } else {
                                lostTimeList.add(payTimeString);
                            }
                        }

                        System.out.println("winNum = " + winTimeList.size());
                        System.out.println("lostNum = " + lostTimeList.size());
/*                        System.out.println("winTimeList = " + winTimeList);
                        System.out.println("lostTimeList = " + lostTimeList);*/

                    }

                }
            }
        }

/*        //创建策略结果文件
        if (strategyContinuousFilter.getCreateResultFile()) {
            FileUtil.createJsonFile(content, "D:/iq", "StrategyContinuous_" + strategyContinuousFilter.getActiveId() + ".json");
        }*/
    }

}
