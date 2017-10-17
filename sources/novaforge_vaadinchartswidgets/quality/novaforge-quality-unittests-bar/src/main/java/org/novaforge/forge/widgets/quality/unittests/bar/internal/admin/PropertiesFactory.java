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
package org.novaforge.forge.widgets.quality.unittests.bar.internal.admin;

import net.sf.json.JSONObject;

import org.novaforge.forge.widgets.quality.admin.QualityResource;

import com.google.common.base.Strings;

/**
 * @author Gauthier Cart
 */
public class PropertiesFactory extends org.novaforge.forge.widgets.quality.admin.PropertiesFactory
{
  /**
   * Key value for properties, the number of build to print
   */
  private static final String NUMBER_OF_BUILD         = "NUMBER_OF_BUILD";

  public static final int     DEFAULT_NUMBER_OF_BUILD = 10;

  /**
   * Build properties string from argument
   * 
   * @param pResource
   *          resource selected
   * @return properties string
   */
  public static JSONObject buildProperties(final QualityResource pResource, final int pNumberOfBuild)
  {
    final JSONObject json = org.novaforge.forge.widgets.quality.admin.PropertiesFactory
        .buildProperties(pResource);
    json.element(NUMBER_OF_BUILD, pNumberOfBuild);
    return json;
  }

  /**
   * Read properties string and return concerned number of build.
   * 
   * @param pProperties
   *          the string properties
   * @return concerned number of build or any (<code>null</code>) by default
   */
  public static Integer readPropertiesNumberOfBuild(final String pProperties)
  {

    Integer numberOfBuild = DEFAULT_NUMBER_OF_BUILD;

    if (Strings.isNullOrEmpty(pProperties) == false)
    {
      final JSONObject json = JSONObject.fromObject(pProperties);
      numberOfBuild = json.optInt(NUMBER_OF_BUILD, DEFAULT_NUMBER_OF_BUILD);
    }

    return numberOfBuild;
  }
}
