package com.j256.ormlite.android.loader;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.content.Loader;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

/**
 * A {@link Loader} implementation that queries specified {@link Dao} using a {@link PreparedQuery}.
 * 
 * @author Egorand
 */
public class OrmLitePreparedQueryLoader<T, ID> extends BaseOrmLiteLoader<T, ID> {

	private final PreparedQuery<T> preparedQuery;

	public OrmLitePreparedQueryLoader(Context context, Dao<T, ID> dao, PreparedQuery<T> preparedQuery) {
		this(context,dao,preparedQuery,true);
	}

	public OrmLitePreparedQueryLoader(Context context, Dao<T, ID> dao, PreparedQuery<T> preparedQuery, boolean enableAutorefresh) {
		super(context, dao, enableAutorefresh);
		this.preparedQuery = preparedQuery;
	}

	@Override
	protected List<T> runQuery(Dao<T, ID> dao) throws SQLException {
		return dao.query(preparedQuery);
	}
}
