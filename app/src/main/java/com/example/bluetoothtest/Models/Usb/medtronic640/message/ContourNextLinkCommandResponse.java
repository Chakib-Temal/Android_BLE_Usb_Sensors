package com.example.bluetoothtest.Models.Usb.medtronic640.message;


import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;

/**
 * Created by volker on 10.12.2016.
 */
public class ContourNextLinkCommandResponse extends ContourNextLinkBinaryResponseMessage {

    public ContourNextLinkCommandResponse(byte[] payload) throws ChecksumException {
        super(payload);
    }
}
