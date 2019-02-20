package com.zcy.iqoperate.service;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.filter.CandlesFilter;

/**
 * 获取otc蜡烛集合
 * create date : 2019/1/7
 */
public interface OtcCandlesService {

    /**
     * 执行
     *
     * @param candlesFilter
     */
    BtResult execute(CandlesFilter candlesFilter) throws Exception;

}
