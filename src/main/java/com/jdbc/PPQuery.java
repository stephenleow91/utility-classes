/**
 *
 */
package com.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author USER
 *
 */
public interface PPQuery<T> {

	/**
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public List<T> map(ResultSet rs) throws SQLException;

	/**
	 *
	 * @return
	 */
	public String columnList();

	/**
	 *
	 * @return
	 */
	public String from();

	/**
	 *
	 * @param map
	 * @return
	 */
	public String where(Map<String, Object> map);

	/**
	 *
	 * @return
	 */
	public String orderString();

	/**
	 *
	 * @param map
	 * @return
	 */
	public String queryString(Map<String, Object> map);

	/**
	 *
	 * @return
	 */
	public String limitString();

}
