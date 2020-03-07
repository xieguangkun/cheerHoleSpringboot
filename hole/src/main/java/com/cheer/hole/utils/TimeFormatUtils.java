package com.cheer.hole.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

//将timestamp转化为毫秒
public class TimeFormatUtils {
    public long getMillionSeconds(Timestamp timestamp){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        long date = System.currentTimeMillis();

        String time = format.format(timestamp);
        try {
            long millionSeconds = format.parse(time).getTime();
            return timestamp.getTime()-28800000;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
