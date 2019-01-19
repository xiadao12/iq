package com.zcy.iqoperate.filter;

import java.math.BigDecimal;

/**
 * create date : 2019/1/13
 */
public class StrategyLongFilter extends StrategyBaseFilter {

    //因子最小值
    private BigDecimal startFactor;

    //因子最大值
    private BigDecimal endFactor;

    //因子从小到大间距
    private BigDecimal factorDistance;

    //跳过个数再获取结果
    private Integer skipSize;

    public BigDecimal getStartFactor() {
        return startFactor;
    }

    public void setStartFactor(BigDecimal startFactor) {
        this.startFactor = startFactor;
    }

    public BigDecimal getEndFactor() {
        return endFactor;
    }

    public void setEndFactor(BigDecimal endFactor) {
        this.endFactor = endFactor;
    }

    public BigDecimal getFactorDistance() {
        return factorDistance;
    }

    public void setFactorDistance(BigDecimal factorDistance) {
        this.factorDistance = factorDistance;
    }

    public Integer getSkipSize() {
        return skipSize;
    }

    public void setSkipSize(Integer skipSize) {
        this.skipSize = skipSize;
    }
}
