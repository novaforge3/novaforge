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
import org.novaforge.forge.commons.technical.validation.ValidationService;
import org.novaforge.forge.commons.technical.validation.ValidatorResponse;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.dao.GroupDAO;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.delegates.MessageDelegate;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.handlers.MembershipHandler;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.GroupInfo;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.GroupService;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.route.OptionalPluginGroupQueue;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.domain.route.PluginQueues;
import org.novaforge.forge.core.plugins.services.PluginsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of {@link GroupService}
 * 
 * @author sbenoist
 * @see GroupService
 */
public class GroupServiceImpl implements GroupService
{
  private static final Log LOG = LogFactory.getLog(GroupServiceImpl.class);
  /**
   * Reference to {@link GroupDAO} service injected by the container
   */
  private GroupDAO                  groupDAO;
  /**
   * Reference to {@link ProjectDAO} service injected by the container
   */
  private ProjectDAO                projectDAO;
  /**
   * Reference to {@link MembershipDAO} service injected by the container
   */
  private MembershipDAO             membershipDAO;
  /**
   * Reference to {@link ValidationService} service injected by the container
   */
  private ValidationService         validationService;
  /**
   * Reference to {@link ApplicationService} service injected by the container
   */
  private ApplicationService        applicationService;
  /**
   * Reference to {@link MembershipHandler} service injected by the container
   */
  private MembershipHandler         membershipHandler;
  /**
   * Reference to {@link ForgeConfigurationService} service injected by the
   * container
   */
  private ForgeConfigurationService forgeConfigurationService;
  /**
   * Reference to {@link PluginsManager} service injected by the container
   */
  private PluginsManager            pluginsManager;
  /**
   * Reference to {@link MessageDelegate} service injected by the container
   */
  private MessageDelegate           messageDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public Group newGroup()
  {
    return groupDAO.newGroup();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createGroup(final Group pGroup, final String pProjectId) throws GroupServiceException
  {
    checkGroup("", pGroup, pProjectId);
    try
    {
      final Project project = projectDAO.findByProjectId(pProjectId);
      // add the group to the project
      project.addGroup(pGroup);
      projectDAO.update(project);
    }
    catch (final Exception e)
    {
      throw new GroupServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws GroupServiceException
   */
  @Override
  public Group getGroup(final String pProjectId, final String pName) throws GroupServiceException
  {
    try
    {
      return groupDAO.findByProjectIdAndName(pProjectId, pName);
    }
    catch (final Exception e)
    {
      throw new GroupServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateGroup(final String pProjectId, final String pOldName, final Group pGroup,
      final String pCurrentUser) throws GroupServiceException
  {
    checkGroup(pOldName, pGroup, pProjectId);

    // check if users have been added or removed
    final Group storedGroup = getGroup(pProjectId, pOldName);
    final Set<User> diffUsers = getDiffUsers(storedGroup.getUsers(), pGroup.getUsers());

    final List<User> usersToAdd = new ArrayList<User>();
    final List<User> usersToRemove = new ArrayList<User>();
    if (!diffUsers.isEmpty())
    {
      usersToAdd.addAll(getUsersToAdd(storedGroup.getUsers(), pGroup.getUsers()));
      usersToRemove.addAll(getUsersToRemove(storedGroup.getUsers(), pGroup.getUsers()));
    }

    if ((forgeConfigurationService.getForgeProjectId().equals(pProjectId)) && (pGroup.isVisible()))
    {
      updatePublicGroup(diffUsers, usersToAdd, usersToRemove, pProjectId, pOldName, pGroup, pCurrentUser);

      // send message to notify applications for all projects
      final List<Project> projects = projectDAO.findAllByStatus(ProjectStatus.VALIDATED);
      for (final Project project : projects)
      {
        sendGroupMessage(pGroup, project.getProjectId(), PluginQueueAction.UPDATE, pCurrentUser);
      }
    }
    else
    {
      updatePrivateGroup(diffUsers, usersToAdd, usersToRemove, pProjectId, pOldName, pGroup, pCurrentUser);

      // send message to notify applications
      sendGroupMessage(pGroup, pProjectId, PluginQueueAction.UPDATE, pCurrentUser);
    }
  }

  private Set<User> getDiffUsers(final List<User> pOld, final List<User> pNew)
  {
    final Set<User> diff = new HashSet<User>();

    final List<User> usersToRemove = new ArrayList<User>(pOld);
    usersToRemove.removeAll(pNew);

    final List<User> usersToAdd = new ArrayList<User>(pNew);
    usersToAdd.removeAll(pOld);

    for (final User user : usersToRemove)
    {
      diff.add(user);
    }

    for (final User user : usersToAdd)
    {
      diff.add(user);
    }

    return diff;
  }

  private List<User> getUsersToAdd(final List<User> pOld, final List<User> pNew)
  {
    final List<User> usersToAdd = new ArrayList<User>(pNew);
    usersToAdd.removeAll(pOld);
    return usersToAdd;
  }

  private List<User> getUsersToRemove(final List<User> pOld, final List<User> pNew)
  {
    final List<User> usersToRemove = new ArrayList<User>(pOld);
    usersToRemove.removeAll(pNew);
    return usersToRemove;
  }

  private void updatePublicGroup(final Set<User> pDiffUsers, final List<User> pUsersToAdd,
      final List<User> pUsersToRemove, final String pProjectId, final String pGroupOldName,
      final Group pGroup, final String pCurrentUser) throws GroupServiceException
  {
    try
    {
      // Get all the projects where the group has any membership
      final List<String> projects = membershipDAO.findProjectsIdForActors(pGroup.getUuid());
      final Map<String, Map<String, Map<String, Membership>>> projectToolsMemberships = new HashMap<String, Map<String, Map<String, Membership>>>();
      final Map<String, List<ProjectApplication>> userApplications = new HashMap<String, List<ProjectApplication>>();
      if (!pDiffUsers.isEmpty())
      {
        for (final String projectId : projects)
        {
          // get all the applications concerned by the propagation
          final List<ProjectApplication> projectApplication = retrieveUserApplication(projectId);
          userApplications.put(projectId, projectApplication);

          final Map<String, Map<String, Membership>> initialToolsMemberships = retrieveInitialToolsMembership(
              pDiffUsers, projectId, projectApplication);

          projectToolsMemberships.put(projectId, initialToolsMemberships);
        }
      }

      // Update the group and the project
      persistGroup(pProjectId, pGroup);

      if (!pDiffUsers.isEmpty())
      {
        // propagate the membership
        for (final Map.Entry<String, Map<String, Map<String, Membership>>> entryProject : projectToolsMemberships
            .entrySet())
        {
          final String projectId = entryProject.getKey();
          if (userApplications.containsKey(projectId))
          {
            sendPropagation(pUsersToAdd, pUsersToRemove, projectId, pCurrentUser, entryProject.getValue(),
                userApplications.get(projectId));
          }
        }
      }
    }
    catch (final ProjectServiceException e)
    {
      throw new GroupServiceException(String.format(
          "Unable to propagate group changes on applications with [name=%s]", pGroup.getName()), e);
    }
    catch (final ApplicationServiceException e)
    {
      throw new GroupServiceException(
          String.format("Unable to get tol memberships for group changes on applications with [name=%s]",
              pGroup.getName()), e);
    }
    catch (final Exception e)
    {
      throw new GroupServiceException("a technical error occured", e);
    }
  }

  private void sendGroupMessage(final Group pGroup, final String pProjectId, final PluginQueueAction pAction,
                                final String pUsername) throws GroupServiceException
  {
    try
    {
      // get all the applications
      final List<ProjectApplication> apps = applicationService.getAllProjectApplications(pProjectId);
      // send a message for each application if plugin subscribes to group notification
      for (final ProjectApplication app : apps)
      {
        if (ApplicationStatus.ACTIVE.equals(app.getStatus()))
        {
          final PluginMetadata pluginMetadataByUUID = pluginsManager.getPluginMetadataByUUID(app.getPluginUUID()
                                                                                                .toString());
          if (pluginMetadataByUUID != null)
          {
            final PluginQueues pluginQueues = pluginMetadataByUUID.getJMSQueues();
            if (pluginQueues instanceof OptionalPluginGroupQueue)
            {
              messageDelegate.sendGroupMessage(app.getPluginUUID().toString(), app.getPluginInstanceUUID().toString(),
                                               pProjectId, pGroup, pAction.getLabel(), pUsername);
            }
          }
        }
      }
    }
    catch (final Exception e)
    {
      throw new GroupServiceException(String
                                          .format("unable to propagate %s of group with name=%s for all its applications",
                                                  pAction.getLabel(), pGroup.getName()), e);
    }
  }

  private void updatePrivateGroup(final Set<User> pDiffUsers, final List<User> pUsersToAdd,
                                  final List<User> pUsersToRemove, final String pProjectId, final String pGroupOldName,
                                  final Group pGroup, final String pCurrentUser) throws GroupServiceException
  {
    try
    {
      // check if users have been added or removed
      final Map<String, Map<String, Membership>> initialToolsMemberships = new HashMap<String, Map<String, Membership>>();
      final List<ProjectApplication> userApplications = new ArrayList<ProjectApplication>();
      if (!pDiffUsers.isEmpty())
      {
        // get all the applications concerned by the propagation
        userApplications.addAll(retrieveUserApplication(pProjectId));

        initialToolsMemberships.putAll(retrieveInitialToolsMembership(pDiffUsers, pProjectId, userApplications));
      }
      // Update the group and the project
      persistGroup(pProjectId, pGroup);

      if (!pDiffUsers.isEmpty())
      {
        sendPropagation(pUsersToAdd, pUsersToRemove, pProjectId, pCurrentUser, initialToolsMemberships,
                        userApplications);
      }
    }
    catch (final ProjectServiceException e)
    {
      throw new GroupServiceException(String.format("Unable to propagate group changes on applications with [name=%s]",
                                                    pGroup.getName()), e);
    }
    catch (final ApplicationServiceException e)
    {
      throw new GroupServiceException(String
                                          .format("Unable to get tol memberships for group changes on applications with [name=%s]",
                                                  pGroup.getName()), e);
    }
    catch (final Exception e)
    {
      throw new GroupServiceException("a technical error occured", e);
    }
  }

  private List<ProjectApplication> retrieveUserApplication(final String pProjectId) throws ApplicationServiceException
  {
    return applicationService.getAllProjectApplications(pProjectId);
  }

  private Map<String, Map<String, Membership>> retrieveInitialToolsMembership(final Set<User> pDiffUsers,
                                                                              final String pProjectId,
                                                                              final List<ProjectApplication> userApplications)
      throws ProjectServiceException, ApplicationServiceException
  {
    final Map<String, Map<String, Membership>> initialToolsMemberships = new HashMap<String, Map<String, Membership>>();
    // get all the tools initials memberships to prepare propagation for all
    // users of the group
    for (final User user : pDiffUsers)
    {
      final String login = user.getLogin();
      initialToolsMemberships.put(login, membershipHandler.getToolsMemberships(pProjectId, userApplications, login));
    }
    return initialToolsMemberships;
  }

  /**
   * Update the group and the project : this will allows to add or remove
   * meberships for users if diff
   * users is not empty
   *
   * @param pProjectId
   * @param pGroup
   */
  private void persistGroup(final String pProjectId, final Group pGroup)
  {
    final Project project = projectDAO.findByProjectId(pProjectId);
    projectDAO.update(project);
    groupDAO.update(pGroup);
  }

  private void sendPropagation(final List<User> pUsersToAdd, final List<User> pUsersToRemove, final String pProjectId,
                               final String pCurrentUser,
                               final Map<String, Map<String, Membership>> initialToolsMemberships,
                               final List<ProjectApplication> userApplications) throws ProjectServiceException
  {
    // propagate the membership
    for (final Map.Entry<String, Map<String, Membership>> entry : initialToolsMemberships.entrySet())
    {
      membershipHandler.sendUserMembershipsPropagation(entry.getValue(), userApplications, pProjectId, entry.getKey(),
                                                       pCurrentUser);
    }

    // send an email to notify changes to user
    if (!pUsersToAdd.isEmpty())
    {
      membershipHandler.notifyGroupForMembershipsChange(pProjectId, pUsersToAdd, true, MailDelegateEnum.ADD_MEMBERSHIP);
    }

    if (!pUsersToRemove.isEmpty())
    {
      membershipHandler.notifyGroupForMembershipsChange(pProjectId, pUsersToRemove, true,
                                                        MailDelegateEnum.REMOVE_MEMBERSHIP);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Group deleteGroup(final String pProjectId, final UUID pGroupUUID) throws GroupServiceException
  {
    try
    {
      // get group
      final Group group = groupDAO.findByUUID(pGroupUUID);
      // check if it exists any membership with this group role on all projects for public group
      if (group.isVisible())
      {
        final long nbMemberships = membershipDAO.countForActor(group.getUuid());
        if (nbMemberships > 0)
        {
          throw new GroupServiceException(ExceptionCode.ERR_DELETE_GROUP_USER_WITH_MEMBERSHIP_EXIST,
              String.format("projectId=%s, uuid=%s", pProjectId, pGroupUUID));
        }
      }

      final Project project = projectDAO.findByProjectId(pProjectId);
      // delete group reference
      project.removeGroup(group);
      projectDAO.update(project);

      // send message to notify applications
      if (group.isVisible())
      {
        // send message to notify applications for all projects
        final List<Project> projects = projectDAO.findAllByStatus(ProjectStatus.VALIDATED);
        for (final Project projectElmt : projects)
        {
          sendGroupMessage(group, projectElmt.getProjectId(), PluginQueueAction.DELETE, null);
        }
      }
      else
      {
        sendGroupMessage(group, pProjectId, PluginQueueAction.DELETE, null);
      }

      return group;
    }
    catch (final Exception e)
    {
      if (e instanceof GroupServiceException)
      {
        throw e;
      }
      else
      {
        throw new GroupServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws GroupServiceException
   */
  @Override
  public List<Group> getAllGroups(final String pProjectId, final boolean pWithPublic)
      throws GroupServiceException
  {
    try
    {
      List<Group> groups = null;

      if (pWithPublic)
      {
        groups = groupDAO.findByProjectWithPublic(pProjectId);
      }
      else
      {
        groups = groupDAO.findByProjectWithoutPublic(pProjectId);
      }
      return groups;
    }
    catch (final Exception e)
    {
      throw new GroupServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GroupInfo> getAllUserGroups(final UUID pUserUUID) throws GroupServiceException
  {
    return groupDAO.findGroupsInfosForUser(pUserUUID);
  }

  /**
   * Check if a group is correct to be created
   *
   * @param group
   *
   * @throws GroupServiceException
   */
  private void checkGroup(final String pOldName, final Group group, final String projectId) throws GroupServiceException
  {
    // validate the bean
    final ValidatorResponse response = validationService.validate(Group.class, group);
    if (!response.isValid())
    {
      LOG.error(ExceptionCode.ERR_VALIDATION_BEAN.toString() + " : " + response.getMessage());
      throw new GroupServiceException(ExceptionCode.ERR_VALIDATION_BEAN, response.getMessage());
    }

    // check that the login is unique for private group of the project or public groups
    if ((!pOldName.equals(group.getName())) && (groupDAO.existGroup(projectId, group.getName())))
    {
      throw new GroupServiceException(ExceptionCode.ERR_CREATE_GROUP_LOGIN_ALREADY_EXISTS,
                                      String.format("projectId=%s, name=%s", projectId, group.getName()));
    }

    // only forge project can create public groups
    if ((!forgeConfigurationService.getForgeProjectId().equals(projectId)) && (group.isVisible()))
    {
      throw new GroupServiceException(ExceptionCode.ERR_PUBLIC_GROUP_NOT_ALLOWED_FOR_USER_PROJECT, String
                                                                                                       .format("A public group creation or modification is not allowed for others projects than forge project [projectId=%s, group_name=%s]",
                                                                                                               projectId,
                                                                                                               group
                                                                                                                   .getName()));
    }

  }

  /**
   * Use by container to inject {@link GroupDAO} implementation
   * 
   * @param pGroupDAO
   *          the groupDAO to set
   */
  public void setGroupDAO(final GroupDAO pGroupDAO)
  {
    groupDAO = pGroupDAO;
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
   * Use by container to inject {@link ValidationService} implementation
   * 
   * @param pValidationService
   *          the validationService to set
   */
  public void setValidationService(final ValidationService pValidationService)
  {
    validationService = pValidationService;
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
   * Use by container to inject {@link MembershipHandler} implementation
   * 
   * @param pMembershipHandler
   *          the membershipHandler to set
   */
  public void setMembershipHandler(final MembershipHandler pMembershipHandler)
  {
    membershipHandler = pMembershipHandler;
  }

  /**
   * Use by container to inject {@link ForgeConfigurationService} implementation
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * @param pluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pluginsManager)
  {
    this.pluginsManager = pluginsManager;
  }

  /**
   * @param messageDelegate
   *          the messageDelegate to set
   */
  public void setMessageDelegate(final MessageDelegate messageDelegate)
  {
    this.messageDelegate = messageDelegate;
  }

}
