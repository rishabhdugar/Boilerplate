package com.sarkisian.boilerplate.db.handler;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.sarkisian.boilerplate.db.TlDataBase;
import com.sarkisian.boilerplate.db.entity.User;
import com.sarkisian.boilerplate.db.provider.UriBuilder;

import java.lang.ref.WeakReference;

public class TlAsyncQueryHandler extends AsyncQueryHandler {

    // ===========================================================
    // Constants
    // ===========================================================

    private final static String LOG_TAG = TlAsyncQueryHandler.class.getSimpleName();

    public static class QueryToken {
        public static final int GET_USER = 100;
        public static final int GET_USERS = 101;
        public static final int ADD_USER = 102;
        public static final int UPDATE_USER = 104;
        public static final int DELETE_USER = 105;
        public static final int DELETE_USERS = 106;
    }

    // ===========================================================
    // Fields
    // ===========================================================

    private WeakReference<AsyncQueryListener> mQueryListenerReference;

    // ===========================================================
    // Constructors
    // ===========================================================

    public TlAsyncQueryHandler(Context context, AsyncQueryListener queryListenerReference) {
        super(context.getContentResolver());
        mQueryListenerReference = new WeakReference<>(queryListenerReference);
    }

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        final AsyncQueryListener queryListener = mQueryListenerReference.get();
        if (queryListener != null) {
            queryListener.onQueryComplete(token, cookie, cursor);
        } else if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        final AsyncQueryListener queryListener = mQueryListenerReference.get();
        if (queryListener != null) {
            queryListener.onInsertComplete(token, cookie, uri);
        }
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        final AsyncQueryListener queryListener = mQueryListenerReference.get();
        if (queryListener != null) {
            queryListener.onUpdateComplete(token, cookie, result);
        }
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        final AsyncQueryListener queryListener = mQueryListenerReference.get();
        if (queryListener != null) {
            queryListener.onDeleteComplete(token, cookie, result);
        }
    }

    // ===========================================================
    // Util Methods
    // ===========================================================

    public synchronized void getUser(long userId) {
        startQuery(
                QueryToken.GET_USER,
                null,
                UriBuilder.buildUserUri(),
                TlDataBase.Projection.USER,
                TlDataBase.USER_PK + "=?",
                new String[]{String.valueOf(userId)},
                null
        );
    }

    public synchronized void getUserByPk(long pk) {
        startQuery(
                QueryToken.GET_USER,
                null,
                UriBuilder.buildUserUri(pk),
                TlDataBase.Projection.USER,
                null,
                null,
                null
        );
    }

    public synchronized void getUsers() {
        startQuery(
                QueryToken.GET_USERS,
                null,
                UriBuilder.buildUserUri(),
                TlDataBase.Projection.USER,
                null,
                null,
                null
        );
    }

    public synchronized void addUser(User user) {
        startInsert(
                QueryToken.ADD_USER,
                null,
                UriBuilder.buildUserUri(),
                TlDataBase.composeValues(user, TlDataBase.USER_TABLE)
        );
    }

    public synchronized void updateUser(User user) {
        startUpdate(
                QueryToken.UPDATE_USER,
                null,
                UriBuilder.buildUserUri(),
                TlDataBase.composeValues(user, TlDataBase.USER_TABLE),
                TlDataBase.USER_ID + "=?",
                new String[]{String.valueOf(user.getId())}
        );
    }

    public synchronized void updateUserByPk(User user) {
        startUpdate(
                QueryToken.UPDATE_USER,
                null,
                UriBuilder.buildUserUri(user.getPk()),
                TlDataBase.composeValues(user, TlDataBase.USER_TABLE),
                null,
                null
        );
    }

    public synchronized void deleteUser(User user) {
        startDelete(
                QueryToken.DELETE_USER,
                null,
                UriBuilder.buildUserUri(),
                TlDataBase.USER_ID + "=?",
                new String[]{String.valueOf(user.getId())}
        );
    }

    public synchronized void deleteUserByPk(User user) {
        startDelete(
                QueryToken.DELETE_USER,
                null,
                UriBuilder.buildUserUri(user.getPk()),
                null,
                null
        );
    }

    public synchronized void deleteUsers() {
        startDelete(
                QueryToken.DELETE_USERS,
                null,
                UriBuilder.buildUserUri(),
                null,
                null
        );
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public interface AsyncQueryListener {
        void onQueryComplete(int token, Object cookie, Cursor cursor);

        void onInsertComplete(int token, Object cookie, Uri uri);

        void onUpdateComplete(int token, Object cookie, int result);

        void onDeleteComplete(int token, Object cookie, int result);
    }


}