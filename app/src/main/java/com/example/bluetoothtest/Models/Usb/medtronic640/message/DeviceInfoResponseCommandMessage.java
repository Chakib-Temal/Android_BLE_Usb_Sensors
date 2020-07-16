package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by lgoedhart on 10/05/2016.
 */
public class DeviceInfoResponseCommandMessage extends ContourNextLinkResponseMessage {
    private String serial = "";
    private final Pattern pattern = Pattern.compile(".*?\\^(\\d{4}-\\d{7})\\^.*");

    protected DeviceInfoResponseCommandMessage(byte[] payload)
            throws ChecksumException, EncryptionException, TimeoutException, UnexpectedMessageException, IOException {
        super(payload);

        extractStickSerial(new String(payload));
    }

    public String getSerial() {
        return serial;
    }

    private void extractStickSerial(String astmMessage) {
        Matcher matcher = pattern.matcher(astmMessage);
        if (matcher.find()) {
            serial = matcher.group(1);
        }
    }

}