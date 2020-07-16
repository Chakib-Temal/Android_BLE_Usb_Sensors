package com.example.bluetoothtest.Models.Usb.medtronic640.message;

import android.util.Log;

import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlSession;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;



/**
 * Created by lgoedhart on 10/05/2016.
 */
public class RequestLinkKeyResponseMessage extends MedtronicResponseMessage {
    private static final String TAG = RequestLinkKeyResponseMessage.class.getSimpleName();

    private byte[] key;

    protected RequestLinkKeyResponseMessage(MedtronicCnlSession pumpSession, byte[] payload) throws EncryptionException, ChecksumException, UnexpectedMessageException {
        super(pumpSession, payload);

        if (this.encode().length < (0x57 - 4)) {
            // Invalid message. Don't try and parse it
            // TODO - deal with this more elegantly
            Log.e(TAG, "Invalid message received for requestLinkKey");
            throw new UnexpectedMessageException("Invalid message received for requestLinkKey, Contour Next Link is not paired with pump.");
        }

        ByteBuffer infoBuffer = ByteBuffer.allocate(55);
        infoBuffer.order(ByteOrder.BIG_ENDIAN);
        infoBuffer.put(this.encode(), 0x21, 55);

        setPackedLinkKey(infoBuffer.array());
    }

    public byte[] getKey() {
        return key;
    }

    private void setPackedLinkKey(byte[] packedLinkKey) {
        this.key = new byte[16];

        int pos = mPumpSession.getStickSerial().charAt(mPumpSession.getStickSerial().length() - 1) & 7;

        for (int i = 0; i < this.key.length; i++) {
            if ((packedLinkKey[pos + 1] & 1) == 1) {
                this.key[i] = (byte) ~packedLinkKey[pos];
            } else {
                this.key[i] = packedLinkKey[pos];
            }

            if (((packedLinkKey[pos + 1] >> 1) & 1) == 0) {
                pos += 3;
            } else {
                pos += 2;
            }
        }
    }
}