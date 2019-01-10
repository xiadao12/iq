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

}
