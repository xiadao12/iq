package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.model.response.CandlesResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 影线
 *
 * create date : 2019/1/19
 */
@Component
public class StrategyShadowLine {

    public void execute(List<CandlesResponse.Candle> candles) {

/*        //记录输赢的次数
        Integer winNum = 0;
        Integer lostNum = 0;

        //记录赢输的时间点
        List<Long> winTimeList = new ArrayList<>();
        List<Long> lostTimeList = new ArrayList<>();

        //记录前三根蜡烛图是涨还是跌
        Boolean pre1 = null;
        Boolean pre2 = null;
        //Boolean pre3 = null;

        List<Boolean> preList = new ArrayList<>();
        preList.add(pre1);
        preList.add(pre2);
        //preList.add(pre3);

        for (int i = 0; i < candles.size() - 1; i++) {
            CandlesResponse.Candle candle = candles.get(i);

            BigDecimal open = candle.getOpen();
            BigDecimal close = candle.getClose();
            BigDecimal min = candle.getMin();
            BigDecimal max = candle.getMax();

            Boolean currentProcess = null;
            //判断第四次是涨或跌
            if (open == close) {
                currentProcess = null;
            } else if (close > open) {
                currentProcess = true;
            } else {
                currentProcess = false;
            }

            //下影线长
            BigDecimal lowerShadow = 0D;
            //上影线长
            BigDecimal upperShadow = 0D;
            //实体长度
            // BigDecimal entity = Math.abs(open - close);
            BigDecimal entity = Math.abs(DoubleUtil.sub(open, close));

            //如果是上涨
            if (currentProcess == null || currentProcess) {
*//*                lowerShadow = open - min;
                upperShadow = max - close;*//*
                lowerShadow = DoubleUtil.sub(open, min);
                upperShadow = DoubleUtil.sub(max, close);


            } else {
*//*                lowerShadow = close - min;
                upperShadow = max - open;*//*
                lowerShadow = DoubleUtil.sub(close, min);
                upperShadow = DoubleUtil.sub(max, open);

            }

            //判断是否前三次是连续三次涨或跌
            Boolean judgeResult = judgeTimes(preList, 2);

            if (judgeResult != null) {
                //获取下一个蜡烛图
                CandlesResponse.Candle nextCandle = candles.get(i + 1);

                //如果前三次都是涨
                if (judgeResult) {
                    //如果上影线长
                    if (upperShadow > 2 * entity && upperShadow > 2 * lowerShadow) {
                        //预测下一个蜡烛图跌
                        if (judgeRise(nextCandle) != null && judgeRise(nextCandle) == false) {
                            winNum++;
                            winTimeList.add(candle.getFrom());
                        } else {
                            lostNum++;
                            lostTimeList.add(candle.getFrom());
                        }
                    }
                } else {
                    //如果前三次都是跌
                    //如果下影线长
                    if (lowerShadow > 2 * entity && lowerShadow > 2 * upperShadow) {
                        //预测下一个蜡烛图涨
                        if (judgeRise(nextCandle) != null && judgeRise(nextCandle) == true) {
                            winNum++;
                            winTimeList.add(candle.getFrom());
                        } else {
                            lostNum++;
                            lostTimeList.add(candle.getFrom());
                        }
                    }
                }
            }

            pre1 = pre2;
            //pre2 = pre3;
            pre2 = currentProcess;

            preList = new ArrayList<>();
            preList.add(pre1);
            preList.add(pre2);
            //preList.add(pre3);
        }

        System.out.println("winNum = " + winNum);
        System.out.println("lostNum = " + lostNum);
        System.out.println("winTimeList = " + winTimeList);
        System.out.println("lostTimeList = " + lostTimeList);*/
    }

}
