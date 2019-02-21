package com.zcy.iqoperate.filter;

/**
 * 获取蜡烛图条件
 * create date : 2019/2/20
 */
public class CandlesFilter {

    //外汇id
    private Integer activeId;

    //蜡烛图天数
    private Integer candleDays;

    //当前id
    private Long currentId;

    //蜡烛尺寸，以秒为单位，默认60
    Integer candleSize = 60;

    public Integer getActiveId() {
        return activeId;
    }

    public void setActiveId(Integer activeId) {
        this.activeId = activeId;
    }

    public Integer getCandleDays() {
        return candleDays;
    }

    public void setCandleDays(Integer candleDays) {
        this.candleDays = candleDays;
    }

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long currentId) {
        this.currentId = currentId;
    }

    public Integer getCandleSize() {
        return candleSize;
    }

    public void setCandleSize(Integer candleSize) {
        this.candleSize = candleSize;
    }
}