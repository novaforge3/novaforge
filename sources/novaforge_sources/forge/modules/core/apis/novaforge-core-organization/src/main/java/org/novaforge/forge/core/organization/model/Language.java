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
/**
 * 
 */
package org.novaforge.forge.core.organization.model;

import org.novaforge.forge.commons.technical.historization.annotations.Historizable;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author BILET-JC
 * @author Guillaume Lamirand
 */
public interface Language extends Serializable
{

  /**
   * Returns the name of language. Should be build according a {@link Locale}
   * 
   * @return the name
   */
  @Historizable(label = "name")
  String getName();

  /**
   * Sets a name
   * 
   * @param pName
   *          the name to set
   */
  void setName(final String pName);

  /**
   * Returns the {@link Locale} associated to this language.{@link Locale#ENGLISH} by default
   * 
   * @return {@link Locale} retrieved or {@link Locale#ENGLISH} by default
   */
  Locale getLocale();

  /**
   * Return true if the current language is the default language, return false otherwise
   * 
   * @return a {@link boolean} retrived
   */
  boolean IsDefault();

  /**
   * Sets if the language is the default one
   * 
   * @param pIsDefault
   *          boolean to define if the language is default one
   */
  void setIsDefault(boolean pIsDefault);

}
