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
package org.novaforge.beaver.deployment.plugin.validate;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.novaforge.beaver.deployment.plugin.deploy.AbstractBeaverMojo;
import org.novaforge.beaver.deployment.plugin.deploy.constant.BeaverMavenProperty;
import org.novaforge.beaver.deployment.plugin.deploy.model.BeaverMavenProject;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * This MOJO is used to validate beaver process content
 * 
 * @author Guillaume Lamirand
 * @version 3.0
 * @since 3.0
 */
@Mojo(name = "validate")
@Execute(lifecycle = "beaver-package-lifecycle")
public class ValidateBeaverMojo extends AbstractBeaverMojo
{
  /**
   * Represents the current Maven project
   */
  @Parameter(property = "project.build.directory", readonly = true)
  private File outputPath;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws MojoExecutionException
  {
    // Defined BeaverLogger info
    try
    {
      BeaverLogger.setLogger(outputPath + "/beaver-log", getLog());
    }
    catch (final BeaverException e)
    {
      getLog().warn("BeaverLogger cannot be configured.");
    }
    // Need to handle requisite
    BeaverServices.init(this, "", false, false, null);
    final BeaverMavenProject beaverMavenProject = getBeaverMavenProject();
    final String projectPackaging = beaverMavenProject.getMavenProject().getPackaging();

    // ignore unsupported project types
    if ((BeaverMavenProperty.PROCESS_PACKAGING.getPropertyKey().equals(projectPackaging) == false)
        && (BeaverMavenProperty.SERVER_PACKAGING.getPropertyKey().equals(projectPackaging) == false))
    {
      getLog().warn(
          String.format("Project type [%s] invalid, only [%s, %s] supported.", projectPackaging,
              BeaverMavenProperty.PROCESS_PACKAGING.getPropertyKey(),
              BeaverMavenProperty.SERVER_PACKAGING.getPropertyKey()));
    }
    else if ((BeaverMavenProperty.PROCESS_PACKAGING.getPropertyKey().equals(projectPackaging))
        && (beaverMavenProject.hasProcessInfo() == false))
    {
      throw new MojoExecutionException(
          String
              .format(
                  "Some of required properties for a beaver-process are missing, check pom and defined the following : artifact-version, %s, %s, %s.",
                  BeaverMavenProperty.PROCESS.getPropertyKey(),
                  BeaverMavenProperty.PRODUCT_ID.getPropertyKey(),
                  BeaverMavenProperty.SCRIPT.getPropertyKey()));

    }
    else if ((BeaverMavenProperty.SERVER_PACKAGING.getPropertyKey().equals(projectPackaging))
        && (beaverMavenProject.hasServerInfo() == false))
    {
      throw new MojoExecutionException(
          String
              .format(
                  "Some of required properties for a beaver-server are missing, check pom and defined the following : %s.",
                  BeaverMavenProperty.SERVER_ID.getPropertyKey()));

    }
  }

  private BeaverMavenProject getBeaverMavenProject() throws MojoExecutionException
  {
    try
    {
      return getProject();
    }
    catch (final BeaverException e)
    {
      throw new MojoExecutionException("Unable to build BeaverMavenProject from the current project");
    }
  }

}
