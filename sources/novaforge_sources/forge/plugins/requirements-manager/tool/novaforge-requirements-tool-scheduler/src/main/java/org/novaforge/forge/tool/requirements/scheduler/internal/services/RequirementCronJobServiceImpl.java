/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.tool.requirements.scheduler.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementSchedulerServiceException;
import org.novaforge.forge.tools.requirements.common.model.scheduling.CronJobRunnable;
import org.novaforge.forge.tools.requirements.common.model.scheduling.SchedulingConfiguration;
import org.novaforge.forge.tools.requirements.common.services.RequirementCronJobService;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Map;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class RequirementCronJobServiceImpl implements RequirementCronJobService, Job
{

  private static final Log log = LogFactory.getLog(RequirementCronJobServiceImpl.class);
  private final SchedulerFactory schedulerFactory;
  private final String           GROUPNAME            = "requirement-sch-group";
  private final String           JOB_PREFIX           = "job_";
  private final String           TRIGGER_PREFIX       = "trigger_";
  private final String           CONFIG_KEY           = "CONFIG_KEY";
  private final String           SERVICE_RUNNABLE_KEY = "SERVICE_RUNNABLE_KEY";
  private Scheduler              scheduler;

  public RequirementCronJobServiceImpl()
  {
    schedulerFactory = new StdSchedulerFactory();
    try
    {
      scheduler = schedulerFactory.getScheduler();
      scheduler.start();
    }
    catch (SchedulerException e)
    {
      /**
       * Ok the component can use the scheduler, invalid this one ?
       */
      e.printStackTrace();
    }
  }

  public void starting()
  {
    log.info("[RequirementCronJobServiceImpl] starting ....");
  }

  public void stopping()
  {
    log.info("[RequirementCronJobServiceImpl] stopping ....");
  }

  @Override
  public void addSchedulingConfig(final SchedulingConfiguration pSchedulingConfiguration,
      final CronJobRunnable pCronJobRunnable, final String pCronArguments)
      throws RequirementSchedulerServiceException
  {
    try
    {
      String projectIDkey = pSchedulingConfiguration.getProjectId();

      JobBuilder jobBuilder = newJob(this.getClass());
      JobKey jobkey = jobNameBuilder(projectIDkey);
      jobBuilder.withIdentity(jobkey);
      JobDetail job = jobBuilder.build();

      Map<String, Object> wrappedMap = job.getJobDataMap().getWrappedMap();
      wrappedMap.put(CONFIG_KEY, pSchedulingConfiguration);
      wrappedMap.put(SERVICE_RUNNABLE_KEY, pCronJobRunnable);

      TriggerBuilder<Trigger> triggerBuilder = newTrigger();
      triggerBuilder.withIdentity(triggerNameBuilder(projectIDkey));
      triggerBuilder.withSchedule(cronSchedule(pCronArguments));
      Trigger trigger = triggerBuilder.build();
      scheduler.scheduleJob(job, trigger);
      log.info("A new Job is activate into the pool :" + jobkey);
    }
    catch (Exception e)
    {
      throw new RequirementSchedulerServiceException("An error occurs during the cron job configuration "
          + e.getMessage(), e);
    }
  }

  @Override
  public void removeSchedulingConfig(final SchedulingConfiguration pSchedulingConfiguration)
      throws RequirementSchedulerServiceException
  {
    try
    {
      String projectIDkey = pSchedulingConfiguration.getProjectId();
      JobKey jobKey = jobNameBuilder(projectIDkey);
      boolean deletedJob = scheduler.deleteJob(jobKey);
      if (deletedJob)
      {
        log.info("job identified by  [" + jobKey + "] is removed");
      }
    }
    catch (SchedulerException e)
    {
      throw new RequirementSchedulerServiceException("An error occurs during the cron job removing "
          + e.getMessage(), e);
    }
  }

  @Override
  public boolean hasSchedulingConfig(final SchedulingConfiguration pSchedulingConfiguration)
      throws RequirementSchedulerServiceException
  {
    try
    {
      String projectIDkey = pSchedulingConfiguration.getProjectId();
      JobKey jobKey = jobNameBuilder(projectIDkey);
      return scheduler.checkExists(jobKey);
    }
    catch (SchedulerException e)
    {
      throw new RequirementSchedulerServiceException("An error occurs during scheduler list check "
          + e.getMessage(), e);
    }
  }

  private JobKey jobNameBuilder(final String pKeyJob)
  {
    String jobStringName = JOB_PREFIX + pKeyJob;
    return JobKey.jobKey(jobStringName, GROUPNAME);
  }

  private TriggerKey triggerNameBuilder(final String pKeyTrigger)
  {
    String triggerkeyName = TRIGGER_PREFIX + pKeyTrigger;
    return TriggerKey.triggerKey(triggerkeyName, GROUPNAME);
  }

  @Override
  public void execute(final JobExecutionContext pjobContext) throws JobExecutionException
  {
    JobDetail           jobDetail  = pjobContext.getJobDetail();
    JobDataMap          jobDataMap = jobDetail.getJobDataMap();
    Map<String, Object> wrappedMap = jobDataMap.getWrappedMap();

    SchedulingConfiguration config  = (SchedulingConfiguration) wrappedMap.get(CONFIG_KEY);
    CronJobRunnable         service = (CronJobRunnable) wrappedMap.get(SERVICE_RUNNABLE_KEY);
    service.run(config);
  }

}
