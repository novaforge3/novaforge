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
import java.util.ArrayList;
import java.util.List;

/**
 * @author lamirang
 */
public class SpaceNodeDTO implements Serializable, NodeDTO
{
	/**
    * 
    */
	private static final long        serialVersionUID = -9170826331530031113L;
	private String                   uri;
	private String                   name;
	private RootNodeDTO              rootNode;
	private List<ApplicationNodeDTO> childs           = new ArrayList<ApplicationNodeDTO>();

	public SpaceNodeDTO()
	{
		// Used for serialization
	}

	/**
	 * @param pName
	 * @param pUri
	 */
	public SpaceNodeDTO(final String pUri, final String pName)
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
	 * @param uri
	 *          the uri to set
	 */
	public void setUri(final String uri)
	{
		this.uri = uri;
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
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the rootNode
	 */
	public RootNodeDTO getRootNode()
	{
		return rootNode;
	}

	/**
	 * @param rootNode
	 *          the rootNode to set
	 */
	public void setRootNode(final RootNodeDTO rootNode)
	{
		this.rootNode = rootNode;
	}

	/**
	 * @return the childs
	 */
	public List<ApplicationNodeDTO> getApplications()
	{
		return childs;
	}

	/**
	 * @param rootNode
	 *          the rootNode to set
	 */
	public void setApplications(final List<ApplicationNodeDTO> pChilds)
	{
		childs = pChilds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((rootNode == null) ? 0 : rootNode.hashCode());
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
		if (!(obj instanceof SpaceNodeDTO))
		{
			return false;
		}
		final SpaceNodeDTO other = (SpaceNodeDTO) obj;
		if (childs == null)
		{
			if (other.childs != null)
			{
				return false;
			}
		}
		else if (!childs.equals(other.childs))
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
		return "SpaceNodeDTO [uri=" + uri + ", name=" + name + ", childs=" + childs + "]";
	}

}
