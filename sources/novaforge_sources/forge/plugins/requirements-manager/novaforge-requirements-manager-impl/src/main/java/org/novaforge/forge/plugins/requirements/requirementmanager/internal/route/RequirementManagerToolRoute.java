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
package org.novaforge.forge.plugins.requirements.requirementmanager.internal.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.plugins.requirements.requirementmanager.internal.handlers.RequirementManagerTestHandler;
import org.novaforge.forge.plugins.requirements.requirementmanager.route.RequirementManagerQueueName;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementConfigurationService;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementManagerCodeHandler;

import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public class RequirementManagerToolRoute extends RouteBuilder
{
	private static final Log log = LogFactory.getLog(RequirementManagerToolRoute.class);
	private RequirementConfigurationService requirementConfigurationService;
	private RequirementManagerTestHandler requirementManagerTestHandler;
	private List<RequirementManagerCodeHandler> requirementManagerCodeHandler;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure() throws Exception
	{
		from(RequirementManagerQueueName.REQUIREMENT_TEST_IMPORT_TOPIC_FULL_NAME).id(
				RequirementManagerQueueName.REQUIREMENT_TEST_IMPORT_ROUTE).process(new Processor()
		{
			@Override
			public void process(final Exchange pExchange) throws Exception
			{
				// Get the tool informations
				@SuppressWarnings("unchecked")
				final Map<String, String> map = pExchange.getIn().getBody(Map.class);
				String projectId = map.get("projectId");
				String currentUser = map.get("currentUser");

				log.info(String
						.format(
								"a message requesting importing tests from testlink has been received for projectID=%s and login=%s",
								projectId, currentUser));
				requirementManagerTestHandler.importTests(projectId, currentUser);
			}
		});

		from(RequirementManagerQueueName.REQUIREMENT_TEST_EXPORT_TOPIC_FULL_NAME).id(
				RequirementManagerQueueName.REQUIREMENT_TEST_EXPORT_ROUTE).process(new Processor()
		{
			@Override
			public void process(final Exchange pExchange) throws Exception
			{
				// Get the tool informations
				@SuppressWarnings("unchecked")
				final Map<String, String> map = pExchange.getIn().getBody(Map.class);
				String projectId = map.get("projectId");
				String currentUser = map.get("currentUser");

				log.info(String
						.format(
								"a message requesting exporting requirements to testlink has been received for projectID=%s and login=%s",
								projectId, currentUser));
				requirementManagerTestHandler.exportRequirements(projectId, currentUser);
			}
		});

		from(RequirementManagerQueueName.REQUIREMENT_SOURCES_SYNCHRO_TOPIC_FULL_NAME).id(
				RequirementManagerQueueName.REQUIREMENT_SOURCES_SYNCHRO_ROUTE).process(new Processor()
		{
			@Override
			public void process(final Exchange pExchange) throws Exception
			{
				// Get the tool informations
				@SuppressWarnings("unchecked")
				final Map<String, String> map = pExchange.getIn().getBody(Map.class);
				String projectId = map.get("projectId");
				String codeRepositoryPath = map.get("codeRepositoryPath");
				String currentUser = map.get("currentUser");
				String enumClassName = map.get("enumClassName");

				log.info(String
						.format(
								"a message requesting getting code resources from svn has been received for projectId=%s, login=%s, enumClass=%s",
								projectId, currentUser, enumClassName));
				getRequirementManagerCodeHandler().getCodeResources(projectId, codeRepositoryPath, currentUser, enumClassName);
			}
			private RequirementManagerCodeHandler getRequirementManagerCodeHandler() {
				RequirementManagerCodeHandler result = null;
				for (RequirementManagerCodeHandler rmch : requirementManagerCodeHandler) {
					String type = requirementConfigurationService.getCodeHandlerType();
					if(type.equals(rmch.getType()))
					{
						result = rmch;
						// We found the correct code handler => stop searching
						break;
					}
					else if(type.equals("*"))
					{
						result = rmch;
						// using default code handler, search continue to find the good one
					}
				}
				return result;
			}
		});
	}

	public void setRequirementConfigurationService(
			final RequirementConfigurationService pRequirementConfigurationService)
	{
		requirementConfigurationService = pRequirementConfigurationService;
	}

	public void setRequirementManagerTestHandler(
			final RequirementManagerTestHandler pRequirementManagerTestHandler)
	{
		requirementManagerTestHandler = pRequirementManagerTestHandler;
	}

	public void setRequirementManagerCodeHandler(
			final List<RequirementManagerCodeHandler> pRequirementManagerCodeHandler)
	{
		requirementManagerCodeHandler = pRequirementManagerCodeHandler;
	}

}
