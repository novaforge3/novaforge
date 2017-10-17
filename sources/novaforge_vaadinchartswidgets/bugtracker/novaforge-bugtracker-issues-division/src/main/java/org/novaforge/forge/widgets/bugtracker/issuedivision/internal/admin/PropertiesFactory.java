/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.widgets.bugtracker.issuedivision.internal.admin;

import net.sf.json.JSONObject;

import com.google.common.base.Strings;


/**
 * @author Gauthier Cart
 */
public class PropertiesFactory
{
  /**
   * Key value for properties
   */
  private static final String VERSION = "VERSION";

  /**
   * Read properties string and return concerned version.( any (<code>null</code>) by default)
   * 
   * @param pProperties
   *          the string properties
   * @return concerned version or any (<code>null</code>) by default
   */
  public static String readProperties(final String pProperties)
  {
    String version = null;
    if (Strings.isNullOrEmpty(pProperties) == false)
    {
      final JSONObject json = JSONObject.fromObject(pProperties);
      version = json.optString(VERSION, null);
    }
    return version;

  }

  /**
   * Build properties string from argument
   * 
   * @param pVersions
   *          the list of versions defined
   * @return properties string
   */
  public static String buildProperties(final String pVersion)
  {
    final JSONObject json = new JSONObject();
    json.element(VERSION, pVersion);
    return json.toString();

  }

}
