package com.zcy.iqoperate.filter;

import java.util.List;

/**
 * 策略条件
 * create date : 2019/1/13
 */
public class BaseStrategyFilter {

    //外汇id
    private Integer activeId;

    //蜡烛图天数
    private Integer candleDays;

/*    //查询的开始日期 2019-01-01 22:00:00
    private String startDateString;

    //查询的开始日期 2019-01-01 22:00:00
    private String endDateString;*/

    //内部类，活跃时间
    /*
        [
            {
                "activeStartTimeString":"22:00:00",
                "activeEndTimeString":"23:59:59"
            },
            {
                "activeStartTimeString":"00:00:00",
                "activeEndTimeString":"04:59:59"
            }
        ]
     */
    private List<ActiveTime> activeTimes;

    public Integer getActiveId() {
        return activeId;
    }

    public void setActiveId(Integer activeId) {
        this.activeId = activeId;
    }

    public List<ActiveTime> getActiveTimes() {
        return activeTimes;
    }

    public void setActiveTimes(List<ActiveTime> activeTimes) {
        this.activeTimes = activeTimes;
    }

    public Integer getCandleDays() {
        return candleDays;
    }

    public void setCandleDays(Integer candleDays) {
        this.candleDays = candleDays;
    }

    public static class ActiveTime{

        //每天活跃开始时间 22:00:00
        private String activeStartTimeString;

        //每天活跃结束时间 05:00:00
        private String activeEndTimeString;

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
    }
}
