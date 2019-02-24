package com.zcy.iqoperate.util;

import java.util.List;

/**
 * create date : 2019/1/10
 */
public class ListUtil {

    /**
     * 向左偏移
     *
     * @param list
     * @param newObject 新增的对象
     */
    public static void offsetLeft(List list, Object newObject) {
        if (list != null && list.size() > 0) {
            list.remove(0);
            list.add(newObject);
        }
    }

    /**
     * 排序，正序
     *
     * @param list
     */
    public static void order(List list) {
        order(list, false);
    }

    /**
     * 排序
     *
     * @param list
     * @param desc
     */
    public static void order(List list, Boolean desc) {

        //当比对结果为1时，正序。为-1时，逆序
        int compareResult = 1;

        //逆序
        if (desc == null || desc) {
            compareResult = -1;
        }

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {

                Object oi = list.get(i);
                Object oj = list.get(j);

                if (Integer.valueOf(oi.toString().compareTo(oj.toString())).compareTo(0) == compareResult) {
                    Object temp = oi;
                    list.set(i, oj);
                    list.set(j, temp);
                }
            }
        }

    }

}
