/**
 *
 */
package com.utility;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class GenDao implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(GenDao.class);

	public GenDao() {
		super();
	}

	public static void main(String[] args) {
		GenDao app = new GenDao();
		GenConfig config = new GenConfig();

		config.setPkgDao("com.prudential.prupayment.bo");
		config.setPkgBean("com.prudential.prupayment.bo");
		config.setBeanName("EmplCache");

		config.setUrl("jdbc:db2://10.163.170.185:55701/PRUPAYDB");
		config.setUserName("finapp");
		config.setPwd("abcd1234");
		config.setDriver("com.ibm.db2.jcc.DB2Driver");

		config.setTable("EMPL_CACHE");
		config.setSchema("PRUPAY");

		try {
			List<ColInfo> listCol = app.findColInfo(config);
			for (ColInfo col : listCol) {
				col.setAttrName(app.jcolName(col.getColName()));
				col.setJavaType(app.jtype(col.getDbType()));
				col.setMethName(app.methName(col.getColName()));
				col.setJdbcSetter(app.jdbcSetter(col.getDbType()));
			}

			app.genDao(config, listCol);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void genDao(GenConfig config, List<ColInfo> list) {
		int size = list.size();
		String bean = config.getBeanName();
		boolean hasAutoId = false;

		for (ColInfo col : list) {
			if (col.isAutoIncrement()) {
				hasAutoId = true;
				break;
			}
		}

		pl("----------------------------------");
		pl("package $s;", config.getPkgDao());
		pl("import java.io.Serializable;");
		pl("import java.math.BigDecimal;");

		pl("import java.sql.Connection");
		pl("import java.sql.PreparedStatement");
		pl("import java.sql.ResultSet");
		pl("import java.sql.SQLException");

		pl("import %s.%sBO;", config.getPkgBean(), bean);
		pl("import java.sql.Timestamp;");
		pl("import java.util.Date;");
		pl("");

		pl("public class $sDAO extends PruPaymentDAO {", bean);
		pl("");
		pl(tab(1) + "private String insertSql;");
		pl(tab(1) + "private String updateSql;");
		pl(tab(1) + "private String byIdSql;");
		pl("");

		pl(tab(1) + "public %sDAO() {", bean);
		pl(tab(2) + "super();");
		pl(tab(2) + "table = \"%s\";", config.getTable());

		if (hasAutoId) {
			pl(tab(2) + "autoIncrement = true;");

		} else {
			pl(tab(2) + "autoIncrement = false;");
		}

		for (ColInfo col : list) {
			String colName = col.getColName();

			if (col.isPkey()) {
				pl(tab(2) + "putPrimaryKey(%sBO.FILTER_%s, \"%s\", %s);", bean, colName, colName, jcolName(colName));

			} else {
				pl(tab(2) + "putMetaData(%sBO.FILTER_%s, \"%s\");", bean, colName, colName);
			}
		}

		pl(tab(1) + "}");
		pl("");

		pl(tab(1) + "@Override");
		pl(tab(1) + "protected String prepareInsertStatement() {");
		pl(tab(2) + "if (insertSql == null) {");
		pl(tab(3) + "StringBuilder sql = new StringBuilder();");
		pl(tab(3) + "sql.append(\"insert into \" + getTableName() + \"(\");");

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < size; i++) {
			ColInfo col = list.get(i);
			if (!col.isAutoIncrement()) {
				if (i != (size - 1)) {
					pl(tab(3) + "sql.append(\"%s, \");", col.getColName());
					sb.append("?, ");

				} else {
					pl(tab(3) + "sql.append(\"%s\");", col.getColName());
					sb.append("?");
				}
			}
		}

		pl(tab(3) + "sql.append(\") values (\");");
		pl(tab(3) + "sql.append(\"%s\");", sb.toString());
		pl(tab(3) + "sql.append(\")\");");
		pl(tab(3) + "");
		pl(tab(3) + "insertSql = sql.toString();");
		pl(tab(2) + "}");
		pl(tab(2) + "return insertSql;");
		pl(tab(1) + "}");

		// update SQL string
		pl(tab(1) + "@Override");
		pl(tab(1) + "protected String prepareUpdateStatement() {");
		pl(tab(2) + "if (updateSql == null) {");
		pl(tab(3) + "StringBuilder sql = new StringBuilder();");
		pl(tab(3) + "sql.append(\"update \" + getTableName() + \" set \");");

		for (int i = 0; i < size; i++) {
			ColInfo col = list.get(i);
			if (!col.isPkey()) {
				if (i != (size - 1)) {
					pl(tab(3) + "sql.append(\"%s = ?, \");", col.getColName());

				} else {
					pl(tab(3) + "sql.append(\"%s = ?\");", col.getColName());
				}
			}
		}

		pl(tab(3) + "sql.append(\" WHERE \");");
		int ucount = 0;

		for (int i = 0; i < size; i++) {
			ColInfo col = list.get(i);
			if (col.isPkey()) {
				if (ucount == 0) {
					pl(tab(3) + "sql.append(\" %s = ? \");", col.getColName());

				} else {
					pl(tab(3) + "sql.append(\" AND %s = ? \");", col.getColName());
				}
			}
		}

		pl(tab(3) + "");
		pl(tab(3) + "updateSql = sql.toString();");
		pl(tab(2) + "}");
		pl(tab(2) + "return updateSql;");
		pl(tab(1) + "}");

		pl("");

		// setAttributes
		pl(tab(1)
				+ "public void setAttributes(AbstractBusinessObject abo, ResultSet rs) throws java.sql.SQLException {");
		pl(tab(2) + "super.setAttributes(abo, rs);");
		pl(tab(2) + "%sBO bo = (%sBO) abo;", bean, bean);

		for (int i = 0; i < size; i++) {
			ColInfo col = list.get(i);
			pl(tab(2) + "bo.set%s(rs.get%s(getColumn(%sBO.FILTER_%s)));", col.getMethName(), col.getJdbcSetter(), bean,
					col.getColName());
		}

		pl("");
		pl(tab(2) + "bo.setLoaded(true");
		pl(tab(1) + "}");

		// pst setter
		insSetter(config, list);

		updSetter(config, list);

		updateId(config, list);

		getById(config, list);

		pl(tab(1) + "");
		pl(tab(1) + "");
		pl(tab(1) + "");
		pl(tab(1) + "");
		pl(tab(1) + "");

		pl("");
		pl("}");
	}

	/**
	 *
	 * @param config
	 * @param list
	 */
	private void getById(GenConfig config, List<ColInfo> list) {
		pl("");
		pl(tab(1) + "protected String getByIdSql() {");
		pl(tab(2) + "if (byIdSql == null) {");
		pl(tab(2) + "StringBuilder sql = new StringBuilder();");
		pl(tab(2) + "sql.append(\"SELECT * FROM \" + getTableName());");
		pl(tab(3) + "sql.append(\" WHERE \");");

		int size = list.size();
		int ucount = 0;

		for (int i = 0; i < size; i++) {
			ColInfo col = list.get(i);
			if (col.isPkey()) {
				if (ucount == 0) {
					pl(tab(3) + "sql.append(\" %s = ? \");", col.getColName());

				} else {
					pl(tab(3) + "sql.append(\" AND %s = ? \");", col.getColName());
				}
				ucount++;
			}
		}

		pl(tab(3) + "");
		pl(tab(3) + "byIdSql = sql.toString();");
		pl(tab(2) + "}");
		pl(tab(2) + "return byIdSql;");
		pl(tab(1) + "}");
		pl("");

	}

	/**
	 *
	 * @param config
	 * @param list
	 */
	private void updateId(GenConfig config, List<ColInfo> list) {
		pl("");
		pl(tab(1) + "public void updateId(ResultSet rs, %s bean) throws SQLException {", config.getBeanName());

		for (ColInfo col : list) {
			if (col.isAutoIncrement()) {
				pl(tab(2) + "bean.set%s(rs.getLong(1));", col.getMethName());
			}
		}

		pl(tab(1) + "}");
		pl("");
	}

	/**
	 *
	 * @param config
	 * @param list
	 */
	private void updSetter(GenConfig config, List<ColInfo> list) {
		String bean = config.getBeanName();
		pl("");

		pl(tab(1)
				+ "protected void setUpdateParameters(PreparedStatement stmt, AbStractBusinessObject abo) throws SQLException {");
		pl("");
		pl(tab(2) + "%sBO bo = (%sBO) abo;", bean, bean);
		pl("");
		pl(tab(2) + "int pos = 1");

		for (ColInfo col : list) {
			if (!col.isPkey()) {
				pl(tab(2) + "pstSet%s(stmt, pos++, bo.get%s());", col.getJdbcSetter(), col.getMethName());
			}
		}

		pl(tab(2) + "// primary keys");

		for (ColInfo col : list) {
			if (col.isPkey()) {
				pl(tab(2) + "pstSet%s(stmt, pos++, bo.get%s());", col.getJdbcSetter(), col.getMethName());
			}
		}

		pl(tab(2) + "");
		pl(tab(1) + "}");
		pl("");

	}

	/**
	 *
	 * @param config
	 * @param list
	 */
	private void insSetter(GenConfig config, List<ColInfo> list) {
		String bean = config.getBeanName();
		pl("");

		pl(tab(1)
				+ "protected void setInsertParameters(PreparedStatement stmt, AbStractBusinessObject abo) throws SQLException {");
		pl("");
		pl(tab(2) + "%sBO bo = (%sBO) abo;", bean, bean);
		pl("");
		pl(tab(2) + "int pos = 1");

		for (ColInfo col : list) {
			if (!col.isAutoIncrement()) {
				pl(tab(2) + "pstSet%s(stmt, pos++, bo.get%s());", col.getJdbcSetter(), col.getMethName());
			}
		}

		pl(tab(2) + "");
		pl(tab(1) + "}");
		pl("");
	}

	/**
	 *
	 * @param len
	 * @return
	 */
	private String tab(int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

	/**
	 *
	 * @param format
	 * @param args
	 */
	private void pl(String format, Object... args) {
		System.out.println(String.format(format, args));
	}

	/**
	 *
	 * @param type
	 * @return
	 */
	private String jdbcSetter(int type) {
		String rv = "";

		if (type == Types.BIGINT) {
			rv = "Long";

		} else if (type == Types.VARCHAR) {
			rv = "String";

		} else if (type == Types.TIMESTAMP) {
			rv = "Timestamp";

		} else if (type == Types.DATE) {
			rv = "Date";

		} else if (type == Types.CHAR) {
			rv = "String";

		} else if (type == Types.INTEGER) {
			rv = "Int";

		} else if (type == Types.DECIMAL) {
			rv = "BigDecimal";
		}

		return rv;
	}

	/**
	 *
	 * @param colName
	 * @return
	 */
	public String methName(String colName) {
		StringBuilder sb = new StringBuilder();
		String[] arr = colName.split("_");
		int len = arr.length;

		for (int i = 0; i < len; i++) {
			String tmp = arr[i].toLowerCase().trim();
			if (tmp.length() > 0) {
				sb.append(tmp.substring(0, 1).toUpperCase());
				sb.append(tmp.substring(1));
			}
		}
		return sb.toString();
	}

	/**
	 *
	 * @param type
	 * @return
	 */
	public String jtype(int type) {
		String rv = "";

		if (type == Types.BIGINT) {
			rv = "Long";

		} else if (type == Types.VARCHAR) {
			rv = "String";

		} else if (type == Types.TIMESTAMP) {
			rv = "Timestamp";

		} else if (type == Types.DATE) {
			rv = "Date";

		} else if (type == Types.CHAR) {
			rv = "String";

		} else if (type == Types.INTEGER) {
			rv = "Integer";

		} else if (type == Types.DECIMAL) {
			rv = "BigDecimal";
		}

		return rv;
	}

	/**
	 *
	 * @param colName
	 * @return
	 */
	private String jcolName(String colName) {
		StringBuilder sb = new StringBuilder();
		String[] arr = colName.split("_");
		int len = arr.length;

		for (int i = 0; i < len; i++) {
			if (i == 0) {
				sb.append(arr[i].toLowerCase().trim());

			} else {
				String tmp = arr[i].toLowerCase().trim();
				if (tmp.length() > 0) {
					sb.append(tmp.substring(0, 1).toUpperCase());
					sb.append(tmp.substring(1));
				}
			}
		}
		return sb.toString();
	}

	private List<ColInfo> findColInfo(GenConfig config) {
		List<ColInfo> list = new ArrayList<ColInfo>();
		String url;
		String user;
		String password;
		Connection con;

		url = config.getUrl();
		user = config.getUserName();
		password = config.getPwd();

		try {
			Class.forName(config.getDriver());
			con = DriverManager.getConnection(url, user, password);
			con.setAutoCommit(true);

			DatabaseMetaData meta = con.getMetaData();
			ResultSet colRs = meta.getColumns(null, config.getSchema(), config.getTable(), null);

			while (colRs.next()) {
				ColInfo info = new ColInfo();
				info.setColName(colRs.getString("COLUMN_NAME"));
				info.setDbType(colRs.getInt("DATA_TYPE"));
				info.setColSize(colRs.getInt("COLUMN_SIZE"));
				info.setAutoIncrement("YES".equals(colRs.getString("IS_AUTOINCREMENT")));
				list.add(info);
			}

			colRs.close();

			Set<String> pkeySet = new HashSet<String>();
			ResultSet rsKey = meta.getPrimaryKeys(null, config.getSchema(), config.getTable());

			while (rsKey.next()) {
				pkeySet.add(rsKey.getString("COLUMN_NAME"));
			}

			rsKey.close();

			for (ColInfo col : list) {
				if (pkeySet.contains(col.getColName())) {
					col.setPkey(true);

				} else {
					col.setPkey(false);
				}
			}

			con.close();
			System.out.println("************** Disconnected form data source");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return list;
	}
}
