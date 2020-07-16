package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;

import java.io.IOException;



/**
 * Created by volker on 10.12.2016.
 */

public class ReadInfoRequestMessage extends ContourNextLinkBinaryRequestMessage<ReadInfoResponseMessage> {
    public ReadInfoRequestMessage(MedtronicCnlSession pumpSession) throws ChecksumException {
        super(ContourNextLinkBinaryRequestMessage.CommandType.READ_INFO, pumpSession, null);
    }

    @Override
    protected ReadInfoResponseMessage getResponse(byte[] payload) throws ChecksumException, EncryptionException, IOException {
        return new ReadInfoResponseMessage(mPumpSession, payload);
    }
}
