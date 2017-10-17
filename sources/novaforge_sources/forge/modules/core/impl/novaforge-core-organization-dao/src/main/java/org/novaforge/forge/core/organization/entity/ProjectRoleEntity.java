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
package org.novaforge.forge.core.organization.entity;

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.ProjectRole;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lamirang
 */
@Entity
@Table(name = "ROLE")
@FetchGroup(name = "role_permission", attributes = { @FetchAttribute(name = "permissions") })
public class ProjectRoleEntity extends RoleEntity implements ProjectRole
{
  private static final long serialVersionUID = -7926308168352876969L;

  @ManyToMany(fetch = FetchType.LAZY, targetEntity = PermissionEntity.class)
  @JoinTable(name = "ROLE_PERMISSION", joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  private List<Permission>  permissions      = new ArrayList<Permission>();

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Permission> getPermissions()
  {
    return Collections.unmodifiableList(new ArrayList<Permission>(permissions));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPermission(final Permission pPermission)
  {
    permissions.add(pPermission);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePermission(final Permission pPermission)
  {
    permissions.remove(pPermission);
  }

  /**
   * @param permissions
   *          the permissions to set
   */
  public void setPermissions(final List<Permission> permissions)
  {
    this.permissions = permissions;
  }
}
