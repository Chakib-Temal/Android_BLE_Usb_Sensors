package com.example.bluetoothtest.Models.bleCallBacks.JP1304Pedometer.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

import com.example.bluetoothtest.App;

/**
 * Helper functions to toast message.
 *
 * @version 1.3.0
 * @since 1.3.0
 */
public class ToastUtils {

    public final static String TAG = ToastUtils.class.getSimpleName();

    private static Toast mLastToast;

    /**
     * Toast a message in the UI thread from anywhere in the app.<br />
     *
     * @param resId resource id for the string
     * @param duration the duration of display
     * @param queue true to display the message after the end of the previous message,
     *              false to display the message right now
     */
    public static void toast(@StringRes int resId, final int duration, boolean queue) {

        String message = App.getContext().getString(resId);
        toast(message, duration, queue);
    }

    /**
     * Toast a message in the UI thread from anywhere in the app.<br />
     *
     * @param message the message
     * @param duration the duration of display
     * @param queue true to display the message after the end of the previous message,
     *              false to display the message right now
     */
    public static void toast(final String message, final int duration, boolean queue) {

        if(!queue)
            cancelLastToast();

        toast(message, duration);
    }

    /**
     * Toast a message in the UI thread from anywhere in the app.<br />
     *
     * @param resId resource id for the string
     * @param duration the duration of display
     */
    public static void toast(@StringRes int resId, int duration) {

        String message = App.getContext().getString(resId);
        toast(message, duration);
    }

    /**
     * Toast a message in the UI thread from anywhere in the app.<br />
     *
     * @param message the message
     * @param duration the duration of display
     */
    public static void toast(final String message, final int duration) {

        Log.i(TAG, message);

        if(Looper.myLooper() == Looper.getMainLooper()) {
            toastInternal(message, duration);
            return;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                toastInternal(message, duration);
            }
        });
    }

    private static void toastInternal(final String message, final int duration) {

        Toast toast = Toast.makeText(App.getContext(), message, duration);
        toast.show();
        mLastToast = toast;
    }

    public static void cancelLastToast() {

        if(mLastToast != null) {
            mLastToast.cancel();
            mLastToast = null;
        }
    }
}
