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
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustWeightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The async counterpart of <code>lastcommitservice</code>.
 */
public interface ProjectPlanServiceAsync {
	void getProjectPlanList(String projectId, AsyncCallback<List<ProjectPlanDTO>> callback);

	void getProjectPlan(String projectId, Integer version, AsyncCallback<ProjectPlanDTO> callback);

	void getLastProjectPlan(String pProjectId, AsyncCallback<ProjectPlanDTO> callback);

	void getProjectPlan(Long projectPlanId, AsyncCallback<ProjectPlanDTO> callback);
	
	void creeteProjectPlan(String projectId, AsyncCallback<ProjectPlanDTO> callback);

	void validateProjectPlan(String projectId, AsyncCallback<ProjectPlanDTO> callback);

	void getLotList(Long projectPlanId, AsyncCallback<List<LotDTO>> callback);

	void creationLot(String lotName, Date startDate, Date endDate, String desc, Long projectPlanId, Long parentLotId,
			AsyncCallback<LotDTO> callback);

	void saveLot(Long lotId, LotDTO dto, AsyncCallback<Boolean> callback);

	void getLot(Long lotId, AsyncCallback<LotDTO> callback);

	void deleteLot(Long lotId, AsyncCallback<Boolean> callback);

	void getEstimationList(Long pProjectPlanId, AsyncCallback<List<EstimationDTO>> callback);

	void validateEstimation(List<EstimationDTO> estimationList, AsyncCallback<Void> cb);

	void creeteMarker(Long projectPlanId, String name, Date date, String desc, String markerTypeFunctionalId,
			AsyncCallback<MarkerDTO> callback);

	void saveMarker(Long id, MarkerDTO marker, AsyncCallback<MarkerDTO> callback);

	void getMarker(Long id, AsyncCallback<MarkerDTO> callback);

	void deleteMarker(Long markerId, AsyncCallback<Boolean> callback);

	void getMarkerList(Long projectPlanId, AsyncCallback<List<MarkerDTO>> callback);

	void getEstimationComponent(String pProjectId, AsyncCallback<EstimationComponentDTO> callback);

	void getLastValidatedProjectPlan(String projectId, AsyncCallback<ProjectPlanDTO> pCb);

	void getLastProjectPlanFull(String projectId, AsyncCallback<ProjectPlanDTO> callback);

	void getAllAdjustWeightDTOList(AsyncCallback<List<AdjustWeightDTO>> callback);

	/**
	 * Save the project plan and his settings
	 * 
	 * @param projectPlan
	 *            the project plan to save
	 */
	void saveProjectPlanWithSettings(ProjectPlanDTO projectPlan, AsyncCallback<ProjectPlanDTO> callback);

	/**
	 * 
	 * Async update remaining scopeUnit
	 * @param list
	 * @param pCb
	 */
	void updateRemainingScopeUnit(Map<String, Float> remainingsScopeUnit, AsyncCallback<Boolean> pCb);
}
