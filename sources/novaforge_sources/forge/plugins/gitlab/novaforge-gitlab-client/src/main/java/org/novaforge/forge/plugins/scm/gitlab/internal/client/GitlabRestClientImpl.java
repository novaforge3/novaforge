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
import org.gitlab.api.models.GitlabCommitDiff;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabGroupMember;
import org.gitlab.api.models.GitlabMergeRequest;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestClient;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestConnector;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used in order to instantiate new connector to jira web-service.
 * 
 * @author Gauthier Cart
 */
public class GitlabRestClientImpl implements GitlabRestClient
{

  /**
   * 
   */
  private static final String MASTER_BRANCH = "master";

  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabRestConnector getConnector(final String pBaseUrl, final String pToken)
      throws GitlabRestException
  {
    GitlabRestConnector connector;

    try
    {
      final GitlabAPI restClient = GitlabAPI.connect(pBaseUrl, pToken);
      connector = new GitlabRestConnectorImpl(restClient, pBaseUrl);
    }
    catch (final Exception e)
    {
      throw new GitlabRestException(String.format("Unable to get the GitLab Connector binding with [URL=%s]",
          pBaseUrl), e);
    }

    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabUser createUser(final GitlabRestConnector pConnector, final GitlabUser pGitlabUser,
      final String pPassword, final Integer pProjectsLimit) throws GitlabRestException
  {
    GitlabUser gitlabUser;
    try
    {
      gitlabUser = pConnector.getGitlabRestClient().createUser(pGitlabUser.getEmail(), pPassword,
          pGitlabUser.getUsername(), pGitlabUser.getName(), pGitlabUser.getSkype(),
          pGitlabUser.getLinkedin(), pGitlabUser.getTwitter(), pGitlabUser.getWebsiteUrl(), pProjectsLimit,
          pGitlabUser.getExternUid(), pGitlabUser.getExternProviderName(), pGitlabUser.getBio(),
          pGitlabUser.isAdmin(), pGitlabUser.isCanCreateGroup(), true);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format(
          "Unable to create user on GitLab with [Email=%s, Username=%s, Name=%s]", pGitlabUser.getEmail(),
          pGitlabUser.getUsername(), pGitlabUser.getName()), e);
    }
    return gitlabUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabUser getUserViaSudo(final GitlabRestConnector pConnector, final String pUsername)
      throws GitlabRestException
  {
    GitlabUser gitlabUser;
    try
    {
      gitlabUser = pConnector.getGitlabRestClient().getUserViaSudo(pUsername);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format("Unable to get user on GitLab with [Username=%s]",
          pUsername), e);
    }
    return gitlabUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteUser(final GitlabRestConnector pConnector, final Integer pUserId)
      throws GitlabRestException
  {
    try
    {
      pConnector.getGitlabRestClient().deleteUser(pUserId);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format("Unable to delete user on GitLab with [User Id=%s]",
          pUserId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabUser updateUser(final GitlabRestConnector pConnector, final Integer pUserId,
      final GitlabUser pGitlabUser, final String pPassword, final Integer pProjectsLimit)
      throws GitlabRestException
  {
    GitlabUser gitlabUser;
    try
    {
      gitlabUser = pConnector.getGitlabRestClient().updateUser(pUserId, pGitlabUser.getEmail(), pPassword,
          pGitlabUser.getUsername(), pGitlabUser.getName(), pGitlabUser.getSkype(),
          pGitlabUser.getLinkedin(), pGitlabUser.getTwitter(), pGitlabUser.getWebsiteUrl(), pProjectsLimit,
          pGitlabUser.getExternUid(), pGitlabUser.getExternProviderName(), pGitlabUser.getBio(),
          pGitlabUser.isAdmin(), pGitlabUser.isCanCreateGroup(), true);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format(
          "Unable to update user on GitLab with [User Id=%s, Email=%s, Username=%s, Name=%s]", pUserId,
          pGitlabUser.getEmail(), pGitlabUser.getUsername(), pGitlabUser.getName()), e);
    }

    return gitlabUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabGroup createGroup(final GitlabRestConnector pConnector, final GitlabGroup pGitlabGroup)
      throws GitlabRestException
  {
    GitlabGroup gitlabGroup;
    try
    {
      gitlabGroup = pConnector.getGitlabRestClient().createGroup(pGitlabGroup.getName(),
          pGitlabGroup.getPath());
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format(
          "Unable to create group on GitLab with [Group name=%s, Group path=%s]", pGitlabGroup.getName(),
          pGitlabGroup.getPath()), e);
    }

    return gitlabGroup;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteGroup(final GitlabRestConnector pConnector, final Integer pGroupId) throws GitlabRestException
  {
    try
    {
      pConnector.getGitlabRestClient().deleteGroup(pGroupId);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format("Unable to delete group on GitLab with [Group Id=%s]", pGroupId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GitlabGroup> getGroups(final GitlabRestConnector pConnector) throws GitlabRestException
  {
    List<GitlabGroup> gitlabGroup = new ArrayList<>();
    try
    {
      gitlabGroup = pConnector.getGitlabRestClient().getGroups();
    }
    catch (final IOException e)
    {
      throw new GitlabRestException("Unable to get the list of groups on GitLab", e);
    }
    return gitlabGroup;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabProject createProject(final GitlabRestConnector pConnector, final GitlabProject pGitlabProject)
      throws GitlabRestException
  {
    GitlabProject gitlabProject;
    try
    {
      gitlabProject = pConnector.getGitlabRestClient().createProject(pGitlabProject.getName(),
          pGitlabProject.getNamespace().getId(), pGitlabProject.getDescription(),
          pGitlabProject.isIssuesEnabled(), pGitlabProject.isWallEnabled(),
          pGitlabProject.isMergeRequestsEnabled(), pGitlabProject.isWikiEnabled(),
          pGitlabProject.isSnippetsEnabled(), pGitlabProject.isPublic(), pGitlabProject.getVisibilityLevel(),
          null);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format(
          "Unable to create project on GitLab with [Project name=%s, Namespace Id path=%s]",
          pGitlabProject.getName(), pGitlabProject.getNamespace()), e);
    }

    return gitlabProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GitlabProject> getProjects(final GitlabRestConnector pConnector) throws GitlabRestException
  {
    List<GitlabProject> gitlabProjects = new ArrayList<>();
    try
    {
      gitlabProjects = pConnector.getGitlabRestClient().getAllProjects();
    }
    catch (final IOException e)
    {
      throw new GitlabRestException("Unable to get all projects on GitLab", e);
    }

    return gitlabProjects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteProject(final GitlabRestConnector pConnector, final String pProjectId)
      throws GitlabRestException
  {
    try
    {
      pConnector.getGitlabRestClient().deleteProject(pProjectId);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format("Unable to delete project on GitLab with [Project Id=%s]",
          pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabGroupMember addGroupMember(final GitlabRestConnector pConnector, final Integer pGroupId,
      final Integer pUserId, final GitlabAccessLevel pAccessLevel) throws GitlabRestException
  {
    GitlabGroupMember gitlabGroupMember = new GitlabGroupMember();
    try
    {
      gitlabGroupMember = pConnector.getGitlabRestClient().addGroupMember(pGroupId, pUserId, pAccessLevel);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format(
          "Unable to add group member project on GitLab with [Group Id=%s, User Id=%s, Access Level=%s]",
          pGroupId, pUserId, pAccessLevel.name()), e);
    }

    return gitlabGroupMember;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GitlabGroupMember> getGroupMembers(final GitlabRestConnector pConnector, final Integer pGroupId)
      throws GitlabRestException
  {
    List<GitlabGroupMember> gitlabGroupMembers = new ArrayList<>();
    try
    {
      gitlabGroupMembers = pConnector.getGitlabRestClient().getGroupMembers(pGroupId);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format(
          "Unable to add group member project on GitLab with [Group Id=%s]", pGroupId), e);
    }

    return gitlabGroupMembers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteGroupMember(final GitlabRestConnector pConnector, final Integer pGroupId,
      final Integer pUserId) throws GitlabRestException
  {
    try
    {
      pConnector.getGitlabRestClient().deleteGroupMember(pGroupId, pUserId);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format(
          "Unable to add group member project on GitLab with [Group Id=%s, User Id=%s]", pGroupId, pUserId),
          e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabProject getProject(final GitlabRestConnector pConnector, final String pProjectId)
      throws GitlabRestException
  {
    GitlabProject gitlabProject;
    try
    {
      gitlabProject = pConnector.getGitlabRestClient().getProject(pProjectId);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format("Unable to get project on GitLab with [id=%s]", pProjectId), e);
    }

    return gitlabProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GitlabGroup getGroup(final GitlabRestConnector pConnector, final int pGroupId) throws GitlabRestException
  {
    GitlabGroup gitlabGroup;
    try
    {
      gitlabGroup = pConnector.getGitlabRestClient().getGroup(pGroupId);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format("Unable to get group on GitLab with [id=%s]", pGroupId), e);
    }

    return gitlabGroup;
  }

  /**
   * {@inheritDoc}
   * 
   * @return
   */
  @Override
  public List<GitlabCommit> getCommits(final GitlabRestConnector pConnector, final int pProjectId,
      final String pBranch) throws GitlabRestException
  {
    try
    {
      final GitlabMergeRequest gitlabMergeRequest = new GitlabMergeRequest();
      gitlabMergeRequest.setSourceProjectId(pProjectId);

      if ((pBranch == null) || ("".equals(pBranch)))
      {
        gitlabMergeRequest.setSourceBranch(MASTER_BRANCH);
      }
      else
      {
        gitlabMergeRequest.setSourceBranch(pBranch);
      }

      return pConnector.getGitlabRestClient().getCommits(gitlabMergeRequest);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format(
          "Unable to get project commits on GitLab with [Project Id=%s]", pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @return
   */
  @Override
  public List<GitlabCommitDiff> getCommitDiffs(final GitlabRestConnector pConnector, final int pProjectId,
      final String pCommitHash) throws GitlabRestException
  {

    try
    {
      return pConnector.getGitlabRestClient().getCommitDiffs(String.valueOf(pProjectId), pCommitHash);
    }
    catch (final IOException e)
    {
      throw new GitlabRestException(String.format(
          "Unable to get project commit diffs on GitLab with [project Id=%s, commit_hash=%s]", pProjectId,
          pCommitHash), e);
    }
  }
}