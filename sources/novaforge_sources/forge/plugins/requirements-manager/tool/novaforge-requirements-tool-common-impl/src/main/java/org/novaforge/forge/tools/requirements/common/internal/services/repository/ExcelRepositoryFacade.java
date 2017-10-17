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
package org.novaforge.forge.tools.requirements.common.internal.services.repository;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

/**
 * @author Guillaume Lamirand
 */
public class ExcelRepositoryFacade
{

  private static final Log              LOGGER               = LogFactory.getLog(ExcelRepositoryFacade.class);

  private static final String           REPOSITORY_DIRECTORY = "repository";
  private static final String           TMP                  = "tmp";
  private static final SimpleDateFormat tempFolderFormater   = new SimpleDateFormat("yyyyMMdd-HHmmss");

  public static void delete(final String pExcelURI)
  {
    final Path path = Paths.get(pExcelURI).getParent();
    try
    {
      if (Files.isDirectory(path))
      {
        Files.walkFileTree(path, new DeleteDirVisitor());
      }
      else
      {
        Files.deleteIfExists(path);
      }
    }
    catch (final IOException e)
    {
      LOGGER.error(
          String.format("Unable to delete excel file associated to the repository [uri=%s]", pExcelURI), e);
      // We want to delete directory silently

    }
  }

  public static Path prepareExcelParent(final String pDataPath, final String pProjectId,
      final String pRepositoryId) throws RequirementManagerServiceException
  {
    final StringBuilder excelParent = new StringBuilder();
    if (StringUtils.isNotBlank(pDataPath))
    {
      excelParent.append(pDataPath);
      if (!pDataPath.endsWith(File.separator))
      {
        excelParent.append(File.separator);
      }
    }
    else
    {
      // In case of missing prefix for destination folder
      excelParent.append("/tmp");
    }
    excelParent.append(pProjectId).append(File.separator).append(REPOSITORY_DIRECTORY).append(File.separator)
        .append(pRepositoryId).append(File.separator);

    // Creating, if needed, the destination folder
    final Path parentPath = Paths.get(excelParent.toString());
    if (!Files.exists(parentPath))
    {
      try
      {
        Files.createDirectories(parentPath);
      }
      catch (final IOException e)
      {
        throw new RequirementManagerServiceException(String.format(
            "Unable to create parent directory for repository with [project=%s, repository_id=%s",
            pProjectId, pRepositoryId), e);
      }
    }
    return parentPath;
  }

  public static Path copyToTmp(final String pExcelURI) throws RequirementManagerServiceException
  {
    Path tmpExcelPath;
    final Path path = Paths.get(pExcelURI);
    try
    {
      final Path tmpDir = Files.createTempDirectory(path.getParent(), null);
      tmpDir.toFile().deleteOnExit();
      tmpExcelPath = tmpDir.resolve(path.getFileName());
      Files.copy(path, tmpExcelPath, StandardCopyOption.COPY_ATTRIBUTES);
    }
    catch (final IOException e)
    {
      throw new RequirementManagerServiceException(String.format(
          "Unable to create tmp directory for repository with [repository_uri=%s", pExcelURI), e);
    }
    return tmpExcelPath;
  }
}
