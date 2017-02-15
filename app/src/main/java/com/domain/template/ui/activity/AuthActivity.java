package com.domain.template.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.domain.template.BuildConfig;
import com.domain.template.R;
import com.domain.template.io.bus.BusProvider;
import com.domain.template.io.bus.event.ApiEvent;
import com.domain.template.io.bus.event.Event;
import com.domain.template.io.rest.HttpRequestManager;
import com.domain.template.io.rest.util.APIUtil;
import com.domain.template.io.rest.util.PostEntityUtil;
import com.domain.template.io.service.TIntentService;
import com.domain.template.util.AppUtil;
import com.domain.template.util.manager.DialogManager;
import com.domain.template.util.manager.SnackBarManager;
import com.google.common.eventbus.Subscribe;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = AuthActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private Button mBtnSign;
    private TextInputLayout mTilEmail;
    private TextInputEditText mTietEmail;
    private TextInputLayout mTilPass;
    private TextInputEditText mTietPass;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.unregister(this);
        DialogManager.getInstance().dismissPreloader(this.getClass());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.register(this);
        findViews();
        setListeners();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_auth;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ===========================================================
    // Observer callback
    // ===========================================================

    @Subscribe
    public void onEventReceived(Event event) {
        if (event instanceof ApiEvent) {
            if (event.getSubscriber().equals(getClass().getSimpleName())) {
                handleApiEvents((ApiEvent) event);
            }
        }
    }

    // ===========================================================
    // Observer methods
    // ===========================================================

    private void handleApiEvents(ApiEvent event) {
        DialogManager.getInstance().dismissPreloader(this.getClass());

        switch (event.getEventType()) {

            case Event.EventType.Api.LOGIN_COMPLETED:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case Event.EventType.Api.Error.NO_NETWORK:
                SnackBarManager.show(this, getString(R.string.msg_network_connection_error),
                        SnackBarManager.Duration.LONG);
                break;

            case Event.EventType.Api.Error.PAGE_NOT_FOUND:
                if (BuildConfig.isDEBUG) Log.i(LOG_TAG, getString(R.string.msg_page_not_found));
                break;

            case Event.EventType.Api.Error.BAD_REQUEST:
                String body = (String) event.getEventData();
                if (body != null) {
                    SnackBarManager.show(this, body, SnackBarManager.Duration.LONG);
                    if (BuildConfig.isDEBUG) Log.i(LOG_TAG, getString(R.string.msg_bad_request) + body);
                }
                break;

            case Event.EventType.Api.Error.UNAUTHORIZED:
                if (BuildConfig.isDEBUG) Log.i(LOG_TAG, getString(R.string.msg_not_authorized));
                break;

            case Event.EventType.Api.Error.UNKNOWN:
                SnackBarManager.show(this, getString(R.string.msg_unknown_error),
                        SnackBarManager.Duration.LONG);
                if (BuildConfig.isDEBUG) Log.i(LOG_TAG, getString(R.string.msg_unknown_error));
                break;
        }
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auth_sign:
                String mail = mTietEmail.getText().toString();
                String pass = mTietPass.getText().toString();
                grabDataAndSingIn(mail, pass);
                break;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
        mBtnSign.setOnClickListener(this);
    }

    private void findViews() {
        mTilEmail = (TextInputLayout) findViewById(R.id.til_auth_email);
        mTietEmail = (TextInputEditText) findViewById(R.id.tiet_auth_email);
        mTilPass = (TextInputLayout) findViewById(R.id.til_auth_pass);
        mTietPass = (TextInputEditText) findViewById(R.id.tiet_auth_pass);
        mBtnSign = (Button) findViewById(R.id.btn_auth_sign);
    }

    private void grabDataAndSingIn(String mail, String pass) {
        boolean isValidationSucceeded = true;
        DialogManager.getInstance().showPreloader(this);

        // validate password (empty or not)
        if (pass.trim().length() == 0) {
            isValidationSucceeded = false;
            mTilPass.setError(getString(R.string.msg_edt_error_mail));
        }

        // validate email (empty or not)
        if (mail.trim().length() == 0) {
            isValidationSucceeded = false;
            mTilEmail.setError(getString(R.string.msg_edt_pass_error));
        }

        // if required fields are filled up then proceed with sign in
        if (isValidationSucceeded) {
            mTilEmail.setErrorEnabled(false);
            mTilPass.setErrorEnabled(false);

            AppUtil.closeKeyboard(this);

            String postEntity = PostEntityUtil.composeSignInPostEntity(
                    mail,
                    pass
            );

            TIntentService.start(
                    this,
                    getClass().getSimpleName(),
                    APIUtil.getURL(APIUtil.LOGIN),
                    postEntity,
                    HttpRequestManager.RequestType.LOGIN_IN
            );
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}