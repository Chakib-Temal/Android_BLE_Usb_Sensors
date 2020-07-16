package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;


/**
 * Created by lgoedhart on 10/05/2016.
 */
public class OpenConnectionResponseMessage extends ContourNextLinkBinaryResponseMessage {
    protected OpenConnectionResponseMessage(byte[] payload) throws ChecksumException, EncryptionException {
        super(payload);
    }

}