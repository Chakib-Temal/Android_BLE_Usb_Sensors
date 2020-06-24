package com.example.bluetoothtest.Models.LsBleSdk;

import android.util.Log;

import com.example.bluetoothtest.MainActivity;
import com.example.bluetoothtest.Models.LsBleSdk.view.OnDialogClickListener;
import com.example.bluetoothtest.Models.LsBleSdk.view.ShowTextDialogFragment;
import com.lifesense.ble.LsBleManager;
import com.lifesense.ble.SearchCallback;
import com.lifesense.ble.bean.LsDeviceInfo;
import com.lifesense.ble.bean.WeightUserInfo;
import com.lifesense.ble.bean.constant.DeviceTypeConstants;
import com.lifesense.ble.bean.constant.ProtocolType;
import com.lifesense.ble.bean.constant.SexType;
import com.lifesense.ble.bean.constant.UnitType;

public class LsBleScanCallBack extends SearchCallback {

    public static final String TAG = LsBleScanCallBack.class.getSimpleName();


    @Override
    public void onSearchResults(LsDeviceInfo lsDevice)
    {

        Log.i(TAG, lsDevice.toString());

        if(lsDevice.getPairStatus()==1
                || (ProtocolType.A6.toString().equalsIgnoreCase(lsDevice.getProtocolType())
                &&  lsDevice.getRegisterStatus() ==0 ))
        {

            Log.i(LsBlePairCallBack.TAG, "MAMBI IS HERE ...");
            LsBleManager.getInstance().stopSearch();
            setProductUserInfoOnPairingMode(lsDevice);

            //S9 互联秤，在绑定过程中添加用户信息
            WeightUserInfo userInfo = new WeightUserInfo();
            userInfo.setProductUserNumber(2);
            userInfo.setAge(30);
            userInfo.setHeight((float) 1.70);
            userInfo.setWeight(40);
            userInfo.setSex(SexType.MALE);
            userInfo.setAthlete(true);
            userInfo.setAthleteActivityLevel(3);
            //currentPairingDevice.setUserInfo(userInfo);
            //直接配对设备
            Log.i(LsBlePairCallBack.TAG, "Trying to Pair ...");

            LsBleManager.getInstance().pairingWithDevice(lsDevice, new LsBlePairCallBack());

        }

        else if (lsDevice.getDeviceName().equals("MAMBO")) {
            LsBleManager.getInstance().stopSearch();
            lsDevice.setDeviceId(lsDevice.getMacAddress().replace(":", ""));
            saveDeviceInfo(lsDevice);

        }

    }

    private void saveDeviceInfo(final LsDeviceInfo lsDevice){

        ShowTextDialogFragment showInfoDialog=new ShowTextDialogFragment(lsDevice,new OnDialogClickListener()
        {
            @Override
            public void onDialogCancel()
            {

                MainActivity.LsopenDeviceFragment(lsDevice);
            }
        });

        try{

            showInfoDialog.show(MainActivity.mFragmentManager, "show info");

        } catch (ClassCastException e) {
            Log.d(TAG, "Can't get the fragment manager with this");
        }

    }



    /**
     * set product user info on pairing mode
     * @param device
     */
    private void setProductUserInfoOnPairingMode(LsDeviceInfo lsDevice)
    {
        if(lsDevice==null)
        {
            return ;
        }
        /**
         * in some old products, such as A2, A3,
         * you can change the device's settings info before pairing
         */
        if(DeviceTypeConstants.FAT_SCALE.equalsIgnoreCase(lsDevice.getDeviceType())
                ||DeviceTypeConstants.WEIGHT_SCALE.equalsIgnoreCase(lsDevice.getDeviceType()))
        {
            /**
             * optional step,set product user info in pairing mode
             */
            WeightUserInfo weightUserInfo=new WeightUserInfo();
            weightUserInfo.setAge(34);					//user age
            weightUserInfo.setHeight((float) 1.78); 	//unit of measurement is m
            weightUserInfo.setGoalWeight(78);			//unit of measurement is kg
            weightUserInfo.setUnit(UnitType.UNIT_KG);	//measurement unit
            weightUserInfo.setSex(SexType.FEMALE);		//user gender
            weightUserInfo.setAthlete(true);			//it is an athlete
            weightUserInfo.setAthleteActivityLevel(3);	//athlete level
            //set device macAddress
            weightUserInfo.setMacAddress(lsDevice.getMacAddress());
            //calling interface
            LsBleManager.getInstance().setProductUserInfo(weightUserInfo);
        }
    }

}
