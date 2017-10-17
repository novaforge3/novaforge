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
package org.novaforge.forge.core.organization.internal.handlers;

import org.novaforge.forge.core.organization.dao.NodeDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.dao.RoleDAO;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.handlers.ApplicationHandler;
import org.novaforge.forge.core.organization.handlers.PICApplicationHandler;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.enumerations.NodeType;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.ProjectApplicationRequestService;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.plugins.services.ToolInstanceProvisioningService;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.core.security.authorization.PermissionHandler;

import javax.persistence.NoResultException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
public class PICApplicationHandlerImpl implements PICApplicationHandler
{
  private static final String SONAR_TYPE   = "sonar";
  private static final String JENKINS_TYPE = "jenkins";
  /**
   * Reference to {@link PermissionHandler} service injected by the container
   */
  private PermissionHandler                permissionHandler;
  /**
   * Reference to {@link ApplicationService} service injected by the container
   */
  private ApplicationService               applicationService;
  /**
   * Reference to {@link ProjectDAO} service injected by the container
   */
  private ProjectDAO                       projectDAO;
  /**
   * Reference to {@link NodeDAO} service injected by the container
   */
  private NodeDAO                          nodeDAO;
  /**
   * Reference to {@link RoleDAO} service injected by the container
   */
  private RoleDAO                          roleDAO;
  /**
   * Reference to {@link ProjectApplicationRequestService} service injected by the container
   */
  private ProjectApplicationRequestService projectApplicationRequestService;
  /**
   * Reference to {@link PluginsManager} service injected by the container
   */
  private PluginsManager                   pluginsManager;
  /**
   * Reference to {@link ApplicationHandler} service injected by the container
   */
  private ApplicationHandler               applicationHandler;

  /**
   * {@inheritDoc}
   */
  @Override
  // if the application type is sonar or jenkins, we need to find if there is another application (sonar or
  // jenkins) in order to get the tool instance id to host the 2 applications
  // we assume there can only have one sonar or one jenkins by project
  // FIXME this needs to be thought more longer perhaps by modeling the notion of applications family
  public ProjectApplication addApplication(final String pProjectId, final String pParentNodeUri,
      final String pApplicationLabel, final String pDescription, final UUID pPluginUUID,
      final Map<String, String> pRolesMapping, final String pUsername) throws ApplicationServiceException
  {

    // check the availability of the tool instance
    ApplicationStatus applicationStatus = null;
    UUID toolInstanceUUID = null;

    // chek if a "PIC family" application exists in order to get its status and tool instance id
    final ProjectApplication applicationPIC = getPICApplication(pPluginUUID, pProjectId);

    // the application is PROVIDING_PENDING except if a PIC family application already exists
    if (applicationPIC != null)
    {
      applicationStatus = applicationPIC.getStatus();
      if (ApplicationStatus.ACTIVE.equals(applicationStatus))
      {
        toolInstanceUUID = getPICToolInstanceUUID(pPluginUUID, applicationPIC.getPluginUUID(),
            applicationPIC.getPluginInstanceUUID());
      }
    }
    else
    {
      applicationStatus = ApplicationStatus.PROVIDING_PENDING;
    }

    // check that no another request is pending for application for the plugin and the project
    if (ApplicationStatus.PROVIDING_PENDING.equals(applicationStatus))
    {
      if (projectApplicationRequestService.hasRequest(pProjectId, pPluginUUID))
      {
        throw new ApplicationServiceException(ExceptionCode.ERR_PROJECT_APPLICATION_REQUEST_ALREADY_EXISTS,
            String.format("projectId=%s, pluginUUID=%s", pProjectId, pPluginUUID));
      }
    }

    // instanciate the application
    final ProjectApplication applicationNode = (ProjectApplication) nodeDAO
        .newNode(NodeType.ProjectApplication);
    final UUID instanceUUID = UUID.randomUUID();
    applicationNode.setName(pApplicationLabel);
    applicationNode.setDescription(pDescription);
    applicationNode.setPluginInstanceUUID(instanceUUID);
    applicationNode.setPluginUUID(pPluginUUID);
    applicationNode.setStatus(applicationStatus);

    // Build set of permissions
    final Set<String> permissions = permissionHandler.buildApplicationPermission(pProjectId, applicationNode
        .getPluginInstanceUUID().toString(), PermissionAction.values());
    for (final String permissionName : permissions)
    {
      final Permission permission = roleDAO.newPermission(permissionName);
      roleDAO.persist(permission);
    }

    // create the application node
    ProjectApplication returnNode;
    try
    {
      returnNode = (ProjectApplication) nodeDAO.addApplicationToSpace(pParentNodeUri, applicationNode);
    }
    catch (final SpaceServiceException e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to add child to a node [uri=%s, parent=%s]", applicationNode, pParentNodeUri), e);
    }

    // to update last modified date of the project
    final Project project = projectDAO.findByProjectId(pProjectId);
    projectDAO.update(project);

    // Add application access to each role mapped
    final Set<Entry<String, String>> entrySet = pRolesMapping.entrySet();

    for (final Entry<String, String> entry : entrySet)
    {
      addApplicationAccessToRole(pProjectId, entry.getKey(), returnNode.getUri(), PermissionAction.READ);
    }

    // send the message propagation only if application status is ACTIVE
    if (ApplicationStatus.ACTIVE.equals(applicationStatus))
    {
      applicationHandler.sendApplicationMessage(pPluginUUID.toString(), instanceUUID.toString(),
          pApplicationLabel, pProjectId, pRolesMapping, toolInstanceUUID.toString(),
          PluginQueueAction.CREATE.getLabel(), pUsername);
    }
    else if (ApplicationStatus.PROVIDING_PENDING.equals(applicationStatus))
    {
      // add a request for providing tool instance
      projectApplicationRequestService.addRequest(pUsername, pProjectId, instanceUUID, pRolesMapping);
    }

    return returnNode;
  }

  // we're looking for a PIC application (sonar or jenkins) which status in not in error
  // we assume the fact that there is only one sonar and one jenkins max by project
  private ProjectApplication getPICApplication(final UUID pluginUUID, final String pProjectId)
      throws ApplicationServiceException
  {
    ProjectApplication returned = null;

    final List<ProjectApplication> applications = getAllProjectApplications(pProjectId);
    for (final ProjectApplication application : applications)
    {
      if ((isPICType(application.getPluginUUID())) && (!ApplicationStatus.CREATE_ON_ERROR.equals(application
                                                                                                     .getStatus())))
      {
        returned = application;
        break;
      }
    }

    return returned;
  }

  private void addApplicationAccessToRole(final String pProjectId, final String roleName,
      final String pApplicationUri, final PermissionAction... pActions) throws ApplicationServiceException
  {
    final ProjectApplication app = getApplication(pProjectId, pApplicationUri);

    final ProjectRole role = (ProjectRole) roleDAO.findByNameAndElement(pProjectId, roleName);
    final Set<String> perms = permissionHandler.buildApplicationPermission(pProjectId, app
        .getPluginInstanceUUID().toString(), pActions);
    for (final String name : perms)
    {
      final Permission permission = roleDAO.findByName(name);
      role.addPermission(permission);
    }
    roleDAO.update(role);
  }

  private List<ProjectApplication> getAllProjectApplications(final String pProjectId) throws ApplicationServiceException
  {
    return applicationService.getAllProjectApplications(pProjectId);
  }

  private ProjectApplication getApplication(final String pProjectId, final String applicationUri)
      throws ApplicationServiceException
  {
    try
    {
      return (ProjectApplication) nodeDAO.findByUri(applicationUri);
    }
    catch (final NoResultException e)
    {
      throw new ApplicationServiceException(String.format("Unable to find any application for [uri=%s]",
                                                          applicationUri), e);
    }
  }

  private boolean isPICType(final String pType)
  {
    boolean is = false;
    if (SONAR_TYPE.equalsIgnoreCase(pType) || JENKINS_TYPE.equalsIgnoreCase(pType))
    {
      is = true;
    }
    return is;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPICType(final UUID pPluginUUID) throws ApplicationServiceException
  {
    try
    {
      final String type = pluginsManager.getPluginMetadataByUUID(pPluginUUID.toString()).getType();
      return isPICType(type);
    }
    catch (final PluginManagerException e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to find the plugin type with [pluginUUID=%s]", pPluginUUID), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getOtherLinkedPICToolInstanceUUID(final UUID pPluginUUID, final UUID pToolInstanceUUID,
      final UUID pTargetPluginUUID) throws ApplicationServiceException
  {
    UUID returned = null;

    final ToolInstance targetInstance = getOtherLinkedPICToolInstance(pPluginUUID, pToolInstanceUUID,
        pTargetPluginUUID);
    if (targetInstance != null)
    {
      returned = targetInstance.getUUID();
    }
    return returned;
  }

  private ToolInstance getOtherLinkedPICToolInstance(final UUID pPluginUUID, final UUID pToolInstanceUUID,
                                                     final UUID pTargetPluginUUID) throws ApplicationServiceException
  {
    try
    {
      final ToolInstanceProvisioningService provisioningService = pluginsManager.getPluginService(pPluginUUID
                                                                                                      .toString())
                                                                                .getToolInstanceProvisioningService();
      final ToolInstance instanceOne = provisioningService.getToolInstanceByUUID(pToolInstanceUUID);
      final String host = instanceOne.getBaseURL().getHost();

      final ToolInstanceProvisioningService targetProvisioningService = pluginsManager
                                                                            .getPluginService(pTargetPluginUUID
                                                                                                  .toString())
                                                                            .getToolInstanceProvisioningService();

      return targetProvisioningService.getToolInstanceByHost(host);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException(String
                                                .format("Unable to get the other linked PIC tool instance with [plugin_uuid=%s, toolInstance_uuid=%s, target_plugin_uuid=%s]",
                                                        pPluginUUID.toString(), pToolInstanceUUID.toString(),
                                                        pTargetPluginUUID.toString()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getPICToolInstanceUUID(final UUID pTargetUUID, final UUID pPluginUUID, final UUID pInstanceUUID)
      throws ApplicationServiceException
  {
    try
    {
      // first we get the tool instance which host the PIC application in order to get the base URL
      final ToolInstanceProvisioningService picProvisioningService = pluginsManager.getPluginService(
          pPluginUUID.toString()).getToolInstanceProvisioningService();
      final URL baseURL = picProvisioningService.getToolInstanceByApplication(pInstanceUUID.toString())
          .getBaseURL();

      // next we get the tool instance linked to this baseURL for the target plugin
      final ToolInstanceProvisioningService targetProvisioningService = pluginsManager.getPluginService(
          pTargetUUID.toString()).getToolInstanceProvisioningService();
      final ToolInstance instance = targetProvisioningService.getToolInstanceByHost(baseURL.getHost());
      if (instance == null)
      {
        throw new ProjectServiceException(String.format(
            "Unable to find the required tool instance for PIC application with [host=%s, plugin_uuid=%s]",
            baseURL.getHost(), pTargetUUID.toString()));
      }

      return instance.getUUID();
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException(
          String.format(
              "Unable to get the UUID identifying the PIC tool instance with [instanceUUID=%s, target pluginUUID=%s, existing pluginUUID=%s]",
              pInstanceUUID.toString(), pTargetUUID.toString(), pPluginUUID.toString()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canHostPICApplicationForProject(final UUID pPluginUUID, final UUID pToolInstanceUUID,
      final String pProjectId) throws ApplicationServiceException
  {
    boolean can = true;

    // get the other plugin UUID PIC
    try
    {
      final String targetPluginUUID = getTheOtherPluginUUID(pPluginUUID);
      if (targetPluginUUID != null)
      {
        // get the reverse (other PIC tool instance uuid)
        final ToolInstance targetInstance = getOtherLinkedPICToolInstance(pPluginUUID, pToolInstanceUUID,
            UUID.fromString(targetPluginUUID));
        if (targetInstance != null)
        {
          final ToolInstanceProvisioningService provisioningService = pluginsManager.getPluginService(
              targetPluginUUID).getToolInstanceProvisioningService();
          final Set<InstanceConfiguration> applications = provisioningService
              .getApplicationsByUUID(targetInstance.getUUID());

          for (final InstanceConfiguration application : applications)
          {
            if (!application.getForgeProjectId().equals(pProjectId))
            {
              can = false;
              break;
            }
          }
        }
      }
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException(
          String.format(
              "Unable to know if tool instance can host application with [plugin=%s, toolInstance_uuid=%s, projectId=%s]",
              pPluginUUID.toString(), pToolInstanceUUID.toString(), pProjectId), e);
    }
    return can;
  }

  private String getTheOtherPluginUUID(final UUID pPluginUUID) throws PluginManagerException
  {
    String theOtherOne = null;

    final List<PluginMetadata> plugins = pluginsManager.getAllPlugins();
    for (final PluginMetadata plugin : plugins)
    {
      if (isPICType(plugin.getType()) && (!pPluginUUID.toString().equals(plugin.getUUID())))
      {
        theOtherOne = plugin.getUUID();
        break;
      }
    }

    return theOtherOne;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPICPartitioned() throws ApplicationServiceException
  {
    try
    {
      final String jenkinsUUID = getPluginUUID(JENKINS_TYPE);
      final String sonarUUID = getPluginUUID(SONAR_TYPE);

      return isPartitioned(jenkinsUUID) && isPartitioned(sonarUUID);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException("Unable to check if PIC plugins are partitioned", e);
    }
  }

  private String getPluginUUID(final String pType) throws PluginManagerException
  {
    String                     pluginUUID = null;
    final List<PluginMetadata> plugins    = pluginsManager.getAllPlugins();
    for (final PluginMetadata plugin : plugins)
    {
      if (plugin.getType().equalsIgnoreCase(pType))
      {
        pluginUUID = plugin.getUUID();
        break;
      }
    }
    return pluginUUID;
  }

  private boolean isPartitioned(final String pPluginUUID) throws ApplicationServiceException
  {
    boolean ret = true;
    try
    {
      final ToolInstanceProvisioningService provisioningService = pluginsManager
          .getPluginService(pPluginUUID).getToolInstanceProvisioningService();

      final Set<ToolInstance> instances = provisioningService.getAllToolInstances();
      for (final ToolInstance instance : instances)
      {
        if (instance.isShareable())
        {
          ret = false;
          break;
        }
      }

      return ret;
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to get know if plugin is partitioned with [plugin_uuid=%s]", pPluginUUID), e);
    }
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
   * Use by container to inject {@link RoleDAO} implementation
   * 
   * @param pRoleDAO
   *          the roleDAO to set
   */
  public void setRoleDAO(final RoleDAO pRoleDAO)
  {
    roleDAO = pRoleDAO;
  }

  /**
   * Use by container to inject {@link ProjectApplicationRequestService} implementation
   * 
   * @param pProjectApplicationRequestService
   *          the projectApplicationRequestService to set
   */
  public void setProjectApplicationRequestService(
      final ProjectApplicationRequestService pProjectApplicationRequestService)
  {
    projectApplicationRequestService = pProjectApplicationRequestService;
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
   * Use by container to inject {@link ApplicationHandler} implementation
   * 
   * @param pApplicationHandler
   *          the applicationHandler to set
   */
  public void setApplicationHandler(final ApplicationHandler pApplicationHandler)
  {
    applicationHandler = pApplicationHandler;
  }

}
