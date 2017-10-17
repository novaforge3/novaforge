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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.plugins.scm.svn.domain.RepositoryPath;
import org.novaforge.forge.plugins.scm.svn.domain.User;
import org.novaforge.forge.plugins.scm.svn.domain.UserPermission;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "SVN_USER_PERMISSION")
@NamedQueries({
    @NamedQuery(name = "UserPermissionEntity.findAllPermissionsByRepositoryPath", query = "SELECT p FROM UserPermissionEntity p WHERE p.repositoryPath.id = :repositoryPathId"),
    @NamedQuery(name = "UserPermissionEntity.findAllPermissionsByUser", query = "SELECT p FROM UserPermissionEntity p WHERE p.user.name = :username"),
    @NamedQuery(name = "UserPermissionEntity.findPermissionsByUserAndPath", query = "SELECT p FROM UserPermissionEntity p WHERE p.user.name = :username AND p.repositoryPath.id =:repositoryPathId") })
public class UserPermissionEntity implements UserPermission, Serializable
{

	private static final long      serialVersionUID = -8811615562435966419L;

	@EmbeddedId
	private UserPermissionEntityId id               = new UserPermissionEntityId();

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private User                   user;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = RepositoryPathEntity.class)
	@JoinColumn(name = "repository_path_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private RepositoryPath         repositoryPath;

	@Column(name = "read_permission", nullable = false, columnDefinition = "TINYINT")
	private boolean                read             = false;

	@Column(name = "write_permission", nullable = false, columnDefinition = "TINYINT")
	private boolean                write            = false;

	@Column(name = "recursive_permission", nullable = false, columnDefinition = "TINYINT")
	private boolean                recursive        = false;

	public UserPermissionEntity()
	{
		super();
	}

	public UserPermissionEntity(final User user, final RepositoryPath repositoryPath)
	{
		this(user, repositoryPath, false, false, false);
	}

	public UserPermissionEntity(final User user, final RepositoryPath repositoryPath, final boolean read,
	    final boolean write, final boolean recursive)
	{
		super();
		this.user = user;
		this.repositoryPath = repositoryPath;
		this.read = read;
		this.write = write;
		this.recursive = recursive;

		UserEntity userEntity = (UserEntity) user;
		RepositoryPathEntity repositoryPathEntity = (RepositoryPathEntity) repositoryPath;
		setId(new UserPermissionEntityId(userEntity.getId(), repositoryPathEntity.getId()));
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public User getUser()
	{
		return user;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setUser(final User user)
	{
		this.user = user;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public RepositoryPath getRepositoryPath()
	{
		return repositoryPath;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setRepositoryPath(final RepositoryPath repositoryPath)
	{
		this.repositoryPath = repositoryPath;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean isRead()
	{
		return read;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setRead(final boolean read)
	{
		this.read = read;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean isWrite()
	{
		return write;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setWrite(final boolean write)
	{
		this.write = write;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean isRecursive()
	{
		return recursive;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setRecursive(final boolean recursive)
	{
		this.recursive = recursive;
	}

	/**
	 * @inheritDoc
	 */
	public UserPermissionEntityId getId()
	{
		return id;
	}

	public void setId(final UserPermissionEntityId pId)
	{
		id = pId;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (!(other instanceof UserPermissionEntity))
		{
			return false;
		}
		UserPermissionEntity castOther = (UserPermissionEntity) other;
		return new EqualsBuilder().append(user, castOther.getUser())
		    .append(repositoryPath, castOther.getRepositoryPath()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(user).append(repositoryPath).toHashCode();
	}
}
