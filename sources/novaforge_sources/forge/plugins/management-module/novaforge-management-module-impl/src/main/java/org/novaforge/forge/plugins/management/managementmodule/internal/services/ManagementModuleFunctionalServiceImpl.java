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
package org.novaforge.forge.plugins.management.managementmodule.internal.services;

import org.novaforge.forge.core.plugins.categories.management.IterationBean;
import org.novaforge.forge.core.plugins.categories.management.ManagementServiceException;
import org.novaforge.forge.core.plugins.categories.management.TaskBean;
import org.novaforge.forge.core.plugins.categories.management.TaskInfoBean;
import org.novaforge.forge.core.plugins.categories.management.TaskStatusEnum;
import org.novaforge.forge.core.plugins.categories.management.TaskTypeEnum;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.plugins.management.managementmodule.services.ManagementModuleFunctionalService;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.business.TaskManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCriteria;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.services.ManagementModuleResourceMapper;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the functional service of the management module, it contains all the methods accessible
 * for other plugins
 */
public class ManagementModuleFunctionalServiceImpl implements ManagementModuleFunctionalService
{

	// injected classes
	private InstanceConfigurationDAO			 instanceConfigurationDAO;

	private TaskManager										taskManager;

	private ManagementModuleManager				managementModuleManager;

	private ProjectPlanManager						 projectPlanManager;

	private IterationManager							 iterationManager;

	private ManagementModuleResourceMapper managementModuleResourceMapper;

	@Override
	public TaskBean getTask(final String login, final String instanceId, final String forgeId,
			final String taskId) throws ManagementServiceException
	{
		try
		{
			final Task task = taskManager.getTask(Long.parseLong(taskId));
			if (task == null)
			{
				throw new ManagementServiceException(String.format("No task exist with [taskId=%s]", taskId));
			}
			// maj of bug information from interplugin
			if (task.getBug() != null)
			{
				final String toolProjectId = getToolProjectId(instanceId);
				taskManager.fillBugWithBugTrackerInformations(task.getBug(), toolProjectId, "");
			}
			final Iteration currentTaskIteration = taskManager.getCurrentIteration(task);
			return managementModuleResourceMapper.buildTaskBeanFromTask(task, currentTaskIteration);
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementServiceException(
					String.format("Unable to get task object with [taskId=%s]", taskId), mmex);
		}
	}

	/**
	 * This method returns the appropriate tool project id using the instanceId
	 *
	 * @param pInstanceId
	 *     the instanceId to use to get the configuration
	 *
	 * @return the tool project id
	 *
	 * @throws ManagementServiceException
	 *     problem during the get
	 */
	private String getToolProjectId(final String pInstanceId) throws ManagementServiceException
	{
		final InstanceConfiguration configuration = instanceConfigurationDAO.findByInstanceId(pInstanceId);
		if (configuration == null)
		{
			throw new ManagementServiceException(String
																							 .format("Unable to find a InstanceConfiguration object with [instance_id=%s]",
																											 pInstanceId));
		}
		return configuration.getToolProjectId();
	}

	@Override
	public Set<TaskInfoBean> getTaskList(final String login, final String instanceId, final String forgeId)
			throws ManagementServiceException
	{
		try{
			final Set<TaskInfoBean> taskInfoBeanSet = new HashSet<TaskInfoBean>();
			Set<IterationBean> iterationSet = getIterationList(login, instanceId, forgeId);
			for ( IterationBean iterationBean : iterationSet)
			{
				taskInfoBeanSet.addAll( getTaskListByIteration(login, instanceId, forgeId, iterationBean.getId()) );
			}
			
			return taskInfoBeanSet ;
			}
		catch (final ManagementServiceException mmex)
		{
			throw new ManagementServiceException(
					String.format("Unable to get task list with [userLogin=%s]", login), mmex);
		}
	}

	@Override
	public Set<TaskInfoBean> getTaskListOfCurrentIteration(final String login, final String instanceId,
			final String forgeId) throws ManagementServiceException
	{
		try
		{
			final TaskCriteria criteria = new TaskCriteria();
			criteria.setUserLogin(login);
			final ProjectPlan projectPlan = getProjectPlanFromInstanceId(instanceId);
			final Iteration currentIteration = iterationManager.getCurrentIteration(projectPlan.getId());
			criteria.setIteration(currentIteration);
			final Set<Task> taskList = taskManager.getTasksByCriteria(criteria);
			final Set<TaskInfoBean> taskInfoBeanSet = new HashSet<TaskInfoBean>();
			for (final Task task : taskList)
			{
				taskInfoBeanSet.add(managementModuleResourceMapper.buildTaskInfoBeanFromTask(task));
			}
			return taskInfoBeanSet;
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementServiceException(String.format(
					"Unable to get task list of current iteration with [userLogin=%s]", login), mmex);
		}
	}

	/**
	 * Get the current project plan using the instance id
	 *
	 * @param pInstanceId
	 *     the instance id
	 *
	 * @return the project plan containing current informations
	 *
	 * @throws ManagementServiceException
	 *     problem getting the project plan
	 */
	private ProjectPlan getProjectPlanFromInstanceId(final String pInstanceId) throws ManagementServiceException
	{
		try
		{
			final Project project = getProjectFromInstanceId(pInstanceId);
			final ProjectPlan projectPlan = projectPlanManager.getLastValidatedProjectPlan(project.getProjectId());
			if (projectPlan == null)
			{
				throw new ManagementServiceException(String
																								 .format("Project configuration is insufficient to use it - [instance_id=%s]",
																												 pInstanceId));
			}
			return projectPlan;
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementServiceException(String.format("Unable to get last project plan with [instance_id=%s]",
																												 pInstanceId), mmex);
		}
	}

	/**
	 * Get the management module project using the instance id
	 *
	 * @param pInstanceId
	 *     the instanceId to use
	 *
	 * @return the project
	 *
	 * @throws ManagementServiceException
	 *     problem getting the project
	 */
	private Project getProjectFromInstanceId(final String pInstanceId) throws ManagementServiceException
	{
		try
		{
			final String toolProjectId = getToolProjectId(pInstanceId);
			final Project project = managementModuleManager.getProject(toolProjectId);
			if (project == null)
			{
				throw new ManagementServiceException(String.format("Unable to find a a Project using [instance_id=%s]",
																													 pInstanceId));
			}
			return project;
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementServiceException(String.format("Unable to get tool project with [instance_id=%s]",
																												 pInstanceId), mmex);
		}
	}

	@Override
	public Set<IterationBean> getIterationList(final String login, final String instanceId, final String forgeId)
			throws ManagementServiceException
	{
		try
		{
			final ProjectPlan projectPlan = getProjectPlanFromInstanceId(instanceId);
			final List<Iteration> iterationList = iterationManager.getFinishedAndCurrentIterationList(projectPlan
					.getId());
			final Set<IterationBean> iterationSetToReturn = new HashSet<IterationBean>();
			for (final Iteration iteration : iterationList)
			{
				iterationSetToReturn.add(managementModuleResourceMapper.buildIterationBeanFromIteration(iteration));
			}
			return iterationSetToReturn;
		}
		catch (ManagementModuleException mmex)
		{
			throw new ManagementServiceException(String.format(
					"Unable to get iteration list for [user=%s] and [instanceId=%s]", login, instanceId), mmex);
		}
	}

	@Override
	public Set<TaskInfoBean> getTaskListByIteration(final String login, final String instanceId,
			final String forgeId, final String iterationId) throws ManagementServiceException
	{
		try
		{
			final TaskCriteria criteria = new TaskCriteria();
			criteria.setUserLogin(login);
			criteria.setIterationId(Long.parseLong(iterationId));
			final Set<Task> taskList = taskManager.getTasksByCriteria(criteria);
			final Set<TaskInfoBean> taskInfoBeanSet = new HashSet<TaskInfoBean>();
			for (final Task task : taskList)
			{
				taskInfoBeanSet.add(managementModuleResourceMapper.buildTaskInfoBeanFromTask(task));
			}
			return taskInfoBeanSet;
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementServiceException(String.format(
					"Unable to get task list of current iteration with [userLogin=%s]", login), mmex);
		}
	}

	@Override
	public Set<TaskInfoBean> getTaskListByType(final String login, final String instanceId,
			final String forgeId, final TaskTypeEnum taskType) throws ManagementServiceException
	{
		try
		{
			final Set<TaskInfoBean> taskInfoBeanSet = new HashSet<TaskInfoBean>();
			Set<IterationBean> iterationSet = getIterationList(login, instanceId, forgeId);
			for ( IterationBean iterationBean : iterationSet)
			{
				final TaskCriteria criteria = new TaskCriteria();
				criteria.setUserLogin(login);
				criteria.setTaskTypeFunctionalId(managementModuleResourceMapper
						.getTaskTypeFunctionalIdFromTaskTypeEnum(taskType));
				criteria.setIterationId(Long.parseLong(iterationBean.getId()));
				final Set<Task> taskList = taskManager.getTasksByCriteria(criteria);

				for (final Task task : taskList)
				{
					taskInfoBeanSet.add(managementModuleResourceMapper.buildTaskInfoBeanFromTask(task));
				}
			}
			return taskInfoBeanSet;
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementServiceException(String.format(
					"Unable to get task list with [userLogin=%s] and [TaskType=%s]", login, taskType), mmex);
		}
	}

	@Override
	public Set<TaskInfoBean> getTaskListByStatus(final String login, final String instanceId,
			final String forgeId, final TaskStatusEnum taskStatus) throws ManagementServiceException
	{
		try
		{
			final Set<TaskInfoBean> taskInfoBeanSet = new HashSet<TaskInfoBean>();
			Set<IterationBean> iterationSet = getIterationList(login, instanceId, forgeId);
			for ( IterationBean iterationBean : iterationSet)
			{
				final TaskCriteria criteria = new TaskCriteria();
				criteria.setUserLogin(login);
				criteria.setTaskStatusFunctionalId(managementModuleResourceMapper
						.getTaskStatusFunctionalIdFromStatusEnum(taskStatus));
				criteria.setIterationId(Long.parseLong(iterationBean.getId()));
	
				final Set<Task> taskList = taskManager.getTasksByCriteria(criteria);
				for (final Task task : taskList)
				{
					taskInfoBeanSet.add(managementModuleResourceMapper.buildTaskInfoBeanFromTask(task));
				}
			}
			return taskInfoBeanSet;
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementServiceException(String.format(
					"Unable to get task list with [userLogin=%s] and [TaskStatus=%s]", login, taskStatus), mmex);
		}
	}

	@Override
	public void modifyTask(final String login, final String instanceId, final String forgeId,
			final TaskBean taskToModify) throws ManagementServiceException
	{
		try
		{
			final Task task = taskManager.getTask(Long.parseLong(taskToModify.getId()));
			if (task == null)
			{
				throw new ManagementServiceException(String.format("No task exist with [taskId=%s]",
						taskToModify.getId()));
			}
			task.setComment(taskToModify.getComment());
			task.setConsumedTime(taskToModify.getConsumedTime());
			task.setDescription(taskToModify.getDescription());
			task.setEndDate(taskToModify.getEndDate());
			task.setLastUpdateDate(new Date());
			task.setName(taskToModify.getTitle());
			task.setRemainingTime(taskToModify.getRemainingTime());
			task.setStartDate(taskToModify.getStartDate());
			final String statusFunctionalId = managementModuleResourceMapper
					.getTaskStatusFunctionalIdFromStatusEnum(taskToModify.getStatus());
			task.setStatus(taskManager.getStatusTask(statusFunctionalId));
			taskManager.updateTask(task);
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementServiceException(String.format("Unable to modify task with [taskId=%s]",
					taskToModify.getId()), mmex);
		}
	}

	@Override
	public void closeTask(final String login, final String instanceId, final String forgeId, final String taskId)
			throws ManagementServiceException
	{
		try
		{
			final Task task = taskManager.getTask(Long.parseLong(taskId));
			if (task == null)
			{
				throw new ManagementServiceException(String.format("No task exist with [taskId=%s]", taskId));
			}
			final StatusTask statusTask = taskManager.getStatusTask(ManagementModuleConstants.TASK_STATUS_DONE);
			task.setRemainingTime(0f);
			task.setStatus(statusTask);
			taskManager.updateTask(task);
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementServiceException(String.format("Unable to modify task with [taskId=%s]", taskId),
					mmex);
		}
	}

	public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
	{
		instanceConfigurationDAO = pInstanceConfigurationDAO;
	}

	public void setTaskManager(final TaskManager pTaskManager)
	{
		taskManager = pTaskManager;
	}

	public void setManagementModuleManager(final ManagementModuleManager pManagementModuleManager)
	{
		managementModuleManager = pManagementModuleManager;
	}

	public void setProjectPlanManager(final ProjectPlanManager pProjectPlanManager)
	{
		projectPlanManager = pProjectPlanManager;
	}

	public void setIterationManager(final IterationManager pIterationManager)
	{
		iterationManager = pIterationManager;
	}

	public void setManagementModuleResourceMapper(
			final ManagementModuleResourceMapper pManagementModuleResourceMapper)
	{
		managementModuleResourceMapper = pManagementModuleResourceMapper;
	}

}
