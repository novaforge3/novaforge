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
package org.novaforge.forge.core.plugins.services;

import org.novaforge.forge.core.plugins.categories.PluginCategoryService;
import org.novaforge.forge.core.plugins.categories.PluginRealm;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class defines a service which will manage plugins into forge
 * 
 * @author Guillaume Lamirand
 * @author trolat-f
 */
public interface PluginsManager
{
  /**
   * This method allows to register an new plugin
   * 
   * @param pPluginService
   *          represents {@link PluginService}
   * @throws PluginManagerException
   */
  void registerPlugin(final PluginService pPluginService) throws PluginManagerException;

  /**
   * This method allows to update plugin metadata
   * 
   * @param pUUID
   *          represents plugin uuid
   * @throws PluginManagerException
   */
  void updatePluginMetadata(final UUID pUUID) throws PluginManagerException;

  /**
   * This method allows to update plugin availability
   * 
   * @param pUUID
   *          represents plugin uuid
   * @param pAvailability
   *          represents availability with true or false
   * @throws PluginManagerException
   */
  void updatePluginAvailability(UUID pUUID, boolean pAvailability) throws PluginManagerException;

  /**
   * This method allows to get plugin service implementation
   * 
   * @param pId
   *          represents plugin uuid
   * @return {@link PluginService} found
   * @throws PluginManagerException
   */
  PluginService getPluginService(final String pId) throws PluginManagerException;

  /**
   * This method allows to get all plugins metadata
   * 
   * @return list of plugin metadata
   * @throws PluginManagerException
   */
  List<PluginMetadata> getAllPlugins() throws PluginManagerException;

  /**
   * This method allows to get all plugins metadata for a specific category
   * 
   * @param pLabel
   *          represents category labels
   * @return list of plugin metadata
   * @throws PluginManagerException
   */
  List<PluginMetadata> getPluginsMetadataByCategory(final String... pLabel) throws PluginManagerException;

  /**
   * This method allows to get all instantiable plugins metadata for a specific category. Instantiable means
   * : available and Status.ACTIVATED
   * 
   * @param pLabel
   *          represents category label
   * @return list of plugins metadata
   * @throws PluginManagerException
   */
  List<PluginMetadata> getAllInstantiablePluginsMetadataByCategory(final String pLabel)
      throws PluginManagerException;

  /**
   * This method allows to get all instantiable plugins metadata for a specific realm, ie user or system
   * realm. Instantiable means
   * : available and Status.ACTIVATED
   * 
   * @param pRealmType
   *          represents category label
   * @return list of plugins metadata
   * @throws PluginManagerException
   */
  List<PluginMetadata> getAllInstantiablePluginsMetadataByRealm(final PluginRealm pRealmType)
      throws PluginManagerException;

  /**
   * This method allows to get all plugins metadata for a specific type
   * 
   * @param pLabel
   *          represents type label
   * @return list of plugin metadata
   * @throws PluginManagerException
   */
  List<PluginMetadata> getPluginsMetadataByType(final String pLabel) throws PluginManagerException;

  /**
   * This method allows to get all plugins metadata for a specific status
   * 
   * @param pStatus
   *          represents status used as filter
   * @return list of plugin metadata
   * @throws PluginManagerException
   */
  List<PluginMetadata> getPluginsMetadataByStatus(final PluginStatus pStatus) throws PluginManagerException;

  /**
   * This method allows to get all instantiable plugins metadata. Instantiable means : available and
   * Status.ACTIVATED
   * 
   * @return list of plugin metadata
   * @throws PluginManagerException
   */
  List<PluginMetadata> getAllInstantiablePluginsMetadata() throws PluginManagerException;

  /**
   * Allows to know if a plugin is available in OSGI Registry.
   * 
   * @param pId
   *          represents plugin uuid
   * @return availability of plugin
   */
  boolean isAvailablePlugin(final String pId);

  /**
   * Return PluginMetadata Object regaring a plugin uuid
   * 
   * @param pPluginUUID
   *          represents the key of current search
   * @return PluginMetadata found
   * @throws PluginManagerException
   */
  PluginMetadata getPluginMetadataByUUID(final String pPluginUUID) throws PluginManagerException;

  /**
   * This method allows to get all plugins categories
   * 
   * @return list of plugin categories
   * @throws PluginManagerException
   */
  List<String> getAllPluginCategories() throws PluginManagerException;

  /**
   * This method returns all the plugin status
   * 
   * @return List<Status>
   */
  List<PluginStatus> getAllPluginStatus();

  /**
   * This method allows to change the plugin status
   * 
   * @param pPluginUUID
   *          the reference to the plugin
   * @param pStatus
   *          the new status
   * @throws PluginManagerException
   */
  void changePluginStatus(String pPluginUUID, PluginStatus pStatus) throws PluginManagerException;

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
   * This method allows to get a Category specific plugin functional service
   * 
   * @param pId
   *          represents plugin uuid
   * @param pClassService
   *          the target class service wanted
   * @return the target service found
   * @throws PluginManagerException
   */
  <T extends PluginCategoryService> T getPluginCategoryService(final String pId, Class<T> pClassService)
      throws PluginManagerException;

  /**
   * Get the instance url for a specific instance according to the view given,
   * 
   * @param pPluginUUID
   *          represents plugin uuid
   * @param pInstanceUUID
   *          represents instance uuid
   * @param pPluginView
   *          the requested view
   * @return access URI to the requested view (shouldn't start with http:// or https://)
   * @throws PluginManagerException
   */
  URI getViewAccessForInstance(final String pPluginUUID, final String pInstanceUUID,
      final PluginViewEnum pPluginView) throws PluginManagerException;

  /**
   * Get the instance url for a specific plugin tool according to the view given,
   * 
   * @param pPluginUUID
   *          represents plugin uuid
   * @param pToolUUID
   *          represents plugin tool uuid
   * @param pPluginView
   *          the requested view
   * @return access URI to the requested view (shouldn't start with http:// or https://)
   * @throws PluginManagerException
   */
  URI getViewAccessForTool(final String pPluginUUID, final String pToolUUID, final PluginViewEnum pPluginView)
      throws PluginManagerException;

  /**
   * This method returns true if the plugin has available tool instance
   * 
   * @param pUUID
   * @return boolean
   * @throws PluginManagerException
   */
  boolean hasAvailableToolInstance(UUID pUUID) throws PluginManagerException;

  /**
   * This methode returns true if the plugin can provide tool instances
   * 
   * @param pUUID
   * @return boolean
   * @throws PluginManagerException
   */
  boolean isToolInstanceProvisionable(UUID pUUID) throws PluginManagerException;

  /**
   * This method allwos to initialize a new tool instance. If second parameter is null the default instance
   * will be initialize
   * 
   * @param pPluginService
   *          plugin service
   * @param pInstanceName
   *          tool instance name, if <code>null</code> all instance declared will be set up
   * @throws PluginManagerException
   */
  void configureToolInstance(PluginService pPluginService, UUID pToolUUID) throws PluginManagerException;

}
