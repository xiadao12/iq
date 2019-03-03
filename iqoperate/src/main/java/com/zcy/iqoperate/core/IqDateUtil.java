package com.zcy.iqoperate.core;

import com.zcy.iqoperate.util.DateUtil;

import java.util.Calendar;

/**
 * create date : 2019/3/3
 */
public class IqDateUtil {


    /**
     * 判断是否是otc时间，时间区间[周六 08:00:00 - 23:59:59，周日 全天，周一 00:00:00 - 04:00:00]
     *
     * @param time 毫秒
     * @param saturdayStartTime 周六开始时间 默认07:00:00
     * @param mondayEndTime 周一结束时间
     * @return
     */
    public static Boolean judgeOTCTime(Long time,String saturdayStartTime,String mondayEndTime) {

        //return true;

        //周六开始时间 默认06
        if(saturdayStartTime == null){
            saturdayStartTime = "08:00:00";
        }

        //周六开始时间 默认06
        if(mondayEndTime == null){
            mondayEndTime = "04:00:00";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        //周几
        Integer weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        //获取时间string字符串
        String dateString = DateUtil.timeStampToDateString(time);
        String[] dateStringArray = dateString.split(" ");
        //只获取时间的字符串
        String dateTimeString = dateStringArray[1];

        //如果是周六，判断时间是否大于08:00:00
        if (weekDay.equals(7) && dateTimeString.compareTo(saturdayStartTime) >= 0) {
            return true;
        }

        //如果是周日，则全天都是
        if (weekDay.equals(1)) {
            return true;
        }

        //如果是周一，则小于04:00:00
        if (weekDay.equals(2) && dateTimeString.compareTo(mondayEndTime) <= 0) {
            return true;
        }

        return false;
    }

}
