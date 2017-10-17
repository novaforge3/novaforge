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
package org.novaforge.beaver.context;

/**
 * Defined different kind of server
 * 
 * @author Guillaume Lamirand
 */
public enum ServerType
{
  /**
   * Use for the main server, has to be unique
   */
  MAIN("main"),
  /**
   * For the other server
   */
  SIMPLE("simple");

  /**
   * The xml value used to declare the server
   */
  private final String value;

  private ServerType(final String value)
  {
    this.value = value;
  }

  /**
   * Returns the xml value used to declare the server
   * 
   * @return the xml value used to declare the server
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Returns the {@link ServerType} found from the given type value. If no {@link ServerType} are found so,
   * {@link ServerType#SIMPLE} is returned.
   * 
   * @param pTypeValue
   *          the type value to look for
   * @return the {@link ServerType} found from the given type value. If no {@link ServerType} are found so,
   *         {@link ServerType#SIMPLE} is returned.
   */
  public static ServerType get(final String pTypeValue)
  {
    ServerType returnServerType = ServerType.SIMPLE;
    for (final ServerType serverType : values())
    {
      if (serverType.getValue().equals(pTypeValue))
      {
        returnServerType = serverType;
        break;
      }
    }
    return returnServerType;
  }

}
