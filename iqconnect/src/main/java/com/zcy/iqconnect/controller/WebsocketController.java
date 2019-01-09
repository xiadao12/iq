package com.zcy.iqconnect.controller;

import com.zcy.iqconnect.core.BtResult;
import com.zcy.iqconnect.core.IqWebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2019/1/9
 */
@RestController
@RequestMapping("/iq/connect/websocket")
public class WebsocketController {
    @Autowired
    IqWebSocketClient iqWebSocketClient;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/sendMessage")
    public BtResult<?> sendMessage(String message){
        logger.info("开始发送请求：" + message);
        iqWebSocketClient.send(message);
        return BtResult.OK();
    }
}
