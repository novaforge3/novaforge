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
package org.novaforge.forge.ui.forge.reference.shared.template;

import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lamirang
 */
public class TemplateDTO implements Serializable
{

	public static final  String TEMPLATE_STATUS_IN_PROGRESS = "IN_PROGRESS";
	public static final  String TEMPLATE_STATUS_ENABLE      = "ENABLE";
	/**
	 *
	 */
	private static final long   serialVersionUID            = -8716270406520754772L;
	private String              idTemplate;
	private String              name;
	private String              userCreator;
	private String              description;
	private String              licence;
	private TemplateRootNodeDTO rootNode;
	private String              status;
	private Date                createdDate;
	private Date                modifiedDate;
	private List<RoleDTO> roles = new ArrayList<RoleDTO>();

	public TemplateDTO()
	{
		super();
		rootNode = new TemplateRootNodeDTO();
	}

	/**
	 * @return
	 */
	public String getId()
	{
		return idTemplate;
	}

	/**
	 * @param idTemplate
	 */
	public void setId(final String id)
	{
		idTemplate = id;
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
	public String getLicence()
	{
		return licence;

	}

	/**
	 * @param licence
	 */
	public void setLicence(final String licence)
	{
		this.licence = licence;
	}

	/**
	 * @return
	 */
	public TemplateRootNodeDTO getRootNode()
	{
		return rootNode;
	}

	/**
	 * @param rootNode
	 */
	public void setRootNode(final TemplateRootNodeDTO rootNode)
	{
		this.rootNode = rootNode;
	}

	/**
	 * @return
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the projectCreator
	 */
	public String getProjectCreator()
	{
		return userCreator;
	}

	/**
	 * @param projectCreator
	 *          the projectCreator to set
	 */
	public void setProjectCreator(final String projectCreator)
	{
		userCreator = projectCreator;
	}

	/**
	 * This method returns the date of creation of the project
	 *
	 * @return Date
	 */
	public Date getCreatedDate()
	{
		return createdDate;

	}

	/**
	 * @param pCreatedDate
	 *          the createdDate to set
	 */
	public void setCreatedDate(final Date pCreatedDate)
	{
		createdDate = pCreatedDate;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate()
	{
		return modifiedDate;
	}

	/**
	 * @param pModifiedDate
	 *          the modifiedDate to set
	 */
	public void setModifiedDate(final Date pModifiedDate)
	{
		modifiedDate = pModifiedDate;
	}

	public List<RoleDTO> getRoles()
	{
		if (roles == null)
		{
			roles = new ArrayList<RoleDTO>();
		}
		return roles;
	}

	public void setRoles(final List<RoleDTO> roles)
	{
		this.roles = roles;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = (prime * result) + ((description == null) ? 0 : description.hashCode());
		result = (prime * result) + ((idTemplate == null) ? 0 : idTemplate.hashCode());
		result = (prime * result) + ((licence == null) ? 0 : licence.hashCode());
		result = (prime * result) + ((modifiedDate == null) ? 0 : modifiedDate.hashCode());
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((status == null) ? 0 : status.hashCode());
		result = (prime * result) + ((userCreator == null) ? 0 : userCreator.hashCode());
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
		final TemplateDTO other = (TemplateDTO) obj;
		if (createdDate == null)
		{
			if (other.createdDate != null)
			{
				return false;
			}
		}
		else if (!createdDate.equals(other.createdDate))
		{
			return false;
		}
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
		if (idTemplate == null)
		{
			if (other.idTemplate != null)
			{
				return false;
			}
		}
		else if (!idTemplate.equals(other.idTemplate))
		{
			return false;
		}
		if (licence == null)
		{
			if (other.licence != null)
			{
				return false;
			}
		}
		else if (!licence.equals(other.licence))
		{
			return false;
		}
		if (modifiedDate == null)
		{
			if (other.modifiedDate != null)
			{
				return false;
			}
		}
		else if (!modifiedDate.equals(other.modifiedDate))
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
		if (status == null)
		{
			if (other.status != null)
			{
				return false;
			}
		}
		else if (!status.equals(other.status))
		{
			return false;
		}
		if (userCreator == null)
		{
			if (other.userCreator != null)
			{
				return false;
			}
		}
		else if (!userCreator.equals(other.userCreator))
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
		return "TemplateDTO [idTemplate=" + idTemplate + ", name=" + name + ", userCreator=" + userCreator
							 + ", description=" + description + ", licence=" + licence + ", rootNode=" + rootNode + ", status="
							 + status + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + "]";
	}

}
