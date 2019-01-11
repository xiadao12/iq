package com.zcy.iqoperate.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * create date : 2019/1/7
 */
public class DoubleUtil {

    /**
     * 加法
     *
     * @param a
     * @param b
     * @return
     */
    public static Double add(Double a, Double b) {
        BigDecimal bigDecimalA = new BigDecimal(Double.toString(a));
        BigDecimal bigDecimalB = new BigDecimal(Double.toString(b));
        Double result = bigDecimalA.add(bigDecimalB).doubleValue();
        return round(result, 8);
    }

    /**
     * 减法
     *
     * @param a
     * @param b
     * @return
     */
    public static Double sub(Double a, Double b) {
        BigDecimal bigDecimalA = new BigDecimal(Double.toString(a));
        BigDecimal bigDecimalB = new BigDecimal(Double.toString(b));
        Double result = bigDecimalA.subtract(bigDecimalB).doubleValue();
        return round(result, 8);
    }

    /**
     * 乘法
     *
     * @param a
     * @param b
     * @return
     */
    public static Double multiply(Double a, Double b) {
        BigDecimal bigDecimalA = new BigDecimal(Double.toString(a));
        BigDecimal bigDecimalB = new BigDecimal(Double.toString(b));
        Double result = bigDecimalA.multiply(bigDecimalB).doubleValue();
        return round(result, 8);
    }

    /**
     * 除法，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
     *
     * @param a
     * @param b
     * @return
     */
    public static Double divide(Double a, Double b) {
        BigDecimal bigDecimalA = new BigDecimal(Double.toString(a));
        BigDecimal bigDecimalB = new BigDecimal(Double.toString(b));
        return bigDecimalA.divide(bigDecimalB, 8, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 提供指定数值的（精确）小数位四舍五入处理。
     *
     * @param value 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double value, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(value));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }


}
