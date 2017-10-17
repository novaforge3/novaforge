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
package org.novaforge.forge.plugins.requirements.requirementmanager.services;

import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.requirements.requirementmanager.exception.RequirementManagerException;

/**
 * @author Guillaume Lamirand
 */
public interface RequirementConfigurationService extends PluginConfigurationService
{
  /**
   * Method returning the path to data folder for Requirements.
   *
   * @return the path to data folder for Requirements
   */
  String getDataPath();

  /**
   * Method returning the RegExp of the Code Parser for Requirements.
   *
   * @return the RegExp
   */
  String getParserRegexp();

  /**
   * Method returning the FileExtensions of the Code Parser for Requirements.
   *
   * @return the FileExtensions
   * @throws RequirementManagerException
   */
  String[] getFileExtensionsArray() throws RequirementManagerException;

  /**
   * Method returning the max size of an excel file.
   *
   * @return the max size of an excel file
   */
  int getExcelFileMaxSize();

  /**
   * Method returning the type of the code handler to use
   * @return The type of the code handler to use
   */
  String getCodeHandlerType();

}
