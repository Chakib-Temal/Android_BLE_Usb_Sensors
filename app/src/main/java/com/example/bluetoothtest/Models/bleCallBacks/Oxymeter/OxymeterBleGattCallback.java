package com.example.bluetoothtest.Models.bleCallBacks.Oxymeter;

import android.bluetooth.BluetoothGatt;
import android.os.Environment;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.bluetoothtest.MainActivity;
import com.example.bluetoothtest.Models.bleCallBacks.BleModelCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OxymeterBleGattCallback extends BleModelCallBack {

    public final static String TAG = OxymeterBleGattCallback.class.getSimpleName();

    private static final String SERVICE_UUID = "CDEACB80-5235-4C07-8846-93A37EE6B86D";
    private static final String RX_CHAR_UUID = "CDEACB81-5235-4C07-8846-93A37EE6B86D";
    private static final String TX_CHAR_UUID = "CDEACB82-5235-4C07-8846-93A37EE6B86D";

    boolean canAddNewMeasure = true;

    @Override
    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
        super.onDisConnected(isActiveDisConnected, device, gatt, status);
        MainActivity.writeTextSucces("Fin de connexion avec l'oxymetre");

    }

    @Override
    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
        mDevice = bleDevice;
        canAddNewMeasure = true;

        MainActivity.writeTextSucces("Connexion établie avec l'oxymètre, demarrage du temps réel");


        BleManager.getInstance().notify(mDevice
                , SERVICE_UUID
                , RX_CHAR_UUID
                , new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {

                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {

                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {

                        int BYTE0 = 0x81 & 0xFF;
                        int byte0 = data[0] & 0xFF;
                        if(byte0 == BYTE0) {
                            if(data[1] != 0xFF && data[2] != 0x7F && data[3] != 0x00) {
                                int sat = (int) data[2];
                                int fq = (int) data[1];
                                if(fq > 10 && sat > 10 && fq < 240 && sat < 101) {

                                    Log.i(TAG,  "sat " + sat + "  fre : " + fq);

                                    if (canAddNewMeasure) {
                                        saveOxymeterData(new OXY(sat, fq));
                                    }

                                }
                            }
                        }

                    }
                });
    }


    private void saveOxymeterData(OXY measure){

        try {
            File oxymeterDataFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/oxymeterData.csv");

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
            writer.append(df + ";" + measure.getSat() + ";" + measure.getFq() + "\n");
            writer.close();
            fileOutputStream.close();


        } catch (IOException e) {

        }finally {

        }

        canAddNewMeasure = true;

    }

}