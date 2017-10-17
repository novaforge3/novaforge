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
package org.novaforge.forge.ui.historization.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.ui.historization.shared.export.CSVProperties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class DownloadServlet extends HttpServlet
{

  private static final long   serialVersionUID         = -6206539116281696688L;

  // Return messages to client
  private static final String NO_FILENAME_PARAMETER    = "NO_FILENAME_PARAMETER";

  private static final String IMPORT_DIRECTORY         = "/tmp";

  private static final Log    LOG                      = LogFactory.getLog(DownloadServlet.class);

  private static final String DEFAULT_CHARSET_ENCODING = "UTF-8";

  private static final String CSV_EXTENSION            = ".csv";

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
    final String fileName = pRequest.getParameter(CSVProperties.FILENAME_PARAMETER);
    if ((fileName == null) || (fileName.trim().length() == 0))
    {
      throw new ServletException(NO_FILENAME_PARAMETER);
    }

    // get the full name where is stored the file to download
    final String fullFileName = getDownloadFullPathName(fileName);

    final File source = new File(fullFileName);
    final File destination = new File(fullFileName + CSV_EXTENSION);

    source.renameTo(destination);

    pResponse.setContentType("application/download; charset=" + DEFAULT_CHARSET_ENCODING);
    pResponse.setHeader("Content-disposition", "attachment; filename=" + destination.getName());

    // copy the file in the response header (take care to do it after setting the header and content-type)
    try
    {
      final PrintWriter out = new PrintWriter(new OutputStreamWriter(pResponse.getOutputStream(),
          DEFAULT_CHARSET_ENCODING));
      copyFile(destination.getAbsolutePath(), out);
      out.flush();
      out.close();
      destination.delete();
    }
    catch (final IOException e)
    {
      e.printStackTrace();
      LOG.error(MessageFormat.format("unable to copy csv file {0} on response header", destination.getName()));
      throw new ServletException();
    }
  }

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

  private void copyFile(final String pFileName, final PrintWriter pWriter) throws IOException
  {
    final BufferedReader buff = new BufferedReader(new FileReader(pFileName));
    try
    {
      String line;
      while ((line = buff.readLine()) != null)
      {
        pWriter.write(line);
        pWriter.write("\n");
      }
    }
    finally
    {
      buff.close();
      pWriter.close();
    }
  }
}
