package com.example.bluetoothtest.Models.bleCallBacks.Scale;

import android.bluetooth.BluetoothGatt;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.bluetoothtest.App;
import com.example.bluetoothtest.MainActivity;
import com.example.bluetoothtest.Models.bleCallBacks.BleModelCallBack;
import com.example.bluetoothtest.Models.bleCallBacks.sleep_band.Convert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Service that handle bluetooth communications with the scale.
 *
 * @version 1.3.0
 * @since 1.1.0
 */
public class ScaleBleGattCallback extends BleModelCallBack {

    public final static String TAG = ScaleBleGattCallback.class.getSimpleName();


    private static final String SERVICE_UUID = "0000181D-0000-1000-8000-00805f9b34fb";
    private static final String RX_CHAR_UUID = "00002A9D-0000-1000-8000-00805f9b34fb";
    private static final String TX_CHAR_UUID = "00002A9D-0000-1000-8000-00805f9b34fb";


    @Override
    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
        super.onDisConnected(isActiveDisConnected, device, gatt, status);
        MainActivity.writeTextSucces("Fin de connexion avec la balance");
        saveLog("onDisConnected");

    }


    @Override
    public void onStartConnect() {
        saveLog("onStartConnect");

    }

    @Override
    public void onConnectFail(BleDevice bleDevice, BleException exception) {
        saveLog("onConnectFail");

    }


    @Override
    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
        mDevice = bleDevice;

        mBluetoothGatt = gatt;


        MainActivity.writeTextSucces("Connexion OK avec la balance");
        saveLog("Connexion OK avec la balance");

        BleManager.getInstance().indicate(mDevice
                , SERVICE_UUID
                , RX_CHAR_UUID
                , new BleIndicateCallback() {
                    @Override
                    public void onIndicateSuccess() {
                        Log.d(TAG,"onIndicateSuccess");
                        saveLog("onIndicateSuccess");

                        Toast.makeText(App.getContext(),"OnIndicateSuccess ",Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onIndicateFailure(BleException exception) {
                        Log.d(TAG,"onIndicateFailure");
                        Toast.makeText(App.getContext(),"OnIndicateFailure ",Toast.LENGTH_LONG).show();
                        saveLog("OnIndicateFailure");

                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        Toast.makeText(App.getContext(),"onCharacteristicChanged ",Toast.LENGTH_LONG).show();
                        saveLog("onCharacteristicChanged");
                        saveLog(data.toString());

                        if(data[1] != 0xFF && data[2] != 0x7F) {
                            float weight = Convert.twoByteToInt(data[1], data[2]);
                            weight = (float) (weight * 0.005);
                            Log.d(TAG, "weight=" + weight);
                            Toast.makeText(App.getContext(),"weight=" + weight ,Toast.LENGTH_LONG).show();
                            saveLog("weight=" + weight);


                        }
                    }

                });

    }

    private void saveLog(String message){

        try {
            File oxymeterDataFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/logScale.txt");

            if (!oxymeterDataFile.exists()) {
                try {
                    oxymeterDataFile.createNewFile();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            Date c = Calendar.getInstance().getTime();
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c);


            FileOutputStream fileOutputStream = new FileOutputStream(oxymeterDataFile, true);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.append( df + "      "  + message + " \n");
            writer.close();
            fileOutputStream.close();


        } catch (IOException e) {

        }finally {

        }

    }

}
