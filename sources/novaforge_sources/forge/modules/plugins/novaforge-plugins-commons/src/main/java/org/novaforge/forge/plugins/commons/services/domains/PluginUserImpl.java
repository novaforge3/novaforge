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
package org.novaforge.forge.plugins.commons.services.domains;

import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;

/**
 * This class is an implementation of PluginUser Interface
 * 
 * @see org.novaforge.forge.plugins.domain.plugin.PluginUser
 * @author lamirang
 */
public class PluginUserImpl implements PluginUser
{

	/**
    * 
    */
	private static final long serialVersionUID = 6923098324005631577L;

	/**
	 * Define user login
	 */
	private String            login;

	/**
	 * Define user password TODO What type of password to we used in clear or SHA-1?
	 */
	private String            password;

	/**
	 * Define first name
	 */
	private String            firstName;

	/**
	 * Define lastname
	 */
	private String            name;

	/**
	 * Define email
	 */
	private String            email;

	/**
	 * Define language
	 */
	private String            language;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLogin()
	{
		return login;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLogin(final String pLogin)
	{
		login = pLogin;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPassword()
	{
		return password;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPassword(final String pPassword)
	{
		password = pPassword;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFirstName(final String pFirstName)
	{
		firstName = pFirstName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(final String pName)
	{
		name = pName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEmail()
	{
		return email;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEmail(final String pEmail)
	{
		email = pEmail;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLanguage()
	{
		return language;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLanguage(final String pLanguage)
	{
		language = pLanguage;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString()
	{
		return "PluginUserImpl [login=" + login + ", firstName=" + firstName
		    + ", name=" + name + ", email=" + email + ", language=" + language + "]";
	}
}
