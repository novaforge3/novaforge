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
package org.novaforge.forge.tools.managementmodule.dao;

import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCriteria;

import java.util.List;
import java.util.Set;

/**
 * The DataAccessObject to manipulate Tasks
 */
public interface TaskDAO
{
	/**
	 * return the tasks linked to a specified scopeUnit
	 * 
	 * @param scopeUnitId
	 * @return
	 *         @
	 */
	List<Task> findByScopeUnitId(Long scopeUnitId);
	
	/**
	 * return the tasks linked to a specified taskCategory
	 * @param taskCategoryId id of task category
	 * @return list of task matching input taskCategoryId
	 */
	List<Task> findByTaskCategoryId(Long taskCategoryId);

	/**
	 * Return tasks linked to the given ScopeUnit and Discipline
	 * 
	 * @param scopeUnitId
	 * @param disciplineId
	 * @return the matching list task
	 */
	List<Task> findByScopeUnitIdAndDisciplineId(Long scopeUnitId, Long disciplineId);

	/**
	 * Return tasks linked to the given projectPlanId with an optional filter on displineId
	 * 
	 * @param projectPlanId
	 * @param disciplineId
	 *          Option filter (null to disable)
	 * @return
	 *         @
	 */
	Set<Task> findByProjectPlanIdAndDisciplineId(Long projectPlanId, String disciplineId);

	/**
	 * Get a task list using criteria in TaskCriteria object
	 * 
	 * @param taskCriteria
	 *          the object containing task criteria
	 * @return the appropriate tasks
	 *         @ problem during data access
	 */
	Set<Task> getTasksByCriteria(TaskCriteria taskCriteria);

	/**
	 * Delete the tasks of a project plan id
	 * 
	 * @param projectPlanId
	 *          the id of the project plan
	 *          @ exception d'accès aux données
	 */
	void deleteByProjectPlanId(Long projectPlanId);

	/**
	 * @param pTaskId
	 * @return
	 */
	Task findById(long pTaskId);

	/**
	 * @param pTask
	 * @return
	 */
	Task save(Task pTask);

	/**
	 * @param pTask
	 * @return
	 */
	Task merge(Task pTask);

	/**
	 * @param pFindById
	 */
	void delete(Task pFindById);

}
