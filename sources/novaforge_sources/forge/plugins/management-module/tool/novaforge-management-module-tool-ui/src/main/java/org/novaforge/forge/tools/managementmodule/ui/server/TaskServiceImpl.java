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
package org.novaforge.forge.tools.managementmodule.ui.server;

import org.novaforge.forge.tools.managementmodule.domain.Bug;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDisciplineStatus;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCriteria;
import org.novaforge.forge.tools.managementmodule.domain.transfer.GlobalMonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.domain.transfer.MonitoringIndicators;
import org.novaforge.forge.tools.managementmodule.ui.client.service.TaskService;
import org.novaforge.forge.tools.managementmodule.ui.shared.BugDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.GlobalMonitoringIndicatorsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationTaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MonitoringIndicatorsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskStatusDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The implementation of the task service
 */
public class TaskServiceImpl extends SimpleServiceImpl implements TaskService
{

	/** Uid for serialization */
	private static final long	 serialVersionUID	 = 7566337808803137756L;

	private static final String CSV_LINE_SEPARATOR = System.getProperty("line.separator");

	@Override
	public Set<TaskStatusDTO> getAllTaskStatus()
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		try
		{
			final Set<TaskStatusDTO> taskStatusDTOSet = new LinkedHashSet<TaskStatusDTO>();
			final StatusTask defaultStatusTask = getTaskManager().getDefaultTaskStatus();
			for (StatusTask statusTask : getTaskManager().getAllStatusTask())
			{
				boolean isDefaultStatusTask = statusTask.getFunctionalId()
						.equals(defaultStatusTask.getFunctionalId());
				taskStatusDTOSet.add(BuildResources.buildTaskStatusDTO(statusTask, isDefaultStatusTask));
			}
			return taskStatusDTOSet;
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException ex)
		{
			throw manageException("Unable to get TaskStatus List", ex);
		}
	}

	@Override
	public TaskDTO createTask(final TaskDTO taskDTOToCreate)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		try
		{
			final Task task = buildTaskFromTaskDTO(taskDTOToCreate);
			final Task persistedTask = getTaskManager().persistTask(task);
			return BuildResources.buildTaskDTOFromTask(persistedTask);
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException ex)
		{
			throw manageException("Unable to create the task : " + taskDTOToCreate.getLabel(), ex);
		}
	}

	@Override
	public TaskDTO modifyTask(final TaskDTO taskDTOToModify)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		try
		{
			final Task task = buildTaskFromTaskDTO(taskDTOToModify);
			final Task persistedTask = getTaskManager().updateTask(task);
			return BuildResources.buildTaskDTOFromTask(persistedTask);
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException ex)
		{
			throw manageException("Unable to modify the task : " + taskDTOToModify.getLabel(), ex);
		}
	}

	@Override
	public Boolean deleteTask(final Long taskId)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		try
		{
			getTaskManager().deleteTask(taskId);
			return true;
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to delete task with taskId=" + taskId, e);
		}
	}

	@Override
	public Boolean isAllowIterationPreparation(final Long currentValidatedProjectPlanId)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		Boolean ret = false;
		try
		{
			List<Iteration> list = getIterationManager().getCurrentIterationList(currentValidatedProjectPlanId);
			if (list.size() <= 1)
			{
				ret = true;
			}
		}
		catch (Exception e)
		{
			throw manageException("Unable to get finished and current iteration list", e);
		}

		return ret;
	}

	@Override
	public Set<ScopeUnitDisciplineDTO> getScopeUnitDiscipline(final Long projectPlanId)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		Set<ScopeUnitDisciplineDTO> ret = new HashSet<ScopeUnitDisciplineDTO>();
		try
		{
			Set<ScopeUnitDiscipline> list = getProjectPlanManager().getScopeUnitDisciplines(projectPlanId);
			if (list != null && list.size() > 0)
			{
				for (ScopeUnitDiscipline scopeUnitDiscipline : list)
				{
					ret.add(BuildResources.buildScopeUnitDisciplineDTOFromScopeUnitDiscipline(scopeUnitDiscipline));
				}
			}
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to get finished and current iteration list", e);
		}
		return ret;
	}

	@Override
	public List<ScopeUnitDisciplineDTO> createScopeUnitDiscipline(
			final List<ScopeUnitDisciplineDTO> scopeUnitDisciplines)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		List<ScopeUnitDisciplineDTO> ret = new ArrayList<ScopeUnitDisciplineDTO>();
		try
		{
			for (ScopeUnitDisciplineDTO scopeUnitDisciplineDTO : scopeUnitDisciplines)
			{
				final ScopeUnit scopeUnit = getProjectPlanManager().getScopeUnit(
						scopeUnitDisciplineDTO.getScopeUnit().getUnitId());
				final Discipline discipline = getReferentielManager().getDiscipline(
						scopeUnitDisciplineDTO.getDiscipline().getFunctionalId());
				final ScopeUnitDisciplineStatus scopeUnitDisciplineStatus = getProjectPlanManager()
						.getScopeUnitDisciplineStatus(scopeUnitDisciplineDTO.getStatus().getFunctionalId());
				final ScopeUnitDiscipline scopeUnitDiscipline = getProjectPlanManager().newScopeUnitDiscipline();
				scopeUnitDiscipline.setDiscipline(discipline);
				scopeUnitDiscipline.setScopeUnit(scopeUnit);
				scopeUnitDiscipline.setStatus(scopeUnitDisciplineStatus);
				scopeUnitDiscipline.setStatusUpdatedDate(new Date());
				ScopeUnitDiscipline scopeUnitDisciplinePersisted = getProjectPlanManager().creeteScopeUnitDiscipline(
						scopeUnitDiscipline);
				ret.add(BuildResources
						.buildScopeUnitDisciplineDTOFromScopeUnitDiscipline(scopeUnitDisciplinePersisted));
			}
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to create scopeUnitDiscipline", e);
		}
		return ret;

	}

	@Override
	public boolean deleteScopeUnitDiscipline(final String scopeUnitId, final String functionalId)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		boolean ret = false;
		try
		{
			ret = getProjectPlanManager().deleteScopeUnitDiscipline(scopeUnitId, functionalId);
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to delete scopeUnitDiscipline", e);
		}
		return ret;
	}

	@Override
	public boolean terminateScopeUnitDiscipline(final String scopeUnitId, final String functionalId)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		boolean ret = false;
		try
		{
			ret = getProjectPlanManager().terminateScopeUnitDiscipline(scopeUnitId, functionalId);
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to delete scopeUnitDiscipline", e);
		}
		return ret;
	}

	@Override
	public Integer getCategoriesCountForAnIterationAndADiscipline(final long iterationId,
			final String disciplineFunctionalId)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{

		final Set<Long> taskCategoryIds = new HashSet<Long>();

		try
		{
			final TaskCriteria criteria = new TaskCriteria();
			if (!disciplineFunctionalId.equalsIgnoreCase(Constants.ALL))
			{
				criteria.setDisciplineFunctionalId(disciplineFunctionalId);
			}
			criteria.setIterationId(iterationId);
			final Set<Task> taskList = getTaskManager().getTasksByCriteria(criteria);
			for (Task task : taskList)
			{
				if (task.getTaskCategory() != null)
				{
					taskCategoryIds.add(task.getTaskCategory().getId());
				}
			}

		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException mmex)
		{
			throw manageException(String.format("Unable to get task categories count for iteration with [iterationId=%s]",
																					iterationId), mmex);
		}

		return Integer.valueOf(taskCategoryIds.size());
	}

	@Override
	public GlobalMonitoringIndicatorsDTO getGlobalMonitoringInformations(final Long projectPlanId)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		try
		{
			final GlobalMonitoringIndicators indicators = getIndicatorsManager().getGlobalMonitoringIndicators(projectPlanId);
			return BuildResources.buildGlobalMonitoringIndicatorsDTOFromGlobalMonitoringIndicators(indicators);
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException mmex)
		{
			throw manageException("Unable to get global monitoring informations with projectPlanId=" + projectPlanId, mmex);
		}
	}

	@Override
	public Set<BugDTO> getBugList(final String projectId, final String userLogin)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		try
		{
			final List<Bug> bugList = getTaskManager().getAllBugsFromBugTracker(projectId, userLogin);
			final Set<BugDTO> bugSet = new HashSet<BugDTO>();
			for (final Bug bug : bugList)
			{
				bugSet.add(BuildResources.buildBugDTOFromBug(bug));
			}
			return bugSet;
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to get bug list", e);
		}
	}

	@Override
	public BugDTO getBugDetail(final String bugTrackerId, final String projectId, final String userLogin)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		try
		{
			Bug bug = getTaskManager().getBug(bugTrackerId);
			// if it does not exist we instantiate a new Bug
			if (bug == null)
			{
				bug = getTaskManager().newBug();
				bug.setBugTrackerId(bugTrackerId);
			}
			getTaskManager().fillBugWithBugTrackerInformations(bug, projectId, "");
			return BuildResources.buildBugDTOFromBug(bug);
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
		{
			throw manageException("Unable to getBugDetail for bugTrackerId:" + bugTrackerId, e);
		}
	}

	@Override
	public IterationDTO closeIteration(final long iterationId)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		try
		{
			final Iteration iteration = getTaskManager().closeIteration(iterationId);
			iteration.setIterationTasks(getTaskManager().getFullIterationTasksList(iteration));
			return getComplexIterationDTOFromIteration(iteration);
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException mmex)
		{
			throw manageException(String.format("Unable to close the iteration with id=%s", iterationId), mmex);
		}
	}

	@Override
	public MonitoringIndicatorsDTO getMonitoringIndicators(final long iterationId,
			final String disciplineFunctionalId)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		try
		{
			final MonitoringIndicators monitoringIndicator = getIndicatorsManager()
					.getIterationMonitoringIndicators(iterationId, disciplineFunctionalId);
			return BuildResources.buildMonitoringIndicatorsDTOFromMonitoringIndicators(monitoringIndicator);
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException mmex)
		{
			throw manageException(
					"Unable to get iteration monitoring informations with iterationId=" + iterationId, mmex);
		}
	}

	@Override
	public Boolean createCSVFormatWithIterationTaskDTOList(final List<IterationTaskDTO> list)
	{
		final StringBuilder sb = new StringBuilder();
		for (IterationTaskDTO iterationTask : list)
		{
			final TaskDTO task = iterationTask.getTask();
			sb.append(task.getLabel()).append(Constants.CSV_COLUMN_SEPARATOR);
			if (task.getScopeUnit() != null)
			{
				sb.append(task.getScopeUnit().getName()).append(Constants.CSV_COLUMN_SEPARATOR);
				sb.append(task.getParentScopeUnitName()).append(Constants.CSV_COLUMN_SEPARATOR);
			}
			else
			{
				sb.append(Constants.CSV_COLUMN_SEPARATOR).append(Constants.CSV_COLUMN_SEPARATOR);
			}
			sb.append(task.getDiscipline().getLibelle()).append(Constants.CSV_COLUMN_SEPARATOR);
			if (task.getUser() != null)
			{
				sb.append(task.getUser().getFirstName()).append(" ").append(task.getUser().getLastName());
			}
			else
			{
				sb.append(Constants.CSV_EMPTY);
			}
			sb.append(Constants.CSV_COLUMN_SEPARATOR).append(task.getInitialEstimation())
					.append(Constants.CSV_COLUMN_SEPARATOR);
			if (task.getStartDate() != null)
			{
				sb.append(task.getStartDate());
			}
			else
			{
				sb.append(Constants.CSV_EMPTY);
			}
			sb.append(Constants.CSV_COLUMN_SEPARATOR);
			if (task.getStartDate() != null)
			{
				sb.append(task.getEndDate());
			}
			else
			{
				sb.append(Constants.CSV_EMPTY);
			}
			sb.append(Constants.CSV_COLUMN_SEPARATOR);
			if (task.getCategory() != null)
			{
				sb.append(task.getCategory().getName());
			}
			else
			{
				sb.append(Constants.CSV_EMPTY);
			}
			sb.append(Constants.CSV_COLUMN_SEPARATOR).append(task.getStatus().getLabel())
					.append(Constants.CSV_COLUMN_SEPARATOR).append(iterationTask.getReestimate())
					.append(Constants.CSV_COLUMN_SEPARATOR).append(iterationTask.getConsumedTime())
					.append(Constants.CSV_COLUMN_SEPARATOR).append(iterationTask.getRemainingTime())
					.append(Constants.CSV_COLUMN_SEPARATOR).append(iterationTask.getAdvancement())
					.append(Constants.CSV_COLUMN_SEPARATOR).append(iterationTask.getErrorEstimation())
					.append(CSV_LINE_SEPARATOR);
		}
		getSession().setAttribute(Constants.SESSION_EXPORT_CSV_PARAMS, sb.toString());
		return true;
	}

	/**
	 * This method builds and returns a domain server Task from a taskDTO
	 *
	 * @param taskDTO
	 *     the dto to use to build the task
	 *
	 * @return the builded task
	 *
	 * @throws ManagementModuleException
	 *     problem during build
	 */
	private Task buildTaskFromTaskDTO(final TaskDTO taskDTO)
			throws org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException
	{
		try
		{
			Task taskToReturn;
			if (taskDTO.getId() != null)
			{
				taskToReturn = getTaskManager().getTask(taskDTO.getId());
			}
			else
			{
				taskToReturn = getTaskManager().newTask();
			}
			taskToReturn.setName(taskDTO.getLabel());
			final String typeFunctionalId = ClientToServerMapper.getTaskTypeServerValueFromEnumValue(taskDTO.getType());
			taskToReturn.setTaskType(getTaskManager().getTaskType(typeFunctionalId));
			taskToReturn.setDiscipline(getReferentielManager().getDiscipline(taskDTO.getDiscipline().getFunctionalId()));
			taskToReturn.setInitialEstimation(taskDTO.getInitialEstimation());
			taskToReturn.setStatus(getTaskManager().getStatusTask(taskDTO.getStatus().getFunctionalId()));
			taskToReturn.setStartDate(taskDTO.getStartDate());
			taskToReturn.setEndDate(taskDTO.getEndDate());
			if (taskDTO.getCategory() != null)
			{
				taskToReturn.setTaskCategory(getTaskManager().getTaskCategory(taskDTO.getCategory().getId()));
			}
			else
			{
				taskToReturn.setTaskCategory(null);
			}
			if (taskDTO.getScopeUnit() != null)
			{
				taskToReturn.setScopeUnit(getProjectPlanManager().getScopeUnit(taskDTO.getScopeUnit().getUnitId()));
			}
			else
			{
				taskToReturn.setScopeUnit(null);
			}
			taskToReturn.setRemainingTime(taskDTO.getRemainingTime());
			taskToReturn.setConsumedTime(taskDTO.getConsumedTime());
			taskToReturn.setComment(taskDTO.getCommentary());
			taskToReturn.setDescription(taskDTO.getDescription());
			if (taskDTO.getUser() != null)
			{
				taskToReturn.setUser(getManagementModuleManager().getUser(taskDTO.getUser().getLogin()));
			}
			else
			{
				taskToReturn.setUser(null);
			}
			if (taskDTO.getBug() != null)
			{
				// we look if this bug exist in database
				Bug bug = getTaskManager().getBug(taskDTO.getBug().getBugTrackerId());
				// if it does not exist we create a new Bug
				if (bug == null)
				{
					bug = getTaskManager().newBug();
					bug.setBugTrackerId(taskDTO.getBug().getBugTrackerId());
					bug.setTitle(taskDTO.getBug().getTitle());
					bug = getTaskManager().persistBug(bug);
				}
				taskToReturn.setBug(bug);
			}
			// we look if we need to create a new iterationTask - dispatch will
			// be
			// done by server business logic
			boolean existingIteration = false;
			for (final IterationTask iterationTask : taskToReturn.getIterationsTasks())
			{
				if (iterationTask.getIteration().getId().equals(taskDTO.getIteration().getIterationId()))
				{
					existingIteration = true;
					break;
				}
			}
			if (!existingIteration)
			{
				final IterationTask iterationTask = getTaskManager().newIterationTask();
				iterationTask.setIteration(getIterationManager().getIteration(taskDTO.getIteration().getIterationId()));
				iterationTask.setTask(taskToReturn);
				taskToReturn.getIterationsTasks().add(iterationTask);
			}
			return taskToReturn;
		}
		catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException ex)
		{
			throw manageException("Unable to build the task : " + taskDTO.getLabel(), ex);
		}
	}
}
