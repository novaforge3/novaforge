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
package org.novaforge.forge.plugins.ecm.alfresco.internal;

import org.novaforge.forge.core.plugins.categories.ecm.DocumentNodeBean;
import org.novaforge.forge.core.plugins.categories.ecm.ECMCategoryService;
import org.novaforge.forge.core.plugins.categories.ecm.ECMServiceException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoConfigurationService;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoFunctionalService;

import java.util.Locale;

/**
 * @author rols-p
 */
public class AlfrescoCategoryServiceImpl extends AbstractPluginCategoryService implements ECMCategoryService
{

	private static final String       PROPERTY_FILE = "alfresco";

	/**
	 * Reference to service implementation of {@link AlfrescoFunctionalService}
	 */
	private AlfrescoFunctionalService alfrescoFunctionalService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getApplicationAccessInfo(final String pInstanceId, final Locale pLocale)
	    throws PluginServiceException
	{
		return getMessage(KEY_ACCESS_INFO, pLocale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getPropertyFileName()
	{
		return PROPERTY_FILE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DocumentNodeBean getRepositoryTree(final String pForgeId, final String pInstanceId,
	    final String pCurrentUser) throws ECMServiceException
	{
		return alfrescoFunctionalService.getRepositoryTree(pForgeId, pInstanceId, pCurrentUser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyRepositoryContent(final String pForgeId, final String pInstanceId,
	    final String pCurrentUser, final String pJSONParameter) throws ECMServiceException
	{
		alfrescoFunctionalService.copyRepositoryContent(pForgeId, pInstanceId, pCurrentUser, pJSONParameter);

	}

	/**
	 * Use by container to inject {@link AlfrescoConfigurationService}
	 * 
	 * @param pFunctionalService
	 *          the functionalService to set
	 */
	public void setAlfrescoFunctionalService(final AlfrescoFunctionalService pFunctionalService)
	{
		alfrescoFunctionalService = pFunctionalService;
	}

}
