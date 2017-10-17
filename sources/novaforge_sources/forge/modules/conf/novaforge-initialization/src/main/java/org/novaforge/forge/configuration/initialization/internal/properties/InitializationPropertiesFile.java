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
package org.novaforge.forge.configuration.initialization.internal.properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.io.FileUtils;
import org.novaforge.forge.configuration.initialization.exceptions.ForgeInitializationException;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;

import java.io.File;
import java.io.IOException;

/**
 * Bean used to read initialization configuration
 * 
 * @author Guillaume Lamirand
 */
public class InitializationPropertiesFile
{

  /**
   * Default file name
   */
  private static final String       CONFIG_FILE             = "novaforge-init.properties";
  /**
   * Lock file name
   */
  private static final String       LOCK_FILE               = "novaforge-init.properties.lock";
  /*
   * Reference to forgeCfgService service
   */
  private ForgeConfigurationService forgeConfigurationService;
  /*
   * Contains key/value read from original properties files
   */
  private PropertiesConfiguration propertiesConfiguration = null;
  private File propertiesFile;
  private File lockPropertiesFile;

  /**
   * Callback method call when the component become valid.
   * This will read the properties file and build configuration associated.
   *
   * @throws ForgeInitializationException
   *           throw if the configuration file can be read
   */
  public void init() throws ForgeInitializationException
  {
    propertiesFile = new File(forgeConfigurationService.getForgeConfDirectory(), CONFIG_FILE);
    lockPropertiesFile = new File(forgeConfigurationService.getForgeConfDirectory(), LOCK_FILE);
    if ((!isLocked()) && (propertiesConfiguration == null))
    {
      buildPropertiesConfiguration(propertiesFile);
    }
    else if (!isLocked())
    {
      propertiesConfiguration.setFile(propertiesFile);
      propertiesConfiguration.reload();
    }
    else if ((isLocked()) && (propertiesConfiguration == null))
    {
      buildPropertiesConfiguration(lockPropertiesFile);
    }
    else if (isLocked())
    {
      propertiesConfiguration.setFile(lockPropertiesFile);
      propertiesConfiguration.reload();
    }
  }

  /**
   * Check if properties file has been locked
   *
   * @return <code>true</code> if initialization properties file has been locked, <code>false</code> otherwise
   */
  public boolean isLocked()
  {
    return (!propertiesFile.exists()) && (lockPropertiesFile.exists());
  }

  private void buildPropertiesConfiguration(final File pPropertiesFile) throws ForgeInitializationException
  {
    if ((pPropertiesFile != null) && (pPropertiesFile.exists()))
    {
      try
      {
        // load a properties file
        propertiesConfiguration = new PropertiesConfiguration(pPropertiesFile);
        propertiesConfiguration.setReloadingStrategy(new FileChangedReloadingStrategy());
        propertiesConfiguration.setAutoSave(true);
      }
      catch (final ConfigurationException e)
      {
        throw new ForgeInitializationException(String.format("Unable to read properties file with [path=%s]",
                                                             propertiesFile.getAbsolutePath()), e);
      }
    }
    else
    {
      throw new ForgeInitializationException(String.format(
          "NovaForge initialization properties file doesn't exist with [path=%s]",
          pPropertiesFile.getAbsolutePath()));
    }
  }

  /**
   * Lock properties file avoiding to initialize forge twice
   * 
   * @throws ForgeInitializationException
   *           thrown if lock file cannot be created
   */
  public void lockProperties() throws ForgeInitializationException
  {
    try
    {
      if ((lockPropertiesFile.exists()) && (propertiesFile.exists()))
      {
        FileUtils.deleteQuietly(lockPropertiesFile);
      }
      FileUtils.moveFile(propertiesFile, lockPropertiesFile);
    }
    catch (final IOException e)
    {
      throw new ForgeInitializationException(String.format(
          "Unable to lock properties file with [src=%s, target=%s]", propertiesFile.getAbsolutePath(),
          lockPropertiesFile.getAbsolutePath()), e);
    }
  }

  /**
   * Returns the current {@link PropertiesConfiguration} file
   * 
   * @return the current {@link PropertiesConfiguration} file
   */
  public PropertiesConfiguration getPropertiesConfiguration()
  {
    return propertiesConfiguration;
  }

  /**
   * Bind method used by the container to inject {@link ForgeConfigurationService} service.
   * 
   * @param pForgeConfigurationService
   *          the ForgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

}
