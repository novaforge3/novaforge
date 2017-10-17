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

import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.gcl.nexus.domain.NexusConstant;
import org.novaforge.forge.plugins.gcl.nexus.domain.NexusRolePrivilege;
import org.novaforge.forge.plugins.gcl.nexus.domain.PrivilegeActionType;
import org.novaforge.forge.plugins.gcl.nexus.domain.PrivilegeActionCategoryType;
import org.novaforge.forge.plugins.gcl.nexus.domain.PublicRepository;
import org.novaforge.forge.plugins.gcl.nexus.domain.Repository;
import org.novaforge.forge.plugins.gcl.nexus.domain.RepositoryFormat;
import org.novaforge.forge.plugins.gcl.nexus.internal.datamapper.PrivilegeMapper;
import org.novaforge.forge.plugins.gcl.nexus.internal.datamapper.RepositoryMapper;
import org.novaforge.forge.plugins.gcl.nexus.internal.datamapper.RoleFactory;
import org.novaforge.forge.plugins.gcl.nexus.internal.datamapper.RoleMapper;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestClientCustom;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestException;
import org.sonatype.nexus.repository.maven.LayoutPolicy;
import org.sonatype.nexus.repository.maven.VersionPolicy;
import org.sonatype.nexus.repository.storage.WritePolicy;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.User;

/**
 * This is an implementation of PluginProjectService for Nexus plugin.
 * 
 * @see org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService
 * @see org.novaforge.forge.core.plugins.services.PluginProjectService
 * @author lamirang
 */
public class NexusProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
{

  private static final LayoutPolicy  DEFAULT_LAYOUT_POLICY        = LayoutPolicy.STRICT;
  private static final WritePolicy   DEFAULT_WRITE_POLICY         = WritePolicy.ALLOW;

  private static final boolean       DOCKER_DEFAULT_V1_COMPLIANCE = true;

  private static final boolean       USER_STATUS_ACTIVE           = true;
  private static final boolean       STRICT_CONTENT_VALIDATION    = true;

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
   */
  @Override
  protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
      final PluginProject pPluginProject, final List<PluginMembership> pMemberships)
      throws PluginServiceException
  {
    String toolProjectId = "";
    try
    {
      nexusRestClientCustom.initConnectionSettings(
          pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      toolProjectId = RepositoryMapper.buildBaseRepositoryId(pPluginProject.getProjectId(),
          pInstanceConfiguration.getConfigurationId());

      for (RepositoryFormat repositoryFormat : RepositoryFormat.values())
      {
        createRepository(pInstanceConfiguration, toolProjectId, pMemberships, repositoryFormat,
            VersionPolicy.SNAPSHOT);
        createRepository(pInstanceConfiguration, toolProjectId, pMemberships, repositoryFormat,
            VersionPolicy.RELEASE);
      }
    }
    catch (final NexusRestException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create project to Nexus Instance with [instance=%s, project=%s, memberships=%s]",
          pInstanceConfiguration.toString(), pPluginProject.toString(), pMemberships.toString()), e);
    }
    return toolProjectId;
  }

  /**
   * That allow to create a repository with a specific type depending on
   * boolean pRelease (true for a release one or false for a snapshot one)
   *
   * @param pInstanceConfiguration
   * @param pPluginProject
   * @param pMemberships
   * @return
   * @throws NexusRestException
   * @throws PluginServiceException
   */
  private void createRepository(final InstanceConfiguration pInstanceConfiguration,
      final String pBaseRepositoryName, final List<PluginMembership> pMemberships,
      final RepositoryFormat repositoryFormat, final VersionPolicy versionPolicy)
      throws NexusRestException, PluginServiceException
  {
    // Build repository Id
    final String repositoryId = RepositoryMapper.getRepositoryId(pBaseRepositoryName, repositoryFormat,
        versionPolicy);

    final Repository repository;
    switch (repositoryFormat)
    {
      case BOWER:
      case NPM:
      case NUGET:
      case PYPI:
      case RAW:
      case RUBYGEMS:
        repository = nexusRestClientCustom.createSimpleHostedRepository(repositoryId,
            NexusConstant.DEFAULT_BLOBSTORE_NAME, STRICT_CONTENT_VALIDATION, DEFAULT_WRITE_POLICY,
            repositoryFormat);
        break;
      case DOCKER:
        repository = nexusRestClientCustom.createDockerHostedRepository(repositoryId,
            NexusConstant.DEFAULT_BLOBSTORE_NAME, STRICT_CONTENT_VALIDATION, DEFAULT_WRITE_POLICY, null, null,
            DOCKER_DEFAULT_V1_COMPLIANCE);
        break;
      case MAVEN2:
        repository = nexusRestClientCustom.createMavenHostedRepository(repositoryId,
            NexusConstant.DEFAULT_BLOBSTORE_NAME, STRICT_CONTENT_VALIDATION, versionPolicy,
            DEFAULT_WRITE_POLICY, DEFAULT_LAYOUT_POLICY);
        break;
      default:
        repository = null;
        break;
    }

    if (repository != null)
    {
      addPermissions(pInstanceConfiguration, pMemberships, repository);

      // create read/view privileges to anonymous role on release repository
      if (versionPolicy == VersionPolicy.RELEASE)
      {
        final String projectId = repository.getName();
        this.createAnonymousRole(projectId, repository);
      }
    }
    else
    {
      throw new PluginServiceException(String.format(
          "Unable to create repository [repositoryId=%s, format=%s]", repositoryId, repositoryFormat));
    }
  }

  private void addPermissions(final InstanceConfiguration pInstanceConfiguration,
      final List<PluginMembership> pMemberships, final Repository repository)
      throws NexusRestException, PluginServiceException
  {
    // Create default roles for repository created
    createRoles(repository);

    // Add user to nexus with correct roles
    addUsers(pInstanceConfiguration.getInstanceId(), repository.getName(), pMemberships);
  }

  /**
   * This method will create default roles for a repository on nexus instance.
   * 
   * @param targetRepository
   * @throws NexusRestException
   */
  private void createRoles(Repository targetRepository) throws NexusRestException
  {
    for (NexusRolePrivilege nexusRolePrivilege : NexusRolePrivilege.values())
    {
      this.nexusRestClientCustom.createRole(RoleFactory.getRole(nexusRolePrivilege, targetRepository));
    }
  }

  /**
   * This method will create the anonymous role for a repository on nexus instance.
   * 
   * @param projectId
   * @param targetRepository
   * @throws NexusRestException
   */
  private void createAnonymousRole(final String projectId, Repository targetRepository)
      throws NexusRestException
  {
    Role anonymousRole = new Role();

    String repositoryId = targetRepository.getName();

    anonymousRole.setRoleId(RoleMapper.getRoleId(repositoryId, NexusConstant.ANONYMOUS_ID));
    anonymousRole.setName(RoleMapper.getRoleName(repositoryId, NexusConstant.ANONYMOUS_LABEL));
    anonymousRole.setDescription(RoleMapper.getRoleDescription(projectId, NexusConstant.ANONYMOUS_LABEL));
    anonymousRole.setReadOnly(false);
    anonymousRole.setSource(NexusConstant.DEFAULT_SOURCE);

    // Add privileges
    Set<PrivilegeActionType> privileges = new HashSet<PrivilegeActionType>();
    privileges.add(PrivilegeActionType.ACTION_BROWSE);
    privileges.add(PrivilegeActionType.ACTION_READ);

    Iterator<PrivilegeActionType> iterator = privileges.iterator();
    while (iterator.hasNext())
    {
      PrivilegeActionType privilegeActionType = iterator.next();
      anonymousRole.addPrivilege(PrivilegeMapper.getPrivilegeId(PrivilegeActionCategoryType.VIEW,
          privilegeActionType, targetRepository));
    }
    this.nexusRestClientCustom.createRole(anonymousRole);
  }

  /**
   * This method will add to users specific roles which will allow them to use
   * repository. If the user is already existing on nexus instance, this one
   * will be just updated.
   * 
   * @param pInstanceId
   *          represents instance's id
   * @param pRepositoryId
   *          represents repository's id
   * @param pMemberships
   *          represents mapping between users and roles
   * @throws NexusRestException
   *           occured if the communication with nexus instance failed, or
   *           if any http errors occured.
   * @throws PluginServiceException
   *           occured if tool role cannot be found.
   */
  private void addUsers(final String pInstanceId, final String pRepositoryId,
      final List<PluginMembership> pMemberships) throws NexusRestException, PluginServiceException
  {

    for (final PluginMembership pluginMembership : pMemberships)
    {

      final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, pluginMembership.getRole());
      final String roleId = pluginRoleMappingService.getToolRoleId(toolRole);

      PluginUser pluginUser = pluginMembership.getPluginUser();

      String userId = pluginUser.getLogin();
      User user = null;

      if (!nexusRestClientCustom.existsUser(userId))
      {
        user = nexusRestClientCustom.createUser(userId, pluginUser.getFirstName(), pluginUser.getName(),
            pluginUser.getEmail(), USER_STATUS_ACTIVE, pluginUser.getPassword(),
            NexusUserServiceImpl.ROLE_ANONYMOUS_PROXY);
      }
      else
      {
        user = nexusRestClientCustom.getUser(userId);
      }

      RoleIdentifier roleIdentifier = new RoleIdentifier(NexusConstant.DEFAULT_SOURCE,
          RoleMapper.getRoleId(pRepositoryId, roleId));
      user.addRole(roleIdentifier);
      nexusRestClientCustom.updateUser(user);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean deleteToolProject(final InstanceConfiguration pInstanceConfiguration)
      throws PluginServiceException
  {
    try
    {
      final String toolProjectId = pInstanceConfiguration.getToolProjectId();

      if (toolProjectId != null)
      {

        nexusRestClientCustom.initConnectionSettings(
            pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
            pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

        String repositoryId = null;
        // remove all existing repositories
        for (RepositoryFormat repositoryFormat : RepositoryFormat.values())
        {
          for (VersionPolicy versionPolicy : VersionPolicy.values())
          {
            if ((versionPolicy == VersionPolicy.SNAPSHOT) || (versionPolicy == VersionPolicy.RELEASE))
            {
              repositoryId = RepositoryMapper.getRepositoryId(toolProjectId, repositoryFormat, versionPolicy);
              // check if repository exists
              if (nexusRestClientCustom.existsRepository(repositoryId))
              {
                nexusRestClientCustom.deleteRepository(repositoryId);
              }
            }
          }
        }

        final List<Role> roles = nexusRestClientCustom.getRoles();
        for (final Role role : roles)
        {
          String roleId = role.getRoleId();
          if (roleId.startsWith(toolProjectId))
          {
            nexusRestClientCustom.deleteRole(roleId);
          }
        }
      }
    }
    catch (final NexusRestException e)
    {
      throw new PluginServiceException("Unable to connect to Nexus Restfull services", e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void archiveProject(final String pInstanceId) throws PluginServiceException
  {
    // TODO Auto-generated method stub

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolProject(final InstanceConfiguration pInstanceConfiguration,
      final PluginProject pluginProject) throws PluginServiceException
  {

    try
    {

      final String toolProjectId = pInstanceConfiguration.getToolProjectId();

      if (toolProjectId != null)
      {

        nexusRestClientCustom.initConnectionSettings(
            pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
            pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

        String repositoryId = null;
        // update all existing repositories
        for (RepositoryFormat repositoryFormat : RepositoryFormat.values())
        {
          for (VersionPolicy versionPolicy : VersionPolicy.values())
          {
            if ((versionPolicy == VersionPolicy.SNAPSHOT) || (versionPolicy == VersionPolicy.RELEASE))
            {
              repositoryId = RepositoryMapper.getRepositoryId(toolProjectId, repositoryFormat, versionPolicy);
              // check if repository exists
              if (nexusRestClientCustom.existsRepository(repositoryId))
              {
                Repository repository = nexusRestClientCustom.getRepository(repositoryId);
                repository.setName(pluginProject.getName());
                nexusRestClientCustom.updateRepository(repository);
              }
            }
          }
        }
      }
    }
    catch (final NexusRestException e)
    {
      throw new PluginServiceException("Unable to connect to Nexus Restfull services", e);
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
  public void setPluginConfigurationService(final PluginConfigurationService pNexusConfigurationService)
  {
    pluginConfigurationService = pNexusConfigurationService;
  }

}
