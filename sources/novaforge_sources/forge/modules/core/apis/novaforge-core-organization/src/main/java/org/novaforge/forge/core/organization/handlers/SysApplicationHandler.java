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
import org.novaforge.forge.core.organization.model.Project;

import java.util.Set;

/**
 * @author sbenoist
 */
public interface SysApplicationHandler
{
  /**
   * This method allows to handle SYSTEM applications
   * 
   * @param pProject
   * @throws ApplicationServiceException
   */
  void handleSysApplications(Project pProject) throws ApplicationServiceException;

  /**
   * This method allows to create SYSTEM application for a project only if the plugin is SYSTEM realm
   * 
   * @param pProject
   * @param pPluginUUID
   * @return application uri
   * @throws ApplicationServiceException
   */
  String createSysApplication(Project pProject, String pPluginUUID) throws ApplicationServiceException;

  /**
   * This method build for a project set of permissions to acces to its system applications
   * 
   * @param pProjectId
   * @return set of permissions
   * @throws ApplicationServiceException
   */
  Set<String> buildSysApplicationPermissions(final String pProjectId) throws ApplicationServiceException;

  /**
   * This method update roles mapping for system applicaations of a project adding the new role given
   * 
   * @param pProjectId
   *          the project id concerned
   * @param pRoleName
   *          the new role to add
   * @throws ApplicationServiceException
   */
  void addRoleToSysApplicationsMapping(String pProjectId, String pRoleName)
      throws ApplicationServiceException;
}
