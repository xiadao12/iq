package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.filter.StrategyLongFilter;
import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.JsonUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 策略：长蜡烛，一分钟，快到时间的时候一直反向
 * create date : 2019/1/19
 */
@Component
public class StrategyLong1m {

    public void execute(List<CandlesResponse.Candle> candles, Object strategyFilterObject) {
        StrategyLongFilter strategyLongFilter = JsonUtil.convertValue(strategyFilterObject, StrategyLongFilter.class);

        System.out.println();

        //长蜡烛快结束时候，最后5s连续反向
        //Integer preSize = 5;
        //BigDecimal factor = new BigDecimal(0.0001);

        for (int preSize = 1; preSize <= 3; preSize++) {

            //遍历起止因子
            for (BigDecimal factor = new BigDecimal(0.000000); factor.compareTo(new BigDecimal(0.0001)) < 0; factor = factor.add(new BigDecimal(0.000001))) {

                //记录输赢的次数
                Integer winNum = 0;
                Integer lostNum = 0;

                //记录赢输的时间点
                List winTimeList = new ArrayList<>();
                List lostTimeList = new ArrayList<>();

                for (int i = 0 + 60; i < candles.size() - 60; i++) {
                    CandlesResponse.Candle candle = candles.get(i);

                    CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);

                    BigDecimal open = candle.getOpen();
                    BigDecimal entity = candle.getClose().subtract(candles.get(i - 60).getClose()).abs();

                    //判断蜡烛是否是每分钟整点时间
                    Long currentCandleto = candle.getTo();
                    String currentCandletoString = DateUtil.timeStampToDateString(currentCandleto * 1000);
                    if (currentCandleto % 60 != 0) {
                        continue;
                    }

                    //判断实体是否足够长
                    if (entity.compareTo(open.multiply(factor)) < 0) {
                        continue;
                    }

                    //获取蜡烛涨跌
                    Integer trend = candle.getClose().compareTo(candles.get(i - 60).getClose());

                    //最后的几个是否是反向
                    Boolean preSame = true;
                    for (int j = 0; j < preSize; j++) {

                        CandleMessage mCandleMessage = CandleMessage.getCandleMessage(candles.get(i - j - 1));
                        CandleMessage nCandleMessage = CandleMessage.getCandleMessage(candles.get(i - j));

                        //如果长蜡烛是涨
                        Integer tempBidui = mCandleMessage.getEntity().compareTo(nCandleMessage.getEntity());
                        if (tempBidui.equals(trend) || tempBidui == 0) {
                            preSame = false;
                        }
                    }

                    if (preSame) {
                        //获取结果蜡烛,也就是下一个蜡烛
                        Integer resultTrend = candles.get(i + 60).getClose().compareTo(candle.getClose());

                        if (resultTrend.equals(trend)) {
                            lostNum++;
                            lostTimeList.add(currentCandletoString);
                        } else {
                            winNum++;
                            winTimeList.add(currentCandletoString);
                        }
                    }

                }

                System.out.println();
                System.out.println("反向个数：" + preSize);
                System.out.println("因子为：" + factor);
                System.out.println("winNum = " + winNum);
                System.out.println("lostNum = " + lostNum);
                System.out.println("winTimeList = " + winTimeList);
                System.out.println("lostTimeList = " + lostTimeList);
            }
        }
    }
}
