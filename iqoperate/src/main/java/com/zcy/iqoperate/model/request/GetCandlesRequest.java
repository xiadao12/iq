package com.zcy.iqoperate.model.request;

/**
 * 根据起止时间获取蜡烛图
 * <p>
 * create date : 2019/1/7
 */
//{"name":"sendMessage","request_id":"104","msg":{"name":"get-candles","version":"2.0","body":{"active_id":4,"size":60,"from_id":440428,"to_id":440453,"only_closed":true}}}
public class GetCandlesRequest {
    //名称
    private final String name = "sendMessage";
    //请求id
    private String request_id;
    //消息
    private Msg msg;

    /**
     * 默认构造方法
     */
    public GetCandlesRequest() {
    }

    /**
     * 构造方法
     *
     * @param request_id
     * @param msg_body_active_id
     * @param msg_body_size
     * @param msg_body_from_id
     * @param msg_body_to_id
     */
    public GetCandlesRequest(
            String request_id,
            Integer msg_body_active_id,
            Integer msg_body_size,
            Long msg_body_from_id,
            Long msg_body_to_id) {

        Body body = new Body();
        body.setActive_id(msg_body_active_id);
        body.setSize(msg_body_size);
        body.setFrom_id(msg_body_from_id);
        body.setTo_id(msg_body_to_id);

        Msg msg = new Msg();
        msg.setBody(body);

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
        private final String name = "get-candles";
        private final String version = "2.0";
        private Body body;

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }
    }

    //"body":{"active_id":4,"size":60,"from_id":440428,"to_id":440453,"only_closed":true}
    public class Body {
        private Integer active_id;
        private Integer size;
        private Long from_id;
        private Long to_id;
        private final Boolean only_closed = true;

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

        public Long getFrom_id() {
            return from_id;
        }

        public void setFrom_id(Long from_id) {
            this.from_id = from_id;
        }

        public Long getTo_id() {
            return to_id;
        }

        public void setTo_id(Long to_id) {
            this.to_id = to_id;
        }

        public Boolean getOnly_closed() {
            return only_closed;
        }
    }
}
