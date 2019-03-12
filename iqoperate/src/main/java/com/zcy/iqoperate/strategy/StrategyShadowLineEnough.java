package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.core.StrategyUtil;
import com.zcy.iqoperate.filter.StrategyBaseFilter;
import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DateUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 影线
 * <p>
 * create date : 2019/1/19
 */
@Component
public class StrategyShadowLineEnough implements StrategyBase {

    public void execute(List<CandlesResponse.Candle> candles, Object strategyFilterObject) {

/*        //符合长度的主影线长度因子
        BigDecimal conformMainShadowFactor = new BigDecimal(0.0001);

        //符合长度的副影线长度因子
        BigDecimal conformSecondShadowFactor = new BigDecimal(0.00001);

        //符合长度的实体长度因子
        BigDecimal conformEntityShadowFactor = new BigDecimal(0.00001);*/

        //支付分钟数
        //Integer payMin = 2;

        //前面蜡烛连续个数
        //Integer preSize = 3;

        //长影线蜡烛是否反向
        Boolean entityReverse = true;

        //每天开始时间
        List<StrategyBaseFilter.ActiveTime> activeTimes = new ArrayList<>();
        StrategyBaseFilter.ActiveTime activeTime1 =
                new StrategyBaseFilter.ActiveTime("14:00:00", "23:59:59");
        StrategyBaseFilter.ActiveTime activeTime2 =
                new StrategyBaseFilter.ActiveTime("00:00:00", "04:00:00");
        activeTimes.add(activeTime1);
        activeTimes.add(activeTime2);

        //前面蜡烛连续至少个数
        for (Integer preSize = 2; preSize <= 2; preSize++) {
            //前面蜡烛连续总长度
            for (BigDecimal totalSizeFactor = new BigDecimal(0.00001);
                 totalSizeFactor.compareTo(new BigDecimal(0.0001)) <= 0;
                 totalSizeFactor = totalSizeFactor.add(new BigDecimal(0.00001))) {
                //支付分钟数
                for (Integer payMin = 1; payMin <= 3; payMin++) {
                    //符合长度的主影线长度因子
                    for (BigDecimal conformMainShadowFactor = new BigDecimal(0.00001);
                         conformMainShadowFactor.compareTo(new BigDecimal(0.0001)) <= 0;
                         conformMainShadowFactor = conformMainShadowFactor.add(new BigDecimal(0.00001))) {

                        //符合长度的副影线长度因子
                        for (BigDecimal conformSecondShadowFactor = new BigDecimal(0.00001);
                             conformSecondShadowFactor.compareTo(new BigDecimal(0.0001)) <= 0;
                             conformSecondShadowFactor = conformSecondShadowFactor.add(new BigDecimal(0.00001))) {

                            //符合长度的实体长度因子
                            for (BigDecimal conformEntityShadowFactor = new BigDecimal(0.00001);
                                 conformEntityShadowFactor.compareTo(new BigDecimal(0.0001)) <= 0;
                                 conformEntityShadowFactor = conformEntityShadowFactor.add(new BigDecimal(0.00001))) {

                                System.out.println();
                                System.out.println("前面蜡烛连续个数：" + preSize);
                                System.out.println("支付分钟数：" + payMin);
                                System.out.println("主影线长度因子：" + conformMainShadowFactor);
                                System.out.println("副影线长度因子：" + conformSecondShadowFactor);
                                System.out.println("实体长度因子：" + conformEntityShadowFactor);
                                System.out.println("长影线蜡烛是否反向：" + entityReverse);
                                System.out.println("符合时间：");
                                activeTimes.stream().forEach(u ->
                                        System.out.println("[" + u.getActiveStartTimeString() + u.getActiveEndTimeString() + "]，"));

                                //记录赢输的时间点
                                List winTimeList = new ArrayList<>();
                                List lostTimeList = new ArrayList<>();

                                first:
                                for (int i = 0; i < candles.size() - payMin; i++) {

                                    //获取当前蜡烛
                                    CandlesResponse.Candle currentCandle = candles.get(i);

                                    ///////////////////判断是否符合时间
                                    if (StrategyUtil.judgeActivetime(activeTimes, currentCandle)) {
                                        continue;
                                    }
/*
                                if (preSize == 2 && DateUtil.timeStampToDateString(currentCandle.getTo().longValue() * 1000).equals("2018-12-11 06:26:00")) {
                                    System.out.println();
                                }*/

                                    BigDecimal currentClose = currentCandle.getClose();

                                    //符合条件的主影线长度
                                    BigDecimal conformMainShadow = currentClose.multiply(conformMainShadowFactor);

                                    //符合长度的副影线长度
                                    BigDecimal conformSecondShadow = currentClose.multiply(conformSecondShadowFactor);

                                    //符合长度的实体长度
                                    BigDecimal conformEntityShadow = currentClose.multiply(conformEntityShadowFactor);

                                    //获取当前蜡烛图信息
                                    CandleMessage currentCandleMessage = CandleMessage.getCandleMessage(currentCandle);

                                    //上影线长
                                    BigDecimal upperShadow = currentCandleMessage.getUpperShadow();
                                    //下影线长
                                    BigDecimal lowerShadow = currentCandleMessage.getLowerShadow();
                                    //实体长度
                                    BigDecimal entity = currentCandleMessage.getEntity();

                                    //获取购买时间
                                    String payTimeString = DateUtil.timeStampToDateString(currentCandle.getTo() * 1000);

                                    //如果上影线大于下影线
                                    if (upperShadow.compareTo(lowerShadow) > 0) {

                                        //长影线蜡烛是否反向
                                        if (entityReverse && currentCandleMessage.getTrend() > 0) {
                                            continue;
                                        }

                                        //判断是否符合主影线长度
                                        if (upperShadow.compareTo(conformMainShadow) < 0) {
                                            continue;
                                        }

                                        //判断是否符合副影线长度
                                        if (lowerShadow.compareTo(conformSecondShadow) > 0) {
                                            continue;
                                        }

                                        //判断是否符合实体长度
                                        if (entity.compareTo(conformEntityShadow) > 0) {
                                            continue;
                                        }

                                        //判断前置蜡烛是否连续涨
                                        for (int j = 1; j <= preSize; j++) {
                                            CandleMessage preCandleMessage = CandleMessage.getCandleMessage(candles.get(i - j));
                                            if (preCandleMessage.getTrend() != 1) {
                                                continue first;
                                            }
                                        }

                                        //判断盈亏
                                        //获取目标跌涨方向
                                        Integer currentTrend = -1;
                                        //获取结果方向
                                        Integer resultTrend = candles.get(i + payMin).getClose().subtract(currentCandle.getClose()).compareTo(new BigDecimal(0));
                                        if (resultTrend == 0) {
                                            continue;
                                        }

                                        if (resultTrend.equals(currentTrend)) {
                                            winTimeList.add(payTimeString);
                                        } else {
                                            lostTimeList.add(payTimeString);
                                        }

                                    }
                                    //如果上影线小于下影线
                                    else {

                                        //长影线蜡烛是否反向
                                        if (entityReverse && currentCandleMessage.getTrend() < 0) {
                                            continue;
                                        }

                                        //判断是否符合主影线长度
                                        if (lowerShadow.compareTo(conformMainShadow) < 0) {
                                            continue;
                                        }

                                        //判断是否符合副影线长度
                                        if (upperShadow.compareTo(conformSecondShadow) > 0) {
                                            continue;
                                        }

                                        //判断是否符合实体长度
                                        if (entity.compareTo(conformEntityShadow) > 0) {
                                            continue;
                                        }

                                        //判断前置蜡烛是否连续涨
                                        for (int j = 1; j <= preSize; j++) {
                                            CandleMessage preCandleMessage = CandleMessage.getCandleMessage(candles.get(i - j));
                                            if (preCandleMessage.getTrend() != -1) {
                                                continue first;
                                            }
                                        }

                                        //判断盈亏
                                        //获取目标跌涨方向
                                        Integer currentTrend = 1;
                                        //获取结果方向
                                        Integer resultTrend = candles.get(i + payMin).getClose().subtract(currentCandle.getClose()).compareTo(new BigDecimal(0));
                                        if (resultTrend == 0) {
                                            continue;
                                        }

                                        if (resultTrend.equals(currentTrend)) {
                                            winTimeList.add(payTimeString);
                                        } else {
                                            lostTimeList.add(payTimeString);
                                        }

                                    }
                                }

                                System.out.println("winNum = " + winTimeList.size());
                                System.out.println("lostNum = " + lostTimeList.size());
                                System.out.println("winTimeList = " + winTimeList);
                                System.out.println("lostTimeList = " + lostTimeList);

                            }
                        }
                    }
                }

            }
        }
    }

}
