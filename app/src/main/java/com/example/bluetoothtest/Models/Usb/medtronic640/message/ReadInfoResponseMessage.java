package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;



/**
 * Created by lgoedhart on 10/05/2016.
 */
public class ReadInfoResponseMessage extends MedtronicResponseMessage {
    private long linkMAC;
    private long pumpMAC;

    protected ReadInfoResponseMessage(MedtronicCnlSession pumpSession, byte[] payload) throws ChecksumException, EncryptionException {
        super(pumpSession, payload);

        ByteBuffer infoBuffer = ByteBuffer.allocate(16);
        infoBuffer.order(ByteOrder.BIG_ENDIAN);
        infoBuffer.put(this.encode(), 0x21, 16);
        linkMAC = infoBuffer.getLong(0);
        pumpMAC = infoBuffer.getLong(8);
    }

    public long getLinkMAC() {
        return linkMAC;
    }

    public long getPumpMAC() {
        return pumpMAC;
    }
}