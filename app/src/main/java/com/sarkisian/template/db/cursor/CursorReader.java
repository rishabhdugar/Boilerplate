package com.sarkisian.template.db.cursor;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.sarkisian.template.db.TlDataBase;
import com.sarkisian.template.db.entity.User;

import java.util.ArrayList;

public class CursorReader {

    // ===========================================================
    // Constants
    // ===========================================================

    private final static String LOG_TAG = CursorReader.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * USER
     *************************************************************/

    @Nullable
    public static User parseUser(Cursor cursor) {
        User user = null;
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            user = composeUser(cursor);
        }

        if (cursor != null) {
            cursor.close();
        }

        return user;
    }

    public static ArrayList<User> parseUsers(Cursor cursor) {
        ArrayList<User> userArrayList = new ArrayList<>();
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            do {
                User user = composeUser(cursor);
                userArrayList.add(user);

            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return userArrayList;
    }

    /**
     * UTIL METHODS
     *************************************************************/

    private static User composeUser(Cursor cursor) {
        User user = new User();
        user.setPk(cursor.getLong(cursor.getColumnIndex(TlDataBase.USER_PK)));
        user.setId(cursor.getLong(cursor.getColumnIndex(TlDataBase.USER_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(TlDataBase.USER_NAME)));
        user.setFirstName(cursor.getString(cursor.getColumnIndex(TlDataBase.USER_FIRST_NAME)));
        user.setLastName(cursor.getString(cursor.getColumnIndex(TlDataBase.USER_LAST_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(TlDataBase.USER_EMAIL)));
        user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(TlDataBase.USER_PHONE_NUMBER)));
        user.setAvatar(cursor.getString(cursor.getColumnIndex(TlDataBase.USER_AVATAR)));

        return user;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}