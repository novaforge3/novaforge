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
package org.novaforge.forge.core.organization.data;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.core.organization.entity.ActorEntity;
import org.novaforge.forge.core.organization.entity.GroupEntity;
import org.novaforge.forge.core.organization.entity.ProjectRoleEntity;
import org.novaforge.forge.core.organization.entity.UserEntity;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;

import java.io.Serializable;

/**
 * @author Guillaume Lamirand
 */
public class MembershipInfoImpl implements MembershipInfo, Serializable
{
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private String            projectId;
  private Actor             actor;
  private ProjectRole       role;

  private boolean           priority;

  public MembershipInfoImpl()
  {
    super();
  }

  public MembershipInfoImpl(final String pProjectId, final Actor pActor, final ProjectRole pRole,
      final boolean priority)
  {
    actor = pActor;
    role = pRole;
    projectId = pProjectId;
    this.priority = priority;
  }

  /*
   * This constructor is used by OpenJPA during JPQL requests
   */
  public MembershipInfoImpl(final String pProjectId, final ActorEntity pActor, final ProjectRoleEntity pRole,
      final boolean priority)
  {
    actor = pActor;
    role = pRole;
    projectId = pProjectId;
    this.priority = priority;
  }

  public MembershipInfoImpl(final String pProjectId, final UserEntity pUser, final ProjectRoleEntity pRole,
      final boolean priority)
  {
    actor = pUser;
    role = pRole;
    projectId = pProjectId;
    this.priority = priority;
  }

  public MembershipInfoImpl(final String pProjectId, final GroupEntity pGroup, final ProjectRoleEntity pRole,
      final boolean priority)
  {
    actor = pGroup;
    role = pRole;
    projectId = pProjectId;
    this.priority = priority;
  }

  @Override
  public Actor getActor()
  {
    return actor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectRole getRole()
  {
    return role;
  }

  @Override
  public String getProjectId()
  {
    return projectId;
  }

  @Override
  public boolean getPriority()
  {
    return priority;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(projectId).append(role.getName()).append(actor.getUuid()).toHashCode();
  }

  /**
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(Object)
   */
  @Override
  public boolean equals(final Object pOther)
  {
    if (this == pOther)
    {
      return true;
    }
    if (!(pOther instanceof MembershipInfoImpl))
    {
      return false;
    }
    final MembershipInfoImpl castOther = (MembershipInfoImpl) pOther;
    return new EqualsBuilder().append(projectId, castOther.projectId).append(role, castOther.role)
        .append(actor, castOther.actor).isEquals();
  }

}
