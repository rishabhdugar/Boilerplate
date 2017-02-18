package com.domain.template.db.handler;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.domain.template.db.GCDataBase;
import com.domain.template.db.cursor.CursorReader;
import com.domain.template.db.entity.User;
import com.domain.template.db.provider.UriBuilder;

import java.util.ArrayList;

public class TQueryHandler {

    // ===========================================================
    // Constants
    // ===========================================================

    private final static String LOG_TAG = TQueryHandler.class.getSimpleName();

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
     * USER METHODS
     *************************************************************/

    public synchronized static void addUser(Context context, User user) {
        context.getContentResolver().insert(
                UriBuilder.buildUserUri(),
                GCDataBase.composeValues(user, GCDataBase.USER_TABLE)
        );
    }

    public synchronized static void addUsers(Context context, ArrayList<User> userArrayList) {
        context.getContentResolver().bulkInsert(
                UriBuilder.buildUserUri(),
                GCDataBase.composeValuesArray(userArrayList, GCDataBase.USER_TABLE)
        );
    }

    public synchronized static void updateUser(Context context, User user) {
        context.getContentResolver().update(
                UriBuilder.buildUserUri(),
                GCDataBase.composeValues(user, GCDataBase.USER_TABLE),
                GCDataBase.USER_ID + "=?",
                new String[]{String.valueOf(user.getId())}
        );
    }

    public synchronized static void updateUsers(Context context, ArrayList<User> users) {
        for (User user : users) {
            context.getContentResolver().update(
                    UriBuilder.buildUserUri(),
                    GCDataBase.composeValues(user, GCDataBase.USER_TABLE),
                    GCDataBase.USER_ID + "=?",
                    new String[]{String.valueOf(user.getId())}
            );
        }
    }

    public synchronized static User getUser(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(
                UriBuilder.buildUserUri(),
                GCDataBase.Projection.USER,
                GCDataBase.USER_ID + "=?",
                new String[]{String.valueOf(id)},
                null
        );
        return CursorReader.parseUser(cursor);
    }

    public synchronized static ArrayList<User> getUsers(Context context) {
        Cursor cursor = context.getContentResolver().query(
                UriBuilder.buildUserUri(),
                GCDataBase.Projection.USER,
                null,
                null,
                null
        );
        return CursorReader.parseUsers(cursor);
    }

    public synchronized static void deleteUser(Context context, User user) {
        context.getContentResolver().delete(
                UriBuilder.buildUserUri(),
                GCDataBase.USER_ID + "=?",
                new String[]{String.valueOf(user.getId())}
        );
    }

    public synchronized static void deleteUsers(Context context) {
        long i = context.getContentResolver().delete(
                UriBuilder.buildUserUri(),
                null,
                null
        );
    }

    /**
     * UTIL METHODS
     *************************************************************/

    public synchronized static boolean isTableEmpty(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(
                uri,
                GCDataBase.Projection.COUNT,
                null,
                null,
                null
        );

        boolean exist = false;
        if (cursor != null) {
            cursor.moveToFirst();
            exist = cursor.getInt(0) > 0;
            cursor.close();
        }
        return exist;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}