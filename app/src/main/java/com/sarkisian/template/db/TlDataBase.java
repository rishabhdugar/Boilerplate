package com.sarkisian.template.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.sarkisian.template.db.entity.User;
import com.sarkisian.template.util.Logger;

import java.util.ArrayList;

public class TlDataBase {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = TlDataBase.class.getName();

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
            + USER_AVATAR + " TEXT"
            + ");";

    /**
     * PROJECTIONS
     ***************************************************************/

    public static class Projection {
        public static String[] USER = {
                TlDataBase.USER_PK,
                TlDataBase.USER_ID,
                TlDataBase.USER_NAME,
                TlDataBase.USER_FIRST_NAME,
                TlDataBase.USER_LAST_NAME,
                TlDataBase.USER_EMAIL,
                TlDataBase.USER_PHONE_NUMBER,
                TlDataBase.USER_AVATAR
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
        Logger.i(LOG_TAG, "Upgrading database from version "
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
            case TlDataBase.USER_TABLE:
                User user = (User) object;
                values.put(TlDataBase.USER_ID, user.getId());
                values.put(TlDataBase.USER_NAME, user.getName());
                values.put(TlDataBase.USER_FIRST_NAME, user.getFirstName());
                values.put(TlDataBase.USER_LAST_NAME, user.getLastName());
                values.put(TlDataBase.USER_EMAIL, user.getEmail());
                values.put(TlDataBase.USER_PHONE_NUMBER, user.getPhoneNumber());
                values.put(TlDataBase.USER_AVATAR, user.getAvatar());
                break;
        }
        return values;
    }

    public static ContentValues[] composeValuesArray(ArrayList<?> objects, String table) {
        ArrayList<ContentValues> valuesList = new ArrayList<>();

        switch (table) {
            case TlDataBase.USER_TABLE:
                ArrayList<User> users = (ArrayList<User>) objects;
                for (User user : users) {
                    ContentValues values = new ContentValues();
                    values.put(TlDataBase.USER_ID, user.getId());
                    values.put(TlDataBase.USER_NAME, user.getName());
                    values.put(TlDataBase.USER_FIRST_NAME, user.getFirstName());
                    values.put(TlDataBase.USER_LAST_NAME, user.getLastName());
                    values.put(TlDataBase.USER_EMAIL, user.getEmail());
                    values.put(TlDataBase.USER_PHONE_NUMBER, user.getPhoneNumber());
                    values.put(TlDataBase.USER_AVATAR, user.getAvatar());
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
