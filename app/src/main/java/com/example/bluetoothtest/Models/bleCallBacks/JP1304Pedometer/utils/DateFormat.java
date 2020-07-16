package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A helper class that contains a list of predefined date/time.
 * @version 1.0.0
 */
public class DateFormat {

    //mardi
    public static String EEEE = "EEEE";
    //15:30
    public static String HH_mm = "HH:mm";
    //décembre 2018
    public static String MMMM_yyyy = "MMMM yyyy";
    //25/11/17
    public static String dd_MM_yy = "dd/MM/yy";
    //25/11/2017
    public static String dd_MM_yyyy = "dd/MM/yyyy";
    public static String dd_MMMM_yyyy = "dd MMMM yyyy";
    public static String dd_MM_yyyy_HH_mm = "dd/MM/yyyy HH:mm";
    public static String dd_MM_yyyy_HH_mm_ss_SSS = "dd/MM/yyyy HH:mm:ss:SSS";
    //mardi 25/12/2012 00:00:00:000
    public static String EEEE_dd_MM_yyyy = "EEEE dd/MM/yyyy";
    public static String EEEE_dd_MMMM_yyyy = "EEEE dd MMMM yyyy";
    public static String EEEE_dd_MM_yy_HH_mm_ss_SSS = "EEEE dd/MM/yy HH:mm:ss:SSS";

    public static String format(Calendar calendar, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(calendar.getTime());
    }

    public static String format(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date.getTime());
    }

    public static String format(long timestamp, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(calendar.getTime());
    }

    public static String formatTime(int hour, int minute) {
        return String.format("%02d", hour) + ':' + String.format("%02d", minute);
    }
}
