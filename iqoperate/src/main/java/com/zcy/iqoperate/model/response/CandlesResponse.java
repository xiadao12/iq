package com.zcy.iqoperate.model.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 蜡烛图集合
 * {"name":"candles","request_id":"1546867719_8254357746","msg":{"candles":[{"id":440897,"from":1546867560,"to":1546867620,"open":1.144935,"close":1.14492,"min":1.144875,"max":1.144995,"volume":292},{"id":440898,"from":1546867620,"to":1546867680,"open":1.14492,"close":1.14476,"min":1.144655,"max":1.14495,"volume":392}]},"status":2000}
 * <p>
 * {
 * "name":"candles",
 * "request_id":"1546867719_8254357746",
 * "msg":{
 * "candles":[
 * {
 * "id":440897,
 * "from":1546867560,
 * "to":1546867620,
 * "open":1.144935,
 * "close":1.14492,
 * "min":1.144875,
 * "max":1.144995,
 * "volume":292
 * },
 * {
 * "id":440898,
 * "from":1546867620,
 * "to":1546867680,
 * "open":1.14492,
 * "close":1.14476,
 * "min":1.144655,
 * "max":1.14495,
 * "volume":392
 * }
 * ]
 * },
 * "status":2000
 * }
 * create date : 2019/1/7
 */
public class CandlesResponse {
    private String name;
    private String request_id;
    //内部类
    private Msg msg;
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 内部类
     */
    public static class Msg {
        List<Candle> candles = new ArrayList<>();

        public List<Candle> getCandles() {
            return candles;
        }

        public void setCandles(List<Candle> candles) {
            this.candles = candles;
        }
    }

    public static class Candle {
        private Long id;
        private Long from;
        private Long to;
        private BigDecimal open;
        private BigDecimal close;
        private BigDecimal min;
        private BigDecimal max;
        private Long volume;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public Long getVolume() {
            return volume;
        }

        public void setVolume(Long volume) {
            this.volume = volume;
        }
    }

}
