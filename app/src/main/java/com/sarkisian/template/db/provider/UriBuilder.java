package com.sarkisian.template.db.provider;

import android.net.Uri;

public class UriBuilder {

    public static Uri buildUserUri(long pk) {
        return Uri.parse("content://" + TlProvider.AUTHORITY + "/"
                + TlProvider.Path.USER_LOCATION + "/" + pk);
    }

    public static Uri buildUserUri() {
        return Uri.parse("content://" + TlProvider.AUTHORITY + "/"
                + TlProvider.Path.USER_LOCATION);
    }

}
