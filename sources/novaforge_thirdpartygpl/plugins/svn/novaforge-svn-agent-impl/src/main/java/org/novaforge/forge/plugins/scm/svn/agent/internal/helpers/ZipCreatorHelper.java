/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.internal.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author sbenoist
 *
 */
public final class ZipCreatorHelper
{
   public static void createZipFile(String pDirectoryName, String pZipFileName) throws IOException
   {
      ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(pZipFileName));
      zipOutputStream.setLevel(2);

      zipDir(pDirectoryName, zipOutputStream, new String());
      zipOutputStream.close();
   }

   private static void zipDir(String dir2zip, ZipOutputStream zos, String pInnerArchivePath)
         throws IOException
   {
      File zipDir = new File(dir2zip);

      String[] dirList = zipDir.list();
      byte[] readBuffer = new byte[2156];
      int bytesIn = 0;

      for (String element : dirList)
      {
         File f = new File(zipDir, element);
         String newInnerPath = pInnerArchivePath + File.separatorChar + f.getName();
         if (f.isDirectory())
         {
            String filePath = f.getPath();
            zipDir(filePath, zos, newInnerPath);
         }
         else
         {
            FileInputStream fis = new FileInputStream(f);

            ZipEntry anEntry = new ZipEntry(newInnerPath);

            zos.putNextEntry(anEntry);

            while ((bytesIn = fis.read(readBuffer)) != -1)
            {
               zos.write(readBuffer, 0, bytesIn);
            }

            fis.close();
         }
      }
   }
}
