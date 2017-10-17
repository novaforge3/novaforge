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
package org.novaforge.forge.plugins.scm.gitlab.internal.datamapper;

import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.hamcrest.CoreMatchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gauthier Cart
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GitlabRescourceBuilderImplTest
{

  private static final String  USER_EMAIL             = ".net";
  private static final String  USER_FIRSTNAME         = "Gauthier";
  private static final String  USER_NAME              = "CART";
  private static final String  USER_FULLNAME          = "CART Gauthier";
  private static final String  USER_LANGUAGE          = "EN";
  private static final String  USER_LOGIN             = "cart-g";

  private static final String  NAME                   = "Mon Projet GCA";
  private static final String  DESCRIPTION            = "Mon Projet GCA";
  private static final String  LISCENSE               = "My liscense";
  private static final String  STATUS                 = "Activated";
  private static final String  AUTHOR                 = "cart-g";
  private static final String  PROJECT_ID             = "monprojetgca";

  private static final String  INSTANCE_ID            = "gitlab";

  private static final String  GROUP_NAME             = "monprojetgca-gitlab";
  private static final Integer GROUP_ID               = 1;

  private boolean              gitlabProfileActivated = false;

  public GitlabRescourceBuilderImplTest()
  {
    final String property = System.getProperty("gitlab.profile");
    if ("true".equals(property))
    {
      gitlabProfileActivated = true;
    }
  }

  /**
   *
   */
  @Test
  public void test01BuildGitlabUser()
  {
    if (gitlabProfileActivated)
    {
      final GitlabResourceBuilderImpl gitlabRescourceBuilder = new GitlabResourceBuilderImpl();

      final PluginUser pluginUser = mock(PluginUser.class);
      when(pluginUser.getEmail()).thenReturn(USER_EMAIL);
      when(pluginUser.getFirstName()).thenReturn(USER_FIRSTNAME);
      when(pluginUser.getLanguage()).thenReturn(USER_LANGUAGE);
      when(pluginUser.getLogin()).thenReturn(USER_LOGIN);
      when(pluginUser.getName()).thenReturn(USER_NAME);

      final GitlabUser gitlabUser = gitlabRescourceBuilder.buildGitlabUser(pluginUser);

      assertThat(gitlabUser, notNullValue());
      assertThat(gitlabUser.getEmail(), CoreMatchers.is(USER_EMAIL));
      assertThat(gitlabUser.getUsername(), CoreMatchers.is(USER_LOGIN));
      assertThat(gitlabUser.getName(), notNullValue());
    }
  }

  /**
   * 
   */
  @Test
  public void test02BuildbuildGitlabGroup()
  {
    if (gitlabProfileActivated)
    {
      final GitlabResourceBuilderImpl gitlabRescourceBuilder = new GitlabResourceBuilderImpl();

      final GitlabGroup gitlabGroup = gitlabRescourceBuilder.buildGitlabGroup(PROJECT_ID, INSTANCE_ID);

      assertThat(gitlabGroup.getName(), CoreMatchers.is(GROUP_NAME));
      assertThat(gitlabGroup.getPath(), CoreMatchers.is(GROUP_NAME));
    }
  }

  /**
   * 
   */
  @Test
  public void test03BuildGitlabProject()
  {
    if (gitlabProfileActivated)
    {
      final GitlabResourceBuilderImpl gitlabRescourceBuilder = new GitlabResourceBuilderImpl();

      final GitlabUser gitlabUser = new GitlabUser();
      gitlabUser.setEmail(USER_EMAIL);
      gitlabUser.setUsername(USER_LOGIN);
      gitlabUser.setName(USER_FULLNAME);

      final GitlabGroup gitlabGroup = new GitlabGroup();
      gitlabGroup.setId(GROUP_ID);
      gitlabGroup.setName(GROUP_NAME);
      gitlabGroup.setPath(GROUP_NAME);

      final GitlabNamespace gitlabNamespace = new GitlabNamespace();
      gitlabNamespace.setId(gitlabGroup.getId());

      final PluginProject pluginProject = mock(PluginProject.class);
      when(pluginProject.getAuthor()).thenReturn(AUTHOR);
      when(pluginProject.getDescription()).thenReturn(DESCRIPTION);
      when(pluginProject.getLicense()).thenReturn(LISCENSE);
      when(pluginProject.getProjectId()).thenReturn(PROJECT_ID);
      when(pluginProject.getName()).thenReturn(NAME);
      when(pluginProject.getStatus()).thenReturn(STATUS);

      final GitlabProject gitlabProject = gitlabRescourceBuilder.buildGitlabProject(pluginProject,
          INSTANCE_ID, gitlabUser, gitlabNamespace);

      assertThat(gitlabProject, notNullValue());
      assertThat(gitlabProject.getName(), CoreMatchers.is(INSTANCE_ID));
      assertThat(gitlabProject.getOwner().getUsername(), CoreMatchers.is(USER_LOGIN));
    }
  }
}
