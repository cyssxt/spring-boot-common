package com.cyssxt.common.utils;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zqy on 18/05/2018.
 */
public class DateUtils {

    public final static String YYYYMMDD = "yyyyMMdd";
    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";
    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String HH_MM = "HH:mm";
    public static final String HHMM = "HHmm";

    public static Timestamp getCurrentTimestamp(){
        return new Timestamp(new Date().getTime());
    }

    public static Timestamp getCurrentTimestampBeforeHour(int hour){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,hour);
        return new Timestamp(calendar.getTime().getTime());
    }

    public static String getDataFormatString(Date date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    public static String format(Date timestamp,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(timestamp);
    }
    public static Date strToDate(String date,String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }
    public static Timestamp dateToTimestamp(Date date){
        return new Timestamp(date.getTime());
    }

    public static String getCurrentDateFormatStr(String format){
        return getDataFormatString(new Date(), format);
    }
}
