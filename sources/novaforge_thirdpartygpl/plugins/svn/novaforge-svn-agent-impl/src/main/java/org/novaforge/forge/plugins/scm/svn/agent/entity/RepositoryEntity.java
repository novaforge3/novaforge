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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.plugins.scm.svn.domain.Repository;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "SVN_REPOSITORY")
@NamedQuery(name = "RepositoryEntity.findByName", query = "SELECT p FROM RepositoryEntity p WHERE p.name = :name")
public class RepositoryEntity implements Repository, Serializable
{
	private static final long serialVersionUID = 711619220850396469L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long              id;

	@Column(name = "name", nullable = false, unique = true)
	private String            name;

	public RepositoryEntity()
	{
		super();
	}

	public RepositoryEntity(final String name)
	{
		super();
		this.name = name;
	}

	public Long getId()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "RepositoryEntity [id=" + id + ", name=" + name + "]";
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (!(other instanceof RepositoryEntity))
		{
			return false;
		}
		RepositoryEntity castOther = (RepositoryEntity) other;
		return new EqualsBuilder().append(name, castOther.getName()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(name).toHashCode();
	}

}
