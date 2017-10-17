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
package org.novaforge.forge.core.security.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.security.cas.CasSecurityUrl;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Filter that allows access to resources if the accessor is a known user, which is defined as
 * having a known principal. This means that any user who is authenticated or remembered via a
 * 'remember me' feature will be allowed access from this filter.
 * <p/>
 * If the accessor is not a known user, then they will be redirected to the {@link #setLoginUrl(String)
 * loginUrl}
 * </p>
 * 
 * @author Guillaume Lamirand
 */
public class CasAuthFilter extends AccessControlFilter
{
  /**
   * Slash url separator
   */
  private static final String SUFFIX      = "/";

  /**
   * The login url to used to authenticate a user, used when redirecting users if authentication is required.
   */
  private String              casEndPoint = DEFAULT_LOGIN_URL;

  /**
   * Default constructor.
   */
  public CasAuthFilter()
  {
    super();
  }

  /**
   * Returns <code>true</code> if the request is a
   * {@link #isLoginRequest(javax.servlet.ServletRequest, javax.servlet.ServletResponse) loginRequest} or
   * if the current {@link #getSubject(javax.servlet.ServletRequest, javax.servlet.ServletResponse) subject}
   * is not <code>null</code>, <code>false</code> otherwise.
   * 
   * @return <code>true</code> if the request is a
   *         {@link #isLoginRequest(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   *         loginRequest} or
   *         if the current {@link #getSubject(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   *         subject} is not <code>null</code>, <code>false</code> otherwise.
   */
  @Override
  protected boolean isAccessAllowed(final ServletRequest pRequest, final ServletResponse pResponse,
      final Object pMappedValue)
  {
    boolean isAccessAllowed = isLoginRequest(pRequest, pResponse);
    if (!isAccessAllowed)
    {
      final Subject subject = getSubject(pRequest, pResponse);
      // If principal is not null, then the user is known and should be allowed access.
      isAccessAllowed = subject.getPrincipal() != null;
    }
    return isAccessAllowed;
  }

  /**
   * This default implementation simply calls
   * {@link #saveRequestAndRedirectToLogin(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   * saveRequestAndRedirectToLogin} and then immediately returns <code>false</code>, thereby preventing the
   * chain from continuing so the redirect may
   * execute.
   */
  @Override
  protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response)
      throws Exception
  {
    saveRequest(request);
    final String casUrl = getCasSecurityUrl().getCasLogin(getCasService());

    /*
     * The following code allows to get a particuliar http status code.
     * In particular for POST requests, we need to get an 303 error to avoid reposting.
     * Take a look at Post/Redirect/Get (PRG) pattern.
     */
    boolean http10Compatible = false;
    final HttpServletRequest http = WebUtils.toHttp(request);
    if (GET_METHOD.equals(http.getMethod()))
    {
      http10Compatible = true;
    }
    WebUtils.issueRedirect(request, response, casUrl, null, false, http10Compatible);
    return false;
  }

  /**
   * Get {@link CasSecurityUrl} implementation.
   *
   * @return service implementation available.
   */
  private CasSecurityUrl getCasSecurityUrl()
  {
    return getService(CasSecurityUrl.class);
  }

  /**
   * Return the complete service url build from {@link CasAuthFilter#casEndPoint}
   *
   * @return cas service url
   */
  private String getCasService()
  {
    final StringBuilder casService = new StringBuilder();
    if ((casEndPoint.startsWith("http://")) || (casEndPoint.startsWith("https://")))
    {
      casService.append(casEndPoint);
    }
    else
    {
      casService.append(getForgeConfigurationService().getPublicUrl());
      if ((!casService.toString().endsWith(SUFFIX)) && (!casEndPoint.startsWith(SUFFIX)))
      {
        casService.append(SUFFIX);
      }
      else if ((casService.toString().endsWith(SUFFIX)) && (casEndPoint.startsWith(SUFFIX)))
      {
        casEndPoint = casEndPoint.substring(1);
      }
      casService.append(casEndPoint);
    }
    return casService.toString();
  }

  /**
   * Return the service implementation available for the given class.
   *
   * @param pClass
   *          the service class
   * @return service implementation available.
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
          canonicalName), e);
    }
    return service;

  }

  /**
   * Get {@link ForgeCfgService} implementation.
   *
   * @return service implementation available.
   */
  private ForgeConfigurationService getForgeConfigurationService()
  {
    return getService(ForgeConfigurationService.class);
  }

  /**
   * Gets the cas end point service URL used to authenticate a user.
   *
   * @return the casEndPoint
   */
  public String getCasEndPoint()
  {
    return casEndPoint;
  }

  /**
   * Sets the cas end point service URL used to authenticate a user.
   *
   * @param casEndPoint
   *     the casEndPoint to set
   */
  public void setCasEndPoint(final String casEndPoint)
  {
    this.casEndPoint = casEndPoint;
    setLoginUrl(getCasSecurityUrl().getCasLogin(getCasService()));
  }

}
