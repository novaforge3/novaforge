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

package org.novaforge.forge.ui.distribution.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeRequestDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ForgeViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.OrganizationViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ProfilViewDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.SynchDifferedDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.TargetForgeDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.ComponentToSyncEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeDistributionRequestEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeForgeRequestEnum;
import org.novaforge.forge.ui.distribution.shared.exceptions.DistributionServiceException;

import java.util.List;
import java.util.Set;

/**
 * The client side stub for the RPC service. Subscription : affiliation demand. Distribution : relation
 * between 2 forges
 */
@RemoteServiceRelativePath("distributionservice")
public interface DistributionService extends RemoteService
{

   /**
    * @return ForgeDTO
    * @throws DistributionServiceException
    */
   ForgeDTO getCurrentForge() throws DistributionServiceException;

   /**
    * Validate affiliation
    * 
    * @param pType
    * @param pAffiliationDTO
    * @return ForgeDTO
    * @throws DistributionServiceException
    */
   ForgeDTO approveDistributionRequest(TypeDistributionRequestEnum pType, String pAffiliationId)
         throws DistributionServiceException;

   /**
    * Invalidate affiliation
    * 
    * @param pType
    * @param pAffiliationId
    * @param pReason
    * @throws DistributionServiceException
    */
   void disapproveDistributionRequest(TypeDistributionRequestEnum pType, String pAffiliationId, String pReason)
         throws DistributionServiceException;

   /**
    * Get all subscription
    * 
    * @param pType
    * @return List<SubscriptionDTO>
    * @throws DistributionServiceException
    */
   List<ForgeRequestDTO> getForgeRequestList(TypeForgeRequestEnum pType) throws DistributionServiceException;

   /**
    * Create subscription
    * 
    * @param pSubscriptionDTO
    * @throws DistributionServiceException
    */
   void createSubscription(ForgeRequestDTO pSubscriptionDTO) throws DistributionServiceException;

   /**
    * Get a specific forge according to its id
    * 
    * @param pCurrentIdForge
    * @return
    * @throws DistributionServiceException
    */
   ForgeDTO getForge(String pCurrentIdForge) throws DistributionServiceException;

   List<ForgeDTO> getMotherList() throws DistributionServiceException;

   List<ForgeViewDTO> getIndicatorsByForgeView() throws DistributionServiceException;

   List<OrganizationViewDTO> getIndicatorsByOrganizationView() throws DistributionServiceException;

   List<ProfilViewDTO> getIndicatorsByProfilView() throws DistributionServiceException;

   void propagate(Set<ComponentToSyncEnum> pComponent) throws DistributionServiceException;

   List<TargetForgeDTO> getTargetForges() throws DistributionServiceException;

   void configureScheduling(SynchDifferedDTO psynchDifferedDTO, final boolean canPropagate)
         throws DistributionServiceException;

   void disableScheduling(final boolean canPropagate) throws DistributionServiceException;

   SynchDifferedDTO loadSchedulingConfig();

   boolean hasUserRefProjectAdminPermission();
}
