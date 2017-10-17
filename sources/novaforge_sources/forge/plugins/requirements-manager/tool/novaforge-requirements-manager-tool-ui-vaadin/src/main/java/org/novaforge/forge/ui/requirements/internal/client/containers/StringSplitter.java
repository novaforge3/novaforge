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

package org.novaforge.forge.ui.requirements.internal.client.containers;

import org.apache.commons.lang.StringUtils;

/**
 * Util class for String Splitting.
 * 
 * @author B-Martinelli
 */
public class StringSplitter
{
  /** Char used to cut description */
  private static final String cutterSign = " (..) ";

  /**
   * Cut a string to keep it first and last part and put a cutter sign "(..)"
   * 
   * @param stringToCut
   *          the string to cut
   * @return the string cut
   */
  public static String stringCutter(String stringToCut, int viewableSize)
  {
    StringBuilder sb = new StringBuilder();
    if (!StringUtils.isBlank(stringToCut) && stringToCut.length() > viewableSize)
    {
      // Cutting description, keeping the first partand the last part
      String firstPart = stringToCut.substring(0, (viewableSize / 2) - (cutterSign.length()/2));
      String secondPart = stringToCut.substring(stringToCut.length() - 1 - (viewableSize / 2) + (cutterSign.length()/2),
          stringToCut.length());

      sb.append(firstPart).append(cutterSign).append(secondPart);
    }
    else
    {
      if (!StringUtils.isBlank(stringToCut) && stringToCut.length() <= viewableSize)
      {
        sb.append(stringToCut);
      }
      else
      {
        sb.append("");
      }
    }
    return sb.toString();
  }
}
