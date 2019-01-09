package com.zcy.iqoperate.core;

/**
 * create date : 2019/1/6
 */
public class BtResult<T> {

    //反馈码
    private Integer code;
    //返回数据
    private T data;
    //消息
    private String message;

    public static BtResult OK() {
        BtResult btResult = new BtResult();
        btResult.code = 200;
        btResult.message = "执行成功";
        return btResult;
    }

    public static BtResult OK(String message) {
        BtResult btResult = new BtResult();
        btResult.code = 200;
        btResult.message = message;
        return btResult;
    }

    public static BtResult OK(String message, Object data) {
        BtResult btResult = new BtResult();
        btResult.code = 200;
        btResult.data = data;
        btResult.message = message;
        return btResult;
    }


    public static BtResult ERROR(String message) {
        BtResult btResult = new BtResult();
        btResult.code = 500;
        btResult.message = message;
        return btResult;
    }

    public static BtResult ERROR(String message, Object data) {
        BtResult btResult = new BtResult();
        btResult.code = 500;
        btResult.data = data;
        btResult.message = message;
        return btResult;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
