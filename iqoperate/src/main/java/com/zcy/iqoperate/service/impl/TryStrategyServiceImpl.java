package com.zcy.iqoperate.service.impl;

import com.zcy.iqoperate.filter.BaseStrategyFilter;
import com.zcy.iqoperate.filter.LongStrategyFilter;
import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.request.GetCandlesRequest;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.service.TryStrategyService;
import com.zcy.iqoperate.service.WebsocketService;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.JsonUtil;
import com.zcy.iqoperate.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * create date : 2019/1/7
 */
@Service
public class TryStrategyServiceImpl implements TryStrategyService {

    //条件
    private Object strategyFilterObject;

    @Autowired
    WebsocketService websocketService;

    /**
     * 执行
     *
     * @param strategyFilterObject
     */
    @Override
    public void execute(Object strategyFilterObject) {
        this.strategyFilterObject = strategyFilterObject;

        BaseStrategyFilter baseStrategyFilter = JsonUtil.convertValue(strategyFilterObject, BaseStrategyFilter.class);

        //外汇id
        Integer activeId = baseStrategyFilter.getActiveId();

        //查询的开始日期 2019-01-01 22:00:00
        String startDateString = baseStrategyFilter.getStartDateString();

        //查询的开始日期 2019-01-01 22:00:00
        String endDateString = baseStrategyFilter.getEndDateString();

        //活跃时间
        List<BaseStrategyFilter.ActiveTime> activeTimes = baseStrategyFilter.getActiveTimes();

        //Long currentId = IqUtil.getCurrentId();
        Long currentId = 447397L;

        GetCandlesRequest getCandlesRequest = new GetCandlesRequest(
                "112_233",
                activeId,
                60,
                currentId - 10,
                currentId);

        websocketService.sendMessage(getCandlesRequest);
    }

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

        strategyLong(candles);
    }

    /**
     * 策略：长蜡烛
     *
     * @param candles
     */
    public void strategyLong(List<CandlesResponse.Candle> candles) {

        LongStrategyFilter longStrategyFilter = JsonUtil.convertValue(strategyFilterObject, LongStrategyFilter.class);

        //因子最小值
        BigDecimal startFactor = longStrategyFilter.getStartFactor();

        //因子最大值
        BigDecimal endFactor = longStrategyFilter.getEndFactor();

        //因子从小到大间距
        BigDecimal factorDistance = longStrategyFilter.getFactorDistance();

        //跳过个数再获取结果
        Integer skipSize = longStrategyFilter.getSkipSize();

        //遍历起止因子
        for (BigDecimal factor = startFactor; factor.compareTo(endFactor) < 0; factor = factor.add(factorDistance)) {

            System.out.println("计算因子为：" + factor);

            //记录输赢的次数
            Integer winNum = 0;
            Integer lostNum = 0;

            //记录赢输的时间点
            List winTimeList = new ArrayList<>();
            List lostTimeList = new ArrayList<>();

            for (int i = 0; i < candles.size() - 1 - skipSize; i++) {
                CandlesResponse.Candle candle = candles.get(i);

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
                }

            }

            System.out.println("winNum = " + winNum);
            System.out.println("lostNum = " + lostNum);
            System.out.println("winTimeList = " + winTimeList);
            System.out.println("lostTimeList = " + lostTimeList);
        }
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
        for (int i = 0; i < preSize; i++) {
            preList.add(null);
        }

        for (int i = 0; i < candles.size() - 1; i++) {
            CandlesResponse.Candle candle = candles.get(i);

            CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);

            //获取蜡烛涨跌
            Integer trend = candleMessage.getTrend();

            //判断是否前三次是连续三次涨或跌
            Boolean judgeResult = judgeTimes(preList);

            if (judgeResult != null) {
                if ((judgeResult && trend < 0) || (!judgeResult && trend > 0)) {
                    winNum++;
                    winTimeList.add(DateUtil.timeStampToDateString(candle.getFrom() * 1000));
                } else {
                    lostNum++;
                    lostTimeList.add(DateUtil.timeStampToDateString(candle.getFrom() * 1000));
                }

                //符合标准后跳过
                i = i + preSize;
            }

            //向左偏移一位
            ListUtil.offsetLeft(preList, trend);
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

        if (preList == null || preList.size() <= 0) {
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
        BigDecimal open = candle.getOpen();
        BigDecimal close = candle.getClose();

        Boolean currentProcess = null;
        //判断第四次是涨或跌
        if (open.compareTo(close) == 0) {
            currentProcess = null;
        } else if (close.compareTo(open) > 0) {
            currentProcess = true;
        } else {
            currentProcess = false;
        }

        return currentProcess;
    }
}
