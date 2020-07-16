package com.example.bluetoothtest.Models.Usb.medtronic640.message;


import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;

/**
 * Created by lgoedhart on 26/03/2016.
 */
public class ContourNextLinkBinaryResponseMessage extends ContourNextLinkResponseMessage {

    public ContourNextLinkBinaryResponseMessage(byte[] payload) throws ChecksumException {
        super(payload);
    }
}
