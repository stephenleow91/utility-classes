/**
 *
 */
package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class JdbcQueryDao {

	private static Logger logger = LoggerFactory.getLogger(JdbcQueryDao.class);

	/**
	 *
	 */
	public JdbcQueryDao() {
		super();
	}

	/**
	 *
	 * @param query
	 * @param map
	 * @return
	 */
	public <T> List<T> query(PPQuery<T> query, Map<String, Object> map) {
		List<T> list = new ArrayList<T>();
		Connection rconn = null;

		try {
			rconn = getReadOnlyConnection();
			String sql = query.queryString(map);
			String orderBy = query.orderString();

			if (orderBy != null) {
				sql += orderBy;
			}

			ResultSet rs = QueryHelper.namedParamQuery(rconn, sql, map);
			list = query.map(rs);

			logger.trace("query : " + sql);
			rs.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}

	public <T> List<T> query(PPQuery<T> query, Map<String, Object> map, int start, int end) {
		List<T> list = new ArrayList<T>();
		Connection rconn = null;

		try {
			rconn = getReadOnlyConnection();
			String sql = limitSql(query.columnList(), query.from(), query.where(map), query.orderString(), start, end);

			long startTs = System.currentTimeMillis();
			ResultSet rs = QueryHelper.namedParamQuery(rconn, sql, map);
			long endTs = System.currentTimeMillis();
			logger.trace("Time used : " + (end - start));

			list = query.map(rs);

			logger.trace("query with start and end : " + sql);
			rs.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 *
	 * @param columnList
	 * @param from
	 * @param where
	 * @param orderBy
	 * @param start
	 * @param end
	 * @return
	 */
	private String limitSql(String columnList, String from, String where, String orderBy, int start, int end) {
		StringBuilder buf = new StringBuilder();
		buf.append("select * from (");

		buf.append("select ROW_NUMBER() OVER(").append(orderBy).append(") as rn, ").append(columnList);
		buf.append(from);

		if ((where != null) && (where.length() > 0)) {
			buf.append(" WHERE ");
			buf.append(where.toString());
		}

		buf.append(orderBy);
		buf.append("");

		buf.append(" where rn between " + start + " and " + end);
		logger.trace("limit sql : " + buf);
		return null;
	}

	/**
	 *
	 * @param sql
	 * @return
	 */
	public long count(String sql) {
		long count = 0;
		Connection conn = null;

		try {
			conn = getReadOnlyConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				count = rs.getLong(1);
			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		} finally {
			closeConn(conn);
		}
		return count;
	}

	/**
	 *
	 * @param query
	 * @param map
	 * @return
	 */
	public <T> long count(PPQuery<T> query, Map<String, Object> map) {
		long rv = 0;
		Connection conn = null;
		try {
			conn = getReadOnlyConnection();
			StringBuilder buf = new StringBuilder();
			buf.append("select count(1)");
			buf.append(" " + query.from());
			String wh = query.where(map);

			if (!StringUtils.isEmpty(wh)) {
				buf.append(" where ");
				buf.append(wh.toString());
			}

			ResultSet rs = QueryHelper.namedParamQuery(conn, buf.toString(), map);
			if (rs.next()) {
				rv = rs.getLong(1);
			}

			rs.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(conn);
		}
		logger.trace("count : " + rv);
		return rv;
	}

	/**
	 *
	 * @param conn
	 */
	private void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

	}

	/**
	 *
	 * @param stmt
	 */
	protected void closeStmt(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 *
	 * @param conn
	 */
	protected void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public Connection getReadOnlyConnection() throws Exception {
		Connection conn = null;
		String url;
		String user;
		String password;
		String driver;

		// TODO connect condition
		if ("Y".equals("Y")) {
			url = "";
			user = "";
			password = "";
			driver = "";

			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, password);
				conn.setReadOnly(true);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return conn;
	}

}
