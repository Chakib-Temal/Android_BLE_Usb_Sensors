package com.example.bluetoothtest.Models.Usb.hid;

import android.hardware.usb.UsbDevice;

import com.example.bluetoothtest.Models.Usb.UsbInfo;

/**
 * This class implements the @see fr.semantic.ecare.app.android.components.sensor.diabete.core.hid.UsbHidDriver
 * It  handle communication with usb devices who two endpoints : the first  to write and the second to read
 *
 * Write and read data method use both @see android.hardware.usb#bulkTransfer
 *
 * @author  Gedeon AGOTSI
 * @version
 */
public abstract class UsbHid2EndpointsSensor extends UsbHidDriver {

    public UsbHid2EndpointsSensor(UsbInfo usbInfo, UsbDevice device) throws Exception {
        super(usbInfo, device);
    }

    @Override
    public int write(byte[] src, int timeoutMillis)  throws Exception {
        int r = getConnection().bulkTransfer(getWriteEndpoint(), src, src.length,
                timeoutMillis);
        if (r<= 0) {
            throw new Exception("Error writing to usb endpoint : byte read < 0 ");
        }
        return  r;
    }


    @Override
    public void setup() {
        this.mConnection = this.usbManager.openDevice(this.getDevice());
        this.setInterface(this.getDevice().getInterface(0));
        this.setReadEndpoint(this.getDevice().getInterface(0).getEndpoint(0)); // command linux lsusb -v --> OUT
        this.setWriteEndpoint(this.getDevice().getInterface(0).getEndpoint(1));
    }

}


