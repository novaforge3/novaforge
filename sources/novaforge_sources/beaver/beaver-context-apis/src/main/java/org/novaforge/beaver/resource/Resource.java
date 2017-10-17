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
package org.novaforge.beaver.resource;

import org.novaforge.beaver.exception.BeaverException;

/**
 * This interface describes an object wrapping a full resource value to find the relative server, productId
 * and property key.
 * 
 * @author Guillaume Lamirand
 */
public interface Resource
{

  /**
   * Defined the string used to defined a "product" property such as 'product.mykey'
   */
  public static String IGNORE_PREFIX     = "_";
  /**
   * Defined the string used to defined a "product" property such as 'product.mykey'
   */
  public static String PRODUCT           = "product";
  /**
   * Defined the string used to separate product id and the key such as 'productId.mykey' or 'product.mykey'
   */
  public static String PRODUCT_SEPARATOR = ".";
  /**
   * Defined the string used to define a server reference such as 'serverid:productId.mykey' or
   * 'serverid:product.mykey'
   */
  public static String SERVER_SEPARATOR  = ":";
  /**
   * Defined the string used to define a locale server reference such as 'local:productId.mykey' or
   * 'local:product.mykey'
   */
  public static String LOCAL_SERVER      = "local";
  /**
   * Defined the string used to define a main server reference such as 'main:productId.mykey' or
   * 'main:product.mykey'
   */
  public static String MAIN_SERVER       = "main";

  /**
   * Returns the maven property depending on the mode choosen and originalresource ie if
   * {@link Resource#isProductRelated()} returns <code>true</code> or if {@link Resource#isServerRelated()}
   * returns <code>true</code>.
   * 
   * @param pMode
   *          includes server in the return value (server:productId.mykey)
   * @return the maven property
   * @throws BeaverException
   */
  String getMavenProperty(final MavenPropertyMode pMode) throws BeaverException;

  /**
   * Returns the server id found in the source property
   * 
   * @return the server id concerned
   */
  String getServerId();

  /**
   * Check if resource is related to a server and not to a product. It means
   * {@link Resource#isProductRelated()} returns <code>false</code> if this method return <code>true</code>.
   * 
   * @return <code>true</code> is related to a server and not a product, <code>false</code> if it
   *         concerns a product
   */
  boolean isServerRelated();

  /**
   * Check is property concerns the local server or remote one
   * 
   * @return <code>true</code> is property given concerned the locale server, <code>false</code> if it
   *         concerns a remote one
   */
  boolean isLocaleServerRelated();

  /**
   * Check is property concerns the main server or an other one
   * 
   * @return <code>true</code> is property given concerned the main server, <code>false</code> otherwise
   */
  boolean isMainServerRelated();

  /**
   * Returns the product Id found in the source property
   * 
   * @return the productid concerned
   */
  String getProductId();

  /**
   * Check if resource is related to a product and not only to a product. It means
   * {@link Resource#isServerRelated()} returns <code>false</code> if this method return <code>true</code>.
   * 
   * @return <code>true</code> is related to a product and not only to a server, <code>false</code> if it
   *         concerns a server
   */
  boolean isProductRelated();

  /**
   * Check is property concerns current product or a specific product id
   * 
   * @return <code>true</code> is property given concerned the current product, <code>false</code> if it
   *         concerns a specific productid
   */
  boolean isCurrentProductRelated();

  /**
   * Returns the property keyfound in the source property
   * 
   * @return the property key concerned
   */
  String getKey();

  /**
   * Check if the property is correctly formated
   * 
   * @return <code>true</true> if the property can be read
   */
  boolean isCorrectFormat();

}