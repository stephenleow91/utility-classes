/**
 *
 */
package com.scheduler;

import java.io.Serializable;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utility.SetupUtils;
import com.utility.Struct;

/**
 * @author USER
 *
 */
public class SchedulerManager implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(SchedulerManager.class);

	private static final String JOB_EMAIL = "email";
	private static final String CONST_YES = "yes";
	private static final String CONST_NO = "no";
	private static SchedulerManager instance = new SchedulerManager();
	private Scheduler scheduler;

	/**
	 *
	 */
	public SchedulerManager() {
		super();
	}

	/**
	 *
	 * @return
	 */
	public static SchedulerManager getInstance() {
		return instance;
	}

	/**
	 *
	 * @return
	 */
	public boolean startUp() {
		boolean rv = false;
		try {
			SchedulerFactory schedFactory = new StdSchedulerFactory();
			scheduler = schedFactory.getScheduler();

			if (SetupUtils.isDev()) {
				registerCronJob(JOB_EMAIL, "group1", "0 0/5 * * * ?", EmailJob.class);
			}

			scheduler.start();
			rv = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rv = false;
		}

		return rv;
	}

	/**
	 *
	 * @param jobName
	 * @param group
	 * @param cronDefault
	 * @param cls
	 * @throws SchedulerException
	 */
	public void registerCronJob(String jobName, String group, String cronDefault, Class<? extends Job> cls)
			throws SchedulerException {
		String enableKey = "sched." + jobName + ".enabled";
		String cronKey = "sched." + jobName + ".cron";

		String enable = getConfig(enableKey, CONST_NO);
		String cron = getConfig(cronKey, cronDefault);

		if (CONST_YES.equals(enable)) {
			JobDetail job = JobBuilder.newJob(cls).withIdentity(jobName, group).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_" + jobName + group)
					.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
			logger.info(String.format("sched002 - registering cron job: %s-$s, cron expr: %s ", group, jobName, cron));
			scheduler.scheduleJob(job, trigger);

		} else {
			logger.info(String.format("sched002 - cron job: %s-$s, cron expr: %s - disabled", group, jobName, cron));
		}
	}

	/**
	 *
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	private String getConfig(String key, String defaultVal) {
		String config = Struct.getMessage(key);

		if (key.equals(config)) {
			config = defaultVal;
		}

		return config;
	}

	/**
	 *
	 */
	public void shutdown() {
		if (scheduler != null) {
			try {
				scheduler.shutdown();
				logger.info("shceduler is shutdown");

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
