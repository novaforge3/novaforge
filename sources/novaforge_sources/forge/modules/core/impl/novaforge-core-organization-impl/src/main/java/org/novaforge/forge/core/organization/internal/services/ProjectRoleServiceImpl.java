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
package org.novaforge.forge.core.organization.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.validation.ValidationService;
import org.novaforge.forge.commons.technical.validation.ValidatorResponse;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.dao.RoleDAO;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.handlers.ApplicationHandler;
import org.novaforge.forge.core.organization.handlers.MembershipHandler;
import org.novaforge.forge.core.organization.handlers.SysApplicationHandler;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.ProjectRoleService;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.core.security.authorization.PermissionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link ProjectRoleService}
 * 
 * @author Guillaume Lamirand
 * @author sbenoist
 * @see ProjectRoleService
 */
public class ProjectRoleServiceImpl implements ProjectRoleService
{

  private static final Log LOG = LogFactory.getLog(ProjectRoleServiceImpl.class);
  /**
   * Reference to {@link ProjectDAO} service injected by the container
   */
  private ProjectDAO            projectDAO;
  /**
   * Reference to {@link RoleDAO} service injected by the container
   */
  private RoleDAO               roleDAO;
  /**
   * Reference to {@link ValidationService} service injected by the container
   */
  private ValidationService     validationService;
  /**
   * Reference to {@link PermissionHandler} service injected by the container
   */
  private PermissionHandler     permissionHandler;
  /**
   * Reference to {@link ApplicationHandler} service injected by the container
   */
  private ApplicationHandler    applicationHandler;
  /**
   * Reference to {@link MembershipDAO} service injected by the container
   */
  private MembershipDAO         membershipDAO;
  /**
   * Reference to {@link MembershipHandler} service injected by the container
   */
  private MembershipHandler     membershipHandler;
  /**
   * Reference to {@link ApplicationService} service injected by the container
   */
  private ApplicationService    applicationService;
  /**
   * Reference to {@link SysApplicationHandler} service injected by the container
   */
  private SysApplicationHandler sysApplicationHandler;

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectRole newRole()
  {
    return roleDAO.newProjectRole();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createRole(final Role pRole, final String projectId) throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input : role = " + pRole.toString());
      LOG.debug("Input : projectId = " + projectId);
    }
    createRole(pRole, projectId, RealmType.USER);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createSystemRole(final Role pRole, final String pProjectId) throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input : role = " + pRole.toString());
      LOG.debug("Input : projectId = " + pProjectId);
    }
    createRole(pRole, pProjectId, RealmType.SYSTEM);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteRole(final String pRoleName, final String pProjectId) throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input : projectId = " + pProjectId);
    }

    try
    {
      final Role role = roleDAO.findByNameAndElement(pProjectId, pRoleName);

      // check if the role can be updated (the system roles can't...)
      if (RealmType.SYSTEM.equals(role.getRealmType()))
      {
        throw new ProjectServiceException(ExceptionCode.ERR_UPDATE_OR_DELETE_SYSTEM_ROLE, String.format(
            "The role name=%s is a system role. It can't be modified", pRoleName));
      }

      roleDAO.delete(role);
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectRole> getAllRoles(final String pProjectId) throws ProjectServiceException
  {
    try
    {
      if (LOG.isDebugEnabled())
      {
        LOG.debug("Input : projectId = " + pProjectId);
      }

      final List<ProjectRole> returnList = new ArrayList<ProjectRole>();
      final List<Role> roles = roleDAO.findAllRole(pProjectId);
      for (final Role role : roles)
      {
        if (role instanceof ProjectRole)
        {
          returnList.add((ProjectRole) role);
        }
      }
      return returnList;
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectRole getRole(final String pRoleName, final String pProjectId) throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input : projectId = " + pProjectId);
      LOG.debug("Input : roleName = " + pRoleName);
    }

    try
    {
      return (ProjectRole) roleDAO.findByNameAndElement(pProjectId, pRoleName);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateRole(final String pOldName, final Role pRole, final String pProjectId, final String pUserName)
      throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input : role = " + pRole.toString());
      LOG.debug("Input : projectId = " + pProjectId);
    }
    try
    {
      // Update persisted role
      final ProjectRole role = checkRole(pOldName, pRole, pProjectId);
      roleDAO.update(role);

      // Update role mapping existing for the old name
      final List<ProjectApplication> availableApplications = applicationService.getAvailableApplications(pProjectId);
      for (final ProjectApplication projectApplication : availableApplications)
      {
        final Map<String, String> originalMapping = applicationHandler.getRoleMapping(projectApplication.getUri());
        final Map<String, String> targetMapping = new HashMap<String, String>(originalMapping);

        if (originalMapping.containsKey(pOldName))
        {
          targetMapping.remove(pOldName);
        }
        targetMapping.put(pRole.getName(), originalMapping.get(pOldName));

        applicationHandler.sendRolesMappingMessage(projectApplication.getUri(),
                                                   projectApplication.getPluginUUID().toString(),
                                                   projectApplication.getPluginInstanceUUID().toString(), pProjectId,
                                                   targetMapping, PluginQueueAction.UPDATE.getLabel(), pUserName);

      }
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPermissionToRole(final String pProjectId, final String pRoleName, final Class<?> pResource,
                                  final PermissionAction... pActions) throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input : projectId = " + pProjectId);
      LOG.debug("Input : roleName = " + pRoleName);
    }

    try
    {
      final ProjectRole role = (ProjectRole) roleDAO.findByNameAndElement(pProjectId, pRoleName);
      final Set<String> perms = permissionHandler.buildProjectPermissions(pProjectId, pResource,
                                                                          Arrays.asList(pActions));

      for (final String name : perms)
      {
        final Permission permission = roleDAO.findByName(name);
        role.addPermission(permission);
      }
      roleDAO.update(role);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectRole changeOrder(final String pProjectId, final String pRoleName, final boolean pIncrease,
      final String pUserName) throws ProjectServiceException
  {
    ProjectRole returnRole = null;
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input : roleName = " + pRoleName);
      LOG.debug("Input : projectId=" + pProjectId);
    }

    try
    {
      // get all the applications concerned by the propagation
      final List<ProjectApplication> activeApplication = applicationService
          .getAvailableApplications(pProjectId);

      // get all the tools initials memberships to prepare propagation for all users of the project
      final List<Actor> actors = membershipDAO.findActorsByProject(pProjectId);
      final Map<String, Map<String, Membership>> initialToolsMemberships = new HashMap<String, Map<String, Membership>>();
      for (final Actor actor : actors)
      {
        if (actor instanceof User)
        {
          final User user = (User) actor;
          initialToolsMemberships.put(user.getLogin(),
              membershipHandler.getToolsMemberships(pProjectId, activeApplication, user.getLogin()));
        }
        else if (actor instanceof Group)
        {
          final Group group = (Group) actor;
          for (final User user : group.getUsers())
          {
            final String userLogin = user.getLogin();
            initialToolsMemberships.put(userLogin,
                membershipHandler.getToolsMemberships(pProjectId, activeApplication, userLogin));
          }
        }
      }
      returnRole = (ProjectRole) roleDAO.changeOrder(pProjectId, pRoleName, pIncrease);

      // propagate the membership
      for (final Map.Entry<String, Map<String, Membership>> entry : initialToolsMemberships.entrySet())
      {
        membershipHandler.sendUserMembershipsPropagation(entry.getValue(), activeApplication, pProjectId,
            entry.getKey(), pUserName);
      }
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
    return returnRole;
  }

  private void createRole(final Role pRole, final String pProjectId, final RealmType realmType)
      throws ProjectServiceException
  {
    try
    {
      final ProjectRole role = checkRole("", pRole, pProjectId);
      final Set<String> permissions = permissionHandler.buildProjectPermissions(pProjectId, PermissionAction.READ);
      // Retrieve permissions for system applications
      final Set<String> sysApplicationPermissions = sysApplicationHandler.buildSysApplicationPermissions(pProjectId);
      permissions.addAll(sysApplicationPermissions);

      final Project project = projectDAO.findByProjectId(pProjectId);

      // Add set of permissions to role
      for (final String name : permissions)
      {
        final Permission perm = roleDAO.findByName(name);
        role.addPermission(perm);
      }
      role.setRealmType(realmType);

      // add the role
      project.addRole(role);

      // persist it
      projectDAO.update(project);

      // update system application mapping
      sysApplicationHandler.addRoleToSysApplicationsMapping(pProjectId, pRole.getName());
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  private ProjectRole checkRole(final String pOldRoleName, final Role pRole, final String pProjectId)
      throws ProjectServiceException
  {

    // check the role doesn't already exists for the project
    if (!(pRole instanceof ProjectRole))
    {
      throw new ProjectServiceException(ExceptionCode.ERR_CREATE_ROLE_ENTITY_UNDEFINED,
                                        String.format("Role does not have the good type [current=%s ,expected=%s]",
                                                      pRole.getClass().getName(), ProjectRole.class.getName()));
    }

    // check the role doesn't already exists for the project
    if ((!pOldRoleName.equals(pRole.getName())) && (roleDAO.existRole(pProjectId, pRole.getName())))
    {
      throw new ProjectServiceException(ExceptionCode.ERR_CREATE_ROLE_ROLENAME_ALREADY_EXIST,
                                        String.format("The role name=%s already exists.", pRole.getName()));
    }

    // check if the role can be updated (the system roles can't...)
    if ((pOldRoleName != null) && (!"".equals(pOldRoleName)) && (RealmType.SYSTEM.equals(pRole.getRealmType()))
            && (!pOldRoleName.equals(pRole.getName())))
    {
      throw new ProjectServiceException(ExceptionCode.ERR_UPDATE_OR_DELETE_SYSTEM_ROLE,
                                        String.format("The role name=%s is a system role. It can't be modified",
                                                      pOldRoleName));
    }

    // validate the bean
    final ValidatorResponse response = validationService.validate(ProjectRole.class, (ProjectRole) pRole);
    if (!response.isValid())
    {
      LOG.error(ExceptionCode.ERR_VALIDATION_BEAN.toString() + " : " + response.getMessage());
      throw new ProjectServiceException(ExceptionCode.ERR_VALIDATION_BEAN, response.getMessage());
    }

    // add an order (the less important) if there is not
    if (pRole.getOrder() == null)
    {
      final int lastOrder = roleDAO.getMaxOrder(pProjectId);
      final int newOrder = lastOrder + 1;
      pRole.setOrder(newOrder);
    }
    return (ProjectRole) pRole;
  }

  /**
   * Use by container to inject {@link ProjectDAO} implementation
   * 
   * @param pProjectDAO
   *          the projectDAO to set
   */
  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }

  /**
   * Use by container to inject {@link RoleDAO} implementation
   * 
   * @param pRoleDAO
   *          the roleDAO to set
   */
  public void setRoleDAO(final RoleDAO pRoleDAO)
  {
    roleDAO = pRoleDAO;
  }

  /**
   * Use by container to inject {@link ValidationService} implementation
   * 
   * @param pValidationService
   *          the validationService to set
   */
  public void setValidationService(final ValidationService pValidationService)
  {
    validationService = pValidationService;
  }

  /**
   * Use by container to inject {@link PermissionHandler} implementation
   * 
   * @param pPermissionHandler
   *          the authorizationHandler to set
   */
  public void setPermissionHandler(final PermissionHandler pPermissionHandler)
  {
    permissionHandler = pPermissionHandler;
  }

  /**
   * Use by container to inject {@link MembershipDAO} implementation
   * 
   * @param pMembershipDAO
   *          the membershipDAO to set
   */
  public void setMembershipDAO(final MembershipDAO pMembershipDAO)
  {
    membershipDAO = pMembershipDAO;
  }

  /**
   * Use by container to inject {@link ApplicationService} implementation
   * 
   * @param pApplicationService
   *          the applicationService to set
   */
  public void setApplicationService(final ApplicationService pApplicationService)
  {
    applicationService = pApplicationService;
  }

  /**
   * Use by container to inject {@link ApplicationHandler} implementation
   * 
   * @param pApplicationHandler
   *          the applicationHandler to set
   */
  public void setApplicationHandler(final ApplicationHandler pApplicationHandler)
  {
    applicationHandler = pApplicationHandler;
  }

  /**
   * Use by container to inject {@link MembershipHandler} implementation
   * 
   * @param pMembershipHandler
   *          the membershipHandler to set
   */
  public void setMembershipHandler(final MembershipHandler pMembershipHandler)
  {
    membershipHandler = pMembershipHandler;
  }

  /**
   * Use by container to inject {@link SysApplicationHandler} implementation
   * 
   * @param pSysApplicationHandler
   *          the sysApplicationHandler to set
   */
  public void setSysApplicationHandler(final SysApplicationHandler pSysApplicationHandler)
  {
    sysApplicationHandler = pSysApplicationHandler;
  }

}
