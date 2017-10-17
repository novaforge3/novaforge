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

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.net.URI;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link PortalApplicationImpl}
 * 
 * @author Guillaume Lamirand
 */
public class PortalApplicationImplTest
{

  /**
   * PortalApplication id
   */
  private static final String   ID            = "id";
  /**
   * PortalApplication id 2
   */
  private static final String   ID2           = "id2";
  /**
   * PortalApplication name
   */
  private static final String   NAME          = "name";
  /**
   * PortalApplication name 2
   */
  private static final String   NAME2         = "name2";
  /**
   * PortalApplication availability
   */
  private static final boolean  AVAILABILITY  = true;
  /**
   * PortalApplication availability 2
   */
  private static final boolean  AVAILABILITY2 = false;
  /**
   * PortalApplication icon
   */
  private static final Resource ICON          = new ThemeResource(NovaForgeResources.FLAG_EN);
  /**
   * PortalApplication icon 2
   */
  private static final Resource ICON2         = new ThemeResource(NovaForgeResources.FLAG_FR);
  /**
   * PortalApplication uri
   */
  private static URI  URI;
  /**
   * PortalApplication uri 2
   */
  private static URI  URI2;
  /**
   * PortalApplication uuid
   */
  private static UUID UUID;
  /**
   * PortalApplication uuid 2
   */
  private static UUID UUID2;
  /**
   * PortalApplication to test
   */
  private PortalApplication     app;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    UUID = java.util.UUID.randomUUID();
    UUID2 = java.util.UUID.randomUUID();
    URI = new URI("/myuri?instance_id=" + UUID);
    URI2 = new URI("/myuri?instance_id=" + UUID);

    app = new PortalApplicationImpl();
    app.setId(ID);
    app.setName(NAME);
    app.setUniqueId(UUID);
    app.setPortalURI(new PortalURIImpl(URI));
    app.setAvailability(AVAILABILITY);
    app.setIcon(ICON);
  }

  /**
   * Test method for {@link PortalApplicationImpl#getId()}.
   */
  @Test
  public void testGetId()
  {
    assertThat(app.getId(), is(ID));
  }

  /**
   * Test method for {@link PortalApplicationImpl#setId(java.lang.String)}.
   */
  @Test
  public void testSetId()
  {
    app.setId(ID2);
    assertThat(app.getId(), is(ID2));
  }

  /**
   * Test method for {@link PortalApplicationImpl#getUniqueId()}.
   */
  @Test
  public void testGetUniqueId()
  {
    assertThat(app.getUniqueId(), is(UUID));
  }

  /**
   * Test method for {@link PortalApplicationImpl#setUniqueId(UUID)}.
   */
  @Test
  public void testSetUniqueId()
  {
    app.setUniqueId(UUID2);
    assertThat(app.getUniqueId(), is(UUID2));
  }

  /**
   * Test method for {@link PortalApplicationImpl#getName()}.
   */
  @Test
  public void testGetName()
  {
    assertThat(app.getName(), is(NAME));
  }

  /**
   * Test method for {@link PortalApplicationImpl#setName(java.lang.String)}.
   */
  @Test
  public void testSetName()
  {
    app.setName(NAME2);
    assertThat(app.getName(), is(NAME2));
  }

  /**
   * Test method for {@link PortalApplicationImpl#getUri()}.
   */
  @Test
  public void testGetUri()
  {
    assertThat(app.getPortalURI().getRelativePath(), is(URI.toString()));
  }

  /**
   * Test method for {@link PortalApplicationImpl#setUri(java.net.URI)}.
   * 
   * @throws PortalException
   */
  @Test
  public void testSetUri() throws PortalException
  {
    app.setPortalURI(new PortalURIImpl(URI2));
    assertThat(app.getPortalURI().getRelativePath(), is(URI2.toString()));
  }

  /**
   * Test method for {@link PortalApplicationImpl#getIcon()}.
   */
  @Test
  public void testGetIcon()
  {
    assertThat(app.getIcon(), is(ICON));
  }

  /**
   * Test method for {@link PortalApplicationImpl#setIcon(byte[])}.
   */
  @Test
  public void testSetIcon()
  {
    app.setIcon(ICON2);
    assertThat(app.getIcon(), is(ICON2));
  }

  /**
   * Test method for {@link PortalApplicationImpl#isAvailable()}.
   */
  @Test
  public void testIsAvailable()
  {
    assertThat(app.isAvailable(), is(AVAILABILITY));
  }

  /**
   * Test method for {@link PortalApplicationImpl#setAvailability(boolean)}.
   */
  @Test
  public void testSetAvailability()
  {
    app.setAvailability(AVAILABILITY2);
    assertThat(app.isAvailable(), is(AVAILABILITY2));
  }

}
