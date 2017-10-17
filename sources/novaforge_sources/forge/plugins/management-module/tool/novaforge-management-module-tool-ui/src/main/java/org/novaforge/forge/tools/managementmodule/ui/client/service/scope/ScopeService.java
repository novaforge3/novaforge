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

package org.novaforge.forge.tools.managementmodule.ui.client.service.scope;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitGlobalMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitIterationMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitLightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitMonitoringStatusEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.List;
import java.util.Map;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("scopeservice")
public interface ScopeService extends RemoteService {

	List<ScopeUnitDTO> getProjectRefScopeUnitList(String projectId) throws ManagementModuleException;

	List<ScopeUnitDTO> getProjectPlanScope(Long projectPlanId, Long lotId, boolean showChildren,
			boolean showScopeForSubLot) throws ManagementModuleException;

	/**
	 * Get all scopeUnitLight (use to create a scopeUnitDiscipline) ScopeUnitDTO
	 * is a light version of ScopeUnitDTO
	 * 
	 * @param projectPlanId
	 * @return the list of ScopeUnitLightDTO
	 * @throws ManagementModuleException
	 */
	List<ScopeUnitLightDTO> getScopeUnitLight(Long projectPlanId) throws ManagementModuleException;

	List<String> getScopeTypeList() throws ManagementModuleException;

	List<LotDTO> getLotList(Long projectPlanId) throws ManagementModuleException;

	ScopeUnitDTO createScopeUnit(ScopeUnitDTO pScopeUnit, String projectId) throws ManagementModuleException;

	ScopeUnitDTO createScopeUnitFromRef(ScopeUnitDTO pRefScopeUnit, Long lotId)
			throws ManagementModuleException;

	ScopeUnitDTO editScopeUnit(ScopeUnitDTO pScopeUnit, String projectId) throws ManagementModuleException;

	Boolean deleteManualScopeUnit(String pScopeUnitId) throws ManagementModuleException;

	Boolean isAlreadyLinkedRef(String pRefScopeUnitId) throws ManagementModuleException;

	Boolean linkRefScopeUnitToLot(String pRefScopeUnitId, String RefScopeUnitVersion, Long lotId)
			throws ManagementModuleException;

	Boolean removeScopeUnitFromScope(String pScopeUnitId) throws ManagementModuleException;

	Boolean unlinkScopeUnitFromRef(String pScopeUnitId) throws ManagementModuleException;

	Boolean updateLinkRefScopeUnitToLot(String pScopeUnitId, Long lotId) throws ManagementModuleException;

	Boolean linkExistingScopeUnit(String existingRefScopeId, String RefScopeUnitVersion, String pScopeUnitId)
			throws ManagementModuleException;

	/**
	 * Get all scopeUnits and them monitoring for the given projectPlan
	 * 
	 * @param currentValidatedProjectPlanId
	 * @return the list of scopeUnits and them monitoring
	 * @throws ManagementModuleException
	 */
	List<ScopeUnitGlobalMonitoringDTO> getScopeUnitMonitoring(Long currentValidatedProjectPlanId)
			throws ManagementModuleException;

	/**
	 * Finish a scopeUnit with the given id
	 * 
	 * @param scopeUnitId
	 * @return a Boolean, attesting or not the success
	 * @throws ManagementModuleException
	 */
	Boolean finishScopeUnit(String scopeUnitId) throws ManagementModuleException;

	/**
	 * Get the scope unit monitoring list of an iteration
	 * 
	 * @param iterationId
	 *            the iteration identifier
	 * @return the list
	 */
	List<ScopeUnitIterationMonitoringDTO> getScopeUnitMonitoringByIteration(long iterationId,
			String disciplineFunctionalId) throws ManagementModuleException;

    /**
     * Create  a CSV format with the given list
     * @param list, the list of ScopeUnitGlobalMonitoringDTO
     * @param map
	 * @return true if ok
     */
	Boolean createCSVFormatWithScopeUnitGlobalMonitoringDTOList(List<ScopeUnitGlobalMonitoringDTO> list,
			Map<ScopeUnitMonitoringStatusEnum, String> map);

	/**
    * Create a CSV with the given list
    * @param list, the list of ScopeUnitIterationMonitoringDTO
    */
   void createCSVFromScopeUnitIterationMonitoringDTOList(List<ScopeUnitIterationMonitoringDTO> listToExport);
	
}
