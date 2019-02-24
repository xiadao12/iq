package com.zcy.iqoperate.filter;

/**
 * create date : 2019/2/24
 */
public class StrategyOtcContinuousFilter {

    //外汇id
    private Integer activeId;

    //是否创建策略结果文件
    private Boolean createResultFile = false;

    public Integer getActiveId() {
        return activeId;
    }

    public void setActiveId(Integer activeId) {
        this.activeId = activeId;
    }

    public Boolean getCreateResultFile() {
        return createResultFile;
    }

    public void setCreateResultFile(Boolean createResultFile) {
        this.createResultFile = createResultFile;
    }
}
