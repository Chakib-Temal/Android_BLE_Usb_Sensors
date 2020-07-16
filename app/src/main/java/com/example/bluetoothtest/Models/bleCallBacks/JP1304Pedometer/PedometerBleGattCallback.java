package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer;

import android.bluetooth.BluetoothGatt;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.bluetoothtest.MainActivity;
import com.example.bluetoothtest.Models.bleCallBacks.BleModelCallBack;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.Sleep.DailySleep;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.Sleep.SleepQuarterHour;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity.ActivityQuarterHour;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.activity.DailyActivity;
import com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils.PedometerCommand;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Service that handles Bluetooth communications with J-Style JP-1304 and EcareFit pedometers.
 *
 * @version 1.3.0
 * @since 1.1.0
 */
public class PedometerBleGattCallback extends BleModelCallBack {

    public final static String TAG = PedometerBleGattCallback.class.getSimpleName();


    public static final String SERVICE_UUID = "0000FFF0-0000-1000-8000-00805f9b34fb";
    public static final String RX_CHAR_UUID = "0000FFF7-0000-1000-8000-00805f9b34fb";
    private static final String TX_CHAR_UUID = "0000FFF6-0000-1000-8000-00805f9b34fb";

    private byte[] mLastCommand;

    private int mDaysAgo = 10;

    private DailyActivity mDailyActivity;
    private DailySleep mDailySleep;

    private BleNotifyCallback mBleNotifyCallback;

    protected String getTAG() {
        return TAG;
    }


    protected String getServiceUUID() {
        return SERVICE_UUID;
    }

    protected String getRxCharUUID() {
        return RX_CHAR_UUID;
    }

    protected String getTxCharUUID() {
        return TX_CHAR_UUID;
    }

    //==============================================================================================
    // Custom event methods
    //==============================================================================================

    @Override
    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
        super.onDisConnected(isActiveDisConnected, device, gatt, status);
        MainActivity.writeTextSucces("Fin de connexion le podometre");
    }

    @Override
    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
        mDevice = bleDevice;
        MainActivity.writeTextSucces("Connexion établie avec le Podometre " + bleDevice.getName() + " " + bleDevice.getMac());

        dailyActivityList = new ArrayList<>();
        dailySleepList = new ArrayList<>();

        preparePedometerModes();
    }

    //==============================================================================================
    // Global Functions
    //==============================================================================================

    public int getDaysAgo() {
        return mDaysAgo;
    }

    public void setDaysAgo(int mDaysAgo) {
        this.mDaysAgo = mDaysAgo;
    }

    public BleDevice getDevice(){
        return mDevice;
    }


    public DailyActivity getDailyActivity() {
        return mDailyActivity;
    }

    public DailySleep getDailySleep() {
        return mDailySleep;
    }

    //==============================================================================================
    // Methods
    //==============================================================================================

    List<DailyActivity> dailyActivityList = new ArrayList<>();
    List<DailySleep> dailySleepList = new ArrayList<>();

    public void addNewMeasureToList(DailyActivity dailyActivity, DailySleep dailySleep){
        dailyActivityList.add(dailyActivity);
        dailySleepList.add(dailySleep);
    }

    public void saveMeasures() {
        Log.i(TAG, "New measures received");


            try {
                saveActivityData();
            } catch (Exception e) {
                e.printStackTrace();
                File activityDataFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/ActivityErrorPedometer.csv");


                try {
                    activityDataFile.createNewFile();
                    FileWriter activityDataFileWriter = new FileWriter(activityDataFile);
                    activityDataFileWriter.append(e.getMessage());

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }


        try {
                saveSleepData();
            } catch (IOException e) {
                e.printStackTrace();
            e.printStackTrace();
            File activityDataFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/SleepErrorPedometer.csv");


            try {
                activityDataFile.createNewFile();
                FileWriter activityDataFileWriter = new FileWriter(activityDataFile);
                activityDataFileWriter.append(e.getMessage());

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            }


    }

    public void saveActivityData() throws Exception {

        File activityDataFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/ActivityPedometer.csv");


        activityDataFile.createNewFile();
        FileWriter activityDataFileWriter = new FileWriter(activityDataFile);

        for (DailyActivity dailyActivity : dailyActivityList) {

            Date dailyActivityDate = new Date();
            dailyActivityDate.setTime(dailyActivity.getDate());


            String dateMeasureFormated = new SimpleDateFormat
                    ("yyyy-MM-dd HH:mm:ss ").format(dailyActivityDate);

            activityDataFileWriter.append("Total journée : " + dateMeasureFormated + " ;");
            activityDataFileWriter.append("Distance : " + dailyActivity.getDistance() + " ;");
            activityDataFileWriter.append("Calorie : " + dailyActivity.getCalories() + " ;");
            activityDataFileWriter.append("Steps : " + dailyActivity.getSteps() + " ;");
            activityDataFileWriter.append("\n \n");


            for (ActivityQuarterHour quarterHour : dailyActivity.getQuarterHourList()){

                Date quarterHourDate = new Date();
                quarterHourDate.setTime(quarterHour.getDate());

                String dateQuarterHourFormated = new SimpleDateFormat
                        ("yyyy-MM-dd HH:mm:ss ").format(quarterHourDate);

                activityDataFileWriter.append(quarterHour.getNumber() + " - " + dateQuarterHourFormated);
                activityDataFileWriter.append(quarterHour.getDistance() + " ;");

                activityDataFileWriter.append(dailyActivity.getCalories() + " ;");
                activityDataFileWriter.append(dailyActivity.getSteps() + " ;");
                activityDataFileWriter.append("\n");
            }
            activityDataFileWriter.append("\n \n");
        }

        activityDataFileWriter.flush();
        activityDataFileWriter.close();

    }

    public void saveSleepData() throws IOException {

        File sleepDataFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/SleepPedometer.csv");


        sleepDataFile.createNewFile();
        FileWriter sleepDataFileWriter = new FileWriter(sleepDataFile);

        for (DailySleep dailySleep : dailySleepList) {

            Date sleepActivityDate = new Date();
            sleepActivityDate.setTime(dailySleep.getDate());


            String dateMeasureFormated = new SimpleDateFormat
                    ("yyyy-MM-dd HH:mm:ss ").format(sleepActivityDate);

            sleepDataFileWriter.append("Total journée : " + dateMeasureFormated + " ;");

            int deepSleep = dailySleep.getDeepSleepDuration(DailySleep.Period.LAST_NIGHT) +
                    dailySleep.getDeepSleepDuration(DailySleep.Period.TONIGHT);

            sleepDataFileWriter.append("Deep Sleep : " + deepSleep + " ;");
            sleepDataFileWriter.append("Quality : " + dailySleep.getQuality() + " ;");
            sleepDataFileWriter.append("\n ");


            for (SleepQuarterHour quarterHour : dailySleep.getQuarterHourList()) {

                Date quarterHourDate = new Date();
                quarterHourDate.setTime(quarterHour.getDate());

                String dateQuarterHourFormated = new SimpleDateFormat
                        ("yyyy-MM-dd HH:mm:ss ").format(quarterHourDate);

                sleepDataFileWriter.append(quarterHour.getNumber() + " - " + dateQuarterHourFormated);
                sleepDataFileWriter.append(quarterHour.getMean() + " ;");
                sleepDataFileWriter.append("\n");
            }

            sleepDataFileWriter.append("\n \n");
        }
        sleepDataFileWriter.flush();
        sleepDataFileWriter.close();

    }


    public void startSync(final BleDevice device) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "startSync()");
                getData(device,mDaysAgo);
            }
        }, 1000);

    }

    public void getData(BleDevice device, int day) {
        Log.d(TAG, "getData(): day=" + day);
        mDailyActivity = new DailyActivity();
        mDailySleep = new DailySleep();
        mLastCommand = PedometerCommand.getData(day);
        writeRXCharacteristic(device,mLastCommand);
    }

    public void setTime(BleDevice device) {
        Log.d(TAG, "setTime()");
        mLastCommand = PedometerCommand.setTime();
        writeRXCharacteristic(device,mLastCommand);
    }

    public void vibrate(BleDevice device) {
        Log.d(TAG, "vibrate()");
        mLastCommand = PedometerCommand.vibrate();
        writeRXCharacteristic(device,mLastCommand);
    }

    //==============================================================================================
    // Changing Pedometer Mode functions
    //==============================================================================================

    public void preparePedometerModes(){
        changeToSynchronizeMode();
    }

    public void changeToSynchronizeMode(){
        mBleNotifyCallback = new SynchronizeBleNotifyCallback(this);
        setModePedometer();
    }

    public void setModePedometer(){
        BleManager.getInstance().notify(mDevice, SERVICE_UUID, RX_CHAR_UUID, mBleNotifyCallback );
    }

    public void writeRXCharacteristic(final BleDevice device, final byte[] cmd) {
        BleManager.getInstance().write(device
                , getServiceUUID()
                , getTxCharUUID()
                , cmd
                , new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        Log.i(getTAG(), "onWriteSuccess");
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        Log.i(getTAG(), "onWriteFailure");
                        // writeRXCharacteristic(device,cmd);
                    }
                });
    }


}