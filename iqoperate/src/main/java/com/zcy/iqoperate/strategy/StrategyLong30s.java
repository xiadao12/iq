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
public class StrategyLong30s {

    public void execute(List<CandlesResponse.Candle> candles, Object strategyFilterObject) {
        StrategyLongFilter strategyLongFilter = JsonUtil.convertValue(strategyFilterObject, StrategyLongFilter.class);

        System.out.println();

        //长蜡烛快结束时候，最后5s连续反向
        //Integer preSize = 5;
        //BigDecimal factor = new BigDecimal(0.0001);

        for (int preSize = 0; preSize <= 5; preSize++) {

            //遍历起止因子
            for (BigDecimal factor = new BigDecimal(0.0); factor.compareTo(new BigDecimal(0.0005)) <= 0; factor = factor.add(new BigDecimal(0.0001))) {

                //记录输赢的次数
                Integer winNum = 0;
                Integer lostNum = 0;

                //记录赢输的时间点
                List winTimeList = new ArrayList<>();
                List lostTimeList = new ArrayList<>();

                for (int i = 0 + 30; i < candles.size() - 30; i++) {
                    CandlesResponse.Candle candle = candles.get(i);

/*                    if(DateUtil.timeStampToDateString(candle.getTo()*1000).equals("2019-01-16 17:16:00")){
                        System.out.println();
                    }*/


                    //判断蜡烛是否是每分钟整点时间
                    Long currentCandleto = candle.getTo();
                    if (currentCandleto % 30 != 0) {
                        continue;
                    }

                    if (currentCandleto % 60 == 0) {
                        continue;
                    }

                    BigDecimal open = candle.getOpen();
                    BigDecimal entity = candle.getClose().subtract(candles.get(i - 30).getClose()).abs();

                    String currentCandletoString = DateUtil.timeStampToDateString(currentCandleto * 1000);

                    //判断实体是否足够长
                    if (entity.compareTo(open.multiply(factor)) < 0) {
                        continue;
                    }

                    //获取蜡烛涨跌
                    Integer trend = candle.getClose().compareTo(candles.get(i - 30).getClose());

                    if(trend == 0){
                        continue;
                    }

                    //最后的几个是否是反向
                    Boolean preSame = true;
                    for (int j = 0; j < preSize; j++) {

                        CandlesResponse.Candle mCandle = candles.get(i - j - 1);
                        CandlesResponse.Candle nCandle = candles.get(i - j);

                        //如果长蜡烛是涨
                        Integer tempBidui = nCandle.getClose().compareTo(mCandle.getClose());

/*                        CandleMessage mCandleMessage = CandleMessage.getCandleMessage(candles.get(i - j - 1));
                        CandleMessage nCandleMessage = CandleMessage.getCandleMessage(candles.get(i - j));

                        //如果长蜡烛是涨
                        Integer tempBidui = mCandleMessage.getEntity().compareTo(nCandleMessage.getEntity());*/


                        if (tempBidui.equals(trend) || tempBidui == 0) {
                            preSame = false;
                        }
                    }

                    if (preSame) {
                        //获取结果蜡烛,也就是下一个蜡烛
                        Integer resultTrend = candles.get(i + 30).getClose().compareTo(candle.getClose());

                        if (!resultTrend.equals(trend) && resultTrend != 0) {
                            winNum++;
                            winTimeList.add(currentCandletoString);
                        } else {
                            lostNum++;
                            lostTimeList.add(currentCandletoString);
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
