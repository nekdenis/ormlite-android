package com.j256.ormlite.android.loadercallback;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

import com.j256.ormlite.android.loader.OrmLitePreparedQueryLoader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.util.List;

public abstract class OrmObjectLoaderCallback<T, ID> implements LoaderManager.LoaderCallbacks<List<T>> {

    protected Context context;
    protected Dao<T, ID> dao;
    protected PreparedQuery<T> query;

    public OrmObjectLoaderCallback(Context context, Dao<T, ID> dao, PreparedQuery<T> query) {
        this.context = context;
        this.dao = dao;
        this.query = query;
    }

    public Loader<List<T>> onCreateLoader(int arg0, Bundle arg1) {
        return new OrmLitePreparedQueryLoader(context, dao, query);
    }

    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        onLoadFinished(data);
    }

    public void onLoaderReset(Loader<List<T>> arg0) {
    }

    public abstract void onLoadFinished(List<T> data);

}