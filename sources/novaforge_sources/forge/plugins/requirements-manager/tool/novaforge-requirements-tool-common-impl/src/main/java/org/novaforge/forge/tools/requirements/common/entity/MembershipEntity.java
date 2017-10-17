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
package org.novaforge.forge.tools.requirements.common.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.Membership;
import org.novaforge.forge.tools.requirements.common.model.Role;
import org.novaforge.forge.tools.requirements.common.model.User;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "MEMBERSHIP", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "project_id" }))
@NamedQueries({
    @NamedQuery(
        name = "MembershipEntity.findByUserAndProject",
        query = "SELECT m FROM MembershipEntity m WHERE m.user.login = :login AND m.project.projectId = :projectId"),
    @NamedQuery(name = "MembershipEntity.findAllByProject",
        query = "SELECT m FROM MembershipEntity m WHERE m.project.projectId = :projectId"),
    @NamedQuery(name = "MembershipEntity.findAllByUser",
        query = "SELECT m FROM MembershipEntity m WHERE m.user.login = :login") })
public class MembershipEntity implements Membership, Serializable
{

  private static final long  serialVersionUID = -3964869904693838561L;

  @EmbeddedId
  private MembershipEntityId id               = new MembershipEntityId();

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false,
      updatable = false)
  private User               user;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = RoleEntity.class)
  @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
  private Role               role;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
  @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false, insertable = false,
      updatable = false)
  private IProject           project;

  public MembershipEntity()
  {
    super();
  }

  public MembershipEntity(final User pUser, final IProject pProject, final Role pRole)
  {
    super();
    user = pUser;
    role = pRole;
    project = pProject;

    UserEntity userEntity = (UserEntity) user;
    RoleEntity roleEntity = (RoleEntity) role;
    ProjectEntity projectEntity = (ProjectEntity) project;
    setId(new MembershipEntityId(userEntity.getId(), roleEntity.getId(), projectEntity.getId()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser()
  {
    return user;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUser(final User pUser)
  {
    user = pUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IProject getProject()
  {
    return project;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProject(final IProject pProject)
  {
    project = pProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role getRole()
  {
    return role;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRole(final Role pRole)
  {
    role = pRole;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(id).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof MembershipEntity))
    {
      return false;
    }
    final MembershipEntity castOther = (MembershipEntity) other;
    return new EqualsBuilder().append(id, castOther.getId()).isEquals();
  }

  /**
   * @return
   */
  private MembershipEntityId getId()
  {
    return id;
  }

  private void setId(final MembershipEntityId pId)
  {
    id = pId;
  }

  @Override
  public String toString()
  {
    return "MembershipEntity [id=" + id + ", user=" + user + ", role=" + role + ", project=" + project + "]";
  }

}
