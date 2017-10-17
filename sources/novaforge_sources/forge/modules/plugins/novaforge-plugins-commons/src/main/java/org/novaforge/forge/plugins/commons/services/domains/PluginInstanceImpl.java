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

import org.novaforge.forge.core.plugins.domain.plugin.PluginInstance;

/**
 * This class is an implementation of PluginInterface Interface
 * 
 * @see org.novaforge.forge.core.plugins.domain.plugin.PluginInstance
 * @author lamirang
 */
public class PluginInstanceImpl implements PluginInstance
{

	private String forgeId;
	private String instanceId;
	private String instanceLabel;
	private String forgeProjectId;
	private String toolId;

	/**
	 * Public constructor used to create an instance
	 * 
	 * @param pInstanceId
	 *          represents the instance id
	 * @param pInstanceLabel
	 *          represents the instance label
	 */
	public PluginInstanceImpl(final String pInstanceId, final String pInstanceLabel)
	{
		instanceId = pInstanceId;
		instanceLabel = pInstanceLabel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getForgeId()
	{
		return forgeId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForgeId(final String pForgeId)
	{
		forgeId = pForgeId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInstanceId()
	{
		return instanceId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInstanceId(final String pInstanceId)
	{
		instanceId = pInstanceId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getForgeProjectId()
	{
		return forgeProjectId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForgeProjectId(final String pForgeProjectId)
	{
		forgeProjectId = pForgeProjectId;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInstanceLabel()
	{
		return instanceLabel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInstanceLabel(final String pInstanceLabel)
	{
		instanceLabel = pInstanceLabel;
	}

	@Override
	public String getToolInstanceId()
	{
		return toolId;
	}

	@Override
	public void setToolInstanceId(final String pPluginToolId)
	{
		toolId = pPluginToolId;
	}
}
