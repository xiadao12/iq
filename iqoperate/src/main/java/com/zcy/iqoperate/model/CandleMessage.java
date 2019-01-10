package com.zcy.iqoperate.model;

import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DoubleUtil;

/**
 * 蜡烛信息
 *
 * create date : 2019/1/8
 */
public class CandleMessage {

    //实体最小值
    private Double lower = 0D;
    //实体最大值
    private Double upper = 0D;
    //下影线长
    private Double lowerShadow = 0D;
    //上影线长
    private Double upperShadow = 0D;
    //实体长度
    private Double entity = 0D;
    //是否是涨,true为涨，false为跌，null为平
    private Boolean rise = null;
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

        Double open = candle.getOpen();
        Double close = candle.getClose();
        Double min = candle.getMin();
        Double max = candle.getMax();

        Boolean rise = null;
        //判断是涨或跌
        if (open == close) {
            rise = null;
        } else if (close > open) {
            rise = true;
        } else {
            rise = false;
        }

        //实体最小值
        Double lower = 0D;
        //实体最大值
        Double upper = 0D;
        //下影线长
        Double lowerShadow = 0D;
        //上影线长
        Double upperShadow = 0D;
        //实体长度
        Double entity = Math.abs(DoubleUtil.sub(open, close));
        //开始时间
        Long from = candle.getFrom();
        //结束时间
        Long to = candle.getTo();

        //如果是上涨
        if (rise == null || rise) {
            lower = open;
            upper = close;
            lowerShadow = DoubleUtil.sub(open, min);
            upperShadow = DoubleUtil.sub(max, close);
        } else {
            lower = close;
            upper = open;
            lowerShadow = DoubleUtil.sub(close, min);
            upperShadow = DoubleUtil.sub(max, open);
        }

        CandleMessage candleMessage = new CandleMessage();

        candleMessage.setLower(lower);

        candleMessage.setUpper(upper);
        candleMessage.setLowerShadow(lowerShadow);
        candleMessage.setUpperShadow(upperShadow);
        candleMessage.setEntity(entity);
        candleMessage.setRise(rise);
        candleMessage.setFrom(from);
        candleMessage.setTo(to);

        return candleMessage;
    }

    public Double getLower() {
        return lower;
    }

    public void setLower(Double lower) {
        this.lower = lower;
    }

    public Double getUpper() {
        return upper;
    }

    public void setUpper(Double upper) {
        this.upper = upper;
    }

    public Double getLowerShadow() {
        return lowerShadow;
    }

    public void setLowerShadow(Double lowerShadow) {
        this.lowerShadow = lowerShadow;
    }

    public Double getUpperShadow() {
        return upperShadow;
    }

    public void setUpperShadow(Double upperShadow) {
        this.upperShadow = upperShadow;
    }

    public Double getEntity() {
        return entity;
    }

    public void setEntity(Double entity) {
        this.entity = entity;
    }

    public Boolean getRise() {
        return rise;
    }

    public void setRise(Boolean rise) {
        this.rise = rise;
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
