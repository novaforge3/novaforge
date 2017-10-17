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
package org.novaforge.forge.plugins.bugtracker.mantis.ard.internal.servlets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.reporting.exceptions.ReportingException;
import org.novaforge.forge.commons.reporting.model.OutputFormat;
import org.novaforge.forge.commons.reporting.services.ReportingService;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.client.MantisARDSoapException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author sbenoist
 */
public class MantisARDReportServlet extends HttpServlet
{
	private static final String PROJECT_ID								 = "projectId";
	/**
	 * serialVersionUID
	 */
	private static final long	 serialVersionUID					 = -4420112051667571438L;
	/**
	 * The parameter for the rptdesign file's name for report generation
	 */
	private static final String BIRT_REPORT_NAME_PARAMETER = "birt_report_name";
	private static final Log    LOG                      = LogFactory.getLog(MantisARDReportServlet.class);
	private static final String DEFAULT_CHARSET_ENCODING = "UTF-8";
	private static final String TEMPLATE_DIRECTORY       = "/report/";
	private static final String PDF_EXTENSION            = ".pdf";
	private static final String RPTDESIGN_EXTENSION      = ".rptdesign";
	private BundleContext			 bundleContext;
	private ReportingService		reportingService;

	/**
	 * Define if the result must be displayed in the page or downloaded
	 */

	public MantisARDReportServlet(final ReportingService pReportingService, final BundleContext pBundleContext)
	{
		LOG.info("InternalReportServlet created");
		reportingService = pReportingService;
		bundleContext = pBundleContext;
	}

	@Override
	protected void doGet(final HttpServletRequest pReq, final HttpServletResponse pResp)
			throws ServletException, IOException
	{

		this.doPost(pReq, pResp);
	}

	@Override
	protected void doPost(final HttpServletRequest pRequest, final HttpServletResponse pResponse)
			throws ServletException, IOException
	{

		// get the fileName parameter
		final String reportName = pRequest.getParameter(BIRT_REPORT_NAME_PARAMETER);

		boolean downloadFile = true;

		final Map<String, Object> parameters = new HashMap<String, Object>();

		if (pRequest.getParameter(PROJECT_ID) != null && !"".equalsIgnoreCase(pRequest.getParameter(PROJECT_ID)))
		{
			parameters.put(PROJECT_ID, pRequest.getParameter(PROJECT_ID));
		}
		LOG.info("Parameters map size : " + parameters.size());

		for (Entry<String, Object> entry : parameters.entrySet())
		{
			LOG.info("entry in parameters map => key : " + entry.getKey() + " value : "
					+ entry.getValue().toString());
		}

		final String rptdesignName = TEMPLATE_DIRECTORY + reportName + RPTDESIGN_EXTENSION;

		// preparing response
		String fileName = reportName + PDF_EXTENSION;
		OutputFormat format = OutputFormat.PDF;
		pResponse.setContentType("application/pdf; charset=" + DEFAULT_CHARSET_ENCODING);

		// INFO : for debug, use HTML outputFormat to see errors
		final boolean debugMode = false;
		if (debugMode)
		{
			fileName = reportName + ".html";
			format = OutputFormat.HTML;
			pResponse.setContentType("application/xhtml+xml; charset=" + DEFAULT_CHARSET_ENCODING);
		}
		if (downloadFile)
		{
			pResponse.setHeader("Content-disposition", "attachment; filename=" + fileName);
		}

		try
		{

			try
			{
				// FIXME the rpdtdesing is embedded in the plugin. It's must be send by the request.
				final Bundle bundle = bundleContext.getBundle();
				final URL url = bundle.getResource(rptdesignName);
				InputStream inputStream = url.openStream();
				reportingService.renderReport(inputStream, format, pResponse.getOutputStream(), parameters);
			}
			catch (ReportingException e)
			{
				throw new MantisARDSoapException(MessageFormat.format("Problem while generate report with name =%s",
						rptdesignName), e);
			}
			catch (IOException e)
			{
				throw new MantisARDSoapException(MessageFormat.format("Problem while reading report design file",
						rptdesignName), e);
			}
		}
		catch (final MantisARDSoapException e1)
		{
			throw new ServletException(e1.getMessage(), e1);
		}
	}

}
