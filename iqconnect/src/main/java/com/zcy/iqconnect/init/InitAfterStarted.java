package com.zcy.iqconnect.init;

import com.zcy.iqconnect.IqconnectApplication;
import com.zcy.iqconnect.core.IqWebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 在系统启动后运行
 * <p>
 * create date : 2019/1/9
 */
@Component
public class InitAfterStarted implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(IqconnectApplication.class);

    @Autowired
    IqWebSocketClient iqWebSocketClient;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.info("服务启动完成");

        //启动时候就连接websocket
        logger.info("开始连接websocket");
        //连接socket
        iqWebSocketClient.connect();
    }
}
