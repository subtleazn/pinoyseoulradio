package com.pinoyseoul.radio;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.onesignal.OneSignal;

public class Apps extends Application {

    private static final String ONESIGNAL_APP_ID = "939145d8-14fb-4065-93cc-57a0ba801451";

    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}
