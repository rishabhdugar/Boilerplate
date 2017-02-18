package com.sarkisian.template.io.rest.util;

import android.util.Log;

import com.sarkisian.template.BuildConfig;
import com.sarkisian.template.util.EncodeUtil;

public class PostEntityUtil {

    private static final String LOG_TAG = PostEntityUtil.class.getSimpleName();

    public static String composeSignInPostEntity(String email, String pass) {
        String entityString = "{\"email\": \"" + EncodeUtil.escapeString(email)
                + "\", \"password\": \"" + EncodeUtil.escapeString(pass) + "\"}";
        if (BuildConfig.isDEBUG) Log.i(LOG_TAG, entityString);
        return entityString;
    }

}
