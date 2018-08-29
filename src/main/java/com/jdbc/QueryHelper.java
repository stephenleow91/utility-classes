package com.jdbc;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryHelper {

	private static Logger logger = LoggerFactory.getLogger(QueryHelper.class);

	/**
	 *
	 */
	public QueryHelper() {
		super();
	}

	public static ResultSet namedParamQuery(Connection conn, String sql, Map<String, Object> map) throws SQLException {
		ResultSet rs = null;

		try {
			QToken token = rebuild(sql);
			PreparedStatement pst = conn.prepareStatement(token.getPsql());
			List<String> plist = token.getList();

			if (plist != null) {

				for (int i = 0; i < plist.size(); i++) {
					String param = plist.get(i);
					Object val = map.get(param);

					if (val == null) {
						throw new SQLException("Parameter value not found : " + param);

					} else {
						setParam(pst, i, val);
					}
				}
				rs = pst.executeQuery();
			}
		} catch (Exception e) {
			throw new SQLException("Named Query exceptiom : " + e.getMessage(), e);
		}
		return rs;
	}

	/**
	 *
	 * @param sql
	 * @return
	 */
	public static QToken rebuild(String sql) {
		QToken rv = new QToken();
		rv.setOrig(sql);
		logger.trace("sql : " + sql);

		if (!StringUtils.isEmpty(sql)) {
			List<String> list = new ArrayList<String>();
			boolean param = false;
			StringBuilder pbuf = new StringBuilder();
			StringBuilder nsql = new StringBuilder();

			for (int i = 0; i < sql.length(); i++) {
				char c = sql.charAt(i);

				if (c == ':') {
					if (!param) {
						nsql.append("?");
						param = true;

					} else {
						nsql.append(c);
					}

				} else if ((c == ' ') || (c == ')')) {
					if (param) {
						param = false;
						list.add(pbuf.toString());
						pbuf = new StringBuilder();
						nsql.append(c);

					} else {
						nsql.append(c);
					}

				} else {
					if (param) {
						pbuf.append(c);

					} else {
						nsql.append(c);
					}
				}
			}
			if (param) {
				list.add(pbuf.toString());
			}
			rv.setPsql(nsql.toString());
			rv.setList(list);
		}
		return rv;
	}

	/**
	 *
	 * @param pst
	 * @param i
	 * @param val
	 * @throws SQLException
	 */
	private static void setParam(PreparedStatement pst, int i, Object val) throws SQLException {
		int index = i + 1;
		if (val instanceof String) {
			pst.setString(index, (String) val);

		} else if (val instanceof Date) {
			Timestamp ts = new Timestamp(((Date) val).getTime());
			pst.setTimestamp(index, ts);

		} else if (val instanceof List<?>) {
			StringBuilder buf = new StringBuilder();
			buf.append("(");

			for (Object o : (List<?>) val) {
				if (o != null) {
					buf.append("'" + o.toString() + "',");
				}
			}
			buf.deleteCharAt(buf.length() - 1);
			buf.append(")");

			logger.trace("in query : " + buf.toString());

			if (buf.length() < 256) {
				pst.setString(index, buf.toString());

			} else {
				pst.setCharacterStream(index, new StringReader(buf.toString()));
			}
		} else {
			pst.setObject(index, val);
		}
	}

	/**
	 *
	 * @param wh
	 * @param expr
	 */
	public static void and(StringBuilder wh, String expr) {
		if (wh != null) {
			if (wh.length() > 0) {
				wh.append(" AND ");
			}
			wh.append(expr);
		}
	}

	/**
	 *
	 * @param wh
	 * @param expr
	 * @param expr2
	 */
	public static void from(StringBuilder wh, String expr, String expr2) {
		if (wh != null) {
			if (wh.length() > 0) {
				wh.append(" AND ");
			}
			wh.append(expr + " >= " + expr2);
		}
		logger.trace("FROM : " + wh.toString());
	}

	/**
	 *
	 * @param wh
	 * @param expr
	 * @param expr2
	 */
	public static void to(StringBuilder wh, String expr, String expr2) {
		if (wh != null) {
			if (wh.length() > 0) {
				wh.append(" AND ");
			}
			wh.append(expr + " <= " + expr2);
		}
		logger.trace("TO : " + wh.toString());
	}

	/**
	 *
	 * @param wh
	 * @param expr
	 * @param expr2
	 * @param expr3
	 */
	public void between(StringBuilder wh, String expr, String expr2, String expr3) {
		if (wh != null) {
			if (wh.length() > 0) {
				wh.append(" AND ");
			}
			wh.append(expr + " BETWEEN ");
			wh.append(expr2 + " AND ");
			wh.append(expr3);
		}
		logger.trace("BETWEEN : " + wh.toString());
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public static String fullTableName(String name) {
		StringBuilder buf = new StringBuilder();
		// TODO get database scheme
		buf.append("");
		return buf.toString();
	}
}
