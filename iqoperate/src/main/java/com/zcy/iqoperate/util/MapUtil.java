package com.zcy.iqoperate.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * create date : 2019/1/19
 */
public class MapUtil {
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<Integer, Integer> sortMapByKey(Map<Integer, Integer> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<Integer, Integer> sortMap = new TreeMap<Integer, Integer>(new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }


    /**
     * 比较器类
     */
    private static class MapKeyComparator<I extends Number> implements Comparator<Integer>{

        @Override
        public int compare(Integer str1, Integer str2) {

            return str1.compareTo(str2);
        }
    }
}
