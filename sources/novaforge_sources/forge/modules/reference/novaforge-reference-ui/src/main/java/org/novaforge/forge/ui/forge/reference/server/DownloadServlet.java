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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.ui.forge.reference.shared.tools.ToolProperties;

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

	public static final String EXPORT_SERVLET_NAME = "download";
	private static final long   serialVersionUID         = -5641739972260552574L;
	// Return messages to client
	private static final String NO_FILENAME_PARAMETER    = "NO_FILENAME_PARAMETER";
	private static final Log    LOG                      = LogFactory.getLog(DownloadServlet.class);
	private static final String DEFAULT_CHARSET_ENCODING = "UTF-8";

	// Request Parameters
	private static final int    BUFFER_SIZE              = 1024;

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

		// get the fileName parameter
		final String fileName = pRequest.getParameter(ToolProperties.NAME_PARAMETER);
		if ((fileName == null) || (fileName.trim().length() == 0))
		{
			throw new ServletException(NO_FILENAME_PARAMETER);
		}
		final File source = new File(fileName);

		pResponse.setContentType("application/download; charset=" + DEFAULT_CHARSET_ENCODING);
		pResponse.setHeader("Content-disposition", "attachment; filename=" + source.getName());

		// copy the file in the response header (take care to do it after
		// setting the header and content-type)
		try
		{
			final OutputStream out = pResponse.getOutputStream();
			FileInputStream fis;

			fis = new FileInputStream(source);

			final byte[] buffer = new byte[BUFFER_SIZE];
			int n;
			while (-1 != (n = fis.read(buffer)))
			{
				out.write(buffer, 0, n);
			}
			out.flush();
			out.close();
		}
		catch (final IOException e)
		{
			LOG.error(MessageFormat.format("unable to copy file {0} on response header", fileName));
			throw new ServletException();
		}
	}

}
