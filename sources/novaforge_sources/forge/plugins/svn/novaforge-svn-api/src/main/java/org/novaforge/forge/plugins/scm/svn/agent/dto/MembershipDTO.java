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

package org.novaforge.forge.plugins.scm.svn.agent.dto;

import org.novaforge.forge.plugins.scm.svn.domain.Membership;
import org.novaforge.forge.plugins.scm.svn.domain.Role;

/**
 * @author rols-p
 */
public class MembershipDTO implements Membership
{

	private String name;
	private String password;
	private Role   role;

	public MembershipDTO()
	{
		super();
	}

	/**
	 * @param name
	 * @param password
	 * @param role
	 */
	public MembershipDTO(final String name, final String password, final Role role)
	{
		this.name = name;
		this.password = password;
		this.role = role;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *          the name to set
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the password
	 */
	@Override
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *          the password to set
	 */
	@Override
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "MembershipDTO [name=" + name + ", password=" + password + ", role=" + role + "]";
	}

	/**
	 * @return the role
	 */
	@Override
	public Role getRole()
	{
		return role;
	}

	/**
	 * @param role
	 *          the role to set
	 */
	@Override
	public void setRole(final Role role)
	{
		this.role = role;
	}

}
