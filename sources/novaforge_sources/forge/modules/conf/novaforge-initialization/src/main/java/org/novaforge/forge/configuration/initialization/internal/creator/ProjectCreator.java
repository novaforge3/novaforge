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
package org.novaforge.forge.configuration.initialization.internal.creator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.configuration.initialization.exceptions.ForgeInitializationException;
import org.novaforge.forge.configuration.initialization.internal.properties.InitializationProperties;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.dao.RoleDAO;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.core.security.authorization.PermissionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public class ProjectCreator
{

  private static final Log          LOGGER    = LogFactory.getLog(ProjectCreator.class);
  private static final String       FORGE_GOD = "*";
  private AuthentificationService   authentificationService;
  private ProjectPresenter          projectPresenter;
  private ProjectRolePresenter      rolePresenter;
  private RoleDAO                   roleDAO;
  private MembershipPresenter       membershipPresenter;
  private UserPresenter             userPresenter;
  private PermissionHandler         permissionHandler;
  private InitializationProperties  initializationProperties;
  private ForgeConfigurationService forgeConfigurationService;

  public void createForgeProject() throws ForgeInitializationException
  {
    LOGGER.info("Forge project is being created.");
    // This is needed in order to authorize role creating
    authentificationService.login(initializationProperties.getSuperAdministratorLogin(),
        initializationProperties.getSuperAdministratorPassword());

    try
    {
      createProject();
      createRoleAndPermissions();
    }
    catch (final Exception e)
    {
      throw new ForgeInitializationException("Unable to create forge project", e);
    }

    authentificationService.logout();

    LOGGER.info("Forge project created.");
  }

  private void createProject() throws ProjectServiceException
  {
    final Project forge = projectPresenter.newProject();
    forge.setProjectId(initializationProperties.getForgeProjectId());
    forge.setName(initializationProperties.getForgeProjectName());
    forge.setDescription(initializationProperties.getForgeProjectDescription());
    forge.setLicenceType(initializationProperties.getForgeProjectLicence());
    byte[] defaultIcon = forgeConfigurationService.getDefaultIcon();
    if (defaultIcon == null)
    {
      final InputStream resourceAsStream = this.getClass().getResourceAsStream("/logoNovaforge.png");

      if (resourceAsStream != null)
      {
        try
        {
          defaultIcon = IOUtils.toByteArray(resourceAsStream);
        }
        catch (final IOException e)
        {
          LOGGER.warn("Unable to retrieve default forge icon from classloader");
        }
      }
    }

    forge.getImage().setFile(defaultIcon);
    forge.getImage().setMimeType("image/png");
    forge.getImage().setName("defaultIcon");

    projectPresenter.createSystemProject(forge);

  }

  private void createRoleAndPermissions() throws ProjectServiceException, UserServiceException
  {
    // This is needed in order to authorize role creating
    authentificationService.login(initializationProperties.getSuperAdministratorLogin(),
        initializationProperties.getSuperAdministratorPassword());

    // Create super administrator role
    final ProjectRole superAdminitrator = rolePresenter.newRole();
    superAdminitrator.setName(initializationProperties.getForgeSuperAdministratorRoleName());
    roleDAO.persist(roleDAO.newPermission(FORGE_GOD));
    superAdminitrator.addPermission(roleDAO.findByName(FORGE_GOD));
    rolePresenter.createSystemRole(superAdminitrator, initializationProperties.getForgeProjectId());

    final Set<String> roles = new HashSet<String>();
    roles.add(initializationProperties.getForgeSuperAdministratorRoleName());
    final User user = userPresenter.getUser(initializationProperties.getSuperAdministratorLogin());
    membershipPresenter.updateUserMembership(initializationProperties.getForgeProjectId(), user.getUuid(),
        roles, null, false);

    // Create default forge permissions and member role
    final ProjectRole member = rolePresenter.newRole();
    member.setName(initializationProperties.getForgeMemberRoleName());
    final Set<String> forgePermissions = permissionHandler.buildForgePermissions(PermissionAction.READ);
    for (final String permission : forgePermissions)
    {
      roleDAO.persist(roleDAO.newPermission(permission));
      if (permission.endsWith(PermissionAction.READ.getLabel()))
      {
        member.addPermission(roleDAO.findByName(permission));
      }
    }
    final Set<String> projectPermissions = permissionHandler.buildProjectPermissions(PermissionAction.CREATE,
        PermissionAction.DELETE);
    for (final String permission : projectPermissions)
    {
      roleDAO.persist(roleDAO.newPermission(permission));
      if (permission.endsWith(PermissionAction.CREATE.getLabel()))
      {
        member.addPermission(roleDAO.findByName(permission));
      }
    }
    rolePresenter.createSystemRole(member, initializationProperties.getForgeProjectId());

    authentificationService.logout();
  }

  public void setInitializationProperties(final InitializationProperties pInitializationProperties)
  {
    initializationProperties = pInitializationProperties;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  public void setRolePresenter(final ProjectRolePresenter pRolePresenter)
  {
    rolePresenter = pRolePresenter;
  }

  public void setRoleDAO(final RoleDAO pRoleDAO)
  {
    roleDAO = pRoleDAO;
  }

  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

  public void setMembershipPresenter(final MembershipPresenter pMembershipPresenter)
  {
    membershipPresenter = pMembershipPresenter;
  }

  public void setPermissionHandler(final PermissionHandler pPermissionHandler)
  {
    permissionHandler = pPermissionHandler;
  }

}
