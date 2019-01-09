package com.zcy.iqoperate.feign;

import com.zcy.iqoperate.core.BtResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 苏州中科蓝迪公司所有(c)2016-2021
 * @version 1.0.0
 * @brief ecs insight
 * @note 修订历史： 1、yangzhouchuan于2019/1/9设计并构建初始版本v1.0.0
 */
@FeignClient("iqconnect")
public interface WebsocketFeign {
    /**
     * 向websocket发送请求消息
     * @param message
     */
    @GetMapping("/iq/connect/websocket/sendMessage")
    BtResult<?> sendMessage(String message);
}
