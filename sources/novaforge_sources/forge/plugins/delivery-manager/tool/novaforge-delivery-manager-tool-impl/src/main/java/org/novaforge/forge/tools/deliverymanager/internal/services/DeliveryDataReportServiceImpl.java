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
package org.novaforge.forge.tools.deliverymanager.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.internal.model.ECMDocumentImpl;
import org.novaforge.forge.tools.deliverymanager.internal.model.FileElementImpl;
import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.ArtefactParameter;
import org.novaforge.forge.tools.deliverymanager.model.BugTrackerIssue;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;
import org.novaforge.forge.tools.deliverymanager.model.ECMDocument;
import org.novaforge.forge.tools.deliverymanager.model.FileElement;
import org.novaforge.forge.tools.deliverymanager.model.Folder;
import org.novaforge.forge.tools.deliverymanager.model.Node;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryDataReportService;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPluginService;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an implementation used to build report object.
 * 
 * @author Guillaume Lamirand
 */
public class DeliveryDataReportServiceImpl implements DeliveryDataReportService
{

	private static final Log        LOGGER     = LogFactory.getLog(DeliveryDataReportServiceImpl.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DeliveryPresenter     deliveryPresenter;
	private DeliveryPluginService deliveryPluginService;

	/**
	 * @inheritDoc
	 */
	@Override
	public List<BugTrackerIssue> getBugTrackerIssues(final String pProjectId, final String pReference,
	    final String pUser)
	{
		final List<BugTrackerIssue> returnIssues = new ArrayList<BugTrackerIssue>();
		try
		{
			final List<Content> contents = deliveryPresenter.getContents(pProjectId, pReference);
			if (contents != null)
			{
				for (final Content content : contents)
				{
					if (ContentType.BUG.equals(content.getType()))
					{
						final String version = content.getNode().getName();
						final List<BugTrackerIssue> issues = deliveryPluginService.getIssues(pProjectId, version, pUser);
						if (issues != null)
						{
							returnIssues.addAll(issues);
						}
						break;
					}
				}
			}
		}
		catch (final DeliveryServiceException e)
		{
			LOGGER.warn("An error occured when building the issues selected on the delivery. No Issues returned!", e);
		}
		return returnIssues;

	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<ECMDocument> getECMDocuments(final String pProjectId, final String pReference)
	{

		final List<ECMDocument> returnDocuments = new ArrayList<ECMDocument>();
		try
		{
			final List<Content> contents = deliveryPresenter.getContents(pProjectId, pReference);
			if (contents != null)
			{
				for (final Content content : contents)
				{
					if (ContentType.ECM.equals(content.getType()))
					{
						final Node rootNode = content.getNode();
						if ((rootNode != null) && (rootNode instanceof Folder))
						{
							returnDocuments.addAll(buildDocuments(((Folder) rootNode).getChildNodes()));
						}
						break;
					}
				}
			}
		}
		catch (final DeliveryServiceException e)
		{
			LOGGER.warn("An error occured when building a list with selected documents on the delivery. No Issues returned!",
			        e);
		}
		return returnDocuments;

	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<FileElement> getFileElements(final String pProjectId, final String pReference)
	{

		final List<FileElement> returnFiles = new ArrayList<FileElement>();
		try
		{
			final List<Content> contents = deliveryPresenter.getContents(pProjectId, pReference);
			if (contents != null)
			{
				for (final Content content : contents)
				{
					if (ContentType.FILE.equals(content.getType()))
					{
						final Node rootNode = content.getNode();
						if ((rootNode != null) && (rootNode instanceof Folder))
						{
							returnFiles.addAll(buildFiles(((Folder) rootNode).getChildNodes()));
						}
						break;
					}
				}
			}
		}
		catch (final DeliveryServiceException e)
		{
			LOGGER.warn("An error occured when building a list with selected documents on the delivery. No Issues returned!",
			        e);
		}
		return returnFiles;

	}

	/**
	 * @param pChildNodes
	 * @return
	 */
	private List<FileElement> buildFiles(final List<Node> pChildNodes)
	{
		final List<FileElement> pArrayList = new ArrayList<FileElement>();
		for (final Node node : pChildNodes)
		{
			if (node instanceof Artefact)
			{
				pArrayList.add(buildFileElement((Artefact) node));
			}
		}
		return pArrayList;
	}

	private FileElement buildFileElement(final Artefact pArtefact)
	{
		final FileElementImpl file = new FileElementImpl();
		file.setFileName(pArtefact.getName());
		file.setSize(getValue(pArtefact.getParameters(), "size"));
		file.setSource(getValue(pArtefact.getParameters(), "source"));
		file.setType(getValue(pArtefact.getParameters(), "type"));
		file.setUrl(getValue(pArtefact.getParameters(), "url"));
		return file;

	}

	private String getValue(final List<ArtefactParameter> parameters, final String pKey)
	{
		String returnValue = "";
		if (parameters != null)
		{
			for (final ArtefactParameter artefactParameter : parameters)
			{
				if (artefactParameter.getKey().equals(pKey))
				{
					returnValue = artefactParameter.getValue();
					break;
				}
			}
		}
		return returnValue;
	}

	/**
	 * @param pChildNodes
	 * @param pArrayList
	 *
	 * @return
	 */
	private List<ECMDocument> buildDocuments(final List<Node> pChildNodes)
	{
		final List<ECMDocument> pArrayList = new ArrayList<ECMDocument>();
		for (final Node node : pChildNodes)
		{
			if (node instanceof Folder)
			{
				pArrayList.addAll(buildDocuments(((Folder) node).getChildNodes()));
			}
			else if (node instanceof Artefact)
			{
				pArrayList.add(buildECMDocument((Artefact) node));
			}
		}
		return pArrayList;
	}

	private ECMDocument buildECMDocument(final Artefact pArtefact)
	{
		final ECMDocumentImpl doc = new ECMDocumentImpl();
		try
		{
			doc.setId(pArtefact.getIdentifiant());
			doc.setName(pArtefact.getName());
			doc.setPath(pArtefact.getPath());
			doc.setAuthor(getValue(pArtefact.getParameters(), "author"));
			doc.setCreatedDate(dateFormat.parse(getValue(pArtefact.getParameters(), "createdDate")));
			doc.setLastModifiedDate(dateFormat.parse(getValue(pArtefact.getParameters(), "lastModifiedDate")));
			doc.setLastModifiedAuthor(getValue(pArtefact.getParameters(), "lastModifiedAuthor"));
		}
		catch (final ParseException e)
		{
			LOGGER.warn("An error occured when parsing dates from ECMDocument's parameters", e);
		}
		return doc;

	}

	/**
	 * @param pDeliveryPresenter
	 *          the deliveryPresenter to set
	 */
	public void setDeliveryPresenter(final DeliveryPresenter pDeliveryPresenter)
	{
		deliveryPresenter = pDeliveryPresenter;
	}

	/**
	 * @param pDeliveryPluginService
	 *          the deliveryPluginService to set
	 */
	public void setDeliveryPluginService(final DeliveryPluginService pDeliveryPluginService)
	{
		deliveryPluginService = pDeliveryPluginService;
	}
}
