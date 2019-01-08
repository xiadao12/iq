package com.zcy.iqconnect.model;

/**
 * 身份验证请求
 *
 * create date : 2019/1/7
 */
//{"name":"ssid","request_id":"1546751287_1049553889","msg":"a87f0428c732dbb972c114dfd5ed6981"}
public class SsidRequest {
    //名称
    private final String name = "ssid";
    //请求id
    private String request_id;
    //消息
    private String msg;

    /**
     * 默认构造方法
     */
    public SsidRequest() {
    }

    public SsidRequest(String request_id, String msg) {
        setRequest_id(request_id);
        setMsg(msg);
    }

    public String getName() {
        return name;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
