package com.example.bluetoothtest.Models.Usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.example.bluetoothtest.App;
import com.example.bluetoothtest.MainActivity;
import com.example.bluetoothtest.Models.Usb.medtronic640.MedtronicCnlReader;
import com.example.bluetoothtest.Models.Usb.medtronic640.PumpInfo;
import com.example.bluetoothtest.Models.Usb.medtronic640.PumpStatusEvent;
import com.example.bluetoothtest.Models.Usb.medtronic640.UsbHidDriver;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.EncryptionException;
import com.example.bluetoothtest.Models.Usb.medtronic640.exception.UnexpectedMessageException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;


public class UsbBroadCast extends BroadcastReceiver {

    private static final String TAG = UsbBroadCast.class.getSimpleName();

    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    MedtronicPumpOperation mCurrentOperation;

    private UsbManager mUsbManager;

    public final static int USB_VID = 0x1a79;
    public final static int USB_PID = 0x6210;
    public final static long USB_WARMUP_TIME_MS = 5000L;
    private long pumpClockDifference;


    private static UsbHidDriver mHidDevice;
    public final static String DEVICE_HEADER = "medtronic-600://";

    MedtronicCnlReader cnlReader;

    private static boolean isConnected = false;
    //==============================================================================================
    // Request permission Parameters
    //==============================================================================================
    private UsbManager usbManager;
    private PendingIntent permissionIntent;

    public UsbBroadCast(){
        usbManager = (UsbManager) App.getContext().getSystemService(Context.USB_SERVICE);
        permissionIntent = PendingIntent.getBroadcast(App.getContext(), 0, new Intent(ACTION_USB_PERMISSION), 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        /**
         * when we attach the sensor, so request the permission
         */
        if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {

            UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            usbManager.requestPermission(usbDevice, permissionIntent);
        }

        if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {

            isConnected = false;
        }

        /**
         * All configuration sensors, it's is the parent Controller
         */
        if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, true)) {
                UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                Log.i(TAG, "New Device");

                if (isConnected){
                    return;
                }

                MainActivity.writeTextSucces("Permission Succes");

                try {
                    mCurrentOperation = MedtronicPumpOperation.OPEN_DEVICE;
                    openDevice();


                    try {


                        //==============================================================================================
                        // Request Serial Number
                        //==============================================================================================
                        mCurrentOperation = MedtronicPumpOperation.GET_SERIAL_NUMBER;

                        cnlReader = new MedtronicCnlReader(mHidDevice);
                        Log.d(TAG, "Connecting to Contour Next Link [pid" + android.os.Process.myPid() + "]");

                        cnlReader.requestDeviceInfo();
                        String serialNumber  = cnlReader.getStickSerial();
                        Log.i(TAG, serialNumber);


                        //==============================================================================================
                        // Request Control && session
                        //==============================================================================================
                        mCurrentOperation = MedtronicPumpOperation.GET_SESSION_AND_CONTROL_MODE;

                        cnlReader.getPumpSession().setStickSerial(serialNumber);
                        cnlReader.enterControlMode();
                        MainActivity.writeTextSucces("OK Control Mode");




                        try {



                            //==============================================================================================
                            // Request PumP MAC
                            //==============================================================================================
                            mCurrentOperation = MedtronicPumpOperation.GET_MAC_ADRESS;

                            cnlReader.enterPassthroughMode();

                            cnlReader.openConnection();

                            cnlReader.requestReadInfo();

                            cnlReader.requestLinkKey();

                            final long pumpMAC = cnlReader.getPumpSession().getPumpMAC();
                            Log.i(TAG, "PumpInfo MAC: " + (pumpMAC & 0xFFFFFF));



                            //==============================================================================================
                            // Negotiate channel
                            //==============================================================================================
                            mCurrentOperation = MedtronicPumpOperation.NEGITIATE_CHANNEL;

                            PumpInfo activePump = new PumpInfo();
                            activePump.setPumpMac(cnlReader.getPumpSession().getPumpMAC());


                            final byte radioChannel = cnlReader.negotiateChannel(activePump.getLastRadioChannel());
                            isConnected = true;

                            if (radioChannel == 0) {
                                //throw new Throwable("Could not communicate with the pump. Is it nearby?");
                            }

                            // if radioChannel == 20 ??
                            Log.i(TAG, String.format("Connected on channel %d  RSSI: %d%%", radioChannel, cnlReader.getPumpSession().getRadioRSSIpercentage()));



                            // read pump status
                            final PumpStatusEvent pumpRecord = new PumpStatusEvent();

                            final String deviceName = DEVICE_HEADER + cnlReader.getStickSerial();
                            Log.i(TAG, "device name " + deviceName);
                            pumpRecord.setDeviceName(deviceName);

                            cnlReader.getPumpTime();
                            pumpClockDifference = cnlReader.getSessionClockDifference();

                            pumpRecord.setPumpMAC(pumpMAC);
                            pumpRecord.setEventDate(cnlReader.getSessionDate());
                            pumpRecord.setEventRTC(cnlReader.getSessionRTC());
                            pumpRecord.setEventOFFSET(cnlReader.getSessionOFFSET());
                            pumpRecord.setClockDifference(pumpClockDifference);
                            cnlReader.updatePumpStatus(pumpRecord);

                            if (pumpRecord.isCgmActive()) {
                                pumpRecord.setCgmOldWhenNewExpected(true);
                            }else {
                                throw new Exception("CGM Not Active");
                            }


                            //validatePumpRecord(pumpRecord, activePump);

                            //pumpRecord.isCgmOldWhenNewExpected()
                            Log.i(TAG, "Battery " + pumpRecord.getBatteryPercentage());


                        } catch (TimeoutException e) {
                            Log.e(TAG, "Timeout communicating with the Contour Next Link.", e);
                        } catch (ChecksumException e) {
                            Log.e(TAG, "Checksum error getting message from the Contour Next Link.", e);
                        } catch (EncryptionException e) {
                            Log.e(TAG, "Error decrypting messages from Contour Next Link.", e);
                        } catch (NoSuchAlgorithmException e) {
                            Log.e(TAG, "Could not determine CNL HMAC", e);
                        } finally {
                            try {
                                cnlReader.closeConnection();
                                cnlReader.endPassthroughMode();
                                cnlReader.endControlMode();
                            } catch (NoSuchAlgorithmException ignored) {
                            }
                        }


                    } catch (IOException e) {
                        Log.e(TAG, "Error connecting to Contour Next Link.", e);
                    } catch (ChecksumException e) {
                        Log.e(TAG, "Checksum error getting message from the Contour Next Link.", e);
                    } catch (EncryptionException e) {
                        Log.e(TAG, "Error decrypting messages from Contour Next Link.", e);
                    } catch (TimeoutException e) {
                        Log.e(TAG, "Timeout communicating with the Contour Next Link.", e);
                    } catch (UnexpectedMessageException e) {
                        Log.e(TAG, "Could not close connection.", e);
                    }

                }catch (Exception e){

                    Log.e(TAG, "Unexpected Error! " + Log.getStackTraceString(e));

                }finally {

                    if (mHidDevice != null) {
                        Log.i(TAG, "Closing serial device...");
                        mHidDevice.close();
                        mHidDevice = null;
                    }

                }


            }
        }

    }

    private void openDevice() throws Exception {

        mUsbManager = (UsbManager) App.getContext().getSystemService(Context.USB_SERVICE);

        UsbDevice cnlStick = UsbHidDriver.getUsbDevice(mUsbManager, USB_VID, USB_PID);

        if (cnlStick == null)
            throw new Exception("sensor null, check PID , VID");

        mHidDevice = UsbHidDriver.acquire(mUsbManager, cnlStick);

        try {
            mHidDevice.open();
        } catch (Exception e) {
            throw new Exception("Unable to open Serial device");
        }

    }


    /*
    private void checkCGM(PumpStatusEvent pumpRecord) throws Exception {
        if (pumpRecord.isCgmActive()) {

            if (pumpRecord.isCgmWarmUp())
                throw new Exception("sensor is in warm-up phase");
            else if (pumpRecord.getCalibrationDueMinutes() == 0)
                throw new Exception("sensor calibration is due now!");
            else if (pumpRecord.getSgv() == 0 && pumpRecord.isCgmCalibrating())
                throw new Exception("sensor is calibrating");
            else if (pumpRecord.getSgv() == 0)
                throw new Exception("sensor error");

            else {
                if (pumpRecord.isCgmCalibrating())
                    UserLogMessage.send(mContext, UserLogMessage.TYPE.CGM, R.string.ul_poll__sensor_is_calibrating);
            }

            if (pumpRecord.isCgmOldWhenNewExpected()) {
                UserLogMessage.send(mContext, UserLogMessage.TYPE.CGM, R.string.ul_poll__old_cgm_event_received);
                if (!pumpRecord.isCgmWarmUp()) {
                    // pump may have missed sensor transmission or be delayed in posting to status message
                    // in most cases the next scheduled poll will have latest sgv, occasionally it is available this period after a delay
                    pollInterval = dataStore.isSysEnablePollOverride() ? dataStore.getSysPollOldSgvRetry() : POLL_OLDSGV_RETRY_MS;
                }
            }

        } else {
            pumpCgmNA++; // poll clash detection
            if (pumpRecord.isCgmLostSensor()) {
                pumpLostSensorError++; // only count errors if cgm is being used
                UserLogMessage.send(mContext, UserLogMessage.TYPE.CGM, R.string.ul_poll__no_cgm_pump_lost_sensor);
            } else {
                UserLogMessage.send(mContext, UserLogMessage.TYPE.CGM, R.string.ul_poll__no_cgm);
            }
        }
    }


     */

    public enum MedtronicPumpOperation{

        OPEN_DEVICE,
        GET_SERIAL_NUMBER,
        GET_SESSION_AND_CONTROL_MODE,
        GET_MAC_ADRESS,
        NEGITIATE_CHANNEL;

    }

}
