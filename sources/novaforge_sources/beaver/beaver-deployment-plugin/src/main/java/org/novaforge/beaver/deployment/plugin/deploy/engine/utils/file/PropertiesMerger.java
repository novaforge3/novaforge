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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Aimen Merkich
 */
public class PropertiesMerger
{
  private final DateFormat   svgFormat = new SimpleDateFormat("yyyyMMdd");
  private final File         newFile;
  private final File         oldFile;
  private final List<String> exclusionList;

  /**
   * @param pNewFile
   *          must exist
   * @param pOldFile
   *          must exist
   * @param pExclusionList
   *          list of exclusion key
   * @throws IOException
   * @throws BeaverException
   */
  public PropertiesMerger(final String pOldFile, final String pNewFile, final List<String> pExclusionList)
      throws BeaverException
  {
    newFile = new File(pNewFile);
    oldFile = new File(pOldFile);
    exclusionList = pExclusionList;
    if (!newFile.exists() || !newFile.canRead())
    {
      throw new BeaverException("newFile: '" + newFile + "' doesn't exist or could not be read");
    }
    else if (!oldFile.exists() || !oldFile.canRead())
    {
      throw new BeaverException("oldFile: '" + oldFile + "' doesn't exist or could not be read");
    }
    else
    {
      try
      {
        merge();
      }
      catch (final IOException e)
      {
        throw new BeaverException("Merge can't be done" + e);
      }
    }
  }

  /**
   * Load the newFile and oldFile properties files and compare their properties
   * and merge them
   * 
   * @throws IOException
   */

  private void merge() throws IOException
  {
    final Properties newFileProps = loadProperties(newFile);
    final Properties oldFileProps = loadProperties(oldFile);
    final Map<String, String> newMap = getMap(newFile);
    final Map<String, String> oldMap = getMap(oldFile);
    final Enumeration<?> enumerator = oldFileProps.propertyNames();
    for (; enumerator.hasMoreElements();)
    {
      final String key = (String) enumerator.nextElement();
      final String file2Value = oldFileProps.getProperty(key);
      final String file1Value = newFileProps.getProperty(key);
      if (((file1Value != null) && (file2Value != null) && !file2Value.equalsIgnoreCase(file1Value))
          && (((exclusionList == null) || exclusionList.isEmpty() || !exclusionList.contains(key))))
      {

        final String pattern = key + "=" + newMap.get(key);
        final String replace = "\n#modify (" + svgFormat.format(new Date()) + ") : '" + key + "="
            + newMap.get(key) + "'\n" + key + "=" + oldMap.get(key);

        execute(newFile, pattern, replace);

      }
    }
  }

  /**
   * Load Properties of file
   * 
   * @param pFile
   * @throws IOException
   */
  private Properties loadProperties(final File pFile) throws IOException
  {
    // load file properties
    final Properties properties = new Properties();
    try (final InputStream inputStream = new FileInputStream(pFile))
    {
      properties.load(inputStream);
      closeInputStream(inputStream);
    }
    return properties;
  }

  /**
   * Search and replace
   * 
   * @param pSrcFile
   * @param pSearch
   * @param pReplace
   */
  private void execute(final File pSrcFile, final String pSearch, final String pReplace)
  {
    // search and replace in srcFile
    InputStream inputStream = null;
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;

    OutputStream outputStream = null;
    OutputStreamWriter outputStreamWriter = null;
    BufferedWriter bufferedWriter = null;

    try
    {
      // create tmp file
      final File tmpFile = new File(pSrcFile.getParent() + File.separator + "searchNreplace.tmp");
      if (tmpFile.exists())
      {
        tmpFile.delete();
      }
      else if (!tmpFile.createNewFile())
      {
        throw new IOException("could not create the tmp file: " + tmpFile);
      }

      outputStream = new FileOutputStream(tmpFile, false);
      outputStreamWriter = new OutputStreamWriter(outputStream);
      bufferedWriter = new BufferedWriter(outputStreamWriter);

      inputStream = new FileInputStream(pSrcFile);
      inputStreamReader = new InputStreamReader(inputStream);
      bufferedReader = new BufferedReader(inputStreamReader);
      // start search and replace
      String line = bufferedReader.readLine();
      while (line != null)
      {
        if (!line.startsWith("#") && !line.startsWith("!") && !"".equals(line.trim()) && line.contains("="))
        {
          String[] keyValue = null;
          if (line.indexOf("=") > 0)
          {
            keyValue = line.split("=", 2);
          }
          if ((keyValue != null) && !keyValue[0].trim().isEmpty() && !keyValue[1].trim().isEmpty())
          {
            if ((keyValue[0].trim() + "=" + keyValue[1].trim()).equals(pSearch))
            {
              bufferedWriter.write(pReplace + "\n");
            }
            else
            {
              bufferedWriter.write(line + "\n");
            }
          }

        }
        else
        {
          bufferedWriter.write(line + "\n");
        }
        line = bufferedReader.readLine();
      }
      // close all streams
      closeBufferedReaderStream(bufferedReader);
      closeInputStreamReader(inputStreamReader);
      closeInputStream(inputStream);

      closeBufferedWriterStream(bufferedWriter);
      closeOutputStreamWriter(outputStreamWriter);
      closeOutputStream(outputStream);

      // rename tmpFile
      if (!pSrcFile.delete())
      {
        throw new IOException("could not delete " + pSrcFile);
      }
      if (!tmpFile.renameTo(pSrcFile))
      {
        throw new IOException("could not rename " + tmpFile + " to " + pSrcFile);
      }

    }
    catch (final IOException ioe)
    {
      closeInputStream(inputStream);
      closeInputStreamReader(inputStreamReader);
      closeBufferedReaderStream(bufferedReader);

      closeOutputStream(outputStream);
      closeOutputStreamWriter(outputStreamWriter);
      closeBufferedWriterStream(bufferedWriter);
    }
  }

  /**
   * This method construct hashmap of .properties file
   * 
   * @param pFile
   * @throws IOException
   */
  private Map<String, String> getMap(final File pFile) throws IOException
  {
    final Map<String, String> map = new HashMap<String, String>();
    final InputStream inputStream = new FileInputStream(pFile);
    final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    try
    {

      String line = bufferedReader.readLine();
      while (line != null)
      {
        if (!line.startsWith("#") && !line.startsWith("!") && !"".equals(line.trim()) && line.contains("="))
        {
          String[] keyValue = null;
          if (line.indexOf("=") > 0)
          {
            keyValue = line.split("=", 2);
          }
          if ((keyValue != null) && !keyValue[0].trim().isEmpty() && !keyValue[1].trim().isEmpty())
          {
            map.put(keyValue[0].trim(), keyValue[1].trim());
          }

        }
        line = bufferedReader.readLine();
      }
      closeInputStream(inputStream);
      closeInputStreamReader(inputStreamReader);
      closeBufferedReaderStream(bufferedReader);

    }
    catch (final IOException ioe)
    {
      closeInputStream(inputStream);
      closeInputStreamReader(inputStreamReader);
      closeBufferedReaderStream(bufferedReader);
    }
    return map;

  }

  /**
   * closeInputStream
   * 
   * @param pInnputStream
   */
  public static void closeInputStream(final InputStream pInnputStream)
  {
    if (pInnputStream != null)
    {
      try
      {
        pInnputStream.close();
      }
      catch (final IOException ioe)
      {
        // nothing todo
      }
    }
  }

  /**
   * closeBufferedReaderStream
   * 
   * @param pBufferedReadern
   */
  public static void closeBufferedReaderStream(final BufferedReader pBufferedReader)
  {
    if (pBufferedReader != null)
    {
      try
      {
        pBufferedReader.close();
      }
      catch (final IOException ioe)
      {
        // nothing todo
      }
    }
  }

  /**
   * closeInputStreamReader
   * 
   * @param pInputStreamReader
   */
  public static void closeInputStreamReader(final InputStreamReader pInputStreamReader)
  {
    if (pInputStreamReader != null)
    {
      try
      {
        pInputStreamReader.close();
      }
      catch (final IOException ioe)
      {
        // nothing todo
      }
    }
  }

  /**
   * closeOutputStream
   * 
   * @param pOutputStream
   */
  public static void closeOutputStream(final OutputStream pOutputStream)
  {
    if (pOutputStream != null)
    {
      try
      {
        pOutputStream.flush();
        pOutputStream.close();
      }
      catch (final IOException ioe)
      {
        // nothing todo
      }
    }
  }

  /**
   * closeOutputStreamWriter
   * 
   * @param pOutputStreamWriter
   */
  public static void closeOutputStreamWriter(final OutputStreamWriter pOutputStreamWriter)
  {
    if (pOutputStreamWriter != null)
    {
      try
      {
        pOutputStreamWriter.flush();
        pOutputStreamWriter.close();
      }
      catch (final IOException ioe)
      {
        // nothing todo
      }
    }
  }

  /**
   * closeBufferedWriterStream
   * 
   * @param pBufferedWriter
   */
  public static void closeBufferedWriterStream(final BufferedWriter pBufferedWriter)
  {
    if (pBufferedWriter != null)
    {
      try
      {
        pBufferedWriter.flush();
        pBufferedWriter.close();
      }
      catch (final IOException ioe)
      {
        // nothing todo
      }
    }
  }

}
