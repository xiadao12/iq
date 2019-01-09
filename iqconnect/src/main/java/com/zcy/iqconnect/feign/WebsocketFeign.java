package com.zcy.iqconnect.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 2019/1/9
 */
@FeignClient("iqoperate")
public interface WebsocketFeign {

    /**
     * 将websocket接收到的信息都返回给operate处理
     * @param message
     */
    @GetMapping("/iq/operate/websocket/receiveMessage")
    void receiveMessage(String message);
}