package com.sarkisian.template.io.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sarkisian.template.db.entity.User;
import com.sarkisian.template.db.handler.TlQueryHandler;
import com.sarkisian.template.io.bus.BusProvider;
import com.sarkisian.template.io.bus.event.ApiEvent;
import com.sarkisian.template.io.bus.event.Event;
import com.sarkisian.template.io.rest.HttpRequestManager;
import com.sarkisian.template.io.rest.RestHttpClient;
import com.sarkisian.template.io.rest.entity.HttpConnection;
import com.sarkisian.template.io.rest.util.HttpErrorUtil;
import com.sarkisian.template.util.Constant;
import com.sarkisian.template.util.Logger;
import com.sarkisian.template.util.Preference;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TlService extends Service {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = TlService.class.getSimpleName();
    private static final int THREAD_POOL_SIZE = 5; // Runtime.getRuntime().availableProcessors()

    private class Extra {
        static final String URL = "URL";
        static final String POST_ENTITY = "POST_ENTITY";
        static final String SUBSCRIBER = "SUBSCRIBER";
        static final String REQUEST_TYPE = "REQUEST_TYPE";
    }

    // ===========================================================
    // Fields
    // ===========================================================

    private ExecutorService mExecutorService;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Start/stop commands
    // ===========================================================

    /**
     * @param url         - calling api url
     * @param requestType - string constant that helps us to distinguish what request it is
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
        mExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mExecutorService.execute(new RunnableTask(intent, startId));
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdown();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ===========================================================
    // Inner classes
    // ===========================================================

    class RunnableTask implements Runnable {

        int startId;
        Intent intent;

        RunnableTask(Intent intent, int startId) {
            this.startId = startId;
            this.intent = intent;
        }

        public void run() {
            String url = intent.getExtras().getString(Extra.URL);
            String postEntity = intent.getExtras().getString(Extra.POST_ENTITY);
            String subscriber = intent.getExtras().getString(Extra.SUBSCRIBER);
            int requestType = intent.getExtras().getInt(Extra.REQUEST_TYPE);
            Logger.i(LOG_TAG, requestType + Constant.Symbol.SPACE + url);

            switch (requestType) {
                case HttpRequestManager.RequestType.LOG_IN:
                    logInRequest(url, postEntity, subscriber);
                    break;

                case HttpRequestManager.RequestType.LOG_OUT:
                    logOutRequest(url, subscriber);
                    break;
            }

            // TODO: implement according to the project requirements
            stopSelf(startId);
        }
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

        /* For your project (with working API) move this code
           in isHttpConnectionSucceeded block */

        // Save token in prefs
        Preference.getInstance(this).setUserToken("RET45456TY6756HF56456yuty567HH");

        // Save user in DB (in template we create fake user, in your project
        // get server user after login, or implement it how you need)
        TlQueryHandler.addUser(this, new User(145, "David Berligon", "david.berligon@db.com"));

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
            Logger.e(LOG_TAG, httpConnection.getHttpConnectionMessage()
                    + Constant.Symbol.SPACE + httpConnection.getHttpConnectionCode());
            handleFailedConnection(subscriber, httpConnection);
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

        /* Technically there is no sense to inform user that something went wrong by logout,
           when user click logout he must logout no matter which response is received from server,
           it is our problem, so we do not wait for response */

        // Drop user token and other necessary data (e.g. DB tables )
        // depending on your implementation
        Preference.getInstance(this).setUserToken(null);
        BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.LOGOUT_COMPLETED, subscriber));

        if (httpConnection.isHttpConnectionSucceeded()) {
            Logger.i(LOG_TAG, "Logged out on server");
        }
    }

    // ===========================================================
    // Util
    // ===========================================================

    private void handleFailedConnection(String subscriber, HttpConnection httpConnection) {
        switch (httpConnection.getHttpConnectionCode()) {
            case HttpErrorUtil.NumericStatusCode.HTTP_NO_NETWORK:
            case HttpErrorUtil.NumericStatusCode.UNABLE_TO_RESOLVE_HOST:
                BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.Error.NO_NETWORK,
                        subscriber));
                break;

            case HttpErrorUtil.NumericStatusCode.HTTP_SERVER_TIMEOUT:
                BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.Error.SERVER_TIMEOUT,
                        subscriber));
                break;

            case HttpErrorUtil.NumericStatusCode.HTTP_UNKNOWN_SERVER_ERROR:
                BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.Error.UNKNOWN,
                        subscriber));
                break;

            case HttpErrorUtil.NumericStatusCode.HTTP_CONNECTION_REFUSED:
                BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.Error.CONNECTION_REFUSED,
                        subscriber));
                break;

            case HttpErrorUtil.NumericStatusCode.HTTP_UNAUTHORIZED:
                BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.Error.UNAUTHORIZED,
                        subscriber));
                break;

            case HttpErrorUtil.NumericStatusCode.HTTP_BAD_REQUEST:
                BusProvider.getInstance().post(new ApiEvent<>(httpConnection.getHttpResponseBody().toString(),
                        Event.EventType.Api.Error.BAD_REQUEST, subscriber));
                break;

            case HttpErrorUtil.NumericStatusCode.HTTP_NOT_FOUND:
                BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.Error.PAGE_NOT_FOUND,
                        subscriber));
                break;

            default:
                BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.Error.UNKNOWN,
                        subscriber));
                break;
        }
    }
}