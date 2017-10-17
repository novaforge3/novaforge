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
package org.novaforge.forge.core.plugins.categories;

import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;

import java.util.Locale;

/**
 * Each plugin will have a set of functional services specific to it's category. As a consequence each plugin
 * category will be able to have it's FunctionalService which will extends the PluginFunctionalService. By
 * this way each service will have at least the services defined in PluginFunctionalService.
 * 
 * @author rols-p
 */
public interface PluginCategoryService
{

  /**
   * This field defines the name of service property ID
   */
  String ID_PROPERTY = "ID";

  /**
   * Returns a String containing enough information in order to be able to connect to the tool. This
   * information is likely to be used from outside the scope of the Forge (for example in an IDE)
   * 
   * @param instanceId
   *          id of the application instance
   * @param locale
   * @return
   * @throws PluginServiceException
   */
  String getApplicationAccessInfo(String instanceId, Locale locale) throws PluginServiceException;
}
