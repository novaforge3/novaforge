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
package org.novaforge.forge.distribution.register.services;

import org.novaforge.forge.distribution.register.domain.ForgeDTO;
import org.novaforge.forge.distribution.register.domain.ForgeRequestDTO;
import org.novaforge.forge.distribution.register.exceptions.ForgeDistributionException;

import javax.jws.WebService;
import java.util.List;

/**
 * Manages the Forge distribution. Exposed as a standard service but can also be called remotely by the
 * children forges.
 *
 * @author Mohamed IBN EL AZZOUZI
 * @date 26 déc. 2011
 */
@WebService
public interface ForgeDistributionService
{

  int CENTRAL   = 0;
  int ZONAL     = 1;
  int LOCAL     = 2;
  int ORPHAN    = -1;
  int SUSPENDED = -2;
  int MAX_LEVEL = 3;

  ForgeDTO addForge(ForgeDTO newForge) throws ForgeDistributionException;

  /**
   * Updates only forge Description. Do not update forgeLevel, and parent and child subscriptions.
   *
   * @param forge
   *
   * @return
   *
   * @throws ForgeDistributionException
   */
  ForgeDTO updateForgeDescription(ForgeDTO forge) throws ForgeDistributionException;

  ForgeDTO getForge(String forgeId) throws ForgeDistributionException;

  List<ForgeDTO> getAvailableMotherForgesToSubscription() throws ForgeDistributionException;

  ForgeRequestDTO subscribeForge(String sourceForgeId, String destinationForgeId, String comment)
      throws ForgeDistributionException;

  ForgeRequestDTO approveSubription(String forgeRequestId) throws ForgeDistributionException;

  ForgeRequestDTO disapproveSubription(String forgeRequestId, String comment) throws ForgeDistributionException;

  ForgeRequestDTO unsubscribeForge(String sourceForgeId, String destinationForgeId, String comment)
      throws ForgeDistributionException;

  ForgeRequestDTO approveUnsubscription(String forgeRequestId) throws ForgeDistributionException;

  ForgeRequestDTO disapproveUnsubscription(String forgeRequestId, String comment) throws ForgeDistributionException;

  List<ForgeRequestDTO> getForgeRequests(String forgeId, String type) throws ForgeDistributionException;

}
