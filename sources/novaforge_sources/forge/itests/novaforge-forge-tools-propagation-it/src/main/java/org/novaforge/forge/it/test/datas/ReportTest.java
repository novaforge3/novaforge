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
package org.novaforge.forge.it.test.datas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReportTest
{
   public static void writeTestResult(String testName, String method, boolean result)
   {
      try
      {
         StringBuffer str = new StringBuffer();
         File file = new File(TestConstants.RESULT_FILENANE);
         boolean lineFound = false;
         if (file.exists())
         {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
               if (line.startsWith(String.format("%s; %s", testName, method)))
               {
                  str = str.append(String.format("%s; %s: %s", testName, method, Boolean.toString(result)));
                  lineFound = true;
               }
               else
               {
                  str = str.append(line);
               }
               str = str.append("\n");
            }
            reader.close();
         }
         if (!lineFound)
         {
            str = str.append(String.format("%s; %s: %s\n", testName, method, Boolean.toString(result)));
         }

         FileWriter writer = new FileWriter(TestConstants.RESULT_FILENANE);
         writer.write(str.toString());
         writer.close();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   public static void newTestResult()
   {
      File file = new File(TestConstants.RESULT_FILENANE);
      if (!file.exists())
      {
         if (!file.getParentFile().exists())
         {
            file.getParentFile().mkdirs();
         }
      }
      else
      {
         file.delete();
      }

   }
}
