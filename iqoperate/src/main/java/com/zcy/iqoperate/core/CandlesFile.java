package com.zcy.iqoperate.core;

import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.FileUtil;
import com.zcy.iqoperate.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * create date : 2019/2/24
 */
public class CandlesFile {

    /**
     * 从文件集合获取蜡烛集合
     *
     * @param filePath 文件路径
     * @return
     */
    public static List<CandlesResponse.Candle> getCandelsFromFile(String filePath) {

        //从文件读取蜡烛集合
        List<Map> fileCandlesMapList = (List) FileUtil.readFileToObject(filePath, List.class);

        //将读取的蜡烛类型转换
        List<CandlesResponse.Candle> allCandles = new ArrayList<>();
        for (Map map : fileCandlesMapList) {
            CandlesResponse.Candle candle = JsonUtil.convertValue(map, CandlesResponse.Candle.class);
            allCandles.add(candle);
        }

        return allCandles;
    }

    /**
     * 从文件获取蜡烛集合
     *
     * @param filePathList 文件路径集合
     * @return
     */
    public static List<List<CandlesResponse.Candle>> getCandelsFromFileList(List<String> filePathList) {

        List<List<CandlesResponse.Candle>> result = new ArrayList<>();

        for (String filePath : filePathList) {
            //单个文件蜡烛集合
            List<CandlesResponse.Candle> candles = getCandelsFromFile(filePath);
            result.add(candles);
        }

        return result;
    }

}