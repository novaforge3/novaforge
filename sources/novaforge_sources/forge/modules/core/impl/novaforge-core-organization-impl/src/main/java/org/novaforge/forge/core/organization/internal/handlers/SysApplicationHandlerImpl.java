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

import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.handlers.SysApplicationHandler;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.ProjectRoleService;
import org.novaforge.forge.core.organization.services.SpaceService;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.categories.PluginRealm;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.core.security.authorization.PermissionHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
public class SysApplicationHandlerImpl implements SysApplicationHandler
{

  private static final String ROOT_SPACE_NAME                      = "ROOT";
  private static final String AUTO_CREATED_APPLICATION_LABEL       = "Auto-created project application label";
  private static final String AUTO_CREATED_APPLICATION_DESCRIPTION = "Auto-created project application description";
  private static final String ROOT_SPACE_dESCRIPTION               = "this is the root space";
  /**
   * Reference to {@link PluginsManager} service injected by the container
   */
  private PluginsManager         pluginsManager;
  /**
   * Reference to {@link PluginsCategoryManager} service injected by the container
   */
  private PluginsCategoryManager pluginsCategoryManager;
  /**
   * Reference to {@link ApplicationService} service injected by the container
   */
  private ApplicationService     applicationService;
  /**
   * Reference to {@link SpaceService} service injected by the container
   */
  private SpaceService           spaceService;
  /**
   * Reference to {@link PermissionHandler} service injected by the container
   */
  private PermissionHandler      permissionHandler;
  /**
   * Reference to {@link ProjectRoleService} service injected by the container
   */
  private ProjectRoleService     projectRoleService;
  /**
   * Reference to {@link SecurityDelegate} service injected by the container
   */
  private SecurityDelegate       securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleSysApplications(final Project pProject) throws ApplicationServiceException
  {
    String currentUUID = "";
    final String projectId = pProject.getProjectId();
    try
    {
      // Get all the plugins instanciable by system
      final List<PluginMetadata> pluginMetadatas = pluginsManager
          .getAllInstantiablePluginsMetadataByRealm(PluginRealm.SYSTEM);

      if ((pluginMetadatas != null) && (pluginMetadatas.size() > 0))
      {
        // Get root space
        final String rootURI = buildRootSpace(projectId);

        // Create application for each system plugin
        for (final PluginMetadata pluginMetadata : pluginMetadatas)
        {
          currentUUID = pluginMetadata.getUUID();
          // Create default rolesMappping
          final String defaultRole = getDefaultRole(currentUUID);
          final Map<String, String> rolesMapping = new HashMap<String, String>();
          // Add project role mapping
          final List<ProjectRole> projectRoles = projectRoleService.getAllRoles(projectId);
          for (final ProjectRole projectRole : projectRoles)
          {
            rolesMapping.put(projectRole.getName(), defaultRole);
          }
          applicationService.addApplication(projectId, rootURI, AUTO_CREATED_APPLICATION_LABEL,
              AUTO_CREATED_APPLICATION_DESCRIPTION, UUID.fromString(currentUUID), rolesMapping,
              pProject.getAuthor());

        }
      }
    }
    catch (final SpaceServiceException e)
    {
      throw new ApplicationServiceException(String.format("Unable to create root space for projectId:%s",
          projectId), e);
    }
    catch (final PluginManagerException e)
    {
      throw new ApplicationServiceException("Unable to get all instanciable plugins metadatas by realm", e);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("An error occurred during handling system applications", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String createSysApplication(final Project pProject, final String pPluginUUID)
      throws ApplicationServiceException
  {
    try
    {
      final String projectId = pProject.getProjectId();
      final PluginMetadata pluginMetadataByUUID = pluginsManager.getPluginMetadataByUUID(pPluginUUID);
      final CategoryDefinitionService service = pluginsCategoryManager.getCategoryService(pluginMetadataByUUID
                                                                                              .getCategory());
      if (!PluginRealm.SYSTEM.equals(service.getRealm()))
      {
        throw new ApplicationServiceException(String
                                                  .format("unable to create system application for the project because plugin realm is not SYSTEM with [projectId=%s, uuid=%s]",
                                                          projectId, pPluginUUID));
      }

      // Get root space
      final String rootURI = buildRootSpace(projectId);

      // Create default rolesMappping
      final String defaultRole = service.getDefaultRole();
      final Map<String, String> rolesMapping = new HashMap<String, String>();
      // Add project role mapping
      final List<ProjectRole> projectRoles = projectRoleService.getAllRoles(projectId);
      for (final ProjectRole projectRole : projectRoles)
      {
        rolesMapping.put(projectRole.getName(), defaultRole);
      }

      final ProjectApplication app = applicationService.addApplication(projectId, rootURI,
                                                                       AUTO_CREATED_APPLICATION_LABEL,
                                                                       AUTO_CREATED_APPLICATION_DESCRIPTION,
                                                                       UUID.fromString(pPluginUUID), rolesMapping,
                                                                       pProject.getAuthor());

      return app.getUri();
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException(String
                                                .format("unable to create system application for the project with the plugin with [projectId=%s, uuid=%s]",
                                                        pProject.getProjectId(), pPluginUUID), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> buildSysApplicationPermissions(final String pProjectId)
      throws ApplicationServiceException
  {
    final Set<String> systemPerm = new HashSet<String>();
    // Build root space uri
    final String rootURI = buildSpaceRootUri(pProjectId);
    try
    {
      // Retrieve all system application existing on the project
      final List<ProjectApplication> allSpaceApplications = applicationService.getAllSpaceApplications(
          rootURI, pProjectId);
      for (final ProjectApplication projectApplication : allSpaceApplications)
      {
        // Build access permission for application
        final Set<String> perms = permissionHandler.buildApplicationPermission(pProjectId, projectApplication
            .getPluginInstanceUUID().toString(), PermissionAction.READ);
        systemPerm.addAll(perms);
      }
    }
    catch (final ApplicationServiceException e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to retrieve system applications for the project with [projectId=%s]", pProjectId), e);
    }
    return systemPerm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRoleToSysApplicationsMapping(final String pProjectId, final String pRoleName)
      throws ApplicationServiceException
  {
    // Build root space uri
    final String rootURI = buildSpaceRootUri(pProjectId);
    try
    {
      // Retrieve all system application existing on the project
      final List<ProjectApplication> allSpaceApplications = applicationService.getAllSpaceApplications(
          rootURI, pProjectId);
      for (final ProjectApplication projectApplication : allSpaceApplications)
      {
        final String pluginUUID = projectApplication.getPluginUUID().toString();
        try
        {
          final PluginMetadata pluginMetadataByUUID = pluginsManager.getPluginMetadataByUUID(pluginUUID);
          if (pluginMetadataByUUID.isAvailable())
          {
            final PluginService pluginService = pluginsManager.getPluginService(pluginUUID);

            final Map<String, String> rolesMapping = pluginService.getRolesMapping(projectApplication
                .getPluginInstanceUUID().toString());
            final String defaultRole = getDefaultRole(pluginUUID);
            rolesMapping.put(pRoleName, defaultRole);

            final String username = securityDelegate.getCurrentUser();
            applicationService.updateRoleMapping(pProjectId, projectApplication.getUri(), rolesMapping,
                username);
          }
        }
        catch (final Exception e)
        {
          throw new ApplicationServiceException(String.format(
              "Unable to retrieve plugin information for the plugin with [plugin=%s]", pluginUUID), e);
        }
      }
    }
    catch (final ApplicationServiceException e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to retrieve system applications for the project with [projectId=%s]", pProjectId), e);
    }

  }

  private String buildRootSpace(final String pProjectId) throws SpaceServiceException
  {
    String rootURI = null;
    // check if the root space already exists
    final List<Space> spaces = spaceService.getAllSpaces(pProjectId);
    for (final Space space : spaces)
    {
      if (ROOT_SPACE_NAME.equals(space.getName()))
      {
        rootURI = space.getUri();
      }
    }

    if (rootURI == null)
    {
      rootURI = buildSpaceRootUri(pProjectId);
      final Space rootSysSpace = spaceService.newSpace();
      rootSysSpace.setRealmType(RealmType.SYSTEM);
      rootSysSpace.setDescription(ROOT_SPACE_dESCRIPTION);
      rootSysSpace.setName(ROOT_SPACE_NAME);
      rootSysSpace.setUri(rootURI);

      spaceService.addSpace(pProjectId, rootSysSpace);
    }

    return rootURI;
  }

  private String getDefaultRole(final String targetUUID) throws ApplicationServiceException
  {
    String target = null;
    try
    {
      final PluginMetadata pluginMetadataByUUID = pluginsManager.getPluginMetadataByUUID(targetUUID);
      final CategoryDefinitionService service = pluginsCategoryManager.getCategoryService(pluginMetadataByUUID
                                                                                              .getCategory());
      target = service.getDefaultRole();
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException(String
                                                .format("Unable to get the plugin category service defined for the plugin with [uuid=%s]",
                                                        targetUUID), e);
    }
    return target;
  }

  private String buildSpaceRootUri(final String pParent)
  {
    return pParent + "/" + ROOT_SPACE_NAME;
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
   * Use by container to inject {@link PluginsCategoryManager} implementation
   * 
   * @param pPluginsCategoryManager
   *          the pluginsCategoryManager to set
   */
  public void setPluginsCategoryManager(final PluginsCategoryManager pPluginsCategoryManager)
  {
    pluginsCategoryManager = pPluginsCategoryManager;
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
   * Use by container to inject {@link SpaceService} implementation
   * 
   * @param pSpaceService
   *          the spaceService to set
   */
  public void setSpaceService(final SpaceService pSpaceService)
  {
    spaceService = pSpaceService;
  }

  /**
   * Use by container to inject {@link PermissionHandler} implementation
   * 
   * @param pPermissionHandler
   *          the permissionHandler to set
   */
  public void setPermissionHandler(final PermissionHandler pPermissionHandler)
  {
    permissionHandler = pPermissionHandler;
  }

  /**
   * Use by container to inject {@link ProjectRoleService} implementation
   * 
   * @param pProjectRoleService
   *          the projectRoleService to set
   */
  public void setProjectRoleService(final ProjectRoleService pProjectRoleService)
  {
    projectRoleService = pProjectRoleService;
  }

  /**
   * Use by container to inject {@link SecurityDelegate} implementation
   * 
   * @param pSecurityDelegate
   *          the securityDelegate to set
   */
  public void setSecurityDelegate(final SecurityDelegate pSecurityDelegate)
  {
    securityDelegate = pSecurityDelegate;
  }

}
