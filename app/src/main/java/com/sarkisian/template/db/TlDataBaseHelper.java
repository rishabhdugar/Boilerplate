package com.sarkisian.template.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TlDataBaseHelper extends SQLiteOpenHelper {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = TlDataBaseHelper.class.getName();

    private static final String DATABASE_NAME = "TL.DB";
    private static final int DATABASE_VERSION = 1;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public TlDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public void onCreate(SQLiteDatabase db) {
        TlDataBase.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TlDataBase.onUpgrade(db, oldVersion, newVersion);
    }

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
