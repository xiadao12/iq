package com.zcy.iqconnect;

import com.zcy.iqconnect.core.IqWebSocketClient;
import com.zcy.iqconnect.service.IqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class IqconnectApplication {

    private static Logger logger = LoggerFactory.getLogger(IqconnectApplication.class);

    @Autowired
    private static IqService iqService;

    @Autowired
    private static IqWebSocketClient iqWebSocketClient;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(IqconnectApplication.class, args);

        //启动时候就连接websocket
        connect();
    }

    private static void connect() throws Exception {

        logger.info("开始连接websocket");
        //连接socket
        iqWebSocketClient.connect();
    }

}

