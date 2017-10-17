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
package org.novaforge.forge.plugins.gcl.nexus.internal;

import org.novaforge.forge.core.plugins.categories.Category;
import org.novaforge.forge.core.plugins.dao.UuidDAO;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata;
import org.novaforge.forge.core.plugins.domain.plugin.PluginView;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginService;
import org.novaforge.forge.plugins.commons.services.domains.PluginMetadataImpl;
import org.novaforge.forge.plugins.commons.services.domains.PluginViewImpl;
import org.novaforge.forge.plugins.gcl.nexus.domain.NexusRolePrivilege;
import org.novaforge.forge.plugins.gcl.nexus.services.NexusConfigurationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is an implementation of PluginService for nexus plugin.
 * 
 * @see org.novaforge.forge.plugins.commons.services.AbstractPluginService
 * @see org.novaforge.forge.core.plugins.services.PluginService
 * @author lamirang
 */
public class NexusServiceImpl extends AbstractPluginService implements PluginService
{

	/**
	 * Default constructor
	 * 
	 * @param pUuidDAO
	 *          uuiDAO service reference
	 */
	public NexusServiceImpl(final UuidDAO pUuidDAO)
	{
		super(pUuidDAO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getIconPath()
	{
		return "/nexus.png";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<PluginView> buildPluginViews()
	{
		final List<PluginView> viewList = new ArrayList<PluginView>();

		viewList.add(new PluginViewImpl(PluginViewEnum.DEFAULT, getDefaultAccess()));
		viewList.add(new PluginViewImpl(PluginViewEnum.ADMINISTRATION, getAdminAccess()));
		return viewList;
	}

	/**
	 * @return admin access alias
	 */
	private String getAdminAccess()
	{
		return getPluginConfigurationService(NexusConfigurationService.class).getAdminAccess();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PluginServiceMetadata getMetadata() throws PluginServiceException
	{
		PluginServiceMetadata pluginServiceMetadata = null;
		if (getServiceUUID() != null)
		{
			pluginServiceMetadata = new PluginMetadataImpl(getServiceUUID(), getDescription(), getType(),
			    Category.REPOSITORYMANAGEMENT.getId(), new NexusQueues(), getPluginViews(), getVersion());
		}
		return pluginServiceMetadata;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> findRoles()
	{
		final Set<String> returnList = new HashSet<String>();
		for (final NexusRolePrivilege role : NexusRolePrivilege.values())
		{
			returnList.add(role.getLabel());
		}
		return returnList;
	}

}
