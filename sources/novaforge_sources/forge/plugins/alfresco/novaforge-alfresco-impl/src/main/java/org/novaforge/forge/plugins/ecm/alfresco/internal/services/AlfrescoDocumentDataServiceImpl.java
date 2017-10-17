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
import org.novaforge.forge.core.plugins.data.AlfrescoDocumentDTO;
import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISException;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISHelper;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocument;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocumentContent;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoDataType;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AlfrescoDocumentDataServiceImpl extends AlfrescoAbstractDataServiceImpl
{
	private static final Log log = LogFactory.getLog(AlfrescoDocumentDataServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataDTO getData(final AlfrescoCMISHelper connector, final ItemDTO item)
	    throws AlfrescoCMISException
	{
		final String dataId = item.getReference().getReferenceId();

		final AlfrescoDocumentContent content = convertDocument(alfrescoCMISClient.getDocument(connector, dataId));
		content.setLocalFile(alfrescoConfigurationService.getDocumentLocalRootPath()
		    + content.getContentStreamFileName());

		alfrescoCMISClient.copyDocumentContent(connector, dataId, content.getLocalFile());

		final AlfrescoDocumentDTO documentDTO = new AlfrescoDocumentDTO();
		documentDTO.setName(content.getName());
		documentDTO.setPath(content.getPath());
		documentDTO.setParentPath(content.getParentPath());
		documentDTO.setVersionLabel(content.getVersionLabel());
		documentDTO.setContentSreamFileName(content.getContentStreamFileName());
		documentDTO.setContentStreamLength(content.getContentStreamLength());
		documentDTO.setContentStreamMimeType(content.getContentStreamMimeType());

		final FileDataSource fileDataSource = new FileDataSource(new File(content.getLocalFile()));
		final DataHandler dataHandler = new DataHandler(fileDataSource);
		documentDTO.setContent(dataHandler);

		return new DataDTO(item, documentDTO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean createData(final AlfrescoCMISHelper connector, final DataDTO data)
	    throws AlfrescoCMISException
	{
		final AlfrescoDocumentContent content = convertData(data);

		final boolean created = alfrescoCMISClient.createDocument(connector, content);

		final boolean deleted = deleteLocalFile(content.getLocalFile());
		if (!deleted)
		{
			log.warn(String.format("The local file %s could not be deleted.", content.getLocalFile()));
		}
		return created;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateData(final AlfrescoCMISHelper connector, final DataDTO data)
	    throws AlfrescoCMISException
	{
		final AlfrescoDocumentContent content = convertData(data);
		final boolean updated = alfrescoCMISClient.updateDocument(connector, convertData(data),
		    alfrescoConfigurationService.getCheckinComment());

		final boolean deleted = deleteLocalFile(content.getLocalFile());
		if (!deleted)
		{
			log.warn(String.format("The local file %s could not be deleted.", content.getLocalFile()));
		}

		return updated;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean deleteData(final AlfrescoCMISHelper connector, final ItemDTO item)
	    throws AlfrescoCMISException
	{
		return alfrescoCMISClient.deleteDocument(connector, item.getReference().getReferenceId());
	}

	private AlfrescoDocumentContent convertData(final DataDTO data) throws AlfrescoCMISException
	{
		final AlfrescoDocumentDTO documentDTO = data.getAlfrescoDocumentDTO();

		final AlfrescoDocumentContent documentContent = alfrescoResourceBuilder.newDocumentContent();
		documentContent.setName(documentDTO.getName());
		documentContent.setPath(documentDTO.getPath());
		documentContent.setParentPath(documentDTO.getParentPath());
		documentContent.setType(AlfrescoDataType.DOCUMENT.value());
		documentContent.setLocalFile(alfrescoConfigurationService.getDocumentLocalRootPath()
		    + documentDTO.getContentSreamFileName());
		documentContent.setContentStreamFileName(documentDTO.getContentSreamFileName());
		documentContent.setContentStreamLength(documentDTO.getContentStreamLength());
		documentContent.setContentStreamMimeType(documentDTO.getContentStreamMimeType());

		try
		{
			final DataHandler dataHandler = documentDTO.getContent();
			final OutputStream outputStream = new FileOutputStream(documentContent.getLocalFile());
			dataHandler.writeTo(outputStream);
			outputStream.close();
		}
		catch (final FileNotFoundException e)
		{
			throw new AlfrescoCMISException(String.format("Could not create the data, file %s not found",
			    documentContent.getLocalFile()));
		}
		catch (final IOException e)
		{
			throw new AlfrescoCMISException("Could not create the data, unexpected IO error", e);
		}
		return documentContent;
	}

	private boolean deleteLocalFile(final String localFile)
	{
		final File file = new File(localFile);
		return file.exists() && file.delete();
	}

	private AlfrescoDocumentContent convertDocument(final AlfrescoDocument input)
	{
		final AlfrescoDocumentContent result = alfrescoResourceBuilder.newDocumentContent();
		result.setName(input.getName());
		result.setPath(input.getPath());
		result.setParentPath(input.getParentPath());
		result.setType(input.getType());
		result.setVersionLabel(input.getVersionLabel());
		result.setContentStreamFileName(input.getContentStreamFileName());
		result.setContentStreamLength(input.getContentStreamLength());
		result.setContentStreamMimeType(input.getContentStreamMimeType());
		return result;
	}
}
