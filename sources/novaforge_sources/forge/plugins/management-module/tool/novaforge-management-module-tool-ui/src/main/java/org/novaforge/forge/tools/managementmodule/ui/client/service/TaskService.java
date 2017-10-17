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
package org.novaforge.forge.tools.managementmodule.ui.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.novaforge.forge.core.plugins.categories.management.ManagementServiceException;
import org.novaforge.forge.tools.managementmodule.ui.shared.BugDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.GlobalMonitoringIndicatorsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationTaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MonitoringIndicatorsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskStatusDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.List;
import java.util.Set;

/**
 * This interface exposes the services to manipulate the task
 */
@RemoteServiceRelativePath("task")
public interface TaskService extends RemoteService {

	/**
	 * Get all the statusTask
	 * 
	 * @return the status task
	 * @throws ManagementModuleException
	 *             problem during operation
	 */
	Set<TaskStatusDTO> getAllTaskStatus() throws ManagementModuleException;

	/**
	 * Create a new task
	 * 
	 * @param taskDTOToCreate
	 *            the task to create
	 */
	TaskDTO createTask(TaskDTO taskDTOToCreate) throws ManagementModuleException;

	/**
	 * Modify an existing task
	 * 
	 * @param taskDTOToModify
	 *            the task to modify
	 */
	TaskDTO modifyTask(TaskDTO taskDTOToModify) throws ManagementModuleException;

	/**
	 * Delete a task with the given taskId
	 * 
	 * @param taskId
	 * @return a Boolean, attesting the success
	 * @throws ManagementModuleException
	 *             problem during operation
	 */
	Boolean deleteTask(Long taskId) throws ManagementModuleException;

	/**
	 * Check if is it allow to prepare a new iteration
	 * 
	 * @param currentValidatedProjectPlanId
	 * @return Boolean
	 */
	Boolean isAllowIterationPreparation(Long currentValidatedProjectPlanId) throws ManagementModuleException;

	/**
	 * 
	 * Get all scopeUnitDiscipline
	 * 
	 * @param projectPlanId
	 * @return
	 */
	Set<ScopeUnitDisciplineDTO> getScopeUnitDiscipline(Long projectPlanId) throws ManagementModuleException;

	/**
	 * Create a new ScopeUnitDisciplineDTO
	 * 
	 * @param scopeUnitDisciplines
	 * @return the created ScopeUnitDisciplines
	 * @throws ManagementModuleException
	 */
	List<ScopeUnitDisciplineDTO> createScopeUnitDiscipline(List<ScopeUnitDisciplineDTO> scopeUnitDisciplines)
			throws ManagementModuleException;

	/**
	 * Delete the ScopeUnitDiscipline identified by parameters
	 * 
	 * @param scopeUnitId
	 * @param functionalId
	 * @return a boolean, attesting the success
	 * @throws ManagementModuleException
	 */
	boolean deleteScopeUnitDiscipline(String scopeUnitId, String functionalId)
			throws ManagementModuleException;

	/**
	 * Try to terminate ScopeUnitDiscipline Ok if no task not 'terminated' in
	 * relation with ScopeUnit or Discipline
	 * 
	 * @param unitId
	 * @param functionalId
	 * @return the boolean attesting the success
	 * @throws ManagementModuleException
	 */
	boolean terminateScopeUnitDiscipline(String unitId, String functionalId) throws ManagementModuleException;

	/**
	 * count Task Categories used in tasks for an iteration and a discipline
	 * 
	 * @param iterationId
	 * @param disciplineFunctionalId
	 * @return
	 * @throws ManagementServiceException
	 */
	Integer getCategoriesCountForAnIterationAndADiscipline(long iterationId, String disciplineFunctionalId)
			throws ManagementModuleException;

	/**
	 * Count different informations about global monitoring
	 * 
	 * @param currentValidatedProjectPlanId
	 * @return GlobalMonitoringIndicatorsDTO
	 * @throws ManagementModuleException
	 */
	GlobalMonitoringIndicatorsDTO getGlobalMonitoringInformations(Long currentValidatedProjectPlanId)
			throws ManagementModuleException;

	/**
	 * Get the bug list using the specified project id and login
	 * 
	 * @param projectId
	 *            the project id
	 * @param userLogin
	 *            the login
	 * @return the appropriate list
	 * @throws ManagementModuleException
	 *             problem during get
	 */
	Set<BugDTO> getBugList(String projectId, String userLogin) throws ManagementModuleException;

	/**
	 * Get the the detail for a bug using the specified project id and login
	 * 
	 * @param bugTrackerId
	 *            the bug tracker id
	 * @param projectId
	 *            the project id
	 * @param userLogin
	 *            the login
	 * @throws ManagementModuleException
	 *             problem during get
	 */
	BugDTO getBugDetail(String bugTrackerId, String projectId, String userLogin)
			throws ManagementModuleException;

	/**
	 * Close the ireation identified by the first parameter
	 * 
	 * @param iterationId
	 *            the id to identify the iteration to close
	 * @param callBack
	 *            the call back
	 * @throws ManagementModuleException
	 *             problem during closure
	 */
	IterationDTO closeIteration(long iterationId) throws ManagementModuleException;

	/**
	 * Get the monitoring indicators for an iteration and a discipline
	 * 
	 * @param iterationId
	 *            the iteration id
	 * @param disciplineFunctionalId
	 *            the discipline to use for filter (or null if we want all
	 *            disciplines)
	 * @return the indicators list
	 * @throws ManagementModuleException
	 *             problem during indicators get
	 */
	MonitoringIndicatorsDTO getMonitoringIndicators(long iterationId, String disciplineFunctionalId)
			throws ManagementModuleException;

	/**
	 * Create a CSV format with the given list
	 * 
	 * @param list
	 *            , the list of IterationTaskDTO
	 * @return true if ok
	 */
	Boolean createCSVFormatWithIterationTaskDTOList(List<IterationTaskDTO> displayedList);

}
