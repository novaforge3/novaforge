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
package org.novaforge.forge.tools.requirements.common.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.requirements.common.dao.SchedulerDAO;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerSchedulingException;
import org.novaforge.forge.tools.requirements.common.model.scheduling.CronJobRunnable;
import org.novaforge.forge.tools.requirements.common.model.scheduling.SchedulingConfiguration;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Sequence;
import org.novaforge.forge.tools.requirements.common.services.RequirementCronJobService;
import org.novaforge.forge.tools.requirements.common.services.RequirementManagerSchedulingService;
import org.novaforge.forge.tools.requirements.common.services.RequirementManagerService;
import org.novaforge.forge.tools.requirements.common.services.RequirementSchedulerService;
import org.novaforge.forge.tools.requirements.common.services.RequirementSequenceBuilderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Morin
 */
public class RequirementManagerSchedulerServiceImpl implements RequirementManagerSchedulingService,
    CronJobRunnable
{

  private static final Log log = LogFactory.getLog(RequirementManagerSchedulerServiceImpl.class);
  private SchedulerDAO                      schedulerDAO;
  private RequirementManagerService         requirementManagerService;
  private RequirementSchedulerService       requirementSchedulerService;
  private RequirementSequenceBuilderService requirementSequenceBuilder;
  private RequirementCronJobService         cronJobService;

  public void starting()
  {
    log.info("[RequirementManagerSchedulerServiceImpl] load the global table event from the base");
    try
    {
      loadCronTabEvent();
    }
    catch (Exception e)
    {
      // TODO ok see later the component ?
      e.printStackTrace();
    }
  }

  private void loadCronTabEvent() throws Exception
  {
    Set<SchedulingConfiguration> scheduleConfigurationSet = schedulerDAO.findAllScheduleConfiguration();
    for (SchedulingConfiguration config : scheduleConfigurationSet)
    {
      if (config.isActive())
      {
        log.info("Scheduling config : " + config + " is loaded in RAM");
        cronJobService.addSchedulingConfig(config, this, buildCron(config));
      }
    }
  }

  private String buildCron(final SchedulingConfiguration pConfig)
  {
    return "0 " + pConfig.getLaunchMinute() + " " + pConfig.getLaunchHour() + "/" + pConfig.getLaunchPeriod()
               + " * * ?";
  }

  public void stopping()
  {
    log.info("stop...");
  }

  @Override
  public void run(final SchedulingConfiguration config)
  {
    try
    {
      log.info("Start scheduler  : " + config);
      String projectID = config.getProjectId();
      String userID = config.getUserId();
      if (!isSynchronizationOngoing(projectID))
      {
        /**
         * <pre>
         * By default, with a cron job, we start all known tasks. We'll change it later.
         * In addition, this list should be provided by an other service
         * </pre>
         */
        List<String> taskItemsList = new ArrayList<String>();
        taskItemsList.add("testlink");
        taskItemsList.add("code");
        taskItemsList.add("requirements");
        launchSynchronization(projectID, userID, taskItemsList);
      }
      else
      {
        final String msg =
            "This is already started, this sequence is running, the job will start after next period [" + config
                                                                                                              .getLaunchPeriod()
                + "]";
        log.error(msg);
        /**
         * Launch an exception here ?
         */
        // throw new Exception(msg);
      }
    }
    catch (Exception e)
    {
      log.error("Error occurs when the cron job start : " + e.getMessage(), e);
    }
  }

  @Override
  public boolean isSynchronizationOngoing(final String pProjectId)
      throws RequirementManagerSchedulingException
  {
    try
    {
      /**
       * if a sequence is startable, that mean this one is not in progress
       */
      boolean startable = requirementSchedulerService.sequenceIsStartable(pProjectId);
      return (!startable);
    }
    catch (Exception e)
    {
      throw new RequirementManagerSchedulingException(
          "An error occured during the sequence activity recover", e);
    }
  }

  @Override
  public void launchSynchronization(final String pProjectId, final String pUserId, final List<String> pItems)
      throws RequirementManagerSchedulingException
  {
    try
    {
      log.info("try to execute synchronization " + pProjectId + " " + pItems);
      String codeRepositoryPath = requirementManagerService.findProjectByID(pProjectId)
          .getCodeRepositoryPath();
      Sequence sequence = requirementSequenceBuilder.createNewSequence(pProjectId, codeRepositoryPath,
          pUserId, pItems);
      requirementSchedulerService.sequenceExecuteAsynch(sequence);
    }
    catch (Exception e)
    {
      throw new RequirementManagerSchedulingException(
          "An error occurred when the service tried to recover the scheduling configuration "
              + e.getMessage(), e);
    }
  }

  @Override
  public SchedulingConfiguration getSchedulingConfiguration(final String pProjectId)
      throws RequirementManagerSchedulingException
  {
    try
    {
      SchedulingConfiguration config = schedulerDAO.findScheduleConfigurationByProjectID(pProjectId);
      if (config == null)
      {
        log.info(String.format("no scheduling configuration found for the project=%s", pProjectId));
      }
      return config;
    }
    catch (Exception e)
    {
      throw new RequirementManagerSchedulingException(
          "An error occurred when the service tried to recover the scheduling configuration "
              + e.getMessage(), e);
    }
  }

  @Override
  public void saveSchedulingConfiguration(final SchedulingConfiguration pNewConfiguration)
      throws RequirementManagerSchedulingException
  {
    try
    {
      /**
       * The project handles at most one job configuration
       **/
      String projectID = pNewConfiguration.getProjectId();

      SchedulingConfiguration config = null;

      config = schedulerDAO.findScheduleConfigurationByProjectID(projectID);
      if (config != null)
      {
        config.setActive(pNewConfiguration.isActive());
        config.setLaunchHour(pNewConfiguration.getLaunchHour());
        config.setLaunchMinute(pNewConfiguration.getLaunchMinute());
        config.setLaunchPeriod(pNewConfiguration.getLaunchPeriod());
        config.setProjectId(pNewConfiguration.getProjectId());
        config.setUserId(pNewConfiguration.getUserId());
        schedulerDAO.update(config);
      }
      else
      {
        schedulerDAO.persist(pNewConfiguration);
      }
      updateCronTabEvent(pNewConfiguration);
    }
    catch (Exception e)
    {
      throw new RequirementManagerSchedulingException(
          "Error with the service saveSchedulingConfiguration(SchedulingConfiguration) " + e.getMessage(), e);
    }
  }

  private void updateCronTabEvent(final SchedulingConfiguration pConfig) throws Exception
  {
    if (cronJobService.hasSchedulingConfig(pConfig))
    {
      cronJobService.removeSchedulingConfig(pConfig);
    }
    if (pConfig.isActive())
    {
      cronJobService.addSchedulingConfig(pConfig, this, buildCron(pConfig));
    }
  }

  public void setSchedulerDAO(final SchedulerDAO pSchedulerDAO)
  {
    schedulerDAO = pSchedulerDAO;
  }

  public void setRequirementManagerService(final RequirementManagerService pRequirementManagerService)
  {
    requirementManagerService = pRequirementManagerService;
  }

  public void setRequirementSchedulerService(final RequirementSchedulerService pRequirementSchedulerService)
  {
    requirementSchedulerService = pRequirementSchedulerService;
  }

  public void setRequirementSequenceBuilder(
      final RequirementSequenceBuilderService pRequirementSequenceBuilder)
  {
    requirementSequenceBuilder = pRequirementSequenceBuilder;
  }

  public void setCronJobService(final RequirementCronJobService pCronJobService)
  {
    cronJobService = pCronJobService;
  }

}
