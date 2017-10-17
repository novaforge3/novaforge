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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.data.ItemReferenceDTO;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginDataService;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocument;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoFolder;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoRepository;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoFunctionalException;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoFunctionalService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author salvat-a
 */
public class AlfrescoPluginDataServiceImpl implements PluginDataService
{
	private final static Log           log       = LogFactory.getLog(AlfrescoPluginDataServiceImpl.class);

	private static final String        SEPARATOR = "--";

	private AlfrescoFunctionalService  alfrescoFunctionalService;
	private AlfrescoDataServiceFactory alfrescoDataServiceFactory;

	@Override
	public List<ItemReferenceDTO> getDataReferences(final String forgeId, final String applicationInstanceId)
	    throws PluginServiceException
	{
		final List<ItemReferenceDTO> references = new ArrayList<ItemReferenceDTO>();
		try
		{
			final AlfrescoRepository repository = alfrescoFunctionalService.getRepository(forgeId,
			    applicationInstanceId);

			for (final AlfrescoFolder folder : repository.getFolders())
			{
				final String modificationComparator = buildFolderModificationComparator(folder);
				references.add(new ItemReferenceDTO(folder.getPath(), modificationComparator, folder.getType()));
			}

			for (final AlfrescoDocument document : repository.getDocuments())
			{
				final String modificationComparator = buildDocumentModificationComparator(document);
				references.add(new ItemReferenceDTO(document.getPath(), modificationComparator, document.getType()));
			}
		}
		catch (final AlfrescoFunctionalException e)
		{
			final String msg = String.format("Unable to get alfresco data references for instance: %s",
			    applicationInstanceId);
			log.error(msg);
			throw new PluginServiceException(msg, e);
		}
		return references;
	}

	@Override
	public DataDTO getData(final String forgeId, final String applicationInstanceId, final ItemDTO itemDTO)
	    throws PluginServiceException
	{

		if ((itemDTO == null) || (itemDTO.getReference() == null))
		{
			throw new PluginServiceException(String.format("The item object is null for the application UUID=%s.",
			    applicationInstanceId));
		}

		final String type = itemDTO.getReference().getItemType();

		DataDTO dataDTO;
		try
		{
			dataDTO = alfrescoDataServiceFactory.getService(type).getData(forgeId, applicationInstanceId, itemDTO);
		}
		catch (final AlfrescoFunctionalException e)
		{
			final String msg = String.format("Unable to get alfresco data for application %s",
			    applicationInstanceId);
			log.error(msg);
			throw new PluginServiceException(msg, e);
		}
		if (log.isDebugEnabled())
		{
			log.debug(String.format("Alfresco data retrieved: %s", dataDTO));
		}
		return dataDTO;
	}

	@Override
	public void putData(final String forgeId, final String applicationInstanceId, final DataDTO dataDTO)
	    throws PluginServiceException
	{
		if (dataDTO == null)
		{
			throw new PluginServiceException(String.format(
			    "Cannot put data, the data object is null for the application UUID=%s.", applicationInstanceId));
		}

		final ItemDTO item = dataDTO.getItemDTO();
		if (item == null)
		{
			throw new PluginServiceException(String.format(
			    "Cannot put data, the item object is null for the application UUID=%s.", applicationInstanceId));
		}

		final ItemReferenceDTO itemReference = item.getReference();
		if (itemReference == null)
		{
			throw new PluginServiceException(String.format(
			    "Cannot put data, the item reference object is null for the application UUID=%s.",
			    applicationInstanceId));
		}

		final String type = item.getReference().getItemType();

		try
		{
			switch (item.getAction())
			{
				case CREATE:
					alfrescoDataServiceFactory.getService(type).createData(forgeId, applicationInstanceId, dataDTO);
					break;
				case UPDATE:
					alfrescoDataServiceFactory.getService(type).updateData(forgeId, applicationInstanceId, dataDTO);
					break;
				case DELETE:
					alfrescoDataServiceFactory.getService(type).deleteData(forgeId, applicationInstanceId, item);
					break;
				default:
					break;
			}
		}
		catch (final AlfrescoFunctionalException e)
		{
			final String msg = String.format("Unable to put alfresco data for application %s",
			    applicationInstanceId);
			log.error(msg);
			throw new PluginServiceException(msg, e);
		}
	}

	private String buildFolderModificationComparator(final AlfrescoFolder folder)
	{
		return folder.getName() + SEPARATOR + folder.getPath();
	}

	private String buildDocumentModificationComparator(final AlfrescoDocument document)
	{
		return document.getName() + SEPARATOR + document.getPath() + SEPARATOR + document.getContentStreamFileName()
							 + SEPARATOR + document.getContentStreamMimeType() + SEPARATOR + document.getContentStreamLength()
							 + SEPARATOR;
	}

	/**
	 * Use by container to inject {@link AlfrescoFunctionalService}
	 * 
	 * @param pAlfrescoFunctionalService
	 *          the alfrescoFunctionalService to set
	 */
	public void setAlfrescoFunctionalService(final AlfrescoFunctionalService pAlfrescoFunctionalService)
	{
		alfrescoFunctionalService = pAlfrescoFunctionalService;
	}

	/**
	 * Use by container to inject {@link AlfrescoDataServiceFactory}
	 * 
	 * @param pAlfrescoDataServiceFactory
	 *          the alfrescoDataServiceFactory to set
	 */
	public void setAlfrescoDataServiceFactory(final AlfrescoDataServiceFactory pAlfrescoDataServiceFactory)
	{
		alfrescoDataServiceFactory = pAlfrescoDataServiceFactory;
	}
}
