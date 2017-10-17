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
package org.novaforge.forge.portal.internal.services.navigation;

import org.novaforge.forge.core.security.authorization.AuthorizationService;
import org.novaforge.forge.core.security.authorization.Logical;
import org.novaforge.forge.portal.models.PortalToken;
import org.novaforge.forge.portal.services.PortalNavigation;
import org.novaforge.forge.portal.services.navigation.NavigationSecurity;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Service implementation of {@link PortalNavigation}
 * 
 * @author Guillaume Lamirand
 */
public class NavigationSecurityImpl implements NavigationSecurity
{
  /**
   * {@link AuthorizationService} injected by container
   */
  private AuthorizationService authorizationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRoles(final String pProjectId, final String pRolesRequested)
  {
    boolean isAuthorized = true;
    if ((pRolesRequested != null) && (!"".equals(pRolesRequested)))
    {
      final Set<String> roles = new HashSet<String>();
      final StringTokenizer token = new StringTokenizer(pRolesRequested, ",");
      while (token.hasMoreTokens())
      {
        roles.add(token.nextToken());
      }
      isAuthorized = authorizationService.hasRoleOnProject(roles, Logical.OR, pProjectId);
    }
    return isAuthorized;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPermissions(final String pProjectId, final String pPermissionsRequested)
  {
    boolean isAuthorized = true;
    if ((pPermissionsRequested != null) && (!"".equals(pPermissionsRequested)))
    {
      final boolean isExplicit = pPermissionsRequested.startsWith("#");
      String permissionClean = pPermissionsRequested;
      if (isExplicit)
      {
        permissionClean = pPermissionsRequested.substring(1);
      }
      final StringTokenizer token = new StringTokenizer(permissionClean, ",");
      final Set<String> perms = new HashSet<String>();
      while (token.hasMoreTokens())
      {
        final String nextToken = token.nextToken();
        perms.add(nextToken.replace(PortalToken.PROJECT_ID.getToken(), pProjectId));
      }
      if (isExplicit)
      {
        isAuthorized = authorizationService.isExplicitlyPermitted(perms, Logical.AND);
      }
      else
      {
        isAuthorized = authorizationService.isPermitted(perms, Logical.AND);
      }
    }
    return isAuthorized;
  }

  /**
   * Use by container to inject {@link AuthorizationService} implementation
   * 
   * @param pAuthorizationService
   *          the authorizationService to set
   */
  public void setAuthorizationService(final AuthorizationService pAuthorizationService)
  {
    authorizationService = pAuthorizationService;
  }
}
