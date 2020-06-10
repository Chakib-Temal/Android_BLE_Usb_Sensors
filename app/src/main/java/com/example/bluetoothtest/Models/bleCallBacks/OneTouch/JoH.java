package com.example.bluetoothtest.Models.bleCallBacks.OneTouch;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by chakib on 25/05/20.
 */

public class JoH {

    public static final String TAG = JoH.class.getSimpleName();

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static DecimalFormatSymbols dfs;
    private static DecimalFormat df;


    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "<empty>";
        final char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static long msSince(long when) {
        return (tsl() - when);
    }

    public static long tsl() {
        return System.currentTimeMillis();
    }


    public static String dateTimeText(long timestamp) {
        return android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", timestamp).toString();
    }

    public static String qs(double x) {
        return qs(x, 2);
    }

    public static String qs(double x, int digits) {

        if (digits == -1) {
            digits = 0;
            if (((int) x != x)) {
                digits++;
                if ((((int) x * 10) / 10 != x)) {
                    digits++;
                    if ((((int) x * 100) / 100 != x)) digits++;
                }
            }
        }

        if (dfs == null) {
            final DecimalFormatSymbols local_dfs = new DecimalFormatSymbols();
            local_dfs.setDecimalSeparator('.');
            dfs = local_dfs; // avoid race condition
        }

        final DecimalFormat this_df;
        // use singleton if on ui thread otherwise allocate new as DecimalFormat is not thread safe
        if (Thread.currentThread().getId() == 1) {
            if (df == null) {
                final DecimalFormat local_df = new DecimalFormat("#", dfs);
                local_df.setMinimumIntegerDigits(1);
                df = local_df; // avoid race condition
            }
            this_df = df;
        } else {
            this_df = new DecimalFormat("#", dfs);
        }

        this_df.setMaximumFractionDigits(digits);
        return this_df.format(x);
    }


    public static byte[] base64decodeBytes(String input) {
        try {
            return Base64.decode(input.getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException | IllegalArgumentException e) {
            Log.e(TAG, "Got unsupported encoding: " + e);
            return new byte[0];
        }
    }

    public static String base64encodeBytes(byte[] input) {
        try {
            return new String(Base64.encode(input, Base64.NO_WRAP), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Got unsupported encoding: " + e);
            return "encode-error";
        }
    }

}
