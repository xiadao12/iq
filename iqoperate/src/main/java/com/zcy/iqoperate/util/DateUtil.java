package com.zcy.iqoperate.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * create date : 2019/1/10
 */
public class DateUtil {

    /**
     * 时间戳转为日期字符串
     * @param time
     * @return
     */
    public static String timeStampToDateString(Long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }

}
