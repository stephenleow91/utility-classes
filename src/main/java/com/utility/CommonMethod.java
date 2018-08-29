/**
 *
 */
package com.utility;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class CommonMethod implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(CommonMethod.class);

	/**
	 *
	 */
	private CommonMethod() {
		super();
	}

	/**
	 *
	 * @param s
	 * @param one
	 * @param another
	 * @return
	 */
	public static String getReplaceString(String s, String one, String another) {
		if (s.equals("")) {
			return "";
		}

		String res = "";
		int i = s.indexOf(one, 0);
		int lastpos = 0;

		while (i != -1) {
			res = res + s.substring(lastpos, i) + another;
			lastpos = i + one.length();
			i = s.indexOf(one, lastpos);
		}
		res = res + s.substring(lastpos);
		return res;
	}

	/**
	 *
	 * @param dt
	 * @return
	 */
	public static int getCurrenctYear(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	/**
	 * To get Period in yyyy-MM
	 *
	 * @return
	 */
	public static String getPeriod() {
		Timestamp timestamp = new Timestamp(new Date().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(new Date(timestamp.getTime()));
	}

	/**
	 * To get Date with dd/MM/yyyy format
	 *
	 * @param strDate
	 * @return
	 */
	public static String formatDate(String strDate) {
		String rv = "";

		try {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date date = formatter.parse(strDate);
			SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
			rv = newFormat.format(date);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return rv;
	}

	/**
	 *
	 * @param strDate
	 * @return
	 */
	public static String reFormatDate(String strDate) {
		String rv = "";

		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = formatter.parse(strDate);
			SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
			rv = newFormat.format(date);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return rv;
	}

	/**
	 *
	 * @return
	 */
	public static String getCurrentDateTime() {
		String rv = "";

		try {
			DateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
			Date date = new Date(System.currentTimeMillis());
			rv = formatter.format(date);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return rv;
	}

	/**
	 * Date format dd-MM-yyyy
	 *
	 * @param strDate
	 * @return
	 */
	public static final Timestamp strToTs(String strDate) {
		Timestamp ts = null;
		try {
			Date dt = org.apache.commons.lang3.time.DateUtils.parseDate(strDate, "dd-MM-yyyy");
			ts = dt != null ? new Timestamp(dt.getTime()) : null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ts;
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	public static boolean isValidBigDecimal(BigDecimal value) {
		boolean rv = false;

		if ((value != null) && (BigDecimal.ZERO.compareTo(value) != 0)) {
			rv = true;
		}

		return rv;
	}

	/**
	 *
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static boolean isSameDt(Date dt1, Date dt2) {
		boolean rv = false;

		if ((dt1 == null) && (dt2 == null)) {
			rv = true;

		} else if ((dt1 != null) && (dt2 != null)) {
			rv = dt1.compareTo(dt2) == 0;
		}

		return rv;
	}

	/**
	 *
	 * @param bd1
	 * @param bd2
	 * @return
	 */
	public static BigDecimal getSubtractAmount(BigDecimal bd1, BigDecimal bd2) {
		BigDecimal rv = BigDecimal.ZERO;

		if (bd1 != null) {
			rv = bd1;
		}

		if (bd2 != null) {
			rv = rv.subtract(bd2).setScale(2, RoundingMode.HALF_UP);
		}

		return rv;
	}

	/**
	 *
	 * @param bd1
	 * @param bd2
	 * @return
	 */
	public static BigDecimal getSumAmount(BigDecimal bd1, BigDecimal bd2) {
		BigDecimal rv = BigDecimal.ZERO;

		if (bd1 != null) {
			rv = bd1;
		}

		if (bd2 != null) {
			rv = rv.add(bd2).setScale(2, RoundingMode.HALF_UP);
		}

		return rv;
	}

	/**
	 *
	 * @param value
	 * @param multiplier
	 * @return
	 */
	public static BigDecimal getMultiplyAmount(BigDecimal value, BigDecimal multiplier) {
		BigDecimal rv = BigDecimal.ZERO;

		if (value != null) {
			rv = value;
			rv = rv.setScale(2, RoundingMode.HALF_UP);
		}

		if (multiplier != null) {
			rv = rv.multiply(multiplier);
			rv = rv.setScale(2, RoundingMode.HALF_UP);
		}

		return rv;
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	public static String checkNull(String value) {
		return value != null ? value : "";
	}

	/**
	 *
	 * @param a
	 * @return
	 */
	public static boolean compareObject(Object a, Object b) {
		boolean rv = false;
		String sa = "";
		String sb = "";

		if (a != null) {
			sa = String.valueOf(a);
		}

		if (b != null) {
			sb = String.valueOf(b);
		}

		if (StringUtils.equals(sa, sb)) {
			rv = true;
		}

		return rv;
	}

	/**
	 *
	 * @param value
	 * @param scale
	 * @return
	 */
	public static BigDecimal toBigDecimal(String value, int scale) {
		BigDecimal rv = BigDecimal.ZERO;

		try {
			if (!StringUtils.isEmpty(value)) {
				rv = new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return rv;
	}

	/**
	 *
	 * @param bd1
	 * @param bd2
	 * @return
	 */
	public static boolean isSameBd(BigDecimal bd1, BigDecimal bd2) {
		boolean rv = false;

		if ((bd1 == null) && (bd2 == null)) {
			rv = true;

		} else if ((bd1 != null) && (bd2 != null)) {
			rv = bd1.compareTo(bd2) != 0;
		}

		return rv;
	}

	/**
	 * To check if 2 decimal are the same
	 *
	 * @param bd1
	 * @param bd2
	 * @return
	 */
	public static boolean compareWithNullDecimal(BigDecimal bd1, BigDecimal bd2) {
		boolean rv = false;

		if (bd1 == null) {
			bd1 = BigDecimal.ZERO;
		}

		if (bd2 == null) {
			bd2 = BigDecimal.ZERO;
		}

		rv = bd1.compareTo(bd2) == 0;
		return rv;
	}

	/**
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean compareWithNull(String a, String b) {
		String sa = a;
		String sb = b;

		if (sa == null) {
			sa = "";
		}

		if (sb == null) {
			sb = "";
		}

		return StringUtils.equals(sa, sb);
	}

	/**
	 *
	 * @param strDate
	 * @return
	 */
	public static int getDiffMonths(String strDate) {
		Date currDate = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);

		String dateDay = strDate.substring(0, 2);
		String dateMonth = strDate.substring(3, 5);
		String dateYear = strDate.substring(6, 10);

		int intDay = Integer.parseInt(dateDay);
		int intMonth = Integer.parseInt(dateMonth);
		int intYear = Integer.parseInt(dateYear);

		int diffMonths = 0;

		if (cal.get(Calendar.YEAR) != intYear) {
			diffMonths = (cal.get(Calendar.MONTH) + 1 + 12) - intMonth;

		} else {
			diffMonths = (cal.get(Calendar.MONTH) + 1) - intMonth;
		}

		if (intDay < cal.get(Calendar.DATE)) {
			diffMonths = diffMonths + 1;
		}

		logger.trace("day : {} - month : {}, year : {}", intDay, intMonth, intYear);
		return diffMonths;
	}
}
