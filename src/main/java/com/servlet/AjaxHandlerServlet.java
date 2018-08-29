/**
 *
 */
package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ajax.AjaxExample;
import com.ajax.AjaxHandler;
import com.ajax.AjaxHandlerManager;
import com.ajax.AjaxResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author USER
 *
 */
public class AjaxHandlerServlet extends HttpServlet implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(AjaxHandlerServlet.class);
	private static final String ATTRIBUTE_EXAMPLE = "ajaxExample";
	private static final String ERROR_MESSAGE = "Bad request. Please try it again,";
	private static final int ERROR_CODE_400 = 400;
	private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

	/**
	 *
	 */
	public AjaxHandlerServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			AjaxHandlerManager.getInstance().register(ATTRIBUTE_EXAMPLE, AjaxExample.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 *
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAjax(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAjax(request, response);
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doAjax(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if (isSessionActive(request)) {
				String ajaxCmd = request.getParameter("");
				AjaxHandler handler = AjaxHandlerManager.getInstance().createHandler(ajaxCmd);
				AjaxResponse ajaxResp = handler.handleAjax(request);
				printResp(response, ajaxResp);

			} else {
				errorResp(response);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 *
	 * @param response
	 * @param resp
	 */
	private void printResp(HttpServletResponse response, AjaxResponse resp) {
		try {
			response.setContentType(CONTENT_TYPE_APPLICATION_JSON);
			PrintWriter pw = response.getWriter();
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(resp);
			pw.write(jsonString);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	private boolean isSessionActive(HttpServletRequest request) {
		boolean rv = true;
		if (!request.isRequestedSessionIdValid()) {
			rv = false;
		}
		return rv;
	}

	/**
	 *
	 * @param response
	 */
	private void errorResp(HttpServletResponse response) {
		try {

			PrintWriter pw = response.getWriter();
			response.setContentType(CONTENT_TYPE_APPLICATION_JSON);
			response.setStatus(ERROR_CODE_400);
			pw.write(ERROR_MESSAGE);
			pw.flush();
			pw.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
