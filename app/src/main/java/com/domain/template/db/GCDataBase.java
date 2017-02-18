package com.domain.template.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.domain.template.BuildConfig;
import com.domain.template.db.entity.User;

import java.util.ArrayList;

public class GCDataBase {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = GCDataBase.class.getName();

    // ===========================================================
    // Fields
    // ===========================================================

    /**
     * TABLES
     ***************************************************************/

    public static final String USER_TABLE = "USER_TABLE";

    public static final String USER_PK = "_id";
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_FIRST_NAME = "USER_FIRST_NAME";
    public static final String USER_LAST_NAME = "USER_LAST_NAME";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    public static final String USER_LANGUAGE = "USER_LANGUAGE";
    public static final String USER_AVATAR = "USER_AVATAR";

    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE
            + " ("
            + USER_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_ID + " INTEGER UNIQUE, "
            + USER_NAME + " TEXT, "
            + USER_FIRST_NAME + " TEXT, "
            + USER_LAST_NAME + " TEXT, "
            + USER_EMAIL + " TEXT, "
            + USER_PHONE_NUMBER + " TEXT, "
            + USER_LANGUAGE + " TEXT, "
            + USER_AVATAR + " TEXT"
            + ");";

    /**
     * PROJECTIONS
     ***************************************************************/

    public static class Projection {
        public static String[] USER = {
                GCDataBase.USER_PK,
                GCDataBase.USER_ID,
                GCDataBase.USER_NAME,
                GCDataBase.USER_FIRST_NAME,
                GCDataBase.USER_LAST_NAME,
                GCDataBase.USER_EMAIL,
                GCDataBase.USER_PHONE_NUMBER,
                GCDataBase.USER_LANGUAGE,
                GCDataBase.USER_AVATAR
        };

        public static String[] COUNT = {
                "COUNT(*)"
        };
    }

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
     * SQL METHODS
     ***************************************************************/

    static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (BuildConfig.isDEBUG) Log.i(LOG_TAG, "Upgrading database from version "
                + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    /**
     * VALUES
     ***************************************************************/

    public static ContentValues composeValues(Object object, String table) {
        ContentValues values = new ContentValues();

        switch (table) {
            case GCDataBase.USER_TABLE:
                User user = (User) object;
                values.put(GCDataBase.USER_ID, user.getId());
                values.put(GCDataBase.USER_NAME, user.getName());
                values.put(GCDataBase.USER_FIRST_NAME, user.getFirstName());
                values.put(GCDataBase.USER_LAST_NAME, user.getLastName());
                values.put(GCDataBase.USER_EMAIL, user.getEmail());
                values.put(GCDataBase.USER_PHONE_NUMBER, user.getPhoneNumber());
                values.put(GCDataBase.USER_AVATAR, user.getAvatar());
                break;
        }
        return values;
    }

    public static ContentValues[] composeValuesArray(ArrayList<?> objects, String table) {
        ArrayList<ContentValues> valuesList = new ArrayList<>();

        switch (table) {
            case GCDataBase.USER_TABLE:
                ArrayList<User> users = (ArrayList<User>) objects;
                for (User user : users) {
                    ContentValues values = new ContentValues();
                    values.put(GCDataBase.USER_ID, user.getId());
                    values.put(GCDataBase.USER_NAME, user.getName());
                    values.put(GCDataBase.USER_FIRST_NAME, user.getFirstName());
                    values.put(GCDataBase.USER_LAST_NAME, user.getLastName());
                    values.put(GCDataBase.USER_EMAIL, user.getEmail());
                    values.put(GCDataBase.USER_PHONE_NUMBER, user.getPhoneNumber());
                    values.put(GCDataBase.USER_AVATAR, user.getAvatar());
                    valuesList.add(values);
                }
                break;
        }
        return valuesList.toArray(new ContentValues[valuesList.size()]);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
