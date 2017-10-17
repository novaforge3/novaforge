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
package org.novaforge.forge.core.plugins.handlers;

import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginService;

import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public interface PluginLifeCycleHandler
{

  /**
   * This method allows to change the lifecycle of a plugin
   * 
   * @param pPluginMetadata
   *          the plugin metadatas of the plugin
   * @param pOldStatus
   *          the old status
   * @param pNewStatus
   *          the new status
   * @throws PluginManagerException
   */
  void changePluginLifeCycle(PluginMetadata pPluginMetadata, PluginStatus pOldStatus, PluginStatus pNewStatus)
      throws PluginManagerException;

  /**
   * This method returns the authorized changes for pStatus
   * 
   * @param pStatus
   * @return List<PluginStatus>
   */
  List<PluginStatus> getAuthorizedChangesForStatus(PluginStatus pStatus);

  /**
   * This method returns all the authorized changes
   * 
   * @return Map<PluginStatus, List<PluginStatus>>
   */
  Map<PluginStatus, List<PluginStatus>> getAllAuthorizedChanges();

  /**
   * This method allows to stop the plugin bundle composite
   * 
   * @param pPluginService
   *          if null the service will be retrieve from OSGi registry using UUID given
   * @param pUUID
   * @throws PluginManagerException
   */
  void disablePluginRoute(final PluginService pPluginService, final String pUUID)
      throws PluginManagerException;

  /**
   * This method allows to start the plugin bundle composite
   * 
   * @param pPluginService
   *          if null the service will be retrieve from OSGi registry using UUID given
   * @param pUUID
   * @throws PluginManagerException
   */
  void enablePluginRoute(final PluginService pPluginService, final String pUUID)
      throws PluginManagerException;

  /**
   * This method is responsible for notifying changes from plugin lifecycle
   * 
   * @param pPluginMetadata
   * @throws PluginManagerException
   */
  void notifyPluginLifecycleChange(PluginMetadata pPluginMetadata) throws PluginManagerException;
}
