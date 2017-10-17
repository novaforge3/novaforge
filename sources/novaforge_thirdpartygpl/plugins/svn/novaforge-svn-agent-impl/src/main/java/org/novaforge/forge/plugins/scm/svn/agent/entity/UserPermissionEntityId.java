/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.plugins.scm.svn.domain.UserPermissionId;

@Embeddable
public class UserPermissionEntityId implements UserPermissionId
{
	// This is a composite key

	private static final long serialVersionUID = 4856987482584795137L;

	@Column(name = "user_id")
	private Long              userId;

	@Column(name = "repository_path_id")
	private Long              repositoryPathId;

	public UserPermissionEntityId()
	{
		super();
	}

	public UserPermissionEntityId(Long userId, Long repositoryPathId)
	{
		super();
		this.userId = userId;
		this.repositoryPathId = repositoryPathId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getUserId()
	{
		return userId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRepositoryPathId(Long repositoryPathId)
	{
		this.repositoryPathId = repositoryPathId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getRepositoryPathId()
	{
		return repositoryPathId;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (!(other instanceof UserPermissionEntityId))
		{
			return false;
		}
		UserPermissionId castOther = (UserPermissionId) other;
		return new EqualsBuilder().append(userId, castOther.getUserId())
		    .append(repositoryPathId, castOther.getRepositoryPathId()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(userId).append(repositoryPathId).toHashCode();
	}
}