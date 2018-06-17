package com.dentalclinic.capstone.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    /**
     * @param source
     * @param srcFormat
     * @param targetFormat
     * @return
     */
    public static String changeDateFormat(String source, DateTimeFormat srcFormat, DateTimeFormat targetFormat){
        try {
            SimpleDateFormat smpSrc = new SimpleDateFormat(srcFormat.toString(), Locale.US);
            Date srcDate = smpSrc.parse(source);
            SimpleDateFormat smpTarget = new SimpleDateFormat(targetFormat.toString(), Locale.US);
            String targetDate = smpTarget.format(srcDate);
            return targetDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @param source
     * @param srcFormat
     * @return
     */
    public static Date getDate(String source, DateTimeFormat srcFormat){
        try {
            SimpleDateFormat smpSrc = new SimpleDateFormat(srcFormat.toString(), Locale.US);
            Date targetDate = smpSrc.parse(source);
            return targetDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDate(Date source, DateTimeFormat targetFormat){
        SimpleDateFormat smpTarget = new SimpleDateFormat(targetFormat.toString(), Locale.US);
        String targetDateStr = smpTarget.format(source);
        return targetDateStr;
    }

    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        return getDate(c.getTime(),DateTimeFormat.DATE_APP);
    }
}
