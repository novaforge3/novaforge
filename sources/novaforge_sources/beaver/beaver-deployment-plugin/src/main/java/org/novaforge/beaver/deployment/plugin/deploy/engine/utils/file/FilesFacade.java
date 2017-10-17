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
package org.novaforge.beaver.deployment.plugin.deploy.engine.utils.file;

import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;

/**
 * @author Guillaume Lamirand
 */
public class FilesFacade
{

  private FilesFacade()
  {
    // Utility class should have private explicit constructor ( Sonar rule : Hide Utility Class Constructor )
  }

  /**
   * This method sets owner attribute to the Path given
   *
   * @param pUser
   *          user to set
   * @param pPath
   *          path to search into
   * @param throwsOnError
   *          throw an exception on error, else hide the error
   * @throws IOException
   */
  public static void setOwnerAttribute(final String pUser, final Path pPath,
      final boolean throwsOnError) throws IOException
  {
    final FileOwnerAttributeView view = Files.getFileAttributeView(pPath, FileOwnerAttributeView.class);

    try
    {
      if (BeaverLogger.getFilelogger().isDebugEnabled())
      {
        final UserPrincipal oldUserPrincipal = view.getOwner();
        BeaverLogger.getFilelogger().debug(
            String.format("Current path element [%s]", pPath.toAbsolutePath().toString()));
        BeaverLogger.getFilelogger().debug(
            String.format("Old owner was [%s]", oldUserPrincipal.getName()));
      }
      final UserPrincipalLookupService lookupService = FileSystems.getDefault()
          .getUserPrincipalLookupService();
      if (StringUtils.isNotBlank(pUser))
      {
        final UserPrincipal userPrincipal = lookupService.lookupPrincipalByName(pUser);
        view.setOwner(userPrincipal);
      }
    }
    catch (final IOException e)
    {
      if (throwsOnError)
      {
        throw new IOException(e);
      }
      else
      {
        if ((e != null) && (e.getCause() != null))
        {
          BeaverLogger.getFilelogger().error(
              String.format("Fail on error hidden by parameter. Cause was [%s]", e.getCause().toString()));
        }
        else
        {
          BeaverLogger.getFilelogger().error(String.format("Fail on error hidden by parameter."));
        }

      }
    }
  }

  /**
   * This method sets posix attribute to the Path given
   *
   * @param pGroup
   *          group to set
   * @param pUser
   *          user to set
   * @param path
   *          path to search into
   * @param throwsOnError
   *          throw an exception on error, else hide the error
   * @throws IOException
   */
  public static void setPosixAttribute(final String pGroup, final String pUser, final Path path,
                                       final boolean throwsOnError) throws IOException
  {
    final PosixFileAttributeView view = Files.getFileAttributeView(path, PosixFileAttributeView.class);

    try
    {
      if (BeaverLogger.getFilelogger().isDebugEnabled())
      {
        final PosixFileAttributes attributes = view.readAttributes();
        BeaverLogger.getFilelogger().debug(
                String.format("Current path element [%s]", path.toAbsolutePath().toString()));
        BeaverLogger.getFilelogger().debug(
                String.format("Old owner was [%s] and group [%s]", attributes.owner().getName(), attributes
                        .group().getName()));
      }
      final UserPrincipalLookupService lookupService = FileSystems.getDefault()
              .getUserPrincipalLookupService();
      if (StringUtils.isNotBlank(pUser))
      {
        final UserPrincipal userPrincipal = lookupService.lookupPrincipalByName(pUser);
        view.setOwner(userPrincipal);
      }
      if (StringUtils.isNotBlank(pGroup))
      {
        final GroupPrincipal groupPrincipal = lookupService.lookupPrincipalByGroupName(pGroup);
        view.setGroup(groupPrincipal);
      }
    }
    catch (final IOException e)
    {
      if (throwsOnError)
      {
        throw new IOException(e);
      }
      else
      {
        if ((e != null) && (e.getCause() != null))
        {
          BeaverLogger.getFilelogger().error(
                  String.format("Fail on error hidden by parameter. Cause was [%s]", e.getCause().toString()));
        }
        else
        {
          BeaverLogger.getFilelogger().error(String.format("Fail on error hidden by parameter."));
        }

      }
    }
  }

  public static void deleteSilent(final File pFile)
  {
    deleteSilent(pFile.getAbsolutePath());
  }

  public static void deleteSilent(final String pFileAbsolutePath)
  {

    final Path path = Paths.get(pFileAbsolutePath);
    deleteSilent(path);
  }

  public static void deleteSilent(final Path pFilePath)
  {
    try
    {
      Files.deleteIfExists(pFilePath);
    }
    catch (final IOException e)
    {
      // Nothing to do we want silent delete
    }
  }

}
