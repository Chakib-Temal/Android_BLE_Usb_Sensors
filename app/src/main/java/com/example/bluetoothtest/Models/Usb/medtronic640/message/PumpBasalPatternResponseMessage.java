package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import android.util.Log;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

import java.util.Arrays;


import static com.example.bluetoothtest.Models.Usb.medtronic640.utils.ToolKit.read16BEtoUInt;
import static com.example.bluetoothtest.Models.Usb.medtronic640.utils.ToolKit.read32BEtoULong;
import static com.example.bluetoothtest.Models.Usb.medtronic640.utils.ToolKit.read8toUInt;

/**
 * Created by lgoedhart on 27/03/2016.
 */
public class PumpBasalPatternResponseMessage extends MedtronicSendMessageResponseMessage {
    private static final String TAG = PumpBasalPatternResponseMessage.class.getSimpleName();

    private byte[] basalPattern; // [8bit] basalPattern number, [8bit] count, { [32bitBE] rate (div 10000), [8bit] time period (mult 30 min) }

    protected PumpBasalPatternResponseMessage(MedtronicCnlSession pumpSession, byte[] payload) throws EncryptionException, ChecksumException, UnexpectedMessageException {
        super(pumpSession, payload);

        if (!MedtronicSendMessageRequestMessage.MessageType.READ_BASAL_PATTERN.response(read16BEtoUInt(payload, 0x01))) {
            Log.e(TAG, "Invalid message received for PumpBasalPattern");
            throw new UnexpectedMessageException("Invalid message received for PumpBasalPattern");
        }

        basalPattern = Arrays.copyOfRange(payload, 3, payload.length);
    }

    public byte[] getBasalPattern() {
        return basalPattern;
    }

    public void logcat() {
        int index = 0;
        double rate;
        int time;

        Log.d(TAG, "Pattern data size: " + basalPattern.length);

        int number = read8toUInt(basalPattern, index++);
        int items = read8toUInt(basalPattern, index++);
        Log.d(TAG, "Pattern: " + number + " Items: " + items);

        for (int i = 0; i < items; i++) {
            rate = read32BEtoULong(basalPattern, index) / 10000.0;
            time = read8toUInt(basalPattern, index + 0x04) * 30;
            Log.d(TAG, "TimePeriod: " + (i + 1) + " Rate: " + rate + " Time: " + time / 60 + "h" + time % 60 + "m");
            index += 5;
        }
    }
}