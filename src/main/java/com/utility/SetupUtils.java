/**
 *
 */
package com.utility;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class SetupUtils implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SetupUtils.class);

	private static final String APP_MODE_DEV = "DEV";
	private static final String APP_MODE_UAT = "DEV";
	private static final String APP_MODE_PROD = "DEV";

	/**
	 *
	 * @return
	 */
	public static boolean isDev() {
		return checkAppMode(APP_MODE_DEV);
	}

	/**
	 *
	 * @return
	 */
	public static boolean isUAT() {
		return checkAppMode(APP_MODE_UAT);
	}

	/**
	 *
	 * @return
	 */
	public static boolean isProd() {
		return checkAppMode(APP_MODE_PROD);
	}

	/**
	 *
	 * @param val
	 * @return
	 */
	private static boolean checkAppMode(String val) {
		boolean rv = false;

		// TODO read from properties file
		String env = APP_MODE_DEV;
		if (env.equals(val)) {
			rv = true;
		}
		return rv;
	}

}
