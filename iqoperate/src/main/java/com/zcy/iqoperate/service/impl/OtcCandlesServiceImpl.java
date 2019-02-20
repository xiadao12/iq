package com.zcy.iqoperate.service.impl;

import com.zcy.iqoperate.core.BtResult;
import com.zcy.iqoperate.filter.CandlesFilter;
import com.zcy.iqoperate.model.request.GetCandlesRequest;
import com.zcy.iqoperate.service.WebsocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 获取otc蜡烛集合
 * create date : 2019/1/7
 */
@Service
public class OtcCandlesServiceImpl {

    @Autowired
    private WebsocketService websocketService;

    /**
     * 执行
     *
     * @param candlesFilter
     */
    BtResult execute(CandlesFilter candlesFilter) throws Exception {

        //每个蜡烛图秒数
        Integer candleSize = candlesFilter.getCandleSize();

        //外汇id
        Integer activeId = candlesFilter.getActiveId();
        if (activeId == null) {
            return BtResult.ERROR("未传activeId");
        }

        //初始化查询天数
        Integer candleDays = candlesFilter.getCandleDays();
        if (candleDays == null) {
            return BtResult.ERROR("未传candleDays");
        }

        //请求的id
        Long currentId = candlesFilter.getRequestId();
        if (currentId == null) {
            return BtResult.ERROR("未传请求的id");
        }

        //每次查询个数
        Integer perSize = 1;

        // 12*60
        //每天有的个数
        Integer dayIdSize = 24 * 60 * 60 / candleSize;

        //获取candles循环次数 = candleDays * 2
        Integer candlesCycleSize = candleDays * (dayIdSize / perSize);

        //计算出第一个请求的id
        currentId = currentId - candlesCycleSize * perSize;

        //将其进行赋值临时处理。因为这边接收candles那边candlesCycleSize会自减，造成数据冲突
        //Integer candlesCycleSizeTemp = candlesCycleSize;

        for (int i = 0; i < candlesCycleSize; i++) {

            System.out.println("发送消息总次数 = " + candlesCycleSize + "   当前次数 = " + i);

            //每隔10次，睡眠2s
            if (i % 10 == 0) {
                Thread.sleep(5000L);
            }

            String request_id = String.valueOf(System.currentTimeMillis() + i);

            GetCandlesRequest getCandlesRequest = new GetCandlesRequest(
                    request_id,
                    activeId,
                    candleSize,
                    currentId - perSize,
                    currentId);

            websocketService.sendMessage(getCandlesRequest);

            currentId = currentId + perSize;
        }

        return BtResult.OK();
    }

}
