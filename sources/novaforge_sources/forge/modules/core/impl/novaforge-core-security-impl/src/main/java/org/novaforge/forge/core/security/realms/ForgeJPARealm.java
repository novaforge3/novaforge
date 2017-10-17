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
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.Saml11TicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.security.authorization.RoleHandler;
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
 * The authentification info are retrieved from {@link CasRealm}
 * </p>
 * 
 * @author Guillaume Lamirand
 */
public class ForgeJPARealm extends CasRealm implements ShiroRealm
{

  /**
   * Shiro Realm Name
   */
  public final static String  REALM_NAME = "ForgeJPARealm";
  /**
   * Logger
   */
  private static final Log    LOGGER     = LogFactory.getLog(ForgeJPARealm.class);
  /**
   * The url prefix/suffix
   */
  private static final String URL_PREFIX = "/";

  /**
   * This is the CAS service end-point of the application (example : /mycontextpath/shiro-cas)
   */
  private String              casEndPoint;
  /**
   * True if the current instance has bean already registered
   */
  private boolean             isRegistered;

  /**
   * Constructor for webapp
   */
  public ForgeJPARealm()
  {
    super();
    setName(REALM_NAME);
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("New instance of ForgeJPARealm is asking... ");
    }
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
   * Always create a new token object to take of cas url prefix change
   */
  @Override
  protected TicketValidator ensureTicketValidator()
  {
    TicketValidator ticketValidator = null;
    if (isRegistered)
    {
      final String urlPrefix = getCasServerUrlPrefix();
      if ("saml".equalsIgnoreCase(getValidationProtocol()))
      {
        return new Saml11TicketValidator(urlPrefix);
      }
      else
      {
        ticketValidator = new Cas20ServiceTicketValidator(urlPrefix);
      }
    }
    return ticketValidator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken pToken)
      throws AuthenticationException
  {
    ensureRegister();
    return super.doGetAuthenticationInfo(pToken);

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
   * Get {@link MembershipProxyManager} implementation.
   *
   * @return {@link MembershipProxyManager} implementation
   */
  private MembershipPresenter getMembershipPresenter()
  {
    return getService(MembershipPresenter.class);
  }

  /**
   * Get {@link RoleHandler} implementation.
   *
   * @return {@link RoleHandler} implementation
   */
  private RoleHandler getRoleHandler()
  {
    return getService(RoleHandler.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCasServerUrlPrefix()
  {
    return getForgeConfigurationService().getCasUrl().toExternalForm();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCasService()
  {
    final StringBuilder casService = new StringBuilder();
    if ((casEndPoint.startsWith("http://")) || (casEndPoint.startsWith("https://")))
    {
      casService.append(casEndPoint);
    }
    else
    {
      casService.append(getForgeConfigurationService().getPublicUrl());
      if ((!casService.toString().endsWith(URL_PREFIX)) && (!casEndPoint.startsWith(URL_PREFIX)))
      {
        casService.append(URL_PREFIX);
      }
      else if ((casService.toString().endsWith(URL_PREFIX)) && (casEndPoint.startsWith(URL_PREFIX)))
      {
        casEndPoint = casEndPoint.substring(1);
      }
      casService.append(casEndPoint);
    }
    return casService.toString();
  }

  /**
   * Get {@link ForgeCfgService} implementation.
   *
   * @return {@link ForgeCfgService} implementation
   */
  private ForgeConfigurationService getForgeConfigurationService()
  {
    return getService(ForgeConfigurationService.class);
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
   * @return {@link ShiroRealmManager} implementation
   */
  private ShiroRealmManager getShiroRealmManager()
  {
    return getService(ShiroRealmManager.class);
  }

  /**
   * Return the service implementation available for the given class.
   *
   * @param pClass
   *          the service class
   * @return service implementation available.
   */
  @SuppressWarnings("unchecked")
  private <T> T getService(final Class<T> pClass) throws IllegalArgumentException
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
          canonicalName), e);
    }
    return service;
  }

  /**
   * Get the cas end point
   *
   * @return cas endpoint set
   */
  public String getCasEndPoint()
  {
    return casEndPoint;

  }

  /**
   * Set the cas end point of the application
   *
   * @param casEndPoint
   *     end point to set
   */
  public void setCasEndPoint(final String casEndPoint)
  {
    this.casEndPoint = casEndPoint;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getAuthorizationCacheKey(final PrincipalCollection principals)
  {
    return principals.fromRealm(getName()).iterator().next();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearCachedAuthorizationInfo(final String pUserName)
  {
    final PrincipalCollection principalCollection = new SimplePrincipalCollection(pUserName, getName());
    super.clearCachedAuthorizationInfo(principalCollection);
  }

}
