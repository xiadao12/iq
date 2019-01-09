package com.zcy.iqoperate.feign;

import com.zcy.iqoperate.core.BtResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 2019/1/9
 */
@FeignClient(value="iqconnect")
public interface WebsocketFeign {
    /**
     * 向websocket发送请求消息
     * @param message
     */
    @GetMapping("/iq/connect/websocket/sendMessage")
    BtResult<?> sendMessage(String message);
}
