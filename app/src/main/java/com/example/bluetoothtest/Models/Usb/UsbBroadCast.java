package com.example.bluetoothtest.Models.Usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.example.bluetoothtest.App;
import com.example.bluetoothtest.MainActivity;
import com.example.bluetoothtest.Models.Usb.devices.AccuChek_Guide;
import com.example.bluetoothtest.Models.Usb.devices.Devices;


public class UsbBroadCast extends BroadcastReceiver {

    private static final String TAG = UsbBroadCast.class.getSimpleName();

    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

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

        /**
         * All configuration sensors, it's is the parent Controller
         */
        if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, true)) {
                UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                try {


                    MainActivity.writeTextSucces("Succes");

                } catch (Exception e) {
                    MainActivity.writeTextSucces(e.getMessage());
                }


            }
        }

    }

    private UsbSensor getInitialInformationOfSensor(UsbDevice usbDevice) throws Exception {
        UsbInfo usbInfo = new UsbInfo();

        if (usbDevice.getVendorId() == Devices.ACCU_CHEK_GUIDE_VENDOR_ID &&
                usbDevice.getProductId() == Devices.ACCU_CHEK_GUIDE_PRODUCT_ID ) {

            usbInfo.setVendorId(Devices.ACCU_CHEK_GUIDE_VENDOR_ID);
            usbInfo.setProductId(Devices.ACCU_CHEK_GUIDE_PRODUCT_ID);

            return new AccuChek_Guide(usbInfo, usbDevice);
        }

        else if (usbDevice.getVendorId() == Devices.MY_STAR_DOSECOACH_VENDOR_ID &&
                usbDevice.getProductId() == Devices.MY_STAR_DOSECOACH_PRODUCT_ID ) {

            usbInfo.setVendorId(Devices.ACCU_CHEK_GUIDE_VENDOR_ID);
            usbInfo.setProductId(Devices.ACCU_CHEK_GUIDE_PRODUCT_ID);

            return new AccuChek_Guide(usbInfo, usbDevice);
        }

        throw new Exception("Sensor not supported ");

    }


}
