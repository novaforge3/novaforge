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
package org.novaforge.forge.ui.forge.reference.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import org.apache.commons.fileupload.FileItem;
import org.novaforge.forge.commons.technical.file.FileServiceException;
import org.novaforge.forge.ui.forge.reference.shared.tools.ToolProperties;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

public class CustomizedToolUploadServlet extends UploadAction
{

	/**
    * 
    */
	private static final long serialVersionUID = -1589858498333893933L;

	/**
	 * Override this method if you want to check the request before it is passed
	 * to commons-fileupload parser.
	 * 
	 * @param pRequest
	 * @throws RuntimeException
	 */
	@Override
	public void checkRequest(final HttpServletRequest pRequest)
	{
		// Overide maxsize value
		maxSize = OSGiHelper.getForgeConfigurationService().getUploadMaxSize();
		super.checkRequest(pRequest);
	}

	@Override
	public String executeAction(final HttpServletRequest pRequest, final List<FileItem> pSessionFiles)
	    throws UploadActionException
	{
		final String filename = pRequest.getParameter(ToolProperties.NAME_PARAMETER);
		final String version = pRequest.getParameter(ToolProperties.VERSION_PARAMETER);
		final String isPublic = pRequest.getParameter(ToolProperties.ISPUBLIC_PARAMETER);
		final String suffix = pRequest.getParameter(ToolProperties.SUFFIX_PARAMETER);
		if (!filename.isEmpty() && !version.isEmpty() && !isPublic.isEmpty())
		{
			for (final FileItem fileItem : pSessionFiles)
			{

				final File uploadedFile = new File(OSGiHelper.getReferenceToolService().getFileName(filename,
				    version, suffix, Boolean.valueOf(isPublic)));
				try
				{
					OSGiHelper.getFileService().storeFile(fileItem, uploadedFile);
				}
				catch (final FileServiceException e)
				{
					throw new UploadActionException(e);
				}
			}
		}
		else
		{
			throw new UploadActionException("The parameters given are wrong.");
		}
		return null;
	}

}