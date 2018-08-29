/**
 *
 */
package com.ajax;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class AjaxHandlerManager implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(AjaxHandlerManager.class);
	private static AjaxHandlerManager instance = new AjaxHandlerManager();
	private Map<String, Class<? extends AjaxHandler>> mapHandler = new HashMap<String, Class<? extends AjaxHandler>>();

	/**
	 *
	 */
	public AjaxHandlerManager() {
		super();
	}

	/**
	 *
	 * @return
	 */
	public static AjaxHandlerManager getInstance() {
		return instance;
	}

	/**
	 *
	 * @param ajaxCmd
	 * @return
	 */
	public AjaxHandler createHandler(String ajaxCmd) {
		logger.trace("createHandler - ajaxCmd{} - map {}", ajaxCmd, mapHandler.get(ajaxCmd));
		AjaxHandler rv = null;
		Class<? extends AjaxHandler> cls = mapHandler.get(ajaxCmd);

		try {
			if (cls != null) {
				rv = cls.newInstance();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return rv;
	}

	/**
	 *
	 * @param ajaxCmd
	 * @param cls
	 */
	public void register(String ajaxCmd, Class<? extends AjaxHandler> cls) {
		if ((ajaxCmd != null) && (cls != null)) {
			mapHandler.put(ajaxCmd, cls);
			logger.info("registering {} : {}", ajaxCmd, cls.getName());
		}
	}

}
