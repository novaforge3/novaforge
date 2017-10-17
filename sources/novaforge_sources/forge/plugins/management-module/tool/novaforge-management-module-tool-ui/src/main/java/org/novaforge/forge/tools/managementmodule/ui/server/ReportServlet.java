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
package org.novaforge.forge.tools.managementmodule.ui.server;

import org.novaforge.forge.commons.reporting.model.OutputFormat;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportServlet extends HttpServlet
{

  private static final long serialVersionUID = -3762019537541982512L;

  private static final String DEFAULT_CHARSET_ENCODING = "UTF-8";

  private static final String TEMPLATE_DIRECTORY = "/report/";

  private static final String PDF_EXTENSION = ".pdf";

  private static final String RPTDESIGN_EXTENSION = ".rptdesign";

  /**
   * The logger
   */
  private static final Logger LOGGER = Logger.getLogger(ReportServlet.class.getName());

  @Override
  protected void doGet(final HttpServletRequest pReq, final HttpServletResponse pResp)
      throws ServletException, IOException
  {
    this.doPost(pReq, pResp);
  }

  private ManagementModuleManager getManagementModuleManager()
  {
    return OSGiServiceGetter.getService(ManagementModuleManager.class);
  }

  @Override
  protected void doPost(final HttpServletRequest pRequest, final HttpServletResponse pResponse)
      throws ServletException, IOException
  {

    // get the fileName parameter
    final String reportName = pRequest.getParameter(Constants.BIRT_REPORT_NAME_PARAMETER);

    boolean downloadFile = true;

    final Map<String, Object> parameters = new HashMap<String, Object>();

    for (String key : (Set<String>) pRequest.getParameterMap().keySet())
    {
      if ((key.matches("birt_\\w++")) &&
              (key.equalsIgnoreCase(Constants.BIRT_DOWNLOAD_FILE_PARAMETER)) && (pRequest.getParameter(key)
                                                                                         .equalsIgnoreCase("false")))
      {
        downloadFile = false;
      }
      else
      {
        parameters.put(key, pRequest.getParameter(key));
      }
    }

    String rptdesignName = TEMPLATE_DIRECTORY + reportName + RPTDESIGN_EXTENSION;

    // preparing response
    String       fileName = reportName + PDF_EXTENSION;
    OutputFormat format   = OutputFormat.PDF;
    pResponse.setContentType("application/pdf; charset=" + DEFAULT_CHARSET_ENCODING);

    // INFO : for debug, use HTML outputFormat to see errors
    boolean debugMode = false;
    if (debugMode)
    {
      fileName = reportName + ".html";
      format = OutputFormat.HTML;
      pResponse.setContentType("text/html; charset=" + DEFAULT_CHARSET_ENCODING);
      downloadFile = false;
    }
    if (downloadFile)
    {
      pResponse.setHeader("Content-disposition", "attachment; filename=" + fileName);
    }

    try
    {
      // INFO : for debug, use HTML outputFormat to see errors
      this.getManagementModuleManager().renderReport(rptdesignName, format, parameters, pResponse.getOutputStream());
    }
    catch (final ManagementModuleException e1)
    {
      throw new ServletException(e1.getMessage(), e1);
    }
    catch (RuntimeException e)
    {
      LOGGER.log(Level.SEVERE, e.getMessage() + " " + e.getMessage(), e);
    }
  }
}
