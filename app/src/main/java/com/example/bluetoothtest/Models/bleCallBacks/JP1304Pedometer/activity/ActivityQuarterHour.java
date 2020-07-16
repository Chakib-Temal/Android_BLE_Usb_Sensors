package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity;

/**
 * This class represents the activity data of a quarter hour.
 */
public class ActivityQuarterHour extends QuarterHour {

	private int mSteps;
	private int mRunningSteps;
	private float mCalories;
	private float mDistance;

	public ActivityQuarterHour(long date, int number, int steps, int runningSteps, float calories, float distance) {
        super(date, number);
        mSteps = steps;
        mRunningSteps = runningSteps;
        mCalories = calories;
        mDistance = distance;
	}

    public ActivityQuarterHour() {
        super(0, 0);
    }

	public ActivityQuarterHour(int steps, int runningSteps, float calories, float distance) {
        this(0, 0, steps, runningSteps, calories, distance);
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

    @Override
    public String toString() {
        return "ActivityQuarterHour{" +
				"mNumber=" + mNumber +
                ", mCalories=" + mCalories +
                ", mSteps=" + mSteps +
                ", mRunningSteps=" + mRunningSteps +
                ", mDistance=" + mDistance +
                '}';
    }
}
