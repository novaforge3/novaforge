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
package org.novaforge.beaver.deployment.plugin.deploy.engine.utils.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Gauthier Cart
 */
public class OwnerDirVisitorWindows extends SimpleFileVisitor<Path>
{
  /** Contains the owner user */
  private final String  user;
  /** Throw an exception on error, else hide the error */
  private final boolean throwsOnError;

  /**
   * Default constructor
   *
   * @param pUser
   *          the user to set, can be <code>null</code>
   * @param pThrowsOnError
   *          throw an exception on error, else hide the error
   */
  public OwnerDirVisitorWindows(final String pUser, final boolean pThrowsOnError)
  {
    user = pUser;
    throwsOnError = pThrowsOnError;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FileVisitResult visitFile(final Path pFilePath, final BasicFileAttributes pAttrs) throws IOException
  {
    FilesFacade.setOwnerAttribute(user, pFilePath, throwsOnError);
    return FileVisitResult.CONTINUE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FileVisitResult preVisitDirectory(final Path pDirPAth, final BasicFileAttributes pAttrs)
      throws IOException
  {
    FilesFacade.setOwnerAttribute(user, pDirPAth, throwsOnError);
    return FileVisitResult.CONTINUE;
  }

}