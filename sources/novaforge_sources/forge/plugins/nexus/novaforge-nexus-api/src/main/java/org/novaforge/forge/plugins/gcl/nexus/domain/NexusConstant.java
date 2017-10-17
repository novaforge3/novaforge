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
package org.novaforge.forge.plugins.gcl.nexus.domain;

/**
 * This class declare somes constants regarding default roles. It uses by role
 * privilege enumeration.
 * 
 * @author lamirang
 */
public class NexusConstant {

	public static final String DEFAULT_APPLICATION_ID = "nexus";
	
	public static final String DEFAULT_SOURCE = "default";
	
	public static final String DEFAULT_BLOBSTORE_NAME = "default";
	
	public static final boolean DEFAULT_USER_STATUS_ACTIVE = true;
	
  /**
   * Represents anonymous proxy id
   */
  public static final String ANONYMOUS_PROXY_ID = "anonymous_proxy";
  /**
   * Represents anonymous proxy name
   */
  public static final String ANONYMOUS_PROXY_LABEL = "Anonymous Proxy";
	/**
	 * Represents anonymous id
	 */
	public static final String ANONYMOUS_ID = "anonymous";
	/**
	 * Represents anonymous name
	 */
	public static final String ANONYMOUS_LABEL = "Anonymous";
	/**
	 * Represents administrator's id
	 */
	public static final String ADMINISTRATOR_ID = "administrator";
	/**
	 * Represents administrator's name
	 */
	public static final String ADMINISTRATOR_LABEL = "Administrator";
	/**
	 * Represents developper senior's id
	 */
	public static final String DEVELOPER_SENIOR_ID = "developper_senior";
	/**
	 * Represents developper senior's name
	 */
	public static final String DEVELOPER_SENIOR_LABEL = "Developper Senior";
	/**
	 * Represents developper's id
	 */
	public static final String DEVELOPER_ID = "developper";
	/**
	 * Represents developper's name
	 */
	public static final String DEVELOPER_LABEL = "Developper";
	/**
	 * Represents observer's id
	 */
	public static final String OBSERVER_ID = "observer";
	/**
	 * Represents observer's id
	 */
	public static final String OBSERVER_LABEL = "Observer";

	/**
	 * Default privileges for Anonymous Proxy Role
	 */
	public static final String PRIV_HEALTHCHECK_READ = "nx-healthcheck-read";
	public static final String PRIV_SEARCH_READ = "nx-search-read";

	/**
	 * Default anonymous user Id and role Id
	 */
	public static final String DEFAULT_ANONYMOUS_USER_ID = "anonymous";
	public static final String DEFAULT_ANONYMOUS_ROLE_ID = "nx-anonymous";
}
