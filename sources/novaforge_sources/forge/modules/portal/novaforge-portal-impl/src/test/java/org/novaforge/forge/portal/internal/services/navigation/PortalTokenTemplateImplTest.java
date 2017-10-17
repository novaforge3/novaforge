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
package org.novaforge.forge.portal.internal.services.navigation;

import org.junit.Test;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.internal.models.PortalStringTokenizedImpl;
import org.novaforge.forge.portal.models.PortalStringTokenized;
import org.novaforge.forge.portal.models.PortalToken;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link NavigationTokenImpl}
 * 
 * @author Guillaume Lamirand
 * @see NavigationTokenImpl
 */
public class PortalTokenTemplateImplTest
{

  /**
   * Source string
   */
  private final static String SOURCE   = "@projectId/@instanceId/@pluginType/@pluginView/@pluginUUID/@userName";

  /**
   * Project id
   */
  private final static String PROJECT  = "myproject";
  /**
   * Instance id
   */
  private final static String INSTANCE = "myinstance";
  /**
   * Type id
   */
  private final static String TYPE     = "mytype";
  /**
   * View id
   */
  private final static String VIEW     = "myview";
  /**
   * UUID
   */
  private final static String UUID     = "myuuid";

  /**
   * Target string
   */
  private final static String TARGET   = "myproject/myinstance/mytype/myview/myuuid/@userName";

  /**
   * Test method for {@link NavigationTokenImpl#resolved(PortalStringTokenized)}.
   * 
   * @throws PortalException
   */
  @Test
  public void resolved() throws PortalException
  {
    final NavigationTokenImpl token = new NavigationTokenImpl();
    final PortalStringTokenized string = new PortalStringTokenizedImpl(SOURCE);
    string.setInstanceId(INSTANCE);
    string.setPluginType(TYPE);
    string.setPluginUuid(UUID);
    string.setPluginViewId(VIEW);
    string.setProjectId(PROJECT);
    assertEquals(TARGET, token.resolved(string));

  }

  /**
   * Test method for {@link NavigationTokenImpl#containsToken(String, PortalToken)}.
   */
  @Test
  public void contains()
  {
    final NavigationTokenImpl token = new NavigationTokenImpl();
    final boolean containsToken = token.containsToken(SOURCE, PortalToken.USER_NAME);
    assertThat(containsToken, is(true));

  }
}
