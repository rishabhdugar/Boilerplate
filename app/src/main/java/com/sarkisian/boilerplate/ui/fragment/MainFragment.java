package com.sarkisian.boilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarkisian.boilerplate.R;
import com.sarkisian.boilerplate.util.Constant;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private Bundle mArgumentData;

    // ===========================================================
    // Constructors
    // ===========================================================

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public static MainFragment newInstance(Bundle args) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        findViews(view);
        setListeners();
        getData();
        customizeActionBar();
        return view;
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
    }

    private void findViews(View view) {
    }

    private void getData() {
        if (getArguments() != null) {
            mArgumentData = getArguments().getBundle(Constant.Argument.ARGUMENT_DATA);
        }
    }

    private void customizeActionBar() {
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}