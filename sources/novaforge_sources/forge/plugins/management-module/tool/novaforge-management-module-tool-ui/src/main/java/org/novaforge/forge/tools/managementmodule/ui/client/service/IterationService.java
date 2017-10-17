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
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.PhaseTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.List;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("iteration")
public interface IterationService extends RemoteService {
   List<IterationDTO> getIterationList(Long projectPlanId) throws ManagementModuleException;

   /**
    * Get all finished and actual iterations
    * Used for the iterationList tab in the pilotageExecution panel
    * @param projectPlanId
    * @return List<IterationDTO>
    * @throws ManagementModuleException
    */
   List<IterationDTO> getFinishedAndCurrentIterationList(Long projectPlanId)
   		throws ManagementModuleException;
   
   /**
    * Get the older not finished iteration
    * @param pProjectPlanId
    * @param pSimple
    * @return the older current IterationDTO
    * @throws ManagementModuleException
    */
   IterationDTO getCurrentOrLastFinishedIteration(Long pProjectPlanId, boolean pSimple)
   		throws ManagementModuleException;
   
   /**
    * Get the next iteration, the one which has to be prepare
    * @param pProjectPlanId
    * @param pSimple
    * @return the IterationDTO to prepare
    * @throws ManagementModuleException
    */
   IterationDTO getNextIteration(Long pProjectPlanId, boolean pSimple)
   		throws ManagementModuleException;
   
   List<PhaseTypeDTO> getPhasesTypesForNextIteration(final IterationDTO currentIterationDTO) throws ManagementModuleException;

   boolean createIteration(IterationDTO iteration) throws ManagementModuleException;

   boolean deleteIteration(long projectPlanId, long pIterationIdToDelete)
         throws ManagementModuleException;

   IterationDTO updateIteration(long projectPlanId, IterationDTO iteration)
         throws ManagementModuleException;

   List<LotDTO> getLotsList(long projectPlanId) throws ManagementModuleException;

   IterationDTO getIterationById(long pIterationId, boolean pSimple) throws ManagementModuleException;



}
