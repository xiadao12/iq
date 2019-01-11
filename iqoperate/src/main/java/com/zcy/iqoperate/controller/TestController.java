package com.zcy.iqoperate.controller;

import com.zcy.iqoperate.core.IqUtil;
import com.zcy.iqoperate.model.request.GetCandlesRequest;
import com.zcy.iqoperate.service.WebsocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create date : 2019/1/9
 */
@RestController
@RequestMapping("/iq")
public class TestController {

    @Autowired
    WebsocketService websocketService;

    @GetMapping("/test")
    public void test(){
        //获取时间点id
/*        Long currentId = IqUtil.getCurrentId();

        String request_id = IqUtil.getRequestId();
        System.out.println(request_id);

        GetCandlesRequest getCandlesRequest = new GetCandlesRequest(
                request_id,
                2,
                60,
                currentId-600,
                currentId);*/

        Long currentId = IqUtil.getCurrentId();

        GetCandlesRequest getCandlesRequest = new GetCandlesRequest(
                "112_233",
                1,
                60,
                currentId-1000,
                currentId);

        websocketService.sendMessage(getCandlesRequest);
    }

}
