package com.example.bluetoothtest.Models.LsBleSdk;

import android.util.Log;
import android.widget.Toast;

import com.example.bluetoothtest.App;
import com.lifesense.ble.LsBleManager;
import com.lifesense.ble.PairCallback;
import com.lifesense.ble.bean.LsDeviceInfo;
import com.lifesense.ble.bean.PairedConfirmInfo;
import com.lifesense.ble.bean.constant.DeviceRegisterState;
import com.lifesense.ble.bean.constant.OperationCommand;
import com.lifesense.ble.bean.constant.PairedConfirmState;

import java.util.List;

public class LsBlePairCallBack extends PairCallback {

    public static final String TAG = LsBlePairCallBack.class.getSimpleName();
    private LsDeviceInfo lsDevice;

    public LsBlePairCallBack(LsDeviceInfo lsDevice){
        super();
        this.lsDevice = lsDevice;
    }

    @Override
    public void onPairResults(final LsDeviceInfo lsDevice,final int status)
    {

        if(lsDevice!=null && status==0)
        {
            // Succes
            Log.i(TAG, "Paired succes");
            LsBleScanCallBack.saveDeviceInfo(lsDevice);

        }
        else {
            //showPromptDialog("Prompt", "Pairing failed, please try again");
            Log.i(TAG, "Paired failed");

        }
    }

    @Override
    public void onWifiPasswordConfigResults(LsDeviceInfo lsDevice,
                                            final boolean isSuccess, final int errorCode)
    {

        String msg="success to set device's wifi password ! ";
        if(!isSuccess)
        {
            msg="failed to set device's wifi password : "+errorCode;
        }

        //showPromptDialog("Prompt", msg);

    }

    @Override
    public void onDeviceOperationCommandUpdate(String macAddress,
                                               OperationCommand cmd, Object obj) {
        Log.e("LS-BLE", "operation command update >> "+cmd+"; from device="+macAddress);

        if(OperationCommand.CMD_DEVICE_ID == cmd)
        {
            //input device id
            String deviceId=macAddress.replace(":", "");//"ff028a0003e1";
            //register device's id for device
            LsBleManager.getInstance().registeringDeviceID(macAddress, deviceId, DeviceRegisterState.NORMAL_UNREGISTER);
        }
        else if(OperationCommand.CMD_PAIRED_CONFIRM == cmd){
            PairedConfirmInfo confirmInfo=new PairedConfirmInfo(PairedConfirmState.PAIRING_SUCCESS);
            confirmInfo.setUserNumber(1);
            LsBleManager.getInstance().inputOperationCommand(macAddress, cmd, confirmInfo);
        }

    }

    @Override
    public void onDiscoverUserInfo(String macAddress,final List userList)
    {

        if(userList==null || userList.size()==0)
        {
            Toast.makeText(App.getContext(), "failed to pairing devie,user list is null...", Toast.LENGTH_LONG).show();
            Log.i(TAG, "onDiscoverUserInfo, wtf ? failed to pairing devie,user list is null...");
            return ;
        }

        //showDeviceUserInfo(userList);
        String userName = "chakib";
        LsBleManager.getInstance().bindDeviceUser(lsDevice.getMacAddress(),1, userName.trim());




    }
}
