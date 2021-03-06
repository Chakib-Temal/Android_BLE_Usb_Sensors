package com.example.bluetoothtest.Models.bleCallBacks;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.Toast;

import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.bluetoothtest.App;

/**
 * Created by chakib on 27/05/20.
 */

public abstract class BleModelCallBack extends BleGattCallback {

    public static BleDevice mDevice;

    protected static BluetoothGattCharacteristic characteristic;
    protected static BluetoothGatt mBluetoothGatt;

    @Override
    public void onStartConnect() {

    }

    @Override
    public void onConnectFail(BleDevice bleDevice, BleException exception) {
        Toast.makeText(App.getContext(), "Connexion failed avec " + bleDevice.getMac(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

    }

}
