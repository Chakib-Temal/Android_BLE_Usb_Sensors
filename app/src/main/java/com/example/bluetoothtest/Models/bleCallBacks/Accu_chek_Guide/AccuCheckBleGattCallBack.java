package com.example.bluetoothtest.Models.bleCallBacks.Accu_chek_Guide;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.bluetoothtest.MainActivity;
import com.example.bluetoothtest.Models.bleCallBacks.BleModelCallBack;
import com.example.bluetoothtest.Models.bleCallBacks.OneTouch.GlucoseReadingRx;
import com.example.bluetoothtest.Models.bleCallBacks.OneTouch.JoH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * [1,1] Read all records
 * [4,1] Number of records
 * [1,6] Read last record received
 * [1,5] Read first record
 * [1,3,1,45,0] Read extract from record 45 onwards
 *
 */

public class AccuCheckBleGattCallBack extends BleModelCallBack {

    public final static String TAG = AccuCheckBleGattCallBack.class.getSimpleName();

    private static final String DEVICE_INFO_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb";
    private static final String MANUFACTURER_NAME = "00002a29-0000-1000-8000-00805f9b34fb";

    private static final String GLUCOSE_SERVICE = ("00001808-0000-1000-8000-00805f9b34fb");
    private static final String DATE_TIME_CHARACTERISTIC = ("00002a08-0000-1000-8000-00805f9b34fb");
    private CurrentTimeRx ct;
    private boolean timeAlreadyRead = false;

    private static BluetoothGatt mBluetoothGatt;


    private static final String GLUCOSE_CHARACTERISTIC = ("00002a18-0000-1000-8000-00805f9b34fb");
    private static final String CONTEXT_CHARACTERISTIC = ("00002a34-0000-1000-8000-00805f9b34fb");
    private boolean newGlucoseContextNotifyAlreadyActived = false;

    private static final String RECORDS_CHARACTERISTIC = ("00002a52-0000-1000-8000-00805f9b34fb");

    ACCU_CHEK_GUIDE_OPERATION mOperation ;

    private static List<String> measureList = new ArrayList<>();

    @Override
    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
        super.onDisConnected(isActiveDisConnected, device, gatt, status);
        MainActivity.writeTextSucces("Fin de connexion l'accu check guideo");

    }

    @Override
    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
        mDevice = bleDevice;

        mBluetoothGatt = gatt;

        measureList = new ArrayList<>();

        MainActivity.writeTextSucces("OK Accu Chek Guide");
        mOperation = ACCU_CHEK_GUIDE_OPERATION.GET_MANUFATURE_NAME;
        BleManager.getInstance().read(mDevice, DEVICE_INFO_SERVICE, MANUFACTURER_NAME, mReadCharacteristicBleCallBack);

    }

    private BleReadCallback mReadCharacteristicBleCallBack = new BleReadCallback() {
        @Override
        public void onReadSuccess(byte[] data) {

            if (timeAlreadyRead)
                return;

            Log.i(TAG, Arrays.toString(data));

            switch (mOperation){

                case GET_MANUFATURE_NAME:

                    readManufactureName();
                    mOperation = ACCU_CHEK_GUIDE_OPERATION.GET_TIME;
                    BleManager.getInstance().read(mDevice, GLUCOSE_SERVICE, DATE_TIME_CHARACTERISTIC, this);
                    break;

                case GET_TIME:

                    timeAlreadyRead = true;
                    readTime();
                    mOperation = ACCU_CHEK_GUIDE_OPERATION.NOTIFY_NEW_GLUCOSE_VALUE;
                    BleManager.getInstance().notify(mDevice, GLUCOSE_SERVICE, GLUCOSE_CHARACTERISTIC, bleNotifyCallback);

                    break;

            }

        }

        @Override
        public void onReadFailure(BleException exception) {
            Log.i(TAG, "onReadFailure");
        }
    };

    private BleNotifyCallback bleNotifyCallback = new BleNotifyCallback() {
        @Override
        public void onNotifySuccess() {

            if (newGlucoseContextNotifyAlreadyActived)
                return;

            Log.i(TAG, "onNotifySuccess" + mOperation);

            switch (mOperation){

                case NOTIFY_NEW_GLUCOSE_VALUE:

                    mOperation = ACCU_CHEK_GUIDE_OPERATION.NOTIFY_NEW_GLUCOSE_CONTEXT;
                    BleManager.getInstance().notify(mDevice, GLUCOSE_SERVICE, CONTEXT_CHARACTERISTIC, bleNotifyCallback);
                    break;

                case NOTIFY_NEW_GLUCOSE_CONTEXT:

                    newGlucoseContextNotifyAlreadyActived = true;
                    mOperation = ACCU_CHEK_GUIDE_OPERATION.INDICATE;

                    BleManager.getInstance().indicate(mDevice, GLUCOSE_SERVICE, RECORDS_CHARACTERISTIC, bleIndicateCallback);

                    break;

                default :

            }
        }

        @Override
        public void onNotifyFailure(BleException exception) {

        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            Log.i(TAG + "***************** " , Arrays.toString(data));

        }
    };

    private BleIndicateCallback bleIndicateCallback = new BleIndicateCallback() {

        private boolean passed = false;

        @Override
        public void onIndicateSuccess() {

            if (passed)
                return;

            passed = true;
            Log.i(TAG, "onIndicateSuccess" + mOperation);
            mOperation = ACCU_CHEK_GUIDE_OPERATION.GET_ALL_READING;

            BleManager.getInstance().write(mDevice
                    , GLUCOSE_SERVICE
                    , RECORDS_CHARACTERISTIC
                    , getRecordNumber()
                    , new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.i(TAG, "onWriteSuccess " + total + "   " + current + " " + Arrays.toString(justWrite));


                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            Log.i(TAG, "onWriteFailure" + exception.toString());
                        }
                    });


        }

        @Override
        public void onIndicateFailure(BleException exception) {

        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            Log.i(TAG + "################", Arrays.toString(data));

            int commandType = data[0] & 0xFF;

            switch (commandType) {
                case 5:

                    int recordNumber = data[2] & 0xFF;
                    Log.i(TAG, "record number " + recordNumber);
                    writeRXCharacteristic(getAllRecords());

                    break;

                default:
                    // envoie pour enlever la popUp


            }


        }
    };


    public void writeRXCharacteristic(final byte[] cmd) {

        Log.i(TAG, Arrays.toString(cmd));

        BleManager.getInstance().notify(mDevice, GLUCOSE_SERVICE, GLUCOSE_CHARACTERISTIC, new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                Log.i(TAG, "onNotifySuccess");

                BleManager.getInstance().write(mDevice
                        , GLUCOSE_SERVICE
                        , RECORDS_CHARACTERISTIC
                        , cmd
                        , new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(int current, int total, byte[] justWrite) {
                                Log.i(TAG, "onWriteSuccess " + total + "   " + current + " " + Arrays.toString(justWrite) );


                            }

                            @Override
                            public void onWriteFailure(BleException exception) {
                                Log.i(TAG, "onWriteFailure" + exception.toString());
                            }
                        });

            }

            @Override
            public void onNotifyFailure(BleException exception) {

            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                Log.i(TAG, Arrays.toString(data));
                final GlucoseReadingRx gtb = new GlucoseReadingRx(data, "Accu-check Adress");
                String measure = gtb.toCustomString() + "  " + JoH.dateTimeText(gtb.time + gtb.offsetMs());
                Log.i(TAG, measure);

                measureList.add(measure);
                if (MainActivity.loadingDataListener != null)
                    MainActivity.loadingDataListener.onLoadDataIsFinish(measureList);

            }
        });



    }

    private void readManufactureName(){


        List<BluetoothGattService> services = BleManager.getInstance().getBluetoothGattServices(mDevice);

        for (BluetoothGattService service : services){

            if (service.getUuid().equals(UUID.fromString(DEVICE_INFO_SERVICE))) {

                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();

                for (BluetoothGattCharacteristic characteristic : characteristics){

                    if (characteristic.getUuid().equals(UUID.fromString(MANUFACTURER_NAME))){
                        Log.i(TAG, "manufacturer = " + characteristic.getStringValue(0));
                    }

                }

            }

        }

    }

    private void readTime(){

        List<BluetoothGattService> services = BleManager.getInstance().getBluetoothGattServices(mDevice);

        for (BluetoothGattService service : services){

            if (service.getUuid().equals(UUID.fromString(GLUCOSE_SERVICE))) {

                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();

                for (BluetoothGattCharacteristic characteristic : characteristics){

                    if (characteristic.getUuid().equals(UUID.fromString(DATE_TIME_CHARACTERISTIC))){
                        ct = new CurrentTimeRx(characteristic.getValue());
                        Log.i(TAG, "Device time: " + ct.toNiceString());
                    }

                }

            }

        }

    }

    private static final byte OPCODE_REPORT_RECORDS = 0x01;
    private static final byte ALL_RECORDS = 0x01;

    public static byte[] getAllRecords() {
        return new byte[]{OPCODE_REPORT_RECORDS, ALL_RECORDS};
    }

    public static byte[] getRecordNumber() {
        return new byte[]{0x04, 0x01};
    }

    public enum ACCU_CHEK_GUIDE_OPERATION{
        GET_MANUFATURE_NAME,
        GET_TIME,
        NOTIFY_NEW_GLUCOSE_VALUE,
        NOTIFY_NEW_GLUCOSE_CONTEXT,
        INDICATE,
        GET_ALL_READING;
    }

}