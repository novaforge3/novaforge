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
package com.prt.gwt.file.core;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class GWTResourceServlet extends HttpServlet
{
	/**
    * 
    */
	private static final long serialVersionUID = 1L;
	private static final int  BUFFER_SIZE      = 1024;

	@Override
	protected void doGet(final HttpServletRequest httpServletRequest,
	    final HttpServletResponse httpServletResponse) throws ServletException, IOException
	{
		if (!doProcess(httpServletRequest, httpServletResponse))
		{
			super.doGet(httpServletRequest, httpServletResponse);
		}
	}

	@Override
	protected void doPost(final HttpServletRequest httpServletRequest,
	    final HttpServletResponse httpServletResponse) throws ServletException, IOException
	{
		if (!doProcess(httpServletRequest, httpServletResponse))
		{
			super.doPost(httpServletRequest, httpServletResponse);
		}
	}

	private boolean doProcess(final HttpServletRequest httpServletRequest,
	    final HttpServletResponse httpServletResponse) throws IOException
	{
		final byte[] byteBuffer = new byte[BUFFER_SIZE];
		boolean result = true;
		final String resource = httpServletRequest.getServletPath()
		    + (httpServletRequest.getPathInfo() != null ? httpServletRequest.getPathInfo() : "");
		final InputStream stream = getClass().getResourceAsStream(resource);
		if (stream != null)
		{
			final ServletOutputStream outputStream = httpServletResponse.getOutputStream();
			int len;
			while ((len = stream.read(byteBuffer)) > 0)
			{
				outputStream.write(byteBuffer, 0, len);
			}
			outputStream.flush();
			outputStream.close();
		}
		else
		{
			result = false;
		}
		return result;
	}
}
