package com.example.bluetoothtest;

import android.app.Application;
import android.content.Context;

/**
 * Created by chakib on 25/05/20.
 */



public class App extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

    }

    public static Context getContext(){
        return mContext;
    }
}
