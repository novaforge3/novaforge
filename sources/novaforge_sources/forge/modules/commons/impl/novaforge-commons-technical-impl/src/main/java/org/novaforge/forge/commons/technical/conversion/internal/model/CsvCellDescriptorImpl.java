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
package org.novaforge.forge.commons.technical.conversion.internal.model;

import org.novaforge.forge.commons.technical.conversion.model.CsvCellDescriptor;

/**
 * @author sbenoist
 * @date Mar 9, 2010
 */
public class CsvCellDescriptorImpl implements CsvCellDescriptor
{
  private String  name;

  private String  bindName;

  private String  format;

  private boolean mandatory;

  public CsvCellDescriptorImpl(final String pName, final String pBindName)
  {
    this(pName, pBindName, false, null);
  }

  public CsvCellDescriptorImpl(final String pName, final String pBindName, final boolean pMandatory,
      final String pFormat)
  {
    super();
    name = pName;
    format = pFormat;
    mandatory = pMandatory;
    bindName = pBindName;
  }

  public CsvCellDescriptorImpl(final String pName, final String pBindName, final boolean pMandatory)
  {
    this(pName, pBindName, pMandatory, null);
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
  public void setName(final String pName)
  {
    name = pName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getBindName()
  {
    return bindName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBindName(final String pFieldName)
  {
    bindName = pFieldName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFormat()
  {
    return format;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFormat(final String pFormat)
  {
    format = pFormat;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isMandatory()
  {
    return mandatory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMandatory(final boolean pMandatory)
  {
    mandatory = pMandatory;
  }
}
