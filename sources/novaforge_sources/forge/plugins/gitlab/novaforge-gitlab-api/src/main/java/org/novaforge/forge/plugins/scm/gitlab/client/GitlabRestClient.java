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
package org.novaforge.forge.plugins.scm.gitlab.client;

import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabCommitDiff;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabGroupMember;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;

import java.util.List;

/**
 * @author Gauthier Cart
 */
public interface GitlabRestClient
{

  /**
   * @param pBaseUrl
   * @param pToken
   * @return
   * @throws GitlabRestException
   */
  GitlabRestConnector getConnector(String pBaseUrl, String pToken) throws GitlabRestException;

  /**
   * @param pConnector
   * @param pGitlabUser
   * @param pPassword
   * @param pIsAdmin
   * @return
   * @throws GitlabRestException
   */
  GitlabUser createUser(final GitlabRestConnector pConnector, final GitlabUser pGitlabUser,
      final String pPassword, final Integer pProjectsLimit) throws GitlabRestException;

  /**
   * @param pConnector
   * @param pUsername
   * @return
   * @throws GitlabRestException
   */
  GitlabUser getUserViaSudo(final GitlabRestConnector pConnector, final String pUsername)
      throws GitlabRestException;

  /**
   * @param pConnector
   * @param pUserId
   * @throws GitlabRestException
   */
  void deleteUser(final GitlabRestConnector pConnector, final Integer pUserId) throws GitlabRestException;

  /**
   * @param pConnector
   * @param pUserId
   * @param pGitlabUser
   * @param pPassword
   * @param pProjectsLimit
   * @throws GitlabRestException
   */
  GitlabUser updateUser(final GitlabRestConnector pConnector, final Integer pUserId,
      final GitlabUser pGitlabUser, final String pPassword, Integer pProjectsLimit)
      throws GitlabRestException;

  /**
   * @param pConnector
   * @param pGitlabGroup
   * @return
   * @throws GitlabRestException
   */
  GitlabGroup createGroup(GitlabRestConnector pConnector, GitlabGroup pGitlabGroup)
      throws GitlabRestException;

  /**
   * @param pConnector
   * @param pGroupId
   * @return
   * @throws GitlabRestException
   */
  void deleteGroup(GitlabRestConnector pConnector, Integer pGroupId) throws GitlabRestException;

  /**
   * @param pConnector
   * @return
   * @throws GitlabRestException
   */
  List<GitlabGroup> getGroups(GitlabRestConnector pConnector) throws GitlabRestException;

  /**
   * @param pConnector
   * @param pGitlabProject
   * @return
   * @throws GitlabRestException
   */
  GitlabProject createProject(GitlabRestConnector pConnector, GitlabProject pGitlabProject)
      throws GitlabRestException;

  /**
   * @param pConnector
   * @return
   * @throws GitlabRestException
   */
  List<GitlabProject> getProjects(GitlabRestConnector pConnector) throws GitlabRestException;

  /**
   * @param pConnector
   * @param pProjectId
   * @throws GitlabRestException
   */
  void deleteProject(GitlabRestConnector pConnector, String pProjectId) throws GitlabRestException;

  /**
   * @param pConnector
   * @param pGroupId
   * @param pUserId
   * @param pAccessLevel
   * @return
   * @throws GitlabRestException
   */
  GitlabGroupMember addGroupMember(GitlabRestConnector pConnector, Integer pGroupId, Integer pUserId,
      GitlabAccessLevel pAccessLevel) throws GitlabRestException;

  /**
   * @param pConnector
   * @param pGitlabGroup
   * @return
   * @throws GitlabRestException
   */
  List<GitlabGroupMember> getGroupMembers(GitlabRestConnector pConnector, Integer pGroupId)
      throws GitlabRestException;

  /**
   * @param pConnector
   * @param pGroupId
   * @param pUserId
   * @throws GitlabRestException
   */
  void deleteGroupMember(GitlabRestConnector pConnector, Integer pGroupId, Integer pUserId)
      throws GitlabRestException;

  /**
   * @param pConnector
   * @param pProjectId
   * @return
   * @throws GitlabRestException
   */
  GitlabProject getProject(GitlabRestConnector pConnector, String pProjectId) throws GitlabRestException;

  /**
   * @param pConnector
   * @param pGroupId
   * @return
   * @throws GitlabRestException
   */
  GitlabGroup getGroup(GitlabRestConnector pConnector, int pGroupId) throws GitlabRestException;

  /**
   * @param pConnector
   * @param pProjectId
   * @param pBranch
   *          if <code>null</code>, master branch will be used
   * @return
   * @throws GitlabRestException
   */
  List<GitlabCommit> getCommits(final GitlabRestConnector pConnector, final int pProjectId,
      final String pBranch) throws GitlabRestException;

  /**
   * @param pConnector
   * @param pProjectId
   * @param pCommitHash
   * @return
   * @throws GitlabRestException
   */
  List<GitlabCommitDiff> getCommitDiffs(GitlabRestConnector pConnector, int pProjectId, String pCommitHash)
      throws GitlabRestException;
}
