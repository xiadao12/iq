package com.zcy.iqoperate.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * create date : 2019/1/10
 */
public class DateUtil {

    /**
     * 时间戳(毫秒)转为日期字符串
     *
     * @param time
     * @return
     */
    public static String timeStampToDateString(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }

    /**
     * 将日期字符串转为日期 2000-01-01 10:00:00
     *
     * @param dateString
     * @return
     */
    public static Date dateStringToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

}
