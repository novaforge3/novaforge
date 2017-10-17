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
package org.novaforge.forge.plugins.scm.svn.client.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.novaforge.forge.plugins.scm.svn.agent.dto.MembershipDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNNodeEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNSearchResultDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.ScmLogEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.UserDTO;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNClientFacadeService;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNFacadeService;
import org.novaforge.forge.plugins.scm.svn.domain.Role;

import javax.activation.DataHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author prols
 */
public final class SVNClientFacadeImpl implements SVNClientFacadeService
{

  /**
   * Logger component
   */
  private static final Log LOGGER = LogFactory.getLog(SVNClientFacadeImpl.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public SVNFacadeService getSVNFacadeService(final String pBaseUrl)
  {
    SVNFacadeService svnFacadeService = null;
    final ClassLoader theGoodOne = ClientProxyFactoryBean.class.getClassLoader();
    final ClassLoader theOldOne = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(theGoodOne);
    try
    {
      final ClientProxyFactoryBean factory = new ClientProxyFactoryBean();

      // Active MTOM for webservice
      final Map<String, Object> props = new HashMap<String, Object>();
      props.put("mtom-enabled", Boolean.TRUE);
      factory.setProperties(props);

      factory.setServiceClass(SVNFacadeService.class);
      factory.setAddress(pBaseUrl);
      svnFacadeService = (SVNFacadeService) factory.create();
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(theOldOne);
    }
    return svnFacadeService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ScmLogEntryDTO> getLastCommit(final SVNFacadeService pSVNFacadeService,
      final String pRepositoriesPath, final String pUserName, final int pNbComit) throws SVNAgentException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : pRepositoriesPath = " + pRepositoriesPath);
      LOGGER.debug("Input : pUserName = " + pUserName);
    }

    return pSVNFacadeService.getLastCommit(pRepositoriesPath, pUserName,
        pNbComit);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addMembership(final SVNFacadeService pSVNFacadeService, final MembershipDTO pMember,
      final String pRepositoryName) throws SVNAgentException
  {
    return pSVNFacadeService.addMembership(pMember, pRepositoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateMembership(final SVNFacadeService pSVNFacadeService, final MembershipDTO pMember,
      final String repositoryName) throws SVNAgentException
  {
    return pSVNFacadeService.updateMembership(pMember, repositoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeMembership(final SVNFacadeService pSVNFacadeService, final String username,
      final Role role, final String repositoryName) throws SVNAgentException
  {
    return pSVNFacadeService.removeMembership(username, role, repositoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean createProject(final SVNFacadeService pSVNFacadeService,
      final List<MembershipDTO> pMemberships, final String repositoryName) throws SVNAgentException
  {
    return pSVNFacadeService.createProject(pMemberships, repositoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteProject(final SVNFacadeService pSVNFacadeService, final String repositoryName)
      throws SVNAgentException
  {
    return pSVNFacadeService.deleteProject(repositoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateUser(final SVNFacadeService pSVNFacadeService, final UserDTO user)
      throws SVNAgentException
  {
    return pSVNFacadeService.updateUser(user);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteUser(final SVNFacadeService pSVNFacadeService, final UserDTO user)
      throws SVNAgentException
  {
    return pSVNFacadeService.deleteUser(user);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserDTO findUserByName(final SVNFacadeService pSVNFacadeService, final String name)
      throws SVNAgentException
  {
    return pSVNFacadeService.findUserByName(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRepositoryUrl(final SVNFacadeService pSVNFacadeService, final String pRepositoryId)
      throws SVNAgentException
  {
    return pSVNFacadeService.getRepositoryUrl(pRepositoryId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SVNNodeEntryDTO getSCMRepositoryTree(final SVNFacadeService pSVNFacadeService,
      final String pRepositoryPath, final String pUserName) throws SVNAgentException
  {
    return pSVNFacadeService.getSCMRepositoryTree(pRepositoryPath, pUserName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataHandler exportSCMRepositoryNodes(final SVNFacadeService pSVNFacadeService,
      final List<String> pRepositoryNodes, final String pRepositoryPath, final String pUserName)
      throws SVNAgentException
  {
    return pSVNFacadeService.exportSCMRepositoryNodes(pRepositoryNodes, pRepositoryPath, pUserName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SVNSearchResultDTO> searchInSourceCode(final SVNFacadeService pSVNFacadeService,
      final String pRegex, final String pRegexFile, final String pRepositoryId, final String pRepositoryPath,
      final String pUserName, String... pFileExtensions) throws SVNAgentException
  {
    return pSVNFacadeService
        .searchInSourceCode(pRegex, pRegexFile, pRepositoryId, pRepositoryPath, pUserName, pFileExtensions);
  }

}
