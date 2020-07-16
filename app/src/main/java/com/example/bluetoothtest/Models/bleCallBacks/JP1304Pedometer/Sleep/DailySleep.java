package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.Sleep;

import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity.DailyMeasure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * This class represents the sleep data of a day.<br />
 * <br />
 * The sleep data are stored like this:<br />
 * <ul>
 *     <li>
 *         {@code mLightSleepDuration[LAST_NIGHT]} contains the duration in minutes
 *         of light sleep between <b>00:00</b>:00.000 and <b>11:59</b>:59.999
 *     </li>
 *     <li>
 *         {@code mLightSleepDuration[TONIGHT]} contains the duration in minutes
 *         of light sleep between <b>12:00</b>:00.000 and <b>23:59</b>:59.999
 *     </li>
 *     <li>
 *         {@code mDeepSleepDuration[LAST_NIGHT]} contains the duration in minutes
 *         of deep sleep between <b>00:00</b>:00.000 and <b>11:59</b>:59.999
 *     </li>
 *     <li>
 *         {@code mDeepSleepDuration[TONIGHT]} contains the duration in minutes
 *         of deep sleep between <b>12:00</b>:00.000 and <b>23:59</b>:59.999
 *     </li>
 * </ul>
 */
public class DailySleep extends DailyMeasure {

    private static final int LAST_NIGHT = 0;
    private static final int TONIGHT = 1;

    public enum Period {
        LAST_NIGHT,
        TONIGHT,
    }

    private int[] mLightSleepDuration = new int[2];
    private int[] mDeepSleepDuration = new int[2];

    private int mQuality = 0;

	private List<SleepQuarterHour> mQuarters = new ArrayList<>();


    public DailySleep() {}

    public DailySleep(long date, String patientId, int quality) {
        mDate = date;
        mPatientId = patientId;
        mQuality = quality;
    }

    public void resetDailySleep() {

        mQuarters.clear();

        mLightSleepDuration[LAST_NIGHT] = 0;
        mDeepSleepDuration[LAST_NIGHT] = 0;

        mLightSleepDuration[TONIGHT] = 0;
        mDeepSleepDuration[TONIGHT] = 0;

        mQuality = 0;
    }

    public DailySleep initializeQuarters(){
        for (int i=0 ; i < 96 ; i++){
            int[] data = new int[8];
            for (int j=0 ; j < 8 ; j++){
                data[j] = 0;
            }
            SleepQuarterHour quarterHour = new SleepQuarterHour(new Date().getTime(), i , data );
            mQuarters.add(quarterHour);
        }
        return this;
    }

    @Override
    public void setDate(long date) {
        super.setDate(date);
        if(mQuarters != null) {
            for(SleepQuarterHour sleepQuarterHour: mQuarters) {
                sleepQuarterHour.setDate(date);
            }
        }
    }

    /**
     * Get the light sleep duration.
     * @return the duration (in minutes)
     */
    public int getLightSleepDuration(Period period) {
        return mLightSleepDuration[period.ordinal()];
    }

    /**
     * Set the light sleep duration.
     * @param period the period
     * @param duration the duration (in minutes)
     */
    public void setLightSleepDuration(Period period, int duration) {
        mLightSleepDuration[period.ordinal()] = duration;
    }

    /**
     * Get the deep sleep duration.
     * @return the duration (in minutes)
     */
    public int getDeepSleepDuration(Period period) {
        return mDeepSleepDuration[period.ordinal()];
    }

    /**
     * Set the deep sleep duration.
     * @param period the period
     * @param duration the duration (in minutes)
     */
    public void setDeepSleepDuration(Period period, int duration) {
        mDeepSleepDuration[period.ordinal()] = duration;
    }

    public List<SleepQuarterHour> getQuarterHourList() {
        return mQuarters;
    }

    public void setQuarterHourList(List<SleepQuarterHour> list) {
        mQuarters = list;
    }

    private boolean isDeepSleep(int movements) {
        return movements > 0 && movements <= 1;
    }

    private boolean isLightSleep(int movements) {
        return movements > 0 && movements > 1;
    }

    /**
     * Add a sleep quarter hour measure.<br />
     * Don't forget to compute the quality after adding all sleep quarter hour measures.
     * @param sleepQuarter the sleep quarter hour measure
     */
	public void addQuarterHour(SleepQuarterHour sleepQuarter) {

        mQuarters.add(sleepQuarter);

        for(int i = 0; i < 8; i++) {

            int movements = sleepQuarter.getData()[i];
            int minutesToAdd = (i < 7) ? 2 : 1;

            if (sleepQuarter.getNumber() < 48) {

                if (isDeepSleep(movements)) {
                    mDeepSleepDuration[LAST_NIGHT] += minutesToAdd;
                }
                else if (isLightSleep(movements)) {
                    mLightSleepDuration[LAST_NIGHT] += minutesToAdd;
                }
            }
            else {
                if (isDeepSleep(movements)) {
                    mDeepSleepDuration[TONIGHT] += minutesToAdd;
                }
                else if (isLightSleep(movements)) {
                    mLightSleepDuration[TONIGHT] += minutesToAdd;
                }
            }
        }
	}

	public void setupDurationSleep(SleepQuarterHour sleepQuarter){

	    for(int i = 0; i < 8; i++) {

            int movements = sleepQuarter.getData()[i];
            int minutesToAdd = (i < 7) ? 2 : 1;

            if (sleepQuarter.getNumber() < 48) {

                if (isDeepSleep(movements)) {
                    mDeepSleepDuration[LAST_NIGHT] += minutesToAdd;
                }
                else if (isLightSleep(movements)) {
                    mLightSleepDuration[LAST_NIGHT] += minutesToAdd;
                }
            }
            else {
                if (isDeepSleep(movements)) {
                    mDeepSleepDuration[TONIGHT] += minutesToAdd;
                }
                else if (isLightSleep(movements)) {
                    mLightSleepDuration[TONIGHT] += minutesToAdd;
                }
            }
        }
    }

    public int getQuality() {
        return mQuality;
    }

    public void setQuality(int quality) {
        mQuality = quality;
    }

    public void computeQuality() {

        if(mQuarters.size() <= 0) {
            mQuality = 0;
            return;
        }

        int sum = 0;
        for(SleepQuarterHour quarterHour: mQuarters){
            if(quarterHour!=null)
                sum += quarterHour.getMean();
        }


        mQuality = sum / mQuarters.size();
    }

    @Override
    public String toString() {
        return "DailySleep{" +
                super.toString() + " " +
                ", mDeepSleepDuration=" + Arrays.toString(mDeepSleepDuration) +
                ", mLightSleepDuration=" + Arrays.toString(mLightSleepDuration) +
                ", mQuality=" + mQuality +
                ", mQuarters=" + mQuarters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailySleep)) return false;

        DailySleep that = (DailySleep) o;

        if(!super.equals(o)) return false;

        if (mQuality != that.mQuality) return false;
        if (!Arrays.equals(mLightSleepDuration, that.mLightSleepDuration)) return false;
        if (!Arrays.equals(mDeepSleepDuration, that.mDeepSleepDuration)) return false;
        return mQuarters.equals(that.mQuarters);
    }
}
