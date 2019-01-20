package com.zcy.iqoperate.filter;

import java.util.List;

/**
 * 策略条件
 * create date : 2019/1/13
 */
public class StrategyBaseFilter {

    //外汇id
    private Integer activeId;

    //蜡烛图天数
    private Integer candleDays;

    //当前id
    private Long currentId;

    //是否从文件中读取蜡烛图
    private Boolean readCandlesFromFile = false;

    //是否创建蜡烛集合文件
    private Boolean createCandlesFile = true;

    //是否创建策略结果文件
    private Boolean createResultFile = true;

    //蜡烛尺寸，以秒为单位，默认60
    Integer candleSize = 60;


/*    //查询的开始时间00:00:00
    private String startTimeString;

    //查询的结束日期23:59:59
    private String endDateString;

    //查询的间隔时间，单位是秒
    private Integer timeDistance;*/

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

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long currentId) {
        this.currentId = currentId;
    }

    public Boolean getReadCandlesFromFile() {
        return readCandlesFromFile;
    }

    public void setReadCandlesFromFile(Boolean readCandlesFromFile) {
        this.readCandlesFromFile = readCandlesFromFile;
    }

    public Boolean getCreateCandlesFile() {
        return createCandlesFile;
    }

    public void setCreateCandlesFile(Boolean createCandlesFile) {
        this.createCandlesFile = createCandlesFile;
    }

    public Boolean getCreateResultFile() {
        return createResultFile;
    }

    public void setCreateResultFile(Boolean createResultFile) {
        this.createResultFile = createResultFile;
    }

    public Integer getCandleSize() {
        return candleSize;
    }

    public void setCandleSize(Integer candleSize) {
        this.candleSize = candleSize;
    }

    /**
     * 内部类
     */
    public static class ActiveTime {

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
