/**
 *
 */
package com.jdbc;

import java.io.Serializable;
import java.util.List;

/**
 * @author USER
 *
 */
public class QToken implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private List<String> list;
	private String psql;
	private String orig;

	/**
	 *
	 */
	public QToken() {
		super();
	}

	/**
	 * @return the list
	 */
	public List<String> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<String> list) {
		this.list = list;
	}

	/**
	 * @return the psql
	 */
	public String getPsql() {
		return psql;
	}

	/**
	 * @param psql
	 *            the psql to set
	 */
	public void setPsql(String psql) {
		this.psql = psql;
	}

	/**
	 * @return the orig
	 */
	public String getOrig() {
		return orig;
	}

	/**
	 * @param orig
	 *            the orig to set
	 */
	public void setOrig(String orig) {
		this.orig = orig;
	}

}
