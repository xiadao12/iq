package com.zcy.iqoperate.filter;

/**
 * 策略条件
 * create date : 2019/1/13
 */
public class BaseStrategyFilter {
    //外汇id
    private Integer activeId;

    //查询的开始日期 2019-01-01 22:00:00
    private String startDate;

    //查询的开始日期 2019-01-01 22:00:00
    private String endDate;

    //每天活跃开始时间 22:00:00
    private String activeStartTimeString;

    //每天活跃结束时间 05:00:00
    private String activeEndTimeString;

    public Integer getActiveId() {
        return activeId;
    }

    public void setActiveId(Integer activeId) {
        this.activeId = activeId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getActiveStartTimeString() {
        return activeStartTimeString;
    }

    public void setActiveStartTimeString(String activeStartTimeString) {
        this.activeStartTimeString = activeStartTimeString;
    }

    public String getActiveEndTimeString() {
        return activeEndTimeString;
    }

    public void setActiveEndTimeString(String activeEndTimeString) {
        this.activeEndTimeString = activeEndTimeString;
    }

    public static Class ActiveDate{

    }
}
