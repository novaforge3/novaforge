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
package org.novaforge.forge.core.organization.internal.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.core.organization.delegates.MembershipDelegate;
import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.internal.model.AuthorizationResourceImpl;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.services.MembershipService;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This is an implementation of {@link MembershipPresenter}
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see MembershipPresenter
 */
public class MembershipPresenterImpl implements MembershipPresenter
{

  private static final Log LOG = LogFactory.getLog(MembershipPresenterImpl.class);
  /**
   * Reference to {@link MembershipService} service injected by the container
   */
  private MembershipService  membershipService;
  /**
   * Reference to {@link MembershipDelegate} service injected by the container
   */
  private MembershipDelegate membershipDelegate;
  /**
   * Reference to {@link SecurityDelegate} service injected by the container
   */
  private SecurityDelegate   securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.ADD_USER_MEMBERSHIP)
  public void addUserMembership(@HistorizableParam(label = "projectId") final String pProjectId,
      @HistorizableParam(label = "uuid") final UUID pUserUUID,
      @HistorizableParam(label = "roles") final Set<String> pRolesName,
      @HistorizableParam(label = "default") final String pDefaultRoleName, final boolean pSendMail)
      throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("input : projectId =" + pProjectId);
      LOG.debug("input : user_uuid =" + pUserUUID);
      LOG.debug("input : roles =" + pRolesName.toString());
      LOG.debug("input : sendMail =" + pSendMail);
    }
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Membership.class,
        PermissionAction.CREATE));

    final String username = securityDelegate.getCurrentUser();
    final String loginCreated = membershipService.addUserMembership(pProjectId, pUserUUID, pRolesName,
        pDefaultRoleName, username, pSendMail);
    securityDelegate.clearPermissionCached(loginCreated);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.ADD_GROUP_MEMBERSHIP)
  public void addGroupMembership(@HistorizableParam(label = "projectId") final String pProjectId,
                                 @HistorizableParam(label = "uuid") final UUID pGroupUUID,
                                 @HistorizableParam(label = "roles") final Set<String> pRolesName,
                                 @HistorizableParam(label = "default") final String pDefaultRoleName)
      throws ProjectServiceException
  {

    if (LOG.isDebugEnabled())
    {
      LOG.debug("input : projectId =" + pProjectId);
      LOG.debug("input : group_uuid =" + pGroupUUID);
      LOG.debug("input : roles =" + pRolesName.toString());
    }
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Membership.class,
                                                                             PermissionAction.CREATE));
    final String username = securityDelegate.getCurrentUser();
    final Group group = membershipService.addGroupMembership(pProjectId, pGroupUUID, pRolesName, pDefaultRoleName,
                                                             username);

    securityDelegate.clearPermissionCached(group.getUsers());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipInfo> getAllUserMemberships(final String pProjectId, final boolean pWithSystem)
      throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Membership.class,
        PermissionAction.READ));
    return membershipService.getAllUserMemberships(pProjectId, pWithSystem);
  }

  /**
   * {@inheritDoc}
   *
   * @throws ProjectServiceException
   */
  @Override
  public List<MembershipInfo> getAllEffectiveMembershipsForUserAndProject(final String pProjectId, final UUID pUserUUID)
      throws ProjectServiceException
  {
    return membershipService.getAllEffectiveMembershipsForUserAndProject(pProjectId, pUserUUID);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipInfo> getAllGroupMemberships(final String pProjectId) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Membership.class, PermissionAction.READ));
    return membershipService.getAllGroupMemberships(pProjectId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.UPDATE_USER_MEMBERSHIP)
  public void updateUserMembership(@HistorizableParam(label = "projectId") final String pProjectId,
                                   @HistorizableParam(label = "uuid") final UUID pUserUUId,
                                   @HistorizableParam(label = "roles") final Set<String> pNewRolesName,
                                   @HistorizableParam(
                                                         label = "default") final String pDefaultRoleName,
                                   final boolean pSendMail)
      throws ProjectServiceException
  {

    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Membership.class, PermissionAction.READ,
                                                                             PermissionAction.UPDATE));
    final String username = securityDelegate.getCurrentUser();
    final String userLoginUpdated = membershipService.updateUserMembership(pProjectId, pUserUUId, pNewRolesName,
                                                                           pDefaultRoleName, pSendMail, username);
    securityDelegate.clearPermissionCached(userLoginUpdated);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.UPDATE_GROUP_MEMBERSHIP)
  public void updateGroupMembership(@HistorizableParam(label = "projectId") final String pProjectId,
                                    @HistorizableParam(label = "uuid") final UUID pGroupUUID,
                                    @HistorizableParam(label = "roles") final Set<String> pNewRolesName,
                                    @HistorizableParam(
                                                          label = "default") final String pDefaultRoleName,
                                    final boolean pSendMail) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Membership.class, PermissionAction.READ,
                                                                             PermissionAction.UPDATE));
    final String username = securityDelegate.getCurrentUser();
    final Group group = membershipService.updateGroupMembership(pProjectId, pGroupUUID, pNewRolesName, pDefaultRoleName,
                                                                username, pSendMail);

    securityDelegate.clearPermissionCached(group.getUsers());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.REMOVE_USER_MEMBERSHIP)
  public void removeUserMembership(@HistorizableParam(label = "projectId") final String pProjectId,
                                   @HistorizableParam(label = "login") final UUID pUserUUID, final boolean pSendMail)
      throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Membership.class, PermissionAction.READ,
                                                                             PermissionAction.DELETE));
    final String username = securityDelegate.getCurrentUser();
    final String loginRemoved = membershipService.removeUserMembership(pProjectId, pUserUUID, pSendMail, username);
    securityDelegate.clearPermissionCached(loginRemoved);

  }

  /**
   * {@inheritDoc}
   *
   * @throws MessageServiceException
   */
  @Override
  @Historization(type = EventType.REMOVE_GROUP_MEMBERSHIP)
  public void removeGroupMembership(@HistorizableParam(label = "projectId") final String pProjectId,
      @HistorizableParam(label = "uuid") final UUID pGroupUUID, final boolean pSendMail)
      throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Membership.class,
        PermissionAction.READ, PermissionAction.DELETE));
    final String username = securityDelegate.getCurrentUser();
    final Group group = membershipService.removeGroupMembership(pProjectId, pGroupUUID, pSendMail, username);

    securityDelegate.clearPermissionCached(group.getUsers());
  }

  /**
   * {@inheritDoc}
   *
   * @throws ProjectServiceException
   */
  @Override
  @Historization(type = EventType.GET_ALL_MEMBERSHIPS)
  public List<Membership> getAllMemberships(@HistorizableParam(label = "projectId") final String pProjectId)
      throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Membership.class, PermissionAction.READ));
    return membershipService.getAllMemberships(pProjectId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSuperAdmin(final User pUser)
  {
    return membershipDelegate.isSuperAdmin(pUser.getLogin());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCurrentSuperAdmin()
  {
    final String username = securityDelegate.getCurrentUser();
    return membershipDelegate.isSuperAdmin(username);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<User> getAllSuperAdmin() throws ProjectServiceException
  {
    return membershipDelegate.getAllSuperAdmin();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Membership> getAllEffectiveUserMembershipsForProject(final String pProjectId)
      throws ProjectServiceException
  {
    return membershipService.getAllEffectiveUserMembershipsForProject(pProjectId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Membership> getAllEffectiveUserMembershipsForUserAndProject(final String pProjectId,
      final String pLogin) throws ProjectServiceException
  {
    return membershipService.getAllEffectiveUserMembershipsForUserAndProject(pProjectId, pLogin);

  }

  /**
   * {@inheritDoc}
   *
   * @throws ProjectServiceException
   */
  @Override
  public Map<String, List<ProjectRole>> getAllEffectiveRolesForUser(final String pLogin) throws ProjectServiceException
  {
    return membershipService.getAllEffectiveRolesForUser(pLogin);
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
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Membership.class, PermissionAction.READ));
    return membershipService.getAllActorsForRole(pProjectId, pRoleName);
  }

}
