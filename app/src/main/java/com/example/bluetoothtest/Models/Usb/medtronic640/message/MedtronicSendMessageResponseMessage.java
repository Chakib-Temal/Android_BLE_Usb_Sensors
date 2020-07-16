package com.example.bluetoothtest.Models.Usb.medtronic640.message;


import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;

/**
 * Created by volker on 18.12.2016.
 */

public class MedtronicSendMessageResponseMessage extends MedtronicResponseMessage {
    protected MedtronicSendMessageResponseMessage(MedtronicCnlSession pumpSession, byte[] payload) throws EncryptionException, ChecksumException {
        super(pumpSession, payload);
    }
}
