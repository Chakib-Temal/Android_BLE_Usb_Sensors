package com.example.bluetoothtest.Models.bleCallBacks.OneTouch.verioReflect;

import android.util.Log;

import com.example.bluetoothtest.Models.bleCallBacks.MeasureModel;
import com.example.bluetoothtest.Models.bleCallBacks.OneTouch.Constants;
import com.example.bluetoothtest.Models.bleCallBacks.OneTouch.GlucoseReadingRx;
import com.example.bluetoothtest.Models.bleCallBacks.OneTouch.JoH;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

import static com.example.bluetoothtest.Models.bleCallBacks.OneTouch.CRC16ccitt.crc16ccitt;

/**
 * Created by jamorham on 07/06/2017.
 * <p>
 * Verio Flex appears not to be bluetooth standards compliant and so requires additional handling
 */


public class VerioReflectHelper {

    public static final String TAG = "GlucoseVerio";
    private static final byte DATA_DELIMITER = 0x03;
    private static final long TIME_OFFSET = 946684799L;



    public static final UUID VERIO_F7A1_SERVICE = UUID.fromString("af9df7a1-e595-11e3-96b4-0002a5d5c51b");
    public static final UUID VERIO_F7A2_WRITE = UUID.fromString("af9df7a2-e595-11e3-96b4-0002a5d5c51b");
    public static final UUID VERIO_F7A3_NOTIFICATION = UUID.fromString("af9df7a3-e595-11e3-96b4-0002a5d5c51b");

    private static int last_received_record = 0;
    public static int highest_record_number = -1;
    private static int number_of_records = -1;

    static int min = 0 ;


    public synchronized static int parseMessage(byte[] message) {

        if (message.length == 1) {

            if ((message[0] & (byte) 0x81) == (byte) 0x81) {
                Log.i(TAG, "ACK received: " + JoH.bytesToHex(message));
                return 0;
            }

            return sendAckImmediateAndReturnResult();

        } else {

            if (message.length > 5) {
                // Multibyte replies
                if ((message[0] != 0x01) && (message[1] != 0x02)) {
                    Log.i(TAG, "Invalid protocol header");
                    return sendAckImmediateAndReturnResult();
                }

                if (message[2] != (message.length - 1)) {
                    Log.i(TAG, "length field problem " + message.length + " vs " + message[2]);
                    return sendAckImmediateAndReturnResult();
                }

                if (checkCRC(message)) {

                    GlucoseReadingRx gtb = null;
                    // message contains data

                    if (message[5] == 0x06) {

                        final byte[] result = new byte[message.length - 9];
                        System.arraycopy(message, 6, result, 0, result.length);
                        final ByteBuffer data = ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN);

                        //Time
                        if (result.length == 4) {

                            long val = data.getInt(0);

                            if (val > 100000) {


                                long tval = ((val + TIME_OFFSET) * 1000) - ( 120 * 60 * 1000); // warning signed

                                Log.i(TAG, JoH.dateTimeText(tval));

                                OneTouchVerioReflectBleCallBack.sendAckImmediate();
                                return 1;

                            } else {

                                Log.i(TAG, "Reading counter: " + val);
                                highest_record_number = (int) val;
                                OneTouchVerioReflectBleCallBack.sendAckImmediate();

                                return 2;
                            }

                        }

                        else if (result.length == 2) {

                            int val = data.getShort(0);
                            Log.i(TAG, " Num records: Int16 value: " + val);
                            number_of_records = (int) val;
                            OneTouchVerioReflectBleCallBack.sendAckImmediate();

                            return 3;

                        }

                        else if (result.length == 11) {

                            final long info = data.getInt(6) + data.get(10);
                            Log.i(TAG, "Bgreading sanity check: " + info);

                            // Check info
                            /*
                            // 256 after lunch , 512 before lunch
                            if (info != 256){

                                return sendAckImmediateAndReturnResult();

                            }
                            */


                            Log.i(VerioReflectHelper.TAG, "value of Highest reccord nnumber " + VerioReflectHelper.highest_record_number);

                            final long tval = ((data.getInt(0) + TIME_OFFSET) * 1000) - ( 60 * 60 * 1000);

                            Log.i(TAG, "BGreading time: " + JoH.dateTimeText(tval));

                            final int mgdl = data.getShort(4);

                            Log.i(TAG, "BGreading mgdl: " + mgdl + " mmol:" + JoH.qs(((double) mgdl) * Constants.MGDL_TO_MMOLL, 1));


                            gtb = new GlucoseReadingRx(); // fakeup a record
                            gtb.mgdl = mgdl;
                            gtb.time = tval;
                            gtb.offset = 0;
                            gtb.device = "OneTouch Verio Flex";
                            gtb.data = data;
                            gtb.sequence = last_received_record;

                            MeasureModel measure = new MeasureModel();
                            measure.setDate(JoH.dateTimeText(tval));
                            measure.setValue("BGreading mgdl: " + mgdl + " mmol:" + JoH.qs(((double) mgdl) * Constants.MGDL_TO_MMOLL, 1));
                            OneTouchVerioReflectBleCallBack.measureList.add(measure.toString());


                            return sendAckImmediateAndReturnResult();
                        }

                        return sendAckImmediateAndReturnResult();

                        // error state
                    } else if (message[5] == 0x07) {

                        if (message[6] == 0x03) {
                            Log.i(TAG, "Verio: Command not allowed!");
                        }

                        return sendAckImmediateAndReturnResult();

                    } else if (message[5] == 0x08) {

                        if (message[6] == 0x03) {
                            Log.i(TAG, "Verio: Command not supported!");
                        }

                        return sendAckImmediateAndReturnResult();

                    } else if (message[5] == 0x09) {


                        if (message[6] == 0x03) {
                            Log.i(TAG, "Verio: Command not understood!");

                        }

                        return sendAckImmediateAndReturnResult();
                    }

                }

                else {
                    Log.i(TAG, "Checksum failure on packet: " + JoH.bytesToHex(message));
                    return sendAckImmediateAndReturnResult();
                }

            }

            Log.i(TAG, "Unexpected message size");
            return sendAckImmediateAndReturnResult();
        }

    }

    private static int sendAckImmediateAndReturnResult(){
        highest_record_number = highest_record_number - 1;

        if (highest_record_number > min) {
            OneTouchVerioReflectBleCallBack.sendAckImmediate();
            return 3;
        }
        else {
            return 4;
        }
    }

    public static byte[] getAckCMD() {
        return new byte[]{(byte) 0x81};
    }

    public static byte[] getTimeCMD() {
        return transmissionTemplate(new byte[]{0x20, 0x02});
    }

    public static byte[] getRcounterCMD() {
        return transmissionTemplate(new byte[]{0x27, 0x00});
    }

    public static byte[] getTcounterCMD() {
        return transmissionTemplate(new byte[]{0x0a, 0x02, 0x06});
    }

    public static byte[] getRecordCMD(int record) {
        return transmissionTemplate(new byte[]{(byte) 0xb3, (byte) (record & 0xff), (byte) ((record >> 8) & 0xff)});
    }

    public static byte[] transmissionTemplate(byte[] payload) {
        final byte[] template = templateProducer(payload);
        final byte[] transmission_template = new byte[template.length + 1];
        transmission_template[0] = 0x01;
        System.arraycopy(template, 0, transmission_template, 1, template.length);
        return transmission_template;
    }

    public static byte[] templateProducer(byte[] payload) {

        final int packet_size = payload.length + 7;
        final byte[] template = new byte[packet_size];
        template[0] = (byte) 0x02; // always the same
        template[1] = (byte) packet_size; // size of packet including payload
        template[2] = (byte) 0x00; // always 0
        template[3] = DATA_DELIMITER; // data start marker?

        System.arraycopy(payload, 0, template, 4, payload.length);
        template[4 + payload.length] = DATA_DELIMITER;
        System.arraycopy(crc16ccitt(template, true, false), 0, template, 5 + payload.length, 2);
        //if (d) Log.i(TAG, "template output: " + JoH.bytesToHex(template));
        return template;
    }

    private static boolean checkCRC(byte[] bytes) {
        final byte[] result = crc16ccitt(bytes, true, true);
        return ((result[0] == bytes[bytes.length - 2]) && (result[1] == bytes[bytes.length - 1]));
    }

}