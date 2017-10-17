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
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Filter that allows access to a path for an unknown user. If the user is known and authenticated so the user
 * is redirected to successful page.
 * 
 * @author Guillaume Lamirand
 */
public class AnonUserFilter extends AuthenticationFilter
{
  /**
   * Returns <code>true</code> if the current
   * {@link #getSubject(javax.servlet.ServletRequest, javax.servlet.ServletResponse) subject} is
   * <code>null</code> or unauthenticated, <code>false</code> otherwise.
   * 
   * @return <code>false</code> if the current
   *         {@link #getSubject(javax.servlet.ServletRequest, javax.servlet.ServletResponse) subject} is
   *         <code>null</code> or unauthenticated, <code>false</code> otherwise.
   */
  @Override
  protected boolean isAccessAllowed(final ServletRequest request, final ServletResponse response,
      final Object mappedValue)
  {
    final Subject subject = getSubject(request, response);
    // If principal is null, then the user is unknown and should be allowed access to page.
    return (subject.getPrincipal() == null) || (!subject.isAuthenticated());
  }

  /**
   * This default implementation simply calls
   * {@link #issueSuccessRedirect(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   * issueSuccessRedirect} and then immediately returns <code>false</code>, thereby preventing the
   * chain from continuing so the redirect may execute.
   */
  @Override
  protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response)
      throws Exception
  {
    issueSuccessRedirect(request, response);
    return false;
  }
}
