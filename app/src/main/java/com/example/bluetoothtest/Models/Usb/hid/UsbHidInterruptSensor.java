package com.example.bluetoothtest.Models.Usb.hid;

import android.hardware.usb.UsbDevice;

import com.example.bluetoothtest.Models.Usb.UsbInfo;

/**
 * This class implements the @see fr.semantic.ecare.app.android.components.sensor.diabete.core.hid.UsbHidDriver
 * It  handle communication with usb devices who have one endpoint to write and a interruption  to read.
 *
 * To read using interrupt endpoint, we  use an android USB @see android.hardware.usb#controlTransfer
 * To write , we use an android USB @see android.hardware.usb#bulkTransfer
 *
 * @author  Gedeon AGOTSI
 * @version
 */
public abstract  class UsbHidInterruptSensor extends UsbHidDriver {


    public UsbHidInterruptSensor(UsbInfo usbInfo, UsbDevice device) throws Exception {
        super(usbInfo, device);
    }

    /**
     * Implements @see UsbHidDriver#write .
     * It use @see android.hardware.usb#controlTransfer because it write using interrupt endpoint
     * About controlTransfer parameter  @see<a href=" https://www.beyondlogic.org/usbnutshell/usb6.shtml#SetupPacket">https://www.beyondlogic.org/usbnutshell/usb6.shtml#SetupPacket</a>
     * @param src a byte buffer array of write data
     * @param timeoutMillis write time
     * @return the number of bits read
     *
     * @throws Exception occurs when the writting is failed.
     */
    @Override
    public int write(byte[] src, int timeoutMillis) throws Exception {
        this.open();
        int r = 0;
        synchronized (mWriteBufferLock) {
            r = getConnection().controlTransfer(0x21, 0x09, 0x0200, 0x0000, src, src.length, timeoutMillis);
            if (r <= 0) {
                throw new Exception("Error writing to usb endpoint : byte read < 0 , verify the controlTransfer method parameters.");
            }
        }
        this.close();
        return r;
    }



    @Override
    public void setup() {
        this.mConnection = this.usbManager.openDevice(this.getDevice());
        this.setInterface(this.getDevice().getInterface(0));
        this.setReadEndpoint(this.getDevice().getInterface(0).getEndpoint(0));
        this.setPacketSize(this.getDevice().getInterface(0).getEndpoint(0).getMaxPacketSize());
    }

}
