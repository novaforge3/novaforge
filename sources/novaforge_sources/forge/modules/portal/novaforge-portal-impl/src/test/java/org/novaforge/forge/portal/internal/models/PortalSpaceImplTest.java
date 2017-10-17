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
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalSpace;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link PortalSpaceImpl}
 * 
 * @author Guillaume Lamirand
 */
public class PortalSpaceImplTest
{
  /**
   * PortalSpace id
   */
  private static final String ID           = "id";
  /**
   * PortalSpace id 2
   */
  private static final String ID2          = "id2";
  /**
   * PortalSpace name
   */
  private static final String NAME         = "name";
  /**
   * PortalSpace name 2
   */
  private static final String NAME2        = "name2";
  /**
   * PortalSpace description
   */
  private static final String DESCRIPTION  = "description";
  /**
   * PortalSpace description 2
   */
  private static final String DESCRIPTION2 = "description2";
  /**
   * PortalApplication mock id
   */
  private static final String APP_ID       = "app_id";
  /**
   * PortalSpace to test
   */
  private PortalSpace         space;
  /**
   * PortalApplication mock
   */
  private PortalApplication   mock;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    space = new PortalSpaceImpl();
    space.setId(ID);
    space.setName(NAME);
    space.setDescription(DESCRIPTION);
    mock = mock(PortalApplication.class);
    when(mock.getId()).thenReturn(APP_ID);

  }

  /**
   * Test method for {@link PortalSpaceImpl#getId()}.
   */
  @Test
  public void testGetId()
  {
    assertThat(space.getId(), is(ID));
  }

  /**
   * Test method for {@link PortalSpaceImpl#setId(java.lang.String)}.
   */
  @Test
  public void testSetId()
  {
    space.setId(ID2);
    assertThat(space.getId(), is(ID2));
  }

  /**
   * Test method for {@link PortalSpaceImpl#getName()}.
   */
  @Test
  public void testGetName()
  {
    assertThat(space.getName(), is(NAME));
  }

  /**
   * Test method for {@link PortalSpaceImpl#setName(java.lang.String)}.
   */
  @Test
  public void testSetName()
  {
    space.setName(NAME2);
    assertThat(space.getName(), is(NAME2));
  }

  /**
   * Test method for {@link PortalSpaceImpl#getDescription()}.
   */
  @Test
  public void testGetDescription()
  {
    assertThat(space.getDescription(), is(DESCRIPTION));
  }

  /**
   * Test method for {@link PortalSpaceImpl#setDescription(java.lang.String)}.
   */
  @Test
  public void testSetDescription()
  {
    space.setDescription(DESCRIPTION2);
    assertThat(space.getDescription(), is(DESCRIPTION2));
  }

  /**
   * Test method for {@link PortalSpaceImpl#getApplications()}.
   */
  @Test
  public void testGetApplications()
  {
    assertThat(space.getApplications(), notNullValue());
    assertThat(space.getApplications().isEmpty(), is(true));
  }

  /**
   * Test method for {@link PortalSpaceImpl#addApplication(PortalApplication)}.
   */
  @Test
  public void testAddApplication()
  {
    space.addApplication(mock);
    assertThat(space.getApplications(), notNullValue());
    assertThat(space.getApplications().isEmpty(), is(false));
    assertThat(space.getApplications().size(), is(1));
    assertThat(space.getApplications().get(0), is(mock));
  }

  /**
   * Test method for {@link PortalSpaceImpl#removeApplication(PortalApplication)}.
   */
  @Test
  public void testRemoveApplication()
  {
    space.removeApplication(mock);
    assertThat(space.getApplications(), notNullValue());
    assertThat(space.getApplications().isEmpty(), is(true));
    assertThat(space.getApplications().size(), is(0));
  }

}
