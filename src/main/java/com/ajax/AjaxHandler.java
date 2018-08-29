/**
 *
 */
package com.ajax;

import javax.servlet.http.HttpServletRequest;

/**
 * @author USER
 *
 */
public interface AjaxHandler {

	/**
	 *
	 * @param req
	 * @return
	 * @throws AjaxException
	 */
	public AjaxResponse handleAjax(HttpServletRequest req) throws AjaxException;
}
