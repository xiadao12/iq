package com.zcy.iqoperate.temp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcy.iqoperate.model.CandleMessage;
import com.zcy.iqoperate.model.response.CandlesResponse;
import com.zcy.iqoperate.util.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * create date : 2019/1/9
 */
public class Test {
    public static void main(String[] args) throws IOException {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1550278740000L);

        Integer ww = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(calendar.get(Calendar.YEAR));

    }
}
