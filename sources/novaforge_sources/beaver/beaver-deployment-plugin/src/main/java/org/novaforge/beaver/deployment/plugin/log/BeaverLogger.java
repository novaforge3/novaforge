/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.beaver.deployment.plugin.log;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.maven.plugin.logging.Log;
import org.novaforge.beaver.exception.BeaverException;

public class BeaverLogger
{
  private final static Logger LOGGER_FILE         = Logger.getLogger("beaver.plugin.file");
  private final static Logger LOGGER_OUT          = Logger.getLogger("beaver.plugin.out");
  private final static Logger LOGGER_SIMULATION   = Logger.getLogger("beaver.plugin.simulation");

  private final static String LOG_PATTERN         = "%d{dd/MM/yyyy HH:mm:ss} [%p] - %m%n";
  private final static String DEPLOYMENT_LOG_NAME = "deployment.log";
  private final static String SIMULATION_LOG_NAME = "simulation_report.log";

  private static File         DEPLOYMENT_LOG;
  private static File         SIMULATION_LOG;

  public final static String  SEPARATOR_TIRET     = "------------------------------------------------------------------";
  public final static String  SEPARATOR_STAR      = "*******************************************************************";

  private BeaverLogger()
  {
    // Utility class should have private explicit constructor ( Sonar rule : Hide Utility Class Constructor )
  }

  public static void setLogger(final String pLogDirectory, final Log pMavenLog) throws BeaverException
  {
    try
    {
      final File dir = new File(pLogDirectory);
      if (dir.exists() == false)
      {
        final boolean mkdirSucces = dir.mkdirs();
        if (mkdirSucces == false)
        {
          throw new BeaverException("Cannot create log's directory: " + dir);
        }
      }
      DEPLOYMENT_LOG = new File(dir + File.separator + DEPLOYMENT_LOG_NAME);

      final PatternLayout layout = new PatternLayout(LOG_PATTERN);
      final ConsoleAppender stdout = new ConsoleAppender(layout);
      final FileAppender fileAppender = new FileAppender(layout, DEPLOYMENT_LOG.getPath());

      LOGGER_OUT.addAppender(stdout);
      LOGGER_FILE.addAppender(fileAppender);

      if (pMavenLog.isDebugEnabled())
      {
        LOGGER_OUT.setLevel(Level.DEBUG);
        LOGGER_FILE.setLevel(Level.DEBUG);
        LOGGER_FILE.addAppender(stdout);
      }
      else if (pMavenLog.isInfoEnabled())
      {
        LOGGER_OUT.setLevel(Level.INFO);
        LOGGER_FILE.setLevel(Level.INFO);
      }
      else if (pMavenLog.isWarnEnabled())
      {
        LOGGER_OUT.setLevel(Level.WARN);
        LOGGER_FILE.setLevel(Level.WARN);
      }
      else if (pMavenLog.isErrorEnabled())
      {
        LOGGER_OUT.setLevel(Level.ERROR);
        LOGGER_FILE.setLevel(Level.ERROR);
      }

      SIMULATION_LOG = new File(dir + File.separator + SIMULATION_LOG_NAME);
      LOGGER_SIMULATION.setAdditivity(false);
      LOGGER_SIMULATION.setLevel(Level.INFO);
      LOGGER_SIMULATION.addAppender(new FileAppender(layout, SIMULATION_LOG.getPath()));
    }
    catch (final IOException e)
    {
      throw new BeaverException("Unable to set up logger", e);
    }
  }

  /**
   * @return the filelogger
   */
  public static Logger getFilelogger()
  {
    return LOGGER_FILE;
  }

  /**
   * @return the outlogger
   */
  public static Logger getOutlogger()
  {
    return LOGGER_OUT;
  }

  /**
   * @return the simulationlogger
   */
  public static Logger getSimulationlogger()
  {
    return LOGGER_SIMULATION;
  }

  /**
   * @return the deploymentLog
   */
  public static File getDeploymentLog()
  {
    return DEPLOYMENT_LOG;
  }

  /**
   * @return the simulationLog
   */
  public static File getSimulationLog()
  {
    return SIMULATION_LOG;
  }
}
