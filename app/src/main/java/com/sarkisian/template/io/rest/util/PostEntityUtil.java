package com.sarkisian.template.io.rest.util;

import com.sarkisian.template.util.EncodeUtil;
import com.sarkisian.template.util.Logger;

public class PostEntityUtil {

    private static final String LOG_TAG = PostEntityUtil.class.getSimpleName();

    public static String composeSignInPostEntity(String email, String pass) {
        String entityString = "{\"email\": \"" + EncodeUtil.escapeString(email)
                + "\", \"password\": \"" + EncodeUtil.escapeString(pass) + "\"}";
        Logger.i(LOG_TAG, entityString);
        return entityString;
    }

}
