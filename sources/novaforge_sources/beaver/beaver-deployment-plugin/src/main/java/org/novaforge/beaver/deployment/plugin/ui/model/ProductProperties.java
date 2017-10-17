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
package org.novaforge.beaver.deployment.plugin.ui.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;

public class ProductProperties
{
  private final String           productName;
  private List<LauncherProperty> launcherList = new ArrayList<LauncherProperty>();

  public ProductProperties(final String pName)
  {
    productName = pName;
  }

  public boolean propertyExists(final String pKey)
  {
    boolean exists = false;

    final Iterator<LauncherProperty> iProduct = launcherList.iterator();
    while ((exists == false) && (iProduct.hasNext() == true))
    {
      final LauncherProperty current = iProduct.next();

      if (current.getKey().equals(pKey))
      {
        exists = true;
      }
    }
    return exists;
  }

  public LauncherProperty getLauncherProperty(final String pKey)
  {
    LauncherProperty launcherProperty = null;

    final Iterator<LauncherProperty> iProduct = launcherList.iterator();
    while ((launcherProperty == null) && (iProduct.hasNext() == true))
    {
      final LauncherProperty current = iProduct.next();

      if (current.getKey().equals(pKey))
      {
        launcherProperty = current;
      }
    }
    return launcherProperty;
  }

  public void display()
  {

    BeaverLogger.getOutlogger().info("Product: " + productName);
    for (final LauncherProperty launcherProperty : launcherList)
    {
      BeaverLogger.getOutlogger().info(
          "    * " + launcherProperty.getKey() + " : " + launcherProperty.getValue());
    }
    BeaverLogger.getOutlogger().info("------------------------------------------------------------------");
  }

  /**
   * @return the productName
   */
  public String getProductName()
  {
    return productName;
  }

  /**
   * @return the launcherList
   */
  public List<LauncherProperty> getLauncherList()
  {
    return launcherList;
  }

  /**
   * @param pLauncherList
   *          the launcherList to set
   */
  public void setLauncherList(final List<LauncherProperty> pLauncherList)
  {
    launcherList = pLauncherList;
  }

}
