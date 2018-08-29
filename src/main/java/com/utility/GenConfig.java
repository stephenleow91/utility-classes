/**
 *
 */
package com.utility;

import java.io.Serializable;

/**
 * @author USER
 *
 */
public class GenConfig implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String pkgBean;
	private String pkgDao;
	private String beanName;
	private String url;
	private String userName;
	private String pwd;
	private String driver;
	private String schema;
	private String table;

	/**
	 * @return the pkgBean
	 */
	public String getPkgBean() {
		return pkgBean;
	}

	/**
	 * @param pkgBean
	 *            the pkgBean to set
	 */
	public void setPkgBean(String pkgBean) {
		this.pkgBean = pkgBean;
	}

	/**
	 * @return the pkgDao
	 */
	public String getPkgDao() {
		return pkgDao;
	}

	/**
	 * @param pkgDao
	 *            the pkgDao to set
	 */
	public void setPkgDao(String pkgDao) {
		this.pkgDao = pkgDao;
	}

	/**
	 * @return the beanName
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * @param beanName
	 *            the beanName to set
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @param pwd
	 *            the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver
	 *            the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}

}
