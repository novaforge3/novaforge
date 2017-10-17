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
package org.novaforge.forge.commons.reporting.internal;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.novaforge.forge.commons.reporting.exceptions.ReportingException;
import org.novaforge.forge.commons.reporting.model.OutputFormat;
import org.novaforge.forge.commons.reporting.services.ReportingService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author sbenoist
 */
public class ReportingServiceImpl implements ReportingService
{
  private String        logDir;

  private String        logLevel;

  private String        workDir;

  private String        imagesDir;

  private String        logFilename;

  private IReportEngine reportEngine = null;

  /**
   * Method to be called when the Blueprint Container is destroying the object instance.
   */
  public void destroy()
  {
    destroyBirtEngine();
  }

  private synchronized void destroyBirtEngine()
  {
    if (reportEngine != null)
    {
      reportEngine.destroy();
      RegistryProviderFactory.releaseDefault();
      Platform.shutdown();
      reportEngine = null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void renderReport(final String pDesignFileName, final OutputFormat pOutputFormat,
      final String pOutputFileName, final Map<String, Object> pParameters) throws ReportingException
  {
    renderReport(pDesignFileName, null, pOutputFormat, pOutputFileName, null, pParameters);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void renderReport(final InputStream pDesignFileInputStream, final OutputFormat pOutputFormat,
      final String pOutputFileName, final Map<String, Object> pParameters) throws ReportingException
  {
    renderReport(null, pDesignFileInputStream, pOutputFormat, pOutputFileName, null, pParameters);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void renderReport(final String pDesignFileName, final OutputFormat pOutputFormat,
      final OutputStream pOutputStream, final Map<String, Object> pParameters) throws ReportingException
  {
    renderReport(pDesignFileName, null, pOutputFormat, null, pOutputStream, pParameters);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void renderReport(final InputStream pDesignFileInputStream, final OutputFormat pOutputFormat,
      final OutputStream pOutputStream, final Map<String, Object> pParameters) throws ReportingException
  {
    renderReport(null, pDesignFileInputStream, pOutputFormat, null, pOutputStream, pParameters);
  }

  /**
   * This method allows to render a report from a design filename in an output format with given parameters
   *
   * @param pDesignFileName
   *          String
   * @param pDesignFileInputStream
   *          InputStream
   * @param pOutputFormat
   *          OutputFormat
   * @param pOutputFileName
   *          String
   * @param pOutputStream
   *          OutputStream
   * @param pParameters
   *          Map<String,Object>
   * @throws ReportingException
   */
  @SuppressWarnings("unchecked")
  private void renderReport(final String pDesignFileName, final InputStream pDesignFileInputStream,
      final OutputFormat pOutputFormat, final String pOutputFileName, final OutputStream pOutputStream,
      final Map<String, Object> pParameters) throws ReportingException
  {

    if ((pDesignFileName == null) && (pDesignFileInputStream == null))
    {
      throw new ReportingException("no DesignFile defined");
    }
    if ((pOutputFileName == null) && (pOutputStream == null))
    {
      throw new ReportingException("no DesignFile defined");
    }
    // Get the report Engine
    final IReportEngine engine = getEngine();
    try
    {
      // Open the report design
      IReportRunnable design;
      if (pDesignFileName != null)
      {
        design = engine.openReportDesign(pDesignFileName);
      }
      else
      {
        design = engine.openReportDesign(pDesignFileInputStream);
      }

      // Create task to run and render the report,
      final IRunAndRenderTask task = engine.createRunAndRenderTask(design);
      // Set parent classloader for engine
      task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());
      if ((pParameters != null) && (!pParameters.isEmpty()))
      {
        // Set parameters and validate
        for (final Map.Entry<String, Object> entry : pParameters.entrySet())
        {
          task.setParameterValue(entry.getKey(), entry.getValue());
        }
        task.validateParameters();
      }
      // Set the output format options
      final IRenderOption options = getRenderOptions(pOutputFileName, pOutputStream, pOutputFormat);
      task.setRenderOption(options);

      // run and render report
      task.run();
      task.close();
    }
    catch (final EngineException e)
    {
      throw new ReportingException(
          String.format(
              "an error occurred during rendering report with [design filename=%s, ouptput format=%s, output filename=%s ]",
              pDesignFileName, pOutputFormat, pOutputFileName), e);
    }
  }

  @SuppressWarnings("unchecked")
  private synchronized IReportEngine getEngine() throws ReportingException
  {
    if (reportEngine == null)
    {
      final EngineConfig config = new EngineConfig();
      config.setEngineHome("ReportEngine");
      config.setLogConfig(logDir, Level.parse(logLevel));
      config.setLogFile(logFilename);
      config.setTempDir(workDir);
      config.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
                                 ReportingServiceImpl.class.getClassLoader());
      try
      {
        Platform.startup(config);
      }
      catch (final BirtException e)
      {
        throw new ReportingException("unable to startup Birt Platform", e);
      }
      final IReportEngineFactory factory = (IReportEngineFactory) Platform
                                                                      .createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
      if (factory == null)
      {
        throw new ReportingException("unable to instanciate Birt Engine Factory");
      }
      reportEngine = factory.createReportEngine(config);
    }
    return reportEngine;
  }

  @SuppressWarnings("deprecation")
  private IRenderOption getRenderOptions(final String pOutputFileName, final OutputStream pOutputStream,
      final OutputFormat pFormat)
  {
    IRenderOption renderOptions = null;

    final IRenderOption defaultOptions = new RenderOption();
    defaultOptions.setOutputFormat(pFormat.getLabel());
    if (pOutputFileName != null)
    {
      defaultOptions.setOutputFileName(pOutputFileName);
    }
    else
    {
      defaultOptions.setOutputStream(pOutputStream);
    }
    defaultOptions.setSupportedImageFormats("SVG;PNG;JPG;GIF");

    switch (pFormat)
    {
      case PDF:
        renderOptions = new PDFRenderOption(defaultOptions);
        renderOptions.setOption(IPDFRenderOption.FIT_TO_PAGE, Boolean.TRUE);
        renderOptions.setOption(IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY, Boolean.TRUE);
        break;

      case HTML:
        final HTMLRenderOption htmlOptions = new HTMLRenderOption(defaultOptions);
        htmlOptions.setUrlEncoding("utf-8");
        htmlOptions.setHtmlPagination(false);
        htmlOptions.setHtmlRtLFlag(false);
        htmlOptions.setEmbeddable(false);
        htmlOptions.setImageDirectory(imagesDir);
        renderOptions = htmlOptions;
        break;

      case XLS:
        final EXCELRenderOption excelOptions = new EXCELRenderOption(defaultOptions);
        excelOptions.setEnableMultipleSheet(false);
        excelOptions.setHideGridlines(true);
        excelOptions.setWrappingText(true);
        renderOptions = excelOptions;
        break;

      default:
        // No more options available for others formats
        renderOptions = defaultOptions;
    }

    return renderOptions;
  }

  public void setLogDir(final String pLogDir)
  {
    logDir = pLogDir;
  }

  public void setLogLevel(final String pLogLevel)
  {
    logLevel = pLogLevel;
  }

  public void setWorkDir(final String workDir)
  {
    this.workDir = workDir;
  }

  public void setLogFilename(final String logFilename)
  {
    this.logFilename = logFilename;
  }

  public void setImagesDir(final String imagesDir)
  {
    this.imagesDir = imagesDir;
  }
}
