package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.model.response.CandlesResponse;

import java.util.List;

/**
 * create date : 2019/3/12
 */
public interface StrategyBase {
    void execute(List<CandlesResponse.Candle> candles, Object strategyFilterObject);
}
