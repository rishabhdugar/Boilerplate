package com.domain.template.db.handler;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.domain.template.db.GCDataBase;
import com.domain.template.db.entity.User;
import com.domain.template.db.provider.UriBuilder;

import java.lang.ref.WeakReference;

public class TAsyncQueryHandler extends AsyncQueryHandler {

    // ===========================================================
    // Constants
    // ===========================================================

    private final static String LOG_TAG = TAsyncQueryHandler.class.getSimpleName();

    public static class QueryToken {
        public static final int GET_USER = 100;
        public static final int GET_USERS = 101;
        public static final int ADD_USER = 102;
        public static final int UPDATE_USER = 104;
        public static final int DELETE_USER = 105;
        public static final int DELETE_USERS = 106;
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

    // ===========================================================
    // Fields
    // ===========================================================

    private WeakReference<AsyncQueryListener> mQueryListenerReference;

    // ===========================================================
    // Constructors
    // ===========================================================

    public TAsyncQueryHandler(ContentResolver contentResolver,
                              AsyncQueryListener queryListenerReference) {
        super(contentResolver);
        mQueryListenerReference = new WeakReference<>(queryListenerReference);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

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
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods systems
    // ===========================================================

    // ===========================================================
    // Methods controls
    // ===========================================================

    /**
     * USER Methods
     *************************************************************/

    public synchronized void getUser(long userId) {
        startQuery(
                QueryToken.GET_USER,
                null,
                UriBuilder.buildUserUri(),
                GCDataBase.Projection.USER,
                GCDataBase.USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null
        );
    }

    public synchronized void getUsers() {
        startQuery(
                QueryToken.GET_USERS,
                null,
                UriBuilder.buildUserUri(),
                GCDataBase.Projection.USER,
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
                GCDataBase.composeValues(user, GCDataBase.USER_TABLE)
        );
    }

    public synchronized void updateUser(User user) {
        startUpdate(
                QueryToken.UPDATE_USER,
                null,
                UriBuilder.buildUserUri(),
                GCDataBase.composeValues(user, GCDataBase.USER_TABLE),
                GCDataBase.USER_ID + "=?",
                new String[]{String.valueOf(user.getId())}
        );
    }

    public synchronized void deleteUser(User user) {
        startDelete(
                QueryToken.DELETE_USER,
                null,
                UriBuilder.buildUserUri(),
                GCDataBase.USER_ID + "=?",
                new String[]{String.valueOf(user.getId())}
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
    // Methods helpers
    // ===========================================================

}
