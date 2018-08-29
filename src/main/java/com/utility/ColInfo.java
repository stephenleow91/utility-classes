/**
 *
 */
package com.utility;

import java.io.Serializable;

/**
 * @author USER
 *
 */
public class ColInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String colName;
	private String attrName;
	private String tableName;
	private String schemaName;

	private int dbType;
	private int colSize;

	private String javaType;
	private String methName;
	private String jdbcSetter;

	private boolean pkey;
	private boolean autoIncrement;

	/**
	 *
	 */
	public ColInfo() {
		super();
	}

	/**
	 * @return the colName
	 */
	public String getColName() {
		return colName;
	}

	/**
	 * @param colName
	 *            the colName to set
	 */
	public void setColName(String colName) {
		this.colName = colName;
	}

	/**
	 * @return the attrName
	 */
	public String getAttrName() {
		return attrName;
	}

	/**
	 * @param attrName
	 *            the attrName to set
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param schemaName
	 *            the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * @return the dbType
	 */
	public int getDbType() {
		return dbType;
	}

	/**
	 * @param dbType
	 *            the dbType to set
	 */
	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	/**
	 * @return the colSize
	 */
	public int getColSize() {
		return colSize;
	}

	/**
	 * @param colSize
	 *            the colSize to set
	 */
	public void setColSize(int colSize) {
		this.colSize = colSize;
	}

	/**
	 * @return the javaType
	 */
	public String getJavaType() {
		return javaType;
	}

	/**
	 * @param javaType
	 *            the javaType to set
	 */
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	/**
	 * @return the methName
	 */
	public String getMethName() {
		return methName;
	}

	/**
	 * @param methName
	 *            the methName to set
	 */
	public void setMethName(String methName) {
		this.methName = methName;
	}

	/**
	 * @return the jdbcSetter
	 */
	public String getJdbcSetter() {
		return jdbcSetter;
	}

	/**
	 * @param jdbcSetter
	 *            the jdbcSetter to set
	 */
	public void setJdbcSetter(String jdbcSetter) {
		this.jdbcSetter = jdbcSetter;
	}

	/**
	 * @return the pkey
	 */
	public boolean isPkey() {
		return pkey;
	}

	/**
	 * @param pkey
	 *            the pkey to set
	 */
	public void setPkey(boolean pkey) {
		this.pkey = pkey;
	}

	/**
	 * @return the autoIncrement
	 */
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * @param autoIncrement
	 *            the autoIncrement to set
	 */
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

}
