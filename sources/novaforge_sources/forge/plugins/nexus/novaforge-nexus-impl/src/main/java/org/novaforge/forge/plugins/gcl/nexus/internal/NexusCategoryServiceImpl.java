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

import java.util.Locale;

import org.novaforge.forge.core.plugins.categories.repositorymanagement.RepositoryManagementCategoryService;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;
import org.novaforge.forge.plugins.gcl.nexus.domain.Repository;
import org.novaforge.forge.plugins.gcl.nexus.domain.RepositoryFormat;
import org.novaforge.forge.plugins.gcl.nexus.internal.datamapper.RepositoryMapper;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestClientCustom;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestException;
import org.sonatype.nexus.repository.maven.VersionPolicy;

/**
 * @author rols-p
 */
public class NexusCategoryServiceImpl extends AbstractPluginCategoryService implements
    RepositoryManagementCategoryService
{

	private static final String      PROPERTY_FILE = "nexus";

	/**
	 * Reference to service implementation of {@link InstanceConfigurationDAO}
	 */
	private InstanceConfigurationDAO instanceConfigurationDAO;



	/**
	 * Reference to service implementation of {@link NexusRestClientCustom}
	 */
	private NexusRestClientCustom    nexusRestClientCustom;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getApplicationAccessInfo(final String pInstanceId, final Locale pLocale)
	    throws PluginServiceException
	{
		return getMessage(KEY_ACCESS_INFO, pLocale, getRepoUrl(pInstanceId, true), getRepoUrl(pInstanceId, false));
	}

	/**
	 * Returns the Url of the Snapshot Repository
	 * o	In Nexus 2.7 : https://<hostname>/nexus-default/nexus/content/repositories/<repo>
	 * o	In Nexus 3   : https://<hostname>/nexus-default/nexus/repository/<repo> 
	 * 
	 * @return the repo url
	 * @throws DataAccessException
	 */
	private String getRepoUrl(final String pInstanceId, final boolean pRelease) throws PluginServiceException
	{
		String url = null;
		String repo = null;
		try
		{
			InstanceConfiguration configuration = instanceConfigurationDAO.findByInstanceId(pInstanceId);
			
	    repo = RepositoryMapper.getRepositoryId(configuration.getToolProjectId(), 
	              RepositoryFormat.MAVEN2,
	              (pRelease)?VersionPolicy.RELEASE:VersionPolicy.SNAPSHOT);

			Repository repoExpected = nexusRestClientCustom.getRepository(repo);

			url = repoExpected.getUrl();
		}
		catch (NexusRestException e)
		{
			throw new PluginServiceException(String.format("Unable to get the Nexus Repository resource [repo=%s]",
			    repo), e);
		}
		return url;
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
	 * Use by container to inject {@link InstanceConfigurationDAO}
	 * 
	 * @param pInstanceConfigurationDAO
	 *          the instanceConfigurationDAO to set
	 */
	public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
	{
		instanceConfigurationDAO = pInstanceConfigurationDAO;
	}

	/**
	 * Use by container to inject {@link NexusRestClientCustom}
	 * 
	 * @param pNexusRestClientCustom
	 *          the nexusRestClientCustom to set
	 */
	public void setNexusRestClientCustom(final NexusRestClientCustom pNexusRestClientCustom)
	{
		nexusRestClientCustom = pNexusRestClientCustom;
	}

}
