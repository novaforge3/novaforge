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
package org.novaforge.forge.tools.deliverymanager.internal.services;

import org.apache.commons.io.FileUtils;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryRepositoryService;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Guillaume Lamirand
 */
public class DeliveryRepositoryServiceImpl implements DeliveryRepositoryService
{

  private static final String NOTE_TEMPLATE             = "note_template";
  private static final String UNDERSCORE                = "_";
  private static final String OUT_FOLDER                = "OUT";
  private static final String TEMP_FOLDER               = "tmp";
  private final String        TEMPLATE_DEFAULT_FILENAME = "nf_delivery_note.rtpdesign";
  /**
   * Repository path
   */
  private String rootRepository = "/datas/novaforge3/data/delivery";

  private static void closeQuietly(final Closeable pStream)
  {
    if (pStream != null)
    {
      try
      {
        pStream.close();
      }
      catch (final IOException ioe)
      {
        // Silent catch
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createProjectDirectory(final String pProjectId) throws DeliveryServiceException
  {

    // getting project path directory
    final String projectRootRepository = getProjectPath(pProjectId);
    createDirectory(projectRootRepository);

    // getting template path directory
    final String templateRepository = getTemplateDirectory(pProjectId);
    createDirectory(templateRepository);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTemplateDirectory(final String pProjectId)
  {

    return getProjectPath(pProjectId) + File.separatorChar + NOTE_TEMPLATE;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createDeliveryDirectories(final String pProjectId, final String pReference)
      throws DeliveryServiceException
  {

    // create delivery repository
    final String deliveryRootDirectory = getDeliveryPath(pProjectId, pReference);
    createDirectory(deliveryRootDirectory);

    // create temporary directory
    final String deliveryTmpDirectory = getDeliveryTempDirectory(pProjectId, pReference);
    createDirectory(deliveryTmpDirectory);

    // create final delivery repository
    final String deliveryOutRepository = getDeliveryOutDirectory(pProjectId, pReference);
    createDirectory(deliveryOutRepository);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createArchive(final String pProjectId, final String pReference) throws DeliveryServiceException
  {
    ZipOutputStream zos                 = null;
    final String emptyPath = "";
    final String    deliveryZipFileName = getDeliveryArchivePath(pProjectId, pReference);
    try
    {
      zos = new ZipOutputStream(new FileOutputStream(deliveryZipFileName));
      createZipFromDirectory(getDeliveryTempDirectory(pProjectId, pReference), emptyPath, zos);
    }
    catch (final IOException ioe)
    {
      throw new DeliveryServiceException(ioe);
    }
    finally
    {
      closeQuietly(zos);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDeliveryArchivePath(final String pProjectId, final String pReference)
      throws DeliveryServiceException
  {

    return getDeliveryOutDirectory(pProjectId, pReference) + File.separatorChar + getDeliveryArchiveFileName(pProjectId,
                                                                                                             pReference);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDeliveryTemporaryPath(final String pProjectId, final String pDeliveryId)
      throws DeliveryServiceException
  {
    final String deliveryTarFileName =
        rootRepository + File.separatorChar + pProjectId + File.separatorChar + pDeliveryId + File.separatorChar
            + TEMP_FOLDER;
    final boolean exists = (new File(deliveryTarFileName)).exists();
    if (!exists)
    {
      throw new DeliveryServiceException(String.format("The temporary folder doesn't exists with [project=%s, delivery=%s]",
                                                       pProjectId, pDeliveryId));
    }

    return deliveryTarFileName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProjectDirectory(final String pProjectId)
  {

    return getProjectPath(pProjectId);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void emptyDeliveryTempContentDirectory(final String pProjectId, final String pReferenceId,
      final String pContentPath) throws DeliveryServiceException
  {
    final String directoryPath = getDeliveryTempDirectory(pProjectId, pReferenceId) + File.separatorChar + pContentPath;
    final File contentDirectory = new File(directoryPath);
    if (contentDirectory.exists())
    {
      FileUtils.deleteQuietly(contentDirectory);
    }
    createDirectory(directoryPath);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteDeliveryTempContentDirectory(final String pProjectId, final String pReferenceId,
      final String pContentPath) throws DeliveryServiceException
  {
    final String directoryPath = getDeliveryTempDirectory(pProjectId, pReferenceId) + File.separatorChar + pContentPath;
    final File contentDirectory = new File(directoryPath);
    if (contentDirectory.exists())
    {
      FileUtils.deleteQuietly(contentDirectory);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createDirectoryInDeliveryContent(final String pProjectId, final String pReferenceId,
      final String pDirectoryPath) throws DeliveryServiceException
  {
    final String directoryPath = getDeliveryTempDirectory(pProjectId, pReferenceId) + File.separatorChar
                                     + pDirectoryPath;
    createDirectory(directoryPath);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDeliveryArchiveFileName(final String pProjectId, final String pReference)
      throws DeliveryServiceException
  {

    return pProjectId + UNDERSCORE + pReference + ".zip";
  }

  @Override
  public String getDeliveryTemplateSamplePath() throws DeliveryServiceException
  {

    return rootRepository + File.separatorChar;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTemplateDefaultFilename()
  {
    return TEMPLATE_DEFAULT_FILENAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createDefaultTemplateFile(final String pProjectId) throws DeliveryServiceException
  {
    // getting template path directory
    final String templateRepository = getTemplateDirectory(pProjectId);
    final String defaultTemplatePath = templateRepository + File.separatorChar + TEMPLATE_DEFAULT_FILENAME;
    final InputStream is              = this.getClass().getResourceAsStream("/" + TEMPLATE_DEFAULT_FILENAME);
    final File        defaultTemplate = new File(defaultTemplatePath);
    OutputStream      out;
    try
    {
      if (is != null)
      {
        out = new FileOutputStream(defaultTemplate);
        final byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1)
        {
          out.write(buffer, 0, len);
        }
        out.close();
        is.close();
      }
      else
      {
        throw new DeliveryServiceException(String.format("Can't find default report template file with [name=%s]",
                                                         TEMPLATE_DEFAULT_FILENAME));
      }
    }
    catch (final FileNotFoundException e)
    {
      throw new DeliveryServiceException(e);
    }
    catch (final IOException e)
    {
      throw new DeliveryServiceException(e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateDeliveryReferenceDirectoryName(final String pProjectId, final String pReference,
                                                   final String pNewReference) throws DeliveryServiceException
  {
    final File deliveryDirectory    = new File(getDeliveryPath(pProjectId, pReference));
    final File deliveryNewDirectory = new File(getDeliveryPath(pProjectId, pNewReference));
    if (!deliveryDirectory.exists())
    {
      throw new DeliveryServiceException(String.format("Can't find delivery directory with [name=%s]",
                                                       deliveryDirectory.getPath()));
    }
    else if (deliveryNewDirectory.exists())
    {
      throw new DeliveryServiceException(String
                                             .format("Can't rename delivery directory with [name=%s] to new directory [name=%s], it's already exist.",
                                                     deliveryDirectory.getPath(), deliveryNewDirectory.getPath()));
    }
    else
    {
      deliveryDirectory.renameTo(deliveryNewDirectory);
    }
  }

  private String getDeliveryTempDirectory(final String pProjectId, final String pReferenceId)
  {
    return getDeliveryPath(pProjectId, pReferenceId) + File.separatorChar + TEMP_FOLDER;
  }

  private String getDeliveryPath(final String pProjectId, final String pReferenceId)
  {

    return getProjectPath(pProjectId) + File.separatorChar + pReferenceId;
  }

  private String getProjectPath(final String pProjectId)
  {
    return rootRepository + File.separatorChar + pProjectId;
  }

  private void createDirectory(final String pPath) throws DeliveryServiceException
  {
    final boolean exists = (new File(pPath)).exists();
    if (!exists)
    {
      // Create directories
      final boolean success = (new File(pPath)).mkdirs();
      if (!success)
      {
        throw new DeliveryServiceException(String.format("Unable to create directory with [path=%s]", pPath));
      }
    }
  }

  /**
   * Compress directory pDirectory in pZipOutputStream
   *
   * @param pDirectory
   * @param pZipOutputStream
   * @throws IOException
   */
  private void createZipFromDirectory(final String pDirectory, final String pInnerArchivePath,
      final ZipOutputStream pZipOutputStream) throws IOException
  {
    final File directoryToZip = new File(pDirectory);
    final String[] dirList = directoryToZip.list();
    final byte[] readBuffer = new byte[2156];
    int bytesIn;
    for (final String element : dirList)
    {
      final File f = new File(directoryToZip, element);
      if (f.isDirectory())
      {

        final String filePath = f.getPath();
        String newPath = "";

        if (pInnerArchivePath.isEmpty())
        {
          newPath = f.getName();
        }
        else
        {
          newPath = pInnerArchivePath + File.separatorChar + f.getName();
        }

        createZipFromDirectory(filePath, newPath, pZipOutputStream);
        continue;
      }

      final FileInputStream fis = new FileInputStream(f);
      final String zipEntryPath = pInnerArchivePath + File.separatorChar + f.getName();
      final ZipEntry anEntry = new ZipEntry(zipEntryPath);
      pZipOutputStream.putNextEntry(anEntry);

      while ((bytesIn = fis.read(readBuffer)) != -1)
      {
        pZipOutputStream.write(readBuffer, 0, bytesIn);
      }

      fis.close();
    }
  }

  private String getDeliveryOutDirectory(final String pProjectId, final String pReferenceId)
  {
    return getDeliveryPath(pProjectId, pReferenceId) + File.separatorChar + OUT_FOLDER;
  }

  /**
   * @param pRootRepository
   *          the rootrepository to set
   */
  public void setRootRepository(final String pRootRepository)
  {
    rootRepository = pRootRepository;
  }

}
