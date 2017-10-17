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
package org.novaforge.forge.core.organization.handlers;

import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface ApplicationHandler
{
  String PLUGIN_CATEGORY = "category";

  String PLUGIN_TYPE = "type";

  String PLUGIN_VERSION = "version";

  /**
   * This method returns the role mapping of an application
   * 
   * @param pApplicationUri
   * @return Map<String, String>
   * @throws ApplicationServiceException
   */
  Map<String, String> getRoleMapping(String pApplicationUri) throws ApplicationServiceException;

  /**
   * This method returns true if an application of the referenced plugin can be added, false otherwise
   * 
   * @param pPluginUUID
   * @return
   * @throws ApplicationServiceException
   */
  boolean canAddApplication(UUID pPluginUUID) throws ApplicationServiceException;

  /**
   * This method returns true if the plugin has available tool instance
   * 
   * @param pPluginUUID
   * @return boolean
   * @throws ApplicationServiceException
   */
  boolean hasAvailableToolInstance(UUID pPluginUUID) throws ApplicationServiceException;

  /**
   * This method returns the available tool instances uuid for a plugin.
   * 
   * @param pPluginUUID
   * @return List<ToolInstance>
   * @throws ApplicationServiceException
   */
  List<ToolInstance> getAvailableToolInstances(UUID pPluginUUID) throws ApplicationServiceException;

  /**
   * This method allows to send a message to plugin in order to handle an application (create, update,
   * delete)
   * 
   * @param pPluginUUID
   * @param pInstanceUUID
   * @param pApplicationLabel
   * @param pProjectId
   * @param pRolesMapping
   * @param pToolInstanceUUID
   * @param pActionLabel
   * @param pLogin
   * @throws ApplicationServiceException
   */
  void sendApplicationMessage(String pPluginUUID, String pInstanceUUID, String pApplicationLabel,
      String pProjectId, Map<String, String> pRolesMapping, String pToolInstanceUUID, String pActionLabel,
      String pLogin) throws ApplicationServiceException;

  /**
   * @param pPluginUUID
   * @param pInstanceUUID
   * @param pApplicationLabel
   * @param pProjectId
   * @param pToolInstanceUUID
   * @param pLogin
   * @throws ApplicationServiceException
   */

  void sendForceApplicationDeleteMessage(String pPluginUUID, String pInstanceUUID, String pApplicationLabel,
      String pProjectId, String pToolInstanceUUID, String pLogin) throws ApplicationServiceException;

  /**
   * This method allows to send a message to plugin in order to handle a change in roles mapping for
   * application
   * 
   * @param pApplicationURI
   * @param pPluginUUID
   * @param pInstanceUUID
   * @param pProjectId
   * @param pRolesMapping
   * @param pActionLabel
   * @param pLogin
   * @throws ApplicationServiceException
   */
  void sendRolesMappingMessage(String pApplicationURI, String pPluginUUID, String pInstanceUUID,
      String pProjectId, Map<String, String> pRolesMapping, String pActionLabel, String pLogin)
      throws ApplicationServiceException;

  /**
   * This method returns the tool instance which hosts the application
   * 
   * @param pPluginUUID
   * @param pApplicationInstanceID
   * @return ToolInstance
   * @throws ApplicationServiceException
   */
  ToolInstance getToolInstanceByApplication(String pPluginUUID, String pApplicationInstanceID)
      throws ApplicationServiceException;

  /**
   * This method returns a map with plugin informations
   * 
   * @param pPluginUUID
   * @return a map with the following keys : ApplicationHandler.PLUGIN_CATEGORY,
   *         ApplicationHandler.PLUGIN_TYPE and ApplicationHandler.PLUGIN_VERSION
   * @throws ApplicationServiceException
   */
  Map<String, String> getPluginInfos(UUID pPluginUUID) throws ApplicationServiceException;

}
