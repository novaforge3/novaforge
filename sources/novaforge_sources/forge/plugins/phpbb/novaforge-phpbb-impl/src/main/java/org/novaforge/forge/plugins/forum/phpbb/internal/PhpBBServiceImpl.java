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
package org.novaforge.forge.plugins.forum.phpbb.internal;

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
import org.novaforge.forge.plugins.forum.phpbb.model.PhpBBRolePrivilege;
import org.novaforge.forge.plugins.forum.phpbb.services.PhpBBConfigurationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public class PhpBBServiceImpl extends AbstractPluginService implements PluginService
{

	/**
	 * Default constructor
	 * 
	 * @param pUuidDAO
	 *          uuiDAO service reference
	 */
	public PhpBBServiceImpl(final UuidDAO pUuidDAO)
	{
		super(pUuidDAO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getIconPath()
	{
		return "/phpbb.png";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<PluginView> buildPluginViews()
	{
		final List<PluginView> viewList = new ArrayList<PluginView>();

		viewList.add(new PluginViewImpl(PluginViewEnum.DEFAULT, getDefaultAccess()));
		PhpBBConfigurationService phpBBConfiguration = getPluginConfigurationService(PhpBBConfigurationService.class);
		viewList.add(new PluginViewImpl(PluginViewEnum.ADMINISTRATION, phpBBConfiguration.getAdminAccess()));
		return viewList;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws PluginServiceException
	 */
	@Override
	public PluginServiceMetadata getMetadata() throws PluginServiceException
	{
		PluginServiceMetadata pluginServiceMetadata = null;
		if (getServiceUUID() != null)
		{
			pluginServiceMetadata = new PluginMetadataImpl(getServiceUUID(), getDescription(), getType(),
			    Category.FORUM.getId(), new PhpBBQueues(), getPluginViews(), getVersion());
		}
		return pluginServiceMetadata;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> findRoles() throws PluginServiceException
	{
		final Set<String> returnList = new HashSet<String>();
		for (final PhpBBRolePrivilege roles : PhpBBRolePrivilege.values())
		{
			returnList.add(roles.getLabel());
		}
		return returnList;
	}

}
