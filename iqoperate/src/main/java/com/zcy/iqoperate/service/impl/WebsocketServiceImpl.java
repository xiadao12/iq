package com.zcy.iqoperate.service.impl;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.feign.WebsocketFeign;
import com.zcy.iqoperate.service.WebsocketService;
import com.zcy.iqoperate.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class WebsocketServiceImpl implements WebsocketService {

    @Autowired
    WebsocketFeign websocketFeign;

    /**
     * 向websocket发送消息
     * @return
     */
    @Override
    public BtResult sendMessage(Object object) {

        String message = "";

        //判断参数是不是String类型的
        if(object instanceof String){
            message = object.toString();
        }else {
            message = JsonUtil.ObjectToJson(object);
        }
        return websocketFeign.sendMessage(message);
    }
}
