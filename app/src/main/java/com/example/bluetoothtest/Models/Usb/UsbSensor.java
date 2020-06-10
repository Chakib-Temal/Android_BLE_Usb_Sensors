package com.example.bluetoothtest.Models.Usb;


import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import com.example.bluetoothtest.App;

import java.io.IOException;

/**
 * This class represents a usb device.
 * it contains usb devices important informations.
 * All usb devices infomations can be read with android UsbManager class or
 * on usb organisation oline database : @see<a href="https://www.the-sz.com/products/usbid/"> https://www.the-sz.com/products/usbid/</a>
 *
 * @version 1.4.0
 */
public abstract class UsbSensor {

    /**
     * System parameters
     */
    protected UsbManager usbManager;
    protected UsbDeviceConnection mConnection;
    private UsbDevice mDevice;
    private UsbEndpoint mReadEndpoint;
    private UsbEndpoint mWriteEndpoint;
    private UsbInterface mInterface;

    /**
     * Mypredi parameters
     */
    protected UsbInfo info;

    public UsbSensor(UsbInfo usbInfo, UsbDevice device) throws Exception {
        usbManager = (UsbManager) App.getContext().getSystemService(Context.USB_SERVICE);

        this.info= usbInfo;
        this.initSensor(device);
        this.setup();
    }


    /**
     * Return an instance of UsbDevice @see android.hardware.usb.UsbDevice
     *
     * @return a instance of UsbDevice
     */
    public void initSensor(UsbDevice device)  {
        mDevice = device;
    }


    //==============================================================================================
    // Abstract Methods
    //==============================================================================================

    public abstract void setup() throws IOException;

    //==============================================================================================
    // Getters / Setters
    //==============================================================================================

    public UsbInfo getInfo()  {
        return info;
    }

    public void setInfo(UsbInfo info) {
        this.info = info;
    }

    public UsbInterface getInterface() {
        return mInterface;
    }

    public UsbEndpoint getReadEndpoint() {
        return mReadEndpoint;
    }

    public UsbEndpoint getWriteEndpoint() {
        return mWriteEndpoint;
    }

    public UsbDevice getDevice() {
        return mDevice;
    }

    public UsbDeviceConnection getConnection() {
        return mConnection;
    }

    public void setInterface(UsbInterface mInterface) {
        this.mInterface = mInterface;
    }

    public void setReadEndpoint(UsbEndpoint mReadEndpoint) {
        this.mReadEndpoint = mReadEndpoint;
    }

    public void setWriteEndpoint(UsbEndpoint mWriteEndpoint) {
        this.mWriteEndpoint = mWriteEndpoint;
    }

    public void setDevice(UsbDevice mDevice) {
        this.mDevice = mDevice;
    }

    public void setConnection(UsbDeviceConnection mConnection) {
        this.mConnection = mConnection;
    }

}

