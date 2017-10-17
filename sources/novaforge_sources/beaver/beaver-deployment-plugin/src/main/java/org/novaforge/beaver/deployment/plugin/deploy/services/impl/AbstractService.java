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
package org.novaforge.beaver.deployment.plugin.deploy.services.impl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.maven.artifact.Artifact;
import org.codehaus.groovy.control.CompilationFailedException;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.packaging.PackageBeaverMojo;
import org.novaforge.beaver.exception.BeaverException;

public class AbstractService
{

  private final static String DATA_FILE        = "dataFile";
  private final static String PROCESS_TMP_PATH = "processTmpPath";
  private final static String ENGINE           = "engine";

  /**
   * Execute a groovy script.
   * 
   * @param pDataDir
   *          represents the path where the data artifact is located.
   * @param pProcessDir
   *          represents the path where the process was unpacked.
   * @param pScript
   *          represents the path of the script
   * @throws BeaverException
   */
  protected void executeGroovy(final Path pDataDir, final Path pProcessDir, final Path pScript)
      throws BeaverException
  {
    final GroovyShell shell = new GroovyShell();
    Script groovyscript;
    try
    {
      groovyscript = shell.parse(pScript.toFile());
      final Binding binding = new Binding();
      if (pDataDir != null)
      {
        binding.setVariable(DATA_FILE, pDataDir.toString());
      }
      binding.setVariable(PROCESS_TMP_PATH, pProcessDir.toString());
      binding.setProperty(ENGINE, BeaverServices.getEngine());
      groovyscript.setBinding(binding);
      groovyscript.run();
    }
    catch (final CompilationFailedException e)
    {
      throw new BeaverException(e.toString(), e);
    }
    catch (final IOException e)
    {
      throw new BeaverException(e.toString(), e);
    }
  }

  protected void prepareProcess(final Artifact currentArtifact, final Path descriptorDir)
      throws BeaverException
  {
    final Path beaverProcess = Paths.get(currentArtifact.getFile().getAbsolutePath());
    final Path descriptorArchive = descriptorDir.resolve(currentArtifact.getArtifactId() + "."
        + PackageBeaverMojo.ARCHIVE_EXTENSTION);
    try
    {
      Files.createDirectories(descriptorArchive.getParent());
      Files.copy(beaverProcess, descriptorArchive, StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.COPY_ATTRIBUTES);
    }
    catch (final IOException e)
    {
      throw new BeaverException(String.format(
          "Unable to create archive from beaver-process artifact [%s] to [%s].", beaverProcess,
          descriptorArchive), e);
    }
    BeaverServices.getEngine().unpackFile(descriptorArchive, descriptorDir);

  }
}
