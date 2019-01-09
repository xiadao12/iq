package com.zcy.iqoperate.model.request;

/**
 * 取消实时请求蜡烛图
 *
 * create date : 2019/1/6
 */

//{"name":"unsubscribeMessage","request_id":"s_219","msg":{"name":"candle-generated","params":{"routingFilters":{"active_id":4,"size":60}}}}

public class UnCandleGeneratedRequest extends CandleGeneratedRequest{
    private final String name = "unsubscribeMessage";

    /**
     * 将CandleGeneratedRequest转为UnCandleGeneratedRequest
     * @return
     */
    public UnCandleGeneratedRequest tranToUn(CandleGeneratedRequest candleGeneratedRequest){
        UnCandleGeneratedRequest unCandleGeneratedRequest = new UnCandleGeneratedRequest();
        unCandleGeneratedRequest.setRequest_id(candleGeneratedRequest.getRequest_id());
        unCandleGeneratedRequest.setMsg(candleGeneratedRequest.getMsg());
        return unCandleGeneratedRequest;
    }
}
