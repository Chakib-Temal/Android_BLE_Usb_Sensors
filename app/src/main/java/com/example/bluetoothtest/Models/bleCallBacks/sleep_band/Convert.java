package com.example.bluetoothtest.Models.bleCallBacks.sleep_band;

public class Convert {

    // format BCD (int = 20, byte = 20)
    public static byte intInByte(int entier) {
        int retenu = entier / 10;
        byte b = (byte) (entier + (retenu * 6));
        return b;
    }

    public static int twoByteToInt(byte low, byte high) {
        low = (byte) (low & 0xFF);
        high = (byte) (high & 0xFF);
        int entier = high & 0xFF;
        entier = entier * 256;
        int entier2 = low & 0xFF;
        entier = entier + entier2;
        return entier;
    }

    public static int threeByteToInt(byte byte1, byte byte2, byte byte3) {
        byte1 = (byte) (byte1 & 0xFF);
        byte2 = (byte) (byte2 & 0xFF);
        byte3 = (byte) (byte3 & 0xFF);
        int entier = byte1 & 0xFF;
        entier = entier * 256 * 256;
        int entier2 = byte2 & 0xFF;
        entier2 = entier2 * 256;
        int entier3 = byte3 & 0xFF;
        entier = entier + entier2 + entier3;
        return entier;
    }

    public static int fourByteToInt(byte byte1, byte byte2, byte byte3, byte byte4) {
        byte1 = (byte) (byte1 & 0xFF);
        byte2 = (byte) (byte2 & 0xFF);
        byte3 = (byte) (byte3 & 0xFF);
        byte4 = (byte) (byte4 & 0xFF);

        int entier = byte1 & 0xFF;
        entier = entier * 256 * 256 *256;

        int entier2 = byte2 & 0xFF ;
        entier2 = entier2 * 256 * 256;

        int entier3 = byte3 & 0xFF;
        entier3 = entier3 *256;

        int entier4 = byte4 &0xFF;

        entier = entier + entier2 + entier3 + entier4;
        return entier;
    }

    public static int getSumForAllDays(int[] daysWeek){
        int value = 0;
        for (int i = 0 ; i < daysWeek.length; i++){
            value += daysWeek[i];
        }
        return value;
    }

    // format BCD (int = 20, byte = 20)
    public static int byteInInt(byte hex) {
        int entier = hex;
        int retenu = entier / 16;
        entier = entier - (retenu * 6);
        return entier;
    }

    public static byte[] string2bytes(String s) {
        String ss = s.replace(" ", "");
        int string_len = ss.length();
        int len = string_len / 2;
        if (string_len % 2 == 1) {
            ss = ss + "0";
            string_len++;
            len++;
        }
        byte[] a = new byte[len];
        for (int i = 0; i < len; i++) {
            a[i] = (byte) Integer.parseInt(ss.substring(2 * i, 2 * i + 2), 16);
        }
        return a;
    }

    public static String hex2HexString(byte[] b) {

        int len = b.length;
        int[] x = new int[len];
        String[] y = new String[len];
        StringBuilder str = new StringBuilder();

        for (int j = 0; j < len; j++) {
            x[j] = b[j] & 0xff;
            y[j] = Integer.toHexString(x[j]);
            while (y[j].length() < 2) {
                y[j] = "0" + y[j];
            }
            str.append(y[j] + " ");
        }

        return new String(str).toUpperCase();
    }

    public static byte get_checksum(byte[] data, int count) {
        byte sum = 0;
        try {
            for (int i = 0; i < count; i++) {
                sum += (byte) data[i];
            }
        }
        catch (Exception e) {
            // Do nothing
        }
        return sum;
    }

    public static boolean checksum(byte[] data, int index) {

        if (index >= data.length)
            return false;

        if (data[index] == get_checksum(data, index))
            return true;

        return false;
    }

    public static byte[] Pack_user_inf(byte sequence, int weight, int age, int height, int stride, boolean sex, int target) {
        byte[] data = new byte[12];

        data[0] = (byte) 0xE1;
        data[1] = sequence;
        data[2] = (byte) (weight >> 8);
        data[3] = (byte) (weight & 0xff);
        data[4] = (byte) age;
        data[5] = (byte) height;
        data[6] = (byte) stride;

        if (sex)
            data[7] = (byte) 0x01;
        else
            data[7] = (byte) 0x00;

        data[8] = (byte) (target >> 16);
        data[9] = (byte) (target >> 8);
        data[10] = (byte) target;

        data[11] = get_checksum(data, 11);

        return data;
    }

    public static byte[] pack_ack(byte sequence, boolean status) {
        byte[] data = new byte[4];
        data[0] = (byte) 0xE0;
        data[1] = sequence;
        if (status)
            data[2] = (byte) 0x00;
        else
            data[2] = (byte) 0x01;
        data[3] = (byte) Convert.get_checksum(data, 3);
        return data;
    }
}



