package com.zcy.iqoperate.core;

import com.zcy.iqoperate.service.impl.BaseServiceImpl;

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

    /**
     * 获取当前时间的id，对应下面的from_id和to_id
     * //{"name":"sendMessage","request_id":"104","msg":{"name":"get-candles","version":"2.0","body":{"active_id":4,"size":60,"from_id":440428,"to_id":440453,"only_closed":true}}}
     * @return
     * 计算方法：（当前系统秒数 - 1520413740） / 60
     */
    public static Long getCurrentId(){

        final Long factorNum = 1520413740L;

        //获取服务器毫秒数
        Long currentTimeSync = BaseServiceImpl.timeSync;

        while (currentTimeSync == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return (currentTimeSync/1000-1520413740) / 60;
    }
}
