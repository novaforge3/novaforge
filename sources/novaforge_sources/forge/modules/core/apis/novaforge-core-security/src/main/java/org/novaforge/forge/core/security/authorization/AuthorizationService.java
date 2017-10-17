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
/**
 *
 */
package org.novaforge.forge.core.security.authorization;

import java.util.Set;

/**
 * Authorization Service Interface
 * 
 * @author lamirang
 */
public interface AuthorizationService
{

   /**
    * Allow to check permissions regarding current subject(user) will throw an RuntimeException
    * 
    * @param pPermissions
    *           represents collection of permissions to check
    * @param pLogical
    *           represents logical of permissions association
    * @throws RuntimeException
    */
   void checkPermissions(final Set<String> pPermissions, final Logical pLogical);

   /**
    * Allow to check permissions regarding current subject(user)
    * 
    * @param pPermissions
    *           represents collection of permissions to check
    * @param pLogical
    *           represents logical of permissions association
    * @return true or false regarding collection of permission and logical applied
    */
   boolean isPermitted(Set<String> pPermissions, Logical pLogical);

   /**
    * @param pRoles
    *           represents collection of roles to check
    * @return true or false regarding collection of roles and logical applied
    */
   boolean hasRole(Set<String> pRoles, Logical pLogical);

   /**
    * Allow to clean permission cached for a specific user
    * 
    * @param pUserName
    *           represents the login of the user
    */
   void clearPermissionCached(String pUser);

   /**
    * Returns true if the current user explicitly has the permissions (without wildcards).
    * 
    * @param pPermissions
    * @param pLogical
    * @return
    */
   boolean isExplicitlyPermitted(final Set<String> pPermissions, final Logical pLogical);

   /**
    * @param pRoles
    *           set of roles
    * @param pLogical
    *           logical to apply
    * @param pProjectId
    *           concret project id
    * @return true or false regarding collection of roles and logical applied
    * @see Logical
    */
   boolean hasRoleOnProject(Set<String> pRoles, Logical pLogical, String pProjectId);

}
