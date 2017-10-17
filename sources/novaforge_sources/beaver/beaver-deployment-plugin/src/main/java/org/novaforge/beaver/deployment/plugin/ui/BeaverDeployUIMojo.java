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
package org.novaforge.beaver.deployment.plugin.ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.novaforge.beaver.deployment.plugin.deploy.AbstractBeaverMojo;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.resources.ResourceImpl;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.deployment.plugin.ui.model.LauncherProperty;
import org.novaforge.beaver.deployment.plugin.ui.model.ListProductProperties;
import org.novaforge.beaver.deployment.plugin.ui.model.ProductProperties;
import org.novaforge.beaver.exception.BeaverException;
import org.novaforge.beaver.resource.Resource;

/**
 * Class which allow to edit and generate launcher of deployment process
 * 
 * @author Guillaume Lamirand
 * @version 3.0
 * @since 0.1
 */
@Mojo(name = "edit")
public class BeaverDeployUIMojo extends AbstractBeaverMojo
{
  /**
   * Default location used to store log files
   */
  @Parameter(property = "logsDirectory", defaultValue = "${project.build.directory}/logs")
  private String                      logsDirectory;

  private Model                       projectModel;

  private final ListProductProperties listProductProperties = new ListProductProperties();

  private final Scanner               systemInScanner       = new Scanner(System.in);
  private final static String         SEPARATOR             = "------------------------------------------------------------------";

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    try
    {
      BeaverLogger.setLogger(logsDirectory, getLog());

      getLog().info("*******************************************************************");
      getLog().info("Starting the UI of deployment");
      getLog().info(SEPARATOR);
      projectModel = getProject().getMavenProject().getOriginalModel();
      displayMenu();
      getLog().info("*******************************************************************");
    }
    catch (final BeaverException e)
    {
      throw new MojoExecutionException("Error when executing UI MOJO.", e);
    }

  }

  private void displayMenu() throws BeaverException
  {
    getLog().info(SEPARATOR);
    getLog().info("Menu");
    getLog().info(SEPARATOR);
    int selection = 0;
    boolean done = false;

    while (done == false)
    {
      getLog().info("Please make a selection:");
      getLog().info("");
      getLog().info("  [1] Wizard to edit properties");
      getLog().info("  [2] Display properties for deployment");
      getLog().info("  [3] Write modifications");
      getLog().info("  [4] Exit");
      getLog().info("");
      getLog().info("Selection:");

      if (systemInScanner.hasNextInt())
      {
        selection = systemInScanner.nextInt();
      }
      else
      {
        systemInScanner.next();
      }
      switch (selection)
      {
        case 1:
          wizardMenu();
          wizardEditMenu();
          backMenu();
          break;

        case 2:
          propertiesMenu();
          buildProductProperties();
          listProductProperties.display();
          backMenu();
          break;

        case 3:
          writeMenu();
          buildProductProperties();
          listProductProperties.display();
          writeNewPom();
          break;

        case 4:
          getLog().info("Exit Successful");
          done = true;
          break;

        default:
          defaultMenu();
          break;
      }

    }
  }

  private void wizardMenu()
  {
    getLog().info(SEPARATOR);
    getLog().info("Wizard to edit properties");
    getLog().info(SEPARATOR);

  }

  private void propertiesMenu()
  {
    getLog().info(SEPARATOR);
    getLog().info("Display properties for deployment");
    getLog().info(SEPARATOR);
  }

  private void writeMenu()
  {
    getLog().info(SEPARATOR);
    getLog().info("Write the new pom file edited");
    getLog().info(SEPARATOR);
  }

  private void backMenu()
  {
    getLog().info(SEPARATOR);
    getLog().info("Back to the main menu");
    getLog().info(SEPARATOR);

  }

  private void defaultMenu()
  {
    getLog().info(SEPARATOR);
    getLog().info("Please enter a valid selection.");
    getLog().info(SEPARATOR);

  }

  public void buildProductProperties() throws BeaverException
  {
    final Properties properties = projectModel.getProperties();
    final Iterator<Object> iProperties = properties.keySet().iterator();

    while (iProperties.hasNext())
    {
      final String key = (String) iProperties.next();
      final String value = properties.getProperty(key);
      final Resource currentProperty = new ResourceImpl(key);
      if ((currentProperty.isCorrectFormat()) && (currentProperty.isCurrentProductRelated() == false))
      {
        final String productId = currentProperty.getProductId();
        if (listProductProperties.productExists(productId) == true)
        {
          final ProductProperties productPropertie = listProductProperties.getProductProperties(productId);
          if (productPropertie.propertyExists(key))
          {
            productPropertie.getLauncherProperty(key).setValue(currentProperty.getKey());
          }
          else
          {
            final LauncherProperty property = new LauncherProperty(currentProperty.getKey(), value);
            productPropertie.getLauncherList().add(property);
          }
        }
        else
        {
          final ProductProperties productProperty = new ProductProperties(productId);
          final LauncherProperty property = new LauncherProperty(currentProperty.getKey(), value);
          productProperty.getLauncherList().add(property);
          listProductProperties.getListProductProperties().add(productProperty);
        }
      }
    }

  }

  private void wizardEditMenu() throws BeaverException
  {
    boolean back = false;
    final Properties properties = projectModel.getProperties();
    final Iterator<Object> iProperties = properties.keySet().iterator();

    getLog().info("Write the new value, 's' to skip the property or 'b' to back to the main menu.");
    while ((back == false) && (iProperties.hasNext()))
    {
      final String key = (String) iProperties.next();
      final String value = properties.getProperty(key);
      getLog().info("");
      getLog().info("  - " + key + " : " + value);
      final String newValue = systemInScanner.next();
      if (("s".equals(newValue) == false) && ("b".equals(newValue) == false))
      {
        getLog().info("Saving " + key + " : " + newValue);
        properties.setProperty(key, newValue);

      }
      else if ("b".equals(newValue) == true)
      {
        back = true;
      }
    }
  }

  private void writeNewPom() throws BeaverException
  {
    getLog().info("Write a name for your new pom file or 'c' to cancel: ");
    final String filename = systemInScanner.next();
    if ("c".equals(filename) == false)
    {
      final File file = new File(filename);
      if (file.exists() == true)
      {
        getLog().info("The file " + file.getName() + " exits already, do you want to overwrite it ? (y/n)");
        final Scanner overwrite = new Scanner(System.in);
        if ("y".equals(overwrite.next()) == true)
        {
          write(file);
        }
      }
      else
      {
        write(file);
      }
    }
  }

  private void write(final File pFile) throws BeaverException
  {
    getLog().info("The file " + pFile.getName() + " will be writed.");
    try
    {
      final FileWriter fileWriter = new FileWriter(pFile);
      new MavenXpp3Writer().write(fileWriter, projectModel);
    }
    catch (final IOException e)
    {
      throw new BeaverException("Error to write XML file on the system.", e);
    }
  }
}
