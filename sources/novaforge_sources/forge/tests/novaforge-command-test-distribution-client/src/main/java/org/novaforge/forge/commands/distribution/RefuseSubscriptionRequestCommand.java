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
package org.novaforge.forge.commands.distribution;

import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.distribution.register.domain.RequestType;
import org.novaforge.forge.distribution.register.services.ForgeDistributionService;

import java.util.Collection;
import java.util.UUID;

/**
 * This command (launched onto CENTRAL forge) will refuse to validate the subscription request.
 * 
 * @author Marc Blachon
 */
@Command(scope = "distribution", name = "refuse-subscription",
    description = "Refuse the received request for subscription")
public class RefuseSubscriptionRequestCommand extends OsgiCommandSupport
{

  /**
   * Reference to implementation of {@link ForgeDistributionService}
   */
  private ForgeDistributionService   forgeDistributionService;
  /**
   * Reference to implementation of {@link ForgeIdentificationService}
   */
  private ForgeIdentificationService forgeIdentificationService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    Collection<org.novaforge.forge.distribution.register.domain.ForgeRequestDTO> forgeRequests;
    UUID forgeId = forgeIdentificationService.getForgeId();
    forgeRequests = forgeDistributionService.getForgeRequests(forgeId.toString(),
        RequestType.UNSUBSCRIBE.toString());
    if (forgeRequests.size() == 1)
    {
      for (org.novaforge.forge.distribution.register.domain.ForgeRequestDTO forgeRequestDTO : forgeRequests)
      {
        String requestId = forgeRequestDTO.getForgeRequestId().toString();
        String forgeSource = forgeRequestDTO.getSourceForge().getLabel();
        forgeDistributionService.disapproveSubription(requestId, "Refuse the subscription for " + forgeSource);
        System.out.println("Subscription has been refused");
      }
    }
    else if (forgeRequests.size() > 1)
    {
      System.err.println("More than one UNSUBSCRIBE request are pending to be refused");
    }
    else
    {
      System.err.println("No UNSUBSCRIBE request has been received");
    }

    return null;
  }

  /**
   * Use by container to inject {@link ForgeDistributionService}
   * 
   * @param pForgeDistributionService
   *          the forgeDistributionService to set
   */

  public void setForgeDistributionService(final ForgeDistributionService pForgeDistributionService)
  {
    forgeDistributionService = pForgeDistributionService;
  }

  /**
   * Use by container to inject {@link ForgeIdentificationService}
   * 
   * @param pForgeIdentificationService
   *          the forgeIdentificationService to set
   */
  public void setForgeIdentificationService(final ForgeIdentificationService pForgeIdentificationService)
  {

    forgeIdentificationService = pForgeIdentificationService;
  }

}
