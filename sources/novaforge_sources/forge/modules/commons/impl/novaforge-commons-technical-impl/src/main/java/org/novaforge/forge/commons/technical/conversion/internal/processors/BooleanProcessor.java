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

/**
 * @author sbenoist
 * @date Mar 11, 2010
 */
public class BooleanProcessor extends CellProcessorAdaptor
{
  private static final String[] DEFAULT_TRUE_VALUES  = new String[] { "1", "true", "t", "y" };

  private static final String[] DEFAULT_FALSE_VALUES = new String[] { "0", "false", "f", "n" };

  public BooleanProcessor()
  {
    super();
  }

  public BooleanProcessor(final CellProcessor pNext)
  {
    super(pNext);
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

    if ((!(pValue instanceof Boolean)) && (!(pValue instanceof String)))
    {
      throw new SuperCsvConstraintViolationException("the value '" + pValue
          + "' is not of required String or Boolean types", pContext, this);
    }

    Object result;

    if (pValue instanceof Boolean) // converts from Boolean to String
    {
      result = Boolean.toString((Boolean) pValue);
    }
    else
    // converts to boolean
    {
      final String strValue = ((String) pValue).toLowerCase();
      if (isFalseValue(strValue))
      {
        result = Boolean.FALSE;
      }
      else if (isTrueValue(strValue))
      {
        result = Boolean.TRUE;
      }
      else
      {
        throw new SuperCsvConstraintViolationException("Cannot parse \"" + pValue
            + "\" to a boolean on line " + pContext.getLineNumber() + " column " + pContext.getColumnNumber()
            + 1, pContext, this);
      }
    }

    return next.execute(result, pContext);
  }

  private boolean isFalseValue(final String sval)
  {
    return indexOf(sval, DEFAULT_FALSE_VALUES) >= 0;
  }

  private boolean isTrueValue(final String sval)
  {
    return indexOf(sval, DEFAULT_TRUE_VALUES) >= 0;
  }

  private static int indexOf(final String sval, final String[] possibleMatches)
  {
    if (possibleMatches == null)
    {
      return -1;
    }
    for (int i = 0; i < possibleMatches.length; i++)
    {
      if (sval.equals(possibleMatches[i]))
      {
        return i;
      }
    }
    return -1;
  }

}
