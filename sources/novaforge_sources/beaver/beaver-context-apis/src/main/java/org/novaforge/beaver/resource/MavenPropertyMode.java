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

/**
 * This enum defines different way to retrieve maven property from a resource
 * 
 * @author Guillaume Lamirand
 */
public enum MavenPropertyMode
{
  /**
   * The maven property returned will contains the server id, the original productid if defined and
   * the key such as <code>server_id:product_id.key</code>, <code>serverid:product.key</code> or
   * <code>server_id:key</code>
   */
  FULL
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayServer()
    {
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayProduct()
    {
      return true;
    }
  },
  /**
   * The maven property returned will contains the server id resolved (local or main replace by concret
   * value), the original productid if defined and
   * the key such as <code>server_id:product_id.key</code>, <code>serverid:product.key</code> or
   * <code>server_id:key</code>
   */
  FULL_RESOLVED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayServer()
    {
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayProduct()
    {
      return true;
    }
  },
  /**
   * The maven property returned will contains the server id and the key such as <code>server_id:key</code>
   */
  SERVER
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayServer()
    {
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayProduct()
    {
      return false;
    }
  },
  /**
   * The maven property returned will contains the main key word, the product id and the key such as
   * <code>main:product_id.key</code>
   */
  SERVER_MAIN_PRODUCT_ID
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayServer()
    {
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayProduct()
    {
      return true;
    }
  },
  /**
   * The maven property returned will contains the main key word, the product id and the key such as
   * <code>local:product_id.key</code>
   */
  SERVER_LOCAL_PRODUCT_ID
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayServer()
    {
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayProduct()
    {
      return true;
    }
  },
  /**
   * The maven property returned will contains the server id of the resource (main, local or null), the
   * product id and the key such as <code>server_id:product_id.key</code>
   */
  SERVER_PRODUCT_ID
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayServer()
    {
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayProduct()
    {
      return true;
    }
  },
  /**
   * The maven property returned will contains the product id and the key such as <code>product_id.key</code>
   */
  PRODUCT_ID
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayServer()
    {
      return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayProduct()
    {
      return true;
    }
  },
  /**
   * The maven property returned will contains the server id, the the key word product and the key such as
   * <code>server_id:product.key</code>
   */
  SERVER_PRODUCT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayServer()
    {
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayProduct()
    {
      return true;
    }
  },
  /**
   * The maven property returned will contains the key word product and the key such as
   * <code>product.key</code>
   */
  PRODUCT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayServer()
    {
      return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displayProduct()
    {
      return true;
    }
  },
  ;

  /**
   * Display server part of resource
   * 
   * @return true or false
   */
  public abstract boolean displayServer();

  /**
   * Display product part of resource
   * 
   * @return true or false
   */
  public abstract boolean displayProduct();

}
