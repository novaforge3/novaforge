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
package org.novaforge.forge.distribution.register.client;

import org.novaforge.forge.distribution.register.domain.ForgeDTO;
import org.novaforge.forge.distribution.register.exceptions.ForgeDistributionException;

import javax.jws.WebService;

/**
 * (Web)Service exposed by each forge and called remotely on the forge which did the (un)subscription request
 * in order to notify this forge that the request has been accepted.
 * 
 * @author rols-p
 */
@WebService
public interface DistributionNotificationService
{

  String DISTRIB_NOTIF_ENDPOINT_NAME = "DistributionNotificationService";

  /**
   * Tell that the subscription has been approved for the provided forge. See the forge details in order to
   * have it's level or parent...
   * 
   * @param forge
   *          the forge for which the subscription has been approved.
   * @param centralForgePublicKey
   *          the central forge public key.
   * @throws ForgeDistributionException
   */
  void subscriptionApproved(ForgeDTO forge, String centralForgePublicKey) throws ForgeDistributionException;

  /**
   * Tell that the unsubscription has been approved for the provided forge. See the forge details in order to
   * have it's level.
   * 
   * @param forge
   *          the forge for which the subscription has been approved.
   * @param centralForgePublicKey
   *          the central forge public key.
   * @throws ForgeDistributionException
   */
  void unsubscriptionApproved(ForgeDTO forge, String centralForgePublicKey) throws ForgeDistributionException;
}
