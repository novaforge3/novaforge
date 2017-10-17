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
package org.novaforge.forge.plugins.bucktracker.mantisstd.services;

import java.util.Locale;

/**
 * Filter for Mantis issues by :
 * * Category
 * * Fixed in version
 * * Version
 * * User locale
 * 
 * @author Gauthier Cart
 */
public interface MantisGetIssuesFilter
{
  /**
   * Get the category to filter
   * 
   * @return the category to filter
   */
  String getCategory();

  /**
   * Set the category to filter
   * 
   * @param pCategory
   *          the category to filter
   */
  void setCategory(String pCategory);

  /**
   * Get the fixed in version to filter
   * 
   * @return the fixed in version to filter
   */
  String getFixedInVersion();

  /**
   * Set the fixed in version to filter
   * 
   * @param pFixedInVersion
   *          the fixed in version to filter
   */
  void setFixedInVersion(String pFixedInVersion);

  /**
   * Get the product version to filter
   * 
   * @return the product version to filter
   */
  String getProductVersion();

  /**
   * Set the product version to filter
   * 
   * @param pProductVersion
   *          the product version to filter
   */
  void setProductVersion(String pProductVersion);

  /**
   * Get user locale
   * 
   * @return the user locale
   */
  Locale getUserLocale();

  /**
   * Set user locale
   * 
   * @param pUserLocale
   *          the user locale
   */
  void setUserLocale(Locale pUserLocale);
}
