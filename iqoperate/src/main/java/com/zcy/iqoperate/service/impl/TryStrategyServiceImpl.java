package com.zcy.iqoperate.service.impl;

import com.zcy.iqoperate.core.BtResult;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create date : 2019/1/7
 */
@Service
public class TryStrategyServiceImpl implements TryStrategyService {

    //条件
    private Object strategyFilterObject;

    @Autowired
    private WebsocketService websocketService;

    //获取candles循环次数 = candleDays * 2
    private Integer candlesCycleSize;

    //请求蜡烛图的id集合
    private List<String> candlesRequestIds = new ArrayList<>();

    //request_id与蜡烛集合的map
    private Map<String, List<CandlesResponse.Candle>> candlesMap = new HashMap<>();

    /**
     * 执行
     *
     * @param strategyFilterObject
     */
    @Override
    public BtResult execute(Object strategyFilterObject) {

        //清空请求蜡烛图的id集合
        candlesRequestIds.clear();

        //清空request_id与蜡烛集合的map
        candlesMap.clear();

        //每个蜡烛图秒数
        Integer candleSize = 60;

        //初始化传入参数
        this.strategyFilterObject = strategyFilterObject;

        LongStrategyFilter longStrategyFilter = JsonUtil.convertValue(strategyFilterObject, LongStrategyFilter.class);
        if (longStrategyFilter == null) {
            return BtResult.ERROR("解析参数失败");
        }

        //外汇id
        Integer activeId = longStrategyFilter.getActiveId();
        if (activeId == null) {
            return BtResult.ERROR("未传activeId");
        }

        //初始化查询天数
        Integer candleDays = longStrategyFilter.getCandleDays();
        if (candleDays == null) {
            return BtResult.ERROR("未传candleDays");
        }

        //Long currentId = IqUtil.getCurrentId();
        //Long currentId = 447397L;
        //Long currentId = 226255L;
        Long currentId = longStrategyFilter.getCurrentId();

        // 12*60
        Integer halfdayIdSize = 720;

        //id跳过个数
        Integer skipIdSize = halfdayIdSize;

        //获取candles循环次数 = candleDays * 2
        candlesCycleSize = candleDays * 2;

        //计算出第一个请求的id
        currentId = currentId - candlesCycleSize * halfdayIdSize;

        for (int i = 0; i < candlesCycleSize; i++) {

            String request_id = String.valueOf(System.currentTimeMillis() + i);
            candlesRequestIds.add(request_id);

            GetCandlesRequest getCandlesRequest = new GetCandlesRequest(
                    request_id,
                    activeId,
                    candleSize,
                    currentId - skipIdSize,
                    currentId);

            websocketService.sendMessage(getCandlesRequest);

            currentId = currentId + skipIdSize;
        }

        return BtResult.OK();
    }

    /**
     * 策略
     *
     * @param candlesResponse
     */
    @Override
    public void strategy(CandlesResponse candlesResponse) {

        //判断传参
        if (candlesResponse == null) {
            return;
        }

        CandlesResponse.Msg msg = candlesResponse.getMsg();
        if (msg == null) {
            return;
        }

        List<CandlesResponse.Candle> candles = msg.getCandles();
        if (candles == null || candles.size() <= 0) {
            return;
        }

        //candles去除第一个，解决造成重复的问题
        candles.remove(0);

        candlesMap.put(candlesResponse.getRequest_id(), candles);

        //查询天数自减，
        candlesCycleSize--;

        //直到0时说明所有蜡烛信息收集完毕
        if (candlesCycleSize.equals(0)) {

            //汇总分批查询到的蜡烛图
            List<CandlesResponse.Candle> allCandles = new ArrayList<>();
            for (String requestId : candlesRequestIds) {
                allCandles.addAll(candlesMap.get(requestId));
            }

            System.out.println(JsonUtil.ObjectToJsonString(allCandles));

            //strategyContinuous(allCandles);
        }
    }

    /**
     * 策略：长蜡烛
     *
     * @param candles
     */
    public void strategyLong(List<CandlesResponse.Candle> candles) {

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
                    if (!judgeActivetime(activeTimes, candle)) {
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
     * 测试最多连续个数
     */
    public void strategyContinuous(List<CandlesResponse.Candle> candles) {

        //上一个涨与跌
        Integer preTrend = 1;

        //记录最多连续蜡烛图个数
        Integer maxSum = 0;

        //记录最多连续蜡烛图的To
        Long maxSumTo = 0L;

        Map<Integer, List<String>> sumListMap = new HashMap<>();
        Map<Integer, Integer> sumMap = new HashMap<>();

        List<CandlesResponse.Candle> candlesResult = new ArrayList<>();

        for (CandlesResponse.Candle candle : candles) {
            CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);
            //当前涨跌
            Integer currentTrend = candleMessage.getTrend();

            //如果和上一个一样，获取是平的
            if (currentTrend.equals(0) || currentTrend.equals(preTrend)) {
                candlesResult.add(candle);

                if (candlesResult.size() > maxSum) {
                    maxSumTo = candle.getTo();
                    maxSum = candlesResult.size();
                }
            } else {

                if (sumMap.get(candlesResult.size()) == null) {
                    sumMap.put(candlesResult.size(), 1);
                } else {
                    sumMap.put(candlesResult.size(), sumMap.get(candlesResult.size()) + 1);
                }

                if (sumListMap.get(candlesResult.size()) == null) {
                    List<String> ss = new ArrayList<>();
                    ss.add(DateUtil.timeStampToDateString(candle.getTo() * 1000));
                    sumListMap.put(candlesResult.size(), ss);
                } else {
                    List<String> ss = sumListMap.get(candlesResult.size());
                    ss.add(DateUtil.timeStampToDateString(candle.getTo() * 1000));
                    sumListMap.put(candlesResult.size(), ss);
                }

                candlesResult.clear();
            }

            preTrend = currentTrend;
        }

        System.out.println("maxSum = " + maxSum);
        System.out.println("maxSumTo = " + DateUtil.timeStampToDateString(maxSumTo * 1000));
        System.out.println("maxSumTo = " + JsonUtil.ObjectToJsonString(sumMap));
        System.out.println("maxSumTo = " + JsonUtil.ObjectToJsonString(sumListMap));
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

    //判断是否是在活跃时间内
    Boolean judgeActivetime(List<BaseStrategyFilter.ActiveTime> activeTimes, CandlesResponse.Candle candle) {

        //如果没有活跃时间集合，则默认返回true
        if (activeTimes == null || activeTimes.size() <= 0) {
            return true;
        }

        if (candle == null) {
            return false;
        }

        //获取开始时间
        Long from = candle.getFrom();
        if (from == null) {
            return false;
        }

        //获取开始时间日期字符串
        String fromString = DateUtil.timeStampToDateString(from * 1000);
        if (fromString == null) {
            return false;
        }

        //只获取时分秒
        String hmsString = fromString.substring(fromString.indexOf(" ") + 1, fromString.length());
        if (hmsString == null) {
            return false;
        }

        //遍历活跃时间集合
        for (BaseStrategyFilter.ActiveTime activeTime : activeTimes) {
            String startTimeString = activeTime.getActiveStartTimeString();
            if (startTimeString == null) {
                continue;
            }

            String endTimeString = activeTime.getActiveEndTimeString();
            if (endTimeString == null) {
                continue;
            }

            if (hmsString.compareTo(startTimeString) > 0 && hmsString.compareTo(endTimeString) < 0) {
                return true;
            }
        }

        return false;
    }
}
