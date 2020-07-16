package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.UsbHidDriver;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Created by volker on 22.12.2016.
 */

public class EHSMMessage extends  MedtronicSendMessageRequestMessage<ContourNextLinkResponseMessage>{
    private static final String TAG = EHSMMessage.class.getSimpleName();

    protected EHSMMessage(MessageType messageType, MedtronicCnlSession pumpSession, byte[] payload) throws EncryptionException, ChecksumException {
        super(messageType, pumpSession, payload);
    }

    @Override
    public ContourNextLinkResponseMessage send(UsbHidDriver mDevice, int millis) throws IOException, TimeoutException, ChecksumException, EncryptionException, UnexpectedMessageException {
        sendToPump(mDevice, TAG);
        return null;
    }
}
