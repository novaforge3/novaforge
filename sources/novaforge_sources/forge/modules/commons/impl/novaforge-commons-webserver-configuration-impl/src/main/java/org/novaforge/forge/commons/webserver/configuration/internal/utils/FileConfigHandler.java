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
package org.novaforge.forge.commons.webserver.configuration.internal.utils;

import org.novaforge.forge.commons.webserver.configuration.exceptions.WebServerConfigurationException;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerConfiguration;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerProxyConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

/**
 * @author Guillaume Lamirand
 */
public class FileConfigHandler
{

  public static boolean deleteValidUserFile(final WebServerConfiguration pConf)
  {
    final Path toDelete = FileNameHandler.getValidUserFileName(ConfigurationHandler.getServicesFolder(),
        pConf);
    return deleteSilent(toDelete);
  }

  private static boolean deleteSilent(final Path pFilePath)
  {
    boolean returnValue = true;
    try
    {
      if (Files.isRegularFile(pFilePath))
      {
        returnValue = Files.deleteIfExists(pFilePath);
      }
    }
    catch (final Exception e)
    {
      // Nothing to do we want silent delete
      returnValue = false;
    }
    return returnValue;
  }

  public static boolean writeConfigurationToFile(final Path pConfigurationPath, final String pConfToWrite)
      throws WebServerConfigurationException
  {
    try
    {
      Files.write(pConfigurationPath, pConfToWrite.getBytes(), StandardOpenOption.CREATE);
    }
    catch (final IOException e)
    {
      throw new WebServerConfigurationException("Can't write configuration file.", e);
    }

    return true;
  }

  public static boolean appendConfigurationToFile(final Path pConfigurationPath, final String pConfToWrite)
      throws WebServerConfigurationException
  {
    try
    {
      Files.write(pConfigurationPath, pConfToWrite.getBytes(), StandardOpenOption.APPEND,
          StandardOpenOption.CREATE);
    }
    catch (final IOException e)
    {
      throw new WebServerConfigurationException("Can't append configuration file.", e);
    }
    return true;
  }

  public static boolean backupFile(final WebServerConfiguration pConf, final Path pSource)
      throws WebServerConfigurationException
  {
    boolean returnValue = false;
    try
    {
      final Path backupFile = FileNameHandler.getBackupConfigFileName(
          ConfigurationHandler.getServicesFolder(), pConf);
      if (!Files.exists(backupFile))
      {
        if ((Files.exists(pSource)) && (Files.isRegularFile(pSource)))
        {
          Files.copy(pSource, backupFile, StandardCopyOption.REPLACE_EXISTING);
        }
        returnValue = true;
      }
    }
    catch (final Exception e)
    {
      throw new WebServerConfigurationException("Can't backup configuration file", e);
    }
    return returnValue;
  }

  /**
   * @param pConf
   * @param pTargetFile
   * @throws WebServerConfigurationException
   */
  public static void restoreBackup(final WebServerProxyConfiguration pConf, final Path pTargetFile)
      throws WebServerConfigurationException
  {
    try
    {
      final Path backupFile = FileNameHandler.getBackupConfigFileName(
          ConfigurationHandler.getServicesFolder(), pConf);
      if ((Files.exists(backupFile)) && (Files.isRegularFile(backupFile)))
      {
        Files.move(backupFile, pTargetFile, StandardCopyOption.REPLACE_EXISTING);
      }
    }
    catch (final IOException e)
    {
      throw new WebServerConfigurationException("Can't restore backup file", e);
    }

  }

  public static boolean saveConfigFile(final WebServerConfiguration pConf)
      throws WebServerConfigurationException
  {
    return saveConfigFile(pConf, false);
  }

  public static boolean saveConfigFile(final WebServerConfiguration pConf, final boolean pCopy)
      throws WebServerConfigurationException
  {
    boolean returnValue = false;
    try
    {
      final Path confFile = FileNameHandler
          .getConfigFileName(ConfigurationHandler.getServicesFolder(), pConf);
      if ((Files.exists(confFile)) && (Files.isRegularFile(confFile)))
      {
        final Path savFile = FileNameHandler.getSavedConfigFileName(ConfigurationHandler.getServicesFolder(),
            pConf);
        if (!Files.exists(savFile))
        {
          if (pCopy)
          {
            Files.copy(confFile, savFile);
          }
          else
          {
            Files.move(confFile, savFile);
          }
          returnValue = true;
        }
      }
    }
    catch (final Exception e)
    {
      throw new WebServerConfigurationException("Can't save configuration file", e);
    }
    return returnValue;
  }

  public static boolean restoreConfigFile(final WebServerConfiguration pConf)
      throws WebServerConfigurationException
  {
    try
    {
      final Path savFile = FileNameHandler.getSavedConfigFileName(ConfigurationHandler.getServicesFolder(),
          pConf);
      if ((Files.exists(savFile)) && (Files.isRegularFile(savFile)))
      {
        final Path confFile = FileNameHandler.getConfigFileName(ConfigurationHandler.getServicesFolder(),
            pConf);
        Files.move(savFile, confFile, StandardCopyOption.REPLACE_EXISTING);
      }
    }
    catch (final IOException e)
    {
      throw new WebServerConfigurationException("Can't restore configuration file", e);
    }
    return true;
  }

  public static void deleteBackupFile(final WebServerConfiguration pConf)
      throws WebServerConfigurationException
  {
    final Path backupFile = FileNameHandler.getBackupConfigFileName(ConfigurationHandler.getServicesFolder(),
        pConf);
    deleteSilent(backupFile);
  }

  public static boolean deleteLines(final Path pConfigurationPath, final String pStart, final String pEnd)
      throws WebServerConfigurationException
  {
    final File file = pConfigurationPath.toFile();
    Path tmpPath;
    try
    {
      tmpPath = Files.createTempFile("proxy_", null);
    }
    catch (final IOException e)
    {
      throw new WebServerConfigurationException("Can't create tmp file", e);
    }
    final File tmpFile = tmpPath.toFile();
    try (BufferedWriter buffTmp = new BufferedWriter(new FileWriter(tmpFile, true)))
    {

      final Scanner scanner = new Scanner(file);
      boolean isIn = false;
      while (scanner.hasNext())
      {
        final String currentLine = scanner.nextLine();
        if (currentLine.contains(pStart))
        {
          isIn = true;
        }
        if (!isIn)
        {
          buffTmp.write(currentLine);
          buffTmp.newLine();
        }
        if (currentLine.contains(pEnd))
        {
          isIn = false;
        }
      }
      buffTmp.flush();
      buffTmp.close();
      scanner.close();

      Files.move(tmpPath, pConfigurationPath, StandardCopyOption.REPLACE_EXISTING);
    }
    catch (final Exception e)
    {
      throw new WebServerConfigurationException("Can't delete configuration from file", e);
    }
    finally
    {
      deleteSilent(tmpPath);
    }
    return true;
  }

}
