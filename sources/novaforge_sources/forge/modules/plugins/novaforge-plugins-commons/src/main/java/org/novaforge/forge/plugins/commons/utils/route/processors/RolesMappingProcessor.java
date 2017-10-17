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
package org.novaforge.forge.plugins.commons.utils.route.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.domain.plugin.PluginRoleInformation;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.core.plugins.services.PluginRoleMappingService;
import org.novaforge.forge.plugins.commons.utils.route.RouteUtil;

/**
 * This {@link Processor} is used to manage Role mapping update
 * 
 * @author sbenoist
 */
public class RolesMappingProcessor implements Processor
{
  /**
   * Logger component
   */
  private static final Log               LOGGER = LogFactory.getLog(RolesMappingProcessor.class);

  /**
   * Reference to service implementation of {@link PluginRoleMappingService}
   */
  private final PluginRoleMappingService pluginRoleMappingService;

  /**
   * Reference to service implementation of {@link PluginMembershipService}
   */
  private final PluginMembershipService  pluginMembershipService;

  /**
   * Contains the queue name associated to this processor
   */
  private final String                   pluginQueueName;

  /**
   * Default constructor
   * 
   * @param pPluginQueueName
   *          the queue name associated to this processor
   * @param pPluginRoleMappingService
   *          the service implementation of {@link PluginRoleMappingService}
   * @param pPluginMembershipService
   *          The service implementation of {@link PluginMembershipService}
   */
  public RolesMappingProcessor(final String pPluginQueueName,
      final PluginRoleMappingService pPluginRoleMappingService,
      final PluginMembershipService pPluginMembershipService)
  {
    super();
    pluginQueueName = pPluginQueueName;
    pluginRoleMappingService = pPluginRoleMappingService;
    pluginMembershipService = pPluginMembershipService;
  }

  @Override
  public void process(final Exchange pExchange)
  {
    PluginRoleInformation pluginRoleInformation = null;
    try
    {
      LOGGER.info(pluginQueueName + "-Get a message.");

      // get the informations about the instance into the header
      final String instanceId = (String) pExchange.getIn().getHeader(PluginQueueHeader.INSTANCE_UUID_HEADER);

      final PluginQueueAction action = PluginQueueAction.fromLabel((String) pExchange.getIn().getHeaders()
          .get(PluginQueueHeader.ACTION_HEADER));

      // get roles mapping and memberships from the body of message
      pluginRoleInformation = pExchange.getIn().getBody(PluginRoleInformation.class);

      if (!action.equals(PluginQueueAction.UPDATE))
      {
        throw new IllegalArgumentException("Only Update action is permitted for roles mapping route.");
      }

      // remove the previous memberships
      pluginMembershipService.removeUserMemberships(instanceId, pluginRoleInformation.getRemoveMemberships());

      // update the roles mapping
      pluginRoleMappingService.updateRolesMapping(instanceId, pluginRoleInformation.getRolesMapping());

      // update the memberships
      pluginMembershipService.updateUserMemberships(instanceId, pluginRoleInformation.getUpdateMemberships());

      // forward all the headers from the input
      pExchange.getOut().setHeaders(pExchange.getIn().getHeaders());

      // send the success response
      pExchange.getOut().setHeader(PluginQueueHeader.STATUS_RESPONSE_HEADER, PluginQueueHeader.STATUS_OK);
    }
    catch (final Exception e)
    {
      // build the response body with the requested object and the exception
      RouteUtil.buildErrorMessage(pExchange, e, pluginRoleInformation);
      LOGGER.error("an error occured during roles mapping propagation", e);
    }

  }

}
