package com.example.bluetoothtest;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.example.bluetoothtest.Models.Usb.UsbBroadCast;
import com.example.bluetoothtest.Models.bleCallBacks.Accu_chek_Guide.AccuCheckBleGattCallBack;
import com.example.bluetoothtest.Models.bleCallBacks.OneTouch.verioFlex.OneTouchVerioFlexBleCallBack;
import com.example.bluetoothtest.Models.bleCallBacks.OneTouch.verioReflect.OneTouchVerioReflectBleCallBack;
import com.example.bluetoothtest.Models.bleCallBacks.Oxymeter.OxymeterBleGattCallback;
import com.example.bluetoothtest.Models.permissions.MarshlowPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements LoadingDataInterfaceCallBack{

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String ONE_TOUCH_VERIO_FLEX_MAC_ADDRESS = "F2:81:36:C8:3E:80";
    public static final String ONE_TOUCH_VERIO_REFLECT_MAC_ADDRESS = "FD:25:BE:48:78:51";

    private static final String SLEEP_BAND_MAC_ADDRESS = "DB:2A:2D:69:E9:E4";

    private static final String OXY_MAC_ADDRESS = "98:7B:F3:6B:33:4";

    private static final String ACCU_CHEK_MAC_ADRESS = "50:51:A9:7C:60:62";


    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 50;

    private static TextView textHold;
    private BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE_BT = 1;

    private int scanCounter = 0;

    public static LoadingDataInterfaceCallBack loadingDataListener;

    private ArrayAdapter arrayAdapter;


    private UsbBroadCast usbBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+ Permission APIs
            MarshlowPermissions permissions = MarshlowPermissions.getInstance(this);
            permissions.fuckMarshMallow();
        }
        loadingDataListener = this;

        setContentView(R.layout.activity_main);
        textHold = (TextView) findViewById(R.id.textHold);

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish();
        }
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();



    }

    private BleScanCallback mBleScanCallBack = new BleScanCallback() {
        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
            scanCounter++;

            Log.i(TAG, "scan finish");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (scanCounter < 7)
                BleManager.getInstance().scan(this);
        }

        @Override
        public void onScanStarted(boolean success) {
            Log.i(TAG, "scan start");
        }

        @Override
        public void onScanning(BleDevice bleDevice) {


            switch (bleDevice.getMac()){
                case ONE_TOUCH_VERIO_REFLECT_MAC_ADDRESS:
                    BleManager.getInstance().connect(bleDevice, new OneTouchVerioReflectBleCallBack());

                    break;

                case ONE_TOUCH_VERIO_FLEX_MAC_ADDRESS:
                    BleManager.getInstance().connect(bleDevice, new OneTouchVerioFlexBleCallBack());
                    break;

                case OXY_MAC_ADDRESS:
                    BleManager.getInstance().connect(bleDevice, new OxymeterBleGattCallback());
                    break;

                case SLEEP_BAND_MAC_ADDRESS:

                    //BleManager.getInstance().connect(bleDevice, new SleepBandBleCallBack());
                    break;

                case ACCU_CHEK_MAC_ADRESS:

                    BleManager.getInstance().connect(bleDevice, new AccuCheckBleGattCallBack());
                    break;


            }

        }
    };

    public static void writeTextSucces(String message){

        textHold.setText(message);

    }

    @Override
    public void onLoadDataIsFinish(List<String> measureList) {

        if (!measureList.isEmpty()){

            ListView listView = (ListView) findViewById(R.id.zone_list);

            arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, measureList);

            listView.setAdapter(arrayAdapter);

            textHold.setText("here is you data");


        }else {
            textHold.setText("End communication, No data");

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File myFile = new File(path, "mytextfile.txt");
            FileOutputStream fOut = new FileOutputStream(myFile,true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append("the text I want added to the file");
            myOutWriter.close();
            fOut.close();

            Toast.makeText(this,"Text file Saved !",Toast.LENGTH_LONG).show();
        }

        catch (java.io.IOException e) {

            //do something if an IOException occurs.
            Toast.makeText(this,"ERROR - Text could't be added",Toast.LENGTH_LONG).show();
        }


        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        scanCounter = 0;

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance().enableBluetooth();
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(null)
                .setDeviceName(true, "")
                .setDeviceMac("")
                .setScanTimeOut(20000)
                .build();

        BleManager.getInstance().initScanRule(scanRuleConfig);

        BleManager.getInstance().scan(mBleScanCallBack);

        usbBroadCast = new UsbBroadCast();

        App.getContext().registerReceiver(usbBroadCast, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
        App.getContext().registerReceiver(usbBroadCast, new IntentFilter(UsbBroadCast.ACTION_USB_PERMISSION));
    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(usbBroadCast);
        }catch (Exception E){

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * PERMISSIONS
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this, "All Permission GRANTED !! Thank You :)", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "One or More Permissions are DENIED Exiting App :(", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
