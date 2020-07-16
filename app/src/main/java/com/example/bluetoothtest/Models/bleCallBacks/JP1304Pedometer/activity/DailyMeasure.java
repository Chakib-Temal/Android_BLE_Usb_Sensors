package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity;

/**
 * A base class for daily measure
 */
public class DailyMeasure {

    protected long mIdLocal;
    protected String mIdServer;
    protected String mPatientId;
    protected String mPhysicianId;
    protected long mDate;
    private boolean mSynchronized = false;

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    @Override
    public String toString() {
        return "DailyMeasure{" +
                "mIdLocal=" + mIdLocal +
                ", mIdServer='" + mIdServer + '\'' +
                ", mPatientId='" + mPatientId + '\'' +
                ", mPhysicianId='" + mPhysicianId + '\'' +
                ", mDate=" + mDate +
                ", mSynchronized=" + mSynchronized +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyMeasure)) return false;

        DailyMeasure that = (DailyMeasure) o;

        if (mDate != that.mDate) return false;
        if (mSynchronized != that.mSynchronized) return false;
        if (!mIdServer.equals(that.mIdServer)) return false;
        if (!mPatientId.equals(that.mPatientId)) return false;
        return mPhysicianId != null ? mPhysicianId.equals(that.mPhysicianId) : that.mPhysicianId == null;

    }
}
