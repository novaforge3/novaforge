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
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustWeightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("projectplan")
public interface ProjectPlanService extends RemoteService {

	List<ProjectPlanDTO> getProjectPlanList(String projectId) throws ManagementModuleException;

	ProjectPlanDTO getProjectPlan(String projectId, Integer version) throws ManagementModuleException;

	/**
	 * Return the last projectPlan of a project, either the draft or the last
	 * version
	 * 
	 * @param pProjectId
	 * @return ProjectPlanDTO
	 * @throws ManagementModuleException
	 */
	ProjectPlanDTO getLastProjectPlan(String pProjectId) throws ManagementModuleException;

	ProjectPlanDTO creeteProjectPlan(String projectId) throws ManagementModuleException;

	ProjectPlanDTO validateProjectPlan(String projectId) throws ManagementModuleException;

	List<LotDTO> getLotList(Long projectPlanId) throws ManagementModuleException;

	LotDTO creationLot(String lotName, Date startDate, Date endDate, String desc, Long projectPlanId,
			Long parentLotId) throws ManagementModuleException;

	boolean saveLot(Long lotId, LotDTO dto) throws ManagementModuleException;

	LotDTO getLot(Long lotId) throws ManagementModuleException;

	boolean deleteLot(Long lotId) throws ManagementModuleException;

	List<EstimationDTO> getEstimationList(Long pProjectPlanId) throws ManagementModuleException;

	void validateEstimation(List<EstimationDTO> estimationList) throws ManagementModuleException;

	MarkerDTO creeteMarker(Long projectPlanId, String name, Date date, String desc,
			String markerTypeFunctionalId) throws ManagementModuleException;

	MarkerDTO saveMarker(Long id, MarkerDTO marker) throws ManagementModuleException;

	MarkerDTO getMarker(Long id) throws ManagementModuleException;

	boolean deleteMarker(Long markerId) throws ManagementModuleException;

	List<MarkerDTO> getMarkerList(Long projectPlanId) throws ManagementModuleException;

	EstimationComponentDTO getEstimationComponent(String pProjectId) throws ManagementModuleException;

	ProjectPlanDTO getLastValidatedProjectPlan(String projectId) throws ManagementModuleException;

	ProjectPlanDTO getLastProjectPlanFull(String projectId) throws ManagementModuleException;

	List<AdjustWeightDTO> getAllAdjustWeightDTOList() throws ManagementModuleException;

	/**
	 * Save the project plan and his settings
	 * 
	 * @param projectPlan
	 *            the project plan to save
	 * @return the project plan after save
	 * @throws ManagementModuleException
	 *             problem during save
	 */
	ProjectPlanDTO saveProjectPlanWithSettings(ProjectPlanDTO projectPlan) throws ManagementModuleException;

	/**                   
	 * Return a projectPlan for a specified projectPlan Id
	 * 
	 * @param projectPlanId
	 * @return
	 * @throws ManagementModuleException
	 */
	ProjectPlanDTO getProjectPlan(Long projectPlanId) throws ManagementModuleException;

	/**
	 * Update the remaining time of scopeUnits
	 * 
	 * @param Map
	 * @return boolean, attesting the success of the update
	 * @throws ManagementModuleException
	 */
	boolean updateRemainingScopeUnit(Map<String, Float> remainingsScopeUnit) throws ManagementModuleException;


}
