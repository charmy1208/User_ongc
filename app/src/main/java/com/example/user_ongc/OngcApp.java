package com.example.user_ongc;

import android.app.Application;

import com.onesignal.OneSignal;

public class OngcApp extends Application {

    private static final String ONESIGNAL_APP_ID ="e857dac4-c6e1-4292-b176-e9004ce7932f";


    @Override
    public void onCreate() {
        super.onCreate();
        //onesignal initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}
