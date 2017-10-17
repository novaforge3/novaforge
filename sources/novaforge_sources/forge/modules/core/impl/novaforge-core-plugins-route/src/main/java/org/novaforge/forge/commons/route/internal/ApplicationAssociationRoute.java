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
import org.novaforge.commons.route.util.RouteUtil;
import org.novaforge.forge.core.organization.exceptions.CompositionServiceException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.organization.services.CompositionService;
import org.novaforge.forge.core.plugins.categories.AssociationType;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.services.PluginArtefactFactory;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lamirang
 */
public class ApplicationAssociationRoute extends RouteBuilder
{
  private final static Log LOG = LogFactory.getLog(ApplicationAssociationRoute.class);
  private PluginsManager        pluginsManager;
  private CompositionService    compositionService;
  private PluginArtefactFactory pluginArtefactFactory;

  /**
   * {@inheritDoc}
   */
  @Override
  public void configure() throws Exception
  {
    // The route is dynamically done with the content of the message from the forge
    this.from(RouteUtil.FORGE_COMPOSITION_QUEUE).id(RouteUtil.FORGE_COMPOSITION_ROUTE_NAME)
        .process(new Processor()
        {
          @Override
          public void process(final Exchange exchange)
          {
            try
            {
              LOG.info(RouteUtil.formatInputMessage(exchange.getIn()));

              // Get project from project id header
              final String projectId = (String) exchange.getIn().getHeaders()
                  .get(PluginQueueHeader.PROJECT_ID_HEADER);
              // get the instanceUUID from the header
              final String instanceId = (String) exchange.getIn().getHeader(
                  PluginQueueHeader.INSTANCE_UUID_HEADER);
              // get the event from the header
              final String event = (String) exchange.getIn().getHeader(PluginQueueHeader.EVENT_SOURCE_HEADER);
              // get the type from the header
              final String type = (String) exchange.getIn().getHeader(PluginQueueHeader.TYPE_SOURCE_HEADER);
              CompositionType compositionType;
              if (AssociationType.NOTIFICATION.name().equals(type))
              {
                compositionType = CompositionType.NOTIFICATION;
              }
              else if (AssociationType.SEND.name().equals(type))
              {
                compositionType = CompositionType.SEND_DATA;
              }
              else
              {
                throw new CompositionServiceException(ExceptionCode.ERR_ASSOCIATION_TYPE_NOT_SUPPORTED, String
                                                                                                            .format("The type of the source event [%s] is not supported",
                                                                                                                    type));
              }
              // Get composition list from source application and event
              final List<Composition> comps = compositionService.getCompositionFromSource(projectId,
                  instanceId, compositionType, event);
              final List<Composition> compositions = new ArrayList<Composition>();
              for (final Composition composition : comps)
              {
                if (composition.isActivated())
                {
                  compositions.add(composition);
                }
              }
              exchange.getOut().setBody(compositions);

              // forward all the headers
              exchange.getOut().setHeaders(exchange.getIn().getHeaders());

              // Set source body as header
              final Object body = exchange.getIn().getBody();
              exchange.getOut().setHeader(PluginQueueHeader.OBJECT_SOURCE_HEADER, body);
            }
            catch (final Exception e)
            {
              LOG.error("an exception occurred during application association processor", e);
            }

          }
        }).split(this.body(List.class)).process(new Processor()
        {
          @Override
          public void process(final Exchange exchange)
          {
            try
            {
              // Getting composition object defined by split EIP
              final Composition composition = exchange.getIn().getBody(Composition.class);

              // Getting source object
              final Object sourceObject = exchange.getIn().getHeader(PluginQueueHeader.OBJECT_SOURCE_HEADER);

              // Getting source plugin metadata
              final String sourceUUId = composition.getSource().getPluginUUID().toString();
              final PluginMetadata source = pluginsManager.getPluginMetadataByUUID(sourceUUId);

              // Getting target plugin metadata
              final String targetUUID = composition.getTarget().getPluginUUID().toString();
              final PluginMetadata target = pluginsManager.getPluginMetadataByUUID(targetUUID);

              // Building target object
              if (CompositionType.NOTIFICATION.equals(composition.getType()))
              {
                final Object targetObject = pluginArtefactFactory.buildTargetArtefact(source.getCategory(),
                    composition.getSourceName(), sourceObject, target.getCategory(),
                    composition.getTargetName(), composition.getTemplate());
                exchange.getOut().setBody(targetObject);
              }
              else
              {
                exchange.getOut().setBody(sourceObject);

              }

              // Adding the target header
              // action requested
              exchange.getOut().setHeader(PluginQueueHeader.ACTION_HEADER, composition.getTargetName());
              // instance uuid
              exchange.getOut().setHeader(PluginQueueHeader.INSTANCE_UUID_HEADER,
                  composition.getTarget().getPluginInstanceUUID().toString());
              // UserName
              final String userName = (String) exchange.getIn().getHeader(PluginQueueHeader.USER_NAME_HEADER);
              exchange.getOut().setHeader(PluginQueueHeader.USER_NAME_HEADER, userName);
              // Forge ID
              final String forgeId = (String) exchange.getIn().getHeader(PluginQueueHeader.FORGE_ID_HEADER);
              exchange.getOut().setHeader(PluginQueueHeader.FORGE_ID_HEADER, forgeId);

              // Get the destination queue
              final PluginService pluginTarget = pluginsManager.getPluginService(targetUUID);
              final String queueName = pluginTarget.getAssociationInfo().getNotificationQueue();

              // add the header to put the dynamic queue for plugin
              exchange.getOut().setHeader(PluginQueueHeader.DESTINATION_QUEUE_HEADER, queueName);
            }
            catch (final Exception e)
            {
              LOG.error("an exception occurred during application association processor", e);
            }

          }
        }).routingSlip(header(PluginQueueHeader.DESTINATION_QUEUE_HEADER));
  }

  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  public void setCompositionService(final CompositionService pCompositionService)
  {
    compositionService = pCompositionService;
  }

  public void setPluginArtefactFactory(final PluginArtefactFactory pPluginArtefactFactory)
  {
    pluginArtefactFactory = pPluginArtefactFactory;
  }

}
