/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.internal.services;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.plugins.scm.svn.agent.dao.UserService;
import org.novaforge.forge.plugins.scm.svn.agent.dto.MembershipDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNNodeEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNSearchResultDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.ScmLogEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.UserDTO;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNFacadeService;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNRepositoryService;
import org.novaforge.forge.plugins.scm.svn.domain.Membership;
import org.novaforge.forge.plugins.scm.svn.domain.Role;
import org.novaforge.forge.plugins.scm.svn.domain.User;
import org.novaforge.forge.plugins.scm.svn.services.SVNToolService;

/**
 * @author sbenoist
 */
public final class SVNFacadeServiceImpl implements SVNFacadeService
{

  private UserService          userService;

  private SVNRepositoryService svnRepositoryService;
  private SVNToolService       svnToolService;
  private static String        repositoriesPath;
  private static final Log     LOGGER = LogFactory.getLog(SVNToolServiceImpl.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addMembership(final MembershipDTO pMember, final String pRepositoryName)
      throws SVNAgentException
  {

    return userService.addMembership(pMember, pRepositoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateMembership(final MembershipDTO pMember, final String repositoryName)
      throws SVNAgentException
  {
    return userService.updateMembership(pMember, repositoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeMembership(final String username, final Role role, final String repositoryName)
      throws SVNAgentException
  {
    return userService.removeMembership(username, role, repositoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean createProject(final List<MembershipDTO> memberships, final String repositoryName)
      throws SVNAgentException
  {
    final List<Membership> members = new ArrayList<Membership>();
    members.addAll(memberships);
    return userService.createProject(members, repositoriesPath, repositoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteProject(final String repositoryName) throws SVNAgentException
  {
    return userService.deleteProject(repositoriesPath, repositoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateUser(final UserDTO user) throws SVNAgentException
  {
    return userService.updateUser(user);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteUser(final UserDTO user) throws SVNAgentException
  {
    return userService.deleteUser(user);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserDTO findUserByName(final String name) throws SVNAgentException
  {
    final User user = userService.findUserByName(name);
    final UserDTO userDTO = new UserDTO(user.getName(), user.getPassword());
    return userDTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ScmLogEntryDTO> getLastCommit(final String pRepositoryId, final String pUserName,
      final int pNbComit) throws SVNAgentException
  {
    return svnRepositoryService.getLastCommit(svnToolService.getRepositoryPath(pRepositoryId, null), pUserName,
        pNbComit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRepositoryUrl(final String pRepositoryId) throws SVNAgentException
  {
    return svnToolService.getRepositoryUrl(pRepositoryId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SVNNodeEntryDTO getSCMRepositoryTree(final String pRepositoryId, final String pUserName)
      throws SVNAgentException
  {
    return svnRepositoryService.getRepositoryTree(svnToolService.getRepositoryPath(pRepositoryId, null), pUserName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataHandler exportSCMRepositoryNodes(final List<String> pRepositoryNodes,
      final String pRepositoryId, final String pUserName) throws SVNAgentException
  {
    return svnRepositoryService.exportRepositoryNodes(pRepositoryNodes,
        svnToolService.getRepositoryPath(pRepositoryId, null), pUserName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SVNSearchResultDTO> searchInSourceCode(final String pRegex, final String pFileRegex,
      final String pRepositoryId, final String pRepositoryPath, final String pUserName, final String[] pFileExtensions) throws SVNAgentException
  {
    final String repositoryPath = svnToolService.getRepositoryPath(pRepositoryId, pRepositoryPath);
    return svnRepositoryService.searchInSourceCode(pRegex, pFileRegex, repositoryPath, pUserName, pFileExtensions);
  }

  public void setRepositoriesPath(final String pRepositoriesPath)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Update SVN repository path configuration from '%s' to '%s'.",
          repositoriesPath, pRepositoriesPath));
    }
    repositoriesPath = pRepositoriesPath;
  }

  /**
   * @param pUserService
   *          the userSessionFacadeRemote to set
   */
  public void setUserService(final UserService pUserService)
  {
    userService = pUserService;
  }

  /**
   * @param pSvnRepositoryService
   *          the svnRepositoryService to set
   */
  public void setSvnRepositoryService(final SVNRepositoryService pSvnRepositoryService)
  {
    svnRepositoryService = pSvnRepositoryService;
  }

  /**
   * @param pSvnToolService
   *          the svnToolService to set
   */
  public void setSvnToolService(final SVNToolService pSvnToolService)
  {
    svnToolService = pSvnToolService;
  }

}