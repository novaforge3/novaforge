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
package org.novaforge.forge.commons.webserver.configuration.internal.services;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.webserver.configuration.exceptions.WebServerConfigurationException;
import org.novaforge.forge.commons.webserver.configuration.internal.model.WebServerGlobalConfigurationImpl;
import org.novaforge.forge.commons.webserver.configuration.internal.utils.ConfigurationHandler;
import org.novaforge.forge.commons.webserver.configuration.internal.utils.FileConfigHandler;
import org.novaforge.forge.commons.webserver.configuration.internal.utils.FileNameHandler;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerAdministrator;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerConfiguration;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerConfiguratorConstants;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerGlobalConfiguration;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerProxyConfiguration;
import org.novaforge.forge.commons.webserver.configuration.services.WebServerConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author caseryj
 */
public class WebServerConfiguratorImpl implements WebServerConfigurator
{
  /**
   * LOGGER
   */
  private static final Log LOGGER = LogFactory.getLog(WebServerConfiguratorImpl.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public void desactivate(final String pConfName, final List<String> pPublicAlias,
      final Map<String, String> pSuperAdmins) throws WebServerConfigurationException
  {
    if (pConfName == null)
    {
      throw new UnsupportedOperationException("One of required parameters is missing.");
    }
    final WebServerGlobalConfiguration conf = new WebServerGlobalConfigurationImpl(pConfName, pPublicAlias,
        buildWebServerAdministratorList(pSuperAdmins));
    if (FileConfigHandler.saveConfigFile(conf))
    {
      final String confToWrite = generateRestrictedMapping(conf);
      if (!confToWrite.isEmpty())
      {
        if (FileConfigHandler.writeConfigurationToFile(
            FileNameHandler.getConfigFileName(ConfigurationHandler.getServicesFolder(), conf), confToWrite))
        {
          try
          {
            reloadWebServer();
          }
          catch (final WebServerConfigurationException e)
          {
            rollBackForStep(3, conf);
            throw e;
          }
        }
        else
        {
          rollBackForStep(2, conf);
          throw new WebServerConfigurationException("Unable to write configuration file.");
        }
      }
      else
      {
        rollBackForStep(2, conf);
        throw new WebServerConfigurationException("The configuration to write is empty.");
      }
    }
    else
    {
      throw new WebServerConfigurationException("Unable to save existing configuration file.");
    }
  }

  private List<WebServerAdministrator> buildWebServerAdministratorList(final Map<String, String> pSuperAdmins)
  {
    final List<WebServerAdministrator> adminList = new ArrayList<WebServerAdministrator>();
    for (final Map.Entry<String, String> entry : pSuperAdmins.entrySet())
    {
      adminList.add(new WebServerAdministrator(entry.getKey(), entry.getValue()));
    }
    return adminList;
  }

  private String generateRestrictedMapping(final WebServerGlobalConfiguration pConf)
      throws WebServerConfigurationException
  {
    final StringBuilder restrictedRule = new StringBuilder();
    // Generate Valid User file
    if (generateValidUserFile(pConf))
    {
      for (final String alias : pConf.getPublicAliases())
      {
        // Build Location directive
        restrictedRule.append("<").append(WebServerConfiguratorConstants.WEBSERVER_DIRECTIVE_LOCATION).append(" ")
                      .append(alias).append(">").append(WebServerConfiguratorConstants.FILE_EOL);

        // Restrict acces only for given Admins
        restrictedRule.append("\t").append(WebServerConfiguratorConstants.WEBSERVER_DESACTIVATED_AUTHTYPE)
                      .append(WebServerConfiguratorConstants.FILE_EOL);
        restrictedRule.append("\t").append(WebServerConfiguratorConstants.WEBSERVER_DESACTIVATED_AUTHNAME)
                      .append(WebServerConfiguratorConstants.FILE_EOL);
        restrictedRule.append("\t").append(WebServerConfiguratorConstants.WEBSERVER_DESACTIVATED_ACCESSFILE).append(" ")
                      .append(FileNameHandler.getValidUserFileName(ConfigurationHandler.getServicesFolder(), pConf))
                      .append(WebServerConfiguratorConstants.FILE_EOL);
        restrictedRule.append("\t").append(WebServerConfiguratorConstants.WEBSERVER_DESACTIVATED_REQUIRE)
                      .append(WebServerConfiguratorConstants.FILE_EOL);
        restrictedRule.append("\t").append(WebServerConfiguratorConstants.WEBSERVER_DIRECTIVE_ERRORDOCUMENT)
                      .append(" \"").append(WebServerConfiguratorConstants.WEBSERVER_REDIRECTMSG_DESACTIVATED)
                      .append("\"").append(WebServerConfiguratorConstants.FILE_EOL);
        restrictedRule.append("</").append(WebServerConfiguratorConstants.WEBSERVER_DIRECTIVE_LOCATION).append(">")
                      .append(WebServerConfiguratorConstants.FILE_EOL);

      }
      // Include saved file
      restrictedRule.append(generateSavedConfigArgs(pConf));
    }
    else
    {
      rollBackForStep(1, pConf);
      throw new WebServerConfigurationException("Unable to generate valid-user file");
    }
    return restrictedRule.toString();
  }

  /**
   * @param pStep
   *     The current step : 1 (Configuration file was only saved), 2 (Configuration file was saved and
   *     valid-user file was generated), 3 (Configuration file was saved, valid-user file was generated
   *     and new Configuration writed)
   * @param pConf
   *     The WebServerConfiguration to rollback
   *
   * @throws WebServerConfigurationException
   */
  private void rollBackForStep(final int pStep, final WebServerConfiguration pConf)
      throws WebServerConfigurationException
  {
    switch (pStep)
    {
      /**
       * Case 1 : Configuration was only saved
       */
      case 1:
        FileConfigHandler.restoreConfigFile(pConf);
        break;
      /**
       * Case 2 : Configuration was saved and valid-user file was generated
       */
      case 2:
        FileConfigHandler.deleteValidUserFile(pConf);
        FileConfigHandler.restoreConfigFile(pConf);
        break;
      /**
       * Case 3 : Configuration was saved, valid-user file was generated and new Configuration writed
       */
      case 3:
        FileConfigHandler.deleteValidUserFile(pConf);
        FileConfigHandler.restoreConfigFile(pConf);
        break;
    }
  }

  private boolean generateValidUserFile(final WebServerGlobalConfiguration pConf) throws WebServerConfigurationException
  {
    try
    {
      final Path validUserFile = FileNameHandler.getValidUserFileName(ConfigurationHandler.getServicesFolder(), pConf);
      Files.deleteIfExists(validUserFile);
      final List<WebServerAdministrator> superadmins = pConf.getAdmins();

      for (final WebServerAdministrator admin : superadmins)
      {
        final String cryptedPassword = cryptPassword(admin.getAdminPassword());
        Files.write(validUserFile,
                    (admin.getAdminLogin() + WebServerConfiguratorConstants.WEBSERVER_ACCESSFILE_SEP + cryptedPassword
                         + WebServerConfiguratorConstants.FILE_EOL).getBytes(), StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE);
      }
      return true;
    }
    catch (final IOException e)
    {
      throw new WebServerConfigurationException("Can't write access file.", e);
    }
  }

  private String generateSavedConfigArgs(final WebServerConfiguration pConf)
  {
    final StringBuilder extraArgsSB = new StringBuilder();
    final Path extraFile = FileNameHandler.getSavedConfigFileName(ConfigurationHandler.getServicesFolder(), pConf);
    if (Files.exists(extraFile))
    {
      extraArgsSB.append(WebServerConfiguratorConstants.WEBSERVER_DIRECTIVE_INCLUDE).append(" ").append(extraFile
                                                                                                            .toString())
                 .append(WebServerConfiguratorConstants.FILE_EOL);
    }
    return extraArgsSB.toString();
  }

  private String convertStreamToStr(final InputStream is) throws IOException
  {
    String response = "";
    if (is != null)
    {
      response = IOUtils.toString(is, Charsets.UTF_8);
    }
    return response.trim();
  }

  private String cryptPassword(final String pPassword) throws WebServerConfigurationException
  {
    try
    {
      final byte[] encryptedPassword = Base64.encodeBase64(Hex.decodeHex(pPassword.toCharArray()));
      final String result = new String(encryptedPassword);
      return String.format("{SHA}%s", result);
    }
    catch (final DecoderException e)
    {
      throw new WebServerConfigurationException("Unable to crypt password", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reactivate(final String pConfName) throws WebServerConfigurationException
  {
    if (pConfName == null)
    {
      throw new UnsupportedOperationException("Configuration Name parameter is missing.");
    }
    final WebServerConfiguration conf = new WebServerGlobalConfigurationImpl(pConfName);
    if (FileConfigHandler.deleteValidUserFile(conf))
    {
      if (FileConfigHandler.restoreConfigFile(conf))
      {
        try
        {
          reloadWebServer();
        }
        catch (final WebServerConfigurationException e)
        {
          rollBackForStep(3, conf);
          throw e;
        }
      }
      else
      {
        throw new WebServerConfigurationException("Unable to restore saved webserver configuration file.");
      }
    }
    else
    {
      throw new WebServerConfigurationException("Unable to delete valid-user file.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reloadWebServer() throws WebServerConfigurationException
  {
    final ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", ConfigurationHandler.getReloadCmd());
    pb.redirectErrorStream(true);

    try
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug("Reloading WebServer requested.");
      }
      final Process process = pb.start();

      // Wait for the process to finish and get the return code
      final int exitValue = process.waitFor();
      if (exitValue != 0)
      {
        // capture output from the process
        final InputStream inputStream = process.getInputStream();
        final InputStream errorStream = process.getErrorStream();

        final String errorMessage = String
            .format(
                "An error occured during the apache reload with exit value=%s, output message=%s, error message=%s",
                exitValue, convertStreamToStr(inputStream), convertStreamToStr(errorStream));

        inputStream.close();
        errorStream.close();
        throw new WebServerConfigurationException(errorMessage);
      }
    }
    catch (final IOException e)
    {
      throw new WebServerConfigurationException("An error occured during the apache reload", e);
    }
    catch (final InterruptedException e)
    {
      throw new WebServerConfigurationException("An error occured during the apache reload", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop(final String pConfName, final List<String> pPublicAlias) throws WebServerConfigurationException
  {
    if (pConfName == null)
    {
      throw new UnsupportedOperationException("One of required parameters is missing.");
    }
    final WebServerGlobalConfiguration conf = new WebServerGlobalConfigurationImpl(pConfName, pPublicAlias);
    if (FileConfigHandler.saveConfigFile(conf))
    {
      final String confToWrite = generateDenyMapping(conf);
      if (!confToWrite.isEmpty())
      {
        if (FileConfigHandler.writeConfigurationToFile(FileNameHandler.getConfigFileName(ConfigurationHandler
                                                                                             .getServicesFolder(),
                                                                                         conf), confToWrite))
        {
          reloadWebServer();
        }
        else
        {
          rollBackForStep(3, conf);
          throw new WebServerConfigurationException("Unable to write webserver configuration to file");
        }
      }
      else
      {
        rollBackForStep(1, conf);
        throw new WebServerConfigurationException("Unable to generate webserver configuration file, missing arguments.");
      }
    }
    else
    {
      throw new WebServerConfigurationException("Unable to save existing webserver configuration file");
    }

  }

  private String generateDenyMapping(final WebServerGlobalConfiguration pConf)
  {
    final StringBuilder denyRule = new StringBuilder();
    for (final String alias : pConf.getPublicAliases())
    {
      denyRule.append("<").append(WebServerConfiguratorConstants.WEBSERVER_DIRECTIVE_LOCATION).append(" ")
          .append(alias).append(">").append(WebServerConfiguratorConstants.FILE_EOL);
      denyRule.append("\t").append(WebServerConfiguratorConstants.WEBSERVER_STOPPED_ORDER)
          .append(WebServerConfiguratorConstants.FILE_EOL);
      denyRule.append("\t").append(WebServerConfiguratorConstants.WEBSERVER_STOPPED_DENY)
          .append(WebServerConfiguratorConstants.FILE_EOL);
      denyRule.append("\t").append(WebServerConfiguratorConstants.WEBSERVER_DIRECTIVE_ERRORDOCUMENT)
          .append(" \"").append(WebServerConfiguratorConstants.WEBSERVER_REDIRECTMSG_DESACTIVATED)
          .append("\"").append(WebServerConfiguratorConstants.FILE_EOL);
      denyRule.append("</").append(WebServerConfiguratorConstants.WEBSERVER_DIRECTIVE_LOCATION).append(">")
          .append(WebServerConfiguratorConstants.FILE_EOL);
    }

    return denyRule.toString();
  }

  @Override
  public void addProxySettings(final WebServerProxyConfiguration pWebServerProxyConfiguration)
      throws WebServerConfigurationException
  {
    if ((pWebServerProxyConfiguration == null)
        || (StringUtils.isBlank(pWebServerProxyConfiguration.getName()))
        || (StringUtils.isBlank(pWebServerProxyConfiguration.getProxySettings())))
    {
      throw new IllegalArgumentException(String.format(
          "WebServerProxyConfiguration argument is invalid [%s]", pWebServerProxyConfiguration));
    }
    Path targetFile = FileNameHandler.getConfigFileName(ConfigurationHandler.getServicesFolder(),
        pWebServerProxyConfiguration);
    final Path savFile = FileNameHandler.getSavedConfigFileName(ConfigurationHandler.getServicesFolder(),
        pWebServerProxyConfiguration);
    if (Files.exists(savFile))
    {
      targetFile = savFile;
    }
    if (FileConfigHandler.backupFile(pWebServerProxyConfiguration, targetFile))
    {
      final String confToWrite = pWebServerProxyConfiguration.getProxySettings();
      if (!confToWrite.isEmpty())
      {
        if (FileConfigHandler.appendConfigurationToFile(targetFile, confToWrite))
        {
          try
          {
            reloadWebServer();
            FileConfigHandler.deleteBackupFile(pWebServerProxyConfiguration);
          }
          catch (final WebServerConfigurationException e)
          {
            FileConfigHandler.restoreBackup(pWebServerProxyConfiguration, targetFile);
            throw e;
          }
        }
        else
        {
          FileConfigHandler.restoreBackup(pWebServerProxyConfiguration, targetFile);
          throw new WebServerConfigurationException("Unable to write configuration file.");
        }
      }
      else
      {
        FileConfigHandler.restoreBackup(pWebServerProxyConfiguration, targetFile);
        throw new WebServerConfigurationException("The configuration to write is empty.");
      }
    }
    else
    {
      throw new WebServerConfigurationException(
          "Unable to backup existing configuration file, check if backup file isn't already existing.");
    }
  }

  @Override
  public void removeProxySettings(final WebServerProxyConfiguration pWebServerProxyConfiguration)
      throws WebServerConfigurationException
  {
    if ((pWebServerProxyConfiguration == null)
        || (StringUtils.isBlank(pWebServerProxyConfiguration.getName())))
    {
      throw new IllegalArgumentException(String.format(
          "WebServerProxyConfiguration argument is invalid [%s]", pWebServerProxyConfiguration));
    }
    Path targetFile = FileNameHandler.getConfigFileName(ConfigurationHandler.getServicesFolder(),
        pWebServerProxyConfiguration);
    final Path savFile = FileNameHandler.getSavedConfigFileName(ConfigurationHandler.getServicesFolder(),
        pWebServerProxyConfiguration);
    if (Files.exists(savFile))
    {
      targetFile = savFile;
    }
    if (Files.exists(targetFile))
    {
      if (FileConfigHandler.backupFile(pWebServerProxyConfiguration, targetFile))
      {
        if (FileConfigHandler.deleteLines(targetFile, pWebServerProxyConfiguration.getStartToken(),
            pWebServerProxyConfiguration.getEndToken()))
        {
          try
          {
            reloadWebServer();
            FileConfigHandler.deleteBackupFile(pWebServerProxyConfiguration);
          }
          catch (final WebServerConfigurationException e)
          {
            FileConfigHandler.restoreBackup(pWebServerProxyConfiguration, targetFile);
            throw e;
          }
        }
        else
        {
          FileConfigHandler.restoreBackup(pWebServerProxyConfiguration, targetFile);
          throw new WebServerConfigurationException("Unable to write configuration file.");
        }
      }
      else
      {
        throw new WebServerConfigurationException("Unable to save existing configuration file.");
      }
    }

  }

  /**
   * @param pConfFolder
   *          the confBuilder to set
   */
  public void setConfFolder(final String pConfFolder)
  {
    final StringBuilder confBuilder = new StringBuilder(pConfFolder);
    if (!pConfFolder.endsWith("/"))
    {
      confBuilder.append("/");
    }
    ConfigurationHandler.setConfFolder(confBuilder.toString());
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("ConfigurationHandler.getServicesFolder() = " + ConfigurationHandler.getServicesFolder());
    }
  }

  /**
   * @param pReloadCmd
   *          the reloadCmd to set
   */
  public void setReloadCmd(final String pReloadCmd)
  {
    ConfigurationHandler.setReloadCmd(pReloadCmd);
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("webserverReloadCmd = " + ConfigurationHandler.getReloadCmd());
    }
  }
}
