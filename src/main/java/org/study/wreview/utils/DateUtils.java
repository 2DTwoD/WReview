package org.study.wreview.utils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date getDateAgo(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTime();
    }

    public static int getDuration(LocalDate date){
        if(date == null){
            date = LocalDate.now();
        }
        LocalDate now = LocalDate.now();
        return date.until(now).getYears();
    }
}
