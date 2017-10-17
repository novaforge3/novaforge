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
package org.novaforge.forge.tools.managementmodule.entity;

import org.novaforge.forge.tools.managementmodule.domain.Language;
import org.novaforge.forge.tools.managementmodule.domain.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user")
@NamedQueries({ @NamedQuery(name = "UserEntity.findByLogin", query = "SELECT p FROM UserEntity p WHERE p.login = :login") })
public class UserEntity implements User, Serializable
{

	/** UID */
	private static final long serialVersionUID = -466447014812253376L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "lastname", nullable = false)
	private String						lastname;

	@Column(name = "firstname", nullable = false)
	private String						firstname;

	@Column(name = "login", nullable = false, unique = true)
	private String						login;

	@ManyToOne(targetEntity = LanguageEntity.class)
	private Language					language;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastName()
	{
		return lastname;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastName(final String lastname)
	{
		this.lastname = lastname;
	}

	@Override
	public String getFirstName()
	{
		return firstname;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFirstName(final String firstname)
	{
		this.firstname = firstname;
	}

	@Override
	public String getLogin()
	{
		return login;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLogin(final String login)
	{
		this.login = login;
	}

	@Override
	public Language getLanguage()
	{
		return language;
	}

	@Override
	public void setLanguage(final Language language)
	{
		this.language = language;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		UserEntity other = (UserEntity) obj;
		if (login == null)
		{
			if (other.login != null)
			{
				return false;
			}
		}
		else if (!login.equals(other.login))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "UserEntity [id=" + id + ", lastname=" + lastname + ", firstname=" + firstname + ", login="
				+ login + ", language=" + language + "]";
	}
}
