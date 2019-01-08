package com.zcy.iqconnect.controller;

import com.zcy.iqconnect.core.BtResult;
import com.zcy.iqconnect.core.IqWebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create date : 2019/1/8
 */
@RestController
@RequestMapping("/iq/connect")
public class IqController {

    @Autowired
    IqWebSocketClient iqWebSocketClient;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/sendRequest")
    public BtResult<?> sendRequest(String requestMessage){
        logger.info("开始发送请求：" + requestMessage);
        iqWebSocketClient.send(requestMessage);
        return BtResult.OK();
    }
}
