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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.beaver.deployment.plugin.deploy.engine.SSLService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.file.DeleteDirVisitor;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SSLServiceImplTest {

  private static final String TARGE_NAME = "test";
  private final static String TARGET_DIR = "target/test/ssl/";
  final Path keyFile = Paths.get(TARGET_DIR, TARGE_NAME + ".key");
  final Path csrFile = Paths.get(TARGET_DIR, TARGE_NAME + ".crt");
  final Path p12File = Paths.get(TARGET_DIR, TARGE_NAME + ".p12");
  final Path jksFile = Paths.get(TARGET_DIR, TARGE_NAME + ".jsk");
  private static SSLService sslService;

  @Before
  public void init() throws BeaverException, IOException {

    sslService = new SSLServiceImpl();
    BeaverLogger.setLogger("target/log", new SystemStreamLog());

    final Path target = Paths.get(TARGET_DIR);
    if (Files.exists(target)) {
      Files.walkFileTree(target, new DeleteDirVisitor());
    }
    Files.createDirectories(target);
  }

  @Test
  public void test001GenerateKeyAndCSR() throws Exception {

    sslService.generateKeyAndCSR(keyFile.toString(), csrFile.toString(), 365, 2048, "FR", "Echirolles", "Bull", "BIS",
            "localhost", "email");
  }

  @Test
  public void test002BuildPKCS12() throws Exception {
    sslService.generateKeyAndCSR(keyFile.toString(), csrFile.toString(), 365, 2048, "FR", "Echirolles", "Bull", "BIS",
            "localhost", "email");

    sslService.buildPKCS12(p12File.toString(), keyFile.toString(), csrFile.toString(), "localhost", "test", "test");
  }

  @Test
  public void test003ImportPKCS12toJKS() throws Exception {
    sslService.generateKeyAndCSR(keyFile.toString(), csrFile.toString(), 365, 2048, "FR", "Echirolles", "Bull", "BIS",
            "localhost", "email");

    sslService.buildPKCS12(p12File.toString(), keyFile.toString(), csrFile.toString(), "localhost", "test", "test");

    sslService.importPKCS12toJKS(p12File.toString(), "test", "localhost", jksFile.toString(), "testtest", 2048);
  }

  @Test
  public void test004ImportJKStoJKS() throws Exception {
    sslService.generateKeyAndCSR(keyFile.toString(), csrFile.toString(), 365, 2048, "FR", "Echirolles", "Bull", "BIS",
            "localhost", "email");

    sslService.buildPKCS12(p12File.toString(), keyFile.toString(), csrFile.toString(), "localhost", "test", "test");

    sslService.importPKCS12toJKS(p12File.toString(), "test", "localhost", jksFile.toString(), "testtest", 2048);

    sslService.importJKStoJKS("src/test/ressources/ssl/cacerts", "changeit", jksFile.toString(), "testtest");

  }
}
