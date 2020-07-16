package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.UsbHidDriver;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;



/**
 * Created by lgoedhart on 26/03/2016.
 */

public class PumpBasalPatternRequestMessage extends MedtronicSendMessageRequestMessage<PumpBasalPatternResponseMessage> {
    private static final String TAG = PumpBasalPatternRequestMessage.class.getSimpleName();

    public PumpBasalPatternRequestMessage(MedtronicCnlSession pumpSession, byte patternNumber) throws EncryptionException, ChecksumException {
        super(MessageType.READ_BASAL_PATTERN, pumpSession, buildPayload(patternNumber));
    }

    protected static byte[] buildPayload(byte patternNumber) {
        return new byte[]{patternNumber};
    }

    public PumpBasalPatternResponseMessage send(UsbHidDriver mDevice, int millis) throws IOException, TimeoutException, ChecksumException, EncryptionException, UnexpectedMessageException {
        sendToPump(mDevice, TAG);
        return getResponse(readFromPump(mDevice, mPumpSession, TAG));
    }

    @Override
    protected PumpBasalPatternResponseMessage getResponse(byte[] payload) throws ChecksumException, EncryptionException, IOException, UnexpectedMessageException {
        return new PumpBasalPatternResponseMessage(mPumpSession, payload);
    }
}
