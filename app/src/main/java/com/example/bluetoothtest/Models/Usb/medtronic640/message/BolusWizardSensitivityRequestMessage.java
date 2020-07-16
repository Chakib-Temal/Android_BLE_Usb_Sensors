package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.UsbHidDriver;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;



/**
 * Created by Pogman on 8.11.17.
 */

public class BolusWizardSensitivityRequestMessage extends MedtronicSendMessageRequestMessage<BolusWizardSensitivityResponseMessage> {
    private static final String TAG = BolusWizardSensitivityRequestMessage.class.getSimpleName();

    public BolusWizardSensitivityRequestMessage(MedtronicCnlSession pumpSession) throws EncryptionException, ChecksumException {
        super(MessageType.READ_BOLUS_WIZARD_SENSITIVITY_FACTORS, pumpSession, null);
    }

    public BolusWizardSensitivityResponseMessage send(UsbHidDriver mDevice, int millis) throws IOException, TimeoutException, ChecksumException, EncryptionException, UnexpectedMessageException {
        sendToPump(mDevice, TAG);
        return getResponse(readFromPump(mDevice, mPumpSession, TAG));
    }

    @Override
    protected BolusWizardSensitivityResponseMessage getResponse(byte[] payload) throws ChecksumException, EncryptionException, IOException, UnexpectedMessageException {
        return new BolusWizardSensitivityResponseMessage(mPumpSession, payload);
    }
}
