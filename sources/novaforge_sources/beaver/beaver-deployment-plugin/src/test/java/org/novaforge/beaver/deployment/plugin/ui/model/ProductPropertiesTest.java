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

import org.junit.Test;
import org.novaforge.beaver.exception.BeaverException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ProductPropertiesTest
{

  @Test
  public void productProperties() throws BeaverException
  {
    final ProductProperties property = new ProductProperties("test");
    assertNotNull(property);
    assertNotNull(property.getProductName());
    assertEquals("test", property.getProductName());
  }

  @Test
  public void addLauncherProperty() throws BeaverException
  {
    final ProductProperties productProperties = new ProductProperties("test");
    final LauncherProperty property = new LauncherProperty("key", "value");
    productProperties.getLauncherList().add(property);

    final LauncherProperty testInsert = productProperties.getLauncherList().get(0);
    assertNotNull(testInsert);
    assertEquals("key", testInsert.getKey());
    assertEquals("value", testInsert.getValue());
  }

  @Test
  public void propertyExists() throws BeaverException
  {

    final ProductProperties productProperties = new ProductProperties("test");
    final LauncherProperty property = new LauncherProperty("key", "value");
    productProperties.getLauncherList().add(property);

    assertTrue(productProperties.propertyExists("key"));
  }

  @Test
  public void getLauncherProperty() throws BeaverException
  {

    final ProductProperties productProperties = new ProductProperties("test");

    final LauncherProperty property = new LauncherProperty("key", "value");
    productProperties.getLauncherList().add(property);

    final LauncherProperty getProperty = productProperties.getLauncherProperty("key");
    assertNotNull(getProperty);
    assertNotNull(getProperty.getKey());
    assertNotNull(getProperty.getValue());

    assertEquals("key", getProperty.getKey());
    assertEquals("value", getProperty.getValue());
  }

}
