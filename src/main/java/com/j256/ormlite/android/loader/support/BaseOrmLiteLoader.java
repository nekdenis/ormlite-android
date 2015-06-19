package com.j256.ormlite.android.loader.support;

import android.content.Context;

import com.j256.ormlite.android.loader.FixedAsyncTaskLoader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.DaoObserver;

import java.sql.SQLException;
import java.util.List;

/**
 * An abstract superclass for the ORMLite Loader classes, which closely resembles to the Android's
 * <code>CursorLoader</code>. Implements basic loading and synchronization logic.
 *
 * This is the support version.  This is basically copy/paste of the "standard" AsyncTaskLoader version.
 *
 * @author EgorAnd
 */
public abstract class BaseOrmLiteLoader<T, ID> extends FixedAsyncTaskLoader<List<T>> implements DaoObserver {

	private final Dao<T, ID> dao;
	private List<T> cachedResults;

	public BaseOrmLiteLoader(Context context, Dao<T, ID> dao) {
		super(context);
		if(dao == null)
			throw new IllegalArgumentException("Dao cannot be null");
		this.dao = dao;
	}

	@Override
	public void deliverResult(List<T> results) {
		if (isReset())
		{
			return;
		}

		List<T> oldData = cachedResults;
		cachedResults = results;

		if (isStarted())
		{
			super.deliverResult(cachedResults);
		}

		if (oldData != null)
		{
			onReleaseResources(oldData);
		}
	}

	protected abstract List<T> runQuery(Dao<T, ID> dao)throws SQLException;

	@Override
	public final List<T> loadInBackground() {
		try {
			return runQuery(dao);
		} catch (SQLException e) {
			return handleError(e);
		}
	}

	/**
	 * You should normally bomb if you're having a SQLException.  If you *need* to
	 * do something else, override this method.
	 *
	 * @param e
	 * @return
	 */
	protected List<T> handleError(SQLException e) {
		throw new RuntimeException(e);
	}

	/**
	 * Starts an asynchronous load of the data. When the result is ready the callbacks will be called on the UI thread.
	 * If a previous load has been completed and is still valid the result may be passed to the callbacks immediately.
	 * 
	 * <p>
	 * Must be called from the UI thread.
	 * </p>
	 */
	@Override
	protected void onStartLoading() {
		if (cachedResults != null) {
			deliverResult(cachedResults);
		}
		if (takeContentChanged() || cachedResults == null) {
			forceLoad();
		}
		// watch for data changes
		dao.registerObserver(this);
	}

	/**
	 * Must be called from the UI thread
	 */
	@Override
	protected void onStopLoading() {
		// attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();

		// ensure the loader is stopped
		onStopLoading();
		if (cachedResults != null) {
			onReleaseResources(cachedResults);
			cachedResults = null;
		}

		// stop watching for changes
		dao.unregisterObserver(this);
	}

	public void onChange() {
		onContentChanged();
	}

	/**
	 * If you want to clear the list, or do something else on remove, do it here.
	 * *NOTE* It is very unlikely that you'll want to override this.
	 *
	 * @param data
	 */
	protected void onReleaseResources(List<T> data)
	{
	}
}
