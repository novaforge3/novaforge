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
package org.novaforge.beaver.deployment.plugin.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;
import org.novaforge.beaver.exception.BeaverException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BeaverLoggerTest
{

  public BeaverLoggerTest() throws BeaverException
  {
    final File logDirectory = new File("target/test/log");
    BeaverLogger.setLogger(logDirectory.getAbsolutePath(), new SystemStreamLog());
  }

  @Test
  public void setLogger() throws BeaverException
  {

    final File logDirectory = new File("target/test/log");
    assertTrue(logDirectory.exists());
    assertTrue(logDirectory.isDirectory());

    assertNotNull(BeaverLogger.getDeploymentLog());
    assertTrue(BeaverLogger.getDeploymentLog().exists());
    assertTrue(BeaverLogger.getDeploymentLog().isFile());
    assertTrue(BeaverLogger.getDeploymentLog().getAbsolutePath().startsWith(logDirectory.getAbsolutePath()));

    assertNotNull(BeaverLogger.getSimulationLog());
    assertTrue(BeaverLogger.getSimulationLog().exists());
    assertTrue(BeaverLogger.getSimulationLog().isFile());
    assertTrue(BeaverLogger.getSimulationLog().getAbsolutePath().startsWith(logDirectory.getAbsolutePath()));
  }

  @Test
  public void getFilelogger() throws BeaverException
  {
    assertNotNull(BeaverLogger.getFilelogger());
    BeaverLogger.getFilelogger().info("Test log a message");
    assertNotNull(BeaverLogger.getDeploymentLog());
    assertTrue(BeaverLogger.getDeploymentLog().exists());
    assertTrue(BeaverLogger.getDeploymentLog().isFile());
    String value = "";
    Scanner scanner;
    try
    {
      scanner = new Scanner(BeaverLogger.getDeploymentLog());
      while (scanner.hasNextLine())
      {
        value = scanner.nextLine();
      }

      scanner.close();
      assertTrue("addPropertyFile", value.endsWith("Test log a message"));
    }
    catch (final FileNotFoundException e)
    {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void getOutlogger() throws BeaverException
  {
    assertNotNull(BeaverLogger.getOutlogger());
  }

  @Test
  public void getSimulationlogger() throws BeaverException
  {
    assertNotNull(BeaverLogger.getSimulationlogger());
    BeaverLogger.getSimulationlogger().info("Test log a simulation message");
    assertNotNull(BeaverLogger.getSimulationLog());
    assertTrue(BeaverLogger.getSimulationLog().exists());
    assertTrue(BeaverLogger.getSimulationLog().isFile());
    String value = "";
    Scanner scanner;
    try
    {
      scanner = new Scanner(BeaverLogger.getSimulationLog());
      while (scanner.hasNextLine())
      {
        value = scanner.nextLine();
      }

      scanner.close();
      assertTrue("addPropertyFile", value.endsWith("Test log a simulation message"));
    }
    catch (final FileNotFoundException e)
    {
      System.out.println(e.getMessage());
    }
  }
}