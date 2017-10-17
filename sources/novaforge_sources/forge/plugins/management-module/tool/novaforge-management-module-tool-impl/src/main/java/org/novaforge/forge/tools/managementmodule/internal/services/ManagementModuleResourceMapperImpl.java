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
package org.novaforge.forge.tools.managementmodule.internal.services;

import org.novaforge.forge.core.plugins.categories.management.DisciplineBean;
import org.novaforge.forge.core.plugins.categories.management.IssueBean;
import org.novaforge.forge.core.plugins.categories.management.IterationBean;
import org.novaforge.forge.core.plugins.categories.management.ScopeUnitBean;
import org.novaforge.forge.core.plugins.categories.management.TaskBean;
import org.novaforge.forge.core.plugins.categories.management.TaskCategoryBean;
import org.novaforge.forge.core.plugins.categories.management.TaskInfoBean;
import org.novaforge.forge.core.plugins.categories.management.TaskStatusEnum;
import org.novaforge.forge.core.plugins.categories.management.TaskTypeEnum;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.Bug;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;
import org.novaforge.forge.tools.managementmodule.domain.TaskType;
import org.novaforge.forge.tools.managementmodule.internal.transfer.DisciplineBeanImpl;
import org.novaforge.forge.tools.managementmodule.internal.transfer.IssueBeanImpl;
import org.novaforge.forge.tools.managementmodule.internal.transfer.IterationBeanImpl;
import org.novaforge.forge.tools.managementmodule.internal.transfer.ScopeUnitBeanImpl;
import org.novaforge.forge.tools.managementmodule.internal.transfer.TaskBeanImpl;
import org.novaforge.forge.tools.managementmodule.internal.transfer.TaskCategoryBeanImpl;
import org.novaforge.forge.tools.managementmodule.internal.transfer.TaskInfoBeanImpl;
import org.novaforge.forge.tools.managementmodule.services.ManagementModuleResourceMapper;

/**
 * This class is used to map domain/entity objects to common/plugin objects (and other way around)
 */
public class ManagementModuleResourceMapperImpl implements ManagementModuleResourceMapper
{

	/**
	 * Build an IterationBean from a Iteration
	 * 
	 * @param currentIteration
	 *          the src iteration
	 * @return the IterationBean created
	 */
	@Override
	public IterationBean buildIterationBeanFromIteration(final Iteration currentIteration)
	{
		final IterationBean iterationBean = new IterationBeanImpl();
		iterationBean.setId(Long.toString(currentIteration.getId()));
		iterationBean.setIterationNumber(currentIteration.getNumIteration());
		iterationBean.setStartDate(currentIteration.getStartDate());
		iterationBean.setEndDate(currentIteration.getEndDate());
		return iterationBean;
	}

	/**
	 * Get the appropriate TaskTypeEnum from TaskType
	 * 
	 * @param taskType
	 *          the source
	 * @return the correct TaskTypeEnum (IllegalStateException if not existing value)
	 */
	@Override
	public TaskTypeEnum getTaskTypeEnumFromType(final TaskType taskType)
	{
		if (taskType.getFunctionalId().equals(ManagementModuleConstants.TASK_TYPE_BUG))
		{
			return TaskTypeEnum.BUG;
		}
		else if (taskType.getFunctionalId().equals(ManagementModuleConstants.TASK_TYPE_WORK))
		{
			return TaskTypeEnum.WORK;
		}
		else
		{
			throw new IllegalStateException("Unknwown taskType with functionalId : " + taskType.getFunctionalId());
		}
	}

	/**
	 * Get the appropriate TaskStatusEnum from StatusTask
	 * 
	 * @param taskStatus
	 *          the source
	 * @return the correct TaskStatusEnum (IllegalStateException if not existing value)
	 */
	@Override
	public TaskStatusEnum getTaskStatusEnumFromStatus(final StatusTask taskStatus)
	{
		if (taskStatus.getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_NOT_AFFECTED))
		{
			return TaskStatusEnum.NOT_AFFECTED;
		}
		else if (taskStatus.getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_NOT_STARTED))
		{
			return TaskStatusEnum.NOT_STARTED;
		}
		else if (taskStatus.getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_IN_PROGRESS))
		{
			return TaskStatusEnum.IN_PROGRESS;
		}
		else if (taskStatus.getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_DONE))
		{
			return TaskStatusEnum.DONE;
		}
		else if (taskStatus.getFunctionalId().equals(ManagementModuleConstants.TASK_STATUS_CANCELED))
		{
			return TaskStatusEnum.CANCELED;
		}
		else
		{
			throw new IllegalStateException("Unknwown taskStatus with functionalId : "
					+ taskStatus.getFunctionalId());
		}
	}

	/**
	 * Build a ScopeUnitBean from a ScopeUnit
	 * 
	 * @param scopeUnit
	 *          the ScopeUnit
	 * @return the ScopeUnitBean created
	 */
	@Override
	public ScopeUnitBean buildScopeUnitBeanFromScopeUnit(final ScopeUnit scopeUnit)
	{
		if (scopeUnit == null)
		{
			return null;
		}
		final ScopeUnitBean scopeUnitBean = new ScopeUnitBeanImpl();
		scopeUnitBean.setId(scopeUnit.getUnitId());
		scopeUnitBean.setDescription(scopeUnit.getDescription());
		scopeUnitBean.setTitle(scopeUnit.getName());
		scopeUnitBean.setTypeDescription(scopeUnit.getType().getName());
		return scopeUnitBean;
	}

	/**
	 * Build a TaskCategoryBean from a TaskCategory
	 * 
	 * @param taskCategory
	 *          the TaskCategory
	 * @return the TaskCategoryBean created
	 */
	@Override
	public TaskCategoryBean buildTaskCategoryBeanFromTaskCategory(final TaskCategory taskCategory)
	{
		if (taskCategory == null)
		{
			return null;
		}
		final TaskCategoryBean taskCategoryBean = new TaskCategoryBeanImpl();
		taskCategoryBean.setId(Long.toString(taskCategory.getId()));
		taskCategoryBean.setDescription(taskCategory.getName());
		return taskCategoryBean;
	}

	/**
	 * This method builds a DisciplineBean from a Discipline
	 * 
	 * @param discipline
	 *          the Discipline source
	 * @return the created DisciplineBean
	 */
	@Override
	public DisciplineBean buildDisciplineBeanFromDiscipline(final Discipline discipline)
	{
		final DisciplineBean disciplineBean = new DisciplineBeanImpl();
		disciplineBean.setId(Long.toString(discipline.getId()));
		disciplineBean.setDescription(discipline.getName());
		return disciplineBean;
	}

	/**
	 * Build an IssueBean from a bug
	 * 
	 * @param bug
	 *          the source
	 * @return the created IssueBean
	 */
	@Override
	public IssueBean buildIssueBeanFromBug(final Bug bug)
	{
		if (bug == null)
		{
			return null;
		}
		final IssueBean issueBean = new IssueBeanImpl();
		issueBean.setBugId(Long.toString(bug.getId()));
		issueBean.setBugTrackerId(bug.getBugTrackerId());
		issueBean.setAdditionalInfo(bug.getAdditionalInfo());
		issueBean.setAssignedTo(bug.getAssignedTo());
		issueBean.setCategory(bug.getCategory());
		issueBean.setDescription(bug.getDescription());
		issueBean.setFixedInVersion(bug.getFixedInVersion());
		issueBean.setPriority(bug.getPriority());
		issueBean.setProductVersion(bug.getProductVersion());
		issueBean.setReporter(bug.getReporter());
		issueBean.setReproducibility(bug.getReproducibility());
		issueBean.setResolution(bug.getResolution());
		issueBean.setSeverity(bug.getSeverity());
		issueBean.setStatus(bug.getStatus());
		issueBean.setTargetVersion(bug.getTargetVersion());
		issueBean.setTitle(bug.getTitle());
		return issueBean;
	}

	/**
	 * Get the functional Id from the enum common value
	 * 
	 * @param status
	 *          the status
	 * @return the functional id of this status
	 */
	@Override
	public String getTaskStatusFunctionalIdFromStatusEnum(final TaskStatusEnum status)
	{
		switch (status)
		{
			case CANCELED:
				return ManagementModuleConstants.TASK_STATUS_CANCELED;
			case DONE:
				return ManagementModuleConstants.TASK_STATUS_DONE;
			case IN_PROGRESS:
				return ManagementModuleConstants.TASK_STATUS_IN_PROGRESS;
			case NOT_AFFECTED:
				return ManagementModuleConstants.TASK_STATUS_NOT_AFFECTED;
			case NOT_STARTED:
				return ManagementModuleConstants.TASK_STATUS_NOT_STARTED;
			default:
				throw new IllegalStateException("Unknwown taskStatus : " + status);
		}
	}

	/**
	 * Get the functional Id from the enum common value
	 * 
	 * @param status
	 *          the status
	 * @return the functional id of this status
	 */
	@Override
	public String getTaskTypeFunctionalIdFromTaskTypeEnum(final TaskTypeEnum type)
	{
		switch (type)
		{
			case BUG:
				return ManagementModuleConstants.TASK_TYPE_BUG;
			case WORK:
				return ManagementModuleConstants.TASK_TYPE_WORK;
			default:
				throw new IllegalStateException("Unknwown taskType : " + type);
		}
	}

	/**
	 * Build a TaskInfoBean from a task
	 * 
	 * @param task
	 *          the task
	 * @return the TaskInfoBean created
	 */
	@Override
	public TaskInfoBean buildTaskInfoBeanFromTask(final Task task)
	{
		final TaskInfoBean taskInfoBean = new TaskInfoBeanImpl();
		fillTaskInfoBeanDatasFromTask(taskInfoBean, task);
		return taskInfoBean;
	}

	/**
	 * Fill the taskInfoToFill parameter with appropriates values, using task datas
	 * 
	 * @param taskInfoToFill
	 *          the TaskInfoBean to fill
	 * @param taskSrc
	 *          the Task to use to fill the TaskInfoBean
	 */
	private void fillTaskInfoBeanDatasFromTask(final TaskInfoBean taskInfoToFill, final Task taskSrc)
	{
		taskInfoToFill.setDescription(taskSrc.getDescription());
		taskInfoToFill.setId(Long.toString(taskSrc.getId()));
		taskInfoToFill.setLastUpdateDate(taskSrc.getLastUpdateDate());
		taskInfoToFill.setTitle(taskSrc.getName());
		taskInfoToFill.setType(this.getTaskTypeEnumFromType(taskSrc.getTaskType()));
		taskInfoToFill.setEndDate(taskSrc.getEndDate());
		taskInfoToFill.setStartDate(taskSrc.getStartDate());
		taskInfoToFill.setStatus(this.getTaskStatusEnumFromStatus(taskSrc.getStatus()));
		taskInfoToFill.setInitialEstimation(taskSrc.getInitialEstimation());
	}

	/**
	 * Build a TaskBean from a task
	 * 
	 * @param task
	 *          the task
	 * @return the TaskBean created
	 */
	@Override
	public TaskBean buildTaskBeanFromTask(final Task task, final Iteration currentTaskIteration)
	{
		final TaskBean taskBean = new TaskBeanImpl();
		fillTaskInfoBeanDatasFromTask(taskBean, task);
		taskBean.setCategory(this.buildTaskCategoryBeanFromTaskCategory(task.getTaskCategory()));
		taskBean.setComment(task.getComment());
		taskBean.setConsumedTime(task.getConsumedTime());
		taskBean.setDiscipline(this.buildDisciplineBeanFromDiscipline(task.getDiscipline()));
		taskBean.setIssue(this.buildIssueBeanFromBug(task.getBug()));
		taskBean.setIteration(this.buildIterationBeanFromIteration(currentTaskIteration));
		taskBean.setRemainingTime(task.getRemainingTime());
		taskBean.setScopeUnit(this.buildScopeUnitBeanFromScopeUnit(task.getScopeUnit()));
		return taskBean;
	}
}
