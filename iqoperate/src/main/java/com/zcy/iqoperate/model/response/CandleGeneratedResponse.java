package com.zcy.iqoperate.model.response;

import java.math.BigDecimal;

/**
 * 实时蜡烛图
 *
 * create date : 2019/1/6
 */
public class CandleGeneratedResponse{
    private String name;
    //内部类
    private Msg msg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    /**
     * 内部类
     */
    public class Msg {
        private Integer active_id;
        private Integer size;
        private Long at;
        private Long from;
        private Long to;
        private Long min_at;
        private Long max_at;
        private Integer id;
        private BigDecimal open;
        private BigDecimal close;
        private BigDecimal min;
        private BigDecimal max;
        private BigDecimal ask;
        private BigDecimal bid;
        private Integer volume;
        private String phase;

        public Integer getActive_id() {
            return active_id;
        }

        public void setActive_id(Integer active_id) {
            this.active_id = active_id;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Long getAt() {
            return at;
        }

        public void setAt(Long at) {
            this.at = at;
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

        public Long getMin_at() {
            return min_at;
        }

        public void setMin_at(Long min_at) {
            this.min_at = min_at;
        }

        public Long getMax_at() {
            return max_at;
        }

        public void setMax_at(Long max_at) {
            this.max_at = max_at;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public void setOpen(BigDecimal open) {
            this.open = open;
        }

        public BigDecimal getClose() {
            return close;
        }

        public void setClose(BigDecimal close) {
            this.close = close;
        }

        public BigDecimal getMin() {
            return min;
        }

        public void setMin(BigDecimal min) {
            this.min = min;
        }

        public BigDecimal getMax() {
            return max;
        }

        public void setMax(BigDecimal max) {
            this.max = max;
        }

        public BigDecimal getAsk() {
            return ask;
        }

        public void setAsk(BigDecimal ask) {
            this.ask = ask;
        }

        public BigDecimal getBid() {
            return bid;
        }

        public void setBid(BigDecimal bid) {
            this.bid = bid;
        }

        public Integer getVolume() {
            return volume;
        }

        public void setVolume(Integer volume) {
            this.volume = volume;
        }

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }
    }

}
