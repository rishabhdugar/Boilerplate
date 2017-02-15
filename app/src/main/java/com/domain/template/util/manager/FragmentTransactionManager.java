package com.domain.template.util.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class FragmentTransactionManager {

    private static final String LOG_TAG = FragmentTransactionManager.class.getSimpleName();

    public static void displayFragment(FragmentManager fragmentManager, Fragment fragment,
                                       int view, boolean mustAddToBackStack) {
        if (mustAddToBackStack) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(view, fragment, fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        } else {
            fragmentManager.beginTransaction()
                    .replace(view, fragment, fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }
}
