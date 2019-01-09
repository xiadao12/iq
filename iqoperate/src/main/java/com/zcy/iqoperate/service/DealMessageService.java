package com.zcy.iqoperate.service;

/**
 * 处理消息
 * create date : 2019/1/6
 */
public interface DealMessageService {

    /**
     * 处理接收到的信息
     * @param message
     */
    void dealMessage(String message);
}
