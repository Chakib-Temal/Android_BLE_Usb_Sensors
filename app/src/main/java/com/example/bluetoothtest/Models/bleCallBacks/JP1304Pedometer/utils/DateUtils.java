package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils;

import java.util.Calendar;

/**
 * Helper functions for date.
 *
 * @version 1.4.0
 * @since 1.0.0
 */
public class DateUtils {

    public static final long MILLISECONDS_IN_ONE_DAY = 86400000; // 24*3600*1000;

    public static final boolean RESET_TIME = true;

    public static Calendar resetTime(Calendar calendar) {

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public static Calendar lastMillisOfDay(Calendar calendar) {

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar;
    }

    public static Calendar getCalendar(int year, int month, int day, int hour, int minute) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public static Calendar getCalendar(long timeInMillis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        return calendar;
    }

    public static Calendar getDaysAgo(int day) {
        return getDaysAgo(day, false);
    }

    public static Calendar getDaysAgo(int day, boolean resetTime) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -day);

        if(resetTime)
            calendar = resetTime(calendar);

        return calendar;
    }

    /**
     * Return the number of day covered by two dates.<br />
     * <br />
     * Examples:
     * <ul>
     *     <li>The number of day between a tuesday and the next wednesday is 2.</li>
     *     <li>The number of day between a monday and the next sunday wednesday is 7.</li>
     * </ul>
     * @param date1 the first date
     * @param date2 the second date
     * @return a number of day
     */
    public static int numberOfDaysCoveredByTwoDates(long date1, long date2) {

        if(date1 > date2) {
            long tmp = date1;
            date1 = date2;
            date2 = tmp;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date2);
        calendar = resetTime(calendar);
        date2 = calendar.getTimeInMillis();

        long diff = date2 - date1;
        int nbDays = (int) Math.ceil(1.0 * diff / MILLISECONDS_IN_ONE_DAY);
        return nbDays + 1;
    }

    /**
     * Returns the first day of a given week.
     * @param weekAgo the number of weeks in the past
     * @return a {@link Calendar}
     */
    public static Calendar getFirstDayOfWeek(int weekAgo) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -weekAgo);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        calendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

        calendar = resetTime(calendar);

        return calendar;
    }

    /**
     * Returns the last day of a given week.
     * @param weekAgo the number of weeks in the past
     * @return a {@link Calendar}
     */
    public static Calendar getLastDayOfWeek(int weekAgo) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -weekAgo);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        calendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

        calendar.add(Calendar.DAY_OF_MONTH, 7);

        calendar = resetTime(calendar);

        return calendar;
    }

    /**
     * Returns the last day of a given week.
     * @param beginTime begin time of the period (2 weeks)
     * @return a {@link Calendar}
     */
    public static Calendar getLastDayOfTwoWeek(long beginTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(beginTime);
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        calendar = resetTime(calendar);

        return calendar;
    }

    /**
     * Returns the first day of a given month.
     * @param monthAgo the number of months in the past
     * @return a {@link Calendar}
     */
    public static Calendar getFirstDayOfMonth(int monthAgo) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -monthAgo);

        return getFirstDayOfMonth(calendar);
    }

    /**
     * Returns the first day of month.
     * @param calendar a calendar
     * @return a {@link Calendar}
     */
    public static Calendar getFirstDayOfMonth(Calendar calendar) {

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar = resetTime(calendar);

        return calendar;
    }

    /**
     * Returns the number of days of a given month.
     * @param monthAgo the number of months in the past
     * @return
     */
    public static int getNumberOfDays(int monthAgo) {

        Calendar calendar = getFirstDayOfMonth(monthAgo);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static boolean isAtMidnight(Calendar calendar) {

        if(calendar.get(Calendar.HOUR_OF_DAY) != 0) return false;
        if(calendar.get(Calendar.MINUTE) != 0) return false;
        if(calendar.get(Calendar.SECOND) != 0) return false;
        if(calendar.get(Calendar.MILLISECOND) != 0) return false;

        return true;
    }

    public static int getDaysAgo(Calendar calendar) {

        Calendar now = Calendar.getInstance();
        now = resetTime(now);

        Calendar cal = (Calendar) calendar.clone();
        cal = resetTime(cal);

        int days = 0;
        if(cal.getTime().before(now.getTime())) {
            while (cal.getTime().before(now.getTime())) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                days++;
            }
        }
        else {
            while (now.getTime().before(cal.getTime())) {
                now.add(Calendar.DAY_OF_MONTH, 1);
                days--;
            }
        }

        return days;
    }

    public static int getMonthAgo(Calendar calendar) {

        Calendar now = Calendar.getInstance();
        now = DateUtils.getFirstDayOfMonth(now);

        Calendar cal = (Calendar) calendar.clone();
        cal = DateUtils.getFirstDayOfMonth(cal);

        int diffYear = cal.get(Calendar.YEAR) - now.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + cal.get(Calendar.MONTH) - now.get(Calendar.MONTH);

        return -diffMonth;
    }

    public static int getWeeksAgo(Calendar calendar) {

        // Set 'now' to the first day of week
        Calendar now = Calendar.getInstance();
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK) - now.getFirstDayOfWeek();
        if(dayOfWeek < 0)
            dayOfWeek += 7;
        now.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
        now = resetTime(now);

        // Set 'cal' to the first day of week
        Calendar cal = (Calendar) calendar.clone();
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - cal.getFirstDayOfWeek();
        if(dayOfWeek < 0)
            dayOfWeek += 7;
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
        cal = resetTime(cal);

        int weeks = 0;
        if(cal.getTime().before(now.getTime())) {
            while (cal.getTime().before(now.getTime())) {
                cal.add(Calendar.WEEK_OF_YEAR, 1);
                weeks++;
            }
        }
        else {
            while (now.getTime().before(cal.getTime())) {
                now.add(Calendar.WEEK_OF_YEAR, 1);
                weeks--;
            }
        }

        return weeks;
    }

    public static Boolean isInTheSameDay(Long date1, Long date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        return calendar1.getTimeInMillis() == calendar2.getTimeInMillis();
    }

    /**
     * Checks if calendar1 day and calendar2 day are same or not
     * @param calendar1 : input calendar1
     * @param calendar2 : input calendar2
     * @return :true if the calendar1 day is same as calendar2 day
     */
    public static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get (Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    public static Long getMin(Long date){
        Calendar calendar =  Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static Long getMax(Long date){
        Calendar calendar =  Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTimeInMillis();
    }

    public static Long add(Long date, int field, int nb) {
        Calendar calendar =  Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.add(field, nb);
        return calendar.getTimeInMillis();
    }
}
