package com.sarkisian.template.db.handler;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.sarkisian.template.db.TlDataBase;
import com.sarkisian.template.db.cursor.CursorReader;
import com.sarkisian.template.db.entity.User;
import com.sarkisian.template.db.provider.UriBuilder;

import java.util.ArrayList;

public class TlQueryHandler {

    // ===========================================================
    // Constants
    // ===========================================================

    private final static String LOG_TAG = TlQueryHandler.class.getSimpleName();

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
                TlDataBase.composeValues(user, TlDataBase.USER_TABLE)
        );
    }

    public synchronized static void addUsers(Context context, ArrayList<User> userArrayList) {
        context.getContentResolver().bulkInsert(
                UriBuilder.buildUserUri(),
                TlDataBase.composeValuesArray(userArrayList, TlDataBase.USER_TABLE)
        );
    }

    public synchronized static void updateUser(Context context, User user) {
        context.getContentResolver().update(
                UriBuilder.buildUserUri(),
                TlDataBase.composeValues(user, TlDataBase.USER_TABLE),
                TlDataBase.USER_ID + "=?",
                new String[]{String.valueOf(user.getId())}
        );
    }

    public synchronized static void updateUserByPk(Context context, User user) {
        context.getContentResolver().update(
                UriBuilder.buildUserUri(user.getPk()),
                TlDataBase.composeValues(user, TlDataBase.USER_TABLE),
                null,
                null
        );
    }

    public synchronized static void updateUsers(Context context, ArrayList<User> users) {
        for (User user : users) {
            context.getContentResolver().update(
                    UriBuilder.buildUserUri(),
                    TlDataBase.composeValues(user, TlDataBase.USER_TABLE),
                    TlDataBase.USER_ID + "=?",
                    new String[]{String.valueOf(user.getId())}
            );
        }
    }

    public synchronized static User getUser(Context context, long userId) {
        Cursor cursor = context.getContentResolver().query(
                UriBuilder.buildUserUri(),
                TlDataBase.Projection.USER,
                TlDataBase.USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null
        );
        return CursorReader.parseUser(cursor);
    }

    public synchronized static User getUserByPk(Context context, long userPk) {
        Cursor cursor = context.getContentResolver().query(
                UriBuilder.buildUserUri(userPk),
                TlDataBase.Projection.USER,
                null,
                null,
                null
        );
        return CursorReader.parseUser(cursor);
    }

    public synchronized static ArrayList<User> getUsers(Context context) {
        Cursor cursor = context.getContentResolver().query(
                UriBuilder.buildUserUri(),
                TlDataBase.Projection.USER,
                null,
                null,
                null
        );
        return CursorReader.parseUsers(cursor);
    }

    public synchronized static void deleteUser(Context context, User user) {
        context.getContentResolver().delete(
                UriBuilder.buildUserUri(),
                TlDataBase.USER_ID + "=?",
                new String[]{String.valueOf(user.getId())}
        );
    }

    public synchronized static void deleteUserByPk(Context context, long userPk) {
        context.getContentResolver().delete(
                UriBuilder.buildUserUri(userPk),
                null,
                null
        );
    }

    public synchronized static void deleteUsers(Context context) {
        context.getContentResolver().delete(
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
                TlDataBase.Projection.COUNT,
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