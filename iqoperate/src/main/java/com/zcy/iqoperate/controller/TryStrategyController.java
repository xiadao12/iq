package com.zcy.iqoperate.controller;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.filter.BaseStrategyFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create date : 2019/1/11
 */
@RestController
@RequestMapping("/iq/operate/tryStrategy")
public class TryStrategyController {

    @PostMapping("/execute")
    public BtResult execute(BaseStrategyFilter baseStrategyFilter){

        return null;
    }

}
