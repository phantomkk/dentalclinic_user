package com.dentalclinic.capstone.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
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
    public static Date getDate(Date source, DateTimeFormat targetFormat){
        SimpleDateFormat smpTarget = new SimpleDateFormat(targetFormat.toString(), Locale.US);
        String targetDateStr = smpTarget.format(source);
        Date targetDate = null;
        try {
            targetDate = smpTarget.parse(targetDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return targetDate;
    }
}
