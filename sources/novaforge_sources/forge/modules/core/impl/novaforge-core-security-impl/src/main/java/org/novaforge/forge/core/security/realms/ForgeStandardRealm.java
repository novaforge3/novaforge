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
package org.novaforge.forge.core.security.realms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authorization.RoleHandler;
import org.novaforge.forge.core.security.matcher.NovaForgeCredentialsMatcher;
import org.novaforge.forge.core.security.shiro.manager.ShiroRealmManager;
import org.novaforge.forge.core.security.shiro.realm.ShiroRealm;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This realm is used to get authentification and authorization information.
 * <p>
 * The authentification info are retrieved from database
 * </p>
 * 
 * @author Guillaume Lamirand
 */
public class ForgeStandardRealm extends AuthorizingRealm implements ShiroRealm
{

  /**
   * Shiro Realm Name
   */
  public final static String REALM_NAME = "ForgeStandardRealm";
  /**
   * Logger
   */
  private static final Log   LOGGER     = LogFactory.getLog(ForgeStandardRealm.class);
  /**
   * True if the current instance has bean already registered
   */
  private boolean            isRegistered;

  /**
   * Constructor for webapp
   */
  public ForgeStandardRealm()
  {
    super();
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("New instance of ForgeStandardRealm is asking... ");
    }
    setName(REALM_NAME);
    setCredentialsMatcher(new NovaForgeCredentialsMatcher());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onInit()
  {
    ensureRegister();
    super.onInit();
  }

  /**
   * Will register this instance if necessary
   */
  private void ensureRegister()
  {
    if (!isRegistered)
    {
      if (getShiroRealmManager() != null)
      {
        getShiroRealmManager().registerRealm(this);
        isRegistered = true;
      }
    }
  }

  /**
   * Get {@link ShiroRealmManager} implementation.
   *
   * @return the {@link ShiroRealmManager}
   */
  private ShiroRealmManager getShiroRealmManager()
  {
    return getService(ShiroRealmManager.class);
  }

  /**
   * Get service implementation from JNDI
   *
   * @return the service implementation or <code>null</code>
   */
  @SuppressWarnings("unchecked")
  private <T> T getService(final Class<T> pClass)
  {
    final String canonicalName = pClass.getCanonicalName();
    T service;
    try
    {
      final InitialContext initialContext = new InitialContext();
      service = (T) initialContext.lookup(String.format("osgi:service/%s", canonicalName));

    }
    catch (final NamingException e)
    {
      throw new IllegalArgumentException(String.format("Unable to get OSGi service with [interface=%s]",
                                                       canonicalName));
    }
    return service;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals)
  {
    ensureRegister();
    AuthorizationInfo returnAuthorizationInfo;
    final String userLogin = (String) principals.fromRealm(getName()).iterator().next();

    final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    try
    {
      final Map<String, List<ProjectRole>> userRoles = getMembershipPresenter().getAllEffectiveRolesForUser(
          userLogin);
      final Set<Entry<String, List<ProjectRole>>> entrySet = userRoles.entrySet();
      for (final Entry<String, List<ProjectRole>> projectRoles : entrySet)
      {
        for (final ProjectRole role : projectRoles.getValue())
        {
          final String cacheName = getRoleHandler().buildCacheName(projectRoles.getKey(), role.getName());
          info.addRole(cacheName);
          final Set<String> permissions = new HashSet<String>();
          for (final Permission perm : role.getPermissions())
          {
            permissions.add(perm.getName());
          }
          info.addStringPermissions(permissions);
        }
      }
      returnAuthorizationInfo = info;
    }
    catch (final Exception e)
    {
      throw new AuthenticationException(String.format(
          "Unable to find list of user's membership with [login=%s]", userLogin), e);

    }
    return returnAuthorizationInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return REALM_NAME;
  }

  /**
   * Get {@link MembershipPresenter} implementation.
   *
   * @return the {@link MembershipPresenter}
   */
  private MembershipPresenter getMembershipPresenter()
  {
    return getService(MembershipPresenter.class);
  }

  /**
   * Get {@link RoleHandler} implementation.
   *
   * @return the {@link RoleHandler}
   */
  private RoleHandler getRoleHandler()
  {
    return getService(RoleHandler.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken authcToken)
      throws AuthenticationException
  {
    ensureRegister();
    AuthenticationInfo          returnAuthentification = null;
    final UsernamePasswordToken token                  = (UsernamePasswordToken) authcToken;
    String userCredential;
    try
    {
      userCredential = getUserPresenter().getUserCredential(token.getUsername());

    }
    catch (final UserServiceException e)
    {
      throw new AuthenticationException(String.format("Unable to find user with [login=%s]", token.getUsername()), e);
    }

    if (userCredential != null)
    {
      returnAuthentification = new SimpleAuthenticationInfo(token.getUsername(), userCredential, getName());
    }

    return returnAuthentification;
  }

  /**
   * Get {@link UserPresenter} implementation.
   *
   * @return the {@link UserPresenter}
   */
  private UserPresenter getUserPresenter()
  {
    return getService(UserPresenter.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearCachedAuthorizationInfo(final String pUserName)
  {
    final SimplePrincipalCollection principals = new SimplePrincipalCollection(pUserName, REALM_NAME);
    super.clearCachedAuthorizationInfo(principals);
  }

}
