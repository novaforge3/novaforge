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
package org.novaforge.forge.remote.services.internal.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectInfo;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.categories.PluginCategoryService;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.portal.models.PortalURI;
import org.novaforge.forge.portal.services.PortalNavigation;
import org.novaforge.forge.remote.services.core.RemoteProjectService;
import org.novaforge.forge.remote.services.exception.ExceptionCode;
import org.novaforge.forge.remote.services.exception.RemoteServiceException;
import org.novaforge.forge.remote.services.model.core.ForgeApplication;
import org.novaforge.forge.remote.services.model.core.ForgeNode;
import org.novaforge.forge.remote.services.model.core.ForgeProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Concrete implementation of the RemoteProjectService.
 * 
 * @author rols-p
 */
@org.apache.cxf.interceptor.InInterceptors(
    interceptors = { "org.novaforge.forge.core.security.cxf.BasicAuthAuthorizationInterceptor" })
public class RemoteProjectServiceImpl extends AbstractRemoteService implements RemoteProjectService
{

  /**
   * Logger
   */
  private static final Log        LOGGER = LogFactory.getLog(RemoteProjectServiceImpl.class);

  /**
   * Project Proxy Manager
   */
  private ProjectPresenter        projectPresenter;
  /**
   * Node Proxy Manager
   */
  private SpacePresenter          spacePresenter;

  /**
   * Plugins Manager
   */
  private PluginsManager          pluginsManager;

  /**
   * Authentification service
   */
  private AuthentificationService authentificationService;

  /**
   * USer Proxy Manager
   */
  private UserPresenter           userPresenter;
  /**
   * PortalNavigation service
   */
  private PortalNavigation        portalNavigation;

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeProject getForgeProject(final String pProjectId) throws RemoteServiceException
  {
    ForgeProject forgeProject = null;
    try
    {
      final ProjectInfo forgeProjectinfo = projectPresenter.getProjectInfo(pProjectId);
      forgeProject = new ForgeProject();
      forgeProject.setId(forgeProjectinfo.getProjectId());
      forgeProject.setName(forgeProjectinfo.getName());
      forgeProject.setDescription(forgeProjectinfo.getDescription());
      forgeProject.setLastModified(forgeProjectinfo.getLastModified());

    }
    catch (final ProjectServiceException e)
    {
      e.printStackTrace();
      throw new RemoteServiceException(ExceptionCode.ERR_GET_FORGE_PROJECT_INFO, e);
    }
    return forgeProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ForgeProject> getForgeProjects() throws RemoteServiceException
  {
    final List<ForgeProject> forgeProjects = new ArrayList<ForgeProject>();

    try
    {
      final ProjectOptions projectOpts = projectPresenter.newProjectOptions(false, false, true);
      final List<Project> projectList = projectPresenter.getValidatedProjects(projectOpts);
      for (final Project project : projectList)
      {
        final ForgeProject forgeProject = new ForgeProject();
        forgeProject.setId(project.getProjectId());
        forgeProject.setName(project.getName());
        forgeProject.setDescription(project.getDescription());
        forgeProject.setLastModified(project.getLastModified());
        forgeProjects.add(forgeProject);
      }
    }
    catch (final ProjectServiceException e)
    {
      throw new RemoteServiceException(ExceptionCode.ERR_LIST_FORGE_PROJECTS, e);
    }

    return forgeProjects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ForgeNode> getAllForgeProjectNodes(final String pProjectId) throws RemoteServiceException
  {
    final List<ForgeNode> spaceNodeList = new ArrayList<ForgeNode>();
    final Locale userLocale = getUserLocale();
    try
    {
      final Map<Space, List<ProjectApplication>> spaces = spacePresenter
          .getAllSpacesWithApplications(pProjectId);
      final Set<Entry<Space, List<ProjectApplication>>> entrySet = spaces.entrySet();
      for (final Entry<Space, List<ProjectApplication>> entry : entrySet)
      {
        final ForgeNode spaceNode = new ForgeNode();
        spaceNode.setName(entry.getKey().getName());
        spaceNode.setUri(entry.getKey().getUri());
        final List<ForgeNode> applicationNodeList = new ArrayList<ForgeNode>();
        for (final ProjectApplication application : entry.getValue())
        {
          final String pluginUUID = (application.getPluginUUID() == null) ? "" : application.getPluginUUID()
              .toString();
          if (pluginUUID.isEmpty())
          {
            final String errorMsg = String.format("Error getting the Plugin UUID for the application uri=%s",
                application.getUri());
            LOGGER.error(errorMsg);
            throw new RemoteServiceException(ExceptionCode.ERR_LIST_FORGE_PROJECT_SPACES_APPLICATIONS,
                errorMsg);
          }
          final ForgeNode applicationNode = new ForgeNode();
          applicationNode.setName(application.getName());
          applicationNode.setUri(application.getUri());
          final ForgeApplication forgeApplication = new ForgeApplication();
          forgeApplication.setInstanceId(application.getPluginInstanceUUID().toString());
          forgeApplication.setPluginUUID(application.getPluginUUID().toString());

          final PortalURI applicationUrl = portalNavigation.getApplicationURI(application.getPluginUUID()
              .toString(), application.getPluginInstanceUUID().toString(), null, pProjectId,
              PluginViewEnum.DEFAULT.toString(), userLocale);
          if (!applicationUrl.isInternalModule())
          {
            forgeApplication.setToolUrl(applicationUrl.getAbsoluteURL().toExternalForm());
          }

          final PluginMetadata pluginMetadataByUUID = pluginsManager.getPluginMetadataByUUID(pluginUUID);
          forgeApplication.setCategory(pluginMetadataByUUID.getCategory());
          forgeApplication.setType(pluginMetadataByUUID.getType());

          try
          {

            final PluginCategoryService pluginCategoryService = pluginsManager.getPluginCategoryService(
                application.getPluginUUID().toString(), PluginCategoryService.class);
            final String accessInfo = pluginCategoryService.getApplicationAccessInfo(application
                .getPluginInstanceUUID().toString(), userLocale);
            forgeApplication.setAccessInfo(accessInfo);
          }
          catch (final Exception e)
          {
            LOGGER.warn(String.format(
                "Unable to get the Application access information [application=%s, error=%s]",
                application.getName(), e));
          }

          applicationNode.setApplication(forgeApplication);
          applicationNodeList.add(applicationNode);
        }
        spaceNode.setChilds(applicationNodeList);
        spaceNodeList.add(spaceNode);
      }
    }
    catch (final Exception e)
    {
      LOGGER.error(String.format("Error getting the Plugin related information error=%s", e));
      throw new RemoteServiceException(ExceptionCode.ERR_LIST_FORGE_PROJECT_SPACES_APPLICATIONS, e);
    }
    return spaceNodeList;
  }

  /**
   * Returns the User Prefered Locale.
   * 
   * @return a not null User prefered Locale
   * @throws UserServiceException
   */
  private Locale getUserLocale()
  {
    String currentUser = null;
    Locale userLocale = null;
    try
    {
      currentUser = authentificationService.getCurrentUser();
      final Language userLangage = userPresenter.getUser(currentUser).getLanguage();
      userLocale = userLangage.getLocale();
    }
    catch (final UserServiceException e)
    {
      LOGGER.warn(String.format(
          "Unable to get the user Locale according to it's prefered language [user=%s, error=%s]",
          currentUser, e));
      userLocale = Locale.ENGLISH;
    }

    return userLocale;
  }

  /**
   * @param pProjectPresenter
   *          the projectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  /**
   * @param pSpacePresenter
   *          the spacePresenter to set
   */
  public void setSpacePresenter(final SpacePresenter pSpacePresenter)
  {
    spacePresenter = pSpacePresenter;
  }

  /**
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  /**
   * @param pAuthentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  /**
   * @param pUserPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

  /**
   * @param pPortalNavigation
   *          the portalNavigation to set
   */
  public void setPortalNavigation(final PortalNavigation pPortalNavigation)
  {
    portalNavigation = pPortalNavigation;
  }
}
