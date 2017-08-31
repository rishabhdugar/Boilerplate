package com.sarkisian.boilerplate.sync.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;

import com.sarkisian.boilerplate.db.entity.User;
import com.sarkisian.boilerplate.db.handler.TlQueryHandler;
import com.sarkisian.boilerplate.sync.bus.BusProvider;
import com.sarkisian.boilerplate.sync.bus.event.ApiEvent;
import com.sarkisian.boilerplate.sync.bus.event.Event;
import com.sarkisian.boilerplate.sync.rest.HttpRequestManager;
import com.sarkisian.boilerplate.sync.rest.RestHttpClient;
import com.sarkisian.boilerplate.sync.rest.entity.HttpConnection;
import com.sarkisian.boilerplate.util.Constant;
import com.sarkisian.boilerplate.util.Logger;
import com.sarkisian.boilerplate.util.Preference;


public class TlService extends Service {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = TlService.class.getSimpleName();

    private static class Extra {
        static final String URL = "URL";
        static final String POST_ENTITY = "POST_ENTITY";
        static final String SUBSCRIBER = "SUBSCRIBER";
        static final String REQUEST_TYPE = "REQUEST_TYPE";
    }

    // ===========================================================
    // Fields
    // ===========================================================

    private Handler mHandler;
    private HandlerThread mHandlerThread;

    // ===========================================================
    // Util Methods
    // ===========================================================

    /**
     * @param url         - calling api url
     * @param requestType - int constant that helps us to distinguish requests
     * @param postEntity  - POST request entity (json string that must be sent on server)
     * @param subscriber  - object(class) that started service
     */

    public static void start(Context context, String subscriber, String url, String postEntity,
                             int requestType) {
        Intent intent = new Intent(context, TlService.class);
        intent.putExtra(Extra.SUBSCRIBER, subscriber);
        intent.putExtra(Extra.URL, url);
        intent.putExtra(Extra.REQUEST_TYPE, requestType);
        intent.putExtra(Extra.POST_ENTITY, postEntity);
        context.startService(intent);
    }

    public static void start(Context context, String subscriber, String url,
                             int requestType) {
        Intent intent = new Intent(context, TlService.class);
        intent.putExtra(Extra.SUBSCRIBER, subscriber);
        intent.putExtra(Extra.URL, url);
        intent.putExtra(Extra.REQUEST_TYPE, requestType);
        context.startService(intent);
    }

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread(TlService.class.getSimpleName(),
                Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        final String url = intent.getExtras().getString(Extra.URL);
        final String postEntity = intent.getExtras().getString(Extra.POST_ENTITY);
        final String subscriber = intent.getExtras().getString(Extra.SUBSCRIBER);
        final int requestType = intent.getExtras().getInt(Extra.REQUEST_TYPE);
        Logger.i(LOG_TAG, requestType + Constant.Symbol.SPACE + url);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (requestType) {
                    case HttpRequestManager.RequestType.LOG_IN:
                        logInRequest(url, postEntity, subscriber);
                        break;

                    case HttpRequestManager.RequestType.LOG_OUT:
                        logOutRequest(url, subscriber);
                        break;
                }
            }
        });

        // TODO: implement stopSelf according to the project requirements
        // stopSelf(startId);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void logInRequest(String url, String postEntity, String subscriber) {

        HttpConnection httpConnection = HttpRequestManager.executeRequest(
                this,
                RestHttpClient.RequestMethod.POST,
                url,
                null,
                postEntity
        );

        /* For project with working API move below code
           into isHttpConnectionSucceeded block */

        // Save token in prefs
        Preference.getInstance(this).setUserToken("RET45456TY6756HF56456yuty567HH");

        // Save user in DB (in template we create fake user, in your project
        // get server user after login, or implement it how you need)
        TlQueryHandler.addUser(this, new User(145, "David Berligen", "david.berligen@db.com"));

        BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.LOGIN_COMPLETED, subscriber));

        if (httpConnection.isHttpConnectionSucceeded()) {
            String token = httpConnection.getHttpResponseHeader().getToken();
            if (token != null) {
                Logger.i(LOG_TAG, token);

                // Save necessary data after success login

            } else {
                BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.Error.UNKNOWN,
                        subscriber));
            }

        } else {
            Logger.e(LOG_TAG, httpConnection.getHttpConnectionMessage());
            HttpRequestManager.handleFailedRequest(subscriber, httpConnection);
        }
    }

    private void logOutRequest(String url, String subscriber) {

        HttpConnection httpConnection = HttpRequestManager.executeRequest(
                this,
                RestHttpClient.RequestMethod.POST,
                url,
                Preference.getInstance(this).getUserToken(),
                null
        );

        // TODO: Implement logout logic depending on project demands
        if (httpConnection.isHttpConnectionSucceeded()) {
        }

        // Drop user token and other necessary data (e.g. DB tables)
        Preference.getInstance(this).setUserToken(null);
        BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.LOGOUT_COMPLETED, subscriber));
    }
}