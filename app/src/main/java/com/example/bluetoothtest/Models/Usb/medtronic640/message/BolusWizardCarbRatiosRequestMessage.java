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

public class BolusWizardCarbRatiosRequestMessage extends MedtronicSendMessageRequestMessage<BolusWizardCarbRatiosResponseMessage> {
    private static final String TAG = BolusWizardCarbRatiosRequestMessage.class.getSimpleName();

    public BolusWizardCarbRatiosRequestMessage(MedtronicCnlSession pumpSession) throws EncryptionException, ChecksumException {
        super(MessageType.READ_BOLUS_WIZARD_CARB_RATIOS, pumpSession, null);
    }

    public BolusWizardCarbRatiosResponseMessage send(UsbHidDriver mDevice, int millis) throws IOException, TimeoutException, ChecksumException, EncryptionException, UnexpectedMessageException {
        sendToPump(mDevice, TAG);
        return getResponse(readFromPump(mDevice, mPumpSession, TAG));
    }

    @Override
    protected BolusWizardCarbRatiosResponseMessage getResponse(byte[] payload) throws ChecksumException, EncryptionException, IOException, UnexpectedMessageException {
        return new BolusWizardCarbRatiosResponseMessage(mPumpSession, payload);
    }
}
