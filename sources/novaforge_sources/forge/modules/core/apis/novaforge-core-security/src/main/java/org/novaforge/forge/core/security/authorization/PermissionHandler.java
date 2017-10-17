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
package org.novaforge.forge.core.security.authorization;

import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public interface PermissionHandler
{

	/**
	 * Build a set of permissions for forge it-self regarding actions.
	 * 
	 * @param pActions
	 *          represents actions as parameters
	 * @return a set of permission
	 * @see org.novaforge.forge.core.organization.services.PermissionAction
	 */
	Set<String> buildForgePermissions(final PermissionAction... pActions);

	/**
	 * Build a set of permissions for project resource regarding actions.
	 * 
	 * @param pActions
	 *          represents actions as parameters
	 * @return a set of permission
	 * @see org.novaforge.forge.core.organization.services.PermissionAction
	 */
	Set<String> buildProjectPermissions(final PermissionAction... pActions);

	/**
	 * Build a map of permissions regarding project id and a collection of actions for all resources know.
	 * 
	 * @param pProjectId
	 *          represents the project id
	 * @param pActions
	 *          represents the collection of action
	 * @return a set of permission
	 * @see org.novaforge.forge.core.organization.services.PermissionAction
	 */
	Set<String> buildProjectPermissions(String pProjectId, final PermissionAction... pActions);

	/**
	 * Build a set of permissions regarding project id, a specific resource and some actions.
	 * 
	 * @param pProjectId
	 *          represents the project id
	 * @param pResourceClass
	 *          represents interface which represents the resource (Role, Membership)
	 * @param pActions
	 *          represents action as parameters
	 * @return a set of permission
	 * @see org.novaforge.forge.core.organization.services.PermissionAction
	 */
	<T> Set<String> buildProjectPermissions(final String pProjectId, final Class<T> pResourceClass,
	    final List<PermissionAction> pActions);

	/**
	 * Build a set of permissions regarding project id, a specific resource and some actions.
	 * 
	 * @param pProjectId
	 *          represents the project id
	 * @param pResourceClass
	 *          represents interface which represents the resource (Role, Membership)
	 * @param pActions
	 *          represents action as parameters
	 * @return a set of permission
	 * @see org.novaforge.forge.core.organization.services.PermissionAction
	 */
	<T> Set<String> buildProjectPermissions(final String pProjectId, final Class<T> pResourceClass,
	    final PermissionAction... pActions);

	/**
	 * Build the specific Project Admin permission regarding project id.
	 * 
	 * @param pProjectId
	 *          represents the project id
	 * @return a permission
	 */
	String buildProjectAdminPermission(final String pProjectId);

	/**
	 * Build a set of permissions regarding project id, application id and a collection of actions.
	 * 
	 * @param pProjectId
	 *          represents the project id
	 * @param pApplicationId
	 * @param pActions
	 *          represents the collection of action
	 * @return a set of permission
	 * @see org.novaforge.forge.core.organization.services.PermissionAction
	 */
	Set<String> buildApplicationPermission(final String pProjectId, final String pApplicationId,
	    final PermissionAction... pActions);

	/**
	 * Resolve the project Id from a specific permission.
	 * 
	 * @param pPermissions
	 *          represents the permission
	 * @return project Id found or null
	 */
	String resolveProjectId(String pPermissions);

	/**
	 * @param pPermissions
	 * @return
	 */
	Set<String> resolveApplicationId(Set<String> pPermissions);

}