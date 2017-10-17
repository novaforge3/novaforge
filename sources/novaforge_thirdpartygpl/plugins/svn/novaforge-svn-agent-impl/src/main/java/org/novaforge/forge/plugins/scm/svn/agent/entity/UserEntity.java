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
import org.novaforge.forge.plugins.scm.svn.domain.User;

/**
 * @author sbenoist
 */
@Entity
@Table(name = "SVN_USER")
@NamedQuery(name = "UserEntity.findByName", query = "SELECT p FROM UserEntity p WHERE p.name = :name")
public class UserEntity implements User, Serializable
{
	private static final long serialVersionUID = 3771420798532407494L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long              id;

	@Column(name = "name", nullable = false, unique = true)
	private String            name;

	@Column(name = "password", nullable = false)
	private String            password;

	public UserEntity()
	{
		super();
	}

	public UserEntity(final String name, final String password)
	{
		super();
		this.name = name;
		this.password = password;
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
	public String getName()
	{
		return name;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (!(other instanceof UserEntity))
		{
			return false;
		}
		UserEntity castOther = (UserEntity) other;
		return new EqualsBuilder().append(id, castOther.getId()).append(name, castOther.getName()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(id).append(name).toHashCode();
	}

	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public void setPassword(final String password)
	{
		this.password = password;

	}

}
