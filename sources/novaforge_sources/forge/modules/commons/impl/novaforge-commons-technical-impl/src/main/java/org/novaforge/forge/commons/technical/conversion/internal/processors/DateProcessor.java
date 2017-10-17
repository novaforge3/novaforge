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
package org.novaforge.forge.commons.technical.conversion.internal.processors;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.util.CsvContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sbenoist
 * @date Mar 11, 2010
 */
public class DateProcessor extends CellProcessorAdaptor
{
  private static final String DEFAULT_FORMAT = "yyyy.MM.dd.HH.mm.ss";
  private final DateFormat    formatter;

  public DateProcessor() throws Exception
  {
    this(DEFAULT_FORMAT);
  }

  public DateProcessor(final String pFormat) throws Exception
  {
    super();
    formatter = new SimpleDateFormat(pFormat);
  }

  public DateProcessor(final CellProcessor pNext) throws Exception
  {
    this(DEFAULT_FORMAT, pNext);
  }

  public DateProcessor(final String pFormat, final CellProcessor pNext) throws Exception
  {
    super(pNext);
    formatter = new SimpleDateFormat(pFormat);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(final Object pValue, final CsvContext pContext)
  {
    if (pValue == null)
    {
      return null;
    }

    if ((!(pValue instanceof Date)) && (!(pValue instanceof String)))
    {
      throw new SuperCsvConstraintViolationException("the value '" + pValue
          + "' is not of required String or Date types", pContext, this);
    }

    Object result = null;
    try
    {
      if (pValue instanceof Date)
      {
        result = formatter.format((Date) pValue);
      }
      else
      {
        final String strValue = (String) pValue;
        if (strValue.trim().length() > 0)
        {
          result = formatter.parse(strValue);
        }
      }
    }
    catch (final Exception e)
    {
      throw new SuperCsvConstraintViolationException("an error occured during Date conversion", pContext,
          this);
    }

    return next.execute(result, pContext);
  }
}
