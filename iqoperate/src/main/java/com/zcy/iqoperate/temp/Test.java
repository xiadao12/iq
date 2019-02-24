package com.zcy.iqoperate.temp;

import com.zcy.iqoperate.util.ListUtil;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * create date : 2019/1/9
 */
public class Test {
    public static void main(String[] args) throws IOException {

        //设置文件集合
        //String[] fileArray = new File("D:\\iq\\otc\\candles\\" + 76).list();

        String[] fileArray = new String[]{"z","d","a","f"};
        List<String> list = Arrays.asList(fileArray);
        ListUtil.order(list,true);
        System.out.println(list);
    }
}
