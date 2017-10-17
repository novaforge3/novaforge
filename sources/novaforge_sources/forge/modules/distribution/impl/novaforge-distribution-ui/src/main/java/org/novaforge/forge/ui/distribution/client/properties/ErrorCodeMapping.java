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
/**
 * 
 */
package org.novaforge.forge.ui.distribution.client.properties;

import com.google.gwt.core.client.GWT;
import org.novaforge.forge.ui.distribution.shared.exceptions.ErrorEnumeration;

/**
 * @author BILET-JC
 *
 */
public final class ErrorCodeMapping
{
   private static final DistributionMessage errorMessage = GWT.create(DistributionMessage.class);

   public static String getLocalizedMessage(final ErrorEnumeration pCode)
   {
      String returnMessage;
      switch (pCode)
      {
      case ERR_LOADING_LIST_FORGE:
         returnMessage = errorMessage.eLoadingListForge();
         break;
      case ERR_LOADING_AFFILIATION:
         returnMessage = errorMessage.eLoadingDistribution();
         break;
      case ERR_DEMAND_AFFILIATION:
         returnMessage = errorMessage.eSubscriptionDemand();
         break;
      case ERR_LOADING_FORGE_VIEW:
         returnMessage = errorMessage.eLoadingForgeView();
         break;
      case ERR_LOADING_ORGANIZATION_VIEW:
         returnMessage = errorMessage.eLoadingOrganizationView();
         break;
      case ERR_LOADING_PROFIL_VIEW:
         returnMessage = errorMessage.eLoadingProfilView();
         break;
      case ERR_DISTRIBUTION_DOWN:
         returnMessage = errorMessage.eDistributionDown();
         break;
      case TECHNICAL_ERROR:
      default:
         returnMessage = errorMessage.eTechnique();
         break;
      }
      return returnMessage;
   }
}
