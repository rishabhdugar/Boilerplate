package com.sarkisian.template;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sarkisian.template.ui.activity.AuthActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AuthInstrumentedTest {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = AuthInstrumentedTest.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    @Rule
    public ActivityTestRule<AuthActivity> mAuthActivityTestRule =
            new ActivityTestRule<>(AuthActivity.class);
    private Context mInstrumentationCtx;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    @Before
    public void setup() {
        mInstrumentationCtx = InstrumentationRegistry.getContext();
    }

    @Test()
    public void authTest() throws Exception {

    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Click Listeners
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
