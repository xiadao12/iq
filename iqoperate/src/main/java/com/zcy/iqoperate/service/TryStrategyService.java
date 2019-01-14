package com.zcy.iqoperate.service;

import com.zcy.iqoperate.model.response.CandlesResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 尝试策略
 *
 * create date : 2019/1/7
 */
@Service
public interface TryStrategyService {

    /**
     * 执行
     * @param strategyFilterObject
     */
    void execute(Object strategyFilterObject);

    /**
     * 根据返回的蜡烛图尝试策略
     * @param candles
     */
    void strategy(List<CandlesResponse.Candle> candles);
}
