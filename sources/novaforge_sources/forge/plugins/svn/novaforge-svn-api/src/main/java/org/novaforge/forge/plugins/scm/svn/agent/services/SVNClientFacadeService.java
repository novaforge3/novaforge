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

package org.novaforge.forge.plugins.scm.svn.agent.services;

import org.novaforge.forge.plugins.scm.svn.agent.dto.MembershipDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNNodeEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNSearchResultDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.ScmLogEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.UserDTO;
import org.novaforge.forge.plugins.scm.svn.domain.Role;

import javax.activation.DataHandler;
import java.util.List;

/**
 * Client facade service used to communicate with a svn facade webservice exposed by the SVN agent
 *
 * @author Guillaume Lamirand
 */
public interface SVNClientFacadeService
{
  /**
   * This method will return the correct {@link SVNFacadeService} according to the URL requested
   *
   * @param pBaseUrl
   *          svn agent base url, ie http://localhost:9000
   * @return {@link SVNFacadeService} build
   */
  SVNFacadeService getSVNFacadeService(final String pBaseUrl);

  /**
   * Retreives the (pNbComit) last commits from a SVN repository.
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param pRepositoryId
   * @param pUserName
   * @param pNbComit
   *          the number of commit to be rereived.
   * @return
   * @throws SVNAgentException
   */
  List<ScmLogEntryDTO> getLastCommit(final SVNFacadeService pSVNFacadeService, final String pRepositoryId,
      final String pUserName, final int pNbComit) throws SVNAgentException;

  /**
   * add a membership for SVN repository
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param member
   * @param repositoryName
   * @return boolean
   * @throws SVNAgentException
   */
  boolean addMembership(final SVNFacadeService pSVNFacadeService, MembershipDTO member, String repositoryName)
      throws SVNAgentException;

  /**
   * update a membership for SVN repository
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param member
   * @param repositoryName
   * @return boolean
   * @throws SVNAgentException
   */
  boolean updateMembership(final SVNFacadeService pSVNFacadeService, MembershipDTO member,
      String repositoryName) throws SVNAgentException;

  /**
   * remove a membership for SVN repository
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param member
   * @param repositoryName
   * @return boolean
   * @throws SVNAgentException
   */
  boolean removeMembership(final SVNFacadeService pSVNFacadeService, String username, Role role,
      String repositoryName) throws SVNAgentException;

  /**
   * create a SVN repository add add a collection of memberships to it
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param memberships
   * @param repositoryId
   * @return boolean
   * @throws SVNAgentException
   */
  boolean createProject(final SVNFacadeService pSVNFacadeService, List<MembershipDTO> memberships,
      String repositoryId) throws SVNAgentException;

  /**
   * delete a SVN repository
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param repositoryId
   * @return boolean
   * @throws SVNAgentException
   */
  boolean deleteProject(final SVNFacadeService pSVNFacadeService, final String repositoryId)
      throws SVNAgentException;

  /**
   * update a user into SVN
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param user
   * @return boolean
   * @throws SVNAgentException
   */
  boolean updateUser(final SVNFacadeService pSVNFacadeService, UserDTO user) throws SVNAgentException;

  /**
   * delete u user into SVN
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param user
   * @return boolean
   * @throws SVNAgentException
   */
  boolean deleteUser(final SVNFacadeService pSVNFacadeService, UserDTO user) throws SVNAgentException;

  /**
   * find a µSVN user by its username
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param name
   * @return
   * @throws SVNAgentException
   */
  UserDTO findUserByName(final SVNFacadeService pSVNFacadeService, String name) throws SVNAgentException;

  /**
   * Return the url used to connect on svn repository
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param pRepositoryId
   *          represents the repository name
   * @return url
   * @throws SVNAgentException
   */
  String getRepositoryUrl(final SVNFacadeService pSVNFacadeService, String pRepositoryId)
      throws SVNAgentException;

  /**
   * This method returns the full node tree of a repository path
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param pRepositoryUrl
   * @param pUserName
   * @return SVNNodeEntryDTO
   * @throws SVNAgentException
   */
  SVNNodeEntryDTO getSCMRepositoryTree(final SVNFacadeService pSVNFacadeService, String pRepositoryUrl,
      String pUserName) throws SVNAgentException;

  /**
   * This method allows to export SCM repository nodes
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param pRepositoryNodes
   *          the nodes to export
   * @param pRepositoryPath
   * @return DataHandler
   * @throws SVNAgentException
   */
  DataHandler exportSCMRepositoryNodes(final SVNFacadeService pSVNFacadeService,
      List<String> pRepositoryNodes, String pRepositoryPath, String pUserName) throws SVNAgentException;

  /**
   * This method allows to search a regex into a SVN repository
   *
   * @param pSVNFacadeService
   *          represents the target facade to call
   * @param pRegex
   *          the regex to search
   * @param pFileRegex
   *          the file regex
   * @param pRepositoryId
   *          the target repository id
   * @param pRepositoryPath
   *          the path in the repository where the search will proceed
   * @param pUserName
   *          the username used to make the search
   * @return List<SVNSearchResultDTO>
   * @throws SVNAgentException
   */
  List<SVNSearchResultDTO> searchInSourceCode(final SVNFacadeService pSVNFacadeService, String pRegex,
      String pFileRegex, String pRepositoryId, String pRepositoryPath, String pUserName, String... pFileExtensions) throws SVNAgentException;
}
