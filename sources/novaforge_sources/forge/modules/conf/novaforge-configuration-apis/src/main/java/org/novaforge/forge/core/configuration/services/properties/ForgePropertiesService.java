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
package org.novaforge.forge.core.configuration.services.properties;

import org.novaforge.forge.core.configuration.exceptions.ForgeConfigurationException;

import java.math.BigInteger;
import java.util.List;

/**
 * This interface describes service which handle novaforge properties
 * 
 * @author Guillaume Lamirand
 */
public interface ForgePropertiesService
{

  /**
   * Get a boolean associated with the given configuration key.
   * 
   * @param pKey
   *          The property key.
   * @return The associated boolean.
   */
  boolean getPropertyAsBoolean(final String pKey);

  /**
   * Get a byte associated with the given property key.
   * 
   * @param pKey
   *          The property key.
   * @return The associated byte.
   */
  byte getPropertyAsByte(final String pKey);

  /**
   * Get a double associated with the given configuration key.
   * 
   * @param pKey
   *          The property key.
   * @return The associated {@link Double}.
   */
  double getPropertyAsDouble(final String pKey);

  /**
   * Get a float associated with the given configuration key.
   * 
   * @param pKey
   *          The property key.
   * @return The associated {@link Float}.
   */
  float getPropertyAsFloat(final String pKey);

  /**
   * Get a int associated with the given configuration key.
   * 
   * @param pKey
   *          The property key.
   * @return The associated {@link Integer}.
   */
  int getPropertyAsInt(final String pKey);

  /**
   * Get a long associated with the given configuration key.
   * 
   * @param pKey
   *          The property key.
   * @return The associated {@link Long}.
   */
  long getPropertyAsLong(final String pKey);

  /**
   * Get a bigInteger associated with the given configuration key.
   * 
   * @param pKey
   *          The property key.
   * @return The associated {@link BigInteger}.
   */
  BigInteger getPropertyAsBigInteger(final String pKey);

  /**
   * Get a string associated with the given configuration key.
   * 
   * @param pKey
   *          The property key.
   * @return The associated {@link String}.
   */
  String getPropertyAsString(final String pKey);

  /**
   * Get a list associated with the given configuration key.
   * 
   * @param pKey
   *          The property key.
   * @return The associated {@link List}.
   */
  List<Object> getList(final String pKey);

  /**
   * This method should be used to refresh forge configuration
   * 
   * @throws ForgeConfigurationException
   *           this can be thrown if refresh failed
   */
  void refresh() throws ForgeConfigurationException;

  /**
   * Check if the configuration requires to be reloaded
   * 
   * @return <code>true</code> if configuration has changed and needs to be reloaded
   */
  boolean reloadingRequired();

  /**
   * Sets a new value for the specified property.
   * 
   * @param pKey
   *          the key of the affected property
   * @param pValue
   *          the value
   */
  void setProperty(final String pKey, final Object pValue);

}