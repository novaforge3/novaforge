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

import java.util.List;
import java.util.Locale;

/**
 * This interface defines a service that should be implemeted by each category in order to be add to core
 * registry.
 * 
 * @author Guillaume Lamirand
 */
public interface CategoryDefinitionService
{
  String CATEGORY_KEY = "category";

  /**
   * @return category
   */
  Category getCategory();

  /**
   * @return concret interface of {@link PluginCategorYService}
   */
  Class<? extends PluginCategoryService> getCategoryServiceInterface();

  /**
   * Return the category's name according to a local
   * 
   * @param pLocale
   *          represensts the locale used to get name
   * @return category's name
   */
  String getName(final Locale pLocale);

  /**
   * @return a list of {@link Association} available for the category
   */
  List<Association> getAssociations();

  /**
   * @param pType
   *          represents the type of the assocation you are looking for
   * @param pName
   *          represents the name of the assocation you are looking for
   * @return a {@link Association} object according to the name given, if no association are found null is
   *         returned
   */
  Association getAssociation(final AssociationType pType, final String pName);

  /**
   * @param pAssociation
   *          represents the association source
   * @return a list of {@link Association} compatible with the association given
   */
  List<Association> getCompatibleAssociations(final Association pAssociation);

  /**
   * @param pAssociation
   *          represents an association
   * @param pLocale
   *          represensts the locale used to get label and description of the field
   * @return a {@link Parameter} according to the association and locale given
   */
  Parameter getParamater(final Association pAssociation, final Locale pLocale);

  /**
   * Returns the realm of the plugin category. It can be either "System" or "User" according to whom can
   * create
   * application linked to the plugin category
   * 
   * @return PluginRealm
   * @throws PluginServiceException
   */
  PluginRealm getRealm();

  /**
   * Returns the default role used for this plugin category in case of SYSTEM handling
   * 
   * @return String
   * @throws PluginServiceException
   */
  String getDefaultRole() throws PluginServiceException;

}
