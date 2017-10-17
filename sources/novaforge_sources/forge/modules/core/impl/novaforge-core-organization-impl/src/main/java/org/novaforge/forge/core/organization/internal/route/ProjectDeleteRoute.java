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
package org.novaforge.forge.core.organization.internal.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.dao.NodeDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.dao.RoleDAO;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.route.OrganizationQueueHeader;
import org.novaforge.forge.core.organization.route.OrganizationQueueName;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.core.security.authorization.PermissionHandler;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Set;

/**
 * @author Lamirand Guillaume
 */
public class ProjectDeleteRoute extends RouteBuilder
{

  /**
   * Logger component
   */
  private static final Log   LOGGER = LogFactory.getLog(ProjectDeleteRoute.class);
  /**
   * Reference to {@link ProjectDAO} service injected by the container
   */
  private ProjectDAO         projectDAO;
  /**
   * Reference to {@link NodeDAO} service injected by the container
   */
  private NodeDAO            nodeDAO;
  /**
   * Reference to {@link ApplicationService} service injected by the container
   */
  private ApplicationService applicationService;
  /**
   * Reference to {@link MembershipDAO} service injected by the container
   */
  private MembershipDAO      membershipDAO;
  /**
   * Reference to {@link RoleDAO} service injected by the container
   */
  private RoleDAO            roleDAO;
  /**
   * Reference to {@link PluginsManager} service injected by the container
   */
  private PluginsManager     pluginsManager;
  /**
   * Reference to {@link PermissionHandler} service injected by the container
   */
  private PermissionHandler  permissionHandler;

  /**
   * {@inheritDoc}
   */
  @Override
  public void configure() throws Exception
  {
    onException(Exception.class).process(new Processor()
    {

      /**
       * {@inheritDoc}
       */
      @Override
      public void process(final Exchange pExchange) throws Exception
      {
        // Get project Id
        final String projectId = (String) pExchange.getIn().getHeader(
            OrganizationQueueHeader.PROJECT_ID_HEADER);
        try
        {
          final Project project = projectDAO.findByProjectId(projectId);
          project.setStatus(ProjectStatus.DELETE_ON_ERROR);
          projectDAO.update(project);
        }
        catch (final NoResultException e)
        {
          LOGGER.error(String.format(
              "An error occured when deleting project, but we cannot update its status [project_id=%s]",
              projectId), e);
        }

      }
    });
    from(OrganizationQueueName.PROJECT_DELETE_QUEUE).id(OrganizationQueueName.PROJECT_DELETE_ROUTE_NAME)
        .process(new Processor()
        {
          /**
           * {@inheritDoc}
           */
          @Override
          public void process(final Exchange pExchange) throws Exception
          {

            final String projectId = (String) pExchange.getIn().getHeader(
                OrganizationQueueHeader.PROJECT_ID_HEADER);

            final List<ProjectApplication> applications = applicationService
                .getAllProjectApplications(projectId);
            pExchange.getProperties().put(OrganizationQueueHeader.APPLICATIONS_NUMBER, applications.size());

            pExchange.getOut().setHeaders(pExchange.getIn().getHeaders());

          }
        }).dynamicRouter(method(ApplicationsDynamicRouter.class)).delay(300).process(new Processor()
        {

          /**
           * {@inheritDoc}
           */
          @Override
          public void process(final Exchange pExchange) throws Exception
          {

            // Get project Id
            final String projectId = (String) pExchange.getIn().getHeader(
                OrganizationQueueHeader.PROJECT_ID_HEADER);

            // Get applications left
            List<ProjectApplication> applicationsLeft = applicationService
                .getAllProjectApplications(projectId);
            if ((applicationsLeft == null) || (applicationsLeft.isEmpty()))
            {
              deleteProject(projectId);
            }
            else
            {
              boolean noMoreApp = false;
              while (!noMoreApp)
              {
                if ((applicationsLeft != null) && (!applicationsLeft.isEmpty()))
                {
                  for (final ProjectApplication projectApplication : applicationsLeft)
                  {
                    if ((ApplicationStatus.DELETE_ON_ERROR.equals(projectApplication.getStatus()))
                        || (ApplicationStatus.ACTIVE.equals(projectApplication.getStatus())))
                    {
                      throw new ProjectServiceException(
                          String
                              .format(
                                  "Unable to delete project because an application has thrown an error [instance=%s]",
                                  projectApplication.getPluginInstanceUUID()));
                    }
                    else
                    {
                      break;
                    }
                  }
                  applicationsLeft = applicationService.getAllProjectApplications(projectId);
                }
                else
                {
                  noMoreApp = true;
                }
              }
              deleteProject(projectId);
            }
          }

    });

    from(OrganizationQueueName.PROJECT_DELETE_APPLICATION_QUEUE).id(
        OrganizationQueueName.PROJECT_DELETE_APPLICATION_ROUTE_NAME).process(new Processor()
    {

      /**
       * {@inheritDoc}
       */
      @Override
      public void process(final Exchange pExchange) throws Exception
      {
        // Get author
        final String author = (String) pExchange.getIn().getHeader(OrganizationQueueHeader.USER_NAME_HEADER);

        // Get project Id
        final String projectId = (String) pExchange.getIn().getHeader(
            OrganizationQueueHeader.PROJECT_ID_HEADER);

        // Get applications left
        final List<ProjectApplication> applications = applicationService.getAllProjectApplications(projectId);
        if (applications != null)
        {
          for (final ProjectApplication applicationToDelete : applications)
          {
            if (ApplicationStatus.ACTIVE.equals(applicationToDelete.getStatus()))
            {
              final boolean availablePlugin = pluginsManager.isAvailablePlugin(applicationToDelete
                  .getPluginUUID().toString());
              if (availablePlugin)
              {
                applicationService.deleteApplication(projectId, applicationToDelete.getUri(), author);
              }
              break;
            }
          }
        }
      }

    });
  }

  /**
   * Delete the persisted objects
   * 
   * @param pProjectId
   *          the id
   */
  private void deleteProject(final String pProjectId)
  {
    // delete the project
    final Project project = projectDAO.findByProjectId(pProjectId);

    // delete all the memberships linked to the project
    final List<Membership> memberships = membershipDAO.findByProject(pProjectId);
    for (final Membership membership : memberships)
    {
      membershipDAO.delete(membership);
    }

    // Delete the project
    projectDAO.delete(project);

    // Delete all permissions
    final Set<String> permissions = permissionHandler.buildProjectPermissions(project.getProjectId(),
        PermissionAction.values());
    for (final String name : permissions)
    {
      final Permission findByName = roleDAO.findByName(name);
      roleDAO.delete(findByName);
    }
  }

  /**
   * Use by container to inject {@link ProjectDAO} implementation
   * 
   * @param pProjectDAO
   *          the projectDAO to set
   */
  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }

  /**
   * Use by container to inject {@link NodeDAO} implementation
   * 
   * @param pNodeDAO
   *          the nodeDAO to set
   */
  public void setNodeDAO(final NodeDAO pNodeDAO)
  {
    nodeDAO = pNodeDAO;
  }

  /**
   * Use by container to inject {@link ApplicationService} implementation
   * 
   * @param pApplicationService
   *          the applicationService to set
   */
  public void setApplicationService(final ApplicationService pApplicationService)
  {
    applicationService = pApplicationService;
  }

  /**
   * Use by container to inject {@link MembershipDAO} implementation
   * 
   * @param pMembershipDAO
   *          the membershipDAO to set
   */
  public void setMembershipDAO(final MembershipDAO pMembershipDAO)
  {
    membershipDAO = pMembershipDAO;
  }

  /**
   * Use by container to inject {@link PermissionHandler} implementation
   * 
   * @param pPermissionHandler
   *          the authorizationHandler to set
   */
  public void setPermissionHandler(final PermissionHandler pPermissionHandler)
  {
    permissionHandler = pPermissionHandler;
  }

  /**
   * Use by container to inject {@link PluginsManager} implementation
   * 
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  /**
   * Use by container to inject {@link RoleDAO} implementation
   * 
   * @param pRoleDAO
   *          the roleDAO to set
   */
  public void setRoleDAO(final RoleDAO pRoleDAO)
  {
    roleDAO = pRoleDAO;
  }

}
