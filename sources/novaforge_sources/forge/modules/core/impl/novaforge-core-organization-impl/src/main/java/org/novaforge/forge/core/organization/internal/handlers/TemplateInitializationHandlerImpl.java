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
package org.novaforge.forge.core.organization.internal.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateApplication;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.ProjectRoleService;
import org.novaforge.forge.core.organization.services.SpaceService;
import org.novaforge.forge.core.organization.services.TemplateInitializationHandler;
import org.novaforge.forge.core.organization.services.TemplateNodeService;
import org.novaforge.forge.core.organization.services.TemplateRoleService;
import org.novaforge.forge.core.organization.services.TemplateService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Guillaume Lamirand
 */
public class TemplateInitializationHandlerImpl implements TemplateInitializationHandler
{

  private static final Log LOGGER = LogFactory.getLog(TemplateInitializationHandlerImpl.class);
  private ProjectDAO          projectDAO;
  private TemplateService     templateService;
  private TemplateRoleService templateRoleService;
  private TemplateNodeService templateNodeService;
  private ProjectRoleService  projectRoleService;
  private SpaceService        spaceService;
  private ApplicationService  applicationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void instantiateProjectFromTemplate(final String pProjectId) throws TemplateServiceException
  {
    final Template template = templateService.getTemplateForProject(pProjectId);
    if (template != null)
    {
      final String templateId = template.getElementId();

      // execute the template instanciation on a new thread because it can takes more than transaction
      // timeout and it doesn't need to be rolledback if errors occurs
      final ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.execute(new Runnable()
      {
        @Override
        public void run()
        {
          try
          {
            LOGGER.debug(String.format("create the roles from template with [project_id=%s, template_id=%s]",
                pProjectId, templateId));
            // Create the roles
            createRoles(pProjectId, templateId);

            LOGGER.debug(String.format("create the nodes from template with [project_id=%s, template_id=%s]",
                pProjectId, templateId));
            // Create the node structure
            createNodes(pProjectId, templateId);
          }
          catch (final Exception e)
          {
            LOGGER.error(String.format(
                "Unable to initialize a project from template with [project_id=%s, template_id=%s]",
                pProjectId, templateId), e);
          }
          finally
          {
            try
            {
              LOGGER.debug(String.format("delete the template instance with [project_id=%s]", pProjectId));
              templateService.deleteTemplateInstance(pProjectId);
            }
            catch (final TemplateServiceException e)
            {
              LOGGER.error(
                  String.format("Unable to delete the template instance with [project_id=%s]", pProjectId), e);
            }
          }
        }
      });
      executor.shutdown();

    }
  }

  private void createRoles(final String pProjectId, final String pTemplateId)
      throws TemplateServiceException, ProjectServiceException
  {
    final List<Role> allRoles = templateRoleService.getAllRoles(pTemplateId);
    for (final Role role : allRoles)
    {
      if (RealmType.SYSTEM.equals(role.getRealmType()))
      {
        final ProjectRole adminRole = projectRoleService.getRole(role.getName(), pProjectId);
        adminRole.setDescription(role.getDescription());
        adminRole.setOrder(role.getOrder());
        projectRoleService.updateRole(role.getName(), adminRole, pProjectId, null);
      }
      else
      {
        final ProjectRole newRole = projectRoleService.newRole();
        newRole.setName(role.getName());
        newRole.setDescription(role.getDescription());
        newRole.setOrder(role.getOrder());
        projectRoleService.createRole(newRole, pProjectId);
      }
    }

  }

  private void createNodes(final String pProjectId, final String pTemplateId)
      throws TemplateServiceException, ApplicationServiceException, SpaceServiceException
  {
    final Project project = projectDAO.findByProjectId(pProjectId);

    final List<Space> spaces = templateNodeService.getAllSpaces(pTemplateId);
    for (final Space templateSpace : spaces)
    {
      Space space = spaceService.newSpace();
      space.setName(templateSpace.getName());
      space = spaceService.addSpace(pProjectId, space);
      final String uri = space.getUri();

      final List<TemplateApplication> apps = templateNodeService.getAllSpaceApplications(
          templateSpace.getUri(), pTemplateId);
      for (final TemplateApplication templateApplication : apps)
      {
        LOGGER.debug(String.format("create the application with [name=%s]", templateApplication.getName()));
        applicationService.addApplication(pProjectId, uri, templateApplication.getName(),
            templateApplication.getDescription(), templateApplication.getPluginUUID(),
            templateApplication.getRolesMapping(), project.getAuthor());
      }
    }

  }

  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }

  public void setTemplateService(final TemplateService pTemplateService)
  {
    templateService = pTemplateService;
  }

  public void setTemplateRoleService(final TemplateRoleService pTemplateRoleService)
  {
    templateRoleService = pTemplateRoleService;
  }

  public void setTemplateNodeService(final TemplateNodeService pTemplateNodeService)
  {
    templateNodeService = pTemplateNodeService;
  }

  public void setProjectRoleService(final ProjectRoleService pProjectRoleService)
  {
    projectRoleService = pProjectRoleService;
  }

  public void setSpaceService(final SpaceService pSpaceService)
  {
    spaceService = pSpaceService;
  }

  public void setApplicationService(final ApplicationService pApplicationService)
  {
    applicationService = pApplicationService;
  }
}
