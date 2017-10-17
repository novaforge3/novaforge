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
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.utils.route.RouteUtil;

import java.io.Serializable;
import java.util.List;

/**
 * @author sbenoist
 */
public class MembershipProcessor implements Processor
{
  private static final Log              LOG = LogFactory.getLog(MembershipProcessor.class);

  private final PluginMembershipService pluginMembershipService;

  private final String                  pluginQueueName;

  public MembershipProcessor(final String pPluginQueueName,
      final PluginMembershipService pPluginMembershipService)
  {
    super();
    pluginMembershipService = pPluginMembershipService;
    pluginQueueName = pPluginQueueName;
  }

  /**
   * @inheritDoc
   */
  @SuppressWarnings("unchecked")
  @Override
  public void process(final Exchange pExchange)
  {
    List<PluginMembership> memberships = null;
    try
    {
      LOG.info(pluginQueueName + "-Get a message.");

      // Get action header
      final PluginQueueAction action = PluginQueueAction.fromLabel((String) pExchange.getIn().getHeaders()
          .get(PluginQueueHeader.ACTION_HEADER));

      // get the informations about the project into the header
      final String instanceId = (String) pExchange.getIn().getHeader(PluginQueueHeader.INSTANCE_UUID_HEADER);

      // get the list of memberships
      memberships = pExchange.getIn().getBody(List.class);

      switch (action)
      {
        case CREATE:
          pluginMembershipService.addUserMemberships(instanceId, memberships);
          break;
        case UPDATE:
          pluginMembershipService.updateUserMemberships(instanceId, memberships);
          break;
        case DELETE:
          pluginMembershipService.removeUserMemberships(instanceId, memberships);
          break;
      }

      // forward all the headers from the input
      pExchange.getOut().setHeaders(pExchange.getIn().getHeaders());

      // send the success response
      pExchange.getOut().setHeader(PluginQueueHeader.STATUS_RESPONSE_HEADER, PluginQueueHeader.STATUS_OK);
    }
    catch (final Exception e)
    {
      // build the response body with the requested object and the exception
      RouteUtil.buildErrorMessage(pExchange, e, (Serializable) memberships);
      LOG.error("an error occured during memberships propagation", e);
    }
  }
}
