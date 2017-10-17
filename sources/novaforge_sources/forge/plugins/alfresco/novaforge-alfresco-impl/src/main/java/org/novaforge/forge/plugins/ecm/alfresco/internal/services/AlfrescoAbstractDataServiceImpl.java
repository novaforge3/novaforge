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
package org.novaforge.forge.plugins.ecm.alfresco.internal.services;

import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISClient;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISException;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISHelper;
import org.novaforge.forge.plugins.ecm.alfresco.datamapper.AlfrescoResourceBuilder;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoConfigurationService;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoDataService;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoFunctionalException;

/**
 * @author salvat-a
 * @author Guillaume Lamirand
 */
public abstract class AlfrescoAbstractDataServiceImpl implements AlfrescoDataService
{
	/**
	 * Reference to service implementation of {@link AlfrescoCMISClient}
	 */
	protected AlfrescoCMISClient           alfrescoCMISClient;
	/**
	 * Reference to service implementation of {@link InstanceConfigurationDAO}
	 */
	protected InstanceConfigurationDAO     instanceConfigurationDAO;
	/**
	 * Reference to service implementation of {@link AlfrescoResourceBuilder}
	 */
	protected AlfrescoResourceBuilder      alfrescoResourceBuilder;
	/**
	 * Reference to service implementation of {@link AlfrescoConfigurationService}
	 */
	protected AlfrescoConfigurationService alfrescoConfigurationService;

	@Override
	public DataDTO getData(final String forgeId, final String instanceId, final ItemDTO item)
	    throws AlfrescoFunctionalException
	{
		final AlfrescoCMISHelper connector = getConnector(forgeId, instanceId);

		DataDTO data;
		try
		{
			data = this.getData(connector, item);
		}
		catch (final AlfrescoCMISException e)
		{
			throw new AlfrescoFunctionalException(String.format("Unable to get data for %s", item.getReference()),
			    e);
		}
		return data;
	}

	@Override
	public boolean createData(final String forgeId, final String instanceId, final DataDTO data)
	    throws AlfrescoFunctionalException
	{
		if (data != null)
		{
			final AlfrescoCMISHelper connector = getConnector(forgeId, instanceId);
			try
			{
				return this.createData(connector, data);
			}
			catch (final AlfrescoCMISException e)
			{
				throw new AlfrescoFunctionalException(String.format("Unable to create data for project %s",
				    instanceId), e);
			}
		}
		return false;
	}

	@Override
	public boolean updateData(final String forgeId, final String instanceId, final DataDTO data)
	    throws AlfrescoFunctionalException
	{
		if (data != null)
		{
			final AlfrescoCMISHelper connector = getConnector(forgeId, instanceId);
			try
			{
				return this.updateData(connector, data);
			}
			catch (final AlfrescoCMISException e)
			{
				throw new AlfrescoFunctionalException(String.format("Unable to update data for project %s",
				    instanceId), e);
			}
		}
		return false;
	}

	@Override
	public boolean deleteData(final String forgeId, final String instanceId, final ItemDTO item)
	    throws AlfrescoFunctionalException
	{
		if (item != null)
		{
			final AlfrescoCMISHelper connector = getConnector(forgeId, instanceId);
			try
			{
				return this.deleteData(connector, item);
			}
			catch (final AlfrescoCMISException e)
			{
				throw new AlfrescoFunctionalException(String.format("Unable to delete data %s for project %s",
				    item.getReference(), instanceId), e);
			}
		}
		return false;
	}

	protected abstract boolean deleteData(AlfrescoCMISHelper connector, ItemDTO item) throws AlfrescoCMISException;

	protected abstract boolean updateData(AlfrescoCMISHelper connector, DataDTO data)
	    throws AlfrescoCMISException;

	protected abstract boolean createData(AlfrescoCMISHelper connector, DataDTO data) throws AlfrescoCMISException;

	private AlfrescoCMISHelper getConnector(final String forgeId, final String instanceId)
	    throws AlfrescoFunctionalException
	{
		// Get instance object
		final InstanceConfiguration instance = getInstance(instanceId);

		// Check if the instance is mapped to the correct forge id
		checkForgeIf(forgeId, instance);

		try
		{
			return alfrescoCMISClient.getConnector(
			    alfrescoConfigurationService.getCmisURL(instance.getToolInstance().getBaseURL()),
			    alfrescoConfigurationService.getClientAdmin(), alfrescoConfigurationService.getClientPwd());
		}
		catch (final PluginServiceException e)
		{
			throw new AlfrescoFunctionalException(String.format(
			    "Unable to build alfresco connector with [instance=%s]", instance));
		}
	}

	protected abstract DataDTO getData(AlfrescoCMISHelper connector, ItemDTO item) throws AlfrescoCMISException;

	private InstanceConfiguration getInstance(final String instanceId) throws AlfrescoFunctionalException
	{
		return instanceConfigurationDAO.findByInstanceId(instanceId);
	}

	private void checkForgeIf(final String forgeId, final InstanceConfiguration instance)
	    throws AlfrescoFunctionalException
	{
		if (instance != null)
		{
			if (!instance.getForgeId().equals(forgeId))
			{
				throw new AlfrescoFunctionalException(
				    "The forge id given as parameter doesn''t match with the instance id");
			}
		}
	}

	/**
	 * Use by container to inject {@link AlfrescoCMISClient}
	 * 
	 * @param pAlfrescoCMISClient
	 *          the cmisClient to set
	 */
	public void setAlfrescoCMISClient(final AlfrescoCMISClient pAlfrescoCMISClient)
	{
		alfrescoCMISClient = pAlfrescoCMISClient;
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
	 * Use by container to inject {@link AlfrescoResourceBuilder}
	 * 
	 * @param pAlfrescoResourceBuilder
	 *          the alfrescoResourceBuilder to set
	 */
	public void setAlfrescoResourceBuilder(final AlfrescoResourceBuilder pAlfrescoResourceBuilder)
	{
		alfrescoResourceBuilder = pAlfrescoResourceBuilder;
	}

	/**
	 * Use by container to inject {@link AlfrescoConfigurationService}
	 * 
	 * @param pAlfrescoConfigurationService
	 *          the alfrescoConfigurationService to set
	 */
	public void setAlfrescoConfigurationService(final AlfrescoConfigurationService pAlfrescoConfigurationService)
	{
		alfrescoConfigurationService = pAlfrescoConfigurationService;
	}

}
