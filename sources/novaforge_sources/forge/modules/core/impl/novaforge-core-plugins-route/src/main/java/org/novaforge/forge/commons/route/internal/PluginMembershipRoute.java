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
import org.novaforge.commons.route.domain.PluginMembershipImpl;
import org.novaforge.commons.route.domain.PluginUserImpl;
import org.novaforge.commons.route.util.RouteUtil;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.services.PluginsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sbenoist
 * @date Mar 2, 2011
 */
public class PluginMembershipRoute extends RouteBuilder
{
  private static final Log LOG = LogFactory.getLog(PluginMembershipRoute.class);
  private PluginsManager pluginsManager;

  @Override
  public void configure() throws Exception
  {
    // The route is dynamically done with the content of the message from the forge
    from(RouteUtil.FORGE_MEMBERSHIP_QUEUE).id(RouteUtil.FORGE_MEMBERSHIP_ROUTE_NAME).process(new Processor()
    {
      @Override
      public void process(final Exchange exchange)
      {
        try
        {
          LOG.info(RouteUtil.formatInputMessage(exchange.getIn()));

          // get the pluginUUID from the header
          final String pluginUUID = (String) exchange.getIn().getHeader(PluginQueueHeader.PLUGIN_UUID_HEADER);

          // Get the destination queue
          final String queueName = pluginsManager.getPluginMetadataByUUID(pluginUUID).getJMSQueues()
              .getMembershipQueue();

          // unpack the object message which has to be a list of memberships
          // TODO should receive list of membership already filtered?
          // Should we filter the list here in deleting group, and many user membership
          @SuppressWarnings("unchecked")
          final List<Membership> memberships = exchange.getIn().getBody(List.class);

          // transform the Membership obj into PluginMembership obj and add it to the out message
          exchange.getOut().setBody(toPlugin(memberships));

          // forward all the headers
          exchange.getOut().setHeaders(exchange.getIn().getHeaders());

          // add the header to put the dynamic queue for plugin
          exchange.getOut().setHeader(PluginQueueHeader.DESTINATION_QUEUE_HEADER, queueName);
        }
        catch (final Exception e)
        {
          LOG.error("an exception occurred during plugin membership processor", e);
        }
      }
    }).routingSlip(header(PluginQueueHeader.DESTINATION_QUEUE_HEADER));
  }

  private List<PluginMembership> toPlugin(final List<Membership> pMemberships)
  {
    final List<PluginMembership> pluginMemberships = new ArrayList<PluginMembership>();
    for (final Membership membership : pMemberships)
    {
      final Actor actor = membership.getActor();
      if (actor instanceof User)
      {
        final PluginUser pluginUser = new PluginUserImpl((User) actor);
        final PluginMembership pluginMembershipImpl = new PluginMembershipImpl(pluginUser, membership
            .getRole().getName());
        pluginMemberships.add(pluginMembershipImpl);
      }
    }
    return pluginMemberships;
  }

  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

}
