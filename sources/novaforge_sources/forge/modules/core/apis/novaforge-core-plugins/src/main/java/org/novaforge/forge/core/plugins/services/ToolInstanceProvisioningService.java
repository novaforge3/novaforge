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

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.ToolInstanceProvisioningException;

import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface ToolInstanceProvisioningService
{

  /**
   * This method returns the instance by name. if the instance doesn't exists, it returns null
   * 
   * @param pName
   * @return ToolInstance
   * @throws ToolInstanceProvisioningException
   */
  ToolInstance getToolInstanceByName(String pName) throws ToolInstanceProvisioningException;

  /**
   * This method returns the instance by uuid. if the instance doesn't exists, it returns null
   * 
   * @param pUUID
   * @return {@link ToolInstance}
   * @throws ToolInstanceProvisioningException
   */
  ToolInstance getToolInstanceByUUID(UUID pUUID) throws ToolInstanceProvisioningException;

  /**
   * This method returns the instance by host. if the instance doesn't exists, it returns null
   * 
   * @param pUUID
   * @return {@link ToolInstance}
   * @throws ToolInstanceProvisioningException
   */
  ToolInstance getToolInstanceByHost(String pHost) throws ToolInstanceProvisioningException;

  /**
   * This method allows to add a tool instance
   * 
   * @param pToolInstance
   * @return {@link ToolInstance}
   * @throws ToolInstanceProvisioningException
   */
  ToolInstance addToolInstance(ToolInstance pToolInstance) throws ToolInstanceProvisioningException;

  /**
   * This method allows to remove a tool instance
   * 
   * @param pToolInstance
   * @throws ToolInstanceProvisioningException
   */
  void removeToolInstance(final ToolInstance pToolInstance) throws ToolInstanceProvisioningException;

  /**
   * This method allows to update a tool instance
   * 
   * @param pToolInstance
   *          contains the new tool instance
   * @param pPreviousName
   *          contains the previous name, if changed
   * @return {@link ToolInstance}
   * @throws ToolInstanceProvisioningException
   */
  ToolInstance updateToolInstance(final ToolInstance pToolInstance, final String pPreviousName)
      throws ToolInstanceProvisioningException;

  /**
   * This method return all the tool instances
   * 
   * @return a {@link Set} of {@link ToolInstance}
   * @throws ToolInstanceProvisioningException
   */
  Set<ToolInstance> getAllToolInstances() throws ToolInstanceProvisioningException;

  /**
   * This method return all the available tool instances
   * 
   * @return a {@link Set} of {@link ToolInstance}
   * @throws ToolInstanceProvisioningException
   */
  Set<ToolInstance> getAvailableToolInstances() throws ToolInstanceProvisioningException;

  /**
   * This method returns true if the plugin has at least one available tool instance
   * 
   * @return boolean
   * @throws ToolInstanceProvisioningException
   */
  boolean hasAvailableToolInstance() throws ToolInstanceProvisioningException;

  /**
   * This method returns the tool instance hosting the application
   * 
   * @param pApplicationInstanceID
   * @return {@link ToolInstance}
   * @throws ToolInstanceProvisioningException
   */
  ToolInstance getToolInstanceByApplication(String pApplicationInstanceID)
      throws ToolInstanceProvisioningException;

  /**
   * @return true if the tool instances can be provided on demand
   */
  boolean isProvisionable();

  /**
   * This method returns a tool instance
   * 
   * @return {@link ToolInstance}
   */
  ToolInstance newToolInstance();

  /**
   * This method returns the number of applications by tool instance
   * 
   * @param pUUID
   *          the tool instance uuid
   * @return the number of applications hosted by the tool instance
   * @throws ToolInstanceProvisioningException
   */
  long countApplications(final UUID pUUID) throws ToolInstanceProvisioningException;

  /**
   * This methos returns all the applications hosted by a tool instance argued by its UUID
   * 
   * @param pUUID
   * @return Set<InstanceConfiguration>
   * @throws ToolInstanceProvisioningException
   */
  Set<InstanceConfiguration> getApplicationsByUUID(final UUID pUUID) throws ToolInstanceProvisioningException;

}
