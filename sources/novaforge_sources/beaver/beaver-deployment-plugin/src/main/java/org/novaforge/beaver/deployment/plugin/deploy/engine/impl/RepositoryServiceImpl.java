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
package org.novaforge.beaver.deployment.plugin.deploy.engine.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.novaforge.beaver.deployment.plugin.deploy.engine.RepositoryService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.rpm.RpmFacade;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Guillaume Lamirand
 */
public class RepositoryServiceImpl implements RepositoryService
{

  private final boolean rpmRepositoryEnable;

  /**
   * @param pNoRPM
   */
  public RepositoryServiceImpl(final boolean pNoRPM)
  {
    rpmRepositoryEnable = pNoRPM;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void installRPMs(final String pArchive, final String... pRpms) throws BeaverException
  {
    if (isRpmRepositoryEnable())
    {
      BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getFilelogger().info(
          String.format("Install RPMs from archive given with [archive=%s, rpms=%s]", pArchive,
              Arrays.toString(pRpms)));
      Path tmpRepo = null;
      Path yumConfFile = null;
      try
      {
        RpmFacade.backupYUMConf();
        tmpRepo = Files.createTempDirectory(BeaverServices.getTempDirectory(), "_yum");
        yumConfFile = RpmFacade.createRepository(tmpRepo, Paths.get(pArchive));
        RpmFacade.yumInstall(pRpms);
      }
      catch (final Exception e)
      {
        throw new BeaverException("Unable to create a temporary directory needed for createrepo command", e);
      }
      finally
      {
        if (tmpRepo != null)
        {
          try
          {
            Files.deleteIfExists(tmpRepo);
          }
          catch (final IOException e)
          {
            // Ignore this
          }
        }
        if (yumConfFile != null)
        {
          try
          {
            Files.deleteIfExists(yumConfFile);
          }
          catch (final IOException e)
          {
            // Ignore this
          }
        }
        RpmFacade.restaureYUMConf();
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeRPMs(final String... pRpms) throws BeaverException
  {
    if (isRpmRepositoryEnable())
    {
      BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getFilelogger().info(String.format("Remove RPMs with [rpms=%s]", Arrays.toString(pRpms)));
      RpmFacade.yumRemove(pRpms);
    }
  }

  /**
   * @return the rpmRepositoryEnable
   */
  public boolean isRpmRepositoryEnable()
  {
    return rpmRepositoryEnable;
  }
}
