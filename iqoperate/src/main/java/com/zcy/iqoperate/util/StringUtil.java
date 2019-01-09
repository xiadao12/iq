package com.zcy.iqoperate.util;

/**
 * @version 1.0.0
 * @brief ecs insight
 * @note 修订历史： 1、yangzhouchuan于2018/12/17设计并构建初始版本v1.0.0
 */
public class StringUtil {

    /**
     * 判断字符串是否为null或为空
     *
     * @param value
     * @return
     */
    public static Boolean isEmpty(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否非null而且非空
     *
     * @param value
     * @return
     */
    public static Boolean isNotEmpty(String value) {
        if (value != null && !value.isEmpty()) {
            return true;
        }
        return false;
    }


}
