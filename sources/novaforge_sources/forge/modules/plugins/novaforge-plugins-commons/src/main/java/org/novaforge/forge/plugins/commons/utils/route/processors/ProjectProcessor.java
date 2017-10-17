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
import org.novaforge.forge.core.plugins.domain.plugin.PluginInstance;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProjectInformation;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.domains.PluginInstanceImpl;
import org.novaforge.forge.plugins.commons.utils.route.RouteUtil;

import java.io.Serializable;

/**
 * @author sbenoist
 */
public class ProjectProcessor implements Processor
{
  private static final Log           LOG = LogFactory.getLog(ProjectProcessor.class);

  private final PluginProjectService pluginProjectService;

  private final String               pluginQueueName;

  public ProjectProcessor(final String pPluginQueueName, final PluginProjectService pPluginProjectService)
  {
    super();
    pluginProjectService = pPluginProjectService;
    pluginQueueName = pPluginQueueName;
  }

  /**
   * @inheritDoc
   */
  @Override
  public void process(final Exchange pExchange)
  {
    // the exceptions are caught via the body response
    try
    {
      LOG.info(pluginQueueName + "-Get a message.");

      // Get action header
      final PluginQueueAction action = PluginQueueAction.fromLabel((String) pExchange.getIn().getHeaders()
          .get(PluginQueueHeader.ACTION_HEADER));

      // get the informations about the project into the header
      final String instanceId = (String) pExchange.getIn().getHeader(PluginQueueHeader.INSTANCE_UUID_HEADER);
      final String instanceLabel = (String) pExchange.getIn().getHeader(
          PluginQueueHeader.INSTANCE_LABEL_HEADER);
      final String forgeId = (String) pExchange.getIn().getHeaders().get(PluginQueueHeader.FORGE_ID_HEADER);

      final String projectId = (String) pExchange.getIn().getHeaders()
          .get(PluginQueueHeader.PROJECT_ID_HEADER);

      final String toolID = (String) pExchange.getIn().getHeaders()
          .get(PluginQueueHeader.TOOL_INSTANCE_UUID_HEADER);

      switch (action)
      {
        case CREATE:
          final PluginProjectInformation pluginProjectInformation = pExchange.getIn().getBody(
              PluginProjectInformation.class);
          // set the pluginInstance
          final PluginInstance pluginInstance = new PluginInstanceImpl(instanceId, instanceLabel);
          pluginInstance.setForgeId(forgeId);
          pluginInstance.setForgeProjectId(projectId);
          pluginInstance.setToolInstanceId(toolID);

          // create the project
          pluginProjectService.createProject(pluginInstance, pluginProjectInformation);
          break;
        case UPDATE:
          final PluginProject pluginProject = pExchange.getIn().getBody(PluginProject.class);

          // update the project
          pluginProjectService.updateProject(instanceId, pluginProject);
          break;
        case DELETE:
          // delete the project
          pluginProjectService.deleteProject(instanceId);
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
      RouteUtil.buildErrorMessage(pExchange, e, (Serializable) pExchange.getIn().getBody());
      LOG.error("an error occured during project propagation", e);
    }
  }

}
