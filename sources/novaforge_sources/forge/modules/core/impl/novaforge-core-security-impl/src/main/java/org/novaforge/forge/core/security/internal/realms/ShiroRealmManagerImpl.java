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
package org.novaforge.forge.core.security.internal.realms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.novaforge.forge.core.security.shiro.manager.ShiroRealmManager;
import org.novaforge.forge.core.security.shiro.realm.ShiroRealm;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation of {@link ShiroRealmManager}.
 * <p>
 * It uses a {@link MemoryConstrainedCacheManager} as cache manager.
 * 
 * @author Guillaume Lamirand
 */
public class ShiroRealmManagerImpl implements ShiroRealmManager
{
  /**
   * Logger
   */
  private static final Log              LOGGER          = LogFactory.getLog(ShiroRealmManagerImpl.class);

  /**
   * Contains reference to all {@link ShiroRealm} regitered
   */
  private final List<ShiroRealm>        registeredRealm = new ArrayList<ShiroRealm>();
  /**
   * 
   */
  private MemoryConstrainedCacheManager cacheManager;

  /**
   * Callback method on initialization
   */
  public void init()
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Initialize Shiro Realm Manager ...");
    }
    cacheManager = new MemoryConstrainedCacheManager();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerRealm(final ShiroRealm pShiroRealm)
  {
    // TODO use EhCache
    // EhCacheManager cacheManager = new EhCacheManager();
    // setCacheManager(cacheManager);
    pShiroRealm.setCacheManager(cacheManager);
    pShiroRealm.setAuthorizationCachingEnabled(true);
    pShiroRealm.setCachingEnabled(true);
    registeredRealm.add(pShiroRealm);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearRealmsCache(final String pUserName)
  {
    for (final ShiroRealm realm : registeredRealm)
    {
      realm.clearCachedAuthorizationInfo(pUserName);
    }
  }
}
