package com.sarkisian.template.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.google.common.eventbus.Subscribe;
import com.sarkisian.template.R;
import com.sarkisian.template.io.bus.BusProvider;
import com.sarkisian.template.io.bus.event.ApiEvent;
import com.sarkisian.template.io.bus.event.Event;
import com.sarkisian.template.io.bus.event.NetworkEvent;
import com.sarkisian.template.io.rest.HttpRequestManager;
import com.sarkisian.template.io.rest.util.APIUtil;
import com.sarkisian.template.io.service.TlIntentService;
import com.sarkisian.template.ui.fragment.MainFragment;
import com.sarkisian.template.util.AppUtil;
import com.sarkisian.template.util.Logger;
import com.sarkisian.template.util.manager.FragmentTransactionManager;
import com.sarkisian.template.util.manager.SnackBarManager;

import static com.sarkisian.template.io.rest.util.APIUtil.LOGOUT;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

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
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.register(this);
        findViews();
        setListeners();
        customizeActionBar();
        initDrawer();//87877968
        openScreen(MainFragment.newInstance(), false, R.id.nav_one);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // ===========================================================
    // Observer callback
    // ===========================================================

    @Subscribe
    public void onEventReceived(Event event) {
        if (event instanceof NetworkEvent) {
            handleNetworkEvent((NetworkEvent) event);

        } else if (event instanceof ApiEvent) {
            if (event.getSubscriber().equals(getClass().getSimpleName())) {
                handleApiEvents((ApiEvent) event);
            }
        }
    }

    // ===========================================================
    // Observer methods
    // ===========================================================

    private void handleNetworkEvent(NetworkEvent event) {
        switch (event.getEventType()) {
            case Event.EventType.Network.CONNECTED:
                break;
        }
    }

    private void handleApiEvents(ApiEvent event) {
        switch (event.getEventType()) {
            case Event.EventType.Api.LOGOUT_COMPLETED:
                AppUtil.logOutFromApp(this);
                break;

            case Event.EventType.Api.Error.NO_NETWORK:
                SnackBarManager.show(this, getString(R.string.msg_network_connection_error),
                        SnackBarManager.Duration.LONG);
                break;

            case Event.EventType.Api.Error.PAGE_NOT_FOUND:
                Logger.i(LOG_TAG, getString(R.string.msg_page_not_found));
                break;

            case Event.EventType.Api.Error.BAD_REQUEST:
                String body = (String) event.getEventData();
                if (body != null) {
                    SnackBarManager.show(this, body, SnackBarManager.Duration.LONG);
                    Logger.i(LOG_TAG, getString(R.string.msg_bad_request) + body);
                }
                break;

            case Event.EventType.Api.Error.UNAUTHORIZED:
                Logger.i(LOG_TAG, getString(R.string.msg_not_authorized));
                break;

            case Event.EventType.Api.Error.UNKNOWN:
                SnackBarManager.show(this, getString(R.string.msg_unknown_error),
                        SnackBarManager.Duration.LONG);
                Logger.i(LOG_TAG, getString(R.string.msg_unknown_error));
                break;
        }
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_one:
                openScreen(MainFragment.newInstance(), false, R.id.nav_one);
                break;

            case R.id.nav_two:
                break;

            case R.id.nav_three:
                break;

            case R.id.nav_four:
                break;

            case R.id.nav_five:
                break;

            case R.id.nav_six:
                TlIntentService.start(
                        MainActivity.this,
                        MainActivity.class.getSimpleName(),
                        APIUtil.getURL(LOGOUT),
                        HttpRequestManager.RequestType.LOG_OUT
                );
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void findViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
        mNavigationView = (NavigationView) findViewById(R.id.nav_main);
    }

    private void customizeActionBar() {
        setActionBarTitle(getString(R.string.app_name));
    }

    private void initDrawer() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                getToolBar(),
                R.string.msg_navigation_drawer_open,
                R.string.msg_navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void openScreen(Fragment fragment, boolean addToBackStack, @IdRes int menuItem) {

        mNavigationView.getMenu().findItem(menuItem).setChecked(true);

        FragmentTransactionManager.displayFragment(
                getSupportFragmentManager(),
                fragment,
                R.id.fl_main_container,
                addToBackStack
        );
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}