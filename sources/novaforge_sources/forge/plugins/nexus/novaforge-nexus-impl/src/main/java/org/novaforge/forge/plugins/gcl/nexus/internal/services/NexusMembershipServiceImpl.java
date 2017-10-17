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
package org.novaforge.forge.plugins.gcl.nexus.internal.services;

import java.util.HashSet;
import java.util.Set;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.gcl.nexus.domain.NexusConstant;
import org.novaforge.forge.plugins.gcl.nexus.domain.NexusRolePrivilege;
import org.novaforge.forge.plugins.gcl.nexus.domain.RepositoryFormat;
import org.novaforge.forge.plugins.gcl.nexus.internal.datamapper.RepositoryMapper;
import org.novaforge.forge.plugins.gcl.nexus.internal.datamapper.RoleMapper;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestClientCustom;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestException;
import org.sonatype.nexus.repository.maven.VersionPolicy;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.User;

/**
 * This is an implementation of PluginMembershipService for Nexus plugin.
 * 
 * @see org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService
 * @see org.novaforge.forge.core.plugins.services.PluginMembershipService
 * @author lamirang
 */
public class NexusMembershipServiceImpl extends AbstractPluginMembershipService implements PluginMembershipService {

	/**
	 * Reference to service implementation of {@link NexusRestClientCustom}
	 */
	private NexusRestClientCustom nexusRestClientCustom;
	/**
	 * Reference to service implementation of {@link PluginConfigurationService}
	 */
	private PluginConfigurationService pluginConfigurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
			final String pToolRole) throws PluginServiceException {

		final String roleId = pluginRoleMappingService.getToolRoleId(pToolRole);
		try {
			nexusRestClientCustom.initConnectionSettings(
					pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
					pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
			 
			if (!nexusRestClientCustom.existsUser(pUser.getLogin()))
			{
				createNexusUser(pInstance.getToolProjectId(), pUser, roleId);
			} 
			else 
			{
			  final User user = nexusRestClientCustom.getUser(pUser.getLogin());
				updateNexusUser(pInstance.getToolProjectId(), user, roleId);
			}

		} catch (final NexusRestException e) {
			throw new PluginServiceException("Unable to build Nexus client through NexusClient.", e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
			final String pToolRole) throws PluginServiceException {
		try {
			nexusRestClientCustom.initConnectionSettings(
					pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
					pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

			final String toolRoleId = pluginRoleMappingService.getToolRoleId(pToolRole);

			if (!nexusRestClientCustom.existsUser(pUser.getLogin()))
			{
				createNexusUser(pInstance.getToolProjectId(), pUser, toolRoleId);
			} 
			else 
			{
	      final User user = nexusRestClientCustom.getUser(pUser.getLogin());

	      final Set<RoleIdentifier> roleIdentifiers = user.getRoles();
				Set<RoleIdentifier> rolesToRemove = new HashSet<RoleIdentifier>();

				for (final NexusRolePrivilege rolePrivilege : NexusRolePrivilege.values()) {
					for (final RoleIdentifier roleIdentifier : roleIdentifiers) {
						String roleId = roleIdentifier.getRoleId();

						if ((roleId.startsWith(pInstance.getToolProjectId()))
								&& (roleId.endsWith(rolePrivilege.getId()))) {
						  rolesToRemove.add(roleIdentifier);
						}
					}
				}
        for (final RoleIdentifier roleToRemove : rolesToRemove) {
          user.removeRole(roleToRemove);
        }
				updateNexusUser(pInstance.getToolProjectId(), user, toolRoleId);
			}
		} catch (final NexusRestException e) {

			throw new PluginServiceException("Unable to connect to Nexus service through NexusClientService.", e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
			final String pToolRole) throws PluginServiceException {
		try {

			nexusRestClientCustom.initConnectionSettings(
					pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
					pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      if (nexusRestClientCustom.existsUser(pUser.getLogin()))
      {
  			final User user = nexusRestClientCustom.getUser(pUser.getLogin());
  			Set<RoleIdentifier> rolesToRemove = new HashSet<RoleIdentifier>();
  
  			for (final NexusRolePrivilege rolePrivilege : NexusRolePrivilege.values()) {
  				for (final RoleIdentifier role : user.getRoles()) {
  					if ((role.getRoleId().startsWith(pInstance.getToolProjectId()))
                && (role.getRoleId().endsWith(rolePrivilege.getId()))) {
  					  rolesToRemove.add(role);
  					}
  				}
  			}
        for (final RoleIdentifier roleToRemove : rolesToRemove) {
          user.removeRole(roleToRemove);
        }
  			nexusRestClientCustom.updateUser(user);
      }
		} catch (final NexusRestException e) {
			throw new PluginServiceException("Unable to connect to Nexus service through NexusClientService.", e);
		}
		return true;
	}

	/**
	 * Create a nexus user with relevant roles on release and snapshot repositories.
	 * @param pToolProjectId
	 * @param pluginUser
	 * @param pRoleId
	 * @throws NexusRestException
	 */
	private void createNexusUser(final String pToolProjectId, final PluginUser pluginUser,
			final String pRoleId) throws NexusRestException {
	  
	  String repositoryId = null;
	  int i = 0;
	  String[] listRoleIds = new String[RepositoryFormat.values().length * 2 + 1];
    for (RepositoryFormat repositoryFormat : RepositoryFormat.values())
    {
      for (VersionPolicy versionPolicy : VersionPolicy.values())
      {
        if ((versionPolicy==VersionPolicy.SNAPSHOT) || (versionPolicy==VersionPolicy.RELEASE)) 
        {
          repositoryId = RepositoryMapper.getRepositoryId(pToolProjectId, repositoryFormat,
                            versionPolicy);
          listRoleIds[i]=RoleMapper.getRoleId(repositoryId,pRoleId);
          i++;
        }
      }
    }
    // Add Anonymous Proxy Role
    listRoleIds[i]=NexusConstant.ANONYMOUS_PROXY_ID;
	  
		// Create the user on Nexus with 2 roles : for repository release and the repository snapshot
		nexusRestClientCustom.createUser(
				pluginUser.getLogin(), 
				pluginUser.getFirstName(), 
				pluginUser.getName(),
				pluginUser.getEmail(), 
				NexusConstant.DEFAULT_USER_STATUS_ACTIVE, 
				pluginUser.getPassword(),
				listRoleIds);
	}

  /**
   * Update a nexus user with relevant roles on release and snapshot repositories.
   * @param pToolProjectId
   * @param user
   * @param pRoleId
   * @throws NexusRestException
   */
	private void updateNexusUser(final String pToolProjectId, final User user, final String roleId)
			throws NexusRestException {
	
    String repositoryId = null;
    for (RepositoryFormat repositoryFormat : RepositoryFormat.values())
    {
      for (VersionPolicy versionPolicy : VersionPolicy.values())
      {
        if ((versionPolicy==VersionPolicy.SNAPSHOT) || (versionPolicy==VersionPolicy.RELEASE)) 
        {
          repositoryId = RepositoryMapper.getRepositoryId(pToolProjectId, repositoryFormat,
                            versionPolicy);
          user.addRole(
              new RoleIdentifier(
                  NexusConstant.DEFAULT_SOURCE, 
                  RoleMapper.getRoleId(repositoryId, roleId)));
        }
      }
    }
		
		// Update the user on Nexus
		nexusRestClientCustom.updateUser(user);
	}

	/**
	 * Use by container to inject {@link NexusRestClientCustom}
	 * 
	 * @param pNexusRestClientCustom
	 *            the nexusRestClientCustom to set
	 */
	public void setNexusRestClientCustom(final NexusRestClientCustom pNexusRestClientCustom) {
		nexusRestClientCustom = pNexusRestClientCustom;
	}

	/**
	 * Use by container to inject {@link PluginConfigurationService}
	 * 
	 * @param pPluginConfigurationService
	 *            the pluginConfigurationService to set
	 */
	public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService) {
		pluginConfigurationService = pPluginConfigurationService;
	}
}
