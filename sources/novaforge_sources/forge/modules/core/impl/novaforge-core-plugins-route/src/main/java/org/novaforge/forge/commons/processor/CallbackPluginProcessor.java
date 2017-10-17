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
package org.novaforge.forge.commons.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.plugins.domain.plugin.PluginErrorMessage;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;

import java.io.Serializable;

/**
 * @author sbenoist
 */
public class CallbackPluginProcessor implements Processor
{
  private final HistorizationService historizationService;

  private final EventType eventType;

  public CallbackPluginProcessor(final HistorizationService pHistorizationService, final EventType pEventType)
  {
    super();
    historizationService = pHistorizationService;
    eventType = pEventType;
  }

  @Override
  public void process(final Exchange pExchange) throws Exception
  {
    // FIXME This has been commented...don't know why
    //    final String response = (String) pExchange.getIn().getHeader(PluginQueueHeader.STATUS_RESPONSE_HEADER);
    //    pExchange.getIn().getHeader(PluginQueueHeader.USER_NAME_HEADER);
    //    if (PluginQueueHeader.STATUS_KO.equals(response))
    //    {
    //       historize the error
    //       historizationService.registerEvent(actor, eventType, EventLevel.ERROR, format(pExchange));
    //    }
  }

  private String format(final Exchange pExchange)
  {
    final StringBuilder sb = new StringBuilder();

    final String action           = (String) pExchange.getIn().getHeader(PluginQueueHeader.ACTION_HEADER);
    final String instanceId       = (String) pExchange.getIn().getHeader(PluginQueueHeader.INSTANCE_UUID_HEADER);
    final String pluginUUID       = (String) pExchange.getIn().getHeader(PluginQueueHeader.PLUGIN_UUID_HEADER);
    final String destinationQueue = (String) pExchange.getIn().getHeader(PluginQueueHeader.DESTINATION_QUEUE_HEADER);
    final String projectID        = (String) pExchange.getIn().getHeader(PluginQueueHeader.PROJECT_ID_HEADER);

    final PluginErrorMessage errorMessage = pExchange.getIn().getBody(PluginErrorMessage.class);

    sb.append("the following exception(s) was caught : ");
    Throwable e = errorMessage.getResponseException();
    if (e.getMessage() != null)
    {
      sb.append("[message = ").append(e.getMessage()).append(", ");
    }

    while (e.getCause() != null)
    {
      if (e.getCause().getMessage() != null)
      {
        sb.append("caused by = ").append(e.getCause().getMessage()).append("]");
      }

      e = e.getCause();
    }

    sb.append("with the generic informations : ");
    sb.append("[pluginUUID = ").append(pluginUUID).append(", ");
    sb.append("recipient = ").append(destinationQueue).append(", ");
    sb.append("action = ").append(action).append(", ");
    sb.append("instanceUUID = ").append(instanceId).append(", ");
    sb.append("projectID = ").append(projectID).append("]");

    final Serializable requestObject = errorMessage.getRequestObject();
    if (requestObject instanceof PluginUser)
    {
      final PluginUser user = (PluginUser) requestObject;
      sb.append("with the specific informations : ");
      sb.append("[user login = ").append(user.getLogin()).append("]");
    }

    return sb.toString();
  }

}
