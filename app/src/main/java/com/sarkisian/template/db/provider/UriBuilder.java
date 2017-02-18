package com.sarkisian.template.db.provider;

import android.net.Uri;

public class UriBuilder {

    public static Uri buildUserUri(long id) {
        return Uri.parse("content://" + TlProvider.AUTHORITY + "/"
                + TlProvider.Path.USER_LOCATION + "/" + id);
    }

    public static Uri buildUserUri() {
        return Uri.parse("content://" + TlProvider.AUTHORITY + "/"
                + TlProvider.Path.USER_LOCATION);
    }

}
