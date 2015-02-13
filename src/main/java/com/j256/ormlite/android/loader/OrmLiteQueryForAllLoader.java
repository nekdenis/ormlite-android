package com.j256.ormlite.android.loader;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

/**
 * A <code>Loader</code> implementation that queries specified {@link com.j256.ormlite.dao.Dao} for all data, using the
 * <code>Dao.queryForAll()</code> call.
 * 
 * @author EgorAnd
 */
public class OrmLiteQueryForAllLoader<T, ID> extends BaseOrmLiteLoader<T, ID> {

	public OrmLiteQueryForAllLoader(Context context, Dao<T, ID> dao) {
		this(context, dao, true);
	}
	public OrmLiteQueryForAllLoader(Context context, Dao<T, ID> dao, boolean enableAutorefresh) {
		super(context, dao, enableAutorefresh);
	}

	@Override
	protected List<T> runQuery(Dao<T, ID> dao) throws SQLException {
		return dao.queryForAll();
	}
}
