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
import org.novaforge.commons.route.domain.PluginProjectImpl;
import org.novaforge.commons.route.domain.PluginProjectInformationImpl;
import org.novaforge.commons.route.util.RouteUtil;
import org.novaforge.forge.core.organization.handlers.MembershipHandler;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProjectInformation;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.services.PluginsManager;

import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 * @date Mar 2, 2011
 */
public class PluginProjectRoute extends RouteBuilder
{
  private static final Log LOG = LogFactory.getLog(PluginProjectRoute.class);
  private PluginsManager     pluginsManager;
  private ProjectService     projectService;
  private ApplicationService applicationService;
  private MembershipHandler  membershipHandler;

  @Override
  public void configure() throws Exception
  {
    // The route is dynamically done with the content of the message from
    // the forge
    from(RouteUtil.FORGE_PROJECT_QUEUE).id(RouteUtil.FORGE_PROJECT_ROUTE_NAME).process(new Processor()
    {
      @Override
      public void process(final Exchange exchange) throws Exception
      {
        LOG.info(RouteUtil.formatInputMessage(exchange.getIn()));
        final String instanceId = (String) exchange.getIn().getHeader(PluginQueueHeader.INSTANCE_UUID_HEADER);

        // get the pluginUUID from the header
        final String pluginUUID = (String) exchange.getIn().getHeader(PluginQueueHeader.PLUGIN_UUID_HEADER);

        // get the author from the header
        final String author = (String) exchange.getIn().getHeader(PluginQueueHeader.USER_NAME_HEADER);

        // Get action header
        final PluginQueueAction action = PluginQueueAction.fromLabel((String) exchange.getIn().getHeaders()
            .get(PluginQueueHeader.ACTION_HEADER));

        try
        {

          // Get the destination queue
          final String queueName = pluginsManager.getPluginMetadataByUUID(pluginUUID).getJMSQueues()
              .getProjectQueue();

          switch (action)
          {
            case CREATE:
              // Get roles mapping in object message original
              @SuppressWarnings("unchecked")
              final Map<String, String> rolesMapping = exchange.getIn().getBody(Map.class);
              rolesMapping.entrySet();

              // Get project from project id header
              final String projectId = (String) exchange.getIn().getHeaders()
                  .get(PluginQueueHeader.PROJECT_ID_HEADER);
              final Project projectToCreate = projectService.getProject(projectId, null);

              // Get the memberships
              final List<Membership> memberships = membershipHandler.getAllToolUserMemberships(
                  projectToCreate.getProjectId(), rolesMapping);

              // transform the Project obj into ProjectPlugin obj and
              // add it to the out message
              exchange.getOut()
                  .setBody(toPluginForCreate(projectToCreate, memberships, rolesMapping, author));
              break;

            case UPDATE:
              // get project object directly in source message
              final Project projectToUpdated = exchange.getIn().getBody(Project.class);

              // transform the Project obj into ProjectPlugin obj and
              // add it to the out message
              exchange.getOut().setBody(toPluginForUpdate(projectToUpdated, author));
              break;

            case DELETE:
              // There is nothing to do in the case of delete
              break;
          }

          // forward all the headers
          exchange.getOut().setHeaders(exchange.getIn().getHeaders());

          // add the header to put the dynamic queue for plugin
          exchange.getOut().setHeader(PluginQueueHeader.DESTINATION_QUEUE_HEADER, queueName);
        }
        catch (final Exception e)
        {
          LOG.error("an exception occurred during plugin project processor", e);
          // change the application status in case of creation
          if (PluginQueueAction.CREATE.equals(action))
          {
            applicationService.changeApplicationStatus(ApplicationStatus.CREATE_ON_ERROR, instanceId);
          }
        }
      }
    }).routingSlip(header(PluginQueueHeader.DESTINATION_QUEUE_HEADER));
  }

  private PluginProjectInformation toPluginForCreate(final Project pProject,
      final List<Membership> pMemberships, final Map<String, String> pRolesMapping, final String pAuthor)
  {
    return new PluginProjectInformationImpl(pProject, pMemberships, pRolesMapping, pAuthor);
  }

  private PluginProject toPluginForUpdate(final Project pProject, final String pAuthor)
  {
    return new PluginProjectImpl(pProject, pAuthor);
  }

  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  public void setProjectService(final ProjectService pProjectService)
  {
    projectService = pProjectService;
  }

  public void setApplicationService(final ApplicationService pApplicationService)
  {
    applicationService = pApplicationService;
  }

  public void setMembershipHandler(final MembershipHandler pMembershipHandler)
  {
    membershipHandler = pMembershipHandler;
  }

}
