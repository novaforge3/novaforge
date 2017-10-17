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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.plugins.gcl.nexus.domain.NexusConstant;
import org.novaforge.forge.plugins.gcl.nexus.domain.PrivilegeActionCategoryType;
import org.novaforge.forge.plugins.gcl.nexus.domain.PrivilegeActionType;
import org.novaforge.forge.plugins.gcl.nexus.domain.PublicRepository;
import org.novaforge.forge.plugins.gcl.nexus.domain.Repository;
import org.novaforge.forge.plugins.gcl.nexus.internal.datamapper.PrivilegeMapper;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestClientCustom;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestException;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.User;


/**
 * This is an implementation of PluginUserService for Nexus plugin.
 * 
 * @see org.novaforge.forge.plugins.commons.services.AbstractPluginUserService
 * @see org.novaforge.forge.core.plugins.services.PluginUserService
 * @author lamirang
 */
public class NexusUserServiceImpl extends AbstractPluginUserService implements PluginUserService
{

	/**
	 * Privilege's id to administrate nexus
	 */
	public static final String ROLE_ADMIN = "nx-admin";
	public static final String ROLE_ANONYMOUS_PROXY = NexusConstant.ANONYMOUS_PROXY_ID;
	public static final String ROLE_SOURCE = "default";
	public static final boolean USER_ACTIVE = true;

	/**
	 * Reference to service implementation of {@link NexusRestClientCustom}
	 */
	private NexusRestClientCustom      nexusRestClientCustom;
	/**
	 * Reference to service implementation of {@link PluginConfigurationService}
	 */
	private PluginConfigurationService pluginConfigurationService;

	/**
	 * {@inheritDoc}
	 * Create the admin user of the forge (owns all privileges)
	 */
	@Override
	public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pluginUser)
			throws PluginServiceException
	{
		try
		{
			nexusRestClientCustom.initConnectionSettings(pluginConfigurationService.getClientURL(pInstance.getBaseURL()),
																		pluginConfigurationService.getClientAdmin(),
																		pluginConfigurationService.getClientPwd());
		}
		catch (final NexusRestException e)
		{
			throw new PluginServiceException(String.format("Unable to init connection settings [URL=%s].",
					pInstance.getBaseURL()), e);
		}
			
		String userId = pluginUser.getLogin();
			
		try {
			
      // Create Anonymous Proxy Role if it does not exist
      createAnonymousProxyRole();
      
			if(!nexusRestClientCustom.existsUser(userId))
			{
				nexusRestClientCustom.createUser(
						userId, 
						pluginUser.getFirstName(),
						pluginUser.getName(),
						pluginUser.getEmail(),
						USER_ACTIVE,
						pluginUser.getPassword(),
						ROLE_ADMIN,
						ROLE_ANONYMOUS_PROXY);
			}
		}
		catch (final NexusRestException e)
		{
			throw new PluginServiceException(String.format("Unable to create administrator user with [userId=%s].",
					userId), e);
		}
		
	}

  /**
   * This method will create the anonymous proxy role for predefined proxy
   * and group repositories on nexus instance.
   * it also replaces the default "nx-anonymous" role assigned to anonymous user
   * with this anonymous proxy role
   * 
   * @throws NexusRestException
   */
  private void createAnonymousProxyRole() throws NexusRestException
  {
    // Check if anonymous proxy role already exists
    List<Role> roles = nexusRestClientCustom.getRoles();
    boolean aproxyexists = false;
    for (final Role role : roles)
    {
      if (role.getRoleId().equals(NexusConstant.ANONYMOUS_PROXY_ID))
      {
        aproxyexists = true;
        break;
      }
    }

    Role anonymousProxyRole = null;
    if (!aproxyexists)
    {
      anonymousProxyRole = new Role();
      anonymousProxyRole.setRoleId(NexusConstant.ANONYMOUS_PROXY_ID);
      anonymousProxyRole.setName(NexusConstant.ANONYMOUS_PROXY_LABEL);
      anonymousProxyRole.setDescription(NexusConstant.ANONYMOUS_PROXY_LABEL + " for proxy repositories");
      anonymousProxyRole.setReadOnly(false);
      anonymousProxyRole.setSource(NexusConstant.DEFAULT_SOURCE);

      // Add default privileges
      anonymousProxyRole.addPrivilege(NexusConstant.PRIV_HEALTHCHECK_READ);
      anonymousProxyRole.addPrivilege(NexusConstant.PRIV_SEARCH_READ);
      // Add privileges for each public repository
      Set<PrivilegeActionType> privileges = new HashSet<PrivilegeActionType>();
      privileges.add(PrivilegeActionType.ACTION_BROWSE);
      privileges.add(PrivilegeActionType.ACTION_READ);

      Repository repository = null;
      // Retrieve all proxy and group repositories
      for (PublicRepository publicRepository : PublicRepository.values())
      {
        // Check if repository exists
        if (nexusRestClientCustom.existsRepository(publicRepository.getValue()))
        {
          repository = nexusRestClientCustom.getRepository(publicRepository.getValue());

          Iterator<PrivilegeActionType> iterator = privileges.iterator();
          while (iterator.hasNext())
          {
            PrivilegeActionType privilegeActionType = iterator.next();
            anonymousProxyRole.addPrivilege(PrivilegeMapper.getPrivilegeId(PrivilegeActionCategoryType.VIEW,
                privilegeActionType, repository));
          }
        }
      }
      if (anonymousProxyRole != null)
      {
        // Create the role
        this.nexusRestClientCustom.createRole(anonymousProxyRole);
        
        // Assign it to the anonymous user in replacement of "nx-anonymous" role
        if (nexusRestClientCustom.existsUser(NexusConstant.DEFAULT_ANONYMOUS_USER_ID))
        {
          final User anonymousUser = nexusRestClientCustom.getUser(NexusConstant.DEFAULT_ANONYMOUS_USER_ID);
          // Remove default nx-anonymous Role
          anonymousUser.removeRole(
              new RoleIdentifier(
                  NexusConstant.DEFAULT_SOURCE, 
                  NexusConstant.DEFAULT_ANONYMOUS_ROLE_ID));
          // Add Anonymous Proxy Role 
          anonymousUser.addRole(
              new RoleIdentifier(
                  NexusConstant.DEFAULT_SOURCE, 
                  NexusConstant.ANONYMOUS_PROXY_ID));
          nexusRestClientCustom.updateUser(anonymousUser);
        }
      }
    }
  }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removeToolUser(final InstanceConfiguration pInstance, final PluginUser pUser)
			throws PluginServiceException
	{
		try
		{
			nexusRestClientCustom.initConnectionSettings(pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
																		pluginConfigurationService.getClientAdmin(),
																		pluginConfigurationService.getClientPwd());
			nexusRestClientCustom.deleteUser(pUser.getLogin());

		}
		catch (final NexusRestException e)
		{
			throw new PluginServiceException("Unable to connect to Nexus Rest Services.", e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateToolUser(final InstanceConfiguration pInstance, final String pUserName,
																	 final PluginUser pUser) throws PluginServiceException
	{
		try
		{
			nexusRestClientCustom.initConnectionSettings(pluginConfigurationService.getClientURL(pInstance.getToolInstance().getBaseURL()),
																		pluginConfigurationService.getClientAdmin(),
																		pluginConfigurationService.getClientPwd());
			final User user = nexusRestClientCustom.getUser(pUserName);
			user.setUserId(pUser.getLogin());
			user.setFirstName(pUser.getFirstName());
			user.setLastName(pUser.getName());
			user.setEmailAddress(pUser.getEmail());
			
			nexusRestClientCustom.updateUser(user);

			nexusRestClientCustom.updateUserPassword(pUser.getLogin(), pUser.getPassword());

		}
		catch (final NexusRestException e)
		{
			throw new PluginServiceException("Unable to connect to Nexus Rest Services.", e);
		}
		return true;
	}


	/**
	 * Use by container to inject {@link NexusRestClientCustom}
	 * 
	 * @param pNexusRestClientCustom
	 *          the nexusRestClientCustom to set
	 */
	public void setNexusRestClientCustom(final NexusRestClientCustom pNexusRestClientCustom)
	{
		nexusRestClientCustom = pNexusRestClientCustom;
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
