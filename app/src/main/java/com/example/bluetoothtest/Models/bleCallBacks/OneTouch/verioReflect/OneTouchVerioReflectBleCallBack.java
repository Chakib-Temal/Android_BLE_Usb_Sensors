package com.example.bluetoothtest.Models.bleCallBacks.OneTouch.verioReflect;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.bluetoothtest.MainActivity;
import com.example.bluetoothtest.Models.bleCallBacks.BleModelCallBack;
import com.example.bluetoothtest.Models.bleCallBacks.OneTouch.verioFlex.VerioFlexHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OneTouchVerioReflectBleCallBack extends BleModelCallBack {

    public static final String TAG  = OneTouchVerioReflectBleCallBack.class.getSimpleName();
    public static final String VERIO_F7A1_SERVICE = "af9df7a1-e595-11e3-96b4-0002a5d5c51b";
    public static final String VERIO_F7A2_WRITE = "af9df7a2-e595-11e3-96b4-0002a5d5c51b";
    public static final String VERIO_F7A3_NOTIFICATION = "af9df7a3-e595-11e3-96b4-0002a5d5c51b";

    public static List<String> measureList = new ArrayList<>();


    @Override
    public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
        mBluetoothGatt = gatt;
        measureList = new ArrayList<>();
        MainActivity.writeTextSucces("Connexion avec succès avec OneTouch Verio Reflect");
        mDevice = bleDevice;
        BleManager.getInstance().notify(bleDevice, VERIO_F7A1_SERVICE, VERIO_F7A3_NOTIFICATION, oneTouchVersioFlexCallBack);
    }

    public static BleNotifyCallback oneTouchVersioFlexCallBack = new BleNotifyCallback() {

        @Override
        public void onNotifySuccess() {

            Log.i(TAG, "onNotifySuccess");
            writeRXCharacteristic(VerioFlexHelper.getTimeCMD());
        }

        @Override
        public void onNotifyFailure(BleException exception) {
            Log.i(TAG, "onNotifyFailure ");

        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            Log.i(TAG, "onCharacteristicChanged");
            Log.i(TAG, Arrays.toString(data));

            //Log.i(TAG, "Change notification for VERIO: " + JoH.bytesToHex(data));

            try {
                int res = VerioReflectHelper.parseMessage(data);

                if (res == 1){
                    writeRXCharacteristic(VerioReflectHelper.getTcounterCMD());
                }

                else if (res == 2){
                    writeRXCharacteristic(VerioReflectHelper.getRcounterCMD());
                }

                else if (res == 3){

                    OneTouchVerioReflectBleCallBack.writeRXCharacteristic(VerioReflectHelper.getRecordCMD(VerioReflectHelper.highest_record_number) );
                }

                else if (res == 4){
                    Log.i(VerioReflectHelper.TAG, "finish");

                    if (MainActivity.loadingDataListener != null)
                        MainActivity.loadingDataListener.onLoadDataIsFinish(measureList);

                }

            } catch (Exception e) {
                Log.i(TAG, "Got exception processing Verio data " + e);
            }


        }


    };

    public static void writeRXCharacteristic(final byte[] cmd) {

        Log.i("SendedMessages   ", Arrays.toString(cmd));

        BleManager.getInstance().write(mDevice
                , VERIO_F7A1_SERVICE
                , VERIO_F7A2_WRITE
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

    public static void sendAckImmediate(){

        BluetoothGattService service = mBluetoothGatt.getService(VerioFlexHelper.VERIO_F7A1_SERVICE);
        characteristic = service.getCharacteristic(VerioFlexHelper.VERIO_F7A2_WRITE);
        characteristic.setValue(VerioFlexHelper.getAckCMD());

        if (!mBluetoothGatt.writeCharacteristic(characteristic))
            Log.i(TAG, "Failed in write characteristic");

    }

}
