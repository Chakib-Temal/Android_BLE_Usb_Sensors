package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity;


public class QuarterHour {

    protected long mDate = 0;
	protected int mNumber = 0;

    public QuarterHour() {}

    public QuarterHour(long date, int number) {
        mDate = date;
        mNumber = number;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    /**
     * Get the quarter hour number.<br />
     * <br />
     * <b>Note :</b>
     * <ul>
     *     <li>0 represents [0:00, 0:15[</li>
     *     <li>1 represents [0:15, 0:30[</li>
     *     <li>2 represents [0:30, 0:45[</li>
     *     <li>...</li>
     *     <li>95 represents [23:45, 0:00[</li>
     * </ul>
     * @return
     */
	public int getNumber() {
		return mNumber;
	}

    /**
     * Set the quarter hour number.<br />
     * <br />
     * <b>Note :</b>
     * <ul>
     *     <li>0 represents [0:00, 0:15[</li>
     *     <li>1 represents [0:15, 0:30[</li>
     *     <li>2 represents [0:30, 0:45[</li>
     *     <li>...</li>
     *     <li>95 represents [23:45, 0:00[</li>
     * </ul>
     * @param number the quarter hour number
     */
	public void setNumber(int number) {
		mNumber = number;
	}
}
