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
/**
 *
 */
package org.novaforge.forge.core.organization.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.dao.NodeDAO;
import org.novaforge.forge.core.organization.dao.RoleDAO;
import org.novaforge.forge.core.organization.delegates.MembershipDelegate;
import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.CompositionServiceException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.handlers.ApplicationHandler;
import org.novaforge.forge.core.organization.handlers.PICApplicationHandler;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.NodeType;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.CompositionService;
import org.novaforge.forge.core.organization.services.ProjectApplicationRequestService;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.core.security.authorization.PermissionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of {@link ApplicationService}
 *
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see ApplicationService
 */
public class ApplicationServiceImpl implements ApplicationService
{
  private static final Log LOGGER = LogFactory.getLog(ApplicationServiceImpl.class);
  /**
   * Reference to {@link NodeDAO} service injected by the container
   */
  private NodeDAO                          nodeDAO;
  /**
   * Reference to {@link RoleDAO} service injected by the container
   */
  private RoleDAO                          roleDAO;
  /**
   * Reference to {@link MembershipDAO} service injected by the container
   */
  private MembershipDAO                    membershipDAO;
  /**
   * Reference to {@link CompositionService} service injected by the container
   */
  private CompositionService               compositionService;
  /**
   * Reference to {@link PICApplicationHandler} service injected by the
   * container
   */
  private PICApplicationHandler            picApplicationHandler;
  /**
   * Reference to {@link ApplicationHandler} service injected by the container
   */
  private ApplicationHandler               applicationHandler;
  /**
   * Reference to {@link ProjectApplicationRequestService} service injected by
   * the container
   */
  private ProjectApplicationRequestService projectApplicationRequestService;
  /**
   * Reference to {@link PermissionHandler} service injected by the container
   */
  private PermissionHandler                permissionHandler;
  /**
   * Reference to {@link PluginsManager} service injected by the container
   */
  private PluginsManager                   pluginsManager;
  /**
   * Reference to {@link MembershipDelegate} service injected by the container
   */
  private MembershipDelegate               membershipDelegate;
  /**
   * Reference to {@link SecurityDelegate} service injected by the container
   */
  private SecurityDelegate                 securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplication addApplication(final String pProjectId, final String pParentNodeUri,
                                           final String pApplicationLabel, final String pDescription,
                                           final UUID pPluginUUID, final Map<String, String> pRolesMapping,
                                           final String pUsername) throws ApplicationServiceException
  {
    // check if the plugin is available and its status is ACTIVATED
    if (!applicationHandler.canAddApplication(pPluginUUID))
    {
      throw new ApplicationServiceException(ExceptionCode.ERR_ADD_APPLICATION_PLUGIN_NOT_ACTIVATED, String
                                                                                                        .format("the plugin with uuid=%s is not activated in order to add application",
                                                                                                                pPluginUUID));
    }

    // Check if an application already exists with the same name
    final List<ProjectApplication> applications = getAllProjectApplications(pProjectId);
    for (final Application application : applications)
    {
      if (application.getName().equals(pApplicationLabel))
      {
        throw new ApplicationServiceException(ExceptionCode.ERR_CREATE_APP_NAME_ALREADY_EXIST,
                                              String.format("projectId=%s, applicationLabel=%s", pProjectId,
                                                            pApplicationLabel));
      }
    }

    // Check if there is restriction about the number of project applications allowed
    checkMaxAllowedProjectInstances(pPluginUUID, pProjectId);

    ProjectApplication applicationReturned;

    try
    {
      if (picApplicationHandler.isPICType(pPluginUUID) && picApplicationHandler.isPICPartitioned())
      {
        applicationReturned = picApplicationHandler.addApplication(pProjectId, pParentNodeUri, pApplicationLabel,
                                                                   pDescription, pPluginUUID, pRolesMapping, pUsername);
      }
      else
      {
        applicationReturned = addStandardApplication(pProjectId, pParentNodeUri, pApplicationLabel, pDescription,
                                                     pPluginUUID, pRolesMapping, pUsername);
      }
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }

    return applicationReturned;
  }

  private void checkMaxAllowedProjectInstances(final UUID pPluginUUID, final String pProjectId)
      throws ApplicationServiceException
  {
    try
    {
      final PluginService pluginService = pluginsManager.getPluginService(pPluginUUID.toString());
      if (pluginService.getMaxAllowedProjectInstances() != -1)
      {
        final int projectApplications = countProjectApplicationsByPluginUUID(pProjectId, pPluginUUID);
        if (projectApplications >= pluginService.getMaxAllowedProjectInstances())
        {
          throw new ApplicationServiceException(ExceptionCode.ERR_MAX_ALLOWED_PROJECT_APPLICATIONS,
                                                String.format("projectId=%s, pluginUUID=%s", pProjectId, pPluginUUID));
        }
      }
    }
    catch (final PluginManagerException e)
    {
      throw new ApplicationServiceException(String.format("Unable to get plugin service with [plugin uuid=%s]",
                                                          pPluginUUID.toString()), e);
    }
  }

  private ProjectApplication addStandardApplication(final String projectId, final String parentNodeUri,
                                                    final String applicationLabel, final String pDescription,
                                                    final UUID pluginUUID, final Map<String, String> pRolesMapping,
                                                    final String pUsername) throws ApplicationServiceException
  {

    // check the availability of the tool instance
    ApplicationStatus applicationStatus;
    UUID              toolInstanceUUID = null;
    if (applicationHandler.hasAvailableToolInstance(pluginUUID))
    {
      applicationStatus = ApplicationStatus.CREATE_IN_PROGRESS;
      final List<ToolInstance> toolInstances = applicationHandler.getAvailableToolInstances(pluginUUID);
      toolInstanceUUID = toolInstances.get(0).getUUID();
    }
    else
    {
      applicationStatus = ApplicationStatus.PROVIDING_PENDING;
    }

    // check that no another request is pending for application for the plugin
    // and the project
    if (ApplicationStatus.PROVIDING_PENDING.equals(applicationStatus))
    {
      if (projectApplicationRequestService.hasRequest(projectId, pluginUUID))
      {
        throw new ApplicationServiceException(ExceptionCode.ERR_PROJECT_APPLICATION_REQUEST_ALREADY_EXISTS,
                                              String.format("projectId=%s, pluginUUID=%s", projectId, pluginUUID));
      }
    }

    // instanciate the application
    final ProjectApplication applicationNode = (ProjectApplication) nodeDAO.newNode(NodeType.ProjectApplication);
    final UUID               instanceUUID    = UUID.randomUUID();
    applicationNode.setName(applicationLabel);
    applicationNode.setDescription(pDescription);
    applicationNode.setPluginInstanceUUID(instanceUUID);
    applicationNode.setPluginUUID(pluginUUID);
    applicationNode.setStatus(applicationStatus);

    // Build and persist a set of permissions
    final Set<String> permissions = permissionHandler.buildApplicationPermission(projectId,
                                                                                 applicationNode.getPluginInstanceUUID()
                                                                                                .toString(),
                                                                                 PermissionAction.values());

    for (final String permission : permissions)
    {
      roleDAO.persist(roleDAO.newPermission(permission));
    }

    // create the application node
    ProjectApplication returnNode;
    try
    {
      returnNode = (ProjectApplication) nodeDAO.addApplicationToSpace(parentNodeUri, applicationNode);
    }
    catch (final SpaceServiceException e)
    {
      throw new ApplicationServiceException(String.format("Unable to add child to a node [uri=%s, parent=%s]",
                                                          applicationNode, parentNodeUri), e);
    }

    // Add application access to each role mapped
    final Set<Entry<String, String>> entrySet = pRolesMapping.entrySet();

    for (final Entry<String, String> entry : entrySet)
    {
      addApplicationAccessToRole(projectId, entry.getKey(), returnNode.getUri(), PermissionAction.READ);
    }

    // send the message propagation only if application status is ACTIVE
    if (ApplicationStatus.CREATE_IN_PROGRESS.equals(applicationStatus))
    {
      applicationHandler.sendApplicationMessage(pluginUUID.toString(), instanceUUID.toString(), applicationLabel,
                                                projectId, pRolesMapping, toolInstanceUUID.toString(),
                                                PluginQueueAction.CREATE.getLabel(), pUsername);
    }
    else if (ApplicationStatus.PROVIDING_PENDING.equals(applicationStatus))
    {
      // add a request for providing tool instance
      projectApplicationRequestService.addRequest(pUsername, projectId, instanceUUID, pRolesMapping);
    }

    return returnNode;
  }

  private int countProjectApplicationsByPluginUUID(final String pProjectId, final UUID pPluginUUID)
      throws ApplicationServiceException
  {
    int nb = 0;

    final List<ProjectApplication> applications = getAllProjectApplications(pProjectId);
    for (final ProjectApplication application : applications)
    {
      if (pPluginUUID.equals(application.getPluginUUID()) && (ApplicationStatus.ACTIVE.equals(application.getStatus())
                                                                  || ApplicationStatus.PROVIDING_PENDING
                                                                         .equals(application.getStatus())))
      {
        nb++;
      }
    }

    return nb;
  }

  /**
   * This method should be managed by presenter but we need it to be here for internal use
   */
  private void clearPermissionCached(final String pProjectId, final String pRoleName)
  {
    try
    {
      final List<Actor> actors = membershipDAO.findActorsByProjectAndRole(pProjectId, pRoleName);
      for (final Actor actor : actors)
      {
        if (actor instanceof User)
        {
          securityDelegate.clearPermissionCached(((User) actor).getLogin());
        }
        else if (actor instanceof Group)
        {
          final Group group = (Group) actor;
          for (final User userGrouped : group.getUsers())
          {
            securityDelegate.clearPermissionCached(userGrouped.getLogin());
          }
        }
      }
    }
    catch (final Exception e)
    {
      LOGGER.warn("Unable to clean permission cached", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteApplication(final String pProjectId, final String pApplicationUri, final String pUsername)
      throws ApplicationServiceException
  {
    try
    {
      // get the application node
      final ProjectApplication application = getApplication(pApplicationUri);

      // Check existing composition
      checkExistingComposition(pProjectId, application);

      // if the application is providing pending, we need to delete first the
      // provisioning request
      if (ApplicationStatus.PROVIDING_PENDING.equals(application.getStatus()))
      {
        try
        {
          projectApplicationRequestService.deleteRequest(pProjectId, application.getPluginInstanceUUID());
        }
        catch (final ApplicationServiceException e)
        {
          // no request have been found : we don't throw any exception
          LOGGER.error(String
                           .format("Unable to delete the provisioning request linked to the application with [projectId=%s, instance_uuid=%s]",
                                   pProjectId, application.getPluginInstanceUUID()), e);
        }
        // Delete the application and permissions
        cleanUpApplication(pProjectId, application);
      }
      else if (ApplicationStatus.ACTIVE.equals(application.getStatus()))
      {
        final boolean availablePlugin = pluginsManager.isAvailablePlugin(application.getPluginUUID().toString());
        if (availablePlugin)
        {
          // Changing its status
          nodeDAO.updateApplicationStatus(ApplicationStatus.DELETE_IN_PROGRESS, application.getPluginInstanceUUID());

          // get the tool instance id in order to free it
          final ToolInstance toolInstance = applicationHandler.getToolInstanceByApplication(application.getPluginUUID()
                                                                                                       .toString(),
                                                                                            application
                                                                                                .getPluginInstanceUUID()
                                                                                                .toString());

          // send the message to plugin in order to delete application
          applicationHandler.sendApplicationMessage(application.getPluginUUID().toString(),
                                                    application.getPluginInstanceUUID().toString(),
                                                    application.getName(), pProjectId, null,
                                                    toolInstance.getUUID().toString(),
                                                    PluginQueueAction.DELETE.getLabel(), pUsername);
        }
        else
        {

          // Changing its status
          nodeDAO.updateApplicationStatus(ApplicationStatus.DELETE_ON_ERROR, application.getPluginInstanceUUID());
        }
      }
      else if ((ApplicationStatus.DELETE_ON_ERROR.equals(application.getStatus())) || (ApplicationStatus.CREATE_ON_ERROR
                                                                                           .equals(application
                                                                                                       .getStatus())))
      {
        final String currentUser = securityDelegate.getCurrentUser();
        if (membershipDelegate.isSuperAdmin(currentUser))
        {
          final boolean availablePlugin = pluginsManager.isAvailablePlugin(application.getPluginUUID().toString());
          if (availablePlugin)
          {
            // get the tool instance id in order to free it
            final ToolInstance toolInstance = applicationHandler.getToolInstanceByApplication(application
                                                                                                  .getPluginUUID()
                                                                                                  .toString(),
                                                                                              application
                                                                                                  .getPluginInstanceUUID()
                                                                                                  .toString());

            // send the message to plugin in order to delete application
            applicationHandler.sendForceApplicationDeleteMessage(application.getPluginUUID().toString(),
                                                                 application.getPluginInstanceUUID().toString(),
                                                                 application.getName(), pProjectId,
                                                                 toolInstance.getUUID().toString(), pUsername);
          }
        }

      }
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * This method will check if an composition is existing for this application
   * and throw an exception if yes
   *
   * @param pProjectId
   *     the project id
   * @param pApplication
   *     the application to check
   *
   * @throws NodeServiceException
   *     thrown if a composition exists or if existing project composition
   *     cannot be retrieved.
   */
  private void checkExistingComposition(final String pProjectId, final ProjectApplication pApplication)
      throws ApplicationServiceException
  {
    try
    {
      final List<Composition> comps = compositionService.getComposition(pProjectId);
      if (comps != null)
      {
        for (final Composition composition : comps)
        {
          if (composition.getSource().getPluginInstanceUUID().equals(pApplication.getPluginInstanceUUID())
                  || composition.getTarget().getPluginInstanceUUID().equals(pApplication.getPluginInstanceUUID()))
          {
            throw new ApplicationServiceException(ExceptionCode.ERR_CREATE_APP_COMP_EXIST, String
                                                                                               .format("A application composition exits for this application, unable to delete it! [project_id=%s, instance=%s]",
                                                                                                       pProjectId,
                                                                                                       pApplication
                                                                                                           .getPluginInstanceUUID()));
          }
        }
      }
    }
    catch (final CompositionServiceException e)
    {
      throw new ApplicationServiceException(String
                                                .format("Unable to obtain all composition application for a project with [id=%s]",
                                                        pProjectId), e);
    }

  }

  private void cleanUpApplication(final String pProjectId, final ProjectApplication applicationToDelete)
      throws ApplicationServiceException
  {
    // Build and remove a set of permissions
    final Set<String> permissions = permissionHandler.buildApplicationPermission(pProjectId, applicationToDelete
                                                                                                 .getPluginInstanceUUID()
                                                                                                 .toString(),
                                                                                 PermissionAction.values());

    if (permissions != null)
    {
      final List<Role> roles = roleDAO.findAllRole(pProjectId);
      for (final String name : permissions)
      {
        for (final Role role : roles)
        {
          cleanPermission((ProjectRole) role, name);
        }
        roleDAO.delete(roleDAO.findByName(name));
      }
    }

    // Get parent space
    final Space parentSpace = nodeDAO.findSpaceForApp(applicationToDelete.getUri());
    try
    {
      nodeDAO.deleteApplication(parentSpace.getUri(), applicationToDelete.getUri());
    }
    catch (final SpaceServiceException e)
    {
      throw new ApplicationServiceException(String
                                                .format("Unable to delete application for a space with [application=%s, space=%s]",
                                                        applicationToDelete, parentSpace), e);
    }
  }

  private void cleanPermission(final ProjectRole pRole, final String pPermission)
  {
    final List<Permission> tempList = new ArrayList<Permission>();
    for (final Permission permission : pRole.getPermissions())
    {
      if (permission.getName().equals(pPermission))
      {
        tempList.add(permission);
      }
    }
    for (final Permission permission : tempList)
    {
      pRole.removePermission(permission);
    }
    roleDAO.update(pRole);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finalizeDeleteApplication(final String pProjectId, final String pInstanceUUID,
                                        final String pToolInstanceUUID) throws ApplicationServiceException
  {
    // Get the ProjectApplication
    final ProjectApplication applicationToDelete = nodeDAO.findByInstanceId(UUID.fromString(pInstanceUUID));

    // Delete the persisted objects : Application and its permissions
    cleanUpApplication(pProjectId, applicationToDelete);

    // TODO no more trigger the provisioning of the free fresh tool instance
    // get the tool instance id in order to free it
    //    final String pluginUUID = applicationToDelete.getPluginUUID().toString();
    //    if ((pToolInstanceUUID != null) && (pluginsManager.isAvailablePlugin(pluginUUID)))
    //    {
    //      try
    //      {
    //        final PluginService pluginService = pluginsManager.getPluginService(applicationToDelete.getPluginUUID()
    //                                                                                               .toString());
    //        if (pluginService != null)
    //        {
    //          final ToolInstance toolInstance = pluginService.getToolInstanceProvisioningService()
    //                                                         .getToolInstanceByUUID(UUID.fromString(pToolInstanceUUID));
    //
    //          if (toolInstance.isShareable() == false)
    //          {
    //             projectApplicationRequestService.handleOldestRequest(toolInstance.getUUID(),
    //             applicationToDelete.getPluginUUID());
    //          }
    //        }
    //      }
    //      catch (final PluginManagerException e)
    //      {
    //        throw new ApplicationServiceException(String.format("Unable to retrieve plugin service with [plugin_uuid=%s]",
    //                                                            pluginUUID), e);
    //      }
    //      catch (final ToolInstanceProvisioningException e)
    //      {
    //        throw new ApplicationServiceException(String
    //                                                  .format("Unable to retrieve plugin tool instance used by this application with [toolInstance=%s]",
    //                                                          pToolInstanceUUID), e);
    //      }
    //      catch (final Exception e)
    //      {
    //        throw new ApplicationServiceException("a technical error occured", e);
    //      }
    //  }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplication updateDescription(final String pApplicationUri, final String pDescription)
      throws ApplicationServiceException
  {
    try
    {
      ProjectApplication projectApplication = getApplication(pApplicationUri);
      if (((projectApplication.getDescription() != null) && (!projectApplication.getDescription().equals(pDescription)))
              || ((projectApplication.getDescription() == null) && (pDescription != null)))
      {
        projectApplication.setDescription(pDescription);
        projectApplication = nodeDAO.update(projectApplication);
      }
      return projectApplication;
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplication getApplication(final String pApplicationUri) throws ApplicationServiceException
  {
    try
    {
      return (ProjectApplication) nodeDAO.findByUri(pApplicationUri);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplication getApplication(final UUID pInstanceUUID) throws ApplicationServiceException
  {
    try
    {
      return nodeDAO.findByInstanceId(pInstanceUUID);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplication> getAllProjectApplications(final String pProjectId) throws ApplicationServiceException
  {
    try
    {
      final List<ProjectApplication> applications = new ArrayList<ProjectApplication>();
      final Map<Space, List<Application>> spacesWithApplications = nodeDAO.findSpacesWithAppsForElementId(pProjectId);
      for (final Map.Entry<Space, List<Application>> entry : spacesWithApplications.entrySet())
      {
        for (final Application application : entry.getValue())
        {
          applications.add((ProjectApplication) application);
        }
      }
      return applications;
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplication> getAllProjectApplications(final String pProjectId, final String... pPluginUUID)
      throws ApplicationServiceException
  {
    try
    {
      final List<String> plugins = Arrays.asList(pPluginUUID);
      final List<ProjectApplication> applications = new ArrayList<ProjectApplication>();
      final Map<Space, List<Application>> spacesWithApplications = nodeDAO.findSpacesWithAppsForElementId(pProjectId);
      for (final Map.Entry<Space, List<Application>> entry : spacesWithApplications.entrySet())
      {
        for (final Application application : entry.getValue())
        {
          if (plugins.contains(application.getPluginUUID().toString()))
          {
            applications.add((ProjectApplication) application);
          }
        }
      }
      return applications;
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplication> getAvailableApplications(final String pProjectId) throws ApplicationServiceException
  {
    try
    {
      final List<ProjectApplication> activeApplication = new ArrayList<ProjectApplication>();

      final List<ProjectApplication> userApplications = getAllProjectApplications(pProjectId);

      for (final ProjectApplication projectApplication : userApplications)
      {
        if ((ApplicationStatus.ACTIVE.equals(projectApplication.getStatus())) && (pluginsManager
                                                                                      .isAvailablePlugin(projectApplication
                                                                                                             .getPluginUUID()
                                                                                                             .toString())))
        {
          activeApplication.add(projectApplication);
        }
      }
      return activeApplication;
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("A technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getRoleMapping(final String pApplicationUri) throws ApplicationServiceException
  {
    try
    {
      return applicationHandler.getRoleMapping(pApplicationUri);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateRoleMapping(final String pProjectId, final String pApplicationUri,
                                final Map<String, String> pRoleMapping, final String pUserName)
      throws ApplicationServiceException
  {
    try
    {
      // get the application node
      final ProjectApplication application = getApplication(pApplicationUri);

      // Update application acces for each membership
      final List<Role> allRoles = roleDAO.findAllRole(pProjectId);
      for (final Role role : allRoles)
      {
        final String roleName = role.getName();
        if (pRoleMapping.get(roleName) != null)
        {
          final Set<ProjectApplication> applicationAccessible = getApplicationAccessForRole(pProjectId, roleName);
          if (!applicationAccessible.contains(application))
          {
            addApplicationAccessToRole(pProjectId, roleName, pApplicationUri, PermissionAction.READ);
          }
        }
        else
        {
          updateApplicationAccessToRole(pProjectId, roleName, pApplicationUri, (PermissionAction) null);
        }
      }

      applicationHandler.sendRolesMappingMessage(pApplicationUri, application.getPluginUUID().toString(),
                                                 application.getPluginInstanceUUID().toString(), pProjectId,
                                                 pRoleMapping, PluginQueueAction.UPDATE.getLabel(), pUserName);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplication> getAllSpaceApplications(final String pSpaceNodeUri, final String pProjectId)
      throws ApplicationServiceException
  {
    try
    {
      final List<ProjectApplication> projectApplications = new ArrayList<ProjectApplication>();

      final List<Application> applications = nodeDAO.findAppsForSpace(pSpaceNodeUri);
      for (final Application application : applications)
      {
        projectApplications.add((ProjectApplication) application);
      }

      return projectApplications;
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addApplicationAccessToRole(final String pProjectId, final String pRoleName, final String pApplicationUri,
                                         final PermissionAction... pActions) throws ApplicationServiceException
  {
    try
    {
      final ProjectApplication app = getApplication(pApplicationUri);

      // Obtain role object
      final ProjectRole role = (ProjectRole) roleDAO.findByNameAndElement(pProjectId, pRoleName);

      // Build permissions
      final Set<String> perms = permissionHandler.buildApplicationPermission(pProjectId,
                                                                             app.getPluginInstanceUUID().toString(),
                                                                             pActions);
      for (final String name : perms)
      {
        role.addPermission(roleDAO.findByName(name));
      }

      roleDAO.update(role);

      // Clean permission cached
      clearPermissionCached(pProjectId, pRoleName);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateApplicationAccessToRole(final String pProjectId, final String pRoleName,
                                            final String pApplicationUri, final PermissionAction... pActions)
      throws ApplicationServiceException
  {
    try
    {
      final ProjectApplication app = getApplication(pApplicationUri);

      // Get instance uuid regarding application
      final String instanceUUID = app.getPluginInstanceUUID().toString();

      // Obtain role object
      final ProjectRole role = (ProjectRole) roleDAO.findByNameAndElement(pProjectId, pRoleName);

      // Clean all access
      final ProjectRole cleanRole = cleanRoleAccess(pProjectId, instanceUUID, role);

      if ((pActions != null) && (pActions.length != 0) && (pActions[0] != null))
      {
        final Set<String> perms = permissionHandler.buildApplicationPermission(pProjectId, instanceUUID, pActions);
        for (final String name : perms)
        {
          final Permission permission = roleDAO.findByName(name);
          cleanRole.addPermission(permission);
        }

      }
      roleDAO.update(cleanRole);

      // Clean permission cached
      clearPermissionCached(pProjectId, pRoleName);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ProjectApplication> getApplicationAccessForRole(final String pProjectId, final String pRoleName)
      throws ApplicationServiceException
  {
    try
    {
      final Set<ProjectApplication> returnList = new HashSet<ProjectApplication>();

      // Obtain role object
      final ProjectRole role = (ProjectRole) roleDAO.findByNameAndElement(pProjectId, pRoleName);

      // Build a set of permission name from List of permission
      final Set<String> perms = new HashSet<String>();
      for (final Permission permission : role.getPermissions())
      {
        perms.add(permission.getName());
      }

      // Resolve application access
      final Set<String> resolveApplications = permissionHandler.resolveApplicationId(perms);

      // Get the list of applications available for current project
      final List<ProjectApplication> applications = getAllProjectApplications(pProjectId);

      // Build the return list which contains all application available for a role
      // given
      for (final ProjectApplication application : applications)
      {
        for (final String applicationId : resolveApplications)
        {
          if (application.getPluginInstanceUUID().toString().equals(applicationId))
          {
            returnList.add(application);
          }
        }
      }
      return returnList;
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeApplicationStatus(final ApplicationStatus pApplicationStatus, final String pInstanceID)
      throws ApplicationServiceException
  {
    try
    {
      nodeDAO.updateApplicationStatus(pApplicationStatus, UUID.fromString(pInstanceID));
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moveApplication(final String pFromSpaceUri, final String pToSpaceUri, final String pApplicationUri)
      throws ApplicationServiceException
  {
    try
    {
      nodeDAO.moveApplication(pFromSpaceUri, pToSpaceUri, pApplicationUri);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("a technical error occured", e);
    }
  }

  private ProjectRole cleanRoleAccess(final String pProjectId, final String pInstanceId, final ProjectRole pRole)
  {
    final Set<String> appAccess = permissionHandler.buildApplicationPermission(pProjectId, pInstanceId,
                                                                               PermissionAction.values());
    for (final String perm : appAccess)
    {
      for (final Permission permission : pRole.getPermissions())
      {
        if (permission.getName().equals(perm))
        {
          pRole.removePermission(permission);
        }
      }
    }
    return pRole;
  }

  /**
   * Use by container to inject {@link NodeDAO} implementation
   *
   * @param pNodeDAO
   *     the nodeDAO to set
   */
  public void setNodeDAO(final NodeDAO pNodeDAO)
  {
    nodeDAO = pNodeDAO;
  }

  /**
   * Use by container to inject {@link RoleDAO} implementation
   *
   * @param pRoleDAO
   *     the roleDAO to set
   */
  public void setRoleDAO(final RoleDAO pRoleDAO)
  {
    roleDAO = pRoleDAO;
  }

  /**
   * Use by container to inject {@link MembershipDAO} implementation
   *
   * @param pMembershipDAO
   *     the membershipDAO to set
   */
  public void setMembershipDAO(final MembershipDAO pMembershipDAO)
  {
    membershipDAO = pMembershipDAO;
  }

  /**
   * Use by container to inject {@link CompositionService} implementation
   *
   * @param pCompositionService
   *     the compositionService to set
   */
  public void setCompositionService(final CompositionService pCompositionService)
  {
    compositionService = pCompositionService;
  }

  /**
   * Use by container to inject {@link PICApplicationHandler} implementation
   *
   * @param pPicApplicationHandler
   *     the picApplicationHandler to set
   */
  public void setPicApplicationHandler(final PICApplicationHandler pPicApplicationHandler)
  {
    picApplicationHandler = pPicApplicationHandler;
  }

  /**
   * Use by container to inject {@link ApplicationHandler} implementation
   *
   * @param pApplicationHandler
   *     the applicationHandler to set
   */
  public void setApplicationHandler(final ApplicationHandler pApplicationHandler)
  {
    applicationHandler = pApplicationHandler;
  }

  /**
   * Use by container to inject {@link ProjectApplicationRequestService} implementation
   *
   * @param pProjectApplicationRequestService
   *     the projectApplicationRequestService to set
   */
  public void setProjectApplicationRequestService(final ProjectApplicationRequestService pProjectApplicationRequestService)
  {
    projectApplicationRequestService = pProjectApplicationRequestService;
  }

  /**
   * Use by container to inject {@link PermissionHandler} implementation
   *
   * @param pPermissionHandler
   *     the permissionHandler to set
   */
  public void setPermissionHandler(final PermissionHandler pPermissionHandler)
  {
    permissionHandler = pPermissionHandler;
  }

  /**
   * Use by container to inject {@link PluginsManager} implementation
   *
   * @param pPluginsManager
   *     the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  /**
   * Use by container to inject {@link MembershipDelegate} implementation
   *
   * @param pMembershipDelegate
   *     the membershipDelegate to set
   */
  public void setMembershipDelegate(final MembershipDelegate pMembershipDelegate)
  {
    membershipDelegate = pMembershipDelegate;
  }

  /**
   * Use by container to inject {@link SecurityDelegate} implementation
   *
   * @param pSecurityDelegate
   *     the securityDelegate to set
   */
  public void setSecurityDelegate(final SecurityDelegate pSecurityDelegate)
  {
    securityDelegate = pSecurityDelegate;
  }
}
