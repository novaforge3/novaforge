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
/**
 * 
 */
package org.novaforge.forge.core.security.internal.authorization;

import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.core.security.authorization.PermissionHandler;
import org.novaforge.forge.dashboard.model.DashBoard;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link PermissionHandler}
 * 
 * @author Guillaume Lamirand
 * @see PermissionHandler
 */
public class PermissionHandlerImpl implements PermissionHandler
{
  private static final String FORGE                = "forge";
  private static final String PROJECT              = "project";
  private static final String ROLE                 = "role";
  private static final String MEMBERSHIP           = "membership";
  private static final String MEMBERSHIP_REQUEST   = "request";
  private static final String GROUP                = "group";
  private static final String SPACE                = "space";
  private static final String APPLICATION          = "application";
  private static final String COMMUNICATION        = "communication";
  private static final String DASHBOARD            = "dashboard";
  private static final String UNKNOWN              = "unknown";
  private static final String PERMISSION_SEPARATOR = ":";

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> buildForgePermissions(final PermissionAction... pActions)
  {
    final StringBuilder permission = new StringBuilder(FORGE);
    permission.append(PERMISSION_SEPARATOR);

    final Set<String> permissions = new HashSet<String>();
    for (final PermissionAction action : pActions)
    {
      permissions.add(permission + action.getLabel());
    }
    return permissions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> buildProjectPermissions(final PermissionAction... pActions)
  {
    final StringBuilder permission = new StringBuilder(PROJECT);
    permission.append(PERMISSION_SEPARATOR);

    final Set<String> permissions = new HashSet<String>();
    for (final PermissionAction action : pActions)
    {
      permissions.add(permission + action.getLabel());
    }
    return permissions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> buildProjectPermissions(final String pProjectId, final PermissionAction... pActions)
  {
    final String projectPermission = buildPermission(pProjectId, Project.class);
    final String rolePermission = buildPermission(pProjectId, Role.class);
    final String groupPermission = buildPermission(pProjectId, Group.class);
    final String membershipPermission = buildPermission(pProjectId, Membership.class);
    final String membershipRequestPermission = buildPermission(pProjectId, MembershipRequest.class);
    final String spacePermission = buildPermission(pProjectId, Space.class);
    final String applPermission = buildPermission(pProjectId, Application.class);
    final String dashboardPermission = buildPermission(pProjectId, DashBoard.class);

    final Set<String> permissions = new HashSet<String>();
    for (final PermissionAction action : pActions)
    {
      permissions.add(projectPermission + action.getLabel());
      permissions.add(rolePermission + action.getLabel());
      permissions.add(groupPermission + action.getLabel());
      permissions.add(membershipPermission + action.getLabel());
      permissions.add(membershipRequestPermission + action.getLabel());
      permissions.add(spacePermission + action.getLabel());
      permissions.add(applPermission + action.getLabel());
      permissions.add(dashboardPermission + action.getLabel());
    }
    return permissions;
  }

  /**
   * {@inheritDoc}
   *
   * @param <T>
   */
  @Override
  public <T> Set<String> buildProjectPermissions(final String pProjectId, final Class<T> pResourceClass,
                                                 final List<PermissionAction> pActions)
  {
    final String smallPermission = buildPermission(pProjectId, pResourceClass);

    final Set<String> permissions = new HashSet<String>();
    for (final PermissionAction action : pActions)
    {
      permissions.add(smallPermission + action.getLabel());
    }
    return permissions;
  }

  /**
   * {@inheritDoc}
   * 
   * @param <T>
   */
  @Override
  public <T> Set<String> buildProjectPermissions(final String pProjectId, final Class<T> pResourceClass,
      final PermissionAction... pActions)
  {
    return buildProjectPermissions(pProjectId, pResourceClass, Arrays.asList(pActions));
  }

  @Override
  public String buildProjectAdminPermission(final String pProjectId)
  {
    final String projectPermission = buildPermission(pProjectId, Project.class);
    return projectPermission + PermissionAction.ALL.getLabel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> buildApplicationPermission(final String pProjectId, final String pApplicationId,
                                                final PermissionAction... pActions)
  {
    final String smallPermission = buildPermission(pProjectId, Application.class);

    final Set<String> permissions = new HashSet<String>();
    for (final PermissionAction action : pActions)
    {
      permissions.add(smallPermission + pApplicationId + PERMISSION_SEPARATOR + action.getLabel());
    }
    return permissions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String resolveProjectId(final String pPermissions)
  {
    String         projectId = null;
    final String[] perm      = pPermissions.split(PERMISSION_SEPARATOR);

    // perm.length should be higher than 2, otherwise it is not a projet permission
    if (perm.length > 2)
    {
      projectId = perm[1];
    }
    return projectId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> resolveApplicationId(final Set<String> pPermissions)
  {
    final Set<String> appliIds = new HashSet<String>();
    for (final String perm : pPermissions)
    {
      final String prefix = buildPermission(resolveProjectId(perm), Application.class);
      if ((prefix != null) && (perm.startsWith(prefix)))
      {
        final String application = resolveApplicationId(perm);
        if (application != null)
        {
          appliIds.add(application);
        }
      }
    }
    return appliIds;
  }

  private String resolveApplicationId(final String pPermissions)
  {
    String returnId = null;
    final String[] perm = pPermissions.split(PERMISSION_SEPARATOR);
    // perm.length should be higher than 5, otherwise it is not a resource(application) permission
    if (perm.length == 5)
    {
      returnId = perm[3];
    }
    return returnId;
  }

  /**
   * Build a permission name from project Id and a specific class resource
   * 
   * @param pProjectId
   *          represents project Id used to build permission name
   * @param pResourceClass
   *          represents resource class used to build permission name
   */
  private String buildPermission(final String pProjectId, final Class<?> pResourceClass)
  {
    final StringBuilder permission = new StringBuilder(PROJECT);
    permission.append(PERMISSION_SEPARATOR);
    permission.append(pProjectId);
    permission.append(PERMISSION_SEPARATOR);
    if (Membership.class.isAssignableFrom(pResourceClass))
    {
      permission.append(MEMBERSHIP);
    }
    else if (Role.class.isAssignableFrom(pResourceClass))
    {
      permission.append(ROLE);
    }
    else if (Group.class.isAssignableFrom(pResourceClass))
    {
      permission.append(GROUP);
    }
    else if (Space.class.isAssignableFrom(pResourceClass))
    {
      permission.append(SPACE);
    }
    else if (Application.class.isAssignableFrom(pResourceClass))
    {
      permission.append(APPLICATION);
    }
    else if (Composition.class.isAssignableFrom(pResourceClass))
    {
      permission.append(COMMUNICATION);
    }
    else if (MembershipRequest.class.isAssignableFrom(pResourceClass))
    {
      permission.append(MEMBERSHIP_REQUEST);
    }
    else if (DashBoard.class.isAssignableFrom(pResourceClass))
    {
      permission.append(DASHBOARD);
    }
    else if (Project.class.isAssignableFrom(pResourceClass))
    {
      // Nothing to do in this case
    }
    else
    {
      permission.append(UNKNOWN);
    }
    if (!Project.class.isAssignableFrom(pResourceClass))
    {
      permission.append(PERMISSION_SEPARATOR);
    }
    return permission.toString();
  }
}
