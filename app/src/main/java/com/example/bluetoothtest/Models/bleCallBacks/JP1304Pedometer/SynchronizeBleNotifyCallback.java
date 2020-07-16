package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer;

import android.util.Log;
import android.widget.Toast;

import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.bluetoothtest.MainActivity;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.Sleep.DailySleep;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity.DailyActivity;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils.DateFormat;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils.DateUtils;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils.PedometerCommand;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils.PedometerResponseJp1304;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils.ToastUtils;

import java.lang.ref.WeakReference;

/**
 * class Notify Pedometer EcareFit JP1304 for Synchronize Mode only
 *
 * @version 1.3.0
 * @since 1.3.0
 */
public class SynchronizeBleNotifyCallback extends BleNotifyCallback {
    public final static String TAG = SynchronizeBleNotifyCallback.class.getSimpleName();

    private WeakReference<PedometerBleGattCallback> mActivity = null;

    public SynchronizeBleNotifyCallback(PedometerBleGattCallback activity) {
        mActivity = new WeakReference<>(activity);
    }

    public PedometerBleGattCallback getActivity(){
        return mActivity.get();
    }

    public BleDevice getDevice(){
        return getActivity().getDevice();
    }

    public int getDaysAgo(){
        return getActivity().getDaysAgo();
    }

    public DailyActivity getDailyActivity(){
        return getActivity().getDailyActivity();
    }

    public DailySleep getDailySleep(){
        return getActivity().getDailySleep();
    }

    @Override
    public void onNotifySuccess() {
        Log.i(TAG, "onNotifySuccess Synchronize mode");
        MainActivity.writeTextSucces("Set Time Podometre " + mActivity.get().getDevice().getName() + " " + mActivity.get().getDevice().getName() );
        getActivity().setTime(getDevice());
    }

    @Override
    public void onNotifyFailure(BleException exception) {
        Log.e(TAG, "onNotifyFailure Synchronize mode: " + exception.getDescription());
    }

    @Override
    public void onCharacteristicChanged(byte[] data) {
        Log.d(TAG, "onCharacteristicChanged Synchronize mode");

        switch (data[0]) {
            case PedometerCommand.CMD_GET_DATA:
                MainActivity.writeTextSucces("Getting Data Podometre " + mActivity.get().getDevice().getName() + " " + mActivity.get().getDevice().getName() );

                boolean isDataComplete = PedometerResponseJp1304.readData(data, getDaysAgo(), getDailyActivity(), getDailySleep());
                // Log.v(TAG, "Get data: day=" + mDaysAgo + " isDataComplete=" + isDataComplete);

                if (isDataComplete) {
                    String date = DateFormat.format(DateUtils.getDaysAgo(getDaysAgo()), DateFormat.dd_MM_yyyy);
                    Log.d(TAG, "Get data: mDaysAgo=" + getDaysAgo() + " date=" + date);
                    ToastUtils.toast("Jour " + date + " synchronisé, patientez svp !", Toast.LENGTH_SHORT, false);

                    getActivity().addNewMeasureToList(getDailyActivity(), getDailySleep());

                    if (getDaysAgo() > 0) {
                        getActivity().setDaysAgo(getDaysAgo() - 1);
                        getActivity().getData(getDevice(), getDaysAgo());
                    } else {
                        getActivity().saveMeasures();

                        Log.i(TAG, "Get data: END");
                        ToastUtils.toast("Synchronisation terminée", Toast.LENGTH_SHORT, false);
                        MainActivity.writeTextSucces("fin synchronisattion Podometre, aller dans /Download ,  " );

                        getActivity().vibrate(getDevice());
                    }
                }
                break;

            case PedometerCommand.CMD_SET_TIME:
                Log.d(TAG, "Time set");
                MainActivity.writeTextSucces("Set Time  OK Podometre " + mActivity.get().getDevice().getName() + " " + mActivity.get().getDevice().getName() );

                getActivity().startSync(getDevice());
                break;

        }
    }
}
