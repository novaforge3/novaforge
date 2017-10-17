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
package org.novaforge.forge.tools.deliverymanager.ui.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.novaforge.forge.commons.technical.file.FileServiceException;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.ArtefactParameter;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;
import org.novaforge.forge.tools.deliverymanager.model.Folder;
import org.novaforge.forge.tools.deliverymanager.model.Node;
import org.novaforge.forge.tools.deliverymanager.model.SourceFile;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryProperties;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ParameterKey;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class CustomizedUploadServlet extends UploadAction
{

	private static final String FILE_SEPARATOR   = "/";

	/**
    * 
    */
	private static final long   serialVersionUID = -1589858498333893933L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String executeAction(final HttpServletRequest pRequest, final List<FileItem> pSessionFiles)
	    throws UploadActionException
	{
		final String projectId = pRequest.getParameter(DeliveryProperties.PROJECT_PARAMETER);
		final String reference = pRequest.getParameter(DeliveryProperties.REFERENCE_PARAMETER);

		if ((projectId != null) && (reference != null))
		{
			manageDeliveryContent(pSessionFiles, projectId, reference);
		}
		else if ((projectId != null) && (reference == null))
		{
			manageTemplateReport(pSessionFiles, projectId);
		}
		else
		{
			throw new UploadActionException("The parameters given are wrong.");
		}
		return null;
	}

	private void manageTemplateReport(final List<FileItem> pSessionFiles, final String projectId)
	    throws UploadActionException
	{
		for (final FileItem fileItem : pSessionFiles)
		{
			final String templateDirectory = OSGiServiceGetter.getDeliveryRepositoryService().getTemplateDirectory(
			    projectId);
			writeFileItem(projectId, fileItem, templateDirectory);

		}
	}

	private void writeFileItem(final String projectId, final FileItem fileItem, final String pDirectory)
	    throws UploadActionException
	{
		final String fileName = getFileName(fileItem);
		final File uploadedFile = new File(pDirectory, fileName);
		try
		{
			if (OSGiServiceGetter.getCommonFileService().existFile(uploadedFile))
			{
				OSGiServiceGetter.getCommonFileService().deleteFile(uploadedFile);
			}
			OSGiServiceGetter.getCommonFileService().storeFile(fileItem, uploadedFile);
		}
		catch (final FileServiceException e)
		{
			throw new UploadActionException(String.format(
			    "Unable to store uploaded file with [fileitem=%s, destination=%s]", fileItem,
			    uploadedFile.getAbsolutePath()), e);
		}
	}

	private void manageDeliveryContent(final List<FileItem> pSessionFiles, final String projectId,
	    final String reference) throws UploadActionException
	{
		final DeliveryPresenter deliveryManager = OSGiServiceGetter.getDeliveryManager();
		final Content content = getContent(projectId, reference);
		final Node node = content.getNode();
		for (final FileItem fileItem : pSessionFiles)
		{
			final Artefact newArtefact = buildArtefact(node, fileItem);
			final Folder folder = (Folder) node;
			folder.addChildNode(newArtefact);
			writeFileItem(projectId, fileItem, getTemporaryPath(projectId, reference, folder.getName()));
		}
		try
		{
			deliveryManager.updateContentNode(projectId, reference, null, content);
		}
		catch (final DeliveryServiceException e)
		{
			throw new UploadActionException(String.format(
			    "Unable to update binary content with [project_id=%s, reference=%s]", projectId, reference), e);

		}
	}

	private String getFileName(final FileItem fileItem)
	{
		String fileName = fileItem.getName();
		if (fileName != null)
		{
			fileName = FilenameUtils.getName(fileName);
		}
		return fileName;

	}

	/**
	 * @param pProjectId
	 * @param pReference
	 * @param pContentFolder
	 * @return
	 * @throws UploadActionException
	 */
	private String getTemporaryPath(final String pProjectId, final String pReference,
	    final String pContentFolder) throws UploadActionException
	{
		final StringBuilder tmp = new StringBuilder();
		try
		{
			tmp.append(OSGiServiceGetter.getDeliveryRepositoryService().getDeliveryTemporaryPath(pProjectId,
			    pReference));
			if (!tmp.toString().endsWith(FILE_SEPARATOR))
			{
				tmp.append(FILE_SEPARATOR);
			}
			tmp.append(pContentFolder);
		}
		catch (final DeliveryServiceException e1)
		{
			throw new UploadActionException(String.format(
			    "Unable to get temporary folder with [project_id=%s, delivery_reference=%s]", pProjectId,
			    pReference), e1);
		}
		return tmp.toString();
	}

	/**
	 * @param deliveryManager
	 * @param node
	 * @param fileItem
	 * @return
	 */
	private Artefact buildArtefact(final Node node, final FileItem fileItem)
	{
		// Getting delivery manager
		final DeliveryPresenter deliveryManager = OSGiServiceGetter.getDeliveryManager();
		// Builder artefact object
		final Artefact newArtefact = deliveryManager.newArtefact();
		newArtefact.setIdentifiant(fileItem.getName());
		newArtefact.setName(fileItem.getName());

		final List<ArtefactParameter> fields = new ArrayList<ArtefactParameter>();

		final ArtefactParameter type = deliveryManager.newArtefactParameter();
		type.setKey(ParameterKey.TYPE.getKey());
		type.setValue(fileItem.getContentType());
		final ArtefactParameter size = deliveryManager.newArtefactParameter();
		size.setKey(ParameterKey.SIZE.getKey());
		size.setValue(String.valueOf(fileItem.getSize()));
		final ArtefactParameter source = deliveryManager.newArtefactParameter();
		source.setKey(ParameterKey.SOURCE.getKey());
		source.setValue(String.valueOf(SourceFile.LOCALE.getId()));

		fields.add(type);
		fields.add(size);
		fields.add(source);
		newArtefact.setParameters(fields);
		// Building artefact path
		final StringBuilder path = new StringBuilder(node.getPath());
		if (!path.toString().endsWith(FILE_SEPARATOR))
		{
			path.append(FILE_SEPARATOR);
		}
		path.append(node.getName());
		newArtefact.setPath(path.toString());

		// Return result
		return newArtefact;
	}

	/**
	 * @param projectId
	 * @param reference
	 * @param deliveryManager
	 * @return
	 */
	private Content getContent(final String projectId, final String reference) throws UploadActionException
	{
		Content content;
		try
		{
			final DeliveryPresenter deliveryManager = OSGiServiceGetter.getDeliveryManager();
			content = deliveryManager.getContent(projectId, reference, ContentType.FILE);
		}
		catch (final DeliveryServiceException e)
		{
			throw new UploadActionException(String.format(
			    "Unable to get the binary content with [project_id=%s, reference=%s]", projectId, reference), e);
		}
		return content;
	}

}