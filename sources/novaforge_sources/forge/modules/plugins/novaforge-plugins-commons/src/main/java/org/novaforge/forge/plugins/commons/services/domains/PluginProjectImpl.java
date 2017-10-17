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

import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;

/**
 * This is an implementation of PluginProject Interface
 * 
 * @see Open Declaration org.novaforge.forge.core.plugins.domain.plugin.PluginProject
 * @author lamirang
 */
public class PluginProjectImpl implements PluginProject
{

	/**
    * 
    */
	private static final long serialVersionUID = -8460106213946694749L;
	/**
	 * Project Id used in forge side
	 */
	private String            projectId;
	/**
	 * Project Name used in forge side
	 */
	private String            name;
	/**
	 * Project Description used in forge side
	 */
	private String            description;
	/**
	 * Project license
	 */
	private String            license;
	/**
	 * Project status used in forge side
	 */
	private String            status;
	/**
	 * Project's author in forge side
	 */
	private String            author;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProjectId()
	{
		return projectId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProjectId(final String pProjectId)
	{
		projectId = pProjectId;
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
	public String getDescription()
	{
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescription(final String pDescription)
	{
		description = pDescription;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLicense()
	{
		return license;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLicense(final String pLicense)
	{
		license = pLicense;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStatus()
	{
		return status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStatus(final String pStatus)
	{
		status = pStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor()
	{
		return author;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAuthor(final String pAuthor)
	{
		author = pAuthor;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString()
	{
		return "PluginProjectImpl [projectId=" + projectId + ", name=" + name + ", description=" + description
		    + ", license=" + license + ", status=" + status + ", author=" + author + "]";
	}

}
