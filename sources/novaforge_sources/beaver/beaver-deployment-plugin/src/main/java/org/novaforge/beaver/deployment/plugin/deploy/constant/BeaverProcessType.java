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
package org.novaforge.beaver.deployment.plugin.deploy.constant;

/**
 * This enum contains process type handled
 * 
 * @author Guillaume Lamirand
 */
public enum BeaverProcessType
{
  /**
   * Contants for requisite property
   */
  INSTALL("install"),
  /**
   * Contants for product id property
   */
  UPDATE("update"),
  /**
   * Contants for process property
   */
  DELETE("delete");

  /**
   * Contains the process type value
   */
  private final String processValue;

  /**
   * Private default constructor
   * 
   * @param pValue
   *          the property key associated
   */
  private BeaverProcessType(final String pValue)
  {
    processValue = pValue;

  }

  /**
   * Returns the property key associated
   * 
   * @return the propertyKey
   */
  public String getPropertyKey()
  {
    return processValue;
  }

  /**
   * Check if the other process value in parameter equals to current process value
   * 
   * @param pOtherValue
   *          an other process value
   * @return <code>true</code> if value given equals current process value
   */
  public boolean equalsValue(final String pOtherValue)
  {
    return processValue.equals(pOtherValue);
  }

  /**
   * Find the {@link BeaverProcessType} from process value
   * 
   * @param pProcess
   *          the process value
   * @return {@link BeaverProcessType} found or <code>null</code> if unexisting
   */
  public static BeaverProcessType get(final String pProcess)
  {
    BeaverProcessType returnValue = null;
    for (final BeaverProcessType beaverProcessType : BeaverProcessType.values())
    {
      if (beaverProcessType.getPropertyKey().equals(pProcess))
      {
        returnValue = beaverProcessType;
        break;
      }

    }
    return returnValue;
  }

}
