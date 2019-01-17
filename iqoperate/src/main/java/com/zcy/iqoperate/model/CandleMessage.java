package com.zcy.iqoperate.model;

import com.zcy.iqoperate.model.response.CandlesResponse;

import java.math.BigDecimal;

/**
 * 蜡烛信息
 * <p>
 * create date : 2019/1/8
 */
public class CandleMessage {

    //实体最小值
    private BigDecimal lower = new BigDecimal(0);
    //实体最大值
    private BigDecimal upper = new BigDecimal(0);
    //下影线长
    private BigDecimal lowerShadow = new BigDecimal(0);
    //上影线长
    private BigDecimal upperShadow = new BigDecimal(0);
    //实体长度
    private BigDecimal entity = new BigDecimal(0);
    //是否是涨,1为涨，-1为跌，0为平
    private Integer trend = 0;
    //开始时间
    private Long from;
    //结束时间
    private Long to;

    /**
     * 根据蜡烛图获取详细信息
     *
     * @param candle
     * @return
     * @throws Exception
     */
    public static CandleMessage getCandleMessage(CandlesResponse.Candle candle) {

        BigDecimal open = candle.getOpen();
        BigDecimal close = candle.getClose();
        BigDecimal min = candle.getMin();
        BigDecimal max = candle.getMax();

        //判断是涨或跌,1涨  0平  -1跌
        Integer trend = close.compareTo(open);
        //实体最小值
        BigDecimal lower = new BigDecimal(0);
        //实体最大值
        BigDecimal upper = new BigDecimal(0);
        //下影线长
        BigDecimal lowerShadow = new BigDecimal(0);
        //上影线长
        BigDecimal upperShadow = new BigDecimal(0);
        //实体长度
        BigDecimal entity = open.subtract(close).abs();
        //开始时间
        Long from = candle.getFrom();
        //结束时间
        Long to = candle.getTo();

        //如果是上涨或平
        if (trend >= 0) {
            lower = open;
            upper = close;
            lowerShadow = open.subtract(min);
            upperShadow = max.subtract(close);
        } else {
            lower = close;
            upper = open;
            lowerShadow = close.subtract(min);
            upperShadow = max.subtract(open);
        }

        CandleMessage candleMessage = new CandleMessage();

        candleMessage.setLower(lower);

        candleMessage.setUpper(upper);
        candleMessage.setLowerShadow(lowerShadow);
        candleMessage.setUpperShadow(upperShadow);
        candleMessage.setEntity(entity);
        candleMessage.setTrend(trend);
        candleMessage.setFrom(from);
        candleMessage.setTo(to);

        return candleMessage;
    }

    public BigDecimal getLower() {
        return lower;
    }

    public void setLower(BigDecimal lower) {
        this.lower = lower;
    }

    public BigDecimal getUpper() {
        return upper;
    }

    public void setUpper(BigDecimal upper) {
        this.upper = upper;
    }

    public BigDecimal getLowerShadow() {
        return lowerShadow;
    }

    public void setLowerShadow(BigDecimal lowerShadow) {
        this.lowerShadow = lowerShadow;
    }

    public BigDecimal getUpperShadow() {
        return upperShadow;
    }

    public void setUpperShadow(BigDecimal upperShadow) {
        this.upperShadow = upperShadow;
    }

    public BigDecimal getEntity() {
        return entity;
    }

    public void setEntity(BigDecimal entity) {
        this.entity = entity;
    }

    public Integer getTrend() {
        return trend;
    }

    public void setTrend(Integer trend) {
        this.trend = trend;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }
}
