package com.zcy.iqoperate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2019/1/9设计并构建初始版本
 */
@RestController
@RequestMapping("/iq/operate/websocket")
public class WebsocketController {


    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理信息
     * @param message
     */
    @PostMapping("/receiveMessage")
    public void receiveMessage(String message){
        logger.info("接收到消息并开始处理：" + message);
    }
}
