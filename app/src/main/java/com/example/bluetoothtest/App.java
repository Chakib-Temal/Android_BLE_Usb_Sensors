package com.example.bluetoothtest;

import android.app.Application;
import android.content.Context;

import com.lifesense.weidong.lzsimplenetlibs.common.ApplicationHolder;

/**
 * Created by chakib on 25/05/20.
 */



public class App extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        ApplicationHolder.setmApplication(this);


    }

    public static Context getContext(){
        return mContext;
    }
}
