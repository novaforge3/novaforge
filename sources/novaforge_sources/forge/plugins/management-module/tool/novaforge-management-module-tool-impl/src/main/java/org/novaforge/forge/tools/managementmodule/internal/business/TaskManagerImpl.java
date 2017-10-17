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
package org.novaforge.forge.tools.managementmodule.internal.business;

import org.novaforge.forge.plugins.management.managementmodule.services.ManagementModulePluginService;
import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.TaskManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.dao.BugDAO;
import org.novaforge.forge.tools.managementmodule.dao.IterationDAO;
import org.novaforge.forge.tools.managementmodule.dao.IterationTaskDAO;
import org.novaforge.forge.tools.managementmodule.dao.StatusTaskDAO;
import org.novaforge.forge.tools.managementmodule.dao.TaskCategoryDAO;
import org.novaforge.forge.tools.managementmodule.dao.TaskDAO;
import org.novaforge.forge.tools.managementmodule.dao.TaskTypeDAO;
import org.novaforge.forge.tools.managementmodule.domain.Bug;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;
import org.novaforge.forge.tools.managementmodule.domain.TaskCriteria;
import org.novaforge.forge.tools.managementmodule.domain.TaskType;
import org.novaforge.forge.tools.managementmodule.exceptions.ExceptionCode;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaskManagerImpl implements TaskManager
{

	private TaskDAO											 taskDAO;

	private TaskCategoryDAO							 taskCategoryDAO;

	private TaskTypeDAO									 taskTypeDAO;

	private StatusTaskDAO								 statusTaskDAO;

	private IterationDAO									iterationDAO;

	private IterationTaskDAO							iterationTaskDAO;

	private BugDAO												bugDAO;

	private IterationManager							iterationManager;

	private ManagementModulePluginService managementModulePluginService;

	private BusinessObjectFactory				 businessObjectFactory;

	public void setTaskDAO(final TaskDAO pTaskDAO)
	{
		taskDAO = pTaskDAO;
	}

	public void setTaskCategoryDAO(final TaskCategoryDAO pTaskCategoryDAO)
	{
		taskCategoryDAO = pTaskCategoryDAO;
	}

	/**
	 * ********************** IterationTask *******************************************************************
	 */

	@Override
	public IterationTask newIterationTask()
	{
		return businessObjectFactory.getInstanceIterationTask();
	}

	public void setTaskTypeDAO(final TaskTypeDAO pTaskTypeDAO)
	{
		taskTypeDAO = pTaskTypeDAO;
	}

	public void setStatusTaskDAO(final StatusTaskDAO pStatusTaskDAO)
	{
		statusTaskDAO = pStatusTaskDAO;
	}

	@Override
	public Set<IterationTask> getFullIterationTasksList(final Iteration iterationSrc)
			throws ManagementModuleException
	{
		try
		{
			Iteration iteration = getIterationWithFullTaskList(iterationSrc.getId());
			return iteration.getIterationTasks();
		}
		catch (ManagementModuleException e)
		{
			throw new ManagementModuleException(String.format("Problem while getting the task list with [iterationSrc=%s]",
																												iterationSrc), e);
		}
	}

	public void setIterationDAO(final IterationDAO pIterationDAO)
	{
		iterationDAO = pIterationDAO;
	}

	public void setIterationTaskDAO(final IterationTaskDAO pIterationTaskDAO)
	{
		iterationTaskDAO = pIterationTaskDAO;
	}

	@Override
	public Iteration getIterationWithFullTaskList(final long iterationId) throws ManagementModuleException
	{
		final Iteration iteration = iterationDAO.findById(iterationId);
		for (final IterationTask iterationTask : iteration.getIterationTasks())
		{
			initializeBusinessFieldsOnTask(iterationTask.getTask());
		}
		return iteration;
	}

	public void setBugDAO(final BugDAO pBugDAO)
	{
		bugDAO = pBugDAO;
	}

	public void setIterationManager(final IterationManager pIterationManager)
	{
		iterationManager = pIterationManager;
	}

	@Override
	public void deleteIterationTaskByProjectPlanId(final Long projectPlanId) throws ManagementModuleException
	{
		iterationTaskDAO.deleteByProjectPlanId(projectPlanId);
	}

	public void setManagementModulePluginService(final ManagementModulePluginService pManagementModulePluginService)
	{
		managementModulePluginService = pManagementModulePluginService;
	}

	public void setBusinessObjectFactory(final BusinessObjectFactory pBusinessObjectFactory)
	{
		businessObjectFactory = pBusinessObjectFactory;
	}

	/**
	 * ********************** Task *******************************************************************
	 */

	@Override
	public Task newTask()
	{
		return businessObjectFactory.getInstanceTask();
	}



	@Override
	public Task getTask(final long taskId) throws ManagementModuleException
	{
		final Task task = taskDAO.findById(taskId);
		initializeBusinessFieldsOnTask(task);
		return task;
	}



	@Override
	public Task persistTask(final Task task) throws ManagementModuleException
	{
		beforeTaskSave(task);
		final Task persistedTask = taskDAO.save(task);
		initializeBusinessFieldsOnTask(persistedTask);
		return persistedTask;
	}



	@Override
	public Task updateTask(final Task task) throws ManagementModuleException
	{
		beforeTaskSave(task);
		final Task persistedTask = taskDAO.merge(task);
		initializeBusinessFieldsOnTask(persistedTask);
		return persistedTask;
	}

	/**
 * Operations to do before task save
 *
 * @param task
 *          the task to control/modify
 * @throws ManagementModuleException
 *           problem during operations
 */
	private void beforeTaskSave(final Task task) throws ManagementModuleException
	{
		if (task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_DONE)
				&& task.getEndDate() == null)
		{
			task.setEndDate(new Date());
		}
		if (task.getEndDate() != null && task.getStartDate() != null
				&& task.getEndDate().before(task.getStartDate()))
		{
			task.getStartDate().setTime(task.getEndDate().getTime());
		}
		if (task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_NOT_AFFECTED)
				&& task.getUser() != null)
		{
			if (task.getConsumedTime() == 0F)
			{
				task.setStatus(getStatusTask(ManagementModuleConstants.TASK_STATUS_NOT_STARTED));
			}
			else
			{
				task.setStatus(getStatusTask(ManagementModuleConstants.TASK_STATUS_IN_PROGRESS));
			}
		}
		if (task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_NOT_STARTED)
				&& task.getConsumedTime() != 0F)
		{
			task.setStatus(getStatusTask(ManagementModuleConstants.TASK_STATUS_IN_PROGRESS));
		}
		task.setLastUpdateDate(new Date());
		manageCurrentIterationTask(task);
	}



	@Override
	public void deleteTask(final long taskId) throws ManagementModuleException
	{
		taskDAO.delete(taskDAO.findById(taskId));
	}

	@Override
	public void deleteTasksByProjectPlanId(final Long projectPlanId) throws ManagementModuleException
	{
		taskDAO.deleteByProjectPlanId(projectPlanId);
	}

	@Override
	public Set<Task> findByProjectPlanIdAndDisciplineId(final Long projectPlanId, final String disciplineId)
			throws ManagementModuleException
	{
		return taskDAO.findByProjectPlanIdAndDisciplineId(projectPlanId, disciplineId);
	}

	@Override
	public List<Task> findByScopeUnitId(final Long scopeUnitId) throws ManagementModuleException
	{
		return taskDAO.findByScopeUnitId(scopeUnitId);
	}
	

  @Override
  public List<Task> findByTaskCategoryId(final Long taskCategoryId) throws ManagementModuleException
  {
    return taskDAO.findByTaskCategoryId(taskCategoryId);
  }

	@Override
	public List<Task> findByScopeUnitIdAndDisciplineId(final Long scopeUnitId, final Long disciplineId)
			throws ManagementModuleException
	{
		return taskDAO.findByScopeUnitIdAndDisciplineId(scopeUnitId, disciplineId);
	}

	@Override
	public Set<Task> getIterationTaskListMergedWithIterationTaskAndDisciplineFunctionalId(
			final long iterationId, final String disciplineId) throws ManagementModuleException
	{
		final Set<Task> tasks = new HashSet<Task>();
		final Iteration iteration = iterationDAO.findById(iterationId);
		for (final IterationTask iterationTask : iteration.getIterationTasks())
		{
			Task task = iterationTask.getTask();

			// Filter on disciplineId
			if (disciplineId == null || disciplineId.isEmpty()
					|| task.getDiscipline().getFunctionalId().equals(disciplineId))
			{
				// task.setLastUpdateDate(iterationTask.getLastUpdateDate());
				task.setStatus(iterationTask.getStatus());
				tasks.add(task);
			}
		}

		return tasks;
	}

	/************************* TaskCategory ********************************************************************/

	@Override
	public TaskCategory newTaskCategory()
	{
		return businessObjectFactory.getInstanceTaskCategory();
	}

	@Override
	public TaskCategory creeteTaskCategory(final TaskCategory taskCategory) throws ManagementModuleException
	{
		return taskCategoryDAO.save(taskCategory);
	}

	@Override
	public TaskCategory updateTaskCategory(final TaskCategory taskCategory) throws ManagementModuleException
	{
		return this.taskCategoryDAO.merge(taskCategory);
	}

	@Override
	public void deleteTaskCategory(final TaskCategory taskCategory) throws ManagementModuleException
	{
		taskCategoryDAO.delete(taskCategory);
	}

	@Override
	public TaskCategory getTaskCategory(final long id) throws ManagementModuleException
	{
		return taskCategoryDAO.findById(id);
	}

	/************************* StatusTask ********************************************************************/

	@Override
	public StatusTask createStatusTask(final StatusTask st) throws ManagementModuleException
	{
		return statusTaskDAO.save(st);
	}

	@Override
	public boolean deleteStatusTask(final String name) throws ManagementModuleException
	{
		StatusTask st = statusTaskDAO.findByFunctionalId(name);
		statusTaskDAO.delete(st);
		return true;
	}

	@Override
	public StatusTask getStatusTask(final String name) throws ManagementModuleException
	{
		return statusTaskDAO.findByFunctionalId(name);
	}

	@Override
	public StatusTask newStatusTask()
	{
		return businessObjectFactory.getInstanceStatusTask();
	}

	@Override
	public boolean updateStatusTask(final StatusTask st) throws ManagementModuleException
	{
		statusTaskDAO.merge(st);
		return true;
	}

	@Override
	public List<StatusTask> getAllStatusTask() throws ManagementModuleException
	{
		final List<StatusTask> statusTaskList = statusTaskDAO.findAll();
		final Map<String, StatusTask> map = new HashMap<String, StatusTask>();
		for (final StatusTask statusTask : statusTaskList)
		{
			map.put(statusTask.getFunctionalId(), statusTask);
		}
		final List<StatusTask> taskStatusListToReturn = new ArrayList<StatusTask>();
		taskStatusListToReturn.add(map.get(ManagementModuleConstants.TASK_STATUS_NOT_AFFECTED));
		taskStatusListToReturn.add(map.get(ManagementModuleConstants.TASK_STATUS_NOT_STARTED));
		taskStatusListToReturn.add(map.get(ManagementModuleConstants.TASK_STATUS_IN_PROGRESS));
		taskStatusListToReturn.add(map.get(ManagementModuleConstants.TASK_STATUS_DONE));
		taskStatusListToReturn.add(map.get(ManagementModuleConstants.TASK_STATUS_CANCELED));
		return taskStatusListToReturn;
	}

	@Override
	public StatusTask getDefaultTaskStatus() throws ManagementModuleException
	{
		return statusTaskDAO.findByFunctionalId(ManagementModuleConstants.TASK_STATUS_NOT_AFFECTED);
	}

	// The TaskType methods

	@Override
	public TaskType newTaskType()
	{
		return businessObjectFactory.getInstanceTaskType();
	}

	@Override
	public TaskType createTaskType(final TaskType taskType) throws ManagementModuleException
	{
		return taskTypeDAO.save(taskType);
	}

	@Override
	public TaskType updateTaskType(final TaskType taskType) throws ManagementModuleException
	{
		return taskTypeDAO.merge(taskType);
	}

	@Override
	public TaskType getTaskType(final String functionalId) throws ManagementModuleException
	{
		return taskTypeDAO.findByFunctionalId(functionalId);
	}

	@Override
	public void deleteTaskType(final String functionalId) throws ManagementModuleException
	{
		TaskType taskType = taskTypeDAO.findByFunctionalId(functionalId);
		taskTypeDAO.delete(taskType);
	}

	/**
	 * Get the newest iterationTask of a task
	 * 
	 * @return the newest iterationtask
	 */
	private IterationTask getNewestIterationTask(final Task task)
	{
		IterationTask iterationTaskToReturn = null;
		for (final IterationTask currentIterationTask : task.getIterationsTasks())
		{
			if (iterationTaskToReturn == null
					|| currentIterationTask.getIteration().getStartDate()
							.after(iterationTaskToReturn.getIteration().getStartDate()))
			{
				iterationTaskToReturn = currentIterationTask;
			}
		}
		return iterationTaskToReturn;
	}

	@Override
	public Iteration getCurrentIteration(final Task task)
	{
		for (final IterationTask iterationTask : task.getIterationsTasks())
		{
			// only one at a time can be open at a time
			if (!iterationTask.getIteration().isFinished())
			{
				return iterationTask.getIteration();
			}
		}
		// if there is no open iteration we return the newest
		IterationTask iterationTask = getNewestIterationTask(task);
		return iterationTask != null  ? iterationTask.getIteration() : null;
	}

	/**
	 * This methods initialize the transient consumed and remaining times using
	 * its IterationTask
	 * 
	 * @param task
	 *          the task
	 */
	@Override
	public Task initializeBusinessFieldsOnTask(final Task task)
	{
		// initialization of consumed time
		float consumedTime = 0;
		for (final IterationTask iterationTask : task.getIterationsTasks())
		{
			consumedTime += iterationTask.getConsumedTime();
		}
		task.setConsumedTime(consumedTime);
		if (getNewestIterationTask(task) != null)
		{
		  task.setRemainingTime(getNewestIterationTask(task).getRemainingTime());
		}
		else
		{
		  task.setRemainingTime(0);
		}
    if (getCurrentIteration(task) != null)
    {
      task.setCurrentIteration(getCurrentIteration(task));  
    }
		return task;
	}

	/**
	 * This methods fill the current IterationTask of the task (if exists) using
	 * transient task fields
	 * 
	 * @param task
	 *          the task
	 */
	private void manageCurrentIterationTask(final Task task)
	{
		// dispatching of consumed time
		// time can't be changed on terminated iterations - we sum the time set
		// to dispatch the difference on open iteration
		float terminatedTime = 0;
		IterationTask nonTerminatedIterationTask = null;
		for (final IterationTask iterationTask : task.getIterationsTasks())
		{
			if (iterationTask.getIteration().isFinished())
			{
				terminatedTime += iterationTask.getConsumedTime();
			}
			else
			{
				nonTerminatedIterationTask = iterationTask;
			}
		}
		// if there is no open iteration for task there is nothing to dispatch
		if (nonTerminatedIterationTask == null)
		{
			return;
		}
		// the consumed time set on the current iteration task is the difference
		// between new consumed time and terminated times
		final float consumedTimeOnLastIteration = task.getConsumedTime() - terminatedTime;
		// can't be negative cause time set can't be smaller than registered
		// consumed time on closed iteration
		nonTerminatedIterationTask.setConsumedTime(Math.max(consumedTimeOnLastIteration, 0));
		// dispatching of remaining time
		if (task.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_CANCELED))
		{
			nonTerminatedIterationTask.setRemainingTime(0F);
		}
		else
		{
			nonTerminatedIterationTask.setRemainingTime(task.getRemainingTime());
		}
		nonTerminatedIterationTask.setLastUpdateDate(new Date());
		nonTerminatedIterationTask.setStatus(task.getStatus());
	}

	@Override
	public Iteration closeIteration(final long iterationId) throws ManagementModuleException
	{
		try
		{
			final Iteration iteration = getIterationWithFullTaskList(iterationId);
			// the iteration must no be finished
			if (iteration.isFinished())
			{
				throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_CLOSE_IS_FINISHED);
			}
			// the iteration must be the oldest non finished
			for (final Lot lot : iteration.getLot().getProjectPlan().getLots())
			{
				for (final Iteration tmpIteration : lot.getIterations())
				{
					if (!tmpIteration.isFinished() && tmpIteration.getStartDate().before(iteration.getStartDate()))
					{
						throw new ManagementModuleException(ExceptionCode.ERR_ITERATION_CLOSE_IS_NOT_OLDEST);
					}
				}
			}
			// we set the iteration to finished
			iteration.setFinished(true);
			iterationDAO.merge(iteration);
			final Set<IterationTask> nonFinishedTaskList = new HashSet<IterationTask>();
			// we get the non finished tasks
			for (final IterationTask iterationTask : iteration.getIterationTasks())
			{
				if (!iterationTask.getStatus().getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_DONE)
						&& !iterationTask.getStatus().getFunctionalId()
								.equals(ManagementModuleConstants.TASK_STATUS_CANCELED))
				{
					nonFinishedTaskList.add(iterationTask);
				}
			}
			// we get the next iteration
			Iteration nextIteration = null;
			final Long projectPlanId = iteration.getLot().getProjectPlan().getId();
			// first we look if there is another in "present"
			final Iteration currentIteration = iterationManager.getCurrentIteration(projectPlanId);
			if (currentIteration != null)
			{
				nextIteration = currentIteration;
			}
			// otherwise its the first in future
			else
			{
				nextIteration = iterationManager.getNextIteration(projectPlanId);
			}
			// if there is remaining tasks and no iteration in future -> error,
			// we cant put the remaining tasks on anything
			if (nextIteration == null && !nonFinishedTaskList.isEmpty())
			{
				throw new ManagementModuleException(
						ExceptionCode.ERR_ITERATION_CLOSE_REMAINING_TASKS_AND_NO_MORE_ITERATION);
			}
			// for the non terminated tasks we create a new IterationTask with
			// the next iteration
			for (final IterationTask oldIterationTask : nonFinishedTaskList)
			{
				final IterationTask iterationTask = newIterationTask();
				iterationTask.setTask(oldIterationTask.getTask());
				iterationTask.setIteration(nextIteration);
				iterationTask.setRemainingTime(oldIterationTask.getRemainingTime());
				iterationTask.setStatus(oldIterationTask.getStatus());
				iterationTask.setConsumedTime(0);
				iterationTask.setLastUpdateDate(new Date());
				iterationTask.getIteration().getIterationTasks().add(iterationTask);
			}
			// we save the iterations tasks using iteration cascading
			if (nextIteration != null && !nonFinishedTaskList.isEmpty())
			{
				iterationDAO.merge(nextIteration);
			}
			return iteration;
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementModuleException(mmex.getCode(), "Problem while closing Iteration with id="
					+ iterationId, mmex);
		}
	}

	@Override
	public Set<Task> getTasksByCriteria(final TaskCriteria taskCriteria) throws ManagementModuleException
	{
		return taskDAO.getTasksByCriteria(taskCriteria);
	}

	/************************* Bug ********************************************************************/

	@Override
	public void fillBugWithBugTrackerInformations(final Bug bug, final String toolProjectId, final String login)
			throws ManagementModuleException
	{
		try
		{
			managementModulePluginService.fillBugWithBugTrackerInformations(toolProjectId, login, bug);
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementModuleException(mmex.getCode(), "Unable to get additional info for bug : "
					+ bug.getId(), mmex);
		}

	}

	@Override
	public List<Bug> getAllBugsFromBugTracker(final String toolProjectId, final String login)
			throws ManagementModuleException
	{
		try
		{
			return managementModulePluginService.getAllIssues(toolProjectId, login);
		}
		catch (final ManagementModuleException mmex)
		{
			throw new ManagementModuleException(mmex.getCode(), "Unable to get bugs from bug tracker", mmex);
		}
	}

	@Override
	public Bug getBug(final String bugTrackerId) throws ManagementModuleException
	{
		return bugDAO.getBugByBugTrackerId(bugTrackerId);
	}

	@Override
	public Bug persistBug(final Bug bug) throws ManagementModuleException
	{
		return bugDAO.save(bug);
	}

	@Override
	public Bug updateBug(final Bug bug) throws ManagementModuleException
	{
		return bugDAO.merge(bug);
	}

	@Override
	public Bug newBug()
	{
		return businessObjectFactory.getInstanceBug();
	}

}
