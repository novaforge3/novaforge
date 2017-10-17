/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.portal.internal.models;

import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.portal.models.PortalStringTokenized;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Guillaume Lamirand
 */
public class PortalStringTokenizedImplTest
{
  /**
   * PortalSpace source
   */
  private static final String   SOURCE      = "source";
  /**
   * PortalSpace project
   */
  private static final String   PROJECTID   = "project";
  /**
   * PortalSpace project 2
   */
  private static final String   PROJECTID2  = "project2";
  /**
   * PortalSpace instance
   */
  private static final String   INSTANCEID  = "instance";
  /**
   * PortalSpace instance 2
   */
  private static final String   INSTANCEID2 = "instance2";
  /**
   * PortalSpace type
   */
  private static final String   TYPE        = "type";
  /**
   * PortalSpace type 2
   */
  private static final String   TYPE2       = "type2";
  /**
   * PortalSpace uuid
   */
  private static final String   UUID        = "uuid";
  /**
   * PortalSpace uuid 2
   */
  private static final String   UUID2       = "uuid2";
  /**
   * PortalSpace view
   */
  private static final String   VIEW        = "view";
  /**
   * PortalSpace view 2
   */
  private static final String   VIEW2       = "view2";
  /**
   * The PortalStringTokenized to test
   */
  private PortalStringTokenized string;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    string = new PortalStringTokenizedImpl(SOURCE);
    string.setInstanceId(INSTANCEID);
    string.setPluginType(TYPE);
    string.setPluginUuid(UUID);
    string.setPluginViewId(VIEW);
    string.setProjectId(PROJECTID);
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#PortalStringTokenizedImpl(java.lang.String)}.
   */
  @Test
  public void testPortalStringTokenizedImpl()
  {
    final PortalStringTokenized string2 = new PortalStringTokenizedImpl(SOURCE);
    assertThat(string2.getSource(), notNullValue());
    assertThat(string2.getSource(), is(SOURCE));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#getSource()}.
   */
  @Test
  public void testGetSource()
  {
    assertThat(string.getSource(), notNullValue());
    assertThat(string.getSource(), is(SOURCE));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#getProjectId()}.
   */
  @Test
  public void testGetProjectId()
  {
    assertThat(string.getProjectId(), notNullValue());
    assertThat(string.getProjectId(), is(PROJECTID));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#setProjectId(java.lang.String)}.
   */
  @Test
  public void testSetProjectId()
  {
    string.setProjectId(PROJECTID2);
    assertThat(string.getProjectId(), notNullValue());
    assertThat(string.getProjectId(), is(PROJECTID2));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#getInstanceId()}.
   */
  @Test
  public void testGetInstanceId()
  {
    assertThat(string.getInstanceId(), notNullValue());
    assertThat(string.getInstanceId(), is(INSTANCEID));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#setInstanceId(java.lang.String)}.
   */
  @Test
  public void testSetInstanceId()
  {
    string.setInstanceId(INSTANCEID2);
    assertThat(string.getInstanceId(), notNullValue());
    assertThat(string.getInstanceId(), is(INSTANCEID2));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#getPluginViewId()}.
   */
  @Test
  public void testGetPluginViewId()
  {
    assertThat(string.getPluginViewId(), notNullValue());
    assertThat(string.getPluginViewId(), is(VIEW));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#setPluginViewId(java.lang.String)}.
   */
  @Test
  public void testSetPluginViewId()
  {
    string.setPluginViewId(VIEW2);
    assertThat(string.getPluginViewId(), notNullValue());
    assertThat(string.getPluginViewId(), is(VIEW2));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#getPluginUuid()}.
   */
  @Test
  public void testGetPluginUuid()
  {
    assertThat(string.getPluginUuid(), notNullValue());
    assertThat(string.getPluginUuid(), is(UUID));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#setPluginUuid(java.lang.String)}.
   */
  @Test
  public void testSetPluginUuid()
  {
    string.setPluginUuid(UUID2);
    assertThat(string.getPluginUuid(), notNullValue());
    assertThat(string.getPluginUuid(), is(UUID2));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#getPluginType()}.
   */
  @Test
  public void testGetPluginType()
  {
    assertThat(string.getPluginType(), notNullValue());
    assertThat(string.getPluginType(), is(TYPE));
  }

  /**
   * Test method for {@link PortalStringTokenizedImpl#setPluginType(java.lang.String)}.
   */
  @Test
  public void testSetPluginType()
  {
    string.setPluginType(TYPE2);
    assertThat(string.getPluginType(), notNullValue());
    assertThat(string.getPluginType(), is(TYPE2));
  }

}
