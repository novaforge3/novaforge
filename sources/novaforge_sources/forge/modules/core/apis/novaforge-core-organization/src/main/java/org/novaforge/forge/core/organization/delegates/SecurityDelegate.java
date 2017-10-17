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
package org.novaforge.forge.core.organization.delegates;

import org.novaforge.forge.core.organization.model.AuthorizationResource;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.security.authorization.Logical;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public interface SecurityDelegate
{

  /**
   * Will check if resource given in parameter are authorized
   * 
   * @param pProjectId
   *          the project id associted to the resource
   * @param pAuthorizationResource
   *          the resource
   * @param pLogical
   *          logical applied to each authorizations
   */
  /**
   * Will check if resource given in parameter are authorized
   * 
   * @param pProjectId
   *          the project id associted to the resource
   * @param pAuthorizationResource
   *          the resource
   */
  void checkResource(final String pProjectId, final AuthorizationResource pAuthorizationResource);

  /**
   * Will check if resource given in parameter are authorized
   * 
   * @param pProjectId
   *          the project id associted to the resource
   * @param pAuthorizationResources
   *          the resources
   * @param pLogical
   *          logical applied to each authorizations
   */
  void checkResource(final String pProjectId, final AuthorizationResource pAuthorizationResources,
      final Logical pLogical);

  /**
   * Will check if resource given in parameter are authorized
   * 
   * @param pProjectId
   *          the project id associted to the resource
   * @param pAuthorizationResources
   *          the resources
   */
  void checkResources(final String pProjectId, final List<AuthorizationResource> pAuthorizationResources);

  /**
   * Will check if resource given in parameter are authorized
   * 
   * @param pProjectId
   *          the project id associted to the resource
   * @param pAuthorizationResources
   *          the resources
   * @param pLogical
   *          logical applied to each authorizations
   */
  void checkResources(final String pProjectId, final List<AuthorizationResource> pAuthorizationResources,
      final Logical pLogical);

  /**
   * Allow to clean permission cached for a specific user
   * 
   * @param pLogins
   *          represents users' login
   */
  void clearPermissionCached(final String... pLogins);

  /**
   * Allow to clean permission cached for a specific user
   * 
   * @param pUsers
   *          represents users
   */
  void clearPermissionCached(final List<User> pUsers);

  /**
   * Allow to get username
   * 
   * @return the login of current user identified
   */
  String getCurrentUser();

  /**
   * Check project permission
   * 
   * @param pAuthorizationResource
   *          the resource
   */
  void checkResource(final AuthorizationResource pAuthorizationResource);

  /**
   * @param pProjectId
   * @param pAuthorizationResourceImpl
   * @return
   */
  boolean isPermitted(final String pProjectId, final AuthorizationResource pAuthorizationResourceImpl);

  /**
   * @param pProjectId
   * @param pApplicationId
   * @param pPermissionAction
   * @return
   */
  boolean isPermitted(final String pProjectId, final String pApplicationId,
      PermissionAction... pPermissionAction);

}