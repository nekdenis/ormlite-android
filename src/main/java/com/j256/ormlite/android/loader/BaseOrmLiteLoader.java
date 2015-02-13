package com.j256.ormlite.android.loader;

import java.sql.SQLException;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

/**
 * An abstract superclass for the ORMLite Loader classes, which closely resembles to the Android's
 * <code>CursorLoader</code>. Implements basic loading and synchronization logic.
 * 
 * @author EgorAnd
 */
public abstract class BaseOrmLiteLoader<T, ID> extends AsyncTaskLoader<List<T>>  implements Dao.DaoObserver{

	private final Dao<T, ID> dao;
	private List<T> cachedResults;
	private DataProcessor dataProcessor;

	public BaseOrmLiteLoader(Context context, Dao<T, ID> dao) {
		super(context);
		if(dao == null)
			throw new IllegalArgumentException("Dao cannot be null");
		this.dao = dao;
	}
	public BaseOrmLiteLoader(Context context, Dao<T, ID> dao, boolean enableAutorefresh) {
		this(context, dao);
		if(enableAutorefresh){
			dao.registerObserver(this);
		}
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

	public void setDataProcessor(DataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
	}

	protected abstract List<T> runQuery(Dao<T, ID> dao)throws SQLException;

	@Override
	public final List<T> loadInBackground() {
		try {
			List<T> list = runQuery(dao);
			if (dataProcessor != null) {
				dataProcessor.process(list);
			}
			return list;
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

	public void onChange() {
		onContentChanged();
	}
}
