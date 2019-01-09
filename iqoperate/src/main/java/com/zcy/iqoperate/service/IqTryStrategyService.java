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
public interface IqTryStrategyService {

    /**
     * 策略一
     * 根据影线判断
     */
    void strategy1(List<CandlesResponse.Candle> candles);
}
