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
package com.prt.gwt.file.core.server.service;

import com.prt.gwt.file.core.client.util.Constants;
import com.prt.gwt.file.core.server.utils.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FileDownloadServlet extends HttpServlet
{

	/**
    * 
    */
	private static final long serialVersionUID = 6705134901282894111L;

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
	    IOException
	{
		doPost(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
	    throws ServletException, IOException
	{
		final String filePath = req.getParameter(Constants.FILE_PATH_PARAM);
		Long startWith;
		Long lines;
		try
		{
			startWith = Long.valueOf(req.getParameter(Constants.FILE_START_LINE));
			lines = Long.valueOf(req.getParameter(Constants.FILE_LINES));
		}
		catch (final NumberFormatException e)
		{
			startWith = null;
			lines = null;
		}
		FileUtils.sendZipFile(filePath, startWith, lines, resp);
	}
}
