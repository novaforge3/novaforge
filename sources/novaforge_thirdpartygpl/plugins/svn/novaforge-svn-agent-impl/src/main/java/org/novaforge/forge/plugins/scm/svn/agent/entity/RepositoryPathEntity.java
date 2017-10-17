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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.plugins.scm.svn.domain.Repository;
import org.novaforge.forge.plugins.scm.svn.domain.RepositoryPath;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "SVN_REPOSITORY_PATH")
@NamedQueries({
    @NamedQuery(name = "RepositoryPathEntity.findAllPathsByRepository", query = "SELECT p FROM RepositoryPathEntity p WHERE p.repository.name = :repositoryName"),
    @NamedQuery(name = "RepositoryPathEntity.findRepositoryPathByRepositoryAndPath", query = "SELECT p FROM RepositoryPathEntity p WHERE p.repository.name = :repositoryName AND p.path = :path") })
public class RepositoryPathEntity implements RepositoryPath, Serializable
{
	private static final long serialVersionUID = 1752795398729691727L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long              id;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, targetEntity = RepositoryEntity.class)
	@JoinColumn(nullable = false, name = "repository_id")
	private Repository        repository;

	@Column(name = "path", nullable = false)
	private String            path;

	public RepositoryPathEntity()
	{
		super();
	}

	public RepositoryPathEntity(final Repository repository, final String path)
	{
		super();
		this.repository = repository;
		this.path = path;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Repository getRepository()
	{
		return repository;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setRepository(final Repository repository)
	{
		this.repository = repository;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getPath()
	{
		return path;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setPath(final String path)
	{
		this.path = path;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (!(other instanceof RepositoryPathEntity))
		{
			return false;
		}
		RepositoryPathEntity castOther = (RepositoryPathEntity) other;
		return new EqualsBuilder().append(repository, castOther.getRepository())
		    .append(path, castOther.getPath()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(repository).append(path).toHashCode();
	}
}
