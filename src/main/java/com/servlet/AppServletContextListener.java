/**
 *
 */
package com.servlet;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scheduler.SchedulerManager;

/**
 * @author USER
 *
 */
public class AppServletContextListener implements ServletContextListener, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(AppServletContextListener.class);

	/**
	 *
	 */
	public AppServletContextListener() {
		super();
	}

	/**
	 *
	 */
	public void contextDestroyed(ServletContextEvent event) {
		SchedulerManager.getInstance().shutdown();

	}

	/**
	 *
	 */
	public void contextInitialized(ServletContextEvent event) {
		// start up scheduler
		try {
			SchedulerManager.getInstance().startUp();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
