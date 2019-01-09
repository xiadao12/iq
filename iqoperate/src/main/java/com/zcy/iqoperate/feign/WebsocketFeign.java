package com.zcy.iqoperate.feign;

import com.zcy.iqoperate.core.BtResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 2019/1/9
 */
@FeignClient("iqconnect")
public interface WebsocketFeign {
    /**
     * 向websocket发送请求消息
     *
     * @param message
     */
    @PostMapping("/iq/connect/websocket/sendMessage")
    BtResult<?> sendMessage(@RequestParam("message") String message);
}
