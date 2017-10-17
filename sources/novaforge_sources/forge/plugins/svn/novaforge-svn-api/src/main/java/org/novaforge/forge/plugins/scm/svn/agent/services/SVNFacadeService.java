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
import javax.jws.WebService;
import java.util.List;

/**
 * Facade service exposed as a webservice and used to communicate between the forge (client) and the SVN agent
 * (server).
 *
 * @author rols-p
 */
@WebService
public interface SVNFacadeService
{
  /**
   * WebService name.
   */
  String SVN_AGENT_FACADE_SERVICE_NAME = "SVNAgentFacadeService";

  /**
   * Retreives the (pNbComit) last commits from a SVN repository.
   *
   * @param pRepositoryId
   * @param pUserName
   * @param pNbComit
   *          the number of commit to be rereived.
   * @return
   * @throws SVNAgentException
   */
  List<ScmLogEntryDTO> getLastCommit(final String pRepositoryId, final String pUserName, final int pNbComit)
      throws SVNAgentException;

  /**
   * add a membership for SVN repository
   *
   * @param member
   * @param repositoryName
   * @return boolean
   * @throws SVNAgentException
   */
  boolean addMembership(MembershipDTO member, String repositoryName) throws SVNAgentException;

  /**
   * update a membership for SVN repository
   *
   * @param member
   * @param repositoryName
   * @return boolean
   * @throws SVNAgentException
   */
  boolean updateMembership(MembershipDTO member, String repositoryName) throws SVNAgentException;

  /**
   * remove a membership for SVN repository
   *
   * @param member
   * @param repositoryName
   * @return boolean
   * @throws SVNAgentException
   */
  boolean removeMembership(String username, Role role, String repositoryName) throws SVNAgentException;

  /**
   * create a SVN repository add add a collection of memberships to it
   *
   * @param memberships
   * @param repositoryId
   * @return boolean
   * @throws SVNAgentException
   */
  boolean createProject(List<MembershipDTO> memberships, String repositoryId) throws SVNAgentException;

  /**
   * delete a SVN repository
   *
   * @param repositoryId
   * @return boolean
   * @throws SVNAgentException
   */
  boolean deleteProject(final String repositoryId) throws SVNAgentException;

  /**
   * update a user into SVN
   *
   * @param user
   * @return boolean
   * @throws SVNAgentException
   */
  boolean updateUser(UserDTO user) throws SVNAgentException;

  /**
   * delete u user into SVN
   *
   * @param user
   * @return boolean
   * @throws SVNAgentException
   */
  boolean deleteUser(UserDTO user) throws SVNAgentException;

  /**
   * find a µSVN user by its username
   *
   * @param name
   * @return
   * @throws SVNAgentException
   */
  UserDTO findUserByName(String name) throws SVNAgentException;

  /**
   * Return the url used to connect on svn repository
   *
   * @param pRepositoryId
   *          represents the repository name
   * @return url
   * @throws SVNAgentException
   */
  String getRepositoryUrl(String pRepositoryId) throws SVNAgentException;

  /**
   * This method returns the full node tree of a repository path
   *
   * @param pRepositoryUrl
   * @param pUserName
   * @return SVNNodeEntryDTO
   * @throws SVNAgentException
   */
  SVNNodeEntryDTO getSCMRepositoryTree(String pRepositoryUrl, String pUserName) throws SVNAgentException;

  /**
   * This method allows to export SCM repository nodes
   *
   * @param pRepositoryNodes
   *          the nodes to export
   * @param pRepositoryPath
   * @return DataHandler
   * @throws SVNAgentException
   */
  DataHandler exportSCMRepositoryNodes(List<String> pRepositoryNodes, String pRepositoryPath, String pUserName)
      throws SVNAgentException;

  /**
   * This method allows to search a regex into a SVN repository
   *
   * @param pRegex
   *          the regex to search
   * @param pFileRegex
   *          the file regex
   * @param pRepositoryId
   *          the target repository id
   * @param pRepositoryPath
   *          the path where the search will process in the repository
   * @param pUserName
   *          the username used to make the search
   * @return List<SVNSearchResultDTO>
   * @throws SVNAgentException
   */
  List<SVNSearchResultDTO> searchInSourceCode(String pRegex, String pFileRegex, String pRepositoryId,
      String pRepositoryPath, String pUserName, String[] pFileExtensions) throws SVNAgentException;
}
