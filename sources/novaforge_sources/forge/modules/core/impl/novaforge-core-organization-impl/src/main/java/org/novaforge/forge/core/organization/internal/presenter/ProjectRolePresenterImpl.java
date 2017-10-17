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

import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.internal.model.AuthorizationResourceImpl;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.services.MembershipService;
import org.novaforge.forge.core.organization.services.ProjectRoleService;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ProjectRolePresenter}
 * 
 * @author Guillaume Lamirand
 * @see ProjectRolePresenter
 */
public class ProjectRolePresenterImpl implements ProjectRolePresenter
{
  /**
   * Reference to {@link ProjectRoleService} service injected by the container
   */
  private ProjectRoleService projectRoleService;
  /**
   * Reference to {@link MembershipService} service injected by the container
   */
  private MembershipService  membershipService;
  /**
   * Reference to {@link SecurityDelegate} service injected by the container
   */
  private SecurityDelegate   securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectRole newRole()
  {
    return projectRoleService.newRole();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createRole(final Role pRole, final String pProjectId) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class,
        PermissionAction.CREATE));
    projectRoleService.createRole(pRole, pProjectId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createSystemRole(final Role pRole, final String pProjectId) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class,
        PermissionAction.CREATE));
    projectRoleService.createSystemRole(pRole, pProjectId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteRole(final String pRoleName, final String pProjectId) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class,
        PermissionAction.DELETE));
    projectRoleService.deleteRole(pRoleName, pProjectId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectRole> getAllRoles(final String pProjectId) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class,
        PermissionAction.READ));
    return projectRoleService.getAllRoles(pProjectId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectRole getRole(final String pRoleName, final String pProjectId) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class,
        PermissionAction.READ));
    return projectRoleService.getRole(pRoleName, pProjectId);
  }

  @Override
  public void updateRole(final String pOldName, final Role pRole, final String pProjectId)
      throws ProjectServiceException
  {
    final String currentUser = securityDelegate.getCurrentUser();
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class, PermissionAction.UPDATE));
    projectRoleService.updateRole(pOldName, pRole, pProjectId, currentUser);
    cleanAuthorization(pProjectId, pRole.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPermissionToRole(final String pProjectId, final String pRoleName, final Class<?> pResource,
                                  final PermissionAction... pActions) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class, PermissionAction.UPDATE));
    projectRoleService.addPermissionToRole(pProjectId, pRoleName, pResource, pActions);
    cleanAuthorization(pProjectId, pRoleName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectRole changeOrder(final String pProjectId, final String pRoleName, final boolean pIncrease)
      throws ProjectServiceException
  {
    final String currentUser = securityDelegate.getCurrentUser();
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class,
        PermissionAction.UPDATE));
    return projectRoleService.changeOrder(pProjectId, pRoleName, pIncrease, currentUser);
  }

  private void cleanAuthorization(final String pProjectId, final String pRoleName)
      throws ProjectServiceException
  {
    final List<Actor> actors = membershipService.getAllActorsForRole(pProjectId, pRoleName);
    final List<User> users = new ArrayList<User>();
    for (final Actor actor : actors)
    {
      if (actor instanceof User)
      {
        users.add((User) actor);
      }
      else if (actor instanceof Group)
      {
        users.addAll(((Group) actor).getUsers());
      }
    }
    securityDelegate.clearPermissionCached(users);
  }

  /**
   * Use by container to inject {@link MembershipService} implementation
   * 
   * @param pMembershipService
   *          the membershipService to set
   */
  public void setMembershipService(final MembershipService pMembershipService)
  {
    membershipService = pMembershipService;
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
}
