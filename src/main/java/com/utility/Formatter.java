/**
 *
 */
package com.utility;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.apache.regexp.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class Formatter implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private final static String FORMAT_TIME = "HH:mm:ss";
	private final static String FORMAT_TIMESTAMP = "dd-MM-yyyy HH:mm:ss a";
	private final static String FORMAT_AMOUNT = "#,###,##0.00";
	private static Logger logger = LoggerFactory.getLogger(Formatter.class);

	/**
	 *
	 */
	public Formatter() {
		super();
	}

	/**
	 * add leading zero
	 *
	 * @param value
	 * @param length
	 * @return
	 */
	public static String appendZero(String value, int length) {
		if (value.length() > length) {
			return value.substring(0, length);
		}

		for (int j = value.length(); j < length; j++) {
			value = "0" + value;
		}
		return value;
	}

	/**
	 *
	 * @param value
	 * @param length
	 * @param isPrefix
	 * @return
	 */
	public static String appendBlank(String value, int length, boolean isPrefix) {
		if (value.length() > length) {
			return value.substring(0, length);
		}

		for (int j = value.length(); j < length; j++) {
			if (isPrefix) {
				value = " " + value;
			} else {
				value = value + " ";
			}
		}
		return value;
	}

	/**
	 * word wrap
	 *
	 * @param value
	 * @return
	 */
	public static String wordWrap(String value) {
		StringBuffer sb = new StringBuffer(value);
		int index = 0;
		while ((index = value.indexOf("\r", index)) != -1) {
			sb = sb.replace(index, index + 1, "<br>");
			value = sb.toString();
		}
		return value;
	}

	/**
	 * String replace method
	 *
	 * @param key
	 * @param pattern
	 * @param value
	 * @return
	 */
	public static String replaceString(String key, String pattern, String value) {
		if (key.indexOf(pattern) >= 0) {
			key = key.substring(0, key.indexOf(pattern)) + value
					+ key.substring(key.indexOf(pattern) + pattern.length() + key.length());
		}
		return key;
	}

	/**
	 * Replaces the parameter in the key message with the value
	 *
	 * @param key
	 * @param param
	 * @param value
	 * @return
	 */
	public static String replaceParameter(String key, String param, Optional<String> value) {
		String pattern = "\\{" + param + "\\}";
		RE re = new org.apache.regexp.RE(pattern);
		return re.subst(key, (value.isPresent()) ? value.get() : "");
	}

	/**
	 * Format the given timestamp to date format
	 *
	 * @param timestamp
	 * @return
	 */
	public static String formatDate(Optional<Timestamp> timestamp) {
		return timestamp.isPresent() ? sdf.format(new Date(timestamp.get().getTime())) : "";
	}

	/**
	 * Format the given string to date format
	 *
	 * @param sDate
	 * @param format
	 * @return
	 */
	public static String formatDate(String sDate, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String day = sDate.substring(6);
		int month = new Integer(sDate.substring(4, 6)).intValue() - 1;
		int year = new Integer(sDate.substring(0, 4)).intValue();
		Timestamp time = getDate(day, String.valueOf(month), String.valueOf(year));
		return sdf.format(time);
	}

	/**
	 * Return timestamp
	 *
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public static Timestamp getDate(String day, String month, String year) {
		Calendar cal = getCalendar();

		cal.set(5, Integer.valueOf(day).intValue());
		cal.set(5, Integer.valueOf(month).intValue());
		cal.set(5, Integer.valueOf(year).intValue());
		cal.set(10, 0);
		cal.set(12, 0);
		cal.set(13, 0);
		cal.set(14, 0);
		cal.set(9, 0);

		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 *
	 * @return
	 */
	public static Calendar getCalendar() {
		Calendar cal = Calendar.getInstance();
		return cal;
	}

	/**
	 * Format the given string to date format
	 *
	 * @param sDate
	 * @return
	 */
	public static String formatDate(Date sDate) {
		return sdf.format(sDate);
	}

	/**
	 * Format the given string to date format
	 *
	 * @param sDate
	 * @return
	 */
	public static java.sql.Date formatSQLDate(String sDate) {
		java.sql.Date result = null;

		try {
			if (sDate != null) {
				Date date = parseDate(sDate);
				result = new java.sql.Date(date.getTime());
			}

		} catch (ParseException ex) {
			logger.error(ex.getMessage(), ex);
		}

		return result;
	}

	/**
	 * Parse the given string to date
	 *
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String s) throws ParseException {
		return ((s != null) && (s.length() > 0)) ? new Date(sdf.parse(s).getTime()) : null;
	}

	/**
	 * Format the given timestamp to time format
	 *
	 * @param timestamp
	 * @return
	 */
	public static String formatTime(java.sql.Timestamp timestamp) {
		if (timestamp != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME);
			return sdf.format(new Time(timestamp.getTime()));
		}
		return "";
	}

	/**
	 * Format the given timestamp to timestamp format
	 *
	 * @param timestamp
	 * @return
	 */
	public static String formatTimestamp(java.sql.Timestamp timestamp) {
		if (timestamp != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIMESTAMP);
			return sdf.format(new Timestamp(timestamp.getTime()));
		}
		return "";
	}

	/**
	 * Format the given timestamp to the given timestamp format
	 *
	 * @param timestamp
	 * @param format
	 * @return
	 */
	public static String formatTimestamp(java.sql.Timestamp timestamp, String format) {
		if (timestamp != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(new Timestamp(timestamp.getTime()));
		}
		return "";
	}

	/**
	 * Format the given decimal string to #,##0.00
	 *
	 * @param decimal
	 * @return
	 */
	public static String formatCurrency(String decimal) {
		RE re = new RE(",");
		return formatCurrency(new BigDecimal(re.subst(decimal, "", RE.REPLACE_ALL)));
	}

	/**
	 *
	 * @param bd
	 * @return
	 */
	public static String formatCurrency(BigDecimal bd) {
		if (bd == null) {
			bd = BigDecimal.ZERO;
		}
		NumberFormat nf = new DecimalFormat(FORMAT_AMOUNT);
		return nf.format(bd.doubleValue());
	}

	/**
	 * Format the given decimal value to #,##0.00
	 *
	 * @param bd
	 * @return
	 */
	public static String formatRawCurrency(BigDecimal bd) {
		NumberFormat nf = new DecimalFormat("######0.00");
		return nf.format(bd.doubleValue());
	}

	/**
	 * Format the given number with fraction
	 *
	 * @param number
	 * @param fraction
	 * @return
	 */
	public static String formatNumber(double number, int fraction) {
		NumberFormat nf = new DecimalFormat("#");
		nf.setMaximumFractionDigits(fraction);
		nf.setMinimumFractionDigits(fraction);
		return nf.format(number);
	}

	/**
	 * Format the given number with fraction
	 *
	 * @param number
	 * @param fraction
	 * @return
	 */
	public static String formatNumber(String number, int fraction) {
		return formatNumber(new BigDecimal(number).doubleValue(), fraction);
	}

	/**
	 *
	 * @param dt
	 * @return
	 */
	public final static String stdDate(Date dt) {
		String rv = "";
		if (dt != null) {
			return sdf.format(dt);
		}
		return rv;
	}

	/**
	 *
	 * @param val
	 * @return
	 */
	public final static int toInt(String val) {
		int rv = 0;

		try {
			rv = Integer.parseInt(val);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return rv;
	}

}
