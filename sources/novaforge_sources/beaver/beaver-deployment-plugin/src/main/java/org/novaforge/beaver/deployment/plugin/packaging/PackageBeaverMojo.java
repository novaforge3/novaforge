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
package org.novaforge.beaver.deployment.plugin.packaging;

import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.novaforge.beaver.deployment.plugin.deploy.constant.BeaverMavenProperty;
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;
import org.novaforge.beaver.deployment.plugin.deploy.engine.impl.EngineImpl;
import org.novaforge.beaver.exception.BeaverException;

/**
 * This MOJO is used to package as tar.gz the beaver process or beaver server
 * 
 * @author Guillaume Lamirand
 * @version 3.0
 * @since 3.0
 */
@Mojo(name = "package")
@Execute(lifecycle = "beaver-package-lifecycle")
public class PackageBeaverMojo extends AbstractMojo
{

  /**
   * Represents the current Maven project
   */
  @Parameter(property = "project", readonly = true)
  private MavenProject           project;

  /**
   * Used to overide default type
   */
  @Component()
  private ArtifactHandlerManager artifactHandlerManager;
  /**
   * Default location used for groovy files
   */
  @Parameter(property = "groovyDirectory", defaultValue = "${basedir}/src/main/groovy")
  private String                 groovyDirectory;
  /**
   * Location of resources files
   */
  @Parameter(property = "resourcesTarget", defaultValue = "${project.build.directory}/classes")
  private String                 resourcesDirectory;

  /**
   * Define the name of resources directory in process
   */
  @Parameter(property = "resourcesParent", defaultValue = "resources")
  private String                 resourcesParent;

  public static final String     ARCHIVE_EXTENSTION = "tar.gz";
  private static final String    GROOVY_EXTENSION   = ".groovy";

  private BeaverEngine           beaverEngine;

  /**
   * Target path used to prepare beaver process
   */
  private Path                   beaverTarget;

  /**
   * Contains the type of current Maven Project
   */
  private String                 projectType;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws MojoExecutionException
  {
    projectType = project.getArtifact().getType();

    // ignore unsupported project types
    if ((BeaverMavenProperty.PROCESS_PACKAGING.getPropertyKey().equals(projectType) == false)
        && (BeaverMavenProperty.SERVER_PACKAGING.getPropertyKey().equals(projectType) == false))
    {
      getLog().warn(
          String.format("Project type %s invalid, only [%s, %s] supported.", projectType,
              BeaverMavenProperty.PROCESS_PACKAGING.getPropertyKey(),
              BeaverMavenProperty.SERVER_PACKAGING.getPropertyKey()));
    }
    else
    {
      beaverEngine = new EngineImpl();
      try
      {
        createTarget();
        copyResources();
        copyGroovy();
        processPackage();
      }
      catch (final BeaverException e)
      {
        throw new MojoExecutionException(
            "Unable to package beaver-process project, see the following error.", e);
      }
    }
  }

  /**
   * Create target directory used to build beaver-process archive
   * 
   * @throws BeaverException
   */
  private void createTarget() throws BeaverException
  {
    beaverTarget = Paths.get(project.getBuild().getDirectory(), projectType);
    beaverEngine.createDirectory(beaverTarget.toString());
  }

  /**
   * Copy resources to package directory
   * 
   * @throws BeaverException
   */
  private void copyResources() throws BeaverException
  {
    final File resourcesDir = new File(resourcesDirectory);
    if (resourcesDir.exists())
    {
      final Path resourcesTarget = beaverTarget.resolve(resourcesParent);
      beaverEngine.copy(resourcesDir.getAbsolutePath(), resourcesTarget.toString());
    }

  }

  /**
   * This methid browses groovy directoy and copy all groovy files to package directory
   * 
   * @throws BeaverException
   */
  private void copyGroovy() throws BeaverException
  {
    final File groovyDir = new File(groovyDirectory);
    if (groovyDir.exists())
    {
      final Path groovyPath = Paths.get(groovyDirectory);
      try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(groovyPath))
      {
        if (directoryStream != null)
        {
          for (final Path path : directoryStream)
          {
            if (path.getFileName().toString().endsWith(GROOVY_EXTENSION))
            {
              try
              {
                new GroovyShell().parse(path.toFile());
              }
              catch (final MultipleCompilationErrorsException e)
              {
                throw new BeaverException(String.format("There are syntaxe errors in groovy file [file=%s]",
                    path), e);
              }
              beaverEngine.copy(path.toString(), beaverTarget.toString());
            }
          }
        }
      }
      catch (final IOException e)
      {
        throw new BeaverException(String.format("Unable to browse groovy directory with [path=%s]",
            groovyPath), e);
      }
    }

  }

  /**
   * Build the process as tar.gz and add file to artifact
   * 
   * @throws BeaverException
   */
  private void processPackage() throws BeaverException
  {
    final String artifactFileName = new StringBuilder(project.getArtifactId()).append(".")
        .append(ARCHIVE_EXTENSTION).toString();
    final File artifact = new File(project.getBuild().getDirectory(), artifactFileName);
    beaverEngine.pack(beaverTarget.toFile(), artifact);
    final Artifact mainArtifact = project.getArtifact();

    mainArtifact.setFile(artifact);
  }
}
