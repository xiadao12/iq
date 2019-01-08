package com.zcy.iqconnect.core;

import com.zcy.iqconnect.model.SsidRequest;
import com.zcy.iqconnect.service.IqService;
import com.zcy.iqconnect.util.IqUtil;
import com.zcy.iqconnect.util.JsonUtil;
import com.zcy.iqconnect.util.StringUtil;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 */
@Scope
@Component
public class IqWebSocketClient extends WebSocketClient {

    //日志
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IqService iqService;

    /**
     * 构造方法
     *
     * @throws URISyntaxException
     */
    public IqWebSocketClient() throws URISyntaxException {
        super(new URI(Constant.WEBSOCKET_URL));
    }

    /**
     * 监听websocket连接
     *
     * @param serverHandshake
     */
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("iqwebsocket已连接");

        logger.info("开始登录iq");

        //登录获取ssid
        String username = SecretContent.Email_QQ_851883560;
        String password = SecretContent.Common_Password;
        String ssid = null;
        try {
            ssid = iqService.login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtil.isEmpty(ssid)) {
            logger.info("登录失败，未获取到ssid");
            return;
        } else {
            logger.info("登录成功，获取到ssid为：" + ssid);
        }

        //获取请求id
        String ssid_request_id = IqUtil.getRequestId();

        //发送ssid请求
        //{"name":"ssid","request_id":"1546751287_1049553889","msg":"a87f0428c732dbb972c114dfd5ed6981"}
        SsidRequest ssidRequest = new SsidRequest(
                ssid_request_id,
                ssid
        );
        String ssidRequestJson = null;
        ssidRequestJson = JsonUtil.ObjectToJson(ssidRequest);
        this.send(ssidRequestJson);
    }

    /**
     * 监听websocket接收消息
     *
     * @param s
     */
    @Override
    public void onMessage(String s) {
        logger.info("iqwebsocket接收到消息：" + s);
    }

    /**
     * 监听websocket关闭
     *
     * @param i
     * @param s
     * @param b
     */
    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("iqwebsocket连接已关闭");
    }

    /**
     * 监听websocket出错
     *
     * @param e
     */
    @Override
    public void onError(Exception e) {
        logger.info("iqwebsocket出错error");
    }
}