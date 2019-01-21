package com.zcy.iqoperate.filter;

/**
 * 连续蜡烛，结束后倍投
 *
 * create date : 2019/1/19
 */
public class StrategyContinuousDoubleFilter extends StrategyBaseFilter{

    //连续个数
    private Integer continuousSize;

    //支付的方向，true为相同，false为相反
    private Boolean payDirect;

    public Integer getContinuousSize() {
        return continuousSize;
    }

    public void setContinuousSize(Integer continuousSize) {
        this.continuousSize = continuousSize;
    }

    public Boolean getPayDirect() {
        return payDirect;
    }

    public void setPayDirect(Boolean payDirect) {
        this.payDirect = payDirect;
    }
}
