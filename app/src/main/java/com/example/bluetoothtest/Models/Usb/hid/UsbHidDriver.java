package com.example.bluetoothtest.Models.Usb.hid;

import android.hardware.usb.UsbDevice;
import android.support.annotation.Nullable;

import com.example.bluetoothtest.Models.Usb.UsbInfo;
import com.example.bluetoothtest.Models.Usb.UsbSensor;

/**
 * <p>
 * This abstract class manages the establishment of all connections of HID usb devices.
 * It contains common attributes and methods for managing usb Hid devices
 * and abstract methods that will be implemented by the differents types of USB HID devices.
 * There are differents types of usb devices class interface , the communication is different for each interface class.
 * The interface class of usb hid devices is 03.
 * Documentation about USB HID devices see it <a href="https://www.silabs.com/documents/public/application-notes/AN249.pdf">https://www.silabs.com/documents/public/application-notes/AN249.pdf</a>
 * </p>
 *
 * <p>
 *     There are two types of usb hid devices : usb Hid devices with on endpoint and usb hid devices with 2 endpoints
 * </p>
 *  Documentation about android usb host, please find it on @see<a href="https://developer.android.com/guide/topics/connectivity/usb/host">https://developer.android.com/guide/topics/connectivity/usb/host</a>
 * @author  AGOTSI
 * @version
 */
public abstract class UsbHidDriver extends UsbSensor {

    private final String TAG = UsbHidDriver.class.getSimpleName();

    /**
     * all attributs to handle usb devices
     */

    protected final Object mReadBufferLock = new Object();
    protected final Object mWriteBufferLock = new Object();
    private boolean isConnectionOpen ;
    private int packetSize;

    public UsbHidDriver(UsbInfo usbInfo, UsbDevice device) throws Exception {
        super(usbInfo, device);
        open();
    }

    /**
     * Etablish connection between usb device and android phone
     *
     */
    public void open() throws Exception {

        if (!this.mConnection.claimInterface(this.getInterface(), true)) {
            isConnectionOpen = false;
            throw new Exception("Could not claim data interface when trying to connect to the device");
        }else{
            isConnectionOpen =true;
        }

        if (this.getWriteEndpoint() != null) {

        }
    }

    /**
     * Close connection between android phone and usb device
     */
    public void close() {
        synchronized (UsbHidDriver.class) {
            if (mConnection != null && isConnectionOpen) {
                if (!mConnection.releaseInterface(this.getInterface()))
                    mConnection.close();
            }
            isConnectionOpen = false;
        }
    }

    /**
     * Read data from usb device
     *
     * @param dest a buffer array that will contain the read data
     * @param timeoutMillis read time
     * @return  the number of bits read
     * @throws Exception if a retrun value  = -1
     */
    public int read(byte[] dest, @Nullable int timeoutMillis) throws Exception {

        int r = 0;
        this.open();
        synchronized (mReadBufferLock) {
            {
                r = mConnection.bulkTransfer(this.getReadEndpoint(), dest, dest.length,
                        timeoutMillis);
                if (r <= 0) {
                    throw new Exception("Error occurs when writing to usb endpoint, the number of bits read equal to -1");
                }
            }
            this.close();
            return r;
        }
    }

    /**
     * Write data to usb device must be implement.
     *
     * @param src a byte buffer array of write data
     * @param timeoutMillis write time
     * @return the number of bits read
     * @throws Exception if a retrun value  = -1
     */
    public abstract int write(byte[] src, int timeoutMillis) throws Exception;


    //Getters and setters

    public boolean isConnectionOpen() {
        return isConnectionOpen;
    }


    public String getTAG() {
        return TAG;
    }


    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }


}
