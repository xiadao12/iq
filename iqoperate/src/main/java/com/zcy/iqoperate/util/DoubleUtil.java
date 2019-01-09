package com.zcy.iqoperate.util;

import java.math.BigDecimal;

/**
 * create date : 2019/1/7
 */
public class DoubleUtil {

    /**
     * 减法
     * @param a
     * @param b
     * @return
     */
    public static Double sub(Double a, Double b){
        BigDecimal bigDecimalA = new BigDecimal(Double.toString(a));
        BigDecimal bigDecimalB = new BigDecimal(Double.toString(b));
        return bigDecimalA.subtract(bigDecimalB).doubleValue();
    }

}
