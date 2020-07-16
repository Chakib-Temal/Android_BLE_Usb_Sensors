package com.example.bluetoothtest.Models.Usb.medtronic640.message;


import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;

/**
 * Created by volker on 15.12.2016.
 */

public class MedtronicPumpMessage extends ContourNextLinkMessage {

    protected MedtronicPumpMessage(MedtronicCnlSession pumpSession, byte[] bytes) {
        super(bytes);
    }
}
