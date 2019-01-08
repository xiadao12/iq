package com.zcy.iqconnect.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * create date : 2019/1/6
 */
public class JsonUtil {

    /**
     * 将对象转为json字符串
     *
     * @param object
     * @return
     * @throws Exception
     */
    public static String ObjectToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

}
