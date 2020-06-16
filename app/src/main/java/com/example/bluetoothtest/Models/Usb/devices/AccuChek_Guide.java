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
                r = mConnection.bulkTransfer(this.getWriteEndpoint(), dest, dest.length,
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

    public int read(byte[] dest, @Nullable int timeoutMillis) throws Exception {

        int r = 0;
        synchronized (mReadBufferLock) {
            {
                r = mConnection.bulkTransfer(this.getWriteEndpoint(), dest, dest.length,
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

    @Override
    public int write(byte[] src, int timeoutMillis)  throws Exception {
        int r = getConnection().bulkTransfer(getReadEndpoint(), src, src.length,
                timeoutMillis);
        if (r<= 0) {
            throw new Exception("Error writing to usb endpoint : byte read < 0 ");
        }
        return  r;
    }

    public void getBuildAssociationx() throws Exception {
        byte[] bytes =  buildAssociationResponse();
        write(bytes, 5000);
    }

    public byte[] buildAssociationResponsex() {

        byte [] byteBuffer = new byte[48];

        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[1] = (byte) 0x00;
        byteBuffer[2] = (byte) 0x00;
        byteBuffer[3] = (byte) 0x2c;
        byteBuffer[4] = (byte) 0x00;
        byteBuffer[5] = (byte) 0x03;
        byteBuffer[6] = (byte) 0x50;
        byteBuffer[7] = (byte) 0x79;

        byteBuffer[8] = (byte) 0x00;
        byteBuffer[9] = (byte) 0x26;
        byteBuffer[10] = (byte) 0x80;
        byteBuffer[11] = (byte) 0x00;
        byteBuffer[12] = (byte) 0x00;
        byteBuffer[13] = (byte) 0x02;
        byteBuffer[14] = (byte) 0x80;
        byteBuffer[15] = (byte) 0x00;

        byteBuffer[16] = (byte) 0x80;
        byteBuffer[17] = (byte) 0x00;
        byteBuffer[18] = (byte) 0x00;
        byteBuffer[19] = (byte) 0x00;
        byteBuffer[20] = (byte) 0x00;
        byteBuffer[21] = (byte) 0x00;
        byteBuffer[22] = (byte) 0x00;
        byteBuffer[23] = (byte) 0x00;

        byteBuffer[24] = (byte) 0x80;
        byteBuffer[25] = (byte) 0x00;
        byteBuffer[26] = (byte) 0x00;
        byteBuffer[27] = (byte) 0x00;
        byteBuffer[28] = (byte) 0x00;
        byteBuffer[29] = (byte) 0x08;
        byteBuffer[30] = (byte) 0x12;
        byteBuffer[31] = (byte) 0x34;

        byteBuffer[32] = (byte) 0x56;
        byteBuffer[33] = (byte) 0x78;
        byteBuffer[34] = (byte) 0x87;
        byteBuffer[35] = (byte) 0x65;
        byteBuffer[36] = (byte) 0x43;
        byteBuffer[37] = (byte) 0x21;
        byteBuffer[38] = (byte) 0x00;
        byteBuffer[39] = (byte) 0x00;

        byteBuffer[40] = (byte) 0x00;
        byteBuffer[41] = (byte) 0x00;
        byteBuffer[42] = (byte) 0x00;
        byteBuffer[43] = (byte) 0x00;
        byteBuffer[44] = (byte) 0x00;
        byteBuffer[45] = (byte) 0x00;
        byteBuffer[46] = (byte) 0x00;
        byteBuffer[47] = (byte) 0x00;

        return byteBuffer;

    }

    public void getBuildAssociation() throws Exception {
        byte[] bytes =  buildAssociationResponse();
        write(bytes, 5000);
    }

    public byte[] buildAssociationResponse() {

        byte [] byteBuffer = new byte[48];

        byteBuffer[0] = (byte) 0xe3;
        byteBuffer[1] = (byte) 0x00;
        byteBuffer[2] = (byte) 0x00;
        byteBuffer[3] = (byte) 0x2c;
        byteBuffer[4] = (byte) 0x00;
        byteBuffer[5] = (byte) 0x03;
        byteBuffer[6] = (byte) 0x50;
        byteBuffer[7] = (byte) 0x79;

        byteBuffer[8] = (byte) 0x00;
        byteBuffer[9] = (byte) 0x26;
        byteBuffer[10] = (byte) 0x80;
        byteBuffer[11] = (byte) 0x00;
        byteBuffer[12] = (byte) 0x00;
        byteBuffer[13] = (byte) 0x02;
        byteBuffer[14] = (byte) 0x80;
        byteBuffer[15] = (byte) 0x00;

        byteBuffer[16] = (byte) 0x80;
        byteBuffer[17] = (byte) 0x00;
        byteBuffer[18] = (byte) 0x00;
        byteBuffer[19] = (byte) 0x00;
        byteBuffer[20] = (byte) 0x00;
        byteBuffer[21] = (byte) 0x00;
        byteBuffer[22] = (byte) 0x00;
        byteBuffer[23] = (byte) 0x00;

        byteBuffer[24] = (byte) 0x80;
        byteBuffer[25] = (byte) 0x00;
        byteBuffer[26] = (byte) 0x00;
        byteBuffer[27] = (byte) 0x00;
        byteBuffer[28] = (byte) 0x00;
        byteBuffer[29] = (byte) 0x08;
        byteBuffer[30] = (byte) 0x12;
        byteBuffer[31] = (byte) 0x34;

        byteBuffer[32] = (byte) 0x56;
        byteBuffer[33] = (byte) 0x78;
        byteBuffer[34] = (byte) 0x87;
        byteBuffer[35] = (byte) 0x65;
        byteBuffer[36] = (byte) 0x43;
        byteBuffer[37] = (byte) 0x21;
        byteBuffer[38] = (byte) 0x00;
        byteBuffer[39] = (byte) 0x00;

        byteBuffer[40] = (byte) 0x00;
        byteBuffer[41] = (byte) 0x00;
        byteBuffer[42] = (byte) 0x00;
        byteBuffer[43] = (byte) 0x00;
        byteBuffer[44] = (byte) 0x00;
        byteBuffer[45] = (byte) 0x00;
        byteBuffer[46] = (byte) 0x00;
        byteBuffer[47] = (byte) 0x00;

        return byteBuffer;

    }



}
