package com.zcy.iqoperate.service;

import com.zcy.iqoperate.core.BtResult;

/**
 * 2019/1/9
 */
public interface WebsocketService {

    /**
     * 向websocket发送消息
     *
     * @return
     */
    BtResult sendMessage(Object message);
}
