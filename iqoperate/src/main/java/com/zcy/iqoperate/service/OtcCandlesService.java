package com.zcy.iqoperate.service;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.filter.CandlesFilter;
import com.zcy.iqoperate.model.response.CandlesResponse;

/**
 * 获取otc蜡烛集合
 * create date : 2019/1/7
 */
public interface OtcCandlesService {

    /**
     * 发送请求蜡烛集合
     *
     * @param candlesFilter
     */
    BtResult sendGetCandles(CandlesFilter candlesFilter) throws Exception;

    /**
     * 接收蜡烛集合
     *
     * @param candlesResponse
     * @throws Exception
     */
    void receiveCandles(CandlesResponse candlesResponse) throws Exception;

}
