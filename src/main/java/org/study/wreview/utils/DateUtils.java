package org.study.wreview.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {
    public static LocalDateTime getDateTimeAgo(){
        return LocalDateTime.now().minusYears(1);
    }

    public static int getDuration(LocalDate date){
        if(date == null){
            date = LocalDate.now();
        }
        LocalDate now = LocalDate.now();
        return date.until(now).getYears();
    }
}
