package com.zcy.iqoperate.filter;

import java.math.BigDecimal;

/**
 * create date : 2019/1/19
 */
public class StrategyContinuousFilter extends StrategyBaseFilter{

    //因子最小值
    private BigDecimal startFactor;

    //因子最大值
    private BigDecimal endFactor;

    //因子从小到大间距
    private BigDecimal factorDistance;

    //几个蜡烛符合达到相应长度
    private Integer conformNumber;

    //从第几个蜡烛图开始支付
    private Integer payFromNumber;

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

    public Integer getConformNumber() {
        return conformNumber;
    }

    public void setConformNumber(Integer conformNumber) {
        this.conformNumber = conformNumber;
    }

    public Integer getPayFromNumber() {
        return payFromNumber;
    }

    public void setPayFromNumber(Integer payFromNumber) {
        this.payFromNumber = payFromNumber;
    }
}
