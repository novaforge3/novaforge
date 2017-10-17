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

/**
 * @author lamirang
 */
public class ApplicationNodeDTO implements Serializable, NodeDTO
{
	/**
    * 
    */
	private static final long serialVersionUID = 6874745170219119125L;
	private String            uri;
	private String            name;
	private SpaceNodeDTO      spaceNode;
	private String            pluginUUID;
	private String            instanceId;

	public ApplicationNodeDTO()
	{
		// Used for serialization
	}

	/**
	 * @param pName
	 * @param pUri
	 */
	public ApplicationNodeDTO(final String pUri, final String pName)
	{
		uri = pUri;
		name = pName;
	}

	/**
	 * @return the uri
	 */
	@Override
	public String getUri()
	{
		return uri;
	}

	/**
	 * @param pUri
	 *          the uri to set
	 */
	public void setUri(final String pUri)
	{
		uri = pUri;
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
	 * @param pName
	 *          the name to set
	 */
	public void setName(final String pName)
	{
		name = pName;
	}

	/**
	 * @return the spaceNode
	 */
	public SpaceNodeDTO getSpaceParent()
	{
		return spaceNode;
	}

	/**
	 * @param spaceNode
	 *          the spaceNode to set
	 */
	public void setSpaceParent(final SpaceNodeDTO spaceNode)
	{
		this.spaceNode = spaceNode;
	}

	/**
	 * @return the instanceId
	 */
	public String getInstanceId()
	{
		return instanceId;
	}

	/**
	 * @param pInstanceId
	 *          the instanceId to set
	 */
	public void setInstanceId(final String pInstanceId)
	{
		instanceId = pInstanceId;
	}

	/**
	 * @return the plugin uuid
	 */
	public String getPluginUUID()
	{
		return pluginUUID;
	}

	/**
	 * @param pPluginUUID
	 *          the plugin to set
	 */
	public void setPluginUUID(final String pPluginUUID)
	{
		pluginUUID = pPluginUUID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((instanceId == null) ? 0 : instanceId.hashCode());
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((pluginUUID == null) ? 0 : pluginUUID.hashCode());
		result = (prime * result) + ((uri == null) ? 0 : uri.hashCode());
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
		if (!(obj instanceof ApplicationNodeDTO))
		{
			return false;
		}
		final ApplicationNodeDTO other = (ApplicationNodeDTO) obj;
		if (instanceId == null)
		{
			if (other.instanceId != null)
			{
				return false;
			}
		}
		else if (!instanceId.equals(other.instanceId))
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
		if (pluginUUID == null)
		{
			if (other.pluginUUID != null)
			{
				return false;
			}
		}
		else if (!pluginUUID.equals(other.pluginUUID))
		{
			return false;
		}
		if (uri == null)
		{
			if (other.uri != null)
			{
				return false;
			}
		}
		else if (!uri.equals(other.uri))
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
		return "ApplicationNodeDTO [uri=" + uri + ", name=" + name + ", pluginUUID=" + pluginUUID + ", instanceId="
							 + instanceId + "]";
	}

}
