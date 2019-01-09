package com.zcy.iqoperate.model.response;

/**
 * 实时获取服务器时间
 *{"name":"timeSync","msg":1546861675261}
 *
 * create date : 2019/1/7
 */
public class TimeSyncResponse {
    private String name = "timeSync";
    private Long msg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMsg() {
        return msg;
    }

    public void setMsg(Long msg) {
        this.msg = msg;
    }
}
