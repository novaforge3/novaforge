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

import org.novaforge.forge.core.plugins.domain.plugin.PluginInstance;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProjectInformation;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;

/**
 * @author lamirang
 */
public interface PluginProjectService
{

  /**
   * This method allows to create project into plugin
   * 
   * @param pPluginInstance
   *          represents information about plugin instance
   * @param pPluginProjectInformation
   *          represents information about project, roles mapping and memberships
   * @throws PluginServiceException
   *           used when errors occured
   */
  void createProject(final PluginInstance pPluginInstance,
      final PluginProjectInformation pPluginProjectInformation) throws PluginServiceException;

  /**
   * This method allows to update project into plugin
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @param pPluginProject
   *          represents the project to be updated
   * @throws PluginServiceException
   *           used if errors occured
   */
  void updateProject(final String pInstanceId, final PluginProject pPluginProject)
      throws PluginServiceException;

  /**
   * This method allows to delete project into plugin and tool
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @throws PluginServiceException
   *           used if errors occured
   */
  void deleteProject(final String pInstanceId) throws PluginServiceException;

  /**
   * This method allows to archive project information
   * 
   * @param pInstanceId
   *          represents the project instance id
   * @throws PluginServiceException
   *           used if errors occured
   */
  void archiveProject(final String pInstanceId) throws PluginServiceException;
}
