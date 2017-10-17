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
package org.novaforge.forge.plugins.scm.svn.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.scm.svn.agent.dto.MembershipDTO;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNClientFacadeService;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNFacadeService;
import org.novaforge.forge.plugins.scm.svn.domain.Role;
import org.novaforge.forge.plugins.scm.svn.internal.utils.Utils;

/**
 * @author benoists
 */
public class SVNMembershipServiceImpl extends AbstractPluginMembershipService implements
    PluginMembershipService
{
  /**
   * Reference to service implementation of {@link SVNClientFacadeService}
   */
  private SVNClientFacadeService     svnClientFacadeService;
  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    boolean returnValue = false;

    // Get the default repository path associated to the project
    final String repositoryName = Utils.getRepositoryName(pInstance);

    // add a new user in the project with the associated permissions
    final Role role = Role.fromLabel(pToolRole);
    if (role == null)
    {
      throw new PluginServiceException("unable to get the svn role from the label:" + pToolRole);
    }
    final MembershipDTO member = new MembershipDTO(pUser.getLogin(), pUser.getPassword(), role);
    try
    {
      final String clientURL = pluginConfigurationService.getClientURL(pInstance.getToolInstance()
          .getBaseURL());
      final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
      returnValue = svnClientFacadeService.addMembership(svnFacadeService, member, repositoryName);
    }
    catch (final SVNAgentException e)
    {
      throw new PluginServiceException(String.format("Unable to create membership with [user=%s, role=%s]",
          pUser, pToolRole), e);
    }
    return returnValue;
  }

  @Override
  protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    boolean returnValue = false;
    // Get the default repository path associated to the project
    final String repositoryName = Utils.getRepositoryName(pInstance);

    // get the new role
    final Role role = Role.fromLabel(pToolRole);
    if (role == null)
    {
      throw new PluginServiceException("unable to get the svn role from the label:" + pToolRole);
    }
    try
    {
      final String clientURL = pluginConfigurationService.getClientURL(pInstance.getToolInstance()
          .getBaseURL());
      final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
      final MembershipDTO member = new MembershipDTO(pUser.getLogin(), pUser.getPassword(), role);
      returnValue = svnClientFacadeService.updateMembership(svnFacadeService, member, repositoryName);
    }
    catch (final SVNAgentException e)
    {
      throw new PluginServiceException(String.format("Unable to update membership with [user=%s, role=%s]",
          pUser, pToolRole), e);
    }
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    boolean returnValue = false;
    // Get the default repository path associated to the project
    final String repositoryName = Utils.getRepositoryName(pInstance);

    // get the new role
    final Role role = Role.fromLabel(pToolRole);
    if (role == null)
    {
      throw new PluginServiceException("unable to get the svn role from the label:" + pToolRole);
    }
    try
    {
      final String clientURL = pluginConfigurationService.getClientURL(pInstance.getToolInstance()
          .getBaseURL());
      final SVNFacadeService svnFacadeService = svnClientFacadeService.getSVNFacadeService(clientURL);
      returnValue = svnClientFacadeService.removeMembership(svnFacadeService, pUser.getLogin(), role,
          repositoryName);
    }
    catch (final SVNAgentException e)
    {
      throw new PluginServiceException(String.format("Unable to update membership with [user=%s, role=%s]",
          pUser, pToolRole), e);
    }
    return returnValue;
  }

  /**
   * Use by container to inject {@link SVNClientFacadeService}
   * 
   * @param pSvnClientFacadeService
   *          the svnClientFacadeService to set
   */
  public void setSvnClientFacadeService(final SVNClientFacadeService pSvnClientFacadeService)
  {
    svnClientFacadeService = pSvnClientFacadeService;
  }

  /**
   * Use by container to inject {@link PluginConfigurationService}
   * 
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }
}
