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

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.novaforge.forge.core.plugins.categories.ecm.DocumentNodeBean;
import org.novaforge.forge.core.plugins.categories.ecm.ECMServiceException;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.categories.beans.DocumentNodeBeanImpl;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISClient;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISException;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISHelper;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoRepository;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestClient;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestHelper;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestException;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoConfigurationService;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoFunctionalException;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoFunctionalService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AlfrescoFunctionalServiceImpl implements AlfrescoFunctionalService
{

	/**
	 * AlfrescoRestClientCustom service injected by container.
	 */
	private AlfrescoRestClient           alfrescoRestClient;
	/**
	 * AlfrescoCMISClient service injected by container.
	 */
	private AlfrescoCMISClient           alfrescoCMISClient;

	/**
	 * InstanceConfigurationDAORemote service injected by container.
	 */
	private InstanceConfigurationDAO     instanceConfigurationDAO;
	/**
	 * AlfrescoConfigurationService service injected by container.
	 */
	private AlfrescoConfigurationService alfrescoConfigurationService;

	@Override
	public AlfrescoRepository getRepository(final String forgeId, final String pluginInstanceId)
	    throws AlfrescoFunctionalException
	{
		final String siteUUID = getSiteUUID(forgeId, pluginInstanceId);

		final AlfrescoCMISHelper cmisConnector = getCMISConnector(forgeId, pluginInstanceId);

		try
		{
			return alfrescoCMISClient.getRepository(cmisConnector, siteUUID, -1);
		}
		catch (final AlfrescoCMISException e)
		{
			throw new AlfrescoFunctionalException(String.format(
			    "Unable to get the repository for forge %s and plugin %s", forgeId, pluginInstanceId), e);
		}
	}

	@Override
	public String getSiteUUID(final String forgeId, final String pluginInstanceId)
	    throws AlfrescoFunctionalException
	{
		// Get instance object
		final InstanceConfiguration instance = getInstance(pluginInstanceId);

		// Check if instance got is mapped to the correct forge id
		checkForgeIf(forgeId, instance);
		try
		{
			// get the rest helper
			final AlfrescoRestHelper restConnector = alfrescoRestClient.getConnector(
			    alfrescoConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
			    alfrescoConfigurationService.getClientAdmin(), alfrescoConfigurationService.getClientPwd());

			// Build root document
			return alfrescoRestClient.getSiteId(restConnector, instance.getToolProjectId());
		}
		catch (final AlfrescoRestException e)
		{
			throw new AlfrescoFunctionalException(String.format(
			    "Unable to get space id with [forge_id=%s, instance_id=%s]", forgeId, pluginInstanceId), e);
		}
		catch (final PluginServiceException e)
		{
			throw new AlfrescoFunctionalException(String.format("Unable to get build connecter with [instance=%s]",
			    pluginInstanceId), e);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws ECMServiceException
	 */
	@Override
	public DocumentNodeBean getRepositoryTree(final String pForgeId, final String pInstanceId, final String pCurrentUser)
			throws ECMServiceException
	{
		// get the tree
		final List<Tree<FileableCmisObject>> descendants = getDescendants(pForgeId, pInstanceId);

		// Build parent node
		final DocumentNodeBean rootNode = getRootNode(descendants);

		// Browse the tree object and build document bean child
		rootNode.setChildren(buildChildren(descendants));

		return rootNode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyRepositoryContent(final String pForgeId, final String pInstanceId,
	    final String pCurrentUser, final String pJSONParameter) throws ECMServiceException
	{
		try
		{
			// get the cmis helper
			final AlfrescoCMISHelper cmisConnector = getCMISConnector(pForgeId, pInstanceId);

			// Build JSON Object
			final JSONObject json = new JSONObject(pJSONParameter);
			// Getting target path
			final String path = json.getString("path");

			// Getting list of document selected
			final JSONArray array = json.getJSONArray("documents");
			final List<String> list = new ArrayList<String>();
			for (int i = 0; i < array.length(); i++)
			{
				list.add(array.getString(i));
			}
			// Browse the list
			for (final String id : list)
			{
				final Document gedDocument = alfrescoCMISClient.getCmisDocument(cmisConnector, id);
				final ContentStream contentStream = gedDocument.getContentStream();
				copyDocument(buildFinalName(path, contentStream.getFileName()), contentStream.getStream());
			}

		}
		catch (final AlfrescoFunctionalException e)
		{
			throw new ECMServiceException(String.format(
			    "Unable to get space id with [forge_id=%s, instance_id=%s]", pForgeId, pInstanceId), e);
		}
		catch (final AlfrescoCMISException e)
		{
			throw new ECMServiceException(String.format(
			    "Unable to copy documents content to folder with [forge_id=%s, instance_id=%s]", pForgeId,
			    pInstanceId), e);
		}
		catch (final JSONException e)
		{
			throw new ECMServiceException(String.format(
			    "Unable to build documents content information with [json_parameter=%s]", pJSONParameter), e);
		}
	}

	/**
	 * @throws ECMServiceException
	 */
	private void copyDocument(final String pTarget, final InputStream pInputStream) throws ECMServiceException
	{
		try
		{
			final OutputStream outpuStream = new FileOutputStream(new File(pTarget));

			final byte buf[] = new byte[1024];
			int len;
			while ((len = pInputStream.read(buf)) > 0)
			{
				outpuStream.write(buf, 0, len);
			}
			outpuStream.close();
			pInputStream.close();
		}
		catch (final FileNotFoundException e)
		{
			throw new ECMServiceException(String.format("The target file is not found with [target=%s]", pTarget),
			    e);
		}
		catch (final IOException e)
		{
			throw new ECMServiceException(
			    String.format("Unable to write the target file with [target=%s]", pTarget), e);
		}
	}

	/**
	 * @param pPath
	 * @param pDocName
	 * @return
	 */
	private String buildFinalName(final String pPath, final String pDocName)
	{
		final StringBuilder build         = new StringBuilder(pPath);
		final String        fileSeparator = "/";
		if (!pPath.endsWith(fileSeparator))
		{
			build.append(fileSeparator);
		}
		build.append(pDocName);

		return build.toString();
	}

	private AlfrescoCMISHelper getCMISConnector(final String forgeId, final String instanceId)
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

	/**
	 * @param pInstanceId
	 * @return
	 * @throws ECMServiceException
	 */
	private InstanceConfiguration getInstance(final String pInstanceId) throws AlfrescoFunctionalException
	{
		return instanceConfigurationDAO.findByInstanceId(pInstanceId);
	}

	/**
	 * @param pForgeId
	 * @param instance
	 * @throws ECMServiceException
	 */
	private void checkForgeIf(final String pForgeId, final InstanceConfiguration instance)
	    throws AlfrescoFunctionalException
	{
		if (instance != null)
		{
			if (!instance.getForgeId().equals(pForgeId))
			{
				throw new AlfrescoFunctionalException(
				    "The forge id given as parameter doesn''t match with the instance id");
			}
		}
	}

	private List<Tree<FileableCmisObject>> getDescendants(final String forgeId, final String pluginInstanceId)
			throws ECMServiceException
	{
		try
		{
			final String siteUUID = getSiteUUID(forgeId, pluginInstanceId);

			final AlfrescoCMISHelper cmisConnector = getCMISConnector(forgeId, pluginInstanceId);
			// get the tree
			return alfrescoCMISClient.getDescendants(cmisConnector, siteUUID, -1);
		}
		catch (final AlfrescoFunctionalException e)
		{
			throw new ECMServiceException(String.format("Unable to get space id with [forge_id=%s, instance_id=%s]", forgeId,
																									pluginInstanceId), e);
		}
		catch (final AlfrescoCMISException e)
		{
			throw new ECMServiceException(String.format("Unable to build repository tree with [forge_id=%s, instance_id=%s]",
																									forgeId, pluginInstanceId), e);
		}
	}

	/**
	 * @param descendants
	 *
	 * @return
	 */
	private DocumentNodeBean getRootNode(final List<Tree<FileableCmisObject>> descendants)
	{
		DocumentNodeBean rootNode = null;
		if ((descendants != null) && (!descendants.isEmpty()))
		{
			final Tree<FileableCmisObject> tree = descendants.get(0);
			final List<Folder> parents = tree.getItem().getParents();
			if ((parents != null) && (!descendants.isEmpty()))
			{
				rootNode = buildFolderNode(parents.get(0));
			}
		}
		return rootNode;
	}

	private List<DocumentNodeBean> buildChildren(final List<Tree<FileableCmisObject>> pTarget) throws ECMServiceException
	{
		final List<DocumentNodeBean> returnList = new ArrayList<DocumentNodeBean>();
		if (pTarget != null)
		{
			for (final Tree<FileableCmisObject> tree : pTarget)
			{
				DocumentNodeBean currentNode = null;
				if (BaseTypeId.CMIS_FOLDER.equals(tree.getItem().getBaseTypeId()))
				{
					currentNode = buildFolderNode((Folder) tree.getItem());
					currentNode.setChildren(buildChildren(tree.getChildren()));
				}
				else if (BaseTypeId.CMIS_DOCUMENT.equals(tree.getItem().getBaseTypeId()))
				{
					currentNode = this.buildDocumentNode((Document) tree.getItem());
				}
				returnList.add(currentNode);
			}
		}

		return returnList;
	}

	/**
	 * Build DocumentNodeBean from a folder object
	 *
	 * @param pFolder
	 */
	private DocumentNodeBean buildFolderNode(final Folder pFolder)
	{
		final DocumentNodeBean folderNode = this.buildDocumentNode(pFolder);
		folderNode.setDocument(false);
		folderNode.setId(pFolder.getId());
		folderNode.setPath(pFolder.getPath());
		folderNode.setParentId(pFolder.getParentId());
		return folderNode;
	}

	/**
	 * Build DocumentNodeBean from a folder object
	 *
	 * @param pDocument
	 */
	private DocumentNodeBean buildDocumentNode(final Document pDocument)
	{
		final DocumentNodeBean docNode = this.buildDocumentNode((CmisObject) pDocument);
		docNode.setDocument(true);
		return docNode;
	}

	private DocumentNodeBean buildDocumentNode(final CmisObject pCmisObject)
	{
		final DocumentNodeBean node = new DocumentNodeBeanImpl();
		node.setId(pCmisObject.getId());
		node.setName(pCmisObject.getName());
		node.setAuthor(pCmisObject.getCreatedBy());
		node.setCreatedDate(pCmisObject.getCreationDate().getTime());
		node.setLastModified(pCmisObject.getLastModifiedBy());
		node.setLastModifiedDate(pCmisObject.getLastModificationDate().getTime());
		return node;
	}

	/**
	 * Use by container to inject {@link AlfrescoRestClient}
	 * 
	 * @param pAlfrescoRestClient
	 *          the alfrescoRestClient to set
	 */
	public void setAlfrescoRestClient(final AlfrescoRestClient pAlfrescoRestClient)
	{
		alfrescoRestClient = pAlfrescoRestClient;
	}

	/**
	 * Use by container to inject {@link AlfrescoCMISClient}
	 * 
	 * @param pAlfrescoCMISClient
	 *          the alfrescoCMISClient to set
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
