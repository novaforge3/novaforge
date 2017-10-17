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

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.novaforge.forge.tools.managementmodule.ui.shared.BugDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.GlobalMonitoringIndicatorsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationTaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MonitoringIndicatorsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskStatusDTO;

import java.util.List;
import java.util.Set;

/**
 * The async services to manipulate tasks in UI
 */
public interface TaskServiceAsync {

	/**
	 * Get and put to callback the list of task status
	 * 
	 * @param callback
	 *            the callback
	 */
	void getAllTaskStatus(AsyncCallback<Set<TaskStatusDTO>> callback);

	/**
	 * Async task creation
	 * 
	 * @param taskDTOToCreate
	 *            the task to create
	 * @param callBack
	 *            the callback
	 */
	void createTask(TaskDTO taskDTOToCreate, AsyncCallback<TaskDTO> callBack);

	/**
	 * Async task modification
	 * 
	 * @param taskDTOToModify
	 *            the task to modify
	 * @param callBack
	 *            the callback
	 */
	void modifyTask(TaskDTO taskDTOToModify, AsyncCallback<TaskDTO> callBack);

	/**
	 * Async task delete
	 * 
	 * @param taskId
	 * @param callback
	 */
	void deleteTask(Long taskId, AsyncCallback<Boolean> callback);

	/**
	 * Check if is it allow to prepare a new iteration
	 * 
	 * @param currentValidatedProjectPlanId
	 * @param pCb
	 */
	void isAllowIterationPreparation(Long currentValidatedProjectPlanId, AsyncCallback<Boolean> pCb);

	/**
	 * Get all scopeUnitDiscipline
	 * 
	 * @param projectPlanId
	 * @param pCb
	 */
	void getScopeUnitDiscipline(Long projectPlanId, AsyncCallback<Set<ScopeUnitDisciplineDTO>> pCb);

	/**
	 * Async ScopeUnitDisciplines creation
	 * 
	 * @param scopeUnitDisciplines
	 * @param callBack
	 *            the callback
	 */
	void createScopeUnitDiscipline(List<ScopeUnitDisciplineDTO> scopeUnitDisciplines,
			AsyncCallback<List<ScopeUnitDisciplineDTO>> pCb);

	/**
	 * Async deleting ScopeUnitDiscipline
	 * 
	 * @param scopeUnitId
	 * @param functionalId
	 * @param callback
	 */
	void deleteScopeUnitDiscipline(String scopeUnitId, String functionalId, AsyncCallback<Boolean> callback);

	/**
	 * Async terminating ScopeUnitDiscipline
	 * 
	 * @param unitId
	 * @param functionalId
	 * @param pCb
	 */
	void terminateScopeUnitDiscipline(String unitId, String functionalId, AsyncCallback<Boolean> pCb);

	void getCategoriesCountForAnIterationAndADiscipline(long iterationId, String disciplineFunctionalId,
			AsyncCallback<Integer> pCb);

	/**
	 * Async get differents informations about global monitoring
	 * 
	 * @param currentValidatedProjectPlanId
	 * @param pCb
	 */
	void getGlobalMonitoringInformations(Long currentValidatedProjectPlanId,
			AsyncCallback<GlobalMonitoringIndicatorsDTO> pCb);

	/**
	 * Get the bug list using the specified project id and login
	 * 
	 * @param projectId
	 *            the project id
	 * @param userLogin
	 *            the login
	 * @param callBack
	 *            the call back
	 */
	void getBugList(String projectId, String userLogin, AsyncCallback<Set<BugDTO>> callBack);

	/**
	 * Get the the detail for a bug using the specified project id and login
	 * 
	 * @param bugTrackerId
	 *            the bug tracker id
	 * @param projectId
	 *            the project id
	 * @param userLogin
	 *            the login
	 * @param callBack
	 *            the call back
	 */
	void getBugDetail(String bugTrackerId, String projectId, String userLogin, AsyncCallback<BugDTO> callBack);

	/**
	 * Close the ireation identified by the first parameter
	 * 
	 * @param iterationId
	 *            the id to identify the iteration to close
	 * @param callBack
	 *            the call back
	 */
	void closeIteration(long iterationId, AsyncCallback<IterationDTO> callBack);

	/**
	 * Get the monitoring indicators for an iteration and a discipline
	 * 
	 * @param iterationId
	 *            the iteration id
	 * @param disciplineFunctionalId
	 *            the discipline to use for filter (or null if we want all
	 *            disciplines)
	 * @param callBack
	 *            the callback
	 */
	void getMonitoringIndicators(long iterationId, String disciplineFunctionalId,
			AsyncCallback<MonitoringIndicatorsDTO> callBack);

	/**
	 * Async create a CSV format with the given list
	 * 
	 * @param list
	 *            , the list of IterationTaskDTO
	 * @param cb
	 */
	void createCSVFormatWithIterationTaskDTOList(List<IterationTaskDTO> displayedList,
			AsyncCallback<Boolean> cb);

}
