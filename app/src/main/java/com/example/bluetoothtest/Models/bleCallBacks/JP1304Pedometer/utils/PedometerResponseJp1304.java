package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils;

import android.util.Log;

import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.Sleep.DailySleep;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.Sleep.SleepQuarterHour;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity.ActivityQuarterHour;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity.DailyActivity;
import com.example.bluetoothtest.Models.bleCallBacks.sleep_band.Convert;

import java.util.Calendar;
import java.util.Date;

public class PedometerResponseJp1304 {

    public static final String TAG = PedometerResponseJp1304.class.getSimpleName();

    public PedometerResponseJp1304() {}

    public static PedometerResponseJp1304 newInstance(){
        return new PedometerResponseJp1304();
    }

    public static Date readTime(byte[] data) {
        Date date = new Date();
        date.setYear(Convert.byteInInt(data[1]) + 100);
        date.setMonth(Convert.byteInInt(data[2]) - 1);
        date.setDate(Convert.byteInInt(data[3]));
        date.setHours(Convert.byteInInt(data[4]));
        date.setMinutes(Convert.byteInInt(data[5]));
        date.setSeconds(Convert.byteInInt(data[6]));
        return date;
    }

    public static int readBatteryLevel(byte[] data) {
        return data[1] & 0xFF;
    }

    /**
     * Read detailed activity and sleep data.<br />
     * The data are send
     * @param data <b>IN</b> - the data received from the pedometer
     * @param day <b>IN</b> - the day passed as parameter when calling {@link PedometerCommand#getData(int day)}
     * @param activity <b>OUT</b> - the daily activity data
     * @param sleep <b>OUT</b> - the daily sleep data
     * @return {@code true if all data for a day have been read} or {@code false otherwise}
     */
    public static boolean readData(byte[] data, int day, DailyActivity activity, DailySleep sleep) {

        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] & 0xFF);
        }

        if (data[1] == (byte) 0xFF) {
            Log.d(TAG, "readData: day=" + day + "=> no data");

            activity.resetDailyActivity();
            sleep.resetDailySleep();

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.AM_PM, Calendar.AM);
            cal.set(Calendar.HOUR, 0);
            int dayInCal = cal.get(Calendar.DATE) - day;
            cal.set(Calendar.DATE, dayInCal);

            activity.setDate(cal.getTimeInMillis());
            sleep.setDate(cal.getTimeInMillis());

            return true;
        }

        int quarter = data[5];

        if (quarter == 0) {
            Log.d(TAG, "Reset daily activity and daily sleep");
            activity.resetDailyActivity();
            sleep.resetDailySleep();

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.AM_PM, Calendar.AM);
            cal.set(Calendar.HOUR, 0);
            int dayInCal = cal.get(Calendar.DATE) - day;
            cal.set(Calendar.DATE, dayInCal);

            activity.setDate(cal.getTimeInMillis());
            sleep.setDate(cal.getTimeInMillis());
        }

        switch (data[6]) {
            case (byte) 0x00:
                readActivityQuarter(data, day, activity);
                break;
            case (byte) 0xFF:
                readSleepQuarter(data, day, sleep);
                break;
        }

        if(quarter == 95) {
            sleep.computeQuality();
        }

        return quarter == 95;
    }


    public static void readActivityQuarter(byte[] data, int day, DailyActivity activity) {

        ActivityQuarterHour activityQuarter = new ActivityQuarterHour();

        int quarter = data[5];

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.HOUR, 0);
        int dayInCal = cal.get(Calendar.DATE) - day;
        cal.set(Calendar.DATE, dayInCal);

        activityQuarter.setDate(cal.getTimeInMillis());
        activityQuarter.setNumber(quarter);

        activityQuarter.setCalories(((float) Convert.twoByteToInt(data[7], data[8]) / 100));
        activityQuarter.setSteps(Convert.twoByteToInt(data[9], data[10]));
        activityQuarter.setDistance(((float) Convert.twoByteToInt(data[11], data[12]) * 10));
        activityQuarter.setRunningSteps(Convert.twoByteToInt(data[13], data[14]));

        activity.addQuarter(activityQuarter);
    }

    public static void readSleepQuarter(byte[] data, int day, DailySleep sleep) {

        SleepQuarterHour sleepQuarter = new SleepQuarterHour();

        int quarter = data[5];

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.HOUR, 0);
        int dayInCal = cal.get(Calendar.DATE) - day;
        cal.set(Calendar.DATE, dayInCal);

        int[] movement = new int[8];
        movement[0] = ((int) data[7] & 0xFF);
        movement[1] = ((int) data[8] & 0xFF);
        movement[2] = ((int) data[9] & 0xFF);
        movement[3] = ((int) data[10] & 0xFF);
        movement[4] = ((int) data[11] & 0xFF);
        movement[5] = ((int) data[12] & 0xFF);
        movement[6] = ((int) data[13] & 0xFF);
        movement[7] = ((int) data[14] & 0xFF);

        sleepQuarter.setDate(cal.getTimeInMillis());
        sleepQuarter.setNumber(quarter);
        sleepQuarter.setData(movement);
        sleepQuarter.computeMean();

        sleep.addQuarterHour(sleepQuarter);
    }

    //==============================================================================================
    // Methods for Real Time Data
    //==============================================================================================

    /*public static DailyActivity readSomedayTotalActivity(byte[] data) {
        int index = data[1];
        DailyActivity daily = DailyActivity.getDailyActivity();
        if (index == 0) {
            daily.resetDailyActivity();
            daily.setSteps(Convert.threeByteToInt(data[6], data[7], data[8]));
            daily.setmRunningSteps(Convert.threeByteToInt(data[9], data[10], data[11]));
            daily.setmCalories(Convert.threeByteToInt(data[12], data[13], data[14]) / 100);
            return null;
        } else {
            daily.setmDistance(Convert.threeByteToInt(data[6], data[7], data[8]) / 100);
        }
        return daily;
    }

    public static DailyActivity readRealTime(byte[] data) {
        DailyActivity daily = DailyActivity.getDailyActivity();
        daily.resetDailyActivity();
        daily.setSteps(Convert.threeByteToInt(data[1], data[2], data[3]));
        daily.setmRunningSteps(Convert.threeByteToInt(data[4], data[5], data[6]));
        daily.setmCalories(Convert.threeByteToInt(data[7], data[8], data[9]) / 100);
        daily.setmDistance(Convert.threeByteToInt(data[10], data[11], data[12]) * 10);
        return daily;
    }*/
}
