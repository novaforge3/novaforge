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
package org.novaforge.forge.core.security.shiro.realm;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.CachingRealm;

/**
 * Describes a shiro realm used by a web application.
 * 
 * @author Guillaume Lamirand
 */
public interface ShiroRealm
{

  /**
   * Set the cache manager
   * 
   * @param pCacheManager
   *          the cacheManager to set
   * @see CachingRealm#setCacheManager(CacheManager)
   */
  void setCacheManager(final CacheManager pCacheManager);

  /**
   * Allow to enable cache mode
   * 
   * @param pCachingEnabled
   *          true to enable caching, false otherwise
   * @see CachingRealm#setCachingEnabled(boolean)
   */
  void setCachingEnabled(final boolean pCachingEnabled);

  /**
   * Allow to enable cache mode on authorization
   * 
   * @param pAuthorizationCachingEnabled
   *          true to enable caching, false otherwise
   * @see AuthorizingRealm#setAuthenticationCachingEnabled(boolean)
   */
  void setAuthorizationCachingEnabled(boolean pAuthorizationCachingEnabled);

  /**
   * Will clear the cache if existing for an user name
   * 
   * @param pUserName
   *          the name of the user
   */
  void clearCachedAuthorizationInfo(final String pUserName);
}
