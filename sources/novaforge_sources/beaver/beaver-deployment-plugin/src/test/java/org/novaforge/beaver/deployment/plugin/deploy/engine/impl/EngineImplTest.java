/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * This file is free software: you may redistribute and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.beaver.deployment.plugin.deploy.engine.impl;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.codehaus.plexus.util.Os;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.Scanner;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EngineImplTest {
  private final static File targetDir = new File("target/test/engine/");
  private final static File testZip = new File(targetDir + "/test/");
  private final static File testAddLine = new File(targetDir + "/addLine");
  private final static File testFilter = new File(targetDir + "/replaceFilter");
  private final static File testConcat1 = new File(targetDir + "/concat1");
  private final static File testConcat2 = new File(targetDir + "/concat2");
  private final static File testFileSH = new File(targetDir + "/testcopy.sh");
  private final static File testCopyFile = new File(targetDir + "/testcopyfile/");
  private final static File testSetOwner = new File(targetDir + "/testsetowner/");
  private final static File testMoveFile = new File(targetDir + "/testmovefile");
  private final static File testMovedFile = new File(targetDir + "/testmovedfile");
  private final static File testMoveDirToMove = new File(targetDir + "/testmovedirtomove/");
  private final static File testMoveDirMoved = new File(targetDir + "/testmovedirmoved/");
  private final static File testSetPermissionOnFile = new File(targetDir + "/setpermissiononfile");
  private final static String testtoken = "@testtoken@";
  private final static String USER = System.getProperty("user.name");
  private final static String USERDOMAIN = System.getenv("userdomain");
  private final static String GROUP = "users";
  private final static String MYSQL_BIN = "mysql";
  private final static String MYSQL_PORT = "3306";
  private final static String MYSQL_USER = "root";
  private final static String MYSQL_PASS = "root";
  private final static String MYSQL_BASE = "beavertest";
  private final static File testExecuteMysqlScript = new File(targetDir + "/executeMysqlScript.sql");

  private static EngineImpl tools;

  private boolean mysqlProfileActivated = false;
  private boolean ownerProfileActivated = false;

  public EngineImplTest() {
     String property = System.getProperty("mysql.profile");
    if ("true".equals(property)) {
      mysqlProfileActivated = true;
    }

    property = System.getProperty("owner.profile");
    if ("true".equals(property)) {
      ownerProfileActivated = true;
    }
  }


  @Before
  public void init() throws BeaverException, IOException {
    tools = new EngineImpl();
    BeaverLogger.setLogger("target/test/log", new SystemStreamLog());

    tools.delete(targetDir.getAbsolutePath());
    final File testResource = new File("src/test/ressources/");
    tools.copy(testResource.getAbsolutePath(), targetDir.getAbsolutePath());

    assertTrue("targetDir is unexisting", targetDir.exists());
    assertTrue("targetDir is not a directory", targetDir.isDirectory());
    assertNotNull("targetDir is empty", targetDir.list());
  }

  @Test
  public void test001unpackFile() throws BeaverException {
    final Path zipFile = Paths.get(targetDir + "/test.zip");
    tools.unpackFile(zipFile, testZip.toPath());
    assertTrue(testAddLine.isFile());
    assertTrue(testFilter.isFile());
    assertTrue(testConcat1.isFile());
    assertTrue(testConcat2.isFile());

  }

  @Test
  public void test002addPropertyFile() throws BeaverException {
    final File properties = new File(targetDir + "/test.properties");
    assertTrue(properties.isFile());
    tools.addPropertyFile(properties.getAbsolutePath(), "key", "insert_to_be_tested");
    String value = "";
    Scanner scanner;
    try {
      scanner = new Scanner(properties);
      while (scanner.hasNextLine()) {
        value = scanner.nextLine();
      }

      scanner.close();
      assertEquals("key=insert_to_be_tested", value);
    } catch (final FileNotFoundException e) {
      System.out.println(e.getMessage());
    }

  }

  @Test
  public void test003addLineToFile() throws BeaverException, FileNotFoundException {
    assertEquals("existing_file", true, testAddLine.isFile());
    tools.addLineToFile(testAddLine.getAbsolutePath(), "AddLineToken", "line insert into it");
    String value = "";
    final Scanner scanner = new Scanner(testAddLine);
    while (scanner.hasNextLine()) {
      value = scanner.nextLine();
    }

    scanner.close();
    assertEquals("addLineToFile", "line insert into it", value);

  }

  @Test
  public void test004concatFiles() throws BeaverException, FileNotFoundException {
    assertTrue("concat1Exist", testConcat1.isFile());
    assertTrue("concat2Exist", testConcat2.isFile());

    tools.concatFiles(testConcat1.getAbsolutePath(), testConcat2.getAbsolutePath());

    int indice = 0;
    final String value[] = new String[4];
    final Scanner scanner = new Scanner(testConcat2);
    while (scanner.hasNextLine()) {
      value[indice] = scanner.nextLine();
      indice++;
    }
    scanner.close();
    assertEquals("Concat2", "concat2", value[0]);
    assertEquals("Concat1", "concat1", value[1]);
  }

  @Test
  public void test005copy() throws BeaverException {
    final File testCopyDir = new File(targetDir + "/testdesdir/");
    final File testSrcDir = new File(targetDir + "/testsrcdir/");
    final boolean succeed = testSrcDir.mkdirs();
    assertTrue("testSrcDir is unexisting", succeed);

    tools.copy(testSrcDir.getAbsolutePath(), testCopyDir.getAbsolutePath());

    assertTrue("testCopyDir is unexisting", testCopyDir.exists());
    assertTrue("testCopyDir is not a directory", testCopyDir.isDirectory());
    assertNotNull("testCopyDir is empty", testCopyDir.list());
  }

  @Test
  public void test006copyFile() throws BeaverException {
    tools.copy(testFilter.getAbsolutePath(), testCopyFile.getAbsolutePath());
    final File filecopy = new File(testCopyFile + File.separator + testFilter.getName());
    assertTrue("filecopy is unexisting", filecopy.exists());
    assertTrue("filecopy is not a file", filecopy.isFile());
  }

  @Test
  public void test007copyToFile() throws BeaverException {
    tools.copyToFile(testFilter.getAbsolutePath(), testFileSH.getAbsolutePath());
    assertTrue("Exists", testFileSH.exists());
    assertTrue("isFile", testFileSH.isFile());
  }

  @Test
  public void test008createDirectory() throws BeaverException {
    final File createDir = new File(targetDir + "/testcreate/");
    tools.createDirectory(createDir.getPath());
    assertTrue("createDir is unexisting", createDir.exists());
    assertTrue("createDir is not a directory", createDir.isDirectory());
  }

  @Test
  public void test009delete() throws BeaverException {
    final File createDir = new File(targetDir + "/testcreate/");
    BeaverServices.init(null, null, false, false, null);
    tools.delete(createDir.getAbsolutePath());
    assertFalse("createDir is existing", createDir.exists());

    final File filecopy = new File(testCopyFile + File.separator + testFilter.getName());
    tools.delete(filecopy.getAbsolutePath());
    assertFalse("Create", filecopy.exists());
  }

  @Test
  public void test011isLinux() throws BeaverException {
    BeaverServices.init(null, null, false, false, null);
    BeaverServices.getLauncherService().init(null);
    assertNotNull("isLinux", tools.isLinux());
    if (Os.isFamily(Os.FAMILY_UNIX)) {
      assertTrue("isLinux", tools.isLinux());
    } else {
      assertFalse("isLinux", tools.isLinux());
    }
  }

  @Test
  public void test012isWindows() throws BeaverException {
    BeaverServices.init(null, null, false, false, null);
    BeaverServices.getLauncherService().init(null);
    assertNotNull("isWindows", tools.isWindows());
    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
      assertTrue("isWindows", tools.isWindows());
    } else {
      assertFalse("isWindows", tools.isWindows());
    }
  }

  @Test
  public void test013replaceElementFilter() throws BeaverException, FileNotFoundException {
    tools.copyToFile(testFilter.getAbsolutePath(), testFileSH.getAbsolutePath());
    assertTrue("Exists", testFileSH.exists());
    assertTrue("isFile", testFileSH.isFile());

    tools.replaceElementFilter(targetDir.getAbsolutePath(), testtoken, "valueoftest", "*.sh", "");
    String value = "";
    final Scanner scanner = new Scanner(testFileSH);
    while (scanner.hasNextLine()) {
      value = scanner.nextLine();
    }

    scanner.close();
    assertEquals("valueoftest", value);

  }

  @Test
  public void test014replaceElement() throws BeaverException, FileNotFoundException {
    tools.replaceElement(testFilter.toString(), testtoken, "valueoftest");
    String value = "";
    final Scanner scanner = new Scanner(testFilter);
    while (scanner.hasNextLine()) {
      final String line = scanner.nextLine();
      value = line;
    }

    scanner.close();
    assertEquals("valueoftest", value);

  }

  @Test
  public void test015createNewFile() throws BeaverException {
    final File newFile = new File(targetDir + "/testFile/");
    tools.createNewFile(newFile.getPath());
    assertTrue("newFile is unexisting", newFile.exists());
    assertTrue("newFile is not a file", newFile.isFile());
  }

  @Test
  public void test016setOwner() throws BeaverException, IOException {

    if (ownerProfileActivated == true) {
      BeaverServices.init(null, null, false, false, null);
      BeaverServices.getLauncherService().init(null);
      final Path directory = Paths.get(testSetOwner.getAbsolutePath());
      Files.createDirectory(directory);
      tools.setOwner(true, GROUP, USER, testSetOwner.getAbsolutePath(), true);

      if (Os.isFamily(Os.FAMILY_UNIX)) {
        PosixFileAttributes attributes = Files.readAttributes(directory, PosixFileAttributes.class);
        assertEquals("hasOwner", USER, attributes.owner().getName());
        assertEquals("hasGroup", GROUP, attributes.group().getName());
      } else {
        FileOwnerAttributeView attributes = Files.getFileAttributeView(directory, FileOwnerAttributeView.class);
        assertEquals("hasOwner", USERDOMAIN + "\\" + USER, attributes.getOwner().getName());
      }
    }
  }

  @Test
  public void test017move() throws BeaverException, IOException {
    BeaverServices.init(null, null, false, false, null);
    BeaverServices.getLauncherService().init(null);

    final Path fileToMove = Paths.get(testMoveFile.getAbsolutePath());
    Files.createFile(fileToMove);

    tools.move(testMoveFile.getAbsolutePath(), testMovedFile.getAbsolutePath());

    assertTrue("Exists", testMovedFile.exists());
  }

  @Test
  public void test018moveDir() throws BeaverException, IOException {
    BeaverServices.init(null, null, false, false, null);
    BeaverServices.getLauncherService().init(null);

    final Path directoryToMove = Paths.get(testMoveDirToMove.getAbsolutePath());
    Files.createDirectory(directoryToMove);

    tools.moveDir(testMoveDirToMove.getAbsolutePath(), testMoveDirMoved.getAbsolutePath());

    assertTrue("Exists", testMoveDirMoved.exists());
  }

  @Test
  public void test019setPermissionsOnFiles() throws BeaverException, IOException {

    // TODO Linux
  }

  @Test
  public void test020executeMysqlScript() throws BeaverException, IOException {

    if (mysqlProfileActivated == true) {
      BeaverServices.init(null, null, false, false, null);
      BeaverServices.getLauncherService().init(null);

      tools.executeMysqlScript(MYSQL_BIN, MYSQL_PORT, MYSQL_USER, MYSQL_PASS, testExecuteMysqlScript.getAbsolutePath());

      // Check database exist
      final StringBuffer command = new StringBuffer();
      command.append(MYSQL_BIN);
      command.append(" --port=" + MYSQL_PORT);
      command.append(" --user=" + MYSQL_USER);
      command.append(" --password=" + MYSQL_PASS);
      command.append(" --skip-column-names");
      command.append(" -e");
      command.append(" \"SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + MYSQL_BASE + "'\"");

      Process process = Runtime.getRuntime().exec(command.toString());
      BufferedReader reader = new BufferedReader (new InputStreamReader(process.getInputStream()));
      assertEquals("Exists", MYSQL_BASE,reader.readLine () );

      // Drop database if exists.
      command.setLength(0);
      command.append(MYSQL_BIN);
      command.append(" --port=" + MYSQL_PORT);
      command.append(" --user=" + MYSQL_USER);
      command.append(" --password=" + MYSQL_PASS);
      command.append(" -e");
      command.append(" \"DROP DATABASE IF EXISTS " + MYSQL_BASE + "\"");
      Runtime.getRuntime().exec(command.toString());
    }
  }
}


