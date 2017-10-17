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
package org.novaforge.forge.plugins.categories.impl.models;

import org.novaforge.forge.core.plugins.categories.FieldDescription;

/**
 * @author Guillaume Lamirand
 */
public class FieldDescriptionImpl implements FieldDescription
{
  private final String   name;
  private final String   desc;
  private final boolean  isRequired;
  private final String   exampleValue;
  private final boolean  isID;
  private final String   fieldName;
  private final Class<?> type;
  private final int      size;

  public FieldDescriptionImpl(final String pFieldName)
  {
    this(pFieldName, pFieldName, pFieldName, false, null, false, null, 0);
  }

  public FieldDescriptionImpl(final String pFieldName, final String pName, final String pDescription,
      final boolean pIsRequired, final String pExampleValue, final boolean pIsID, final Class<?> pType,
      final int pSize)
  {
    fieldName = pFieldName;
    name = pName;
    desc = pDescription;
    isRequired = pIsRequired;
    exampleValue = pExampleValue;
    isID = pIsID;
    type = pType;
    size = pSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription()
  {
    return desc;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRequired()
  {
    return isRequired;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getExampleValue()
  {
    return exampleValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isID()
  {
    return isID;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getClassFieldName()
  {
    return fieldName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getType()
  {
    return type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSize()
  {
    return size;
  }
}
