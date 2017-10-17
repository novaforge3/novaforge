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
package org.novaforge.forge.plugins.scm.gitlab.internal.client;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabGroupMember;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.hamcrest.CoreMatchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestClient;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestConnector;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestException;

import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Gauthier Cart
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GitlabRestClientImplTest
{
  private static final String  BASE_URL                  = "http://maforge-gla/gitlab-default/gitlab";
  private static final String  TOKEN                     = "cg1ke_c1_8szkcSLNqbz";

  private static final Boolean USER_IS_ADMIN             = true;
  private static final String  USER_E_MAIL               = "";
  private static final String  USER_NAME                 = "Gauthier CART";
  private static final String  USER_PASSWORD             = "novaforge_1";
  private static final String  USER_USERNAME             = "cart-g";
  private static final Integer USER_PROJECTS_LIMIT       = 0;

  private static final String  USER_E_MAIL_ALT           = ".net";
  private static final String  USER_NAME_ALT             = "Gauthier CART Alternatif";
  private static final String  USER_PASSWORD_ALT         = "totopouet!38";
  private static final String  USER_USERNAME_ALT         = "cart-ga";

  private static final String  GROUP_NAME                = "Mon groupe";
  private static final String  GROUP_PATH                = "mon-groupe";

  private static final String  PROJECT_NAME              = "Mon projetgca";
  private static final String  PROJECT_DESCRIPTION       = "Mon projetgca description";
  private static final boolean IS_ISSUE_ENABLED          = false;
  private static final boolean IS_WALL_ENABLED           = false;
  private static final boolean IS_MERGE_REQUESTS_ENABLED = false;
  private static final boolean IS_WIKI_ENABLED           = false;
  private static final boolean IS_SNIPPETS_ENABLED       = false;
  private static final boolean IS_PUBLIC                 = false;
  /*
   * PRIVATE = 0 unless const_defined?(:PRIVATE)
   * INTERNAL = 10 unless const_defined?(:INTERNAL)
   * PUBLIC = 20 unless const_defined?(:PUBLIC)
   */
  private static final Integer VISIBILITY_LEVEL          = 10;

  private boolean              gitlabProfileActivated    = false;

  public GitlabRestClientImplTest()
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
  public void test00GetConnector()
  {
    if (gitlabProfileActivated)
    {
      final GitlabAPI restClient = GitlabAPI.connect(BASE_URL, TOKEN);
      final GitlabRestConnector connector = new GitlabRestConnectorImpl(restClient, BASE_URL);
      assertThat(connector, notNullValue());
      assertThat(connector.getGitlabRestClient(), notNullValue());
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test01CreateUser() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final GitlabUser gitlabUser = new GitlabUser();
      gitlabUser.setAdmin(USER_IS_ADMIN);
      gitlabUser.setEmail(USER_E_MAIL);
      gitlabUser.setName(USER_NAME);
      gitlabUser.setUsername(USER_USERNAME);

      final GitlabUser createdGitlabUser = gitlabRestClient.createUser(connector, gitlabUser, USER_PASSWORD,
          USER_PROJECTS_LIMIT);

      assertThat(createdGitlabUser, notNullValue());
      assertThat(createdGitlabUser.getName(), CoreMatchers.is(USER_NAME));
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test02GetUserViaSudo() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final GitlabUser gitlabUser = gitlabRestClient.getUserViaSudo(connector, USER_USERNAME);

      assertThat(gitlabUser, notNullValue());
      assertThat(gitlabUser.getName(), CoreMatchers.is(USER_NAME));
      assertThat(gitlabUser.getEmail(), CoreMatchers.is(USER_E_MAIL));
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test03UpdateUser() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final GitlabUser gitlabUser = gitlabRestClient.getUserViaSudo(connector, USER_USERNAME);
      gitlabUser.setEmail(USER_E_MAIL_ALT);
      gitlabUser.setName(USER_NAME_ALT);
      gitlabUser.setUsername(USER_USERNAME_ALT);

      final GitlabUser updatedGitlabUser = gitlabRestClient.updateUser(connector, gitlabUser.getId(),
          gitlabUser, USER_PASSWORD_ALT, USER_PROJECTS_LIMIT);

      assertThat(updatedGitlabUser, notNullValue());
      assertThat(updatedGitlabUser.getName(), CoreMatchers.is(USER_NAME_ALT));
      assertThat(updatedGitlabUser.getEmail(), CoreMatchers.is(USER_E_MAIL_ALT));
      assertThat(updatedGitlabUser.getUsername(), CoreMatchers.is(USER_USERNAME_ALT));
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test04CreateGroup() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final GitlabGroup gitlabGroup = new GitlabGroup();
      gitlabGroup.setName(GROUP_NAME);
      gitlabGroup.setPath(GROUP_PATH);

      final GitlabGroup createdGitlabGroup = gitlabRestClient.createGroup(connector, gitlabGroup);

      assertThat(createdGitlabGroup, notNullValue());
      assertThat(createdGitlabGroup.getName(), CoreMatchers.is(GROUP_NAME));
      assertThat(createdGitlabGroup.getPath(), CoreMatchers.is(GROUP_PATH));
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test05getGroups() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final List<GitlabGroup> gitlabGroups = gitlabRestClient.getGroups(connector);

      boolean assertion = false;
      for (final GitlabGroup entryGitlabGroup : gitlabGroups)
      {
        if (GROUP_NAME.equals(entryGitlabGroup.getName()))
        {
          assertion = true;
        }
      }
      if (!assertion)
      {
        fail();
      }
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test06CreateGroupMember() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final List<GitlabGroup> gitlabGroups = gitlabRestClient.getGroups(connector);

      GitlabGroup gitlabGroup = new GitlabGroup();
      for (final GitlabGroup entryGitlabGroup : gitlabGroups)
      {
        if (GROUP_NAME.equals(entryGitlabGroup.getName()))
        {
          gitlabGroup = entryGitlabGroup;
        }
      }
      final GitlabUser gitlabUser = gitlabRestClient.getUserViaSudo(connector, USER_USERNAME_ALT);

      final GitlabGroupMember gitlabGroupMember = gitlabRestClient.addGroupMember(connector,
          gitlabGroup.getId(), gitlabUser.getId(), GitlabAccessLevel.Developer);

      assertThat(gitlabGroupMember, notNullValue());
      assertThat(gitlabGroupMember.getUsername(), CoreMatchers.is(USER_USERNAME_ALT));
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test07GetGroupMember() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final List<GitlabGroup> gitlabGroups = gitlabRestClient.getGroups(connector);

      GitlabGroup gitlabGroup = new GitlabGroup();
      for (final GitlabGroup entryGitlabGroup : gitlabGroups)
      {
        if (GROUP_NAME.equals(entryGitlabGroup.getName()))
        {
          gitlabGroup = entryGitlabGroup;
        }
      }

      final List<GitlabGroupMember> gitlabGroupMembers = gitlabRestClient.getGroupMembers(connector,
          gitlabGroup.getId());

      boolean assertion = false;
      for (final GitlabGroupMember entryGitlabGroupMember : gitlabGroupMembers)
      {
        if (USER_USERNAME_ALT.equals(entryGitlabGroupMember.getUsername()))
        {
          assertion = true;
        }
      }
      if (!assertion)
      {
        fail();
      }
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test08CreateProject() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final List<GitlabGroup> gitlabGroups = gitlabRestClient.getGroups(connector);

      GitlabGroup gitlabGroup = new GitlabGroup();
      for (final GitlabGroup entryGitlabGroup : gitlabGroups)
      {
        if (GROUP_NAME.equals(entryGitlabGroup.getName()))
        {
          gitlabGroup = entryGitlabGroup;
        }
      }

      final GitlabNamespace gitlabNamespace = new GitlabNamespace();
      gitlabNamespace.setId(gitlabGroup.getId());

      final GitlabProject gitlabProject = new GitlabProject();
      gitlabProject.setName(PROJECT_NAME);
      gitlabProject.setNamespace(gitlabNamespace);
      gitlabProject.setDescription(PROJECT_DESCRIPTION);
      gitlabProject.setIssuesEnabled(IS_ISSUE_ENABLED);
      gitlabProject.setWallEnabled(IS_WALL_ENABLED);
      gitlabProject.setWikiEnabled(IS_WIKI_ENABLED);
      gitlabProject.setMergeRequestsEnabled(IS_MERGE_REQUESTS_ENABLED);
      gitlabProject.setSnippetsEnabled(IS_SNIPPETS_ENABLED);
      gitlabProject.setPublic(IS_PUBLIC);
      gitlabProject.setVisibilityLevel(VISIBILITY_LEVEL);

      final GitlabProject createdGitlabProject = gitlabRestClient.createProject(connector, gitlabProject);

      assertThat(createdGitlabProject, notNullValue());
      assertThat(createdGitlabProject.getName(), CoreMatchers.is(PROJECT_NAME));
      assertThat(createdGitlabProject.getNamespace().getPath(), CoreMatchers.is(GROUP_PATH));
      assertThat(createdGitlabProject.getDescription(), CoreMatchers.is(PROJECT_DESCRIPTION));
      assertThat(createdGitlabProject.isIssuesEnabled(), CoreMatchers.is(IS_ISSUE_ENABLED));
      assertThat(createdGitlabProject.isWallEnabled(), CoreMatchers.is(IS_WALL_ENABLED));
      assertThat(createdGitlabProject.isWikiEnabled(), CoreMatchers.is(IS_WIKI_ENABLED));
      assertThat(createdGitlabProject.isMergeRequestsEnabled(), CoreMatchers.is(IS_MERGE_REQUESTS_ENABLED));
      assertThat(createdGitlabProject.isSnippetsEnabled(), CoreMatchers.is(IS_SNIPPETS_ENABLED));
      assertThat(createdGitlabProject.isPublic(), CoreMatchers.is(IS_PUBLIC));
      assertThat(createdGitlabProject.getVisibilityLevel(), CoreMatchers.is(VISIBILITY_LEVEL));
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test09getProjects() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final List<GitlabProject> gitlabProjects = gitlabRestClient.getProjects(connector);

      boolean assertion = false;
      for (final GitlabProject entryGitlabProject : gitlabProjects)
      {
        if (PROJECT_NAME.equals(entryGitlabProject.getName()))
        {
          assertion = true;
        }
      }
      if (!assertion)
      {
        fail();
      }
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test10deleteProject() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      List<GitlabProject> gitlabProjects = gitlabRestClient.getProjects(connector);

      boolean assertion = false;
      for (final GitlabProject entryGitlabProject : gitlabProjects)
      {
        if (PROJECT_NAME.equals(entryGitlabProject.getName()))
        {
          assertion = true;
          gitlabRestClient.deleteProject(connector, String.valueOf(entryGitlabProject.getId()));
        }
      }
      if (!assertion)
      {
        fail();
      }

      gitlabProjects = gitlabRestClient.getProjects(connector);
      for (final GitlabProject entryGitlabProject : gitlabProjects)
      {
        if (GROUP_NAME.equals(entryGitlabProject.getName()))
        {
          fail("Project is still existing");
        }
      }
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test11deleteGroupMember() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final List<GitlabGroup> gitlabGroups = gitlabRestClient.getGroups(connector);

      GitlabGroup gitlabGroup = new GitlabGroup();
      for (final GitlabGroup entryGitlabGroup : gitlabGroups)
      {
        if (GROUP_NAME.equals(entryGitlabGroup.getName()))
        {
          gitlabGroup = entryGitlabGroup;
        }
      }
      final GitlabUser gitlabUser = gitlabRestClient.getUserViaSudo(connector, USER_USERNAME_ALT);

      gitlabRestClient.deleteGroupMember(connector, gitlabGroup.getId(), gitlabUser.getId());

      final List<GitlabGroupMember> gitlabGroupMembers = gitlabRestClient.getGroupMembers(connector,
          gitlabGroup.getId());

      for (final GitlabGroupMember entryGitlabGroupMember : gitlabGroupMembers)
      {
        if (USER_USERNAME_ALT.equals(entryGitlabGroupMember.getUsername()))
        {
          fail("Group Member is still existing");
        }
      }
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test12deleteGroup() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      List<GitlabGroup> gitlabGroups = gitlabRestClient.getGroups(connector);

      boolean assertion = false;
      for (final GitlabGroup entryGitlabGroup : gitlabGroups)
      {
        if (GROUP_NAME.equals(entryGitlabGroup.getName()))
        {
          assertion = true;
          gitlabRestClient.deleteGroup(connector, entryGitlabGroup.getId());
        }
      }
      if (!assertion)
      {
        fail();
      }

      gitlabGroups = gitlabRestClient.getGroups(connector);
      for (final GitlabGroup entryGitlabGroup : gitlabGroups)
      {
        if (GROUP_NAME.equals(entryGitlabGroup.getName()))
        {
          fail("Group is still existing");
        }
      }
    }
  }

  /**
   * @throws GitlabRestException
   */
  @Test
  public void test13DeleteUser() throws GitlabRestException
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);

      final GitlabUser gitlabUser = gitlabRestClient.getUserViaSudo(connector, USER_USERNAME_ALT);

      gitlabRestClient.deleteUser(connector, gitlabUser.getId());

      try
      {
        gitlabRestClient.getUserViaSudo(connector, USER_USERNAME_ALT);
        fail("User is still existing");
      }
      catch (final GitlabRestException e)
      {
        if (!(e.getCause() instanceof FileNotFoundException))
        {
          throw e;
        }
      }
    }
  }

  @Test
  public void testGetCommits() throws Exception
  {
    if (gitlabProfileActivated)
    {

      final GitlabRestClient gitlabRestClient = new GitlabRestClientImpl();

      final GitlabRestConnector connector = gitlabRestClient.getConnector(BASE_URL, TOKEN);
      final List<GitlabCommit> commits = gitlabRestClient.getCommits(connector, 2, null);
      for (final GitlabCommit gitlabCommit : commits)
      {
        System.out.println(gitlabCommit.getTitle());
      }

    }
  }
}
