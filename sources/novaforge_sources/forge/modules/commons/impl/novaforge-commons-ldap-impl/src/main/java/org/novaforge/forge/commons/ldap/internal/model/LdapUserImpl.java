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
package org.novaforge.forge.commons.ldap.internal.model;

import org.novaforge.forge.commons.ldap.model.LdapUser;

/**
 * @author petrettf
 */
public class LdapUserImpl implements LdapUser
{
	/**
	 * Serial version uid
	 */
	private static final long	serialVersionUID	= -1588994578050317418L;

	private String						dn;
	private String						uid;
	private String						userPassword;
	private String						userPasswordEncodage;
	private String						surName;
	private String						givenName;
	private String						mail;
	private String						preferredLanguage;

	/**
    */
	public LdapUserImpl()
	{
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getDn()
	{
		return dn;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setDn(String dn)
	{
		this.dn = dn;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getUid()
	{
		return uid;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setUid(String uid)
	{
		this.uid = uid;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getUserPassword()
	{
		return userPassword;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setUserPassword(String userPassword)
	{
		this.userPassword = userPassword;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getUserPasswordEncodage()
	{
		return userPasswordEncodage;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setUserPasswordEncodage(String userPasswordEncodage)
	{
		this.userPasswordEncodage = userPasswordEncodage;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getSurName()
	{
		return surName;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setSurName(String surName)
	{
		this.surName = surName;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getGivenName()
	{
		return givenName;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setGivenName(String givenName)
	{
		this.givenName = givenName;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getMail()
	{
		return mail;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setMail(String mail)
	{
		this.mail = mail;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getPreferredLanguage()
	{
		return preferredLanguage;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setPreferredLanguage(String preferredLanguage)
	{
		this.preferredLanguage = preferredLanguage;
	}
}
