package com.zcy.iqoperate.strategy;

import com.zcy.iqoperate.core.CandlesFile;
import com.zcy.iqoperate.filter.StrategyOtcContinuousFilter;
import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.DateUtil;
import com.zcy.iqoperate.util.FileUtil;
import com.zcy.iqoperate.util.JsonUtil;
import com.zcy.iqoperate.util.ListUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 连续蜡烛，直到结束，专用于OTC，从文件获取
 * create date : 2019/2/24
 */
@Component
public class StrategyContinuousOverOTCFromFile {

    public void execute(Object strategyFilterObject) {
        //条件
        StrategyOtcContinuousFilter strategyOtcContinuousFilter = JsonUtil.convertValue(strategyFilterObject, StrategyOtcContinuousFilter.class);

        //获取activeId
        Integer activeId = strategyOtcContinuousFilter.getActiveId();

        //List<CandlesResponse.Candle> candles = CandlesFile.getCandelsFromFile("D:\\iq\\otc\\candles\\76\\otc_76_2019-02-16.json");

        //文件夹路径
        String dirPath = "D:\\iq\\otc\\candles\\" + activeId;
        //设置文件集合
        List<String> filePathList = FileUtil.getAbsolutePathsByDir(dirPath);
        //排序
        ListUtil.order(filePathList);
        //获取蜡烛集合
        List<List<CandlesResponse.Candle>> candlesList = CandlesFile.getCandelsFromFileList(filePathList);

        //输出文件内容
        //String content = "";

        //延迟几分钟购买
        for (int delayMinutes = 1; delayMinutes <= 2; delayMinutes++) {

            //连续蜡烛图个数以上（包含）
            for (int minCandlenum = 0; minCandlenum <= 3; minCandlenum++) {

                //所有蜡烛图长度因子
                for (BigDecimal allFactor = new BigDecimal(0.0001);
                     allFactor.compareTo(new BigDecimal(0.00025)) <= 0;
                     allFactor = allFactor.add(new BigDecimal(0.00001))) {

                    //反向的蜡烛图长度因子
                    for (BigDecimal reverseFactor = new BigDecimal(0.00001);
                         reverseFactor.compareTo(new BigDecimal(0.00001)) <= 0;
                         reverseFactor = reverseFactor.add(new BigDecimal(0.00001))) {

                        System.out.println();
                        System.out.println("延迟购买分钟 = " + delayMinutes);
                        //content = content + "延迟购买分钟  = " + delayMinutes;

                        System.out.println("最少的连续蜡烛数量 = " + minCandlenum);
                        //content = content + "最少的连续蜡烛数量  = " + minCandlenum;

                        System.out.println("所有蜡烛图长度因子 = " + allFactor);
                        //content = content + "所有蜡烛图长度因子  = " + allFactor;

                        System.out.println("反向的蜡烛图长度因子 = " + reverseFactor);
                        //content = content + "反向的蜡烛图长度因子  = " + reverseFactor;

                        //所有总和
                        Integer allDaysWin = 0;
                        Integer allDaysLost = 0;

                        //遍历日期
                        for (List<CandlesResponse.Candle> candles : candlesList) {

                            //记录赢输的时间点
                            List<String> winTimeList = new ArrayList<>();
                            List<String> lostTimeList = new ArrayList<>();

                            //连续蜡烛的涨跌方向
                            Integer continuousTrend = null;

                            //一个连续的蜡烛集合，默认将第一个蜡烛放在里面
                            List<CandlesResponse.Candle> candlesResult = new ArrayList<>();

                            //上一个蜡烛图信息，初始化为第一个蜡烛
                            CandleMessage preCandleMessage = null;

                            //遍历蜡烛
                            for (int k = 0; k < candles.size() - delayMinutes; k++) {

                                //获取当前蜡烛
                                CandlesResponse.Candle candle = candles.get(k);

                                //蜡烛信息
                                CandleMessage candleMessage = CandleMessage.getCandleMessage(candle);

                                //判断是否是第一个蜡烛
                                if (k == 0) {
                                    //赋值第一个蜡烛
                                    //一个连续的蜡烛集合，默认将第一个蜡烛放在里面
                                    candlesResult.clear();
                                    //清空集合后，放入蜡烛，是下一个集合的开始
                                    candlesResult.add(candle);

                                    //上一个蜡烛图信息，初始化为第一个蜡烛
                                    preCandleMessage = candleMessage;
                                    continue;
                                }

                                //当前涨跌
                                Integer currentTrend = candleMessage.getTrend();

                                if (continuousTrend == null) {
                                    //和上一个一样涨跌，且不是平的
                                    if (currentTrend.equals(preCandleMessage.getTrend()) && !currentTrend.equals(0)) {
                                        //则将蜡烛放到集合中
                                        candlesResult.add(candle);
                                    }
                                    //如果和上一个蜡烛图不同
                                    else {
                                        //反向的蜡烛最大长度
                                        BigDecimal candleMaxSize = candle.getOpen().multiply(reverseFactor);

                                        //符合连续蜡烛的最小长度
                                        BigDecimal allCandleMinSize = candle.getOpen().multiply(allFactor);

                                        //连续蜡烛中最后一个蜡烛的close
                                        BigDecimal lastCandleClose = candlesResult.get(candlesResult.size() - 1).getClose();

                                        //连续蜡烛中第一个蜡烛的close
                                        BigDecimal firstCandleOpen = candlesResult.get(0).getOpen();

                                        //获取总蜡烛长度
                                        BigDecimal allEntity = lastCandleClose.subtract(firstCandleOpen).abs();

                                        //判断连续个数,反向蜡烛实体长度,总蜡烛长度，如果不符合标准
                                        if (
                                                candlesResult.size() < minCandlenum
                                                        ||
                                                        candleMessage.getEntity().compareTo(candleMaxSize) > 0
                                                        ||
                                                        allEntity.compareTo(allCandleMinSize) < 0) {

                                            //清空蜡烛
                                            candlesResult.clear();
                                            continuousTrend = null;

                                            //清空集合后，放入蜡烛，是下一个集合的开始
                                            candlesResult.add(candle);
                                        } else {
                                            //获取连续蜡烛的涨跌
                                            continuousTrend = preCandleMessage.getTrend();
                                        }
                                    }
                                } else {
                                    //////////////////////////////////////
                                    //反向蜡烛下一个蜡烛的开始
                                    BigDecimal buy = candle.getOpen();

                                    //结果蜡烛
                                    CandlesResponse.Candle resultCandle = candles.get(k + delayMinutes - 1);

                                    //判断结果
                                    if ((continuousTrend > 0 && resultCandle.getClose().compareTo(buy) < 0) || (continuousTrend < 0 && resultCandle.getClose().compareTo(buy) > 0)) {

                                        winTimeList.add(DateUtil.timeStampToDateString(preCandleMessage.getTo() * 1000));

                                        candlesResult.clear();
                                        continuousTrend = null;

                                        //清空集合后，放入蜡烛，是下一个集合的开始
                                        candlesResult.add(candle);
                                    } else {
                                        lostTimeList.add(DateUtil.timeStampToDateString(preCandleMessage.getTo() * 1000));

                                        candlesResult.clear();
                                        continuousTrend = null;

                                        //清空集合后，放入蜡烛，是下一个集合的开始
                                        candlesResult.add(candle);
                                    }
                                }
                                preCandleMessage = candleMessage;
                            }

                            allDaysWin = allDaysWin + winTimeList.size();
                            allDaysLost = allDaysLost + lostTimeList.size();

                            System.out.println("日期为 =" + DateUtil.timeStampToDateString(candles.get(0).getTo() * 1000));
                            System.out.println("winNum = " + winTimeList.size());
                            System.out.println("lostNum = " + lostTimeList.size());
                            //System.out.println("winTimeList = " + winTimeList);
                            //System.out.println("lostTimeList = " + lostTimeList);
                        }

                        System.out.println("所有日期赢的总和" + allDaysWin);
                        System.out.println("所有日期输的总和" + allDaysLost);


/*                        content = content + "\nwinNum = " + winTimeList.size() + "\n";
                        content = content + "lostNum = " + lostTimeList.size() + "\n";
                        content = content + "winTimeList = " + lostTimeList + "\n";
                        content = content + "lostTimeList = " + lostTimeList + "\n";*/
                    }
                }

            }

            //创建策略结果文件
/*            if (strategyOtcContinuousFilter.getCreateResultFile()) {
                FileUtil.createFile(content, "D:/iq", "StrategyContinuousOverDelay_" + delayMinutes + strategyOtcContinuousFilter.getActiveId() + ".json");
            }*/
        }
    }

}
