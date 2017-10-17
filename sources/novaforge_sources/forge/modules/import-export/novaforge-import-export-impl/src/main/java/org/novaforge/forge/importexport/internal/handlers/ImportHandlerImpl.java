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
package org.novaforge.forge.importexport.internal.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.GroupPresenter;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.importexport.datas.model.AppCategory;
import org.novaforge.forge.importexport.datas.model.AppType;
import org.novaforge.forge.importexport.datas.model.Applications.ApplicationElement;
import org.novaforge.forge.importexport.datas.model.Domains.DomainElement;
import org.novaforge.forge.importexport.datas.model.Groups.GroupElement;
import org.novaforge.forge.importexport.datas.model.MembershipsGroups.MembershipGroup;
import org.novaforge.forge.importexport.datas.model.MembershipsUsers.MembershipUser;
import org.novaforge.forge.importexport.datas.model.ProjectInfo;
import org.novaforge.forge.importexport.datas.model.Roles.RoleElement;
import org.novaforge.forge.importexport.datas.model.Users.UserElement;
import org.novaforge.forge.importexport.exceptions.ImportExportServiceException;
import org.novaforge.forge.importexport.handlers.ImportHandler;
import org.novaforge.forge.importexport.mappers.ImportDataMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Service implementation of {@link ImportHandler}
 * 
 * @author Guillaume Lamirand
 */
public class ImportHandlerImpl implements ImportHandler
{
  private static final Log LOG = LogFactory.getLog(ImportHandlerImpl.class);
  private UserPresenter        userPresenter;
  private GroupPresenter       groupPresenter;
  private ProjectPresenter     projectPresenter;
  private ProjectRolePresenter projectRolePresenter;
  private ApplicationPresenter applicationPresenter;
  private SpacePresenter       spacePresenter;
  private PluginsManager       pluginsManager;
  private MembershipPresenter  membershipPresenter;
  private ImportDataMapper     importDataMapper;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean importUser(final UserElement pUser)
  {
    boolean success = true;
    try
    {
      importUserWithException(pUser);
    }
    catch (final Exception e)
    {
      LOG.error(String.format("Unable to import the user with [id=%s]", pUser.getLogin()), e);
      success = false;
    }
    return success;
  }

  /**
   * This method will import the {@link User} given in parameter. It will throw an
   * {@link ImportExportServiceException} in
   * any case.
   * 
   * @param pUser
   *          the user to import
   * @throws ImportExportServiceException
   *           if any errors occured.
   */
  private void importUserWithException(final UserElement pUser) throws ImportExportServiceException
  {
    User userGot = getUser(pUser.getLogin());
    if (userGot != null)
    {
      LOG.info("This user is already existing so it not will be created");
    }
    else
    {
      LOG.info("This user cannot be found or update so it will be created");
      userGot = userPresenter.newUser();
      final User updateEntity = importDataMapper.updateEntity(pUser, userGot);
      try
      {
        userPresenter.createUser(updateEntity);
      }
      catch (final UserServiceException e)
      {
        throw new ImportExportServiceException(String.format("Unable to create user [id=%s]",
            pUser.getLogin()), e);
      }
    }
  }

  /**
   * This will get the {@link org.novaforge.forge.core.organization.model.User} according to the parameters.
   * 
   * @param pLogin
   *          the user login
   * @return {@link org.novaforge.forge.core.organization.model.User} found or <code>null</code>
   */
  private User getUser(final String pLogin)
  {
    org.novaforge.forge.core.organization.model.User userGot = null;
    try
    {
      userGot = userPresenter.getUser(pLogin);
    }
    catch (final UserServiceException e)
    {
      // Nothing to do in this case
    }
    return userGot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean importGroup(final String pProjectId, final GroupElement pGroup)
  {
    boolean success = true;
    try
    {
      importGroupWithException(pProjectId, pGroup);
    }
    catch (final Exception e)
    {
      LOG.error(String.format("Unable to import the group with [id=%s]", pGroup.getId()), e);
      success = false;
    }
    return success;
  }

  /**
   * This method will import the {@link Group} given in parameter. It will throw an
   * {@link ImportExportServiceException} in
   * any case.
   * 
   * @param pProjectId
   *          the project id to import group
   * @param pGroup
   *          the group to import
   * @throws ImportExportServiceException
   *           if any errors occured.
   */
  private void importGroupWithException(final String pProjectId, final GroupElement pGroup)
      throws ImportExportServiceException
  {
    final Group groupGot = getGroup(pProjectId, pGroup.getId());
    if (groupGot != null)
    {

      LOG.info("This group is already existing so it will be updated");
      final String oldName = groupGot.getName();
      final Group updateEntity = buildGroupWithUsers(pProjectId, pGroup, groupGot);
      try
      {
        groupPresenter.updateGroup(pProjectId, oldName, updateEntity);
      }
      catch (final GroupServiceException e)
      {
        throw new ImportExportServiceException(
            String.format("Unable to update group [id=%s]", pGroup.getId()), e);
      }
    }
    else
    {
      LOG.info("This group cannot be found or update so it will be created");
      final org.novaforge.forge.core.organization.model.Group updateEntity = buildGroupWithUsers(pProjectId,
          pGroup, groupPresenter.newGroup());
      try
      {
        groupPresenter.createGroup(updateEntity, pProjectId);
      }
      catch (final GroupServiceException e)
      {
        throw new ImportExportServiceException(
            String.format("Unable to create group [id=%s]", pGroup.getId()), e);
      }
    }
  }

  /**
   * This will get the {@link org.novaforge.forge.core.organization.model.Group} according to the parameters.
   *
   * @param pProjectId
   *     the project id to find the group
   * @param pGroupName
   *     the group name
   *
   * @return {@link org.novaforge.forge.core.organization.model.Group} found or <code>null</code>
   */
  private Group getGroup(final String pProjectId, final String pGroupName)
  {
    Group groupGot = null;
    try
    {
      groupGot = groupPresenter.getGroup(pProjectId, pGroupName);
    }
    catch (final GroupServiceException e)
    {
      // Nothing to do in this case
    }
    return groupGot;
  }

  private Group buildGroupWithUsers(final String pProjectId, final GroupElement pGroup,
      final Group pGroupEntity) throws ImportExportServiceException
  {
    final org.novaforge.forge.core.organization.model.Group updateEntity = importDataMapper.updateEntity(
        pGroup, pGroupEntity);

    if ((pGroup.getUsers() != null) && (pGroup.getUsers().getLogin() != null))
    {
      updateEntity.clearUsers();
      for (final String login : pGroup.getUsers().getLogin())
      {
        try
        {
          final org.novaforge.forge.core.organization.model.User user = userPresenter.getUser(login);
          updateEntity.addUser(user);
        }
        catch (final UserServiceException e)
        {
          throw new ImportExportServiceException(String.format(
              "Unable to find user [id=%s] in order to add to the group", login), e);
        }

      }
    }
    return updateEntity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean importProjectInfo(final String pProjectId, final ProjectInfo pProjectInfo)
  {
    boolean success = true;
    try
    {
      importProjectInfoWithException(pProjectId, pProjectInfo);
    }
    catch (final Exception e)
    {
      LOG.error(String.format("Unable to import the project info with [id=%s]", pProjectId), e);
      success = false;
    }
    return success;
  }

  /**
   * This method will import the {@link ProjectInfo} given in parameter. It will throw an
   * {@link ImportExportServiceException} in any case.
   * 
   * @param pProjectId
   *          the project id
   * @param pProjectInfo
   *          the project info to import
   * @throws ImportExportServiceException
   *           if any errors occured.
   */
  private void importProjectInfoWithException(final String pProjectId, final ProjectInfo pProjectInfo)
      throws ImportExportServiceException
  {
    Project projectGot = getProject(pProjectId);
    if (projectGot != null)
    {
      LOG.info("This project is already existing so it will be updated");
      final Project updateEntity = importDataMapper.updateEntity(pProjectId, pProjectInfo, projectGot);
      try
      {
        projectPresenter.updateProject(pProjectInfo.getName(), updateEntity);
      }
      catch (final ProjectServiceException e)
      {
        throw new ImportExportServiceException(String.format("Unable to update project [id=%s]", pProjectId),
            e);
      }
    }
    else
    {
      LOG.info("This project cannot be found so it will be created");
      projectGot = projectPresenter.newProject();
      final Project updateEntity = importDataMapper.updateEntity(pProjectId, pProjectInfo, projectGot);
      try
      {
        projectPresenter.createProject(updateEntity);
        projectPresenter.validateProject(pProjectId);
      }
      catch (final ProjectServiceException e)
      {
        throw new ImportExportServiceException(String.format("Unable to create project [id=%s]", pProjectId),
            e);
      }
    }
  }

  /**
   * This will get the {@link org.novaforge.forge.core.organization.model.Project} according to the
   * parameters.
   * 
   * @param pProjectId
   *          the project id to find
   * @return {@link org.novaforge.forge.core.organization.model.Project} found or <code>null</code>
   */
  private Project getProject(final String pProjectId)
  {
    org.novaforge.forge.core.organization.model.Project projectGot = null;
    try
    {
      projectGot = projectPresenter.getProject(pProjectId, false);
    }
    catch (final ProjectServiceException e)
    {
      // Nothing to do in this case
    }
    return projectGot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean importRole(final String pProjectId, final RoleElement pRole)
  {
    boolean success = true;
    try
    {
      importRoleWithException(pProjectId, pRole);
    }
    catch (final Exception e)
    {
      LOG.error(
          String.format("Unable to import the role with [project_id=%s, name=%s]", pProjectId,
              pRole.getName()), e);
      success = false;
    }
    return success;
  }

  /**
   * This method will import the {@link Role} given in parameter. It will throw an
   * {@link ImportExportServiceException} in
   * any case.
   * 
   * @param pProjectId
   *          the project id
   * @param pRole
   *          the role to import
   * @throws ImportExportServiceException
   *           if any errors occured.
   */
  private void importRoleWithException(final String pProjectId, final RoleElement pRole)
      throws ImportExportServiceException
  {
    ProjectRole roleGot = getProjectRole(pProjectId, pRole.getName());
    if (roleGot != null)
    {
      LOG.info("This role is already existing so it will be updated");
    }
    else
    {
      LOG.info("This role cannot be found so it will be created");
      roleGot = projectRolePresenter.newRole();
      final ProjectRole updateEntity = importDataMapper.updateEntity(pRole, roleGot);
      try
      {
        projectRolePresenter.createRole(updateEntity, pProjectId);
      }
      catch (final ProjectServiceException e)
      {
        throw new ImportExportServiceException(String.format(
            "Unable to create role [project_id=%s, role_name=%s]", pProjectId, pRole.getName()), e);
      }
    }
  }

  /**
   * This will get the {@link org.novaforge.forge.core.organization.model.ProjectRole} according to the
   * parameters.
   * 
   * @param pProjectId
   *          the project id
   * @param pRoleName
   *          the role name id to find
   * @return {@link org.novaforge.forge.core.organization.model.ProjectRole} found or <code>null</code>
   */
  private ProjectRole getProjectRole(final String pProjectId, final String pRoleName)
  {
    ProjectRole roleGot = null;
    try
    {
      roleGot = projectRolePresenter.getRole(pRoleName, pProjectId);
    }
    catch (final ProjectServiceException e)
    {
      // Nothing to do in this case
    }
    return roleGot;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ImportExportServiceException
   */
  @Override
  public boolean importDomain(final String pProjectId, final DomainElement pDomain)
  {
    boolean success = true;
    try
    {
      importDomainWithException(pProjectId, pDomain);
    }
    catch (final Exception e)
    {
      LOG.error(
          String.format("Unable to import the domain with [project_id=%s, name=%s]", pProjectId,
              pDomain.getName()), e);
      success = false;
    }
    return success;
  }

  /**
   * This method will import the {@link Role} given in parameter. It will throw an
   * {@link ImportExportServiceException} in
   * any case.
   * 
   * @param pProjectId
   *          the project id
   * @param pRole
   *          the role to import
   * @throws ImportExportServiceException
   *           if any errors occured.
   */
  private void importDomainWithException(final String pProjectId, final DomainElement pDomain)
      throws ImportExportServiceException
  {
    Space space = getSpace(pProjectId, pDomain.getName());
    if (space != null)
    {
      LOG.info("This space is already existing so it will be updated");
      try
      {
        if (!space.getName().equals(pDomain.getName()))
        {
          space.setName(pDomain.getName());
          spacePresenter.updateSpace(pProjectId, space.getName(), space);
        }
      }
      catch (final Exception e)
      {
        throw new ImportExportServiceException(String.format(
            "Unable to update space [project_id=%s, space_name=%s]", pProjectId, pDomain.getName()), e);
      }
    }
    else
    {
      LOG.info("This space cannot be found so it will be created");
      try
      {
        space = spacePresenter.newSpace();
        space.setName(pDomain.getName());
        spacePresenter.addSpace(pProjectId, space);
      }
      catch (final Exception e)
      {
        throw new ImportExportServiceException(String.format(
            "Unable to create space [project_id=%s, space_name=%s]", pProjectId, pDomain.getName()), e);
      }
    }
  }

  /**
   * This will get the {@link org.novaforge.forge.core.organization.model.Space} according to the parameters.
   *
   * @param pProjectId
   *          the project id
   * @param pSpaceName
   *          the space name to find
   * @return {@link Space} found or <code>null</code>
   */
  private Space getSpace(final String pProjectId, final String pSpaceName)
  {
    Space spaceGot = null;
    try
    {
      final List<Space> spaces = spacePresenter.getAllSpaces(pProjectId);

      for (final Space space : spaces)
      {
        if (space.getName().equals(pSpaceName))
        {
          spaceGot = space;
        }
      }
    }
    catch (final SpaceServiceException e)
    {
      // Nothing to do in this case
    }
    return spaceGot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean importApp(final String pProjectId, final String pSpaceName, final ApplicationElement pApp)
  {
    boolean success = true;
    try
    {
      importAppWithException(pProjectId, pSpaceName, pApp);
    }
    catch (final Exception e)
    {
      LOG.error(String.format("Unable to import the app with [project_id=%s, space_name=%s, name=%s]",
          pProjectId, pSpaceName, pApp.getName()), e);
      success = false;
    }
    return success;
  }

  /**
   * This method will import the {@link Role} given in parameter. It will throw an
   * {@link ImportExportServiceException} in
   * any case.
   *
   * @param pProjectId
   *          the project id
   * @param pRole
   *          the role to import
   * @throws ImportExportServiceException
   *           if any errors occured.
   */
  private void importAppWithException(final String pProjectId, final String pSpaceName,
      final ApplicationElement pApp) throws ImportExportServiceException
  {
    final Space space = getSpace(pProjectId, pSpaceName);
    if (space != null)
    {
      final Map<String, String> buildMapping = importDataMapper.buildMapping(pApp.getRolesMapping());
      final org.novaforge.forge.core.organization.model.Application app = getApplication(pProjectId,
          space.getUri(), pApp.getName());
      if (app != null)
      {
        LOG.info("This app is already existing so it will be updated");
        try
        {
          applicationPresenter.updateRoleMapping(pProjectId, app.getUri(), buildMapping);
        }
        catch (final Exception e)
        {
          throw new ImportExportServiceException(String.format(
              "Unable to update app role mapping [project_id=%s, app_uri=%s]", pProjectId, app.getUri()), e);
        }
      }
      else
      {
        LOG.info("This app cannot be found so it will be created");
        try
        {
          final PluginMetadata pluginMetadata = getPluginMetadata(pApp.getCategory(), pApp.getType());
          if ((pluginMetadata != null) && (pluginMetadata.isAvailable()))
          {
            applicationPresenter.addApplication(pProjectId, space.getUri(), pApp.getName(),
                pApp.getDescription(), UUID.fromString(pluginMetadata.getUUID()), buildMapping);
          }
          else
          {

            throw new ImportExportServiceException(String.format(
                "There is no plugin available with [category=%s, type=%s]", pApp.getCategory(),
                pApp.getType()));
          }
        }
        catch (final Exception e)
        {
          throw new ImportExportServiceException(String.format(
              "Unable to create app [project_id=%s, space_uri=%s, app_name=%s]", pProjectId, pSpaceName,
              pApp.getName()), e);
        }
      }
    }
    else
    {
      throw new ImportExportServiceException(String.format(
          "Unable to find parent space [project_id=%s, space_name=%s]", pProjectId, pSpaceName));
    }
  }

  /**
   * This will get the {@link org.novaforge.forge.core.organization.model.Application} according to the
   * parameters.
   * 
   * @param pProjectId
   *          the project id
   * @param pSpaceUri
   *          the space parent uri
   * @param pAppName
   *          the app name to find
   * @return {@link Application} found or <code>null</code>
   */
  private Application getApplication(final String pProjectId, final String pSpaceUri, final String pAppName)
  {
    Application appGot = null;
    try
    {
      final List<org.novaforge.forge.core.organization.model.ProjectApplication> apps = applicationPresenter
          .getAllSpaceApplications(pSpaceUri, pProjectId);

      for (final org.novaforge.forge.core.organization.model.Application app : apps)
      {
        if (app.getName().equals(pAppName))
        {
          appGot = app;
        }
      }
    }
    catch (final ApplicationServiceException e)
    {
      // Nothing to do in this case
    }
    return appGot;
  }

  /**
   * This will get the {@linkPluginMetadata} according to the parameters.
   * 
   * @param pCat
   *          the plugin category
   * @param pType
   *          the plugin type
   * @return {@link PluginMetadata} found or <code>null</code>
   */
  private PluginMetadata getPluginMetadata(final AppCategory pCat, final AppType pType)
  {
    PluginMetadata pluginGot = null;
    try
    {
      if ((pCat != null) && (pType != null))
      {
        final List<PluginMetadata> plugins = pluginsManager.getPluginsMetadataByCategory(pCat.value());
        for (final PluginMetadata pluginMetadata : plugins)
        {
          if (pluginMetadata.getType().equals(pType.value()))
          {
            pluginGot = pluginMetadata;
          }
        }
      }
    }
    catch (final PluginManagerException e)
    {
      // Nothing to do in this case
    }
    return pluginGot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean importUserMembership(final String pProjectId, final MembershipUser pMembershipUser)
  {
    boolean success = true;
    final Set<String> roles = new HashSet<String>();
    for (final String role : pMembershipUser.getRole())
    {
      roles.add(role);
    }
    try
    {
      User user = userPresenter.getUser(pMembershipUser.getLogin());      
      membershipPresenter.addUserMembership(pProjectId, user.getUuid(), roles, null, false);
    }
    catch (final Exception e)
    {
      success = false;
      LOG.error(String.format(
          "Unable to import the user membership with [project_id=%s, login=%s, roles=%s]", pProjectId,
          pMembershipUser.getLogin(), pMembershipUser.getRole()), e);
    }
    return success;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean importGroupMembership(final String pProjectId, final MembershipGroup pMembershipGroup)
  {
    boolean success = true;
    // Add new ones
    final Set<String> roles = new HashSet<String>();
    for (final String role : pMembershipGroup.getRole())
    {
      roles.add(role);
    }
    try
    {
      Group group = groupPresenter.getGroup(pProjectId, pMembershipGroup.getId());
      membershipPresenter.addGroupMembership(pProjectId, group.getUuid(), roles, null);
    }
    catch (final Exception e)
    {
      success = false;
      LOG.error(String.format(
          "Unable to import the user membership with [project_id=%s, login=%s, roles=%s]", pProjectId,
          pMembershipGroup.getId(), pMembershipGroup.getRole()), e);
    }
    return success;
  }

  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

  public void setGroupPresenter(final GroupPresenter pGroupPresenter)
  {
    groupPresenter = pGroupPresenter;
  }

  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  public void setProjectRolePresenter(final ProjectRolePresenter pProjectRolePresenter)
  {
    projectRolePresenter = pProjectRolePresenter;
  }

  public void setApplicationPresenter(final ApplicationPresenter pApplicationPresenter)
  {
    applicationPresenter = pApplicationPresenter;
  }

  public void setSpacePresenter(final SpacePresenter pSpacePresenter)
  {
    spacePresenter = pSpacePresenter;
  }

  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  public void setMembershipPresenter(final MembershipPresenter pMembershipPresenter)
  {
    membershipPresenter = pMembershipPresenter;
  }

  public void setImportDataMapper(final ImportDataMapper pImportDataMapper)
  {
    importDataMapper = pImportDataMapper;
  }

}
