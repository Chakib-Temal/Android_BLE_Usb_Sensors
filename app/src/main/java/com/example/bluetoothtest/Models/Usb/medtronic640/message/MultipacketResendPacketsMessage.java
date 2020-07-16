package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.UsbHidDriver;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;



/**
 * Created by Pogman on 10.10.17.
 */

public class MultipacketResendPacketsMessage extends MedtronicSendMessageRequestMessage {
    private static final String TAG = MultipacketResendPacketsMessage.class.getSimpleName();

    public MultipacketResendPacketsMessage(MedtronicCnlSession pumpSession, byte[] payload) throws EncryptionException, ChecksumException {
        super(MessageType.MULTIPACKET_RESEND_PACKETS, pumpSession, payload);
    }

    public MultipacketResendPacketsMessage send(UsbHidDriver mDevice) throws IOException, TimeoutException, ChecksumException, EncryptionException, UnexpectedMessageException {

        sendToPump(mDevice, TAG);

        return null;
    }
}