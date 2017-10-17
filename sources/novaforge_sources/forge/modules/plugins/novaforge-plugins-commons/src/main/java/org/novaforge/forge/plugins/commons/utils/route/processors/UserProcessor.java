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
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.utils.route.RouteUtil;

/**
 * @author sbenoist
 */
public class UserProcessor implements Processor
{
	private static final Log        LOG = LogFactory.getLog(UserProcessor.class);

	private final PluginUserService pluginUserService;

	private final String            pluginQueueName;

	public UserProcessor(final String pPluginQueueName, final PluginUserService pPluginUserService)
	{
		super();
		pluginUserService = pPluginUserService;
		pluginQueueName = pPluginQueueName;
	}

	@Override
	public void process(final Exchange pExchange)
	{
		PluginUser user = null;
		try
		{
			LOG.info(pluginQueueName + "-Get a message.");

			// get the informations about the instance into the header
			final String instanceId = (String) pExchange.getIn().getHeader(PluginQueueHeader.INSTANCE_UUID_HEADER);

			final PluginQueueAction action = PluginQueueAction.fromLabel((String) pExchange.getIn().getHeader(
			    PluginQueueHeader.ACTION_HEADER));

			// get user in body of message
			user = pExchange.getIn().getBody(PluginUser.class);
			switch (action)
			{
				case CREATE:
					// This case is not possible, because user are created with a specific role, so we create a
					// membership directly.
					break;
				case UPDATE:
					// get username of user
					final String username = (String) pExchange.getIn().getHeader(PluginQueueHeader.USER_NAME_HEADER);
					pluginUserService.updateUser(instanceId, username, user);
					break;
				case DELETE:
					pluginUserService.removeUser(instanceId, user);
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
			RouteUtil.buildErrorMessage(pExchange, e, user);
			LOG.error("an error occured during user propagation", e);
		}
	}

}
