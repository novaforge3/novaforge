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
import org.novaforge.forge.core.organization.dao.GroupDAO;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.dao.RoleDAO;
import org.novaforge.forge.core.organization.dao.UserDAO;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.handlers.MembershipHandler;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum;
import org.novaforge.forge.core.organization.model.enumerations.MembershipRequestStatus;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.MembershipService;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of {@link MembershipService}
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see MembershipService
 */
public class MembershipServiceImpl implements MembershipService
{
  private static final Log LOGGER = LogFactory.getLog(MembershipServiceImpl.class);
  /**
   * Reference to {@link UserDAO} service injected by the container
   */
  private UserDAO            userDAO;
  /**
   * Reference to {@link ProjectDAO} service injected by the container
   */
  private ProjectDAO         projectDAO;
  /**
   * Reference to {@link MembershipDAO} service injected by the container
   */
  private MembershipDAO      membershipDAO;
  /**
   * Reference to {@link RoleDAO} service injected by the container
   */
  private RoleDAO            roleDAO;
  /**
   * Reference to {@link GroupDAO} service injected by the container
   */
  private GroupDAO           groupDAO;
  /**
   * Reference to {@link ApplicationService} service injected by the container
   */
  private ApplicationService applicationService;
  /**
   * Reference to {@link MembershipHandler} service injected by the container
   */
  private MembershipHandler  membershipHandler;

  /**
   * {@inheritDoc}
   */
  @Override
  public String addUserMembership(final String pProjectId, final UUID pUserUUID,
      final Set<String> pRolesName, final String pDefaultRoleName, final String pUsername,
      final boolean pSendMail) throws ProjectServiceException
  {

    try
    {
      if (!userDAO.existUserWith(pUserUUID))
      {
        LOGGER.error(ExceptionCode.ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST.toString());
        throw new ProjectServiceException(ExceptionCode.ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST, String.format(
            "projectId=%s, uuid=%s", pProjectId, pUserUUID));
      }

      // get the user
      final User user = userDAO.findByUUID(pUserUUID);
      final String userLogin = user.getLogin();

      // get all the applications concerned by the propagation
      final List<ProjectApplication> activeApplication = applicationService
          .getAvailableApplications(pProjectId);
      // get all the tools initials memberships to prepare propagation
      final Map<String, Membership> initialToolsMemberships = membershipHandler.getToolsMemberships(
          pProjectId, activeApplication, userLogin);

      // add the memberships
      final Project project = projectDAO.findByProjectId(pProjectId);
      addMemberships(project, user, pRolesName, pDefaultRoleName);

      // to update last modified date of the project
      updateLastModifiedDate(project);

      // propagate the membership
      membershipHandler.sendUserMembershipsPropagation(initialToolsMemberships, activeApplication,
          pProjectId, userLogin, pUsername);

      // send an email
      membershipHandler.notifyUserForMembershipsChange(pProjectId, user, pSendMail,
          MailDelegateEnum.ADD_MEMBERSHIP);

      // Update membership request for the user
      try
      {
        final MembershipRequest request = membershipDAO.findRequestByProjectAndUser(pProjectId, userLogin);
        request.setStatus(MembershipRequestStatus.VALIDATED);
        membershipDAO.update(request);
      }
      catch (final NoResultException e)
      {
        // Nothing particular to do in this case
      }

      return userLogin;
    }
    catch (final NoResultException e)
    {
      throw new ProjectServiceException(String.format("Unable to obtain user information with [uuid=%s]",
          pUserUUID), e);
    }
    catch (final ApplicationServiceException e)
    {
      throw new ProjectServiceException(String.format(
          "Unable to obtain project applications with [projectId=%s]", pProjectId), e);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  private void addMemberships(final Project pProject, final Actor pActor, final Set<String> pRolesName,
      final String pDefaultRoleName)
  {
    for (final String roleName : pRolesName)
    {
      boolean defaultRole = false;
      if ((pDefaultRoleName != null) && (roleName.equals(pDefaultRoleName)))
      {
        defaultRole = true;
      }
      addMembership(pProject, pActor, roleName, defaultRole);
    }
  }

  private void updateLastModifiedDate(final Project pProject) throws ProjectServiceException
  {
    projectDAO.update(pProject);
  }

  private void addMembership(final Project pProject, final Actor pActor, final String pRoleName,
      final boolean pDefaultRoleName)
  {
    final ProjectRole role = (ProjectRole) roleDAO.findByNameAndElement(pProject.getProjectId(), pRoleName);
    final Membership membership = membershipDAO.newMembership(pActor, pProject, role);
    membership.setPriority(pDefaultRoleName);
    membershipDAO.persist(membership);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Group addGroupMembership(final String pProjectId, final UUID pGroupUUID,
      final Set<String> pRolesName, final String pDefaultRoleName, final String pUsername)
      throws ProjectServiceException
  {
    try
    {
      final Group group = getGroup(pGroupUUID);

      // get all the applications concerned by the propagation
      final List<ProjectApplication> activeApplication = applicationService
          .getAvailableApplications(pProjectId);

      // get all the tools initials memberships to prepare propagation for all users of the group
      final Map<String, Map<String, Membership>> initialToolsMemberships = new HashMap<String, Map<String, Membership>>();
      for (final User user : group.getUsers())
      {
        final String login = user.getLogin();
        initialToolsMemberships.put(login,
            membershipHandler.getToolsMemberships(pProjectId, activeApplication, login));
      }

      // add the memberships
      final Project project = projectDAO.findByProjectId(pProjectId);
      addMemberships(project, group, pRolesName, pDefaultRoleName);

      // to update last modified date of the project
      updateLastModifiedDate(project);

      // propagate the membership
      for (final Map.Entry<String, Map<String, Membership>> entry : initialToolsMemberships.entrySet())
      {
        membershipHandler.sendUserMembershipsPropagation(entry.getValue(), activeApplication, pProjectId,
            entry.getKey(), pUsername);
      }

      // notification
      membershipHandler.notifyGroupForMembershipsChange(pProjectId, group.getUsers(), true,
          MailDelegateEnum.ADD_MEMBERSHIP);

      return group;
    }
    catch (final NoResultException e)
    {
      throw new ProjectServiceException(String.format("Unable to obtain group information with [uuid=%s]",
          pGroupUUID), e);
    }
    catch (final ApplicationServiceException e)
    {
      throw new ProjectServiceException(String.format(
          "Unable to obtain project applications with [projectId=%s]", pProjectId), e);
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipInfo> getAllUserMemberships(final String pProjectId, final boolean pWithSystem)
      throws ProjectServiceException
  {
    try
    {
      final List<MembershipInfo> returnMemberships = new ArrayList<MembershipInfo>();
      final List<MembershipInfo> findUsersMembershipsByProject = membershipDAO
          .findUsersInfoByProject(pProjectId);
      for (final MembershipInfo membershipInfo : findUsersMembershipsByProject)
      {
        // Should be always right in this context
        if (membershipInfo.getActor() instanceof User)
        {
          final User user = (User) membershipInfo.getActor();
          if ((!RealmType.SYSTEM.equals(user.getRealmType()))
              || (RealmType.SYSTEM.equals(user.getRealmType()) == pWithSystem))
          {
            returnMemberships.add(membershipInfo);
          }

        }
      }
      return returnMemberships;
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipInfo> getAllGroupMemberships(final String pProjectId) throws ProjectServiceException
  {
    try
    {
      return membershipDAO.findGroupsInfoByProject(pProjectId);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String updateUserMembership(final String pProjectId, final UUID pUserUUID,
      final Set<String> pNewRolesName, final String pDefaultRoleName, final boolean pSendMail,
      final String pUsername) throws ProjectServiceException
  {
    try
    {
      if (!userDAO.existUserWith(pUserUUID))
      {
        LOGGER.error(ExceptionCode.ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST.toString());
        throw new ProjectServiceException(ExceptionCode.ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST, String.format(
            "projectId=%s, uuid=%s", pProjectId, pUserUUID));
      }
      // get the user
      final User user = userDAO.findByUUID(pUserUUID);
      final String userLogin = user.getLogin();

      // get all the applications concerned by the propagation
      final List<ProjectApplication> activeApplication = applicationService
          .getAvailableApplications(pProjectId);

      // get all the tools initials memberships to prepare propagation
      final Map<String, Membership> initialToolsMemberships = membershipHandler.getToolsMemberships(
          pProjectId, activeApplication, userLogin);

      // update the memberships
      final Project project = projectDAO.findByProjectId(pProjectId);
      updateMemberships(project, user, pNewRolesName, pDefaultRoleName);

      // to update last modified date of the project
      updateLastModifiedDate(project);

      // propagate memberships
      membershipHandler.sendUserMembershipsPropagation(initialToolsMemberships, activeApplication,
          pProjectId, userLogin, pUsername);

      // send an email
      membershipHandler.notifyUserForMembershipsChange(pProjectId, user, pSendMail,
          MailDelegateEnum.UPDATE_MEMBERSHIP);
      return userLogin;
    }
    catch (final NoResultException e)
    {
      throw new ProjectServiceException(String.format("Unable to obtain user information with [uuid=%s]",
          pUserUUID), e);
    }
    catch (final ApplicationServiceException e)
    {
      throw new ProjectServiceException(String.format(
          "Unable to obtain project applications with [projectId=%s]", pProjectId), e);
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  private void updateMemberships(final Project pProject, final Actor pActor, final Set<String> pRolesNames,
      final String pDefaultRoleName)
  {
    // delete all existing memberships regarding the actor and project
    deleteMemberships(pProject, pActor);

    // Add all new role as membership
    addMemberships(pProject, pActor, pRolesNames, pDefaultRoleName);
  }

  private void deleteMemberships(final Project pProject, final Actor pActor)
  {
    // delete all existing memberships regarding the actor and project
    final List<Membership> memberships = membershipDAO.findByProjectAndActor(pProject.getProjectId(),
        pActor.getUuid());
    for (final Membership membership : memberships)
    {
      membershipDAO.delete(membership);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Group updateGroupMembership(final String pProjectId, final UUID pGroupUUID,
      final Set<String> pNewRolesName, final String pDefaultRoleName, final String pUsername,
      final boolean pSendMail) throws ProjectServiceException
  {
    try
    {
      final Group group = getGroup(pGroupUUID);

      // get all the applications concerned by the propagation
      final List<ProjectApplication> activeApplication = applicationService
          .getAvailableApplications(pProjectId);

      // get all the tools initials memberships to prepare propagation for all users of the group
      final Map<String, Map<String, Membership>> initialToolsMemberships = new HashMap<String, Map<String, Membership>>();
      String login = null;
      for (final User user : group.getUsers())
      {
        login = user.getLogin();
        initialToolsMemberships.put(user.getLogin(),
            membershipHandler.getToolsMemberships(pProjectId, activeApplication, login));
      }

      // update the memberships
      final Project project = projectDAO.findByProjectId(pProjectId);
      updateMemberships(project, group, pNewRolesName, pDefaultRoleName);

      // to update last modified date of the project
      updateLastModifiedDate(project);

      // propagate the membership
      for (final Map.Entry<String, Map<String, Membership>> entry : initialToolsMemberships.entrySet())
      {
        membershipHandler.sendUserMembershipsPropagation(entry.getValue(), activeApplication, pProjectId,
            entry.getKey(), pUsername);
      }

      // notification
      membershipHandler.notifyGroupForMembershipsChange(pProjectId, group.getUsers(), true,
          MailDelegateEnum.UPDATE_MEMBERSHIP);

      return group;
    }
    catch (final NoResultException e)
    {
      throw new ProjectServiceException(String.format("Unable to obtain group information with [uuid=%s]",
          pGroupUUID), e);
    }
    catch (final ApplicationServiceException e)
    {
      throw new ProjectServiceException(String.format(
          "Unable to obtain project applications with [projectId=%s]", pProjectId), e);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String removeUserMembership(final String pProjectId, final UUID pUserUUID, final boolean pSendMail,
      final String pUsername) throws ProjectServiceException
  {
    try
    {
      if (!userDAO.existUserWith(pUserUUID))
      {
        LOGGER.error(ExceptionCode.ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST.toString());
        throw new ProjectServiceException(ExceptionCode.ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST, String.format(
            "projectId=%s, uuid=%s", pProjectId, pUserUUID));
      }

      final User user = userDAO.findByUUID(pUserUUID);
      final String userLogin = user.getLogin();

      // get all the applications concerned by the propagation
      final List<ProjectApplication> userApplications = applicationService
          .getAllProjectApplications(pProjectId);

      // get all the tools initials memberships to prepare propagation
      final Map<String, Membership> initialToolsMemberships = membershipHandler.getToolsMemberships(
          pProjectId, userApplications, userLogin);

      // remove all the user memberships
      final Project project = projectDAO.findByProjectId(pProjectId);
      deleteMemberships(project, user);

      // to update last modified date of the project
      updateLastModifiedDate(project);

      // propagate memberships
      membershipHandler.sendUserMembershipsPropagation(initialToolsMemberships, userApplications, pProjectId,
          userLogin, pUsername);

      // send an email
      membershipHandler.notifyUserForMembershipsChange(pProjectId, user, pSendMail,
          MailDelegateEnum.REMOVE_MEMBERSHIP);

      return userLogin;
    }
    catch (final NoResultException e)
    {
      throw new ProjectServiceException(String.format("Unable to obtain user information with [uuid=%s]",
          pUserUUID), e);
    }
    catch (final ApplicationServiceException e)
    {
      throw new ProjectServiceException(String.format(
          "Unable to obtain project applications with [projectId=%s]", pProjectId), e);
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Group removeGroupMembership(final String pProjectId, final UUID pGroupUUID, final boolean pSendMail,
      final String pUsername) throws ProjectServiceException
  {
    try
    {
      final Group group = getGroup(pGroupUUID);

      // get all the applications concerned by the propagation
      final List<ProjectApplication> userApplications = applicationService
          .getAllProjectApplications(pProjectId);

      // get all the tools initials memberships to prepare propagation for all users of the group
      final Map<String, Map<String, Membership>> initialToolsMemberships = new HashMap<String, Map<String, Membership>>();
      String login = null;
      for (final User user : group.getUsers())
      {
        login = user.getLogin();
        initialToolsMemberships.put(user.getLogin(),
            membershipHandler.getToolsMemberships(pProjectId, userApplications, login));
      }

      // remove the memberships
      final Project project = projectDAO.findByProjectId(pProjectId);
      deleteMemberships(project, group);

      // to update last modified date of the project
      updateLastModifiedDate(project);

      // propagate the membership
      for (final Map.Entry<String, Map<String, Membership>> entry : initialToolsMemberships.entrySet())
      {
        membershipHandler.sendUserMembershipsPropagation(entry.getValue(), userApplications, pProjectId,
            entry.getKey(), pUsername);
      }

      // send an email
      membershipHandler.notifyGroupForMembershipsChange(pProjectId, group.getUsers(), pSendMail,
          MailDelegateEnum.REMOVE_MEMBERSHIP);

      return group;
    }
    catch (final NoResultException e)
    {
      throw new ProjectServiceException(String.format("Unable to obtain group information with [uuid=%s]",
          pGroupUUID), e);
    }
    catch (final ApplicationServiceException e)
    {
      throw new ProjectServiceException(String.format(
          "Unable to obtain project applications with [projectId=%s]", pProjectId), e);
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ProjectServiceException
   */
  @Override
  public List<Membership> getAllMemberships(final String pProjectId) throws ProjectServiceException
  {
    try
    {
      return membershipDAO.findByProject(pProjectId);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Membership> getAllEffectiveUserMembershipsForProject(final String pProjectId)
      throws ProjectServiceException
  {
    try
    {
      return membershipHandler.getAllEffectiveUserMembershipsForProject(pProjectId);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Membership> getAllEffectiveUserMembershipsForUserAndProject(final String pProjectId,
      final String pLogin) throws ProjectServiceException
  {
    try
    {
      return membershipHandler.getAllEffectiveUserMembershipsForUserAndProject(pProjectId, pLogin);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ProjectServiceException
   */
  @Override
  public List<Actor> getAllActorsForRole(final String pProjectId, final String pRoleName)
      throws ProjectServiceException
  {
    try
    {
      return membershipDAO.findActorsByProjectAndRole(pProjectId, pRoleName);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws ProjectServiceException
   */
  @Override
  public Map<String, List<ProjectRole>> getAllEffectiveRolesForUser(final String pUserLogin)
      throws ProjectServiceException
  {
    try
    {
      final User user = userDAO.findByLogin(pUserLogin);
      final UUID userUUID = user.getUuid();
      final Map<String, List<ProjectRole>> returnRoles = membershipDAO.findRolesByActors(userUUID);
      final List<UUID> groupsUUIDs = groupDAO.findGroupsUUIDForUser(userUUID);
      final Map<String, List<ProjectRole>> groupsRoles = membershipDAO.findRolesByActors(groupsUUIDs
          .toArray(new UUID[groupsUUIDs.size()]));
      final Set<Entry<String, List<ProjectRole>>> entrySet = groupsRoles.entrySet();
      for (final Entry<String, List<ProjectRole>> entry : entrySet)
      {
        if (returnRoles.containsKey(entry.getKey()))
        {
          final List<ProjectRole> existingRoles = returnRoles.get(entry.getKey());
          existingRoles.addAll(entry.getValue());
        }
        else
        {
          returnRoles.put(entry.getKey(), entry.getValue());
        }
      }

      return returnRoles;
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws ProjectServiceException
   */
  @Override
  public List<MembershipInfo> getAllEffectiveMembershipsForUserAndProject(final String pProjectId,
      final UUID pUserUUID) throws ProjectServiceException
  {

    final List<MembershipInfo> memberShipsInfo = new ArrayList<MembershipInfo>();
    try
    {
      memberShipsInfo.addAll(membershipDAO.findInfoByProjectAndActors(pProjectId, pUserUUID));
      final List<UUID> groupsUUIDs = groupDAO.findGroupsUUIDForUser(pUserUUID);
      memberShipsInfo.addAll(membershipDAO.findInfoByProjectAndActors(pProjectId,
          groupsUUIDs.toArray(new UUID[groupsUUIDs.size()])));
      return memberShipsInfo;
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  private Group getGroup(final UUID pUUID) throws ProjectServiceException
  {
    Group group = null;
    if (groupDAO.existGroup(pUUID))
    {
      group = groupDAO.findByUUID(pUUID);
    }
    else
    {
      LOGGER.error(ExceptionCode.ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST.toString());
      throw new ProjectServiceException(ExceptionCode.ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST, String.format(" uuid=%s",
                                                                                                       pUUID));
    }

    return group;
  }

  /**
   * Use by container to inject {@link UserDAO} implementation
   * 
   * @param pUserDAO
   *          the userDAO to set
   */
  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
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

}
