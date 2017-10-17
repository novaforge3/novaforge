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
package org.novaforge.forge.portal.internal.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata;
import org.novaforge.forge.core.plugins.domain.plugin.PluginView;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.core.security.authorization.AuthorizationService;
import org.novaforge.forge.core.security.authorization.Logical;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.internal.services.navigation.NavigationMessageImpl;
import org.novaforge.forge.portal.internal.services.navigation.NavigationSecurityImpl;
import org.novaforge.forge.portal.internal.services.navigation.NavigationXMLImpl;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalSpace;
import org.novaforge.forge.portal.models.PortalStringTokenized;
import org.novaforge.forge.portal.models.PortalToken;
import org.novaforge.forge.portal.models.PortalURI;
import org.novaforge.forge.portal.services.navigation.NavigationToken;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Guillaume Lamirand
 */
public class PortalNavigationImplTest
{

  /**
   * The PortalNavigationImpl to test
   */
  private PortalNavigationImpl navigation;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    navigation = new PortalNavigationImpl();

    // Mock for PortalTokenTemplate -it has its own tu
    final NavigationToken tokenTemplate = mock(NavigationToken.class);
    when(tokenTemplate.resolved(Matchers.any(PortalStringTokenized.class))).thenReturn(
        PortalConfigTest.SAMPLE_URI);
    when(tokenTemplate.containsToken(Matchers.any(String.class), Matchers.eq(PortalToken.USER_NAME)))
        .thenReturn(false);
    navigation.setNavigationToken(tokenTemplate);

    // Mock for ForgeConfigurationManager
    final ForgeConfigurationService forgeConfigurationService = mock(ForgeConfigurationService.class);
    when(forgeConfigurationService.getForgeProjectId()).thenReturn(PortalConfigTest.FORGE);
    when(forgeConfigurationService.getPublicUrl()).thenReturn(new URL(PortalConfigTest.FORGE_URL));
    when(forgeConfigurationService.getPortalConfDirectory())
        .thenReturn(PortalConfigTest.NAVIGATION_DIRECTORY);
    navigation.setForgeConfigurationService(forgeConfigurationService);

    // Don't use mock for NavigationXML
    final NavigationXMLImpl navigationXML = new NavigationXMLImpl();
    navigation.setNavigationXML(navigationXML);

    // Don't use mock for NavigationMessageImpl
    final NavigationMessageImpl navigationMessage = new NavigationMessageImpl();
    navigation.setNavigationMessage(navigationMessage);

    // Don't use mock for NavigationMessageImpl
    final AuthorizationService authorizationService = mock(AuthorizationService.class);
    when(
        authorizationService.hasRoleOnProject(Matchers.any(Set.class), Matchers.any(Logical.class),
            Matchers.any(String.class))).thenReturn(true);
    when(authorizationService.isPermitted(Matchers.any(Set.class), Matchers.any(Logical.class))).thenReturn(
        true);
    final NavigationSecurityImpl navigationSecurity = new NavigationSecurityImpl();
    navigationSecurity.setAuthorizationService(authorizationService);
    navigation.setNavigationSecurity(navigationSecurity);

    // Mock for AuthentificationService
    final AuthentificationService authentificationService = mock(AuthentificationService.class);
    when(authentificationService.getCurrentUser()).thenReturn(PortalConfigTest.USER);
    navigation.setAuthentificationService(authentificationService);

    // Mock for PluginsManager
    // Initialization plugin views mock
    final PluginView defaultView = mock(PluginView.class);
    when(defaultView.getViewId()).thenReturn(PluginViewEnum.DEFAULT);
    when(defaultView.getName()).thenReturn(PluginViewEnum.DEFAULT.name());
    when(defaultView.getURI()).thenReturn(PortalConfigTest.VIEW_DEFAULT);
    final PluginView adminView = mock(PluginView.class);
    when(adminView.getViewId()).thenReturn(PluginViewEnum.ADMINISTRATION);
    when(adminView.getName()).thenReturn(PluginViewEnum.ADMINISTRATION.name());
    when(adminView.getURI()).thenReturn(PortalConfigTest.VIEW_ADMIN);
    final List<PluginView> mockList = new ArrayList<PluginView>();
    mockList.add(defaultView);
    mockList.add(adminView);
    // Initialization plugin metadata mock
    final PluginServiceMetadata pluginServiceMetadata = mock(PluginServiceMetadata.class);
    when(pluginServiceMetadata.getPluginViews()).thenReturn(mockList);
    // Initialization plugin service mock
    final PluginService pluginService = mock(PluginService.class);
    when(pluginService.getMetadata()).thenReturn(pluginServiceMetadata);
    // Initialization plugins manager mock
    final PluginsManager pluginsManager = mock(PluginsManager.class);
    when(pluginsManager.getPluginService(Matchers.any(String.class))).thenReturn(pluginService);
    final PluginMetadata pluginMetadata = mock(PluginMetadata.class);
    when(pluginMetadata.isAvailable()).thenReturn(true);
    when(pluginMetadata.getUUID()).thenReturn(UUID.randomUUID().toString());
    when(pluginMetadata.getType()).thenReturn("Type");
    final List<PluginMetadata> pluginList = new ArrayList<PluginMetadata>();
    pluginList.add(pluginMetadata);
    navigation.setPluginsManager(pluginsManager);

  }

  /**
   * Test method for {@link PortalNavigationImpl#init()}.
   */
  @Test
  public void testStart()
  {
    try
    {
      navigation.init();
    }
    catch (final PortalException e)
    {
      e.printStackTrace();
      fail("Portal-Config cannot be build from xml");
    }
  }

  /**
   * Test method for {@link PortalNavigationImpl#getForgeSpaces()}.
   * 
   * @throws PortalException
   * @throws URISyntaxException
   */
  @Test
  public void testGetForgeSpaces() throws PortalException, URISyntaxException
  {
    navigation.init();
    final List<PortalSpace> forgeSpaces = navigation.getForgeSpaces(Locale.ENGLISH);
    assertThat(forgeSpaces, notNullValue());
    assertThat(forgeSpaces.isEmpty(), is(false));
    assertThat(forgeSpaces.size(), is(3));
    for (final PortalSpace portalSpace : forgeSpaces)
    {
      assertThat(portalSpace.getId(), notNullValue());
      if (PortalConfigTest.PUBLIC_ID.equals(portalSpace.getId()))
      {
        assertThat(portalSpace.getName(), is(PortalConfigTest.PUBLIC_NAME));
        assertThat(portalSpace.getDescription(), is(PortalConfigTest.SPACE_DESCRIPTION));
        assertThat(portalSpace.getApplications().isEmpty(), is(false));
        assertThat(portalSpace.getApplications().size(), is(1));

        assertThat(portalSpace.getApplications().get(0).getName(), notNullValue());
        assertThat(portalSpace.getApplications().get(0).getName(), is(PortalConfigTest.NOVAFORGE_NAME));
        assertThat(portalSpace.getApplications().get(0).getId(), notNullValue());
        assertThat(portalSpace.getApplications().get(0).getId(), is(PortalConfigTest.NOVAFORGE_ID));
        assertThat(portalSpace.getApplications().get(0).getUniqueId(), notNullValue());
        assertThat(portalSpace.getApplications().get(0).getPortalURI(), notNullValue());
        assertThat(portalSpace.getApplications().get(0).getPortalURI().getAbsoluteURL().toString()
            .startsWith(PortalConfigTest.NOVAFORGE_URL), is(true));
        assertThat(portalSpace.getApplications().get(0).isAvailable(), notNullValue());
        assertThat(portalSpace.getApplications().get(0).isAvailable(), is(true));

      }
      else if (PortalConfigTest.FORGE_TEST_SPACE_ID.equals(portalSpace.getId()))
      {
        assertThat(portalSpace.getName(), notNullValue());
        assertThat(portalSpace.getName(), is(PortalConfigTest.FORGE_TEST_SPACE_NAME));
        assertThat(portalSpace.getDescription(), is(PortalConfigTest.SPACE_DESCRIPTION));
        assertThat(portalSpace.getApplications().isEmpty(), is(false));
        assertThat(portalSpace.getApplications().size(), is(2));
        for (final PortalApplication portalApp : portalSpace.getApplications())
        {
          assertThat(portalSpace.getApplications().get(0).getName(), notNullValue());
          assertThat(portalSpace.getApplications().get(0).getId(), notNullValue());
          assertThat(portalSpace.getApplications().get(0).getUniqueId(), notNullValue());
          assertThat(portalSpace.getApplications().get(0).getPortalURI(), notNullValue());
          assertThat(portalSpace.getApplications().get(0).isAvailable(), notNullValue());
          assertThat(portalSpace.getApplications().get(0).isAvailable(), is(true));
          if (PortalConfigTest.ADMIN_PROJECT_ID.equals(portalApp.getId()))
          {
            assertThat(portalApp.getName(), is(PortalConfigTest.ADMIN_PROJECT_NAME));
            assertThat(portalApp.getId(), is(PortalConfigTest.ADMIN_PROJECT_ID));
            assertThat(portalApp.getPortalURI().isInternalModule(), is(false));
            assertThat(
                portalApp.getPortalURI().getAbsoluteURL().toString().startsWith(PortalConfigTest.APP_URL),
                is(true));

          }
          else if (PortalConfigTest.VALIDATION_ID.equals(portalApp.getId()))
          {
            assertThat(portalApp.getName(), is(PortalConfigTest.VALIDATION_ID));
            assertThat(portalApp.getId(), is(PortalConfigTest.VALIDATION_ID));
            assertThat(portalApp.getPortalURI().isInternalModule(), is(false));
            assertThat(
                portalApp.getPortalURI().getAbsoluteURL().toString().startsWith(PortalConfigTest.APP_URL),
                is(true));

          }
          else if (PortalConfigTest.SAMPLE_URI.equals(portalApp.getId()))
          {
            assertThat(portalApp.getName(), is(PortalConfigTest.SAMPLE_URI));
            assertThat(portalApp.getId(), is(PortalConfigTest.SAMPLE_URI));
            assertThat(portalApp.getPortalURI().isInternalModule(), is(false));
            assertThat(portalApp.getPortalURI().getRelativePath().equals(PortalConfigTest.SAMPLE_URI),
                is(true));
          }
          else
          {
            fail("Invalid application id");
          }
        }

      }
      else if (PortalConfigTest.FORGE_TEST_SPACE_ID2.equals(portalSpace.getId()))
      {
        assertThat(portalSpace.getName(), is(PortalConfigTest.FORGE_TEST_SPACE_NAME2));
        assertThat(portalSpace.getDescription(), is(PortalConfigTest.SPACE_DESCRIPTION));
        assertThat(portalSpace.getApplications().isEmpty(), is(true));

      }
      else
      {
        fail("Invalid space id");
      }
    }

  }

  /**
   * Test method for {@link PortalNavigationImpl#getProjectSpaces(java.lang.String)} .
   * 
   * @throws PortalException
   */
  @Test
  public void testGetProjectSpaces() throws PortalException
  {
    navigation.init();
    final List<PortalSpace> projectSpaces = navigation.getProjectSpaces("monproject", Locale.ENGLISH);
    assertThat(projectSpaces, notNullValue());
    assertThat(projectSpaces.isEmpty(), is(false));
    assertThat(projectSpaces.size(), is(1));
    final PortalSpace portalSpace = projectSpaces.get(0);

    assertThat(portalSpace.getName(), notNullValue());
    assertThat(portalSpace.getName(), is(PortalConfigTest.PROJECT_TEST_SPACE_NAME));
    assertThat(portalSpace.getId(), notNullValue());
    assertThat(portalSpace.getId(), is(PortalConfigTest.PROJECT_TEST_SPACE_ID));
    assertThat(portalSpace.getDescription(), notNullValue());
    assertThat(portalSpace.getDescription(), is(PortalConfigTest.SPACE_DESCRIPTION));
    assertThat(portalSpace.getApplications().isEmpty(), is(true));

  }

  /**
   * Test method for
   * {@link PortalNavigationImpl#getApplicationUrl(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
   * .
   * 
   * @throws Exception
   */
  @Test
  public void testGetApplicationUrl() throws Exception
  {
    final NavigationToken tokenTemplate = mock(NavigationToken.class);
    when(tokenTemplate.resolved(Matchers.any(PortalStringTokenized.class))).thenReturn(
        PortalConfigTest.VIEW_ADMIN);
    navigation.setNavigationToken(tokenTemplate);

    final PortalURI applicationUrl = navigation.getApplicationURI(UUID.randomUUID().toString(), UUID
        .randomUUID().toString(), null, "monproject", PluginViewEnum.ADMINISTRATION.name(), Locale.ENGLISH);
    assertThat(applicationUrl, notNullValue());
    assertThat(applicationUrl.getAbsoluteURL().toExternalForm().startsWith(PortalConfigTest.VIEW_ADMIN),
        is(true));
  }

  /**
   * Test method for {@link PortalNavigationImpl#getAccountSpaces()}.
   * 
   * @throws PortalException
   */
  @Test
  public void testGetAccountApps() throws PortalException, URISyntaxException
  {
    navigation.init();
    final PortalSpace accountApps = navigation.getAccountSpaces(Locale.ENGLISH);
    assertThat(accountApps, notNullValue());
    assertThat(accountApps.getId(), is(PortalConfigTest.MY_ACCOUNT_SPACE_ID));
    assertThat(accountApps.getName(), is(PortalConfigTest.MY_ACCOUNT_SPACE_NAME));
    assertThat(accountApps.getDescription(), is(PortalConfigTest.SPACE_DESCRIPTION));
    assertThat(accountApps.getApplications().size(), is(2));

    for (final PortalApplication portalApplication : accountApps.getApplications())
    {
      assertThat(portalApplication.getUniqueId(), notNullValue());

      if (PortalConfigTest.MY_ACCOUNT_ID.equals(portalApplication.getId()))
      {

        assertThat(portalApplication.getName(), is(PortalConfigTest.MY_ACCOUNT_NAME));
        assertThat(portalApplication.getId(), is(PortalConfigTest.MY_ACCOUNT_ID));
        assertThat(
            portalApplication.getPortalURI().getAbsoluteURL().toString().startsWith(PortalConfigTest.APP_URL),
            is(true));
      }
      else if (PortalConfigTest.LOGOUT_ID.equals(portalApplication.getId()))
      {
        assertThat(portalApplication.getName(), is(PortalConfigTest.LOGOUT_NAME));
        assertThat(portalApplication.getId(), is(PortalConfigTest.LOGOUT_ID));
        assertThat(portalApplication.getPortalURI().isInternalModule(), is(true));

      }
      else
      {
        fail("invalid application id");
      }

    }

  }

}
