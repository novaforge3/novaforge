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

public class RoleDTO implements Serializable, Comparable<RoleDTO>
{
	/**
    * 
    */
	private static final long serialVersionUID = 8915661243463330601L;
	private String            name;
	private String            description;
	private Integer           order;
	private boolean           mandatory        = false;

	public RoleDTO()
	{
		// Used for serialization
		super();
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param pName
	 *          the name to set
	 */
	public void setName(final String pName)
	{
		name = pName;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param pDescription
	 *          the description to set
	 */
	public void setDescription(final String pDescription)
	{
		description = pDescription;
	}

	/**
	 * @return the mandatory
	 */
	public boolean isMandatory()
	{
		return mandatory;
	}

	/**
	 * @param pMandatory
	 *          the mandatory to set
	 */
	public void setMandatory(final boolean pMandatory)
	{
		mandatory = pMandatory;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((description == null) ? 0 : description.hashCode());
		result = (prime * result) + (mandatory ? 1231 : 1237);
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((order == null) ? 0 : order.hashCode());
		return result;
	}

	/**
	 * @inheritDoc
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
		final RoleDTO other = (RoleDTO) obj;
		if (description == null)
		{
			if (other.description != null)
			{
				return false;
			}
		}
		else if (!description.equals(other.description))
		{
			return false;
		}
		if (mandatory != other.mandatory)
		{
			return false;
		}
		if (name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		}
		else if (!name.equals(other.name))
		{
			return false;
		}
		if (order == null)
		{
			if (other.order != null)
			{
				return false;
			}
		}
		else if (!order.equals(other.order))
		{
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "RoleDTO [name=" + name + ", description=" + description + ", order=" + order + ", mandatory=" + mandatory
							 + "]";
	}

	/**
	 * Take care that order=1 > order=10 (inverse to the integers order)
	 */
	@Override
	public int compareTo(final RoleDTO pRole)
	{
		return order.compareTo(pRole.getOrder());
	}

	/**
	 * @return the order
	 */
	public Integer getOrder()
	{
		return order;
	}

	/**
	 * @param order
	 *     the order to set
	 */
	public void setOrder(final Integer order)
	{
		this.order = order;
	}
}
