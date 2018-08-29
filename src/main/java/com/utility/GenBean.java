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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class GenBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(GenBean.class);

	/**
	 *
	 */
	public GenBean() {
		super();
	}

	public static void main(String[] args) {
		GenBean app = new GenBean();
		GenConfig config = new GenConfig();

		config.setPkgBean("sg.prudential.prupayment.bo");
		config.setBeanName("EmpCacheBO");
		config.setUrl("jdbc:db2://10.163.170.185:55701/PRUPAYDB");
		config.setUserName("finapp");
		config.setPwd("abcd1234");
		config.setDriver("com.ibm.db2.jcc.DB2Driver");

		try {
			List<ColInfo> listCol = app.findColInfo(config);

			for (ColInfo col : listCol) {
				col.setAttrName(app.jcolName(col.getColName()));
				col.setJavaType(app.jtype(col.getDbType()));
				col.setMethName(app.methName(col.getColName()));
			}

			app.genBean(config, listCol);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 *
	 * @param config
	 * @return
	 */
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
			con.setAutoCommit(false);

			DatabaseMetaData meta = con.getMetaData();
			ResultSet colRs = meta.getColumns(null, "PRUPAY", "EMP_CACHE", null);

			while (colRs.next()) {
				ColInfo info = new ColInfo();
				info.setColName(colRs.getString("COLUMN_NAME"));
				info.setDbType(colRs.getInt("DATA_TYPE"));
				info.setColSize(colRs.getInt("COLUMN_SIZE"));
				list.add(info);
			}

			colRs.close();
			con.close();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return list;
	}

	/**
	 *
	 * @param colName
	 * @return
	 */
	public String jcolName(String colName) {
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
	public String filter(String colName) {
		return String.format("public final static String FILTER_%s = \"%s\";", colName, colName);
	}

	public void genBean(GenConfig config, List<ColInfo> list) {
		pl("--------------------------");
		pl("package %s;", config.getPkgBean());
		pl("import java.io.Serializable;");
		pl("import java.math.BigDecimal;");
		pl("import java.sql.Timestamp;");
		pl("import java.util.Date;");

		pl("");
		pl("public class %s extends PruPaymentBusinessObject {", config.getBeanName());
		pl("");
		pl(tab(1) + "private static final long serialVersionUID = 1L;");

		pl("");
		for (ColInfo col : list) {
			pl(tab(1) + filter(col.getColName()));
		}

		pl("");
		for (ColInfo col : list) {
			pl(tab(1) + "private $s $s;", col.getJavaType(), col.getAttrName());
		}

		pl("");
		pl(tab(1) + "public %s() {", config.getBeanName());
		pl(tab(2) + "super();");
		pl(tab(1) + "}");

		for (ColInfo col : list) {
			pl("");
			pl(tab(1) + "public %s get$s() {", col.getJavaType(), col.getMethName());
			pl(tab(2) + "return %s;", col.getAttrName());
			pl(tab(1) + "}");

			pl("");
			pl(tab(1) + "public void set$s(%s %s) {", col.getMethName(), col.getJavaType(), col.getAttrName());
			pl(tab(2) + "this.%s = $s;", col.getAttrName(), col.getAttrName());
			pl(tab(1) + "}");
		}

		pl("}");
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

}
