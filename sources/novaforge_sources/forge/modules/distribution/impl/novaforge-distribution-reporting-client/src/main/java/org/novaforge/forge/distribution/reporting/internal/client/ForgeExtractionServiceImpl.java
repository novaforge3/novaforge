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
package org.novaforge.forge.distribution.reporting.internal.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Organization;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.distribution.reporting.client.ForgeReportingClient;
import org.novaforge.forge.distribution.reporting.domain.ProjectDTO;
import org.novaforge.forge.distribution.reporting.domain.UserDTO;
import org.novaforge.forge.distribution.reporting.exceptions.ForgeReportingException;
import org.novaforge.forge.distribution.reporting.services.ForgeExtractionService;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author Petretto-f
 * @author Mohamed IBN EL AZZOUZI
 * @date 13 mars 2012
 */
public class ForgeExtractionServiceImpl implements ForgeExtractionService
{
  private static final Log           LOGGER = LogFactory.getLog(ForgeExtractionServiceImpl.class);

  private ForgeConfigurationService  forgeConfigurationService;
  private AuthentificationService    authentificationService;
  private ForgeIdentificationService forgeIdentificationService;
  private ProjectPresenter           projectPresenter;
  private MembershipPresenter        membershipPresenter;
  private UserPresenter              userPresenter;
  private ForgeReportingClient       forgeReportingClient;

  @Override
  @Historization(type = EventType.START_EXTRACTION)
  public void startExtraction()
  {
    try
    {
      // getting the forge id
      final UUID forgeId = forgeIdentificationService.getForgeId();
      LOGGER.info("=> startExtraction for forgeId : " + forgeId);

      // This is needed in order to authorize role creating
      login();
      final List<ProjectDTO> projectData = new LinkedList<ProjectDTO>();
      final List<UserDTO> usersData = new LinkedList<UserDTO>();

      final List<Project> projects = projectPresenter.getAllProjects(projectPresenter.newProjectOptions(true,
          false, false));

      for (final Project project : projects)
      {
        final RealmType rt = project.getRealmType();
        if (!rt.equals(RealmType.SYSTEM))
        {
          final ProjectDTO projectDTO = new ProjectDTO();
          projectDTO.setProjectId(project.getProjectId());
          final String projectName = project.getName();
          final Organization organization = project.getOrganization();
          final String organizationName = organization != null ? organization.getName() : "";

          projectDTO.setName(projectName);
          projectDTO.setOrganization(organizationName);

          final List<Membership> memberships = membershipPresenter
              .getAllEffectiveUserMembershipsForProject(project.getProjectId());
          for (final Membership membership : memberships)
          {
            final User user = (User) membership.getActor();
            final Role role = membership.getRole();

            final String userLogin = user.getLogin();
            final String userRole = role.getName();

            final UserDTO userDTO = new UserDTO();
            userDTO.setProjectId(project.getProjectId());
            userDTO.setUserLogin(userLogin);
            userDTO.setUserRole(userRole);
            usersData.add(userDTO);

          }
          projectData.add(projectDTO);
        }
      }
      LOGGER.info("=> before storeForgeData projectData.size() : " + projectData.size());

      forgeReportingClient.storeForgeData(forgeId, projectData, usersData);

      // Logout
      authentificationService.logout();
    }
    catch (final Exception e)
    {
      LOGGER.error(String.format("Error occured during the extraction %s", e));
    }

  }

  private void login() throws ForgeReportingException
  {
    try
    {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userPresenter.getUser(superAdministratorLogin);
      authentificationService.login(superAdministratorLogin, user.getPassword());
    }
    catch (final UserServiceException e)
    {
      throw new ForgeReportingException("Unable to authenticate super administrator", e);
    }
  }
}
