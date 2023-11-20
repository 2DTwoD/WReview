package org.study.wreview.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date getDateAgo(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTime();
    }

    public static int getDuration(Date date){
        if(date == null){
            date = new java.sql.Date(System.currentTimeMillis());
        }
        LocalDate lDate1 = LocalDate.now();
        LocalDate lDate2 = ((java.sql.Date)date).toLocalDate();
        return lDate2.until(lDate1).getYears();
    }
}