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
package org.novaforge.forge.dashboard.service;

import org.novaforge.forge.dashboard.model.DataSourceOptions;

import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public interface DataSourceFactory
{

  /**
   * Will build a {@link DataSourceOptions} from parameters.
   * 
   * @param pNeedsProject
   *          true if widget needs at least one project, false otherwise
   * @param pIsMultiProjects
   *          true if widget needs more than one project, false otherwise
   * @param pNeedsApplication
   *          true if widget needs at least one application, false otherwise
   * @param pIsMultiApplications
   *          true if widget needs more than one application, false otherwise
   * @return {@link DataSourceOptions}
   */
  DataSourceOptions buildOptions(final boolean pNeedsProject, final boolean pIsMultiProjects,
      final boolean pNeedsApplication, final boolean pIsMultiApplications);

  /**
   * Will build a string value representing the datasource sets
   * 
   * @param pDataSource
   *          contains list of applications by projects
   * @return {@link String} value representing the datasource sets
   */
  String buildDataSource(final Map<String, List<String>> pDataSource);

  /**
   * Build a map contains applications by project from a string value
   * 
   * @param pDataSource
   *          contains list of applications by projects as {@link String}
   * @return a map contains applications by project
   */
  Map<String, List<String>> readDataSource(final String pDataSource);
}
