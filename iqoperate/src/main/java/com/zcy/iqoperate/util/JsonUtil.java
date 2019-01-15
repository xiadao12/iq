package com.zcy.iqoperate.util;

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
    public static String ObjectToJsonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 将对象转换为实体
     * @param fromObject
     * @param toValueType
     * @param <T>
     * @return
     */
    public static <T> T convertValue(Object fromObject, Class<T> toValueType){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(fromObject,toValueType);
    }

}
