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
package org.novaforge.forge.ui.forge.reference.shared;

import java.io.Serializable;

public class RoleApplicationDTO implements Serializable
{
	/**
    * 
    */
	private static final long  serialVersionUID = -3766980178046089055L;
	private ApplicationNodeDTO application;
	private String             roleName;

	public RoleApplicationDTO()
	{
		// Used for serialization
		super();
	}

	public RoleApplicationDTO(final ApplicationNodeDTO pApplicationNodeDTO, final String pRoleName)
	{
		application = pApplicationNodeDTO;
		roleName = pRoleName;
	}

	/**
	 * @return the application
	 */
	public ApplicationNodeDTO getApplication()
	{
		return application;
	}

	/**
	 * @param application
	 *          the application to set
	 */
	public void setApplication(final ApplicationNodeDTO application)
	{
		this.application = application;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName()
	{
		return roleName;
	}

	/**
	 * @param roleName
	 *          the roleName to set
	 */
	public void setRoleName(final String roleName)
	{
		this.roleName = roleName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((application == null) ? 0 : application.hashCode());
		result = (prime * result) + ((roleName == null) ? 0 : roleName.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
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
		final RoleApplicationDTO other = (RoleApplicationDTO) obj;
		if (application == null)
		{
			if (other.application != null)
			{
				return false;
			}
		}
		else if (!application.equals(other.application))
		{
			return false;
		}
		if (roleName == null)
		{
			if (other.roleName != null)
			{
				return false;
			}
		}
		else if (!roleName.equals(other.roleName))
		{
			return false;
		}
		return true;
	}

}
