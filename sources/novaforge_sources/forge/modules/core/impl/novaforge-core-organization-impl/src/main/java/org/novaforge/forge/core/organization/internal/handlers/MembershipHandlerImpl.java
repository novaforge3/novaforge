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

import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.delegates.MailDelegate;
import org.novaforge.forge.core.organization.delegates.MessageDelegate;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.MailDelegateException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.handlers.ApplicationHandler;
import org.novaforge.forge.core.organization.handlers.MembershipHandler;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.comparator.MembershipComparator;
import org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author sbenoist
 */
public class MembershipHandlerImpl implements MembershipHandler
{
  private MembershipDAO      membershipDAO;

  private ProjectDAO         projectDAO;

  private MessageDelegate    messageDelegate;

  private ApplicationHandler applicationHandler;

  private MailDelegate       mailDelegate;

  /**
   * Use by container to inject {@link MembershipDAO}
   *
   * @param pMembershipDAO
   *          the membershipDAO to set
   */
  public void setMembershipDAO(final MembershipDAO pMembershipDAO)
  {
    membershipDAO = pMembershipDAO;
  }

  /**
   * Use by container to inject {@link ProjectDAO}
   *
   * @param pProjectDAO
   *          the projectDAO to set
   */
  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.organization.handlers.MembershipHandler#getAllEffectiveUserMembershipsForProject
   *      (java.lang.String)
   */
  @Override
  public List<Membership> getAllEffectiveUserMembershipsForProject(final String pProjectId)
      throws ProjectServiceException
  {
    // FIXME This method is not optimized. We should find another way to do the job
    final List<Membership> memberships = membershipDAO.findByProject(pProjectId);
    final List<Membership> userMemberships = new ArrayList<Membership>();

    for (final Membership membership : memberships)
    {
      // add memberships of project's users
      if (membership.getActor() instanceof User)
      {
        userMemberships.add(membership);
      }
      // add memberships of project's groups' users
      else if (membership.getActor() instanceof Group)
      {
        final List<User> users = ((Group) membership.getActor()).getUsers();
        Membership tmpMembership;
        // create a new membership foreach user in the group
        for (final User user : users)
        {
          tmpMembership = membershipDAO.newMembership(user, membership.getProject(), membership.getRole());
          tmpMembership.setActor(user);
          tmpMembership.setProject(membership.getProject());
          tmpMembership.setRole(membership.getRole());
          userMemberships.add(tmpMembership);
        }
      }
    }

    return userMemberships;
  }

  /**
   * Use by container to inject {@link MessageDelegate}
   *
   * @param pMessageDelegate
   *          the messageDelegate to set
   */
  public void setMessageDelegate(final MessageDelegate pMessageDelegate)
  {
    messageDelegate = pMessageDelegate;
  }

  /**
   * Use by container to inject {@link ApplicationHandler}
   *
   * @param pApplicationHandler
   *          the applicationHandler to set
   */
  public void setApplicationHandler(final ApplicationHandler pApplicationHandler)
  {
    applicationHandler = pApplicationHandler;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.organization.handlers.MembershipHandler#
   *      getAllEffectiveUserMembershipsForUserAndProject(java.lang.String, java.lang.String)
   */
  @Override
  public List<Membership> getAllEffectiveUserMembershipsForUserAndProject(final String pProjectId,
      final String pLogin) throws ProjectServiceException
  {
    // FIXME This method is not optimized. We should find another way to do the job
    final List<Membership> userMemberships = new ArrayList<Membership>();

    final List<Membership> projectUsermemberships = getAllEffectiveUserMembershipsForProject(pProjectId);
    for (final Membership membership : projectUsermemberships)
    {
      if (membership.getActor() instanceof User)
      {
        final User user = (User) membership.getActor();
        if (user.getLogin().equals(pLogin))
        {
          userMemberships.add(membership);
        }
      }
    }

    return userMemberships;
  }

  /**
   * Use by container to inject {@link MailDelegate}
   *
   * @param pMailDelegate
   *     the mailDelegate to set
   */
  public void setMailDelegate(final MailDelegate pMailDelegate)
  {
    mailDelegate = pMailDelegate;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.organization.handlers.MembershipHandler#getAllToolUserMemberships(java.lang
   *      .String, java.util.Map)
   */
  @Override
  public List<Membership> getAllToolUserMemberships(final String pProjectId,
      final Map<String, String> pToolRoleMapping) throws ProjectServiceException
  {
    final List<Membership> allUserMemberships = getAllEffectiveUserMembershipsForProject(pProjectId);
    return getAllToolUserMemberships(allUserMemberships, pToolRoleMapping);
  }



  /**
   * This method return the membership used for tool roles mapping for a users (in accordance with roles
   * mapping map). It can return null if no user role is mapped.
   * 
   * @param pProjectId
   * @param pLogin
   * @param pToolRoleMapping
   * @return Membership
   * @throws ProjectServiceException
   * @throws UserServiceException
   */
  private Membership getToolUserMembership(final String pProjectId, final String pLogin,
      final Map<String, String> pToolRoleMapping) throws ProjectServiceException
  {
    final List<Membership> userMemberships = getAllEffectiveUserMembershipsForUserAndProject(pProjectId,
        pLogin);
    return getToolUserMembership(userMemberships, pToolRoleMapping);
  }



  private Membership getToolUserMembership(final List<Membership> pUserMemberships,
      final Map<String, String> pToolRoleMapping)
  {
    Membership toolUserMembership = null;

    final List<Membership> potentialToolUserMemberships = new ArrayList<Membership>();

    String roleName = null;
    for (final Membership membership : pUserMemberships)
    {
      roleName = membership.getRole().getName();
      if (pToolRoleMapping != null)
      {
        if (pToolRoleMapping.containsKey(roleName))
        {
          potentialToolUserMemberships.add(membership);
        }
      }
    }

    // return the greater membership (ie the last one in the sorted collection with the MembershipComparator
    if (potentialToolUserMemberships.size() > 0)
    {
      Collections.sort(potentialToolUserMemberships, new MembershipComparator());
      toolUserMembership = potentialToolUserMemberships.get(potentialToolUserMemberships.size() - 1);
    }

    return toolUserMembership;
  }

  private List<Membership> getAllToolUserMemberships(final List<Membership> pUserMemberships,
      final Map<String, String> pToolRoleMapping)
  {
    final List<Membership> toolUserMemberships = new ArrayList<Membership>();

    // Use a map to get all the memberships relative to a user
    final Map<User, List<Membership>> map = new HashMap<User, List<Membership>>();

    User user = null;
    List<Membership> list = null;
    for (final Membership membership : pUserMemberships)
    {
      user = (User) membership.getActor();
      if (!map.containsKey(user))
      {
        list = new ArrayList<Membership>();
        list.add(membership);
      }
      else
      {
        list = map.get(user);
        list.add(membership);
      }

      map.put(user, list);
    }

    // for each user in the map, find the tool user membership
    for (final Map.Entry<User, List<Membership>> entry : map.entrySet())
    {
      final Membership toolUserMembership = getToolUserMembership(entry.getValue(), pToolRoleMapping);

      // take care that the method can returns null if no user role is mapped with the tool...
      if (toolUserMembership != null)
      {
        toolUserMemberships.add(toolUserMembership);
      }
    }

    return toolUserMemberships;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.organization.handlers.MembershipHandler#sendUserMembershipsPropagation(java
   *      .util.Map, java.util.List, java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public void sendUserMembershipsPropagation(final Map<String, Membership> pInitialToolsMemberships,
      final List<ProjectApplication> pUserApplications, final String pProjectId, final String pLogin,
      final String pUsername) throws ProjectServiceException
  {
    for (final ProjectApplication userApplication : pUserApplications)
    {
      if (ApplicationStatus.ACTIVE.equals(userApplication.getStatus()))
      {
        // get the application uri
        final String uri = userApplication.getUri();
        // get the old membership for this application
        Membership oldToolMembership = null;
        if (pInitialToolsMemberships != null)
        {
          oldToolMembership = pInitialToolsMemberships.get(uri);
        }
        // define the new membership used for application
        Map<String, String> rolesMapping = null;
        try
        {
          rolesMapping = applicationHandler.getRoleMapping(uri);
        }
        catch (final ApplicationServiceException e)
        {
          throw new ProjectServiceException(String.format("Unable to retrieve role mapping for [app_uri=%s]",
              uri), e);
        }
        final Membership newToolMembership = getToolUserMembership(pProjectId, pLogin, rolesMapping);
        // send the propagation message in function of old and new
        // memberships
        final List<Membership> memberships = new ArrayList<Membership>();
        if ((oldToolMembership != null) && (newToolMembership != null) && (!oldToolMembership
                                                                                .equals(newToolMembership)))
        {
          memberships.add(newToolMembership);
          sendMembershipMessage(userApplication.getPluginUUID().toString(), userApplication
              .getPluginInstanceUUID().toString(), pProjectId, memberships, pUsername,
              PluginQueueAction.UPDATE.getLabel());
        }
        else if ((oldToolMembership != null) && (newToolMembership == null))
        {
          memberships.add(oldToolMembership);
          sendMembershipMessage(userApplication.getPluginUUID().toString(), userApplication
              .getPluginInstanceUUID().toString(), pProjectId, memberships, pUsername,
              PluginQueueAction.DELETE.getLabel());
        }
        else if ((oldToolMembership == null) && (newToolMembership != null))
        {
          memberships.add(newToolMembership);
          sendMembershipMessage(userApplication.getPluginUUID().toString(), userApplication
              .getPluginInstanceUUID().toString(), pProjectId, memberships, pUsername,
              PluginQueueAction.CREATE.getLabel());
        }
      }
    }
  }

  private void sendMembershipMessage(final String pPluginUUID, final String pInstanceUUID,
      final String pProjectId, final List<Membership> pMemberships, final String pUsername,
      final String pAction) throws ProjectServiceException
  {
    try
    {
      messageDelegate.sendMembershipMessage(pPluginUUID, pInstanceUUID, pProjectId,
          (Serializable) pMemberships, pUsername, pAction);
    }
    catch (final MessageServiceException e)
    {
      throw new ProjectServiceException(
          String.format(
              "Unable to send propagation message with [plugin_uuid=%s, instance_uuid=%s, project_id=%s, action=%s]",
              pPluginUUID, pInstanceUUID, pProjectId, pAction), e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.organization.handlers.MembershipHandler#getToolsMemberships(java.lang.String,
   *      java.util.List, java.lang.String)
   */
  @Override
  public Map<String, Membership> getToolsMemberships(final String pProjectId,
      final List<ProjectApplication> pUserApplications, final String pLogin) throws ProjectServiceException,
      ApplicationServiceException
  {
    final Map<String, Membership> toolsMemberships = new HashMap<String, Membership>();
    for (final ProjectApplication userApplication : pUserApplications)
    {
      // define the membership used for each application
      if (ApplicationStatus.ACTIVE.equals(userApplication.getStatus()))
      {
        final Map<String, String> rolesMapping = applicationHandler.getRoleMapping(userApplication.getUri());
        final Membership toolUserMembership = getToolUserMembership(pProjectId, pLogin, rolesMapping);
        toolsMemberships.put(userApplication.getUri(), toolUserMembership);
      }

    }
    return toolsMemberships;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.organization.handlers.MembershipHandler#notifyUserForMembershipsChange(java
   *      .lang.String, org.novaforge.forge.core.organization.model.User, boolean,
   *      org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum)
   */
  @Override
  public void notifyUserForMembershipsChange(final String pProjectId, final User pUser,
      final boolean pSendMail, final MailDelegateEnum pMailDelegateEnum) throws ProjectServiceException
  {
    if (pSendMail)
    {
      try
      {
        final Set<Role> roles = new HashSet<Role>();
        final List<Membership> memberships = getAllEffectiveUserMembershipsForUserAndProject(pProjectId,
            pUser.getLogin());
        Project project = null;
        for (final Membership membership : memberships)
        {
          roles.add(membership.getRole());
          if (project == null)
          {
            project = membership.getProject();
          }
        }
        if (project == null)
        {
          project = projectDAO.findByProjectId(pProjectId);
        }
        // send the mail
        final Locale locale = pUser.getLanguage().getLocale();
        mailDelegate.sendProjectMembershipUpdate(project, pUser, roles, pMailDelegateEnum, locale);
      }
      catch (final MailDelegateException e)
      {
        throw new ProjectServiceException(String.format("Unable to send email to user with [user=%s]",
            pUser.getLogin()), e);
      }
      catch (final Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.organization.handlers.MembershipHandler#notifyGroupForMembershipsChange(java
   *      .lang.String, java.util.List, boolean,
   *      org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum)
   */
  @Override
  public void notifyGroupForMembershipsChange(final String pProjectId, final List<User> pUsers,
      final boolean pSendMail, final MailDelegateEnum pMailDelegateEnum) throws ProjectServiceException
  {
    for (final User user : pUsers)
    {
      notifyUserForMembershipsChange(pProjectId, user, pSendMail, pMailDelegateEnum);
    }

  }

}
