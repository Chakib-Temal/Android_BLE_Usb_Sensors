package com.example.bluetoothtest.Models.Usb.medtronic640.message;


import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;

/**
 * Created by lgoedhart on 26/03/2016.
 */
public class MedtronicResponseMessage extends ContourNextLinkResponseMessage {
    private static final String TAG = MedtronicResponseMessage.class.getSimpleName();

    protected MedtronicCnlSession mPumpSession;

    protected MedtronicResponseMessage(MedtronicCnlSession pumpSession, byte[] payload) throws EncryptionException, ChecksumException {
        super(payload);

        mPumpSession = pumpSession;
    }
}