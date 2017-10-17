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
import java.util.HashSet;
import java.util.Set;

/**
 * @author lamirang
 */
public class PluginDTO implements Serializable
{
	/**
    * 
    */
	private static final long serialVersionUID = 5916251234492847865L;
	private String            pluginId;
	private String            pluginCategory;
	private String            pluginType;
	private Set<String>       roles            = new HashSet<String>();

	/**
	 * @return the pluginId
	 */
	public String getPluginId()
	{
		return pluginId;
	}

	/**
	 * @param pPluginId
	 *          the pluginId to set
	 */
	public void setPluginId(final String pPluginId)
	{
		pluginId = pPluginId;
	}

	/**
	 * @return the roles
	 */
	public Set<String> getRoles()
	{
		return roles;
	}

	/**
	 * @param pRoles
	 *          the roles to set
	 */
	public void setRoles(final Set<String> pRoles)
	{
		roles = pRoles;
	}

	/**
	 * @return the pluginCategory
	 */
	public String getPluginCategory()
	{
		return pluginCategory;
	}

	/**
	 * @param pluginCategory
	 *          the pluginCategory to set
	 */
	public void setPluginCategory(final String pluginCategory)
	{
		this.pluginCategory = pluginCategory;
	}

	/**
	 * @return the pluginType
	 */
	public String getPluginType()
	{
		return pluginType;
	}

	/**
	 * @param pluginType
	 *          the pluginType to set
	 */
	public void setPluginType(final String pluginType)
	{
		this.pluginType = pluginType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((pluginCategory == null) ? 0 : pluginCategory.hashCode());
		result = (prime * result) + ((pluginId == null) ? 0 : pluginId.hashCode());
		result = (prime * result) + ((pluginType == null) ? 0 : pluginType.hashCode());
		result = (prime * result) + ((roles == null) ? 0 : roles.hashCode());
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
		final PluginDTO other = (PluginDTO) obj;
		if (pluginCategory == null)
		{
			if (other.pluginCategory != null)
			{
				return false;
			}
		}
		else if (!pluginCategory.equals(other.pluginCategory))
		{
			return false;
		}
		if (pluginId == null)
		{
			if (other.pluginId != null)
			{
				return false;
			}
		}
		else if (!pluginId.equals(other.pluginId))
		{
			return false;
		}
		if (pluginType == null)
		{
			if (other.pluginType != null)
			{
				return false;
			}
		}
		else if (!pluginType.equals(other.pluginType))
		{
			return false;
		}
		if (roles == null)
		{
			if (other.roles != null)
			{
				return false;
			}
		}
		else if (!roles.equals(other.roles))
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
		return "PluginDTO [pluginId=" + pluginId + ", pluginCategory=" + pluginCategory + ", pluginType=" + pluginType
							 + ", roles=" + roles + "]";
	}
}
