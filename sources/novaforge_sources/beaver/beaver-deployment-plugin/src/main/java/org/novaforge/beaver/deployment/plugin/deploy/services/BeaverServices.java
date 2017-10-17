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
package org.novaforge.beaver.deployment.plugin.deploy.services;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.context.DeploymentContext;
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;
import org.novaforge.beaver.deployment.plugin.deploy.model.ProductProcess;
import org.novaforge.beaver.deployment.plugin.deploy.services.impl.ConfigServiceImpl;
import org.novaforge.beaver.deployment.plugin.deploy.services.impl.DeployServiceImpl;
import org.novaforge.beaver.deployment.plugin.deploy.services.impl.LauncherServiceImpl;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * This utility class offers services reference
 * 
 * @author Guillaume Lamirand
 */
public class BeaverServices
{
  private static Path              TEMP_DIRECTORY;
  private static DeploymentContext CONTEXT_FILE;
  private static DeploymentContext BACKUP_CONTEXT_FILE;
  private static BeaverEngine      ENGINE;
  private static ProductProcess    CURRENT_PRODUCT_PROCESS;
  private static MojoService       MOJO_SERVICE;
  private static DeployService     DEPLOY_SERVICE;
  private static ConfigService     CONFIG_SERVICE;
  private static LauncherService   LAUNCHER_SERVICE;
  private static boolean           RESOLVED;
  private static boolean           SIMULATE;
  private static String            SERVER_ID;

  private BeaverServices()
  {
    // Utility class should have private explicit constructor
  }

  /**
   * @param pMojoService
   *          the fMojoService to set
   */
  public static void init(final MojoService pMojoService, final String pTemp, final boolean pResolved,
      final boolean pSimulate, final String pServerId)
  {
    RESOLVED = pResolved;
    SIMULATE = pSimulate;
    SERVER_ID = pServerId;

    // Set commons services
    MOJO_SERVICE = pMojoService;
    DEPLOY_SERVICE = new DeployServiceImpl();
    CONFIG_SERVICE = new ConfigServiceImpl();
    LAUNCHER_SERVICE = new LauncherServiceImpl();

    // Set tmp path
    if (StringUtils.isNotBlank(pTemp))
    {
      final Path tmpPath = Paths.get(pTemp);
      if (Files.notExists(tmpPath))
      {
        try
        {
          Files.createDirectories(tmpPath);
        }
        catch (final IOException e)
        {
          // ignore this
        }
      }
      TEMP_DIRECTORY = tmpPath;
    }

  }

  /**
   * Build persistence implementation used to manage the persistence file.
   * 
   * @throws BeaverException
   */
  public static void buildContextFile(final String pPersistenceClass, final String pPersistenceFile)
      throws BeaverException
  {
    try
    {
      final Class<?> deployClass = Class.forName(pPersistenceClass);
      final Class<?>[] deployClassInterfaces = deployClass.getInterfaces();
      for (final Class<?> implinterface : deployClassInterfaces)
      {
        if (implinterface.getName().equals("org.novaforge.beaver.context.DeploymentContext"))
        {
          BeaverLogger.getOutlogger().debug(
              "The deployment will use the implementation " + implinterface + " to store data.");
          BeaverLogger.getOutlogger().debug(
              "------------------------------------------------------------------");
          Constructor<?> constructor;
          constructor = deployClass.getDeclaredConstructor(new Class[] { Class.forName("java.lang.String") });

          if (constructor != null)
          {
            BACKUP_CONTEXT_FILE = (DeploymentContext) constructor
                .newInstance(new Object[] { pPersistenceFile });
            CONTEXT_FILE = (DeploymentContext) constructor.newInstance(new Object[] { pPersistenceFile });
          }
          else
          {
            throw new BeaverException("Your implementation " + pPersistenceClass
                + " doesn't define any constructeur " + pPersistenceClass + "(String)");
          }
        }
        else
        {
          throw new BeaverException(
              "Implementation specified doesn't implement the correct class DeploymentContext");
        }
      }
    }
    catch (final Exception e)
    {
      throw new BeaverException(e.toString(), e);
    }
  }

  /**
   * Copy the current DeploymentContext to the backup DeploymentContext
   */
  public static void backupDeploymentContext(final String pDeploymentImplClass) throws BeaverException
  {
    try
    {
      final Class<?> deployClass = Class.forName(pDeploymentImplClass);
      final Class<?>[] deployClassInterfaces = deployClass.getInterfaces();
      for (final Class<?> implinterface : deployClassInterfaces)
      {
        if (implinterface.getName().equals("org.novaforge.beaver.context.DeploymentContext"))
        {
          BeaverLogger.getOutlogger().debug(
              "The deployment will use the implementation " + implinterface + " to store data.");
          BeaverLogger.getOutlogger().debug(
              "------------------------------------------------------------------");
          Constructor<?> constructor;
          constructor = deployClass
              .getDeclaredConstructor(new Class[] { Class.forName(pDeploymentImplClass) });

          if (constructor != null)
          {
            BACKUP_CONTEXT_FILE = (DeploymentContext) constructor.newInstance(new Object[] { CONTEXT_FILE });
          }
          else
          {
            throw new BeaverException("Your implementation " + pDeploymentImplClass
                + " doesn't define any constructeur " + pDeploymentImplClass + "(" + pDeploymentImplClass
                + ")");
          }
        }
        else
        {
          throw new BeaverException(
              "Implementation specified doesn't implement the correct class DeploymentContext");
        }
      }
    }
    catch (final Exception e)
    {
      throw new BeaverException(e.toString(), e);
    }
  }

  public static Path getTempDirectory()
  {
    return TEMP_DIRECTORY;
  }

  /**
   * Returns true if resolved mode is enabled
   * 
   * @return simulate mode
   */
  public static boolean isResolved()
  {
    return RESOLVED;
  }

  /**
   * Returns true if simulate mode is enabled
   * 
   * @return simulate mode
   */
  public static boolean isSimulate()
  {
    return SIMULATE;
  }

  /**
   * Return the server id if it is defined, <code>null</code> otherwize
   * @return the server id
   */
  public static String getServerId()
  {
    return SERVER_ID;
  }
  
  /**
   * @return the implPersistenceFile
   */
  public static DeploymentContext getDeploymentContext()
  {
    return CONTEXT_FILE;
  }

  /**
   * @return the backPersistenceFile
   */
  public static DeploymentContext getBackDeploymentContext()
  {
    return BACKUP_CONTEXT_FILE;
  }

  /**
   * @param pDeploymentContext
   *          the implPersistenceFile to set
   */
  public static void setDeploymentContext(final DeploymentContext pDeploymentContext)
  {
    CONTEXT_FILE = pDeploymentContext;
  }

  /**
   * @param pBackDeploymentContext
   *          the backPersistenceFile to set
   */
  public static void setBackDeploymentContext(final DeploymentContext pBackDeploymentContext)
  {
    BACKUP_CONTEXT_FILE = pBackDeploymentContext;
  }

  /**
   * @param pEngineImpl
   *          the fEngineImpl to set
   * @throws BeaverException
   */
  public static void setEngine(final BeaverEngine pEngineImpl) throws BeaverException
  {
    ENGINE = pEngineImpl;
  }

  /**
   * @return the engineImpl
   */
  public static BeaverEngine getEngine()
  {
    return ENGINE;
  }

  /**
   * @param pCurrentProductProcess
   *          the fCurrentProductContext to set
   */
  public static void setCurrentProductProcess(final ProductProcess pCurrentProductProcess)
  {
    CURRENT_PRODUCT_PROCESS = pCurrentProductProcess;
  }

  /**
   * @return the fCurrentProductContext
   */
  public static ProductProcess getCurrentProductProcess()
  {
    return CURRENT_PRODUCT_PROCESS;
  }

  /**
   * @return the launcher Service
   */
  public static LauncherService getLauncherService()
  {
    return LAUNCHER_SERVICE;
  }

  /**
   * @return the fMojoService
   */
  public static MojoService getMojoService()
  {
    return MOJO_SERVICE;
  }

  /**
   * @return the deployService
   */
  public static DeployService getDeployService()
  {
    return DEPLOY_SERVICE;
  }

  /**
   * @return the configService
   */
  public static ConfigService getConfigService()
  {
    return CONFIG_SERVICE;
  }
}
