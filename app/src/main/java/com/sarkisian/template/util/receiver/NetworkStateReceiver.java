package com.sarkisian.template.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sarkisian.template.io.bus.BusProvider;
import com.sarkisian.template.io.bus.event.Event;
import com.sarkisian.template.io.bus.event.NetworkEvent;

public class NetworkStateReceiver extends BroadcastReceiver {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = NetworkStateReceiver.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private static NetworkStateReceiver sInstance;

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
            // TODO: Implement unregister logic depending on project demands
            // unregisterBroadcast(context);

        } else {
            BusProvider.getInstance().post(new NetworkEvent(Event.EventType.Network.DISCONNECTED));
        }
    }

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public void registerBroadcast(Context context) {
        context.registerReceiver(sInstance,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void unregisterBroadcast(Context context) {
        try {
            context.unregisterReceiver(sInstance);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
