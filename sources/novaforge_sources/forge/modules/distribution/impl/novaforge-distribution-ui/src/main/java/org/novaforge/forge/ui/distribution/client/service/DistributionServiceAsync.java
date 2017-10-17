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

import com.google.gwt.user.client.rpc.AsyncCallback;
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

import java.util.List;
import java.util.Set;

/**
 * The async counterpart of <code>forgeaffiliationservice</code>.
 */
public interface DistributionServiceAsync
{

   void getCurrentForge(AsyncCallback<ForgeDTO> pCb);

   void approveDistributionRequest(TypeDistributionRequestEnum pType, String pAffiliationId,
         AsyncCallback<ForgeDTO> pCb);

   void disapproveDistributionRequest(TypeDistributionRequestEnum pType, String pAffiliationId,
         String pReason, AsyncCallback<Void> pCb);

   void createSubscription(ForgeRequestDTO pSubscriptionDTO, AsyncCallback<Void> pCb);

   void getForge(String pCurrentIdForge, AsyncCallback<ForgeDTO> pCb);

   void getMotherList(AsyncCallback<List<ForgeDTO>> cb);

   void getForgeRequestList(TypeForgeRequestEnum pType, AsyncCallback<List<ForgeRequestDTO>> callback);

   void getIndicatorsByForgeView(AsyncCallback<List<ForgeViewDTO>> pCb);

   void getIndicatorsByOrganizationView(AsyncCallback<List<OrganizationViewDTO>> pCb);

   void getIndicatorsByProfilView(AsyncCallback<List<ProfilViewDTO>> pCb);

   void propagate(Set<ComponentToSyncEnum> componentToSyncList, AsyncCallback<Void> callback);

   void getTargetForges(AsyncCallback<List<TargetForgeDTO>> callback);

   void configureScheduling(SynchDifferedDTO pSynchDifferedDTO, boolean canPropagate,
         AsyncCallback<Void> callback);

   void disableScheduling(boolean canPropagate, AsyncCallback<Void> callback);

   void loadSchedulingConfig(AsyncCallback<SynchDifferedDTO> callback);

   void hasUserRefProjectAdminPermission(AsyncCallback<Boolean> callback);

}
