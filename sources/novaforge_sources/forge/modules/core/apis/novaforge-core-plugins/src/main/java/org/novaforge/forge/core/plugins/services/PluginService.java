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

import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.domain.route.PluginAssociation;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This class defines an interface which describes a common plugin service. It has to be implement by plugins
 * to be detected by forge.
 * 
 * @author sbenoist
 * @author lamirang
 */
public interface PluginService
{

  /**
   * This field defines the name of service property ID
   */
  String ID_PROPERTY = "ID";

  /**
   * This method allows to add uuid to {@link PluginService} and will return it
   * 
   * @return uuid object
   */
  UUID getServiceUUID();

  /**
   * This method allows to get plugin service metadata
   * 
   * @return PluginServiceMetadata object
   * @throws PluginServiceException
   */
  PluginServiceMetadata getMetadata() throws PluginServiceException;

  /**
   * This method allows to get a image which should describe the plugin type
   * 
   * @return bytes array representing the icon or null
   */
  byte[] getPluginIcon();

  /**
   * This method allows to get plugin communication information
   * 
   * @return {@link org.novaforge.forge.core.plugins.domain.route.PluginAssociation}
   * @throws PluginServiceException
   *           used if errors occured
   */
  PluginAssociation getAssociationInfo() throws PluginServiceException;

  /**
   * This method allows to get a set of roles used by the tool behind plugin
   * 
   * @return set of tool roles
   * @throws PluginServiceException
   *           used if errors occured
   */
  Set<String> findRoles() throws PluginServiceException;

  /**
   * This method is used in order to send some parameter to the plugin.
   * 
   * @param pParameter
   *          map which contains parameters
   * @see org.novaforge.forge.core.plugins.domain.plugin.PluginParameter
   * @throws PluginServiceException
   *           used if a parameter is missing
   */
  void initialize(Map<String, String> pParameter) throws PluginServiceException;

  /**
   * This method is used in order to get an existing roles mapping for an instance ID given.
   * 
   * @param pInstanceId
   *          used to find the mapping
   * @return map which contains <key=forge role, value=plugins role>
   * @throws PluginServiceException
   */
  Map<String, String> getRolesMapping(final String pInstanceId) throws PluginServiceException;

  /**
   * This method allows to reactivate a plugin after a breakdown
   * 
   * @throws PluginServiceException
   */
  void reactivate() throws PluginServiceException;

  /**
   * This method allows to desactivate a plugin for a scheduled breakdown
   * 
   * @param pSuperAdmins
   *          the superadmins logins allowed to access to the plugin during desactivation
   * @throws PluginServiceException
   */
  void desactivate(Map<String, String> pSuperAdmins) throws PluginServiceException;

  /**
   * This method allows to stop a plugin
   * 
   * @throws PluginServiceException
   */
  void stop() throws PluginServiceException;

  /**
   * Will enable or disable plugin's route
   * 
   * @param pStates
   *          <code>true</code> to enable, false otherwise
   * @throws PluginServiceException
   */
  void enableRoute(boolean pStates) throws PluginServiceException;

  /**
   * <p>
   * This method returns the unique uri of a web-service to be called for the plugin data propagation.
   * </p>
   * <p>
   * The service exposed through this uri must be of the type PluginDataService.
   * 
   * @return a possibly null String. If null (default implementation in the AbstractPluginService, that means
   *         no propagation service is available.
   */
  String getPluginDataServiceUri();

  /**
   * This method returns the service responsible for provisioning tool instances
   * 
   * @return PluginToolProvisioningService
   */
  ToolInstanceProvisioningService getToolInstanceProvisioningService();

  /**
   * This method will return the URI to access to the view requested
   * 
   * @param pToolInstance
   *          the tool instance used to build acccess alias
   * @param pView
   *          the view requested
   * @return URI to access to the requested view
   * @throws PluginServiceException
   *           thrown if any errors occured when building acces URI
   */
  URI getAccessURI(final ToolInstance pToolInstance, final PluginViewEnum pView)
      throws PluginServiceException;

  /**
   * This method returns the number of max allowed project instances for the plugin
   * 
   * @return -1 if no max is defined or int value for max instances
   */
  int getMaxAllowedProjectInstances();

}
