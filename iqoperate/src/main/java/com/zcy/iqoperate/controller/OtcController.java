package com.zcy.iqoperate.controller;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.filter.CandlesFilter;
import com.zcy.iqoperate.service.OtcCandlesService;
import com.zcy.iqoperate.strategy.StrategyShadowLineEnoughOTC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create date : 2019/2/21
 */
@RestController
@RequestMapping("/iq/operate/otc")
public class OtcController {

    @Autowired
    OtcCandlesService otcCandlesService;

/*    @Autowired
    StrategyContinuousOverOTCFromFile strategyContinuousOverOTCFromFile;*/

/*    @Autowired
    StrategyLongContinuousOverOTCFromFile strategyLongContinuousOverOTCFromFile;*/
/*
    @Autowired
    StrategyContinuousLongOTCFromFile strategyContinuousLongOTCFromFile;*/

    @Autowired
    StrategyShadowLineEnoughOTC strategyShadowLineEnoughOTC;


    /**
     * 获取otc蜡烛图
     *
     * @param candlesFilter
     * @return
     * @throws Exception
     */
    @PostMapping("/getOtcCandles")
    public BtResult getOtcCandles(@RequestBody CandlesFilter candlesFilter) throws Exception {
        return otcCandlesService.sendGetCandles(candlesFilter);
    }

    /**
     * 连续策略
     *
     * @param object
     * @return
     */
    @PostMapping("/otcStrategy")
    public BtResult otcStrategy(@RequestBody Object object) {
        strategyShadowLineEnoughOTC.execute(object);
        return BtResult.OK();
    }

}
