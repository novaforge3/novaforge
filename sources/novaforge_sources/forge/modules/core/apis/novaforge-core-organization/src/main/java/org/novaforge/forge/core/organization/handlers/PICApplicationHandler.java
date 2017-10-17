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
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.ProjectApplication;

import java.util.Map;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface PICApplicationHandler
{
  /**
   * This method allows to add a PIC application to a project
   * 
   * @param pProjectId
   * @param pParentNodeUri
   * @param pApplicationLabel
   * @param pDescription
   * @param pluginUUID
   * @param pRolesMapping
   * @param pUsername
   * @return {@link ProjectApplication} created
   * @throws ApplicationServiceException
   */
  ProjectApplication addApplication(String pProjectId, String pParentNodeUri, String pApplicationLabel,
      String pDescription, UUID pluginUUID, Map<String, String> pRolesMapping, String pUsername)
      throws ApplicationServiceException;

  /**
   * @param pPluginUUID
   * @return true if the plugin type is PIC, false elsewhere
   */
  boolean isPICType(UUID pPluginUUID) throws ApplicationServiceException;

  /**
   * This method returns the UUID of the other PIC tool instance linked to the argued tool instance UUID
   * 
   * @param pPluginUUID
   * @param pToolInstanceUUID
   * @param pTargetPluginUUID
   * @return
   * @throws ProjectServiceException
   */
  UUID getOtherLinkedPICToolInstanceUUID(UUID pPluginUUID, UUID pToolInstanceUUID, UUID pTargetPluginUUID)
      throws ApplicationServiceException;

  /**
   * This method returns the UUID of the tool instance which hosts the PIC application
   * 
   * @param pTargetUUID
   *          the id of the plugin target
   * @param pPluginUUID
   *          the id of the plugin
   * @param pInstanceUUID
   *          the instance of the plugin
   * @return the UUID id of the tool instance
   * @throws ProjectServiceException
   */
  UUID getPICToolInstanceUUID(UUID pTargetUUID, UUID pPluginUUID, UUID pInstanceUUID)
      throws ApplicationServiceException;

  /**
   * This method returns true if a tool instance can host an application for a project given
   * 
   * @param pPluginUUID
   * @param pToolInstanceUUID
   * @param pProjectId
   * @return
   * @throws ProjectServiceException
   */
  boolean canHostPICApplicationForProject(UUID pPluginUUID, UUID pToolInstanceUUID, String pProjectId)
      throws ApplicationServiceException;

  /**
   * This method allows to know if the PIC is partitioned
   * 
   * @return true if the PIC is partitioned
   * @throws ProjectServiceException
   */
  boolean isPICPartitioned() throws ApplicationServiceException;
}
