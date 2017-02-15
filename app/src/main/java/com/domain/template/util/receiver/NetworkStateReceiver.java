package com.domain.template.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.domain.template.io.bus.BusProvider;
import com.domain.template.io.bus.event.Event;
import com.domain.template.io.bus.event.NetworkEvent;

public class NetworkStateReceiver extends BroadcastReceiver {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = NetworkStateReceiver.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private static NetworkStateReceiver sInstance;
    private static boolean isRegistered;

    // ===========================================================
    // Constructors
    // ===========================================================

    public static NetworkStateReceiver getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkStateReceiver();
        }
        return sInstance;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null) {
            BusProvider.getInstance().post(new NetworkEvent(Event.EventType.Network.CONNECTED));
            unregisterBroadcast(context);
        }
    }

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public static void registerBroadcast(Context context) {
        isRegistered = true;
        context.registerReceiver(NetworkStateReceiver.getInstance(),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static void unregisterBroadcast(Context context) {
        try {
            if (isRegistered) {
                context.unregisterReceiver(sInstance);
                isRegistered = false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
