package com.zcy.iqoperate.service;

import com.zcy.iqoperate.core.BtResult;
import org.springframework.stereotype.Service;

/**
 * 2019/1/9
 */
@Service
public interface WebsocketService {

    /**
     * 向websocket发送消息
     * @return
     */
    BtResult sendMessage(Object message);
}
