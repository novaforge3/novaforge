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
package org.novaforge.forge.commons.route.internal;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.commons.route.domain.PluginRoleInformationImpl;
import org.novaforge.commons.route.util.RouteUtil;
import org.novaforge.forge.core.organization.handlers.MembershipHandler;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginRoleInformation;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.exceptions.PluginRouteException;
import org.novaforge.forge.core.plugins.services.PluginsManager;

import java.util.List;
import java.util.Map;

/**
 * @author lamirang
 */
public class PluginRolesMappingRoute extends RouteBuilder
{

  /**
   * Logger component
   */
  private static final Log LOG = LogFactory.getLog(PluginRolesMappingRoute.class);
  /**
   * Reference to service implementation of {@link PluginsManager}
   */
  private PluginsManager    pluginsManager;
  /**
   * Reference to service implementation of {@link MembershipHandler}
   */
  private MembershipHandler membershipHandler;

  /**
   * {@inheritDoc}
   */
  @Override
  public void configure() throws Exception
  {
    // The route is dynamically done with the content of the message from the forge
    from(RouteUtil.FORGE_ROLES_MAPPING_QUEUE).id(RouteUtil.FORGE_ROLES_MAPPING_ROUTE_NAME).process(new Processor()
    {
      @Override
      public void process(final Exchange exchange)
      {
        try
        {
          LOG.info(RouteUtil.formatInputMessage(exchange.getIn()));

          // get the pluginUUID from the header
          final String pluginUUID = (String) exchange.getIn().getHeader(PluginQueueHeader.PLUGIN_UUID_HEADER);

          // get the instanceUUID from the header
          final String instanceUUID = (String) exchange.getIn().getHeader(PluginQueueHeader.INSTANCE_UUID_HEADER);

          // Get the destination queue
          final String queueName = pluginsManager.getPluginMetadataByUUID(pluginUUID).getJMSQueues()
                                                 .getRolesMappingQueue();

          // Get project from project id header
          final String projectId = (String) exchange.getIn().getHeaders().get(PluginQueueHeader.PROJECT_ID_HEADER);

          // unpack the object message which has to be map of roles mapping
          @SuppressWarnings("unchecked") final Map<String, String> rolesMapping = exchange.getIn().getBody(Map.class);

          if (rolesMapping == null)
          {
            throw new PluginRouteException(String.format("Unable to catch body of message with [class=%s]", Map.class));
          }

          // Get the previous memberships
          final Map<String, String> previousMapping = pluginsManager.getPluginService(pluginUUID)
                                                                    .getRolesMapping(instanceUUID);
          final List<Membership> previousMemberships = membershipHandler.getAllToolUserMemberships(projectId,
                                                                                                   previousMapping);

          // Get the memberships with new mapping
              final List<Membership> newMemberships = membershipHandler.getAllToolUserMemberships(projectId,
                  rolesMapping);

              // create a composed object to send on the message
              final PluginRoleInformation pluginRoleInformation = new PluginRoleInformationImpl(
                  previousMemberships, newMemberships, rolesMapping);
              exchange.getOut().setBody(pluginRoleInformation);

              // forward all the headers
              exchange.getOut().setHeaders(exchange.getIn().getHeaders());

              // add the header to put the dynamic queue for plugin
              exchange.getOut().setHeader(PluginQueueHeader.DESTINATION_QUEUE_HEADER, queueName);
            }
            catch (final Exception e)
            {
              LOG.error("an exception occurred during plugin roles mapping processor", e);
            }
          }
        }).routingSlip(header(PluginQueueHeader.DESTINATION_QUEUE_HEADER));
  }

  /**
   * Used by container to inject implementation of {@link PluginsManager}
   * 
   * @param pPluginsManager
   *          the pluginManager to inject
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  /**
   * Used by container to inject implementation of {@link MembershipHandler}
   * 
   * @param pMembershipHandler
   *          the membershipHandler to inject
   */
  public void setMembershipHandler(final MembershipHandler pMembershipHandler)
  {
    membershipHandler = pMembershipHandler;
  }

}
