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
package org.novaforge.beaver.deployment.plugin.deploy;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;
import org.novaforge.beaver.deployment.plugin.deploy.engine.impl.EngineImpl;
import org.novaforge.beaver.deployment.plugin.deploy.engine.simulate.EngineSimulateImpl;
import org.novaforge.beaver.deployment.plugin.deploy.model.BeaverMavenProject;
import org.novaforge.beaver.deployment.plugin.deploy.model.ProductProcess;
import org.novaforge.beaver.deployment.plugin.deploy.model.RequisitesBuilder;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * DeployBeaverMojo is a main class of Mojo Beaver Deploy. It will depending on parameter :
 * <ul>
 * <li>Launcher deployment</li>
 * <li>Simulate deployment</li>
 * <li>Configure your deployment</li>
 * <li>resolve the artifacts needed</li>
 * </ul>
 * 
 * @author Guillaume Lamirand
 * @version 3.0
 * @since 0.1
 */
@Mojo(name = "deploy", defaultPhase = LifecyclePhase.INSTALL,
    requiresDependencyResolution = ResolutionScope.TEST)
@Execute(lifecycle = "beaver-deploy-lifecycle")
public class DeployBeaverMojo extends AbstractBeaverMojo
{
  /**
   * Default location used to copy temp files
   */
  @Parameter(property = "tempDirectory", defaultValue = "${project.build.directory}/temp")
  private String            tempDirectory;
  /**
   * Default location used to store log files
   */
  @Parameter(property = "logsDirectory", defaultValue = "${project.build.directory}/logs")
  private String            logsDirectory;
  /**
   * Default location used to store persistence information about current deployment.
   */
  @Parameter(property = "deploymentFile", required = true)
  private String            deploymentFile;
  /**
   * Represents the class which implements DeploymentContext, used to manage persistence file. (ex =
   * org.novaforge.novacic.novadeployimpl.DeploymentPersistenceImpl)
   */
  @Parameter(property = "deploymentImplClass",
      defaultValue = "org.novaforge.beaver.context.impl.DeploymentContextImpl", required = true)
  private String            deploymentImplClass;

  /**
   * Resolved only artifact
   */
  @Parameter(property = "serverId")
  private String           serverId;

  /**
   * Resolved only artifact
   */
  @Parameter(property = "resolved", defaultValue = "false")
  private boolean           resolved;

  /**
   * Ignore prequisite configuration
   */
  @Parameter(property = "ignorePrerequisite", defaultValue = "false")
  private boolean           ignorePrerequisite;
  /**
   * Launcher just simulate mode
   */
  @Parameter(property = "simulate", defaultValue = "false")
  private boolean           simulate;
  /**
   * Launcher just configuration mode
   */
  @Parameter(property = "config", defaultValue = "false")
  private boolean           config;

  @Parameter(property = "productId")
  private String            productId;

  /**
   * SystemdService configuration
   */
  @Parameter(property = "systemdSystem", defaultValue = "true")
  private boolean           systemdSystem;
  /**
   * Yum configuration
   */
  @Parameter(property = "yumSystem", defaultValue = "true")
  private boolean           yumSystem;

  /**
   * Represents the list of pre-requisite
   */
  private RequisitesBuilder requisitesBuilder;

  /**
   * This method is as main one. It will initialize deployement process, and start it.
   * 
   * @throws BeaverException
   */
  @Override
  public void execute() throws MojoExecutionException
  {

    try
    {
      // Defined BeaverLogger info
      BeaverLogger.setLogger(logsDirectory, getLog());

      // Init services and properties
      initialization();

      // Init pre-requisites
      requisitesBuilder = new RequisitesBuilder(ignorePrerequisite);
      final List<DependencyNode> processNodes = BeaverServices.getLauncherService().getProcessNodes();
      if ((processNodes != null) && (processNodes.isEmpty() == false))
      {
        for (final DependencyNode dependencyNode : processNodes)
        {
          requisitesBuilder.buildRequisites(dependencyNode);
        }
      }
      else
      {
        throw new BeaverException("You have to specify one dependency in your launcher");
      }

      // Scan requisites and process them
      scanRequisites();

    }
    catch (final BeaverException e)
    {
      BeaverLogger.getOutlogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getOutlogger().error(e.getMessage());
      throw new MojoExecutionException("Errors occured during deployment process.", e);
    }
    finally
    {
      BeaverLogger.getOutlogger().info(BeaverLogger.SEPARATOR_STAR);
      BeaverLogger.getOutlogger().info("END OF THE DEPLOYMENT");
      BeaverLogger.getOutlogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getOutlogger().info(
          "Let's see the log file " + logsDirectory + " to get more information.");
      BeaverLogger.getOutlogger().info(BeaverLogger.SEPARATOR_STAR);
      writePersistenceFile();
    }
  }

  /**
   * Initialize context of current Mojo. Set up mojo service,deploy service, config service, log service,
   * engine implementation.
   * 
   * @throws BeaverException
   */
  private void initialization() throws BeaverException
  {
    // Initialize BeaverServices
    final BeaverMavenProject project = getProject();
    BeaverServices.init(this, tempDirectory, resolved, simulate, serverId);

    // Build context File
    BeaverServices.buildContextFile(deploymentImplClass, deploymentFile);

    // Initialize BeaverEngine
    BeaverLogger.getOutlogger().info(BeaverLogger.SEPARATOR_STAR);
    BeaverLogger.getOutlogger().info("DEPLOYMENT STARTING");
    BeaverLogger.getOutlogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverEngine beaverEngine = null;
    if (simulate == false)
    {
      beaverEngine = new EngineImpl(systemdSystem, yumSystem);
      if (resolved == true)
      {
        BeaverLogger.getOutlogger().warn(
            "You have actived the resolved mode, so it won't physicaly install your application.");
        BeaverLogger.getOutlogger().warn(
            "It will just download all artifact needed inside your local repository");
        BeaverLogger.getOutlogger().warn(BeaverLogger.SEPARATOR_TIRET);
      }
    }
    else if (resolved == false)
    {
      beaverEngine = new EngineSimulateImpl();

      BeaverLogger.getOutlogger().warn(
          "You have actived the simulation mode, so it won't realy install your application.");
      BeaverLogger.getOutlogger().warn("It will just:");
      BeaverLogger.getOutlogger().warn("    - download all artifacts needed inside your local repository");
      BeaverLogger.getOutlogger().warn("    - execute each process without done anything on your system");
      BeaverLogger.getOutlogger().warn("    - store a report in following file");
      BeaverLogger.getOutlogger().warn(BeaverLogger.getSimulationLog().getAbsolutePath());
      BeaverLogger.getOutlogger().warn(BeaverLogger.SEPARATOR_TIRET);
    }
    BeaverServices.setEngine(beaverEngine);

    // Initialize sub services
    BeaverServices.getLauncherService().init(project);

  }

  /**
   * Scan the list of pre-requisite and execute install process on each.
   * 
   * @throws BeaverException
   */
  private void scanRequisites() throws BeaverException
  {
    if (requisitesBuilder != null)
    {
      for (final DependencyNode requisiteNode : requisitesBuilder.getRequisiteNodes())
      {
        // Update backup deployment context before processing node
        BeaverServices.backupDeploymentContext(deploymentImplClass);

        scanChildren(requisiteNode);

        final ProductProcess requisiteProductProcess = new ProductProcess(requisiteNode);
        if (requisiteProductProcess.isValid())
        {
          processNode(requisiteProductProcess);
        }
      }
    }
  }

  /**
   * Scan each child of main pre-requisite and execute depending on parameter good service invoked :
   * <ul>
   * <li>Deployment</li>
   * <li>Resolve</li>
   * <li>Configure</li>
   * </ul>
   * 
   * @param pNode
   *          represents the node we want to scan.
   * @throws BeaverException
   */
  private void scanChildren(final DependencyNode pNode) throws BeaverException
  {
    final List<DependencyNode> children = pNode.getChildren();
    for (final DependencyNode currendChild : children)
    {
      final ProductProcess currentProductProcess = new ProductProcess(currendChild);
      if (currentProductProcess.isValid())
      {
        scanChildren(currendChild);
        processNode(currentProductProcess);
      }
    }
  }

  /**
   * Process the given {@link BeaverProcess} and execute depending on parameter good service invoked :
   * <ul>
   * <li>Deployment</li>
   * <li>Resolve</li>
   * <li>Configure</li>
   * </ul>
   * 
   * @param pNode
   *          the node to process
   * @throws BeaverException
   */
  private void processNode(final ProductProcess pProductProcess) throws BeaverException
  {
    BeaverServices.setCurrentProductProcess(pProductProcess);
    if (config == true)
    {
      if((productId == null) || (productId.isEmpty() == true) || (Arrays.asList(productId.split(",")).contains(BeaverServices.getCurrentProductProcess().getProductId())))
      {
        BeaverServices.getConfigService().config();
      } else {
        BeaverLogger.getOutlogger().debug(
                  BeaverServices.getCurrentProductProcess().getProductId()
                      + " ("+BeaverServices.getCurrentProductProcess().getArtifact().getArtifactId()+") will not be configured, so skip it.");
      }

    }
    else
    {
      BeaverServices.getDeployService().deploy();
    }

  }

  /**
   * This method is used to store persistence information into file.
   */
  private void writePersistenceFile()
  {
    if (BeaverServices.getEngine() != null)
    {
      if ((resolved == false) && (simulate == false))
      {
        try
        {
          BeaverServices.getDeploymentContext().writeFile();
        }
        catch (final Exception e)
        {
          BeaverLogger.getOutlogger().error("Cannot write persistence file.", e);
        }
      }
      else if (simulate == true)
      {
        ((EngineSimulateImpl) BeaverServices.getEngine()).writeSimulationReport();
      }
    }
  }

  public String getServerId() {
    return serverId;
  }

}
