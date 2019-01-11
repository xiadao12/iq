package com.zcy.iqoperate.service.impl;

import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.service.TryStrategyService;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.ListUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * create date : 2019/1/7
 */
@Service
public class TryStrategyServiceImpl implements TryStrategyService {

    /**
     * 策略
     *
     * @param candles
     */
    @Override
    public void strategy(List<CandlesResponse.Candle> candles) {

        //判断传参
        if (candles == null || candles.size() <= 0) {
            return;
        }

        strategy1(candles);
    }

    public void strategy1(List<CandlesResponse.Candle> candles) {

        //记录输赢的次数
        Integer winNum = 0;
        Integer lostNum = 0;

        //记录赢输的时间点
        List winTimeList = new ArrayList<>();
        List lostTimeList = new ArrayList<>();

        //前面节点个数
        Integer preSize = 4;
        //前置节点涨跌集合
        List<Boolean> preList = new ArrayList<>();
        for(int i=0;i<preSize;i++){
            preList.add(null);
        }

        for (int i = 0; i < candles.size() - 1; i++) {
            CandlesResponse.Candle candle = candles.get(i);

            CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);

            Boolean rise = candleMessage.getRise();

            //判断是否前三次是连续三次涨或跌
            Boolean judgeResult = judgeTimes(preList);

            if (judgeResult != null && rise != null) {
                if ((judgeResult && !rise) || (!judgeResult && rise)) {
                    winNum++;
                    winTimeList.add(DateUtil.timeStampToDateString(candle.getFrom()*1000));
                } else {
                    lostNum++;
                    lostTimeList.add(DateUtil.timeStampToDateString(candle.getFrom()*1000));
                }

                //符合标准后跳过
                i=i+preSize;
            }

            //向左偏移一位
            ListUtil.offsetLeft(preList, rise);
        }

        System.out.println("winNum = " + winNum);
        System.out.println("lostNum = " + lostNum);
        System.out.println("winTimeList = " + winTimeList);
        System.out.println("lostTimeList = " + lostTimeList);
    }

    /**
     * 策略一
     * 根据影线判断
     */
    public void strategy2(List<CandlesResponse.Candle> candles) {

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

            Double open = candle.getOpen();
            Double close = candle.getClose();
            Double min = candle.getMin();
            Double max = candle.getMax();

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
            Double lowerShadow = 0D;
            //上影线长
            Double upperShadow = 0D;
            //实体长度
            // Double entity = Math.abs(open - close);
            Double entity = Math.abs(DoubleUtil.sub(open, close));

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

    /**
     * 判断是否符合标准。比如存在两个涨
     *
     * @param preList
     * @return
     */
    Boolean judgeTimes(List<Boolean> preList) {
        Integer num = preList.size();
        return judgeTimes(preList, num);
    }

    /**
     * 判断是否符合标准。比如存在两个涨
     *
     * @param preList
     * @param num
     * @return
     */
    Boolean judgeTimes(List<Boolean> preList, Integer num) {

        if(preList == null || preList.size() <=0){
            return null;
        }

        Integer riseNum = 0;
        Integer fallNum = 0;

        for (Boolean pre : preList) {
            if (pre == null) {
                return null;
            }

            if (pre) {
                riseNum++;
            } else {
                fallNum++;
            }
        }

        if (riseNum >= num) {
            return true;
        }

        if (fallNum >= num) {
            return false;
        }

        return null;
    }

    /**
     * 判断涨跌
     *
     * @param candle
     * @return
     */
    Boolean judgeRise(CandlesResponse.Candle candle) {
        Double open = candle.getOpen();
        Double close = candle.getClose();

        Boolean currentProcess = null;
        //判断第四次是涨或跌
        if (open == close) {
            currentProcess = null;
        } else if (close > open) {
            currentProcess = true;
        } else {
            currentProcess = false;
        }

        return currentProcess;
    }
}
