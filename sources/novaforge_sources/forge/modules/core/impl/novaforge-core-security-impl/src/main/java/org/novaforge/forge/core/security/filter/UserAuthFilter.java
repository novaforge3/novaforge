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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter that allows access to resources if the accessor is a known user, which is defined as
 * having a known principal. This means that any user who is authenticated or remembered via a
 * 'remember me' feature will be allowed access from this filter.
 * <p/>
 * If the accessor is not a known user, then it will be blocked.
 * </p>
 * 
 * @author Guillaume Lamirand
 */
public class UserAuthFilter extends PathMatchingFilter
{

  /**
   * Returns <code>true</code> if {@link #isAccessAllowed(ServletRequest, ServletResponse, Object)
   * isAccessAllowed(Request,Response,Object)},
   * otherwise returns the result of {@link #onAccessDenied(ServletRequest, ServletResponse, Object)
   * onAccessDenied(Request,Response,Object)}.
   *
   * @return <code>true</code> if
   *         {@link #isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, Object)
   *         isAccessAllowed},
   *         otherwise returns the result of
   *         {@link #onAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   *         onAccessDenied}.
   * @throws Exception
   *           if an error occurs.
   */
  @Override
  public boolean onPreHandle(final ServletRequest request, final ServletResponse response, final Object mappedValue)
      throws Exception
  {
    return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
  }

  /**
   * Returns <code>true</code> if the current
   * {@link #getSubject(javax.servlet.ServletRequest, javax.servlet.ServletResponse) subject} is not
   * <code>null</code>, <code>false</code> otherwise.
   *
   * @return <code>true</code> if the request is a
   *         {@link #isLoginRequest(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   *         loginRequest} or
   *         if the current {@link #getSubject(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   *         subject} is not <code>null</code>, <code>false</code> otherwise.
   */
  protected boolean isAccessAllowed(final ServletRequest request, final ServletResponse response,
      final Object mappedValue)
  {
    final Subject subject = SecurityUtils.getSubject();
    // If principal is not null, then the user is known and should be allowed access.
    return subject.getPrincipal() != null;

  }

  /**
   * Sets an unauthorized header and returns <code>false</code>. User is always considered authenticated by
   * this filter.
   */
  protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response,
      final Object mappedValue) throws Exception
  {
    final HttpServletResponse httpResponse = WebUtils.toHttp(response);
    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    return false;
  }
}
