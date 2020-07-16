package com.example.bluetoothtest.Models.Usb.medtronic640;



/**
 * Created by lgoedhart on 4/06/2016.
 */
public class PumpInfo  {

    private long pumpMac;
    private String deviceName;
    private byte lastRadioChannel;

    public long getPumpMac() {
        return pumpMac;
    }

    public void setPumpMac(long pumpMac) {
        this.pumpMac = pumpMac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public byte getLastRadioChannel() {
        return lastRadioChannel;
    }

    public void setLastRadioChannel(byte lastRadioChannel) {
        this.lastRadioChannel = lastRadioChannel;
    }

    public long getPumpSerial() {
        return pumpMac & 0xffffff;
    }
}
