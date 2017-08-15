package com.sarkisian.template.app;

import android.app.Application;
import android.os.StrictMode;

import com.sarkisian.template.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

public class TlApplication extends Application {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = TlApplication.class.getName();

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onCreate() {
        super.onCreate();
        turnOnStrictMode();
        installLeakCanary();
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

    private void installLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}