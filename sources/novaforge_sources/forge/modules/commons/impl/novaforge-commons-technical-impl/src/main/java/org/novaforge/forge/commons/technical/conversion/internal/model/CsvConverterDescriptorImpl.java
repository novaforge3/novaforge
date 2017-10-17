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
import org.novaforge.forge.commons.technical.conversion.model.CsvConverterDescriptor;

import java.util.Collections;
import java.util.List;

/**
 * @author sbenoist
 * @date Mar 10, 2010
 */
public class CsvConverterDescriptorImpl implements CsvConverterDescriptor
{

  private boolean                 requiredHeader;

  private List<CsvCellDescriptor> cells;

  private char                    delimiter;

  public CsvConverterDescriptorImpl(final List<CsvCellDescriptor> pCells)
  {
    this(pCells, false, (char) 0);
  }

  public CsvConverterDescriptorImpl(final List<CsvCellDescriptor> pCells, final boolean pRequiredHeader,
      final char pDelimiter)
  {
    super();
    requiredHeader = pRequiredHeader;
    cells = pCells;
    delimiter = pDelimiter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRequiredHeader()
  {
    return requiredHeader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRequiredHeader(final boolean pRequiredHeader)
  {
    requiredHeader = pRequiredHeader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CsvCellDescriptor> getCells()
  {
    return Collections.unmodifiableList(cells);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCells(final List<CsvCellDescriptor> pCells)
  {
    cells = pCells;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public char getDelimiter()
  {
    return delimiter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDelimiter(final char pDelimiter)
  {
    delimiter = pDelimiter;
  }

}
