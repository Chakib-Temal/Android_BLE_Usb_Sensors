package com.example.bluetoothtest.Models.bleCallBacks.sleep_band;

import android.bluetooth.BluetoothGatt;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.bluetoothtest.MainActivity;
import com.example.bluetoothtest.Models.bleCallBacks.BleModelCallBack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Service that handle bluetooth communications with the thermometer.
 *
 * @version 1.3.0
 * @since 1.1.0
 */
public class SleepBandBleCallBack extends BleModelCallBack implements OnProcessFinished  {

    public final static String TAG = SleepBandBleCallBack.class.getSimpleName();

    public static String SERVICE_UUID  = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static String SERVICE_RX  = "0000fff7-0000-1000-8000-00805f9b34fb";
    public static String SERVICE_TX  = "0000fff6-0000-1000-8000-00805f9b34fb";
    public static OnProcessFinished mListener;


    private byte[] mLastCommand;

    private int groupHeart = 0;
    private int groupOutOfBed = 0;
    private int groupRolloverStorage = 0;

    @Override
    public void onStartConnect() {

    }

    @Override
    public void onConnectFail(BleDevice bleDevice, BleException exception) {

    }

    @Override
    public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
        mListener = this;

        mDevice = bleDevice;

        MainActivity.writeTextSucces("Connexion établie avec la ceinture, Synchronisation ...");

        BleManager.getInstance().notify(mDevice
                , SERVICE_UUID
                , SERVICE_RX
                , new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        Log.d(TAG,"onNotifySuccess");

                        J1657_Service.initializeList();
                        mLastCommand = setTime();
                        writeRXCharacteristic(mLastCommand);
                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                        Log.d(TAG,"onNotifyFailure");
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        int byte0 = data[0] & 0xFF;
                        Log.i(TAG, Arrays.toString(data));

                        switch (byte0){

                            case CMD_SET_TIME:
                                mLastCommand = getHeartRateBreathingStorageData(groupHeart);
                                writeRXCharacteristic(mLastCommand);
                                break;

                            case CMD_GET_HEART_RATE_BREATHING_STORAGE_DATA:
                                int byte1 = data[1] & 0xFF;

                                if (byte1 == 99){

                                    J1657_Service.groupDataByteList.add(data);

                                    groupHeart++;

                                    ArrayList<byte[]> newList = (ArrayList<byte[]>) J1657_Service.groupDataByteList.clone();
                                    J1657_Service.groupListDataByteList.add(newList);
                                    J1657_Service.groupDataByteList = new ArrayList<>();

                                    mLastCommand = getHeartRateBreathingStorageData(groupHeart);
                                    writeRXCharacteristic(mLastCommand);
                                    break;
                                }
                                else if (byte1 == 255){
                                    J1657_Service.processData();
                                    Log.i(TAG, "finish heart rate , breath data");
                                    break;
                                }
                                J1657_Service.groupDataByteList.add(data);

                                break;
                            case CMD_GET_OUT_OF_BED:
                                byte1 = data[1] & 0xFF;

                                if (byte1 == 3){
                                    J1657_Service.groupOutOfBedDataByteList.add(data);

                                    groupOutOfBed++;

                                    ArrayList<byte[]> newList = (ArrayList<byte[]>) J1657_Service.groupOutOfBedDataByteList.clone();
                                    J1657_Service.groupListOutOfBedDataByteList.add(newList);
                                    J1657_Service.groupOutOfBedDataByteList = new ArrayList<>();

                                    mLastCommand = getOutOfBedData(groupOutOfBed);
                                    writeRXCharacteristic(mLastCommand);
                                    break;
                                }
                                else if (byte1 == 255 || data[10] == 0){
                                    J1657_Service.processDataOutOfBed();
                                    Log.i(TAG, "finish out of bed data");
                                    break;
                                }

                                J1657_Service.groupOutOfBedDataByteList.add(data);

                                break;

                            case CMD_GET_ROLLOVER_STORAGE:
                                byte1 = data[1] & 0xFF;

                                if (byte1 == 49){

                                    J1657_Service.groupRolloverStorageDataByteList.add(data);
                                    groupRolloverStorage++;

                                    ArrayList<byte[]> newList = (ArrayList<byte[]>) J1657_Service.groupRolloverStorageDataByteList.clone();
                                    J1657_Service.groupListRolloverStorageDataByteList.add(newList);
                                    J1657_Service.groupRolloverStorageDataByteList = new ArrayList<>();

                                    mLastCommand = getRolloverStorageData(groupRolloverStorage);
                                    writeRXCharacteristic( mLastCommand);
                                    break;
                                }
                                else if (byte1 == 255 || data[10] == 0){
                                    J1657_Service.processRolloverStorage();
                                    Log.i(TAG, "finish Rollover storage data");
                                    break;
                                }
                                J1657_Service.groupRolloverStorageDataByteList.add(data);

                                break;

                            case CMD_SET_REAL_TIME_TEMP_HUMID:

                                Log.i(TAG, "//");
                                //mLastCommand = setTemperatureAndHumidityRealTimeData();
                                //writeRXCharacteristic( mLastCommand);
                                break;

                        }
                    }
                });

    }

    @Override
    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

    }

    public static void writeRXCharacteristic(final byte[] cmd) {

        Log.i("SendedMessages   ", Arrays.toString(cmd));

        BleManager.getInstance().write(mDevice
                , SERVICE_UUID
                , SERVICE_TX
                , cmd
                , new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        Log.i(TAG, "onWriteSuccess");
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        Log.i(TAG, "onWriteFailure");
                    }
                });
    }


    @Override
    public void onHeartRateBreathingStorageDataFinished() {
        mLastCommand = getOutOfBedData(groupOutOfBed);
        writeRXCharacteristic(mLastCommand);
    }

    @Override
    public void onOutOfBedStorageDataFinished() {
        mLastCommand = getRolloverStorageData(groupRolloverStorage);
        writeRXCharacteristic(mLastCommand);
    }

    @Override
    public void onRolloverStorageDataFinished() {
        mLastCommand = setTemperatureAndHumidityRealTimeData();
        MainActivity.writeTextSucces("synchronisation terminées ... aller dans /Documents pour trouver les fichiers");
        writeRXCharacteristic(mLastCommand);
    }


    public static final int CMD_SET_REAL_TIME_TEMP_HUMID = 0x0d;
    public static byte[] setTemperatureAndHumidityRealTimeData() {

        byte[] command = new byte[16];
        command[0]=(byte) CMD_SET_REAL_TIME_TEMP_HUMID;

        command[1] = (byte) 1;
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);
        return command;
    }

    public static final int CMD_GET_ROLLOVER_STORAGE                   = 0x15;
    public static byte[] getRolloverStorageData(int group) {

        byte[] command = new byte[16];
        command[0]=(byte) CMD_GET_ROLLOVER_STORAGE;

        command[1] = (byte) group;
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);
        return command;
    }

    public static final int CMD_GET_OUT_OF_BED                   = 0x14;
    public static byte[] getOutOfBedData(int group) {

        byte[] command = new byte[16];
        command[0]=(byte) CMD_GET_OUT_OF_BED;

        command[1] = (byte) group;
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);
        return command;
    }

    public static final int CMD_GET_HEART_RATE_BREATHING_STORAGE_DATA                    = 0x17;
    public static byte[] getHeartRateBreathingStorageData(int group) {

        byte[] command = new byte[16];
        command[0]=(byte) CMD_GET_HEART_RATE_BREATHING_STORAGE_DATA;

        command[1] = (byte) group;
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);
        return command;
    }


    public static final int CMD_GET_TIME             = 0x41;
    public static byte[] getTime() {

        byte[] command = new byte[16];
        command[0] = (byte) CMD_GET_TIME;

        command[1] = (byte) 0; //0, 1 , 2
        command[2] = (byte) 0;
        command[3] = (byte) 0;
        command[4] = (byte) 0;
        command[5] = (byte) 0;
        command[6] = (byte) 0;
        command[7] = (byte) 0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);
        return command;
    }

    public static final int CMD_SET_TIME                             = 0x01;

    public static byte[] setTime() {

        Date date = new Date(System.currentTimeMillis());

        byte[] command = new byte[16];
        command[0] = (byte) CMD_SET_TIME;

        command[1] = Convert.intInByte(date.getYear()-100);
        command[2] = Convert.intInByte(date.getMonth()+1);
        command[3] = Convert.intInByte(date.getDate() );
        command[4] = Convert.intInByte(date.getHours());
        command[5] = Convert.intInByte(date.getMinutes());
        command[6] = Convert.intInByte(date.getSeconds());

        command[7] = (byte)0;
        command[8] = (byte) 0;
        command[9] = (byte) 0;
        command[10] = (byte) 0;
        command[11] = (byte) 0;
        command[12] = (byte) 0;
        command[13] = (byte) 0;
        command[14] = (byte) 0;
        command[15] = (byte) 0;

        command[15] = Convert.get_checksum(command, 15);
        return command;
    }

}
