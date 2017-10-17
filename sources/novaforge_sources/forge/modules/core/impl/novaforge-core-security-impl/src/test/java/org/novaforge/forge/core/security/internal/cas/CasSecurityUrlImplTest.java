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
package org.novaforge.forge.core.security.internal.cas;

import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class will test public method of {@link CasSecurityUrlImpl}
 * 
 * @author Guillaume Lamirand
 */
public class CasSecurityUrlImplTest
{

  /**
   * Forge url
   */
  public static final String FORGE_URL         = "http://localhost:9000";
  /**
   * CAS url
   */
  public static final String CAS_URL           = "https://localhost:8443/cas";
  /**
   * CAS login
   */
  public static final String CAS_LOGIN         = CAS_URL + "/login";
  /**
   * Portal Service
   */
  public static final String PORTAL_SERVICE    = FORGE_URL + "/portal/private";
  /**
   * CAS service
   */
  public static final String CAS_LOGIN_SERVICE = CAS_URL + "/login?service=" + PORTAL_SERVICE;
  /**
   * CAS logout
   */
  public static final String CAS_LOGOUT        = CAS_URL + "/logout";

  /**
   * The CasSecurityUrl to test
   */
  private CasSecurityUrlImpl casSecurity;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    casSecurity = new CasSecurityUrlImpl();
    // Mock for ForgeConfigurationManager
    final ForgeConfigurationService forgeConfigurationManager = mock(ForgeConfigurationService.class);
    when(forgeConfigurationManager.getPublicUrl()).thenReturn(new URL(FORGE_URL));
    when(forgeConfigurationManager.getCasUrl()).thenReturn(new URL(CAS_URL));
    casSecurity.setForgeConfigurationService(forgeConfigurationManager);
  }

  /**
   * Test method for {@link CasSecurityUrlImpl#getCasLogin()}.
   */
  @Test
  public void testGetCasLogin()
  {
    final String casLogin = casSecurity.getCasLogin();
    assertThat(casLogin, is(CAS_LOGIN));

  }

  /**
   * Test method for {@link CasSecurityUrlImpl#getCasLogin(String)}.
   */
  @Test
  public void testGetCasLoginService()
  {
    final String casLogin = casSecurity.getCasLogin(PORTAL_SERVICE);
    assertThat(casLogin, is(CAS_LOGIN_SERVICE));

  }

  /**
   * Test method for {@link CasSecurityUrlImpl#getCasLogin(String)}.
   */
  @Test
  public void testGetCasLogout()
  {
    final String casLogin = casSecurity.getCasLogout();
    assertThat(casLogin, is(CAS_LOGOUT));
  }

}
