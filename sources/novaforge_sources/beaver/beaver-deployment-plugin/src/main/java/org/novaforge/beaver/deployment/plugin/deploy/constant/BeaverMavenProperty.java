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

import org.apache.maven.project.MavenProject;

/**
 * This enum contains contants in {@link MavenProject#getProperties()}
 * 
 * @author Guillaume Lamirand
 */
public enum BeaverMavenProperty
{
  /**
   * Contants for packaging type used for process
   */
  PROCESS_PACKAGING("beaver-process"),
  /**
   * Contants for packaging type used for server
   */
  SERVER_PACKAGING("beaver-server"),
  /**
   * Contants for server id property
   */
  SERVER_ID("id"),
  /**
   * Contants for server type property
   */
  SERVER_TYPE("type"),
  /**
   * Contants for server setup property
   */
  SERVER_INSTALL("install"),
  /**
   * Contants for server update property
   */
  SERVER_UPDATE("update"),
  /**
   * Contants for requisite property
   */
  REQUISITE("requisite"),
  /**
   * Contants for product id property
   */
  PRODUCT_ID("productId"),
  /**
   * Contants for process property
   */
  PROCESS("process"),
  /**
   * Contants for data property
   */
  DATA("data"),
  /**
   * Contants for install/update/delete script property
   */
  SCRIPT("script"),
  /**
   * Contants for config script property
   */
  CONFIG("config");

  /**
   * Contains the key used in {@link MavenProject#getProperties()}
   */
  private final String propertyKey;

  /**
   * Private default constructor
   * 
   * @param pKey
   *          the property key associated
   */
  private BeaverMavenProperty(final String pKey)
  {
    propertyKey = pKey;

  }

  /**
   * Returns the property key associated
   * 
   * @return the propertyKey
   */
  public String getPropertyKey()
  {
    return propertyKey;
  }

  /**
   * Verify if the given key is a {@link BeaverMavenProperty}
   * 
   * @param pKey
   *          the key to verify
   * @return true if key given is a {@link BeaverMavenProperty}
   */
  public static boolean isBeaverMavenProperty(final String pKey)
  {
    boolean isBeaver = false;
    for (final BeaverMavenProperty beaverMavenProperty : values())
    {
      if (beaverMavenProperty.getPropertyKey().equals(pKey))
      {
        isBeaver = true;
        break;
      }
    }
    return isBeaver;
  }
}
