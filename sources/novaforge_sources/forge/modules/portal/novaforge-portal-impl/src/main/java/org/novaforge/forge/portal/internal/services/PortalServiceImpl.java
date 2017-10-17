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
package org.novaforge.forge.portal.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.ToolInstanceProvisioningException;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.internal.models.PortalApplicationImpl;
import org.novaforge.forge.portal.internal.models.PortalSpaceImpl;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.models.PortalSettingId;
import org.novaforge.forge.portal.models.PortalSpace;
import org.novaforge.forge.portal.models.PortalURI;
import org.novaforge.forge.portal.services.PortalNavigation;
import org.novaforge.forge.portal.services.PortalService;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * This is an implementation of {@link PortalService}
 * 
 * @author Guillaume Lamirand
 */
public class PortalServiceImpl implements PortalService
{

  /**
   * Logger
   */
  private static final Log          LOG = LogFactory.getLog(PortalServiceImpl.class);

  /**
   * Reference to {@link ForgeConfigurationManager} service injected by the container
   */
  private ForgeConfigurationService forgeConfigurationService;
  /**
   * Reference to {@link ProjectPresenter} service injected by the container
   */
  private ProjectPresenter          projectPresenter;
  /**
   * Reference to {@link UserPresenter} service injected by the container
   */
  private UserPresenter             userPresenter;
  /**
   * Reference to {@link PluginsManager} service injected by the container
   */
  private PluginsManager            pluginsManager;
  /**
   * Reference to {@link SpacePresenter} service injected by the container
   */
  private SpacePresenter            spacePresenter;
  /**
   * Reference to {@link PortalNavigation} service injected by the container
   */
  private PortalNavigation          portalNavigation;
  /**
   * Reference to {@link AuthentificationService} service injected by the container
   */
  private AuthentificationService   authentificationService;

  /**
   * Use by container to inject {@link ForgeCfgService} implementation
   *
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
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
   * {@inheritDoc}
   */
  @Override
  public String getTheme()
  {
    return portalNavigation.getSettingValue(PortalSettingId.THEME);
  }

  /**
   * Use by container to inject {@link PortalNavigation} implementation
   *
   * @param pPortalNavigation
   *          the portalNavigation to set
   */
  public void setPortalNavigation(final PortalNavigation pPortalNavigation)
  {
    portalNavigation = pPortalNavigation;
  }

  /**
   * Use by container to inject {@link AuthentificationService} implementation
   *
   * @param pAuthentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalServiceImpl#getProject(String)
   */
  @Override
  public Project getMainProject() throws PortalException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Retrieve the main project information from service.");
    }
    return getProject(forgeConfigurationService.getForgeProjectId());
  }

  /**
   * Use by container to inject {@link ProjectPresenter} implementation
   *
   * @param pProjectPresenter
   *          the projectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  /**
   * Use by container to inject {@link UserPresenter} implementation
   *
   * @param pUserPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws PortalException
   */
  @Override
  public PortalApplication getPortalApplication(final String pPluginUUID, final String pToolUUID,
      final String pInstanceId, final String pProjectId, final String pPluginView) throws PortalException
  {
    final PortalApplication app = new PortalApplicationImpl();
    app.setId(pToolUUID);
    try
    {
      final ToolInstance tool = pluginsManager.getPluginService(pPluginUUID)
          .getToolInstanceProvisioningService().getToolInstanceByUUID(UUID.fromString(pToolUUID));
      final PortalURI pluginViewURI = portalNavigation.getApplicationURI(pPluginUUID, pInstanceId, pToolUUID,
          pProjectId, pPluginView, getCurrentUserLocale());
      final byte[] pluginIcon = pluginsManager.getPluginService(pPluginUUID).getPluginIcon();
      app.setName(tool.getName());
      app.setUniqueId(UUID.fromString(pToolUUID));
      app.setPortalURI(pluginViewURI);
      app.setAvailability(true);
      app.setIcon(ResourceUtils.buildImageResource(pluginIcon, tool.getName()));
    }
    catch (final PluginManagerException e)
    {
      throw new PortalException(String.format(
          "Unable to get plugin view url and icon with [plugin_uuid=%s,tool_uuid=%s,plugin_view=%s]",
          pPluginUUID, pToolUUID, pPluginView), e);
    }
    catch (final ToolInstanceProvisioningException e)
    {
      throw new PortalException(String.format(
          "Unable to get plugin instance with [plugin_uuid=%s,tool_uuid=%s,plugin_view=%s]", pPluginUUID,
          pToolUUID, pPluginView), e);
    }

    return app;
  }

  /**
   * Use by container to inject {@link SpacePresenter} implementation
   *
   * @param pSpacePresenter
   *          the spaceManager to set
   */
  public void setSpacePresenter(final SpacePresenter pSpacePresenter)
  {
    spacePresenter = pSpacePresenter;
  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalServiceImpl#getProjectNavigation(String)
   */
  @Override
  public List<PortalSpace> getMainNavigation(final Locale pLocale) throws PortalException
  {
    // Get the default spaces from navigation
    final List<PortalSpace> defaultSpaces = portalNavigation.getForgeSpaces(pLocale);

    // Get forge spaces and applications for forge project
    final String forgeProjectId = forgeConfigurationService.getForgeProjectId();
    defaultSpaces.addAll(getProjectSpaces(forgeProjectId, pLocale));

    // Get forge spaces and applications for forge project
    if (forgeConfigurationService.isReferentielCreated())
    {
      final String referentielProjectId = forgeConfigurationService.getReferentielProjectId();
      defaultSpaces.addAll(getProjectSpaces(referentielProjectId, pLocale));
    }
    return defaultSpaces;
  }



  /**
   * {@inheritDoc}
   * 
   * @see ProjectProxyManager#getProject(String)
   */
  @Override
  public Project getProject(final String pProjectId) throws PortalException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug(String.format("Retrieve the project information from service with [project=%s]", pProjectId));
    }
    if ((pProjectId == null) || "".equals(pProjectId))
    {
      throw new IllegalArgumentException("The given project id should not be null or empty");
    }

    try
    {
      return projectPresenter.getProject(pProjectId, true);
    }
    catch (final ProjectServiceException e)
    {
      throw new PortalException(String.format("Unable to get project element from the id given [id=%s]",
          pProjectId), e);
    }

  }



  /**
   * {@inheritDoc}
   * 
   * @see PortalNavigation#getProjectSpaces(String)
   */
  @Override
  public List<PortalSpace> getProjectNavigation(final String pProjectId, final Locale pLocale)
      throws PortalException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug(String.format("Retrieve the project navigation from service with [project=%s]", pProjectId));
    }

    if ((pProjectId == null) || "".equals(pProjectId))
    {
      throw new IllegalArgumentException("The given project id should not be null or empty");
    }

    // Get the default spaces from navigation
    final List<PortalSpace> projectSpaces = portalNavigation.getProjectSpaces(pProjectId, pLocale);

    // Get space added by administrator
    projectSpaces.addAll(getProjectSpaces(pProjectId, pLocale));
    return projectSpaces;
  }

  /**
   * This method will return the spaces and all applications added by project administrators
   * 
   * @param pProjectId
   *          project id
   * @return {@link List} of {@link PortalSpace} for the project adking
   * @throws PortalException
   *           thrown if spaces or applications cannot be retrieve from database
   */
  private List<PortalSpace> getProjectSpaces(final String pProjectId, final Locale pLocale)
      throws PortalException
  {
    final List<PortalSpace> projectSpaces = new ArrayList<PortalSpace>();

    try
    {
      final Map<Space, List<ProjectApplication>> allSpaces = spacePresenter
          .getAllSpacesWithApplications(pProjectId);
      final Set<Entry<Space, List<ProjectApplication>>> entrySet = allSpaces.entrySet();
      for (final Entry<Space, List<ProjectApplication>> entry : entrySet)
      {
        projectSpaces.add(build(pProjectId, entry.getKey(), entry.getValue(), pLocale));

      }
    }
    catch (final SpaceServiceException e)
    {
      throw new PortalException(String.format(
          "Unable to get project spaces and applications with [project_id=%s]", pProjectId), e);

    }
    return projectSpaces;
  }

  /**
   * {@inheritDoc}
   * 
   * @see portalNavigation#getMainDefaultApplication()
   */
  @Override
  public PortalApplication getMainDefaultApplication(final Locale pLocale) throws PortalException
  {
    return portalNavigation.getForgeDefaultApplication(pLocale);
  }

  /**
   * {@inheritDoc}
   * 
   * @see portalNavigation#getProjectDefaultApplication()
   */
  @Override
  public PortalApplication getProjectDefaultApplication(final String pProjectId, final Locale pLocale)
      throws PortalException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug(String.format("Retrieve the default application for project from service with [project=%s]",
          pProjectId));
    }

    if ((pProjectId == null) || "".equals(pProjectId))
    {
      throw new IllegalArgumentException("The given project id should not be null or empty");
    }
    return portalNavigation.getProjectDefaultApplication(pProjectId, pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Locale getCurrentUserLocale()
  {
    Locale locale = null;
    final String currentLogin = authentificationService.getCurrentUser();
    if ((currentLogin != null) && (!"".equals(currentLogin)))
    {
      try
      {
        locale = userPresenter.getUser(currentLogin).getLanguage().getLocale();
      }
      catch (final UserServiceException e)
      {
        // Nothing to do in this case
      }
    }
    return locale;
  }

  /**
   * {@inheritDoc}
   * 
   * @see MembershipProxyManager#getValidatedProjects(boolean)
   */
  @Override
  public List<Project> getUserProjects() throws PortalException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Retrieve the user's project from service");
    }
    try
    {
      final ProjectOptions projectOptions = projectPresenter.newProjectOptions(false, true, false);
      return projectPresenter.getValidatedProjects(projectOptions);
    }
    catch (final ProjectServiceException e)
    {
      throw new PortalException("Unable to get projects for the current user", e);
    }

  }

  /**
   * {@inheritDoc}
   * 
   * @see MembershipProxyManager#getPublicProjects()
   */
  @Override
  public List<Project> getPublicProjects() throws PortalException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Retrieve the public project for user from service");
    }
    try
    {
      final ProjectOptions projectOptions = projectPresenter.newProjectOptions(false, true, false);
      return projectPresenter.getPublicProjects(projectOptions);
    }
    catch (final ProjectServiceException e)
    {
      throw new PortalException("Unable to get public projects", e);
    }

  }

  /**
   * Build a {@link PortalSpace} and its {@link PortalApplication} from the given projectID, {@link Space} and
   * the {@link List} of {@link Application}
   * 
   * @param pProjectId
   *          the project id
   * @param pSpace
   *          the source {@link Space}
   * @param pApplications
   *          the {@link List} of {@link Application}
   * @return {@link PortalSpace} built
   * @throws PortalException
   *           if an error occured when getting the plugin metadata information
   */
  private PortalSpace build(final String pProjectId, final Space pSpace, final List<ProjectApplication> pApplications,
                            final Locale pLocale)
  {
    // build PortalSpace from core space
    final PortalSpace space = new PortalSpaceImpl();
    space.setId(pSpace.getUri());
    space.setName(pSpace.getName());
    space.setDescription(pSpace.getDescription());
    space.setRealmType(pSpace.getRealmType());

    // For each application build a PortalApplication
    for (final ProjectApplication application : pApplications)
    {
      try
      {
        final PortalApplication app = new PortalApplicationImpl();
        app.setId(application.getUri());
        app.setName(application.getName());
        app.setDescription(application.getDescription());
        app.setStatus(application.getStatus());

        final String uuid = application.getPluginUUID().toString();
        final boolean availablePlugin = pluginsManager.isAvailablePlugin(uuid);
        app.setAvailability(availablePlugin);
        final UUID pluginInstanceUUID = application.getPluginInstanceUUID();
        app.setUniqueId(pluginInstanceUUID);
        if (availablePlugin)
        {

          if (ApplicationStatus.ACTIVE.equals(application.getStatus()))
          {
            final PortalURI applicationURI = portalNavigation
                .getApplicationURI(uuid, application.getPluginInstanceUUID().toString(), null, pProjectId,
                    PluginViewEnum.DEFAULT.name(), pLocale);
            app.setPortalURI(applicationURI);

          }
          app.setIcon(ResourceUtils.buildImageResource(pluginsManager.getPluginService(uuid).getPluginIcon(),
              application.getName()));
        }
        space.addApplication(app);
      }
      catch (final Exception e)
      {
        LOG.error(
            String.format("Unable to build PortalApplication from plugin with [plugin_uuid=%s]",
                application.getPluginUUID()), e);
      }
    }

    return space;

  }

  /**
   * {@inheritDoc}
   * 
   * @see portalNavigation#getAccountSpaces()
   */
  @Override
  public PortalSpace getAccountSpaces(final Locale pLocale) throws PortalException
  {
    return portalNavigation.getAccountSpaces(pLocale);
  }

  /**
   * {@inheritDoc}
   * 
   * @see portalNavigation#getLink()
   */
  @Override
  public PortalApplication getLinkFromId(final PortalModuleId pId, final Locale pLocale)
      throws PortalException
  {
    PortalApplication returnApp = null;
    final List<PortalApplication> link = portalNavigation.getLink(pLocale);
    for (final PortalApplication portalApplication : link)
    {
      if (portalApplication.getId().equals(pId.getId()))
      {
        returnApp = portalApplication;
        break;
      }
    }
    return returnApp;
  }

}
