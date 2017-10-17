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
package org.novaforge.forge.plugins.devops.novadeploy.internal;

/**
 * Declare constants used to communication with novadpeloy instance.
 * 
 * @author dekimpea
 */
public final class NovadeployRestConstant
{
	/**
	 * Declare REST method to login to Novadeploy
	 */
	public static final String Novadeploy_METHOD_LOGIN = "/login";
	/**
	 * Declare REST method to create an account on Novadeploy
	 */
	public static final String Novadeploy_METHOD_INIT_CLOUD = "/cloud";

	public static final String Novadeploy_METHOD_LOCK_CLOUD = "/cloud/lock";

	public static final String Novadeploy_METHOD_VALIDATE_CLOUD = "/cloud/validate";

	public static final String Novadeploy_METHOD_CONFIG_CLOUD = "/cloud/config";

	public static final String Novadeploy_METHOD_UNLOCK_CLOUD = "/cloud/unlock";

	public static final String Novadeploy_METHOD_STATUS_CLOUD = "/cloud/status";

	public static final String Novadeploy_METHOD_GET_ACCOUNT_MEMBERSHIPS = "/cloud/organization";
	/**
	 * Declare REST method to set user permission on Novadeploy
	 */
	public static final String Novadeploy_METHOD_SET_PERMISSION = "/user/link";
	/**
	 * Declare REST method to delete user permission on Novadeploy
	 */
	public static final String Novadeploy_METHOD_DELETE_PERMISSION = "/user/unlink";
	/**
	 * Declare REST method to create an user on Novadeploy
	 */
	public static final String Novadeploy_METHOD_CREATE_USER = "/user/add";
	/**
	 * Declare REST method to update an user on Novadeploy
	 */
	public static final String Novadeploy_METHOD_UPDATE_USER = "/user/update";
	/**
	 * Declare REST method to delete an user on Novadeploy
	 */
	public static final String Novadeploy_METHOD_DELETE_USER = "/user/remove";

	// Descriptor Management
	public static final String Novadeploy_METHOD_GET_DESCRIPTORS = "/descriptor/list";
	public static final String Novadeploy_METHOD_ADD_DESCRIPTOR = "/descriptor/new";
	public static final String Novadeploy_METHOD_FORK_DESCRIPTOR = "/descriptor/fork";
	public static final String Novadeploy_METHOD_VALIDATE_DESCRIPTOR = "/descriptor/validate";
	public static final String Novadeploy_METHOD_GET_DESCRIPTOR = "/descriptor/get";
	public static final String Novadeploy_METHOD_UPDATE_DESCRIPTOR = "/descriptor/update";
	public static final String Novadeploy_METHOD_GET_DESCRIPTORS_TREE = "/descriptor/arborescence";
	// Environment Management
	public static final String Novadeploy_METHOD_DEPLOY_ENVIRONMENT = "/environment/deploy";
	public static final String Novadeploy_METHOD_DEPLOY_ENVIRONMENT_STATUS = "/environment/deploy/status";
	public static final String Novadeploy_METHOD_DELETE_ENVIRONMENT = "/environment/delete";
	public static final String Novadeploy_METHOD_LIST_ENVIRONMENTS = "/environment/list";
	public static final String Novadeploy_METHOD_GET_ENVIRONMENT = "/environment/get";

}
