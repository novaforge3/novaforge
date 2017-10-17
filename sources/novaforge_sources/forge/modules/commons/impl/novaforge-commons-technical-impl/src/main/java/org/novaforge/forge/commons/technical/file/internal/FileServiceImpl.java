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
package org.novaforge.forge.commons.technical.file.internal;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.novaforge.forge.commons.technical.file.FileMeta;
import org.novaforge.forge.commons.technical.file.FileService;
import org.novaforge.forge.commons.technical.file.FileServiceException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author germain-c
 */
public class FileServiceImpl implements FileService
{

  /**
   * {@inheritDoc}
   */
  @Override
  public void storeFile(final FileItem pFileItem, final File pFileDest) throws FileServiceException
  {
    createParentDirectories(pFileDest);
    try
    {
      if (pFileDest.createNewFile())
      {
        pFileItem.write(pFileDest);
      }
      else
      {
        throw new FileServiceException(String.format(
            "Unable to create file because it is already existing [path=%s]", pFileDest));
      }
    }
    catch (final IOException e)
    {
      throw new FileServiceException(String.format("Unable to write file with [path=%s]",
          pFileDest.getAbsolutePath()), e);
    }
    catch (final Exception e)
    {
      throw new FileServiceException(String.format("Unable to create file [path=%s]", pFileDest), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void storeFile(final File pFileSrc, final File pFileDest) throws FileServiceException
  {
    createParentDirectories(pFileDest);
    try
    {
      FileUtils.copyFile(pFileSrc, pFileDest);
    }
    catch (final IOException e)
    {
      throw new FileServiceException(String.format("Unable to copy file with [source=%s, target=%s]", pFileSrc,
                                                   pFileDest), e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void storeStream(final InputStream pInputStream, final File pFileDest) throws FileServiceException
  {
    try
    {
      FileUtils.copyInputStreamToFile(pInputStream, pFileDest);
    }
    catch (final IOException e)
    {
      throw new FileServiceException(String.format("Unable to write the target file with [target=%s]",
          pFileDest), e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void renameFile(final File pFileSrc, final File pFileDest) throws FileServiceException
  {
    createParentDirectories(pFileDest);
    try
    {
      FileUtils.moveFile(pFileSrc, pFileDest);
    }
    catch (final IOException e)
    {
      throw new FileServiceException(String.format(
          "Unable to rename source file with [source=%s, target=%s]", pFileSrc, pFileDest), e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteFile(final File pFileSrc) throws FileServiceException
  {
    if (pFileSrc.isFile())
    {
      final boolean isSucceed = pFileSrc.delete();
      if (!isSucceed)
      {
        throw new FileServiceException(String.format("Unable to delete the file element given with [file=%s]",
                                                     pFileSrc));
      }
    }
    else if (pFileSrc.isDirectory())
    {
      try
      {
        FileUtils.deleteDirectory(pFileSrc);
      }
      catch (final IOException e)
      {
        throw new FileServiceException(String.format("Unable to delete the directory element given with [directory=%s]",
                                                     pFileSrc));
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getAllFiles(final String directory) throws FileServiceException
  {
    final File dirFile = new File(directory);
    final List<File> returnedList = new ArrayList<File>();
    if (dirFile.isDirectory())
    {
      Collections.addAll(returnedList, dirFile.listFiles());
    }
    else
    {
      throw new FileServiceException(String.format("the file [%s] is not a directory", directory));
    }
    return returnedList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FileMeta downloadFile(final String pUrl) throws FileServiceException
  {
    final FileMetaImpl returned = new FileMetaImpl();
    // Create an instance of HttpClient.
    final HttpClient client = new HttpClient();

    // Create a method instance.
    final GetMethod method = new GetMethod(pUrl);
    final HostConfiguration hostConfiguration = new HostConfiguration();
    if (System.getProperty("http.proxyHost") != null)
    {
      hostConfiguration.setProxy(System.getProperty("http.proxyHost"),
          Integer.parseInt(System.getProperty("http.proxyPort", "80")));
      client.setHostConfiguration(hostConfiguration);
    }
    // Provide custom retry handler is necessary
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
        new DefaultHttpMethodRetryHandler(3, false));

    try
    {
      // Execute the method.
      final int statusCode = client.executeMethod(method);

      if (statusCode != HttpStatus.SC_OK)
      {
        throw new FileServiceException(String.format(
            "Unable to download external file with [url=%s,status_response=%s]", pUrl, statusCode));
      }
      // Copy content to temporary file
      final File tmpFile = File.createTempFile("commonFile", "nfg");
      tmpFile.deleteOnExit();
      storeStream(method.getResponseBodyAsStream(), tmpFile);
      returned.setFile(tmpFile);
      returned.setType(method.getResponseHeader("Content-Type").getValue());
      returned.setSize(String.valueOf(method.getResponseContentLength()));
      returned.setUrl(pUrl);

    }
    catch (final HttpException e)
    {
      throw new FileServiceException(String.format("Unable to download external file with [url=%s]", pUrl), e);
    }
    catch (final IOException e)
    {
      throw new FileServiceException(String.format("Unable to write external file downloaded with [url=%s]",
          pUrl), e);
    }
    finally
    {
      // Release the connection.
      method.releaseConnection();
    }
    return returned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existFile(final File f)
  {
    return f.exists();
  }

  /**
   * Will create the parent directories
   *
   * @param fileDest
   */
  private void createParentDirectories(final File fileDest)
  {
    final File dirParent = fileDest.getParentFile();
    if (!dirParent.exists())
    {
      fileDest.getParentFile().mkdirs();
    }
  }
}