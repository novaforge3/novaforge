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
package org.novaforge.forge.core.organization.internal.delegates;

import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.model.AuthorizationResource;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.core.security.authorization.AuthorizationService;
import org.novaforge.forge.core.security.authorization.Logical;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.core.security.authorization.PermissionHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public class SecurityDelegateImpl implements SecurityDelegate
{

  private AuthentificationService authentificationService;
  private AuthorizationService    authorizationService;
  private PermissionHandler       permissionHandler;

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkResource(final String pProjectId, final AuthorizationResource pAuthorizationResource)
  {
    checkResource(pProjectId, pAuthorizationResource, Logical.AND);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkResource(final String pProjectId, final AuthorizationResource pAuthorizationResource,
      final Logical pLogical)
  {
    final Set<String> pPermissions = permissionHandler.buildProjectPermissions(pProjectId,
        pAuthorizationResource.getResource(), pAuthorizationResource.getActions());
    authorizationService.checkPermissions(pPermissions, pLogical);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkResources(final String pProjectId,
      final List<AuthorizationResource> pAuthorizationResources)
  {
    checkResources(pProjectId, pAuthorizationResources, Logical.AND);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkResources(final String pProjectId,
      final List<AuthorizationResource> pAuthorizationResources, final Logical pLogical)
  {
    final Set<String> permissions = new HashSet<String>();
    for (final AuthorizationResource authorizationResource : pAuthorizationResources)
    {
      final Set<String> pPermissions = permissionHandler.buildProjectPermissions(pProjectId,
          authorizationResource.getResource(), authorizationResource.getActions());
      permissions.addAll(pPermissions);
    }

    authorizationService.checkPermissions(permissions, pLogical);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearPermissionCached(final String... pLogins)
  {
    for (final String login : pLogins)
    {
      authorizationService.clearPermissionCached(login);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearPermissionCached(final List<User> pUsers)
  {
    for (final User user : pUsers)
    {
      authorizationService.clearPermissionCached(user.getLogin());
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCurrentUser()
  {
    return authentificationService.getCurrentUser();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkResource(final AuthorizationResource pAuthorizationResource)
  {
    if (pAuthorizationResource.getResource().isAssignableFrom(Project.class))
    {

      final Set<String> buildProjectPermissions = permissionHandler.buildProjectPermissions(PermissionAction.CREATE);
      authorizationService.checkPermissions(buildProjectPermissions, Logical.AND);
    }
    else
    {
      throw new IllegalArgumentException("The parameter should be assignable from project class");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPermitted(final String pProjectId, final AuthorizationResource pAuthorizationResource)
  {
    final Set<String> pPermissions = permissionHandler.buildProjectPermissions(pProjectId,
        pAuthorizationResource.getResource(), pAuthorizationResource.getActions());
    return authorizationService.isPermitted(pPermissions, Logical.AND);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPermitted(final String pProjectId, final String pApplicationId,
      final PermissionAction... pPermissionAction)
  {
    final Set<String> pPermissions = permissionHandler.buildApplicationPermission(pProjectId, pApplicationId,
        pPermissionAction);
    return authorizationService.isPermitted(pPermissions, Logical.AND);
  }

  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  public void setAuthorizationService(final AuthorizationService pAuthorizationService)
  {
    authorizationService = pAuthorizationService;
  }

  public void setPermissionHandler(final PermissionHandler pPermissionHandler)
  {
    permissionHandler = pPermissionHandler;
  }

}
