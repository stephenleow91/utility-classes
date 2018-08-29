/**
 *
 */
package com.thread;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class SingleThreadManager implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(SingleThreadManager.class);

	private static SingleThreadManager instance = new SingleThreadManager();
	private boolean running;

	/**
	 *
	 */
	public SingleThreadManager() {
		super();
	}

	/**
	 *
	 * @return
	 */
	public static final SingleThreadManager getInstance() {
		return instance;
	}

	/**
	 *
	 * @param mapCriteria
	 * @return
	 */
	public synchronized boolean genReportAsThread(Map<String, Object> mapCriteria) {

		try {
			if (!running) {
				running = true;

				// init and run as thread
				// the class will extends Thread and override run method
				// DetailReportThread thead = new DetailReportThread(mapCriteria);
				// thread.start();

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return running;
	}

	/**
	 *
	 */
	public synchronized void completed() {
		logger.info("thread completed job");
		running = false;
	}

}
