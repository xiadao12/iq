package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.filter.StrategyBaseFilter;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DateUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 策略工具类
 *
 * create date : 2019/1/19
 */
public class StrategyUtil {
    /**
     * 判断是否符合标准。比如存在两个涨
     *
     * @param preList
     * @return
     */
    public static Boolean judgeTimes(List<Boolean> preList) {
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
    public static Boolean judgeTimes(List<Boolean> preList, Integer num) {

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
    public static Boolean judgeRise(CandlesResponse.Candle candle) {
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
    public static Boolean judgeActivetime(List<StrategyBaseFilter.ActiveTime> activeTimes, CandlesResponse.Candle candle) {

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
        for (StrategyBaseFilter.ActiveTime activeTime : activeTimes) {
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
