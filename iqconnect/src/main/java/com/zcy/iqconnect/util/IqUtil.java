package com.zcy.iqconnect.util;

/**
 * create date : 2019/1/7
 */
public class IqUtil {
    /**
     * 获取请求的id
     *
     * @return
     */
    public static String getRequestId() {
        //{"name":"ssid","request_id":"1546751287_1049553889","msg":"a87f0428c732dbb972c114dfd5ed6981"}
        //获取系统时间戳(秒数)
        Long systemTime = System.currentTimeMillis() / 1000;
        //获取随机数据
        String random = String.valueOf(Math.random()).substring(2, 12);
        //ssid请求的id
        String ssid_request_id = systemTime + "_" + random;
        return ssid_request_id;
    }
}
