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

import org.novaforge.forge.commons.reporting.services.ReportingService;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;

/**
 * @author sbenoist
 */
public class MantisARDReport
{
	private static final String REPORT_ALIAS_SERVLET = "/mantisARD/reportServlet";
	private boolean						 isServletRegister		= false;
	private BundleContext			 bundleContext;
	private ReportingService		reportingService;
	private HttpService				 httpService;

	/**
	 * Init method
	 * 
	 * @throws PluginServiceException
	 */
	public void init() throws PluginServiceException
	{
		// register the servlet to httpService
		try
		{
			httpService.registerServlet(REPORT_ALIAS_SERVLET, new MantisARDReportServlet(reportingService,
					bundleContext), null, null);
			isServletRegister = true;
		}
		catch (final ServletException e)
		{
			throw new PluginServiceException(String.format(
					"Unable to register Mantis ARD Report servlet with [alias=%s]", REPORT_ALIAS_SERVLET), e);
		}
		catch (final NamespaceException e)
		{
			throw new PluginServiceException(String.format(
					"Unable to register Mantis ARD Report servlet with [alias=%s]", REPORT_ALIAS_SERVLET), e);
		}
	}

	/**
	 * Destroy method usd by container
	 */
	public void destroy()
	{
		if (isServletRegister)
		{
			httpService.unregister(REPORT_ALIAS_SERVLET);
			isServletRegister = false;
		}
	}

	public void setBundleContext(final BundleContext pBundleContext)
	{
		bundleContext = pBundleContext;
	}

	public void setReportingService(final ReportingService pReportingService)
	{
		reportingService = pReportingService;
	}

	public void setHttpService(final HttpService pHttpService)
	{
		httpService = pHttpService;
	}

}
