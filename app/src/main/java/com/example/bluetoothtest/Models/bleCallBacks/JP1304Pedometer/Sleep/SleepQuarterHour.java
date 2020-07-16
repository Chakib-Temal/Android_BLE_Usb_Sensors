package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.Sleep;

import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity.QuarterHour;

import java.util.Arrays;


/**
 * This class represents the sleep data of a quarter hour.<br />
 * <br />
 * The sleep data are stored like this:<br />
 * <ul>
 *     <li>{@code mData[0]} contains the data for the <b>1st</b> and <b>2nd</b> minute</li>
 *     <li>{@code mData[1]} contains the data for the <b>3rd</b> and <b>4th</b> minute</li>
 *     <li>{@code mData[2]} contains the data for the <b>5th</b> and <b>6th</b> minute</li>
 *     <li>{@code mData[3]} contains the data for the <b>7th</b> and <b>8th</b> minute</li>
 *     <li>{@code mData[4]} contains the data for the <b>9th</b> and <b>10th</b> minute</li>
 *     <li>{@code mData[5]} contains the data for the <b>11th</b> and <b>12th</b> minute</li>
 *     <li>{@code mData[6]} contains the data for the <b>13th</b> and <b>14th</b> minute</li>
 *     <li>{@code mData[7]} contains the data for the <b>15th</b> minute</li>
 * </ul>
 *
 */
public class SleepQuarterHour extends QuarterHour {

    public static final boolean COMPUTE_MEAN = true;
    public static final boolean DONT_COMPUTE_MEAN = false;

    private int[] mData;

    private int mMean = -1;

    public SleepQuarterHour() {}

	public SleepQuarterHour(long date, int quarterHour, int[] data) {
        super(date, quarterHour);
        setData(data);
	}

    /**
     * Get the sleep data of the quarter hour.<br />
     * See the {@link SleepQuarterHour} documentation which explains the meaning of the return value.
     * @return the sleep data
     */
    public int[] getData() {
        return mData;
    }

    /**
     * Set the sleep data of the quarter hour and compute the mean.
     * See the {@link SleepQuarterHour} documentation which explains the meaning of {@code data} parameter.
     * @param data the sleep data
     */
    public void setData(int[] data) {
        setData(data, COMPUTE_MEAN);
    }

    /**
     * Set the sleep data of the quarter hour and optionnaly compute the mean.
     * See the {@link SleepQuarterHour} documentation which explains the meaning of {@code data} parameter.
     * @param data the sleep data
     * @param computeMean {@link #COMPUTE_MEAN} or {@link #DONT_COMPUTE_MEAN}
     */
    public void setData(int[] data, boolean computeMean) {

        if(data.length != 8)
            throw new IllegalArgumentException("data must be a int[8] array");

        mData = data;

        if(computeMean)
            computeMean();
    }

    public void computeMean() {
        mMean = 0;
        for(int i = 0; i < 8; i++)
            mMean += mData[i];
        mMean = mMean / 8;
    }

    public int getMean() {
        return mMean;
    }

    public void setMean(int mean) {
        mMean = mean;
    }

    @Override
    public String toString() {
        return "SleepQuarterHour{" +
                "mData=" + Arrays.toString(mData) +
                ", mMean=" + mMean +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SleepQuarterHour)) return false;

        SleepQuarterHour that = (SleepQuarterHour) o;

        if (mMean != that.mMean) return false;
        return Arrays.equals(mData, that.mData);
    }
}
