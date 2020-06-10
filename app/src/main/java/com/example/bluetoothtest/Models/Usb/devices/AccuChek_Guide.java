package com.example.bluetoothtest.Models.Usb.devices;

import android.hardware.usb.UsbDevice;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.bluetoothtest.Models.Usb.UsbInfo;
import com.example.bluetoothtest.Models.Usb.hid.UsbHid2EndpointsSensor;

import java.util.Arrays;

public class AccuChek_Guide extends UsbHid2EndpointsSensor {
    private static final String TAG = AccuChek_Guide.class.getSimpleName();


    public AccuChek_Guide(UsbInfo usbInfo, UsbDevice device) throws Exception {
        super(usbInfo, device);
    }

    @Override
    public void setup() {
        this.mConnection = this.usbManager.openDevice(this.getDevice());
        this.setInterface(this.getDevice().getInterface(0));
        this.setReadEndpoint(this.getDevice().getInterface(0).getEndpoint(1)); // command linux lsusb -v --> OUT
        this.setWriteEndpoint(this.getDevice().getInterface(0).getEndpoint(0));
    }

    public int writeInitial(int timeoutMillis) throws Exception {
        byte[] buf = new byte[2];

        int r = 0;
        synchronized (mWriteBufferLock) {
            r = getConnection().controlTransfer(0x80, 0x00, 0x00, 0x00, buf, 2, timeoutMillis);

            if (r <= 0) {
                throw new Exception("Error writing to usb endpoint : byte read < 0 , verify the controlTransfer method parameters.");
            }

        }

        return r;
    }

    public int readInitial(byte[] dest, @Nullable int timeoutMillis) throws Exception {

        int r = 0;
        synchronized (mReadBufferLock) {
            {
                r = mConnection.bulkTransfer(this.getReadEndpoint(), dest, dest.length,
                        timeoutMillis);
                if (r <= 0) {
                    throw new Exception("Error occurs when writing to usb endpoint, the number of bits read equal to -1");
                }
                else {
                    Log.i(TAG, Arrays.toString(dest));
                }
            }
            return r;
        }
    }

    /*
    public byte[] buildAssociationResponse() {
        e3 00 00 2c 00 03 50 79 00 26 80 00 00 02 80 00
        80 00 00 00 00 00 00 00 80 00 00 00 00 08 12 34
        56 78 87 65 43 21 00 00 00 00 00 00 00 00 00 00


        byte [] byteBuffer = new byte[48];

        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[1] = (byte) 0x00;
        byteBuffer[2] = (byte) 0x00;
        byteBuffer[3] = (byte) 0x2c;
        byteBuffer[4] = (byte) 0xe3;
        byteBuffer[5] = (byte) 0xe3;
        byteBuffer[6] = (byte) 0xe3;
        byteBuffer[] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[0] = (byte) 0xe3;

        debug('Association response:', _.toUpper(buffer.toString('hex')));

        return buffer;
    }
    */


}
