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
package org.novaforge.forge.core.organization.internal.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.dao.ProjectElementDAO;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.route.OrganizationQueueHeader;
import org.novaforge.forge.core.organization.route.OrganizationQueueName;
import org.novaforge.forge.core.organization.services.MembershipService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
public class SharedProjectRoute extends RouteBuilder
{

  private static final Log LOGGER = LogFactory.getLog(SharedProjectRoute.class);
  private MembershipService         membershipService;
  private ForgeConfigurationService forgeConfigurationService;
  private ProjectElementDAO         projectElementDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public void configure() throws Exception
  {
    from(OrganizationQueueName.SHARED_PROJECT_QUEUE_FULL).id(OrganizationQueueName.SHARED_PROJECT_ROUTE_NAME)
        .process(new Processor()
        {
          /**
           * {@inheritDoc}
           */
          @Override
          public void process(final Exchange pExchange) throws Exception
          {
            // Get the projectId
            final String projectId = (String) pExchange.getIn().getHeader(
                OrganizationQueueHeader.PROJECT_ID_HEADER);

            // Get the uuid of the user
            final String userUUID = (String) pExchange.getIn().getHeader(
                OrganizationQueueHeader.USER_UUID_HEADER);

            // Get the type od the user
            final RealmType type = RealmType.fromLabel((String) pExchange.getIn().getHeader(
                OrganizationQueueHeader.TYPE_HEADER));

            // if the project doesn't exist : nothing to do
            if (projectElementDAO.existId(projectId))
            {
              createMembership(projectId, UUID.fromString(userUUID), type);
            }
          }
        });
  }

  private void createMembership(final String pProjectId, final UUID pUserUUID, final RealmType pRealmType)
      throws ProjectServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug((String.format("add membership for [projectId=%s, uuid=%s", pProjectId, pUserUUID)));
    }
    final Set<String> roles = new HashSet<String>();
    if (RealmType.SYSTEM.equals(pRealmType))
    {
      roles.add(forgeConfigurationService.getForgeAdministratorRoleName());
    }
    else
    {
      roles.add(forgeConfigurationService.getForgeMemberRoleName());
    }

    // addd the memberships
    membershipService.addUserMembership(pProjectId, pUserUUID, roles, null, null, false);
  }

  public void setMembershipService(final MembershipService pMembershipService)
  {
    membershipService = pMembershipService;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  public void setProjectElementDAO(final ProjectElementDAO pProjectElementDAO)
  {
    projectElementDAO = pProjectElementDAO;
  }

}
