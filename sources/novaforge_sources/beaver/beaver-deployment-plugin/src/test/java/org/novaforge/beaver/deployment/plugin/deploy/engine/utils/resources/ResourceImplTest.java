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
package org.novaforge.beaver.deployment.plugin.deploy.engine.utils.resources;

import org.junit.Test;
import org.novaforge.beaver.exception.BeaverException;
import org.novaforge.beaver.resource.MavenPropertyMode;
import org.novaforge.beaver.resource.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ResourceImplTest
{

  @Test
  public void ResourceImpl() throws BeaverException
  {
    final Resource property = new ResourceImpl("server:product1.property");
    assertNotNull(property);
    assertNotNull(property.getKey());
    assertNotNull(property.getProductId());
    assertNotNull(property.getServerId());
    assertNotNull(property.isCorrectFormat());

    assertTrue(property.isCorrectFormat());
    assertEquals("property", property.getKey());
    assertEquals("product1", property.getProductId());
    assertEquals("server", property.getServerId());
  }

  @Test
  public void getMavenProperty() throws BeaverException
  {
    final Resource property = new ResourceImpl("server:product1.property");
    assertNotNull(property);
    assertNotNull(property.getKey());
    assertNotNull(property.getProductId());
    assertNotNull(property.getServerId());
    assertNotNull(property.isCorrectFormat());

    assertTrue(property.isCorrectFormat());
    assertEquals("property", property.getKey());
    assertEquals("product1", property.getProductId());
    assertEquals("server", property.getServerId());
    assertEquals("server:product1.property", property.getMavenProperty(MavenPropertyMode.FULL));
    assertEquals("server:product1.property", property.getMavenProperty(MavenPropertyMode.SERVER_PRODUCT_ID));
    assertEquals("main:product1.property",
        property.getMavenProperty(MavenPropertyMode.SERVER_MAIN_PRODUCT_ID));
    assertEquals("local:product1.property",
        property.getMavenProperty(MavenPropertyMode.SERVER_LOCAL_PRODUCT_ID));
    assertEquals("product1.property", property.getMavenProperty(MavenPropertyMode.PRODUCT_ID));
    assertEquals("server:product.property", property.getMavenProperty(MavenPropertyMode.SERVER_PRODUCT));
    assertEquals("product.property", property.getMavenProperty(MavenPropertyMode.PRODUCT));

    try
    {

      assertEquals("server:property", property.getMavenProperty(MavenPropertyMode.SERVER));
      fail("Server mode should throw an exception");
    }
    catch (final BeaverException e)
    {
      // Should throw an exception
    }
  }

  @Test
  public void isCurrentProductRelated() throws BeaverException
  {
    final Resource propertyFalse = new ResourceImpl("product1.property");
    assertTrue(propertyFalse.isCorrectFormat());
    assertFalse(propertyFalse.isCurrentProductRelated());

    final Resource propertyTrue = new ResourceImpl("product.property");
    assertTrue(propertyTrue.isCorrectFormat());
    assertTrue(propertyTrue.isCurrentProductRelated());
  }
}
