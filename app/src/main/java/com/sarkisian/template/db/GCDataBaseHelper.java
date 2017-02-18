package com.sarkisian.template.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GCDataBaseHelper extends SQLiteOpenHelper {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = GCDataBaseHelper.class.getName();

    private static final String DATABASE_NAME = "TPL.DB";
    private static final int DATABASE_VERSION = 1;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public GCDataBaseHelper(Context context) {
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
        GCDataBase.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        GCDataBase.onUpgrade(db, oldVersion, newVersion);
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
