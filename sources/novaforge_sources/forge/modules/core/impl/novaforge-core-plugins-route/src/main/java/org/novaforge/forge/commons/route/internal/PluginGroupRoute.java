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
import org.novaforge.commons.route.domain.PluginGroupImpl;
import org.novaforge.commons.route.util.RouteUtil;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.plugin.PluginGroup;
import org.novaforge.forge.core.plugins.domain.route.OptionalPluginGroupQueue;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.domain.route.PluginQueues;
import org.novaforge.forge.core.plugins.exceptions.PluginRouteException;
import org.novaforge.forge.core.plugins.services.PluginsManager;

/**
 * @author sbenoist
 */
public class PluginGroupRoute extends RouteBuilder
{

  private static final Log LOG = LogFactory.getLog(PluginGroupRoute.class);
  private PluginsManager pluginsManager;

  @Override
  public void configure() throws Exception
  {
    // The recipient is dynamically determined with the content of the header message from the forge
    from(RouteUtil.FORGE_GROUP_QUEUE).id(RouteUtil.FORGE_GROUP_ROUTE_NAME).process(new Processor()
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
          final PluginMetadata pluginMetadataByUUID = pluginsManager.getPluginMetadataByUUID(pluginUUID);
          if (pluginMetadataByUUID != null)
          {
            final PluginQueues pluginQueues = pluginMetadataByUUID.getJMSQueues();
            if (pluginQueues instanceof OptionalPluginGroupQueue)
            {
              final String queueName = ((OptionalPluginGroupQueue) pluginQueues).getGroupQueue();

              // Unpack the object message regarding action header
              final Group group = exchange.getIn().getBody(Group.class);
              if (group == null)
              {
                throw new PluginRouteException(String.format(
                    "Unable to catch body of message with [class=%s]", Group.class));
              }
              exchange.getOut().setBody(toPlugin(group));

              // forward all the headers
              exchange.getOut().setHeaders(exchange.getIn().getHeaders());

              // add the header to put the dynamic queue for plugin
              exchange.getOut().setHeader(PluginQueueHeader.DESTINATION_QUEUE_HEADER, queueName);
            }
          }
        }
        catch (final Exception e)
        {
          LOG.error("an exception occurred during plugin group processor", e);
        }
      }
    }).routingSlip(header(PluginQueueHeader.DESTINATION_QUEUE_HEADER));
  }

  private PluginGroup toPlugin(final Group pGroup)
  {
    return new PluginGroupImpl(pGroup);
  }

  public PluginsManager getPluginsManager()
  {
    return pluginsManager;
  }

  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

}
