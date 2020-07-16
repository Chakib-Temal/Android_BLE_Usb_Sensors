package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyActivity extends DailyMeasure {

    private int mSteps = 0;
    private int mRunningSteps = 0;
    private float mCalories = 0;
    private float mDistance = 0;

    private List<ActivityQuarterHour> mQuarters = new ArrayList<>();

    public DailyActivity() {}

    public DailyActivity(long date, String patientId, int steps, int runningSteps, float calories, float distance) {
        mDate = date;
        mPatientId = patientId;
        mSteps = steps;
        mRunningSteps = runningSteps;
        mCalories = calories;
        mDistance = distance;
    }

    public void resetDailyActivity() {
        mQuarters.clear();
        mSteps = 0;
        mRunningSteps = 0;
        mCalories = 0;
        mDistance = 0;
    }

    public DailyActivity initializeQuarters(){
        for (int i=0 ; i < 96 ; i++){
            ActivityQuarterHour quarterHour = new ActivityQuarterHour(new Date().getTime(), i ,0 ,0 ,0 ,0 );
            mQuarters.add(quarterHour);
        }
        return this;
    }

    @Override
    public void setDate(long date) {
        super.setDate(date);
        if(mQuarters != null) {
            for(ActivityQuarterHour activity: mQuarters) {
                activity.setDate(date);
            }
        }
    }

    public int getSteps() {
        return mSteps;

    }
    public void setSteps(int steps) {
        mSteps = steps;
    }

    public int getRunningSteps() {
        return mRunningSteps;
    }

    public void setRunningSteps(int runningSteps) {
        mRunningSteps = runningSteps;
    }

    public float getCalories() {
        return mCalories;
    }

    public void setCalories(float calories) {
        mCalories = calories;
    }

    public float getDistance() {
        return mDistance;
    }

    public void setDistance(float distance) {
        mDistance = distance;
    }

    public List<ActivityQuarterHour> getQuarterHourList() {
        return mQuarters;
    }

    public void setQuarterHourList(List<ActivityQuarterHour> list) {
        mQuarters = list;
    }

    public void addQuarter(ActivityQuarterHour activityQuarter) {

        mQuarters.add(activityQuarter);
        mSteps += activityQuarter.getSteps();
        mRunningSteps += activityQuarter.getRunningSteps();
        mCalories += activityQuarter.getCalories();
        mDistance += activityQuarter.getDistance();
    }

    @Override
    public String toString() {
        return "DailyActivity{" +
                super.toString() +
                ", mSteps=" + mSteps +
                ", mRunningSteps=" + mRunningSteps +
                ", mCalories=" + mCalories +
                ", mDistance=" + mDistance +
                ", mQuarters.size()=" + mQuarters.toString() +
                '}';
    }
}
