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

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitGlobalMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitIterationMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitLightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitMonitoringStatusEnum;

import java.util.List;
import java.util.Map;

/**
 * The async counterpart of <code>lastcommitservice</code>.
 */
public interface ScopeServiceAsync
{
   
 //TODO ajouter les javadocs sur les methodes restantes
   
   void getProjectRefScopeUnitList(String projectId, AsyncCallback<List<ScopeUnitDTO>> callback);

   /**
    * Return all Scope Unit for a projectPlan or for a lot
    * 
    * @param projectPlanId id of the projectPlan
    * @param lotId id of Lot for getting only 
    * @param showChildren specifie if it's return Scope child for the parent scope
    * @param showScopeForSubLot specifie if it's return scole from SubLot
    * @param callback the callback function
    */
   void getProjectPlanScope(Long projectPlanId, Long lotId, boolean showChildren, boolean showScopeForSubLot, AsyncCallback<List<ScopeUnitDTO>> callback);

   void getScopeUnitLight(Long projectPlanId, AsyncCallback<List<ScopeUnitLightDTO>> callback);
 
   void getScopeTypeList(AsyncCallback<List<String>> callback);

   void getLotList(Long projectPlanId, AsyncCallback<List<LotDTO>> callback);

   void createScopeUnit(ScopeUnitDTO pScopeUnit, String projectId, AsyncCallback<ScopeUnitDTO> callback);

   void createScopeUnitFromRef(ScopeUnitDTO pRefScopeUnit, Long lotId, AsyncCallback<ScopeUnitDTO> callback);

   void editScopeUnit(ScopeUnitDTO pScopeUnit, String projectId, AsyncCallback<ScopeUnitDTO> callback);

   void deleteManualScopeUnit(String pScopeUnitId, AsyncCallback<Boolean> callback);

   void isAlreadyLinkedRef(String pRefScopeUnitId, AsyncCallback<Boolean> callback);

   void linkRefScopeUnitToLot(String pRefScopeUnitId, String RefScopeUnitVersion, Long lotId,
         AsyncCallback<Boolean> callback);

   void updateLinkRefScopeUnitToLot(String pScopeUnitId, Long lotId, AsyncCallback<Boolean> callback);

   void linkExistingScopeUnit(String existingRefScopeId, String RefScopeUnitVersion, String pScopeUnitId,
         AsyncCallback<Boolean> callback);

   void removeScopeUnitFromScope(String pScopeUnitId, AsyncCallback<Boolean> callback);

   void unlinkScopeUnitFromRef(String pScopeUnitId, AsyncCallback<Boolean> callback);

	/**
	 * Async get all scopeUnits and them monitoring for the given projectPlan
	 * 
	 * @param currentValidatedProjectPlanId
	 * @param pCb
	 */
   void getScopeUnitMonitoring(Long currentValidatedProjectPlanId, AsyncCallback<List<ScopeUnitGlobalMonitoringDTO>> pCb);

	/**
	 * Asyn finishing a scopeUnit
	 * @param scopeUnitId
	 */
	void finishScopeUnit(String scopeUnitId, AsyncCallback<Boolean> callback);


	/**
    * Get the scope unit monitoring list of an iteration and a discipline
    * @param iterationId the iteration identifier
    * @param disciplineFunctionalId the functional ID we look for (null to avoid filtering)
    * @return the list
    */
    void getScopeUnitMonitoringByIteration(long iterationId, String disciplineFunctionalId, AsyncCallback<List<ScopeUnitIterationMonitoringDTO>> callback);

    /**
     * Create  a CSV format with the given list
     * @param map 
     * @param list, the list of ScopeUnitGlobalMonitoringDTO
     * @param cb
     */
	void createCSVFormatWithScopeUnitGlobalMonitoringDTOList(List<ScopeUnitGlobalMonitoringDTO> list, Map<ScopeUnitMonitoringStatusEnum, String> map, AsyncCallback<Boolean> callback);

	/**
    * Create a CSV with the given list
    * @param list, the list of ScopeUnitIterationMonitoringDTO
    * @param callBack the callBack
    */
   void createCSVFromScopeUnitIterationMonitoringDTOList(List<ScopeUnitIterationMonitoringDTO> listToExport,
         AsyncCallback<Void> callBack);
}
