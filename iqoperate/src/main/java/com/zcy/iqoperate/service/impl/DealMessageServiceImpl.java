package com.zcy.iqoperate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcy.iqoperate.model.response.CandleGeneratedResponse;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.model.response.TimeSyncResponse;
import com.zcy.iqoperate.service.DealMessageService;
import com.zcy.iqoperate.service.OtcCandlesService;
import com.zcy.iqoperate.service.TryStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * create date : 2019/1/6
 */
@Service
public class DealMessageServiceImpl implements DealMessageService {

    //记录上一个蜡烛信息
    private static CandleGeneratedResponse preCandleGeneratedResponse;

    @Autowired
    TryStrategyService tryStrategyService;

    @Autowired
    OtcCandlesService otcCandlesService;

    /**
     * 处理接收到的信息
     *
     * @param message
     */
    @Override
    public void dealMessage(String message) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //判断是什么信息
            if (message.contains("candle-generated")) {
                //实时蜡烛图信息
                CandleGeneratedResponse candleGeneratedResponse = objectMapper.readValue(message, CandleGeneratedResponse.class);
                dealCandleGenerated(candleGeneratedResponse);
            } else if (message.contains("timeSync")) {
                //服务器时间（毫秒）
                TimeSyncResponse timeSyncResponse = objectMapper.readValue(message, TimeSyncResponse.class);
                dealTimeSync(timeSyncResponse);
            } else if (message.contains("candles")) {
                //获取蜡烛集合
                CandlesResponse candlesResponse = objectMapper.readValue(message, CandlesResponse.class);
                dealCandles(candlesResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理蜡烛图消息
     *
     * @param candleGeneratedResponse
     */
    public void dealCandleGenerated(CandleGeneratedResponse candleGeneratedResponse) {

        //判断参数
        if (candleGeneratedResponse == null) {
            return;
        }

        //如果是第一次赋值
        if (preCandleGeneratedResponse == null) {
            preCandleGeneratedResponse = candleGeneratedResponse;
            return;
        }

        //上一个蜡烛图msg信息
        CandleGeneratedResponse.Msg preMsg = preCandleGeneratedResponse.getMsg();
        //当前蜡烛图msg信息
        CandleGeneratedResponse.Msg msg = candleGeneratedResponse.getMsg();
        //如果from相同，则说明还在一个蜡烛图中。不相同则说明进入到了下一个蜡烛图
        if (!preMsg.getFrom().equals(msg.getFrom())) {
            System.out.println("时间：" + preMsg.getFrom());
            System.out.println("本地：" + System.currentTimeMillis());
            System.out.println("实体：" + preMsg.getOpen() + "  " + preMsg.getClose());
            System.out.println("影线：" + preMsg.getMin() + "  " + preMsg.getMax());
        }
        //将最新的蜡烛图赋值
        preCandleGeneratedResponse = candleGeneratedResponse;
    }

    /**
     * 处理服务器时间戳
     *
     * @param timeSyncResponse
     */
    public void dealTimeSync(TimeSyncResponse timeSyncResponse) {

        if (timeSyncResponse == null) {
            return;
        }

        BaseServiceImpl.timeSync = timeSyncResponse.getMsg();
    }

    /**
     * 处理蜡烛图集合
     *
     * @param candlesResponse
     */
    public void dealCandles(CandlesResponse candlesResponse) throws Exception{
        if (candlesResponse == null) {
            return;
        }

        tryStrategyService.strategy(candlesResponse);
        //otcCandlesService.receiveCandles(candlesResponse);
    }
}