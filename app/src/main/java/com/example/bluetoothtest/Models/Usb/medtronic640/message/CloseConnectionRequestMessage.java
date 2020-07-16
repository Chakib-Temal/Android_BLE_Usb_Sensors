package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import android.util.Log;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.UsbHidDriver;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;
import com.example.bluetoothtest.Models.Usb.medtronic640.utils.HexDump;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Created by volker on 10.12.2016.
 */

public class CloseConnectionRequestMessage extends ContourNextLinkBinaryRequestMessage<CloseConnectionResponseMessage> {
    private static final String TAG = CloseConnectionRequestMessage.class.getSimpleName();

    public CloseConnectionRequestMessage(MedtronicCnlSession pumpSession, byte[] payload) throws ChecksumException {
        super(CommandType.CLOSE_CONNECTION, pumpSession, payload);
    }

    @Override
    public CloseConnectionResponseMessage send(UsbHidDriver mDevice, int millis) throws IOException, TimeoutException, ChecksumException, EncryptionException, UnexpectedMessageException {

//        clearMessage(mDevice, CLEAR_TIMEOUT_MS);
        clearMessage(mDevice, PRESEND_CLEAR_TIMEOUT_MS);

        sendMessage(mDevice);
        if (millis > 0) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ignored) {
            }
        }

        byte payload[];
        while (true) {
            payload = readMessage(mDevice);
            if (payload.length < 0x21)
                Log.e(TAG, "response message size less then expected, length = " + payload.length);
            else if ((payload[0x12] & 0xFF) != 0x11)
                Log.e(TAG, "response message not a 0x11, got a 0x" + HexDump.toHexString(payload[0x12]));
            else break;
        }

        return this.getResponse(payload);

/*
        sendMessage(mDevice);
        if (millis > 0) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ignored) {
            }
        }

        return this.getResponse(readMessage(mDevice));
*/
    }

    @Override
    protected CloseConnectionResponseMessage getResponse(byte[] payload) throws ChecksumException, EncryptionException, IOException {
        return new CloseConnectionResponseMessage(payload);
    }
}
