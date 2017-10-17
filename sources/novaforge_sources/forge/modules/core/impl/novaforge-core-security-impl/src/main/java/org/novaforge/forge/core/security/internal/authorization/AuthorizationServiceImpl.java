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
package org.novaforge.forge.core.security.internal.authorization;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.novaforge.forge.core.security.authorization.AuthorizationService;
import org.novaforge.forge.core.security.authorization.Logical;
import org.novaforge.forge.core.security.authorization.RoleHandler;
import org.novaforge.forge.core.security.internal.authorization.nowildcard.NoWildcardPermissionResolver;
import org.novaforge.forge.core.security.realms.ForgeJPARealm;
import org.novaforge.forge.core.security.realms.ForgeStandardRealm;
import org.novaforge.forge.core.security.shiro.manager.ShiroRealmManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of Authz Service. It is based on Shiro Components.
 * 
 * @author lamirang
 */
public class AuthorizationServiceImpl implements AuthorizationService
{
  /**
   * {@link ShiroRealmManager} service injected by container
   */
  private ShiroRealmManager shiroRealmManager;
  /**
   * {@link RoleHandler} service injected by container
   */
  private RoleHandler       roleHandler;

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkPermissions(final Set<String> pPermissions, final Logical pLogical)
  {
    final Subject subject = SecurityUtils.getSubject();

    if (pPermissions.size() == 1)
    {
      subject.checkPermission(pPermissions.iterator().next());
    }
    else if (Logical.AND.equals(pLogical))
    {
      subject.checkPermissions(pPermissions.toArray(new String[pPermissions.size()]));
    }
    else if (Logical.OR.equals(pLogical))
    {
      boolean hasAtLeastOnePermission = false;
      for (final String permission : pPermissions)
      {
        if (subject.isPermitted(permission))
        {
          hasAtLeastOnePermission = true;
        }
      }
      if (!hasAtLeastOnePermission)
      {
        subject.checkPermission(pPermissions.iterator().next());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPermitted(final Set<String> pPermissions, final Logical pLogical)
  {
    boolean returnValue = false;
    final Subject subject = SecurityUtils.getSubject();

    if (pPermissions.size() == 1)
    {
      returnValue = subject.isPermitted(pPermissions.iterator().next());
    }
    else if (Logical.AND.equals(pLogical))
    {
      returnValue = subject.isPermittedAll(pPermissions.toArray(new String[pPermissions.size()]));
    }
    else if (Logical.OR.equals(pLogical))
    {
      for (final String permission : pPermissions)
      {
        if (subject.isPermitted(permission))
        {
          returnValue = true;
          break;
        }
      }
    }
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRole(final Set<String> pRoles, final Logical pLogical)
  {
    boolean returnValue = false;
    final Subject subject = SecurityUtils.getSubject();

    if (pRoles.size() == 1)
    {
      returnValue = subject.hasRole(pRoles.iterator().next());
    }
    else if (Logical.AND.equals(pLogical))
    {
      returnValue = subject.hasAllRoles(pRoles);
    }
    else if (Logical.OR.equals(pLogical))
    {
      for (final String role : pRoles)
      {
        if (subject.hasRole(role))
        {
          returnValue = true;
          break;
        }
      }
    }
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearPermissionCached(final String pUser)
  {
    shiroRealmManager.clearRealmsCache(pUser);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isExplicitlyPermitted(final Set<String> pPermissions, final Logical pLogical)
  {
    boolean returnValue = false;
    // final String userLogin = getCurrentUser();
    final SecurityManager   securityManager = SecurityUtils.getSecurityManager();
    final Collection<Realm> realms          = ((RealmSecurityManager) securityManager).getRealms();
    for (final Realm realm : realms)
    {
      if ((ForgeJPARealm.REALM_NAME.equals(realm.getName())) || (ForgeStandardRealm.REALM_NAME.equals(realm.getName())))
      {
        final AuthorizingRealm authorizingRealm = (AuthorizingRealm) realm;
        // Get the current resolver
        final PermissionResolver wildCardResolver = authorizingRealm.getPermissionResolver();
        // Set our resolver
        authorizingRealm.setPermissionResolver(new NoWildcardPermissionResolver());

        returnValue = isPermitted(pPermissions, pLogical);
        // Set back resolver
        authorizingRealm.setPermissionResolver(wildCardResolver);
      }
    }
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRoleOnProject(final Set<String> pRoles, final Logical pLogical, final String pProjectId)
  {
    final Set<String> userRoles = new HashSet<String>();

    for (final String roleName : pRoles)
    {
      final String cacheName = roleHandler.buildCacheName(pProjectId, roleName);
      userRoles.add(cacheName);
    }

    return hasRole(userRoles, pLogical);
  }

  /**
   * Used by container as injection method
   * 
   * @param pShiroRealmManager
   *          the shiroRealmManager to set
   */
  public void setShiroRealmManager(final ShiroRealmManager pShiroRealmManager)
  {
    shiroRealmManager = pShiroRealmManager;
  }

  /**
   * Used by container as injection method
   * 
   * @param pRoleHandler
   *          the roleHandler to set
   */
  public void setRoleHandler(final RoleHandler pRoleHandler)
  {
    roleHandler = pRoleHandler;
  }
}
