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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.plugins.scm.svn.domain.Group;
import org.novaforge.forge.plugins.scm.svn.domain.GroupPermission;
import org.novaforge.forge.plugins.scm.svn.domain.RepositoryPath;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "SVN_GROUP_PERMISSION")
public class GroupPermissionEntity implements GroupPermission, Serializable
{
	private static final long serialVersionUID = -8085587286645616380L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long              id;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = GroupEntity.class)
	@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
	private Group             group;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = RepositoryPathEntity.class)
	@JoinColumn(name = "repository_path_id", referencedColumnName = "id", nullable = false)
	private RepositoryPath    repositoryPath;

	@Column(name = "read_permission", nullable = false, columnDefinition = "TINYINT")
	private boolean           read;

	@Column(name = "write_permission", nullable = false, columnDefinition = "TINYINT")
	private boolean           write;

	@Column(name = "recursive_permission", nullable = false, columnDefinition = "TINYINT")
	private boolean           recursive;

	public GroupPermissionEntity()
	{
		super();
	}

	public GroupPermissionEntity(final Group group, final RepositoryPath repositoryPath)
	{
		super();
		this.group = group;
		this.repositoryPath = repositoryPath;
	}

	/**
	 * @inheritDoc
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @inheritDoc
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Group getGroup()
	{
		return group;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setGroup(final Group group)
	{
		this.group = group;
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

	@Override
	public String toString()
	{
		return "GroupPermissionEntity [group=" + group + ", id=" + id + ", read=" + read + ", recursive="
		    + recursive + ", repositoryPath=" + repositoryPath + ", write=" + write + "]";
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (!(other instanceof GroupPermissionEntity))
		{
			return false;
		}
		GroupPermissionEntity castOther = (GroupPermissionEntity) other;
		return new EqualsBuilder().append(group, castOther.getGroup())
		    .append(repositoryPath, castOther.getRepositoryPath()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(group).append(repositoryPath).toHashCode();
	}
}
