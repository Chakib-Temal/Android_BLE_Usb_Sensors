package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;

import java.io.IOException;



/**
 * Created by John on 18.3.18.
 */

public class DiscoveryResponseMessage extends ContourNextLinkBinaryResponseMessage {
    private static final String TAG = DiscoveryResponseMessage.class.getSimpleName();

    protected DiscoveryResponseMessage(MedtronicCnlSession pumpSession, byte[] payload) throws EncryptionException, ChecksumException, IOException {
        super(payload);

        byte[] responseBytes = this.encode();
/*
        Log.d(TAG, "negotiateChannel: Check response length");
        if (responseBytes.length > 46) {
            radioChannel = responseBytes[76];
            radioRSSI = responseBytes[59];
            if (responseBytes[76] != pumpSession.getRadioChannel()) {
                throw new IOException(String.format(Locale.getDefault(), "Expected to get a message for channel %d. Got %d", pumpSession.getRadioChannel(), responseBytes[76]));
            }
        } else {
            radioChannel = ((byte) 0);
            radioRSSI = ((byte) 0);
        }
*/
    }

}
