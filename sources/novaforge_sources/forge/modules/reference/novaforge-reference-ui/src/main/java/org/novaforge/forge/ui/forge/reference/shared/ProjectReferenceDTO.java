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
public class ProjectReferenceDTO implements Serializable
{
	/**
    * 
    */
	private static final long serialVersionUID = -8600405196566526312L;
	private String            idProject;
	private String            name;
	private String            description;
	private RootNodeDTO       rootNode;

	public ProjectReferenceDTO()
	{
		super();
	}

	/**
	 * @return
	 */
	public String getId()
	{
		return idProject;
	}

	/**
	 * @param idProject
	 */
	public void setId(final String id)
	{
		idProject = id;
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param projectName
	 */
	public void setName(final String pName)
	{
		name = pName;
	}

	/**
	 * @return
	 */
	public String getDescription()
	{

		return description;
	}

	/**
	 * @param projectDescription
	 */
	public void setDescription(final String pDescription)
	{
		description = pDescription;
	}

	/**
	 * @return
	 */
	public RootNodeDTO getRootNode()
	{
		return rootNode;
	}

	/**
	 * @param rootNode
	 */
	public void setRootNode(final RootNodeDTO rootNode)
	{
		this.rootNode = rootNode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((description == null) ? 0 : description.hashCode());
		result = (prime * result) + ((idProject == null) ? 0 : idProject.hashCode());
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((rootNode == null) ? 0 : rootNode.hashCode());
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
		if (!(obj instanceof ProjectReferenceDTO))
		{
			return false;
		}
		final ProjectReferenceDTO other = (ProjectReferenceDTO) obj;
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
		if (idProject == null)
		{
			if (other.idProject != null)
			{
				return false;
			}
		}
		else if (!idProject.equals(other.idProject))
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
		if (rootNode == null)
		{
			if (other.rootNode != null)
			{
				return false;
			}
		}
		else if (!rootNode.equals(other.rootNode))
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
		return "ProjectReferenceDTO [idProject=" + idProject + ", name=" + name + ", description=" + description
							 + ", rootNode=" + rootNode + "]";
	}

}
