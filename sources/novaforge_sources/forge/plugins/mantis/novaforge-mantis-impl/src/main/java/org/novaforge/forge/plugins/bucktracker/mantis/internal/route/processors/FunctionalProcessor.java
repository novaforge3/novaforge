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
package org.novaforge.forge.plugins.bucktracker.mantis.internal.route.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerAction;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.plugins.bucktracker.mantis.services.MantisFunctionalService;
import org.novaforge.forge.plugins.commons.utils.route.RouteUtil;

import java.io.Serializable;

/**
 * @author sbenoist
 */
public class FunctionalProcessor implements Processor
{
	private final Log                     logger = LogFactory.getLog(this.getClass());

	private final MantisFunctionalService mantisFunctionalService;

	public FunctionalProcessor(final MantisFunctionalService pMantisFunctionalService)
	{
		super();
		mantisFunctionalService = pMantisFunctionalService;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void process(final Exchange pExchange)
	{
		final BugTrackerAction action = BugTrackerAction.valueOf((String) pExchange.getIn().getHeaders()
		    .get(PluginQueueHeader.ACTION_HEADER));
		try
		{
			logger.info("Mantis - Get a functional message.");

			final String forgeId = (String) pExchange.getIn().getHeader(PluginQueueHeader.FORGE_ID_HEADER);
			final String instanceId = (String) pExchange.getIn().getHeader(PluginQueueHeader.INSTANCE_UUID_HEADER);
			final String userName = (String) pExchange.getIn().getHeader(PluginQueueHeader.USER_NAME_HEADER);

			switch (action)
			{
				case createBug:
					final BugTrackerIssueBean bean = pExchange.getIn().getBody(BugTrackerIssueBean.class);
					mantisFunctionalService.createBug(userName, forgeId, instanceId, bean);
					break;
			}

			// send the success response
			pExchange.getOut().setHeader(PluginQueueHeader.STATUS_RESPONSE_HEADER, PluginQueueHeader.STATUS_OK);
		}
		catch (final Exception e)
		{
			// build the response body with the requested object and the exception
			RouteUtil.buildErrorMessage(pExchange, e, (Serializable) pExchange.getIn().getBody());
			logger.error(String.format("Unable to execute action requested with [action=%s]", action.name()), e);
		}
	}

}
