package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

/**
 * Created by volker on 10.12.2016.
 */

public class RequestLinkKeyRequestMessage extends ContourNextLinkBinaryRequestMessage<RequestLinkKeyResponseMessage> {
    public RequestLinkKeyRequestMessage(MedtronicCnlSession pumpSession) throws ChecksumException {
        super(CommandType.REQUEST_LINK_KEY, pumpSession, null);
    }

    @Override
    protected RequestLinkKeyResponseMessage getResponse(byte[] payload) throws ChecksumException, EncryptionException, UnexpectedMessageException {
        return new RequestLinkKeyResponseMessage(mPumpSession, payload);
    }
}
