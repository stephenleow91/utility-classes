/**
 *
 */
package com.ajax;

import java.io.Serializable;

/**
 * @author USER
 *
 */
public class AjaxException extends Exception implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public AjaxException() {
		super();
	}

	/**
	 *
	 * @param message
	 */
	public AjaxException(String message) {
		super(message);
	}

	/**
	 *
	 * @param cause
	 */
	public AjaxException(Throwable cause) {
		super(cause);
	}

	/**
	 *
	 * @param message
	 * @param cause
	 */
	public AjaxException(String message, Throwable cause) {
		super(message, cause);
	}
}
