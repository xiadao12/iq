package com.zcy.iqoperate.temp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * create date : 2019/1/9
 */
public class Test {
    public static void main(String[] args) throws IOException {

        List<CandlesResponse.Candle> candles = new ArrayList<>();

        CandlesResponse.Candle candle1 = new CandlesResponse.Candle();
        candle1.setId(1111L);

        CandlesResponse.Candle candle2 = new CandlesResponse.Candle();
        candle2.setId(2222L);

        candles.add(candle1);
        candles.add(candle2);

        String ss = "aaa";
        System.out.println(ss);

        CandleMessage candleMessage = new CandleMessage();
        candleMessage.setTrend(1);

        //FileUtil.createFile(JsonUtil.ObjectToJsonString(candleMessage),"D:/iq","a.json");

        String content = FileUtil.readFileToString("D:/iq/a.json");

        //CandlesResponse.Candle candle = JsonUtil.convertValue(content,CandlesResponse.Candle.class);
        //CandleMessage candleMessage1 = JsonUtil.convertValue(content, CandleMessage.class);

        File json = new File("D:/iq/candles.json");
        ObjectMapper mapper = new ObjectMapper();//此处引入的是jackson中的ObjectMapper类
        List dataNode = mapper.readValue(json, List.class);

/*        Map<String, Object> dataNode = mapper.readValue(json, Map.class);
        List<Map<String, Object>> data =  (List<Map<String, Object>>)dataNode.get("abc");*/

        System.out.println(11);

    }
}
