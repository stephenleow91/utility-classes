/**
 *
 */
package com.ajax;

import java.io.Serializable;

/**
 * @author USER
 *
 */
public class AjaxResponse implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private int status;
	private Object data;
	private String message;

	/**
	 *
	 */
	public AjaxResponse() {
		super();
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
