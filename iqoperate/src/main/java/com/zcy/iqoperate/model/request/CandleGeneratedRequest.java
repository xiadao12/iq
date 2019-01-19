package com.zcy.iqoperate.model.request;

/**
 * 实时请求蜡烛图
 * <p>
 * create date : 2019/1/6
 */
//{"name":"subscribeMessage","request_id":"s_71","msg":{"name":"candle-generated","params":{"routingFilters":{"active_id":76,"size":1}}}}
public class CandleGeneratedRequest {

    private final String name = "subscribeMessage";
    private String request_id;
    private Msg msg;

    /**
     * 默认构造方法
     */
    public CandleGeneratedRequest() {
    }

    /**
     * 构造方法
     *
     * @param request_id
     * @param msg_params_routingFilters_active_id
     * @param msg_params_routingFilters_size
     */
    public CandleGeneratedRequest(String request_id,
                                  Integer msg_params_routingFilters_active_id,
                                  Integer msg_params_routingFilters_size) {

        RoutingFilters routingFilters = new RoutingFilters();
        routingFilters.setActive_id(msg_params_routingFilters_active_id);
        routingFilters.setSize(msg_params_routingFilters_size);

        Params params = new Params();
        params.setRoutingFilters(routingFilters);

        Msg msg = new Msg();
        msg.setParams(params);

        setRequest_id(request_id);
        setMsg(msg);
    }

    public String getName() {
        return name;
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

    /**
     * 内部类
     */
    public class Msg {
        private final String name = "candle-generated";
        private Params params;

        public String getName() {
            return name;
        }

        public Params getParams() {
            return params;
        }

        public void setParams(Params params) {
            this.params = params;
        }
    }

    /**
     * 内部类
     */
    private class Params {
        RoutingFilters routingFilters;

        public RoutingFilters getRoutingFilters() {
            return routingFilters;
        }

        public void setRoutingFilters(RoutingFilters routingFilters) {
            this.routingFilters = routingFilters;
        }
    }

    /**
     * 内部类
     */
    private class RoutingFilters {
        private Integer active_id;
        private Integer size;

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
    }
}
