package com.domain.template.io.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.domain.template.io.bus.BusProvider;
import com.domain.template.io.bus.event.ApiEvent;
import com.domain.template.io.bus.event.Event;
import com.domain.template.io.rest.HttpRequestManager;
import com.domain.template.io.rest.entity.HttpConnection;
import com.domain.template.io.rest.util.HttpErrorUtil;
import com.domain.template.util.Constant;
import com.domain.template.BuildConfig;
import com.domain.template.io.rest.RestHttpClient;
import com.domain.template.util.Preference;


public class TIntentService extends IntentService {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = TIntentService.class.getSimpleName();

    private class Extra {
        static final String URL = "URL";
        static final String DATA = "DATA";
        static final String SUBSCRIBER = "SUBSCRIBER";
        static final String REQUEST_TYPE = "REQUEST_TYPE";
        static final String REQUEST_MODE = "REQUEST_MODE";
    }

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public TIntentService() {
        super(TIntentService.class.getName());
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Helper methods
    // ===========================================================

    /**
     * @param url         - calling api url
     * @param requestType - string constant that helps us to distinguish what request it is
     * @param data        - usually POST request entity (json string that must be sent on server)
     * @param subscriber  - object(class) that started service
     */
    public static void start(Context context, String subscriber, String url, String data, int requestType) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, TIntentService.class);
        intent.putExtra(Extra.SUBSCRIBER, subscriber);
        intent.putExtra(Extra.URL, url);
        intent.putExtra(Extra.REQUEST_TYPE, requestType);
        intent.putExtra(Extra.DATA, data);
        context.startService(intent);
    }

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getExtras().getString(Extra.URL);
        String data = intent.getExtras().getString(Extra.DATA);
        String subscriber = intent.getExtras().getString(Extra.SUBSCRIBER);
        int requestType = intent.getExtras().getInt(Extra.REQUEST_TYPE);
        int requestMode = intent.getExtras().getInt(Extra.REQUEST_MODE);
        if (BuildConfig.isDEBUG) Log.i(LOG_TAG, requestType + Constant.Symbol.SPACE + url);

        switch (requestType) {
            case HttpRequestManager.RequestType.LOGIN_IN:
                loginInRequest(url, data, subscriber);
                break;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void loginInRequest(String url, String data, String subscriber) {

        HttpConnection httpConnection = HttpRequestManager.executeRequest(
                this,
                RestHttpClient.RequestMethod.POST,
                url,
                null,
                data
        );

        // TODO: 1/10/17 Move into SUCCESS block for valid API
        Preference.getInstance(this).setUserToken("356RT5465east");
        Preference.getInstance(this).setUserName("David McElan");
        BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.LOGIN_COMPLETED, subscriber));

        if (httpConnection.isHttpConnectionSucceeded()) {
            String token = httpConnection.getHttpResponseHeader().getToken();
            if (token != null) {
                if (BuildConfig.isDEBUG) Log.i(LOG_TAG, token);
            } else {
                BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.Error.UNKNOWN, subscriber));
            }
        } else {
            if (BuildConfig.isDEBUG) Log.e(LOG_TAG, httpConnection.getHttpConnectionMessage()
                    + Constant.Symbol.SPACE + httpConnection.getHttpConnectionCode());
            handleFailedConnection(subscriber, httpConnection);
        }
    }

    private void handleFailedConnection(String subscriber, HttpConnection httpConnection) {
        switch (httpConnection.getHttpConnectionCode()) {
            case HttpErrorUtil.NumericStatusCode.HTTP_NO_NETWORK:
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