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
package org.novaforge.forge.ui.projects.internal.client.manage.presenter.components;

import com.vaadin.ui.Upload.Receiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * This {@link Receiver} will store the project image into temp directory.
 * 
 * @author Guillaume Lamirand
 */
public class ImageReceiver implements Receiver
{

  /**
   * Serial id
   */
  private static final long serialVersionUID = 5546555766505254429L;
  /**
   * Parent directory file
   */
  private final File        parent;
  /**
   * Output file
   */
  private File              file;

  /**
   * Default constructor.
   * 
   * @param pTmpDir
   *          the tmp directory to store image
   */
  public ImageReceiver(final File pTmpDir)
  {
    super();
    parent = pTmpDir;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OutputStream receiveUpload(final String pFilename, final String pMimeType)
  {
    // Create upload stream
    FileOutputStream fos = null; // Output stream to write to
    try
    {
      // Open the file for writing.
      file = new File(parent, pFilename);
      fos = new FileOutputStream(file);
    }
    catch (final java.io.FileNotFoundException e)
    {
      // Nothing to do in this case
    }
    return fos; // Return the output stream to write to
  }

}
