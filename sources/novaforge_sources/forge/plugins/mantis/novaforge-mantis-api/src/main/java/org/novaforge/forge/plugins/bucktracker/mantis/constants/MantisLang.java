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
package org.novaforge.forge.plugins.bucktracker.mantis.constants;

import java.util.Locale;

/**
 * This enumeration declares default languages which are used in Mantis
 * 
 * @author Gauthier Cart
 */
public enum MantisLang
{
  /**
   * Represents English language
   */
  ENGLISH
  {
    @Override
    public String getLang()
    {
      return "english";
    }

    @Override
    public Locale getLocale()
    {
      return Locale.ENGLISH;
    }

  },

  /**
   * Represents French language
   */
  FRENCH
  {
    @Override
    public String getLang()
    {
      return "french";
    }

    @Override
    public Locale getLocale()
    {
      return Locale.FRENCH;
    }
  };

  /**
   * @param pLocale
   *          a Locale
   * @return a label of a language in Mantis
   */
  public static MantisLang getMantisLang(final Locale pLocale)
  {

    MantisLang result = MantisLang.ENGLISH;

    MantisLang[] values = MantisLang.values();
    if (pLocale != null)
    {
      for (MantisLang mantisLang : values)
      {
        if (pLocale.equals(mantisLang.getLocale()))
        {
          result = mantisLang;
          break;
        }
      }
    }
    return result;
  }

  /**
   * Return a Locale
   *
   * @return a Locale
   */
  public abstract Locale getLocale();

  /**
   * Return the label of a language in Mantis
   *
   * @return label of a language
   */
  public abstract String getLang();
}
