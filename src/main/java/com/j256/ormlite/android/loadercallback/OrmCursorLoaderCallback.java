package com.j256.ormlite.android.loadercallback;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import com.j256.ormlite.android.apptools.OrmLiteCursorAdapter;
import com.j256.ormlite.android.loader.OrmLiteCursorLoader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

public class OrmCursorLoaderCallback<T, ID> implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = OrmCursorLoaderCallback.class.getSimpleName();

    protected Context context;
    protected Dao<T, ID> dao;
    protected PreparedQuery<T> query;
    protected OrmLiteCursorAdapter<T, ? extends View> adapter;

    public OrmCursorLoaderCallback(Context context, Dao<T, ID> dao, PreparedQuery<T> query, OrmLiteCursorAdapter<T, ? extends View> adapter) {
        this.context = context;
        this.dao = dao;
        this.query = query;
        this.adapter = adapter;
    }

    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new OrmLiteCursorLoader<T>(context, dao, query);
    }

    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        adapter.changeCursor(cursor, query);
    }

    public void onLoaderReset(Loader<Cursor> arg0) {
        adapter.changeCursor(null, null);
    }

}