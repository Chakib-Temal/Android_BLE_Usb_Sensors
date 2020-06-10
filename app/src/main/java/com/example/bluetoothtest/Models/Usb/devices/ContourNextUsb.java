package com.example.bluetoothtest.Models.Usb.devices;

import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.bluetoothtest.Models.Usb.UsbInfo;
import com.example.bluetoothtest.Models.Usb.hid.UsbHid2EndpointsSensor;
import com.example.bluetoothtest.Models.bleCallBacks.MeasureModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class ContourNextUsb extends UsbHid2EndpointsSensor {

    public static final byte AsciiETX = 0x03;
    public static final byte AsciiEOT = 0x04;
    public static final byte AsciiENQ = 0x05;
    public static final byte AsciiACK = 0x06;
    public static final byte AsciiCR = 0x0d;
    public static final byte AsciiLF = 0x0a;
    public static final byte AsciiNAK = 0x15;
    public static final byte AsciiETB = 0x17;
    public static final byte AsciiSTX = 0x02;

    public static final int FREESTYLE_LIBRE_USB_WRITE_TIME_OUT = 250;


    private boolean debug = false;
    private boolean communicationStopped = false;
    private int counter = 0;
    private List<MeasureModel> measureModels = new ArrayList<>();
    public int countNAK = 0;


    public ContourNextUsb(UsbInfo usbInfo, UsbDevice device) throws Exception {
        super(usbInfo, device);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public UsbInfo getInfo() {
        super.setInfo(info);
        this.info.setDeviceName("CONTOUR NEXT USB");
        this.info.setSoftwareVersion(getDevice().getVersion());
        this.info.setSerialNumlber(getDevice().getSerialNumber());
        return this.info;
    }

    public List<MeasureModel> readFromDevice() throws Exception {
        byte whatToSend = AsciiEOT;
        while (true) {
            sendDataToDevice(whatToSend);
            List<Byte> data = readDataFromDevice();
            if (communicationStopped) {
                break;
            }
            byte lastData;
            if (data.size() == 0) {
                lastData = AsciiENQ;
            } else {
                lastData = data.get(data.size() - 1);
            }

            if (lastData == AsciiNAK) {
                //        Log.d("Last device response: NAK (0x15)","");
                // got a <NAK>, send <EOT>
                whatToSend = (byte) countNAK;
                countNAK++;
                if (countNAK == 256)
                    countNAK = 0;
            } else if (lastData == AsciiENQ) {
                // Log.d("Last device response: ENQ (0x05)","");
                whatToSend = AsciiACK;
            } else if (lastData == AsciiEOT) {
                // Log.d("Last device response: EOT (0x04) - Exiting","");
                break;
            }
        }
        return measureModels;

    }


    private void sendDataToDevice(byte whatToSend) throws Exception {
        byte[] packet = createPacket(whatToSend);
        int ret = 0;
        try {
            ret = write(packet, FREESTYLE_LIBRE_USB_WRITE_TIME_OUT);
        } catch (Exception e) {
           // throw new SensorReaderException("Erro writing to the sensor");
        }

        if (ret <= 0) {
            throw new Exception("Erro writing to the sensor");
        }

    }


    private byte[] createPacket(byte whatToSend) {
        byte[] msgData = new byte[64];
        msgData[0] = 0;
        msgData[1] = 0;
        msgData[2] = 0;
        msgData[3] = 1; // size
        msgData[4] = (byte) whatToSend;

        return msgData;
    }


    private boolean containsSTX(byte[] data) {
        return contains(AsciiSTX, data);
    }


    private boolean containsETX(byte[] data) {
        return contains(AsciiETX, data);
    }


    private boolean contains(byte asciiChar, byte[] data) {
        for (byte b : data) {
            if (b == asciiChar) {
                return true;
            }
        }

        return false;
    }


    private boolean containsRecordTermination(byte[] data) {
        boolean data0d = false;

        for (byte b : data) {
            if (b == AsciiCR) {
                data0d = true;
            } else if ((data0d) && (b == AsciiLF)) {
                return true;
            }
        }
        return false;
    }


    private String getText(byte[] data) {
        boolean text = false;
        StringBuffer sb = new StringBuffer();

        for (byte b : data) {
            if (b == AsciiSTX) {
                text = true;
            }
            if (b == AsciiCR) {
                text = false;
            }

            if (text) {
                sb.append((char) b);
            }
        }

        return sb.toString();
    }

    private String getRawText(byte[] data) {
        StringBuffer sb = new StringBuffer();

        for (byte b : data) {
            sb.append((char) b);
        }
        return sb.toString();
    }


    private List<Byte> readDataFromDevice() throws Exception {
        List<Byte> dataOut = new ArrayList<Byte>();

        while (true) {
            byte[] dataFramed = new byte[0];
            try {
                dataFramed = readDataFromDeviceInternal();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (dataFramed == null) {
                // return null;
                break;
            }
            if (dataFramed.length > 4) {
                byte[] data = Arrays.copyOfRange(dataFramed, 3, dataFramed.length);
                if (containsRecordTermination(data)) {
                    if (dataOut.size() == 0) {
                        processLine(data);
                    } else {
                        //Util.addByteArrayToList(dataOut, data);
                        if (data != null) {
                            //processLine(Util.getByteArrayFromList(dataOut));
                        }
                        dataOut.clear();
                    }
                } else {
                   // dataOut.addAll(Util.convertByteArrayToList(data));
                }

                if (data.length <= 36) // 36
                    break;
            }
        }


        return dataOut;
    }


    private void processLine(byte[] data) {
        if (containsSTX(data)) {
            String text = getText(data);
            String code = decode(text);
            if (code.equals("L")) {
                communicationStopped = true;
            }

        } else {
            System.out.println("Raw: " + getRawText(data));
        }
    }


    private byte[] readDataFromDeviceInternal() throws Exception {
        int ret = 0;
        byte[] buf = new byte[64];
        try {
            ret = read(buf, 2000);
        } catch (Exception e) {
        }
        if (debug)
            System.out.println("Read (returned " + ret + ")");

        if (ret <= 0)
            return null;
        return buf;
    }


    private String decode(String textToDecode) {

        int idx = textToDecode.indexOf("|");
        //PatientModel patient = PatientActivity.getSelectedPatient();
        //String patientId = patient.getIdServer();
        if (idx == -1) {
            Log.d("Unidentified Text: ", textToDecode);
        }
        textToDecode = textToDecode.substring(idx - 1);
        String code = textToDecode.substring(0, 1);
        //
        if (code.equals("P")) {
            decodePatientInfo(textToDecode);
        } else if (code.equals("L")) {
            decodeTermination(textToDecode);
        } else if (code.equals("H")) {
            ///decodeHeader(textToDecode);
        } else if (code.equals("R")) {
            counter++;
            //MeasureModel measureModel = decodeResult(textToDecode);
            /*
            measureModel.setIdServer("MEASURE_" + Calendar.getInstance().getTimeInMillis() + "_" + counter);
            if (measureModel != null) {
                measureModel.setSynchronized(false);
                measureModel.setPeriodUpdated(false);
                measureModel.setPatientId(patientId);
                measureModel.setPeriodId(PeriodService.getPeriod(measureModel.getDate()).getIdServer());
                measureModel.setType(MeasureType.BLOOD_SUGAR_LEVEL);
                measureModels.add(measureModel);
            }

             */
        } else {
            Log.d("Record type ", code + " is unknown/unsupported.");
        }

        return code;
    }

    private void decodeTermination(String text) {
        // ignored for now
        // System.out.println("TERMINATION RECORD");
    }

    private void decodePatientInfo(String text) {
        // ignored for now
    }

    /*

    private MeasureModel decodeResult(String input) {
        try {
            Resultrecord resultrecord = new Resultrecord();
            MeasureModel measureModel = new MeasureModel();
            //input = input.replace("\\|\\|", "\\|");
            StringTokenizer strtok = new StringTokenizer(input, "|");
            // we search for entry containing Glucose... (in case that data was
            // not received entirely)
            while (strtok.hasMoreElements()) {
                strtok.nextToken();
                strtok.nextToken();
                String s = strtok.nextToken();
                resultrecord.setRecordId(s);
                String val = strtok.nextToken();
                resultrecord.setValue(val);
                String unitAndRef = strtok.nextToken();
                resultrecord.setUnits_ref(unitAndRef);
                String last = strtok.nextToken(); // N/A
                if (last.length() != 12) {
                    resultrecord.setMarkers(last);
                }
                if (last.length() == 12) {
                    resultrecord.setTimestamp(last);
                }
            }
            return buildMeasure(resultrecord);

        } catch (Exception ex) {
            Log.d("Problem decoding result data. Ex.: " + ex, ex);
            return null;
        }
    }

    private MeasureModel buildMeasure(Resultrecord resultrecord) {

        MeasureModel measureModel = new MeasureModel();
        String ref = "";
        String unit = "";
        if (resultrecord.getUnits_ref().length() == 2) {
            unit = resultrecord.getUnits_ref().split("\\^")[0];
        } else {
            String[] unitAndRefTok = resultrecord.getUnits_ref().split("\\^");
            unit = resultrecord.getUnits_ref().split("\\^")[0];
            ref = unitAndRefTok[1];
        }
        measureModel.setValue(Float.valueOf(resultrecord.getValue()));

        if (resultrecord.getRecordId().equals("^^^Glucose") && ref.equals("P")) {
            measureModel.setType(MeasureType.BLOOD_SUGAR_LEVEL); //TODO Demander à chakib si   glucose
            if (unit.equals("mmol/L")) {
                measureModel.setComment("Unit :" + "mmol/l");
                measureModel.setValue(measureModel.getValue() / 18.0182f);
            } else {
                measureModel.setComment("Unit :" + "mg/dl");
            }
            //TODO unite par defaut du glucose
        }
        if (resultrecord.getRecordId().equals("^^^Carb")) {
            if (unit.equals("0")) {
                measureModel.setComment("Unit :" + "Not defined");
            } else if (unit.equals("1")) {
                measureModel.setComment("Unit :" + "Grams");

            } else if (unit.equals("2")) {
                measureModel.setComment("Unit :" + "Points");

            } else if (unit.equals("3")) {
                measureModel.setComment("Unit :" + "Choices");
            }
        }

        if (resultrecord.getRecordId().equals("^^^Insulin")) {
            //TODO Demander à chakib si   glucose
            if (unit.equals("0")) {
                measureModel.setComment("Unit :" + "Not defined");
            } else if (unit.equals("1")) {
                measureModel.setType(MeasureType.RAPID_ACTING_INSULIN);

            } else if (unit.equals("2")) {
                measureModel.setType(MeasureType.LONG_ACTING_INSULIN);
            } else if (unit.equals("3")) {
                measureModel.setComment("Unit :" + "Mixed Insuline");
            }
            measureModel.setValue(measureModel.getValue() / 10.0f);
        }
        try {
            measureModel.setDate(extractDate(resultrecord.getTimestamp()).getTime());
        } catch (SensorReaderException e) {
            e.printStackTrace();
        }
        return measureModel;
    }

    */
    /**
     * Pass in date and time as strings to return a DateTime object
     * @param datetimes
     * @return a date time object parsed with the provided date and time
     * provided
     */
    private static Date extractDate(String datetimes) throws Exception {
        Date dateTime = null;
        if (datetimes.length() == 12) {
            String year = datetimes.substring(0, 4);
            String month = datetimes.substring(4, 6);
            String day = datetimes.substring(6, 8);
            String hours = datetimes.substring(8, 10);
            String min = datetimes.substring(10, 12);
            String dateTimeCombined = day + "." + month + "." + year + " " + hours + ":" + min;
            // TODO add better error handling for converting to a date from string
            SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            try {
                dateTime = dateTimeFormatter.parse(dateTimeCombined);

            } catch (ParseException ex) {
                throw new Exception("Sensor : Bayer Contour Next One ; Invalid date time format found.", ex);

            }
        }
        return dateTime;

    }

}
