package com.zcy.iqconnect.model;

/**
 * IqOption登录返回对象
 * {
 * "data": {
 * "ssid": "e94c06fbeff03c7e079a837c70780beb"
 * }
 * }
 */
public class IqOptionLoginResponse {

    //内部类
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    /**
     * 静态内部类
     */
    public static class Data {
        //ssid
        private String ssid;

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }
    }

}
