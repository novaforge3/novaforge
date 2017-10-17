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
package org.novaforge.forge.widgets.scm.lastcommit.internal.admin;

import com.google.common.base.Strings;
import net.sf.json.JSONObject;

/**
 * @author Guillaume Lamirand
 */
public class PropertiesFactory
{
  public static final int DEFAULT_NUMBER_OF_COMMIT = 5;
  /**
   * Key value for properties
   */
  private static final String COMMITS = "commits";

  /**
   * Read properties string and return number of commits.( 5 by default)
   * 
   * @param pProperties
   *          the string properties
   * @return number of commits read or 5 by default
   */
  public static int readProperties(final String pProperties)
  {
    int number = DEFAULT_NUMBER_OF_COMMIT;
    if (!Strings.isNullOrEmpty(pProperties))
    {
      final JSONObject json = JSONObject.fromObject(pProperties);
      number = json.getInt(COMMITS);
    }
    return number;

  }

  /**
   * Build properties string from argument
   * 
   * @param pCommits
   *          the number of commit defined
   * @return properties string
   */
  public static String buildProperties(final int pCommits)
  {
    final JSONObject json = new JSONObject();
    json.element(COMMITS, pCommits);
    return json.toString();

  }
}
