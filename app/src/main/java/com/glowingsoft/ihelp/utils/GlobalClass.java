package com.glowingsoft.ihelp.utils;

/**
 * Created by mg on 2/11/2017.
 */


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.net.UnknownHostException;

import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

public class GlobalClass extends Application implements Application.ActivityLifecycleCallbacks {

   public String BASE_URL = "http://glowingsoft.com/ihelp/";
   // public String BASE_URL = "http://192.168.2.109/ihelp/";
    private static GlobalClass singleton;
    boolean applicationOnPause = false;

    public static GlobalClass getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        Log.d("application","start");
        registerActivityLifecycleCallbacks(this);
    }
    public void snakbar(View mView, String Message, int mTextCol, int mBgCol) {
//        Snackbar mSnackbar;
//        mSnackbar = Snackbar.make(mView, Message, Snackbar.LENGTH_LONG);// Sn
//        mView = mSnackbar.getView();
//        mView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), mBgCol));
//        TextView tv = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
//        tv.setTextColor(ContextCompat.getColor(getApplicationContext(), mTextCol));
//        mSnackbar.show();
    }

    public static boolean isOnline(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public String webError(int mErrorCode, Throwable arg3) {
        boolean flag_error = false;
        String mResult = "";
        switch (mErrorCode) {
            case 400:
                mResult = "Error No = 400 Bad Request ";
                flag_error = true;
                break;
            case 401:
                mResult = "Error No = 401 Unauthorized ";
                flag_error = true;
                break;
            case 403:
                mResult = "Error No = 403 Forbidden ";
                flag_error = true;
                break;
            case 404:
                mResult = "Error No = 404 Page Not Found ";
                flag_error = true;
                break;
            case 500:
                mResult = "Error No = 500 Internal Server Error ";
                flag_error = true;
                break;
            case 502:
                mResult = "Error No = 502 Bad Gateway ";
                flag_error = true;
                break;
            case 503:
                mResult = "Error No = 503 Service Unavailable ";
                flag_error = true;
                break;
            case 511:
                mResult = "Error No = 503 Service Unavailable ";
                flag_error = true;
                break;
        }
        if (arg3 instanceof UnknownHostException) {
            mResult = "Unable to resolve host Check Internet Access";
            flag_error = true;
        }
        if (arg3 instanceof ConnectTimeoutException) {
            mResult = "Fail Connection Time Out";
            flag_error = true;
        }
        if (!flag_error) {
            mResult = "StatusCode = " + mErrorCode + " " + arg3.getMessage();
        }
        return mResult;
    }

    @Override
    public void onActivityCreated(Activity arg0, Bundle arg1) {
        Log.e("","onActivityCreated");

    }
    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("","onActivityDestroyed ");

    }
    @Override
    public void onActivityPaused(Activity activity) {
        applicationOnPause = true;
        Log.e("","onActivityPaused "+activity.getClass());

    }
    @Override
    public void onActivityResumed(Activity activity) {
        applicationOnPause = false;
        Log.e("","onActivityResumed "+activity.getClass());

    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("","onActivitySaveInstanceState");

    }
    @Override
    public void onActivityStarted(Activity activity) {
        Log.e("","onActivityStarted");

    }
    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("","onActivityStopped");

    }
}
