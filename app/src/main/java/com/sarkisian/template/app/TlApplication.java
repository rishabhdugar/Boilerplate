package com.sarkisian.template.app;

import android.app.Application;
import android.os.StrictMode;

import com.sarkisian.template.BuildConfig;

public class TlApplication extends Application {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = TlApplication.class.getName();

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onCreate() {
        super.onCreate();
        turnOnStrictMode();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void turnOnStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}