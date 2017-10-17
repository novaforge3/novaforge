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
package org.novaforge.forge.plugins.cms.spip.internal;

import org.novaforge.forge.core.plugins.categories.cms.CMSCategoryService;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.cms.spip.services.SpipConfigurationService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;

import java.util.Locale;

/**
 * @author rols-p
 */
public class SpipCategoryServiceImpl extends AbstractPluginCategoryService implements CMSCategoryService
{

	private static final String PROPERTY_FILE = "spip";
	/**
	 * Reference to service implementation of {@link InstanceConfigurationDAO}
	 */
	protected InstanceConfigurationDAO instanceConfigurationDAO;
	/**
	 * Reference to service implementation of {@link SpipConfigurationService}
	 */
	private SpipConfigurationService   pluginConfigurationService;

	@Override
	public String getApplicationAccessInfo(final String pInstanceId, final Locale pLocale)
	    throws PluginServiceException
	{
		final String url = getSiteUrl(pInstanceId);
		return getMessage(KEY_ACCESS_INFO, pLocale, url);
	}

	private String getSiteUrl(final String pInstanceId) throws PluginServiceException
	{
		try
		{
			final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
			return pluginConfigurationService.getSiteUrl(instance.getToolInstance().getBaseURL(),
			    instance.getToolProjectId());
		}
		catch (final PluginServiceException e)
		{
			throw new PluginServiceException("Unable to get the Spip web site url", e);
		}
	}

	@Override
	protected String getPropertyFileName()
	{
		return PROPERTY_FILE;
	}

	/**
	 * Use by container to inject {@link SpipConfigurationService}
	 * 
	 * @param pPluginConfigurationService
	 *          the pluginConfigurationService to set
	 */
	public void setPluginConfigurationService(final SpipConfigurationService pPluginConfigurationService)
	{
		pluginConfigurationService = pPluginConfigurationService;
	}

	/**
	 * Use by container to inject {@link InstanceConfigurationDAO}
	 * 
	 * @param pInstanceConfigurationDAO
	 *          the instanceConfigurationDAO to set
	 */
	public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
	{
		instanceConfigurationDAO = pInstanceConfigurationDAO;
	}
}
