/**
 *
 */
package com.utility;

import java.io.Serializable;
import java.util.MissingResourceException;

import org.apache.struts.util.MessageResources;

/**
 * @author USER
 *
 */
public class Struct implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// resource properties file
	private static MessageResources messages = MessageResources.getMessageResources("resources.ApplicationResources");

	/**
	 *
	 */
	private Struct() {
		super();
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public static String getMessage(Object key) {
		String msg = null;

		try {
			msg = messages.getMessage(key.toString());
			if ((msg == null) || (msg.length() <= 0)) {
				msg = key.toString();
			}

		} catch (MissingResourceException ex) {
			msg = key.toString();
		}

		return msg;
	}
}
