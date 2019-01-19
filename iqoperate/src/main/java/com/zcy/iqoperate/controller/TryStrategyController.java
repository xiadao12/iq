package com.zcy.iqoperate.controller;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.service.TryStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 尝试策略
 * <p>
 * create date : 2019/1/11
 */
@RestController
@RequestMapping("/iq/operate/tryStrategy")
public class TryStrategyController {

    @Autowired
    TryStrategyService tryStrategyService;

    /**
     * 执行
     *
     * @param object
     * @return
     */
    @PostMapping("/execute")
    public BtResult execute(@RequestBody Object object) {
        tryStrategyService.execute(object);
        return null;
    }

}