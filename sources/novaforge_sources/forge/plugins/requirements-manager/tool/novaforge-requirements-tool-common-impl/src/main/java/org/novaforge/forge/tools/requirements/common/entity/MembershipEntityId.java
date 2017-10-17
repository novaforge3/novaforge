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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author sbenoist
 */
@Embeddable
public class MembershipEntityId implements Serializable
{

  private static final long serialVersionUID = 8531825684130883574L;

  @Column(name = "user_id")
  private Long              userId;

  @Column(name = "role_id")
  private Long              roleId;

  @Column(name = "project_id")
  private Long              projectId;

  public MembershipEntityId()
  {
    super();
  }

  public MembershipEntityId(final Long pUserId, final Long pRoleId, final Long pProjectId)
  {
    super();
    userId = pUserId;
    roleId = pRoleId;
    projectId = pProjectId;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(userId).append(roleId).append(projectId).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof MembershipEntityId))
    {
      return false;
    }
    final MembershipEntityId castOther = (MembershipEntityId) other;
    return new EqualsBuilder().append(userId, castOther.getUserId()).append(roleId, castOther.getRoleId())
                              .append(projectId, castOther.getProjectId()).isEquals();
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId(final Long pUserId)
  {
    userId = pUserId;
  }

  public Long getRoleId()
  {
    return roleId;
  }

  public void setRoleId(final Long pRoleId)
  {
    roleId = pRoleId;
  }

  public Long getProjectId()
  {
    return projectId;
  }

  public void setProjectId(final Long pProjectId)
  {
    projectId = pProjectId;
  }

}
