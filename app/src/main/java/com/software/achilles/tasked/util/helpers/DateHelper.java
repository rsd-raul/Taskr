package com.software.achilles.tasked.util.helpers;

import java.util.Calendar;
import java.util.Date;

public abstract class DateHelper {

    public static Date getStartOfDay(Calendar date){
        if(date == null)
            date = Calendar.getInstance();

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);

        return date.getTime();
    }

    public static Date getEndOfDay(Calendar date){
        if(date == null)
            date = Calendar.getInstance();

        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);

        return date.getTime();
    }

    public static Date getNextWeek(Calendar date){
        if(date == null)
            date = Calendar.getInstance();

        date.add(Calendar.DATE, 7);

        return date.getTime();
    }
}
