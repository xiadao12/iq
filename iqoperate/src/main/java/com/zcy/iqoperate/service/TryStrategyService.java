package com.zcy.iqoperate.service;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.model.response.CandlesResponse;

/**
 * 尝试策略
 * <p>
 * create date : 2019/1/7
 */
public interface TryStrategyService {

    /**
     * 执行
     *
     * @param strategyFilterObject
     */
    BtResult execute(Object strategyFilterObject) throws InterruptedException;

    /**
     * 根据返回的蜡烛图尝试策略
     *
     * @param candlesResponse
     */
    void strategy(CandlesResponse candlesResponse);
}
