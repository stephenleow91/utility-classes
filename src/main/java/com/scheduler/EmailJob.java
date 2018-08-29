/**
 *
 */
package com.scheduler;

import java.io.Serializable;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class EmailJob implements Job, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(EmailJob.class);

	/**
	 *
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("shced101 running job: " + context.getJobDetail().getKey());

		try {

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
