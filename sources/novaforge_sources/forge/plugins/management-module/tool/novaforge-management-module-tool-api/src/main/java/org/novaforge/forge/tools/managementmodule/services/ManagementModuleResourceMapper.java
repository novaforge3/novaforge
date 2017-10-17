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
package org.novaforge.forge.tools.managementmodule.services;

import org.novaforge.forge.core.plugins.categories.management.DisciplineBean;
import org.novaforge.forge.core.plugins.categories.management.IssueBean;
import org.novaforge.forge.core.plugins.categories.management.IterationBean;
import org.novaforge.forge.core.plugins.categories.management.ScopeUnitBean;
import org.novaforge.forge.core.plugins.categories.management.TaskBean;
import org.novaforge.forge.core.plugins.categories.management.TaskCategoryBean;
import org.novaforge.forge.core.plugins.categories.management.TaskInfoBean;
import org.novaforge.forge.core.plugins.categories.management.TaskStatusEnum;
import org.novaforge.forge.core.plugins.categories.management.TaskTypeEnum;
import org.novaforge.forge.tools.managementmodule.domain.Bug;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;
import org.novaforge.forge.tools.managementmodule.domain.TaskType;

/**
 * @author sbenoist
 */
public interface ManagementModuleResourceMapper
{

	/**
	 * Build an IterationBean from a Iteration
	 * 
	 * @param currentIteration
	 *          the src iteration
	 * @return the IterationBean created
	 */
	IterationBean buildIterationBeanFromIteration(final Iteration currentIteration);

	/**
	 * Get the appropriate TaskTypeEnum from TaskType
	 * 
	 * @param taskType
	 *          the source
	 * @return the correct TaskTypeEnum (IllegalStateException if not existing value)
	 */
	TaskTypeEnum getTaskTypeEnumFromType(final TaskType taskType);

	/**
	 * Get the appropriate TaskStatusEnum from StatusTask
	 * 
	 * @param taskStatus
	 *          the source
	 * @return the correct TaskStatusEnum (IllegalStateException if not existing value)
	 */
	TaskStatusEnum getTaskStatusEnumFromStatus(final StatusTask taskStatus);

	/**
	 * Build a ScopeUnitBean from a ScopeUnit
	 * 
	 * @param scopeUnit
	 *          the ScopeUnit
	 * @return the ScopeUnitBean created
	 */
	ScopeUnitBean buildScopeUnitBeanFromScopeUnit(final ScopeUnit scopeUnit);

	/**
	 * Build a TaskCategoryBean from a TaskCategory
	 * 
	 * @param taskCategory
	 *          the TaskCategory
	 * @return the TaskCategoryBean created
	 */
	TaskCategoryBean buildTaskCategoryBeanFromTaskCategory(final TaskCategory taskCategory);

	/**
	 * This method builds a DisciplineBean from a Discipline
	 * 
	 * @param discipline
	 *          the Discipline source
	 * @return the created DisciplineBean
	 */
	DisciplineBean buildDisciplineBeanFromDiscipline(final Discipline discipline);

	/**
	 * Build an IssueBean from a bug
	 * 
	 * @param bug
	 *          the source
	 * @return the created IssueBean
	 */
	IssueBean buildIssueBeanFromBug(final Bug bug);

	/**
	 * Get the functional Id from the enum common value
	 * 
	 * @param status
	 *          the status
	 * @return the functional id of this status
	 */
	String getTaskStatusFunctionalIdFromStatusEnum(final TaskStatusEnum status);

	/**
	 * Get the functional Id from the enum common value
	 * 
	 * @param status
	 *          the status
	 * @return the functional id of this status
	 */
	String getTaskTypeFunctionalIdFromTaskTypeEnum(final TaskTypeEnum type);

	/**
	 * Build a TaskInfoBean from a task
	 * 
	 * @param task
	 *          the task
	 * @return the TaskInfoBean created
	 */
	TaskInfoBean buildTaskInfoBeanFromTask(final Task task);

	/**
	 * Build a TaskBean from a task
	 * 
	 * @param task
	 *          the task
	 * @return the TaskBean created
	 */
	TaskBean buildTaskBeanFromTask(final Task task, final Iteration currentTaskIteration);

}