package com.domain.template.db.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.domain.template.BuildConfig;
import com.domain.template.db.GCDataBase;
import com.domain.template.db.GCDataBaseHelper;


public class TProvider extends ContentProvider {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = TProvider.class.getName();

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    public class Path {
        static final String USER_LOCATION = GCDataBase.USER_TABLE;
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

    private GCDataBaseHelper mScDbHelper;

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
        mScDbHelper = new GCDataBaseHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case Code.ALL_USERS:
                return ContentType.ALL_USERS;

            case Code.SINGLE_USER:
                return ContentType.SINGLE_USER;

            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri contentUri;
        SQLiteDatabase db = mScDbHelper.getWritableDatabase();
        long id;

        switch (sUriMatcher.match(uri)) {
            case Code.SINGLE_USER:
            case Code.ALL_USERS:
                id = db.insertWithOnConflict(GCDataBase.USER_TABLE, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                contentUri = ContentUris.withAppendedId(UriBuilder.buildUserUri(), id);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        return contentUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = mScDbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case Code.ALL_USERS:
                queryBuilder.setTables(GCDataBase.USER_TABLE);
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case Code.SINGLE_USER:
                queryBuilder.setTables(GCDataBase.USER_TABLE);
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleteCount;
        SQLiteDatabase db = mScDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case Code.ALL_USERS:
                deleteCount = db.delete(GCDataBase.USER_TABLE, selection, selectionArgs);
                break;

            case Code.SINGLE_USER:
                deleteCount = db.delete(GCDataBase.USER_TABLE, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount;
        SQLiteDatabase db = mScDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case Code.ALL_USERS:
                updateCount = db.update(GCDataBase.USER_TABLE, values, selection, selectionArgs);
                break;

            case Code.SINGLE_USER:
                updateCount = db.update(GCDataBase.USER_TABLE, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
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
