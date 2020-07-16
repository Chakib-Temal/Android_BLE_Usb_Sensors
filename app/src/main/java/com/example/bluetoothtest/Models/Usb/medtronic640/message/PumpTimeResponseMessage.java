package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import android.util.Log;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

import java.util.Date;


import static com.example.bluetoothtest.Models.Usb.medtronic640.utils.ToolKit.read16BEtoUInt;
import static com.example.bluetoothtest.Models.Usb.medtronic640.utils.ToolKit.read32BEtoInt;


/**
 * Created by lgoedhart on 27/03/2016.
 */
public class PumpTimeResponseMessage extends MedtronicSendMessageResponseMessage {
    private static final String TAG = PumpTimeResponseMessage.class.getSimpleName();

    private Date pumpTime;
    private int pumpTimeRTC;
    private int pumpTimeOFFSET;

    protected PumpTimeResponseMessage(MedtronicCnlSession pumpSession, byte[] payload) throws EncryptionException, ChecksumException, UnexpectedMessageException {
        super(pumpSession, payload);

        if (!MedtronicSendMessageRequestMessage.MessageType.READ_PUMP_TIME.response(read16BEtoUInt(payload, 0x01))) {
            Log.e(TAG, "Invalid message received for getPumpTime");
            throw new UnexpectedMessageException("Invalid message received for PumpTime");
        }

        pumpTimeRTC = read32BEtoInt(payload, 0x04);
        pumpTimeOFFSET = read32BEtoInt(payload, 0x08);
        pumpTime = MessageUtils.decodeDateTime(pumpTimeRTC & 0xFFFFFFFFL, pumpTimeOFFSET);
    }

    public Date getPumpTime() {
        return pumpTime;
    }

    public int getPumpTimeRTC() {
        return pumpTimeRTC;
    }

    public int getPumpTimeOFFSET() {
        return pumpTimeOFFSET;
    }
}
