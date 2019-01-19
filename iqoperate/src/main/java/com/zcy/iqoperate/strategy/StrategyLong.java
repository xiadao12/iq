package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.filter.BaseStrategyFilter;
import com.zcy.iqoperate.filter.LongStrategyFilter;
import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.JsonUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 策略：长蜡烛
 * create date : 2019/1/19
 */
public class StrategyLong {

    public void execute(List<CandlesResponse.Candle> candles, Object strategyFilterObject) {
        LongStrategyFilter longStrategyFilter = JsonUtil.convertValue(strategyFilterObject, LongStrategyFilter.class);

        //活跃时间
        //List<BaseStrategyFilter.ActiveTime> activeTimes = longStrategyFilter.getActiveTimes();

        //因子最小值
        BigDecimal startFactor = longStrategyFilter.getStartFactor();

        //因子最大值
        BigDecimal endFactor = longStrategyFilter.getEndFactor();

        //因子从小到大间距
        BigDecimal factorDistance = longStrategyFilter.getFactorDistance();

        //跳过个数再获取结果
        Integer skipSize = longStrategyFilter.getSkipSize();

        //遍历时间,一个小时为间隔
        //活跃时间
        List<List<BaseStrategyFilter.ActiveTime>> allActiveTime = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            BaseStrategyFilter.ActiveTime activeTime = new BaseStrategyFilter.ActiveTime();
            activeTime.setActiveStartTimeString("0" + i + ":00:00");
            activeTime.setActiveEndTimeString("0" + i + ":59:59");

            List<BaseStrategyFilter.ActiveTime> activeTimes = new ArrayList<>();
            activeTimes.add(activeTime);

            allActiveTime.add(activeTimes);
        }
        for (int i = 10; i <= 23; i++) {
            BaseStrategyFilter.ActiveTime activeTime = new BaseStrategyFilter.ActiveTime();
            activeTime.setActiveStartTimeString(i + ":00:00");
            activeTime.setActiveEndTimeString(i + ":59:59");

            List<BaseStrategyFilter.ActiveTime> activeTimes = new ArrayList<>();
            activeTimes.add(activeTime);

            allActiveTime.add(activeTimes);
        }

/*        BaseStrategyFilter.ActiveTime activeTime = new BaseStrategyFilter.ActiveTime();
        activeTime.setActiveStartTimeString("22:00:00");
        activeTime.setActiveEndTimeString("22:59:59");
        List<BaseStrategyFilter.ActiveTime> activeTimesTemp = new ArrayList<>();
        activeTimesTemp.add(activeTime);
        allActiveTime.add(activeTimesTemp);*/

        //遍历起止因子
        for (BigDecimal factor = startFactor; factor.compareTo(endFactor) < 0; factor = factor.add(factorDistance)) {

            //遍历活跃时间
            for (List<BaseStrategyFilter.ActiveTime> activeTimes : allActiveTime) {

                System.out.println();
                System.out.println("计算因子为：" + factor);
                System.out.println("活跃时间为：" + activeTimes.get(0).getActiveStartTimeString() + " " + activeTimes.get(0).getActiveEndTimeString());

                //记录输赢的次数
                Integer winNum = 0;
                Integer lostNum = 0;

                //记录赢输的时间点
                List winTimeList = new ArrayList<>();
                List lostTimeList = new ArrayList<>();

                for (int i = 0; i < candles.size() - 1 - skipSize; i++) {
                    CandlesResponse.Candle candle = candles.get(i);

                    //判断是否是在活跃时间内
                    if (!StrategyUtil.judgeActivetime(activeTimes, candle)) {
                        continue;
                    }

                    CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);

                    BigDecimal open = candle.getOpen();
                    BigDecimal entity = candleMessage.getEntity();

                    //获取开始时间
                    String fromString = DateUtil.timeStampToDateString(candle.getFrom() * 1000 + 60 * 1000);

                    //判断实体是否足够长
                    if (entity.compareTo(open.multiply(factor)) > 0) {
                        //获取结果蜡烛图（跳过个数为skip）
                        CandlesResponse.Candle resultCandle = candles.get(i + 1 + skipSize);

                        //获取蜡烛涨跌
                        Integer trend = candleMessage.getTrend();

                        //如果长蜡烛是涨
                        if (trend > 0) {
                            if (resultCandle.getClose().compareTo(candle.getClose()) < 0) {
                                winNum++;
                                winTimeList.add(fromString);
                            } else {
                                lostNum++;
                                lostTimeList.add(fromString);
                            }
                        } else {
                            if (resultCandle.getClose().compareTo(candle.getClose()) > 0) {
                                winNum++;
                                winTimeList.add(fromString);
                            } else {
                                lostNum++;
                                lostTimeList.add(fromString);
                            }
                        }

                        i = i + skipSize;
                    }

                }

                System.out.println("winNum = " + winNum);
                System.out.println("lostNum = " + lostNum);
                System.out.println("winTimeList = " + winTimeList);
                System.out.println("lostTimeList = " + lostTimeList);
            }
        }
    }
}
