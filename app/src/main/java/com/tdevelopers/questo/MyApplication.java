package com.tdevelopers.questo;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.FirebaseDatabase;
import com.tdevelopers.questo.libraries.CustomExceptionHandler;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        AppEventsLogger.activateApp(this);
        mInstance = this;

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
            ));
        }

    }


}
