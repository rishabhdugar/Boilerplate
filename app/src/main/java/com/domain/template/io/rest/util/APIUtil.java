package com.domain.template.io.rest.util;

import com.domain.template.util.Constant;
import com.domain.template.BuildConfig;

public class APIUtil {

    private static final String HOST_PRODUCTION = "http://your_domain.com";
    private static final String HOST_STAGING = "http://your_domain.com";

    public static final String LOGIN = "/api/auth/login/";
    public static final String LOGOUT = "/api/auth/logout/";

    public static String getURL(String api) {
        return getHost() + api;
    }

    public static String getHost() {
        if (BuildConfig.BUILD_TYPE.equals(Constant.BuildType.RELEASE)) {
            return HOST_PRODUCTION;
        } else {
            return HOST_STAGING;
        }
    }
}
