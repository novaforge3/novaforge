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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.server;

import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * @author BILET-JC Create a CSV file with data in session and name in parameter
 */
public class ExportCSVServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6765684503484457775L;

	private static final String DEFAULT_CHARSET_ENCODING = "UTF-8";

	/** The output format */
	private static final String CSV_EXTENSION = ".csv";

	/** The logger */
	private static final Logger LOGGER = Logger.getLogger(ExportCSVServlet.class.getName());

	@Override
	protected void doGet(final HttpServletRequest pReq, final HttpServletResponse pResp)
			throws ServletException, IOException
	{
		LOGGER.info("open CSV file");
		this.doPost(pReq, pResp);
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		// get data from session
		final String data = (String) request.getSession().getAttribute(Constants.SESSION_EXPORT_CSV_PARAMS);
		// get the name file from parameter
		final String filename = request.getParameter(Constants.EXPORT_CSV_NAME_PARAMETER) + CSV_EXTENSION;

		LOGGER.info("export " + filename + " file");
		OutputStream stream = response.getOutputStream();
		response.setContentType("charset=" + DEFAULT_CHARSET_ENCODING);
		response.addHeader("Content-Type", "application/force-download");
		response.addHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
		response.addHeader("Content-Transfer-Encoding", "text/csv\n");
		PrintWriter flux = new PrintWriter(stream);
		flux.write(data);
		flux.flush();
	}
}
