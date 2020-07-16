package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.UsbHidDriver;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;



/**
 * Created by Pogman on 8.10.17.
 */

public class AckMessage extends MedtronicSendMessageRequestMessage {
    private static final String TAG = AckMessage.class.getSimpleName();

    public AckMessage(MedtronicCnlSession pumpSession, byte[] payload) throws EncryptionException, ChecksumException {
        super(MessageType.ACK_COMMAND, pumpSession, payload);
    }

    public AckMessage send(UsbHidDriver mDevice) throws IOException, TimeoutException, UnexpectedMessageException {
        sendToPump(mDevice,300, TAG);

        return null;
    }
}