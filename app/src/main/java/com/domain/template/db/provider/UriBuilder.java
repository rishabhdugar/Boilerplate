package com.domain.template.db.provider;

import android.net.Uri;

public class UriBuilder {

    public static Uri buildUserUri(long id) {
        return Uri.parse("content://" + TProvider.AUTHORITY + "/"
                + TProvider.Path.USER_LOCATION + "/" + id);
    }

    public static Uri buildUserUri() {
        return Uri.parse("content://" + TProvider.AUTHORITY + "/"
                + TProvider.Path.USER_LOCATION);
    }

}
