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
package org.novaforge.forge.core.configuration.internal.services.properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.novaforge.forge.core.configuration.exceptions.ForgeConfigurationException;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.configuration.services.properties.ForgePropertiesService;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

/**
 * Service implementation of {@link ForgePropertiesService}
 * 
 * @author Guillaume Lamirand
 */
public class ForgePropertiesServiceImpl implements ForgePropertiesService
{

  /**
   * Encoding value
   */
  private static final String     ENCODING                = "UTF-8";
  /**
   * Default file name
   */
  private static final String     CONFIG_FILE             = "novaforge.properties";
  /*
   * Reference to forgeCfgService service
   */
  private ForgeCfgService forgeCfgService;
  /*
   * Contains key/value read from original properties files
   */
  private PropertiesConfiguration propertiesConfiguration = null;
  private File propertiesFile;

  /**
   * Callback method call when the component become valid.
   * This will read the properties file and build configuration associated.
   *
   * @throws ForgeConfigurationException
   *           throw if the configuration file can be read
   */
  public void init() throws ForgeConfigurationException
  {
    refresh();
  }

  private void buildPropertiesConfiguration() throws ForgeConfigurationException
  {
    if ((propertiesFile != null) && (propertiesFile.exists()))
    {
      try
      {
        // load a properties file
        propertiesConfiguration = new PropertiesConfiguration(propertiesFile);
        propertiesConfiguration.setEncoding(ENCODING);
        propertiesConfiguration.setReloadingStrategy(new FileChangedReloadingStrategy());
        propertiesConfiguration.setAutoSave(true);
      }
      catch (final ConfigurationException e)
      {
        throw new ForgeConfigurationException(String.format("Unable to read properties file with [path=%s]",
                                                            propertiesFile.getAbsolutePath()), e);
      }
    }
    else
    {
      throw new ForgeConfigurationException(String.format("NovaForge properties file doesn't exist with [path=%s]",
                                                          propertiesFile.getAbsolutePath()));
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.commons.configuration.AbstractConfiguration#getBoolean(java.lang.String)
   */
  @Override
  public boolean getPropertyAsBoolean(final String pKey)
  {
    return propertiesConfiguration.getBoolean(pKey);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.commons.configuration.AbstractConfiguration#getByte(java.lang.String)
   */
  @Override
  public byte getPropertyAsByte(final String pKey)
  {
    return propertiesConfiguration.getByte(pKey);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.commons.configuration.AbstractConfiguration#getDouble(java.lang.String)
   */
  @Override
  public double getPropertyAsDouble(final String pKey)
  {
    return propertiesConfiguration.getDouble(pKey);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.commons.configuration.AbstractConfiguration#getFloat(java.lang.String)
   */
  @Override
  public float getPropertyAsFloat(final String pKey)
  {
    return propertiesConfiguration.getFloat(pKey);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.commons.configuration.AbstractConfiguration#getInt(java.lang.String)
   */
  @Override
  public int getPropertyAsInt(final String pKey)
  {
    return propertiesConfiguration.getInt(pKey);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.commons.configuration.AbstractConfiguration#getLong(java.lang.String)
   */
  @Override
  public long getPropertyAsLong(final String pKey)
  {
    return propertiesConfiguration.getLong(pKey);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.commons.configuration.AbstractConfiguration#getBigInteger(java.lang.String)
   */
  @Override
  public BigInteger getPropertyAsBigInteger(final String pKey)
  {
    return propertiesConfiguration.getBigInteger(pKey);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.commons.configuration.AbstractConfiguration#getString(java.lang.String)
   */
  @Override
  public String getPropertyAsString(final String pKey)
  {
    return propertiesConfiguration.getString(pKey);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.commons.configuration.AbstractConfiguration#getList(java.lang.String)
   */
  @Override
  public List<Object> getList(final String pKey)
  {
    return propertiesConfiguration.getList(pKey);
  }

  /**
   * {@inheritDoc}
   * <p>
   * If not exists, it will build a new {@link PropertiesConfiguration} from properties file, otherwise it
   * will just refresh configuration content.
   * </p>
   *
   * @throws ForgeConfigurationException
   *     throw if the configuration file can be read
   */
  @Override
  public void refresh() throws ForgeConfigurationException
  {
    final File newPropertiesFile = new File(forgeCfgService.getForgeConfDirectory(), CONFIG_FILE);
    if (propertiesConfiguration == null)
    {
      propertiesFile = newPropertiesFile;
      buildPropertiesConfiguration();
    }
    else if ((!propertiesFile.equals(newPropertiesFile)) && (newPropertiesFile.exists()))
    {
      propertiesFile = newPropertiesFile;
      propertiesConfiguration.setFile(propertiesFile);
      propertiesConfiguration.reload();
    }
    else
    {
      propertiesConfiguration.reload();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean reloadingRequired()
  {
    return propertiesConfiguration.getReloadingStrategy().reloadingRequired();
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.apache.commons.configuration.AbstractFileConfiguration#setProperty(java.lang.String,
   *      java.lang.Object)
   */
  @Override
  public void setProperty(final String pKey, final Object pValue)
  {
    propertiesConfiguration.setProperty(pKey, pValue);
  }

  /**
   * Bind method used by the container to inject {@link ForgeCfgService} service.
   * 
   * @param pForgeCfgService
   *          the ForgeCfgService to set
   */
  public void setForgeCfgService(final ForgeCfgService pForgeCfgService)
  {
    forgeCfgService = pForgeCfgService;
  }

}
