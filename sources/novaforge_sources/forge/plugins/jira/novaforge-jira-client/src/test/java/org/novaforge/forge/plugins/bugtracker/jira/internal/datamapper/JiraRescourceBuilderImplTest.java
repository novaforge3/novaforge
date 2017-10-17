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
package org.novaforge.forge.plugins.bugtracker.jira.internal.datamapper;

import org.hamcrest.CoreMatchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Matchers;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapException;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemotePermissionScheme;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProject;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteScheme;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteUser;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gauthier Cart
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JiraRescourceBuilderImplTest
{

  private static final String NAME                 = "My Projet";
  private static final String DESCRIPTION          = "My description";
  private static final String LISCENSE             = "My liscense";
  private static final String STATUS               = "Activated";
  private static final String AUTHOR               = "cart-g";
  private static final String PROJECT_ID           = "12";

  private static final String USER_EMAIL           = "bruno.macherel@.test.bull.net";
  private static final String USER_FIRSTNAME       = "Bruno";
  private static final String USER_LANGUAGE        = "EN";
  private static final String USER_LOGIN           = "macherel-b";
  private static final String USER_NAME            = "Macherel";

  private static final String INSTANCE_NAME        = "TEST";
  private static final String KEY                  = "TST";

  private boolean             jiraProfileActivated = false;

  public JiraRescourceBuilderImplTest()
  {
    final String property = System.getProperty("jira.profile");
    if ("true".equals(property))
    {
      jiraProfileActivated = true;
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.datamapper.JiraResourceBuilderImpl#buildRemoteUser(org.novaforge.forge.core.plugins.domain.plugin.PluginUser)}
   * .
   */
  @Test
  public void test01BuildRemoteUser()
  {
    if (jiraProfileActivated)
    {

      final JiraResourceBuilderImpl jiraRescourceBuilder = new JiraResourceBuilderImpl();

      final PluginUser pluginUser = mock(PluginUser.class);
      when(pluginUser.getEmail()).thenReturn(USER_EMAIL);
      when(pluginUser.getFirstName()).thenReturn(USER_FIRSTNAME);
      when(pluginUser.getLanguage()).thenReturn(USER_LANGUAGE);
      when(pluginUser.getLogin()).thenReturn(USER_LOGIN);
      when(pluginUser.getName()).thenReturn(USER_NAME);

      final RemoteUser remoteUser = jiraRescourceBuilder.buildRemoteUser(pluginUser);

      assertThat(remoteUser, notNullValue());
      assertThat(remoteUser.getEmail(), CoreMatchers.is(USER_EMAIL));
      assertThat(remoteUser.getName(), CoreMatchers.is(USER_LOGIN));
      assertThat(remoteUser.getFullname(), notNullValue());
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.datamapper.JiraResourceBuilderImpl#buildRemoteProject(org.novaforge.forge.core.plugins.domain.plugin.PluginProject, java.lang.String, java.lang.String, org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector)}
   * .
   * 
   * @throws JiraSoapException
   */
  @Test
  public void test02BuildRemoteProject() throws JiraSoapException
  {
    if (jiraProfileActivated)
    {

      final JiraResourceBuilderImpl jiraRescourceBuilder = new JiraResourceBuilderImpl();

      final JiraSoapClient jiraSoapClient = mock(JiraSoapClient.class);
      final RemoteScheme remoteScheme = mock(RemoteScheme.class);
      final RemotePermissionScheme remotePermissionScheme = mock(RemotePermissionScheme.class);
      when(
          jiraSoapClient.getNotificationSchemeById(Matchers.any(JiraSoapConnector.class), Matchers.anyLong()))
          .thenReturn(remoteScheme);
      when(jiraSoapClient.getSecuritySchemeById(Matchers.any(JiraSoapConnector.class), Matchers.anyLong()))
          .thenReturn(remoteScheme);

      when(jiraSoapClient.getPermissionSchemeById(Matchers.any(JiraSoapConnector.class), Matchers.anyLong()))
          .thenReturn(remotePermissionScheme);
      jiraRescourceBuilder.setJiraSoapClient(jiraSoapClient);

      final PluginProject pluginProject = mock(PluginProject.class);
      when(pluginProject.getAuthor()).thenReturn(AUTHOR);
      when(pluginProject.getDescription()).thenReturn(DESCRIPTION);
      when(pluginProject.getLicense()).thenReturn(LISCENSE);
      when(pluginProject.getProjectId()).thenReturn(PROJECT_ID);
      when(pluginProject.getName()).thenReturn(NAME);
      when(pluginProject.getStatus()).thenReturn(STATUS);

      final RemoteProject remoteProject = jiraRescourceBuilder.buildRemoteProject(pluginProject,
          INSTANCE_NAME, KEY, mock(JiraSoapConnector.class));

      assertThat(remoteProject, notNullValue());
      assertThat(remoteProject.getName(), notNullValue());
      assertThat(remoteProject.getKey(), CoreMatchers.is(KEY));
      assertThat(remoteProject.getUrl(), nullValue());
      assertThat(remoteProject.getProjectUrl(), nullValue());
      assertThat(remoteProject.getLead(), CoreMatchers.is(AUTHOR));
      assertThat(remoteProject.getIssueSecurityScheme(), CoreMatchers.is(remoteScheme));
      assertThat(remoteProject.getNotificationScheme(), CoreMatchers.is(remoteScheme));
      assertThat(remoteProject.getPermissionScheme(), CoreMatchers.is(remotePermissionScheme));
    }
  }

}
