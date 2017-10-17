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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

public class DownloadServlet extends HttpServlet
{

	private static final long   serialVersionUID = -5641739972260552574L;

	// Return messages to client

	private static final Log    LOG              = LogFactory.getLog(DownloadServlet.class);

	private static final String IMPORT_DIRECTORY = "/tmp";

	public static String getDownloadFullPathName(final String pFileName)
	{
		final StringBuilder sbPath = new StringBuilder(IMPORT_DIRECTORY);

		// create the storage filesystem directory if not exists
		final File projectDirectory = new File(sbPath.toString());
		projectDirectory.mkdirs();

		// add the filename
		sbPath.append(File.separator).append(pFileName);
		return sbPath.toString();
	}

	@Override
	protected void doGet(final HttpServletRequest pReq, final HttpServletResponse pResp)
			throws ServletException, IOException
	{
		doPost(pReq, pResp);
	}

	@Override
	protected void doPost(final HttpServletRequest pRequest, final HttpServletResponse pResponse)
	    throws ServletException, IOException
	{

		String pProjectId = pRequest.getParameter("project");
		String pReference = pRequest.getParameter("reference");
		String pTemplateSample = pRequest.getParameter("templatesample");
		if ((pTemplateSample != null) && (pTemplateSample.equals("true")))
		{
			getTemplateSampleFile(pResponse);
		}
		else
		{
			getDeliveryArchive(pProjectId, pReference, pResponse);
		}
	}

	private void getTemplateSampleFile(final HttpServletResponse pResponse) throws ServletException
	{
		String filePath = "";
		String fileName = "";
		try
		{
			fileName = "delivery-reporting-template.zip";
			filePath = OSGiServiceGetter.getDeliveryRepositoryService().getDeliveryTemplateSamplePath() + fileName;
		}
		catch (DeliveryServiceException e1)
		{

			throw new ServletException(e1.getMessage());
		}

		if ((filePath == null) || (filePath.trim().length() == 0))
		{
			throw new ServletException("NO_FILENAME_PARAMETER");
		}
		File source = new File(filePath);

		pResponse.setContentType("application/download; charset=UTF-8");
		pResponse.setHeader("Content-disposition", "attachment; filename=" + fileName);
		try
		{
			OutputStream out = pResponse.getOutputStream();
			FileInputStream fis;

			fis = new FileInputStream(source);

			byte[] buffer = new byte[1024];
			int n;
			while (-1 != (n = fis.read(buffer)))
			{
				out.write(buffer, 0, n);
			}
			out.flush();
			out.close();
			fis.close();
		}
		catch (IOException e)
		{
			LOG.error(MessageFormat.format("unable to copy file {0} on response header", filePath));
			throw new ServletException();
		}
	}

	private void getDeliveryArchive(final String pProjectId, final String pRefrenceId,
																	final HttpServletResponse pResponse) throws ServletException
	{
		String fileName = "";
		String filePath = "";
		try
		{
			filePath = OSGiServiceGetter.getDeliveryRepositoryService().getDeliveryArchivePath(pProjectId, pRefrenceId);
			fileName = FilenameUtils.getName(filePath);
		}
		catch (DeliveryServiceException e1)
		{
			throw new ServletException(e1.getMessage());
		}
		if ((filePath == null) || (filePath.trim().length() == 0))
		{
			throw new ServletException("NO_FILENAME_PARAMETER");
		}
		File source = new File(filePath);

		pResponse.setContentType("application/download; charset=UTF-8");
		pResponse.setHeader("Content-disposition", "attachment; filename=" + fileName);
		try
		{
			OutputStream out = pResponse.getOutputStream();
			FileInputStream fis;

			fis = new FileInputStream(source);

			byte[] buffer = new byte[1024];
			int n;
			while (-1 != (n = fis.read(buffer)))
			{
				out.write(buffer, 0, n);
			}
			out.flush();
			out.close();
			fis.close();
		}
		catch (IOException e)
		{
			LOG.error(MessageFormat.format("unable to copy file {0} on response header", filePath));
			throw new ServletException();
		}
	}

}
