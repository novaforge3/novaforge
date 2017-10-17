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

import org.novaforge.beaver.exception.BeaverException;

/**
 * Interface describing service to handle beaver deployment context
 * 
 * @author Guillaume Lamirand
 * @version 2.0
 */
public interface DeploymentContext
{
  /**
   * Returns the id of the main server from your context
   * 
   * @return server id of the main server or <code>null</code> if none are found
   */
  String getMainServerId();

  /**
   * Adds if no already existing a new main server with given id to deployment context.
   * If the server type is MAIN, and one is already existing a exception is thrown
   * 
   * @param pServerId
   *          the server id
   * @param pType
   *          the server type used to store the server, can be <code>null</code>
   * @throws BeaverException
   *           thrown if the server type equals MAIN, and one is already existing
   */
  void addServer(String pServerId, final ServerType pType) throws BeaverException;

  /**
   * Return if existing the property value found defined by the server id and the property key
   * 
   * @param pServerId
   *          the server id
   * @param pPropertyKey
   *          the property key
   * @return the value found or <code>null</code> if unexisting
   */
  String getServerProperty(final String pServerId, final String pPropertyKey);

  /**
   * Adds a new property to a server if it is existing
   * 
   * @param pServerId
   *          the server id
   * @param pPropertyKey
   *          the property key
   * @param pPropertyValue
   *          the property value
   */
  void addServerProperty(final String pServerId, final String pPropertyKey, final String pPropertyValue);

  /**
   * Check if the given property key is defined for a server with the given id
   * 
   * @param pServerId
   *          the server id
   * @param pPropertyKey
   *          the property key
   * @return <code>true</code> if existing
   */
  boolean existServerProperty(final String pServerId, final String pPropertyKey);

  /**
   * Removes if existing the server with the given id.
   * <p>
   * WARN : All products and properties will be removed too
   * </p>
   * 
   * @param pServerId
   *          the server id
   */
  void removeServer(String pServerId);

  /**
   * Checks if a server exists with the given id
   * 
   * @param pServerId
   *          the server id
   * @return <code>true</code> if a server exists with the given id
   */
  boolean existServer(String pServerId);

  /**
   * Adds if no already existing a new server with given id to deployment context
   * 
   * @param pServerId
   *          the server id
   */
  void addProduct(final String pServerId, final String pProductId, final String pVersion);

  /**
   * Removes if existing the product on a server with the given id.
   * <p>
   * WARN : All properties will be removed too
   * </p>
   * 
   * @param pServerId
   *          the server id
   */
  void removeProduct(final String pServerId, final String pProductId);

  /**
   * Adds a new property to a product on a server if it is existing
   * 
   * @param pServerId
   *          the server id
   * @param pPropertyKey
   *          the property key
   * @param pPropertyValue
   *          the property value
   */
  void addProductProperty(final String pServerId, final String pProductId, final String pPropertyKey,
      final String pPropertyValue);

  /**
   * Adds a new context property to a product on a server if it is existing
   * 
   * @param pServerId
   *          the server id
   * @param pPropertyKey
   *          the property key
   * @param pPropertyValue
   *          the property value
   */
  void addProductContext(final String pServerId, final String pProductId, final String pContextKey,
      final String pContextValue);

  /**
   * Checks if a product exists with the given id
   * 
   * @param pProductId
   *          the product id
   * @return <code>true</code> if a product exists with the given id
   */
  boolean existProduct(final String pServerId, final String pProductId);

  boolean existProductContext(final String pServerId, final String pProductId, final String pContextKey);

  boolean existProductProperty(final String pServerId, final String pProductId, final String pPropertyKey);

  String getProductContext(final String pServerId, final String pProductId, final String pContextKey);

  String getProductProperty(final String pServerId, final String pProductId, final String pPropertyKey);

  String getProductStatus(final String pServerId, final String pProductId);

  String getProductUpdateVersion(final String pServerId, final String pProductId);

  String getProductVersion(final String pServerId, final String pProductId);

  boolean isProductStatusInstalling(final String pServerId, final String pProductId);

  boolean isProductStatusInstalled(final String pServerId, final String pProductId);

  void setProductUpdateVersion(final String pServerId, final String pProductId, final String pNumber);

  void setProductStatusInstalled(final String pServerId, final String pProductId);

  void setProductStatusInstalling(final String pServerId, final String pProductId);

  void setProductVersion(final String pServerId, final String pProductId, final String pVersion);

  void writeFile() throws Exception;

}
