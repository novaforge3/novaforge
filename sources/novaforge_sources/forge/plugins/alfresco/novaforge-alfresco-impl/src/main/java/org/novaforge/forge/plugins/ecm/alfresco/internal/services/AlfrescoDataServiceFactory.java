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

import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoDataService;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoDataType;

/**
 * @author salvat-a
 */
public class AlfrescoDataServiceFactory
{
	/**
	 * Reference to service implementation of {@link AlfrescoDataService}
	 */
	private AlfrescoDataService alfrescoDocumentDataService;
	/**
	 * Reference to service implementation of {@link AlfrescoDataService}
	 */
	private AlfrescoDataService alfrescoFolderDataService;

	public AlfrescoDataService getService(final String dataType)
	{
		if (StringUtils.equalsIgnoreCase(dataType, AlfrescoDataType.FOLDER.value()))
		{
			return alfrescoFolderDataService;
		}
		else
		{
			return alfrescoDocumentDataService;
		}
	}

	public AlfrescoDataService getDocumentService()
	{
		return alfrescoDocumentDataService;
	}

	public AlfrescoDataService getFolderService()
	{
		return alfrescoFolderDataService;
	}

	/**
	 * Use by container to inject {@link AlfrescoDataService}
	 * 
	 * @param pAlfrescoDocumentDataService
	 *          the alfrescoDocumentDataService to set
	 */
	public void setAlfrescoDocumentDataService(final AlfrescoDataService pAlfrescoDocumentDataService)
	{
		alfrescoDocumentDataService = pAlfrescoDocumentDataService;
	}

	/**
	 * Use by container to inject {@link AlfrescoDataService}
	 * 
	 * @param pAlfrescoFolderDataService
	 *          the alfrescoFolderDataService to set
	 */
	public void setAlfrescoFolderDataService(final AlfrescoDataService pAlfrescoFolderDataService)
	{
		alfrescoFolderDataService = pAlfrescoFolderDataService;
	}
}