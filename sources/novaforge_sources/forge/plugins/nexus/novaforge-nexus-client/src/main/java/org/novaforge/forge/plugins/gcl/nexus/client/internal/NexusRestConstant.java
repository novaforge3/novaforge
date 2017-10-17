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
package org.novaforge.forge.plugins.gcl.nexus.client.internal;

/**
 * Declare constants used to communication with nexus instance.
 * 
 * @author lamirang
 */
public final class NexusRestConstant
{
	/**
	 * Declare end point to nexus' privilege service
	 */
	public static final String	PRIVILEGE_SERVICE			= "privileges_target";
	/**
	 * Declare end point to nexus' user service
	 */
	public static final String	USERS_SERVICE				= "users";
	/**
	 * Declare end point to nexus' user change pwd service
	 */
	public static final String	USERS_SETPWD_SERVICE	= "users_setpw";
	/**
	 * Declare end point to nexus' roles service
	 */
	public static final String	ROLES_SERVICE				= "roles";
	/**
	 * Declare end point to nexus' repositories service
	 */
	public static final String	REPOSITORY_SERVICE		= "repositories";
	/**
	 * This defined a default repository type.
	 */
	public static final String	REPOSITORY_TYPE			= "hosted";
	/**
	 * This defined a default repository provider.
	 * <p>
	 * Provider :[hosted, proxy, virtual]
	 * </p>
	 */
	public static final String	REPOSITORY_PROVIDER		= "maven2";
	/**
	 * This defined a default repository format.
	 * <p>
	 * Repository Format : [maven1, maven2, maven-site, eclipse-update-site]
	 * </p>
	 */
	public static final String	REPOSITORY_FORMAT			= "maven2";
	/**
	 * This defined a default CheckSum policy.
	 * <p>
	 * CheckSum policy : [ignore, warn, strictIfExists, strict]
	 * </p>
	 */
	public static final String	CHECK_SUM_POLICY			= "IGNORE";
}
