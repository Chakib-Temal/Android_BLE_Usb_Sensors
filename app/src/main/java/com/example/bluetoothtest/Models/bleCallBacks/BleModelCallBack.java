package com.example.bluetoothtest.Models.bleCallBacks;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

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

    }

    @Override
    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

    }

}
