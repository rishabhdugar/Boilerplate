package com.sarkisian.template.db.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.sarkisian.template.BuildConfig;
import com.sarkisian.template.db.TlDataBase;
import com.sarkisian.template.db.TlDataBaseHelper;
import com.sarkisian.template.util.UnsupportedUriException;


public class TlProvider extends ContentProvider {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = TlProvider.class.getName();

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    public class Path {
        static final String USER_LOCATION = TlDataBase.USER_TABLE;
    }

    private class Code {
        private static final int ALL_USERS = 1;
        private static final int SINGLE_USER = 2;
    }

    private static class ContentType {
        private static final String ALL_USERS = "vnd.android.cursor.dir/vnd."
                + AUTHORITY + "." + Path.USER_LOCATION;
        private static final String SINGLE_USER = "vnd.android.cursor.item/vnd."
                + AUTHORITY + "." + Path.USER_LOCATION;
    }

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // ===========================================================
    // Fields
    // ===========================================================

    private TlDataBaseHelper mDataBaseHelper;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public boolean onCreate() {
        mDataBaseHelper = new TlDataBaseHelper(getContext());
        return false;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case Code.ALL_USERS:
                return ContentType.ALL_USERS;

            case Code.SINGLE_USER:
                return ContentType.SINGLE_USER;

            default:
                throw new UnsupportedUriException(uri.toString());
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri contentUri;
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        long id;

        switch (sUriMatcher.match(uri)) {
            case Code.SINGLE_USER:
            case Code.ALL_USERS:
                id = db.insertWithOnConflict(TlDataBase.USER_TABLE, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                contentUri = ContentUris.withAppendedId(UriBuilder.buildUserUri(), id);
                break;

            default:
                throw new UnsupportedUriException(uri.toString());
        }
        return contentUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case Code.ALL_USERS:
                queryBuilder.setTables(TlDataBase.USER_TABLE);
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case Code.SINGLE_USER:
                queryBuilder.setTables(TlDataBase.USER_TABLE);
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedUriException(uri.toString());
        }
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleteCount;
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case Code.ALL_USERS:
                deleteCount = db.delete(TlDataBase.USER_TABLE, selection, selectionArgs);
                break;

            case Code.SINGLE_USER:
                deleteCount = db.delete(TlDataBase.USER_TABLE, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedUriException(uri.toString());
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount;
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case Code.ALL_USERS:
                updateCount = db.update(TlDataBase.USER_TABLE, values, selection, selectionArgs);
                break;

            case Code.SINGLE_USER:
                updateCount = db.update(TlDataBase.USER_TABLE, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedUriException(uri.toString());
        }
        return updateCount;
    }

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, Path.USER_LOCATION, Code.ALL_USERS);
        uriMatcher.addURI(AUTHORITY, Path.USER_LOCATION + "/#", Code.SINGLE_USER);

        return uriMatcher;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
