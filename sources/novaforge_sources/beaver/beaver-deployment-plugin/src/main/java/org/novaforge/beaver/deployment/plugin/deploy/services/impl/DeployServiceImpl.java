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

import java.nio.file.Path;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.deploy.model.BeaverMavenProject;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.deploy.services.DeployService;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * Default implementation of {@link DeployService}
 * 
 * @author Guillaume Lamirand
 * @see DeployService
 */
public class DeployServiceImpl extends AbstractService implements DeployService
{

  /**
   * {@inheritDoc}
   */
  @Override
  public void deploy() throws BeaverException
  {
    addProductToContext();
    invokeExecutionProcess();
  }

  /**
   * Add product to deployment context file
   */
  private void addProductToContext()
  {
    final String productId = BeaverServices.getCurrentProductProcess().getProductId();
    final String serverId = BeaverServices.getLauncherService().getServerInfo().getServerId();

    final boolean existProduct = BeaverServices.getDeploymentContext().existProduct(serverId, productId);

    if ((existProduct == false) && (BeaverServices.getCurrentProductProcess().isInstallType()))
    {
      final String productVersion = BeaverServices.getCurrentProductProcess().getProductVersion();
      BeaverServices.getDeploymentContext().addProduct(serverId, productId, productVersion);
      BeaverServices.getDeploymentContext().setProductStatusInstalling(serverId, productId);
    }
  }

  /**
   * invoke install/update/delete process if it necessary
   * 
   * @throws BeaverException
   */
  private void invokeExecutionProcess() throws BeaverException
  {
    BeaverLogger.getOutlogger().info(BeaverLogger.SEPARATOR_STAR);
    BeaverLogger.getOutlogger().info(
        String.format("Processing product <%s>", BeaverServices.getCurrentProductProcess().getArtifact()
            .getArtifactId()));

    final String productId = BeaverServices.getCurrentProductProcess().getProductId();
    final String serverId = BeaverServices.getLauncherService().getServerInfo().getServerId();

    final boolean isInstalling = BeaverServices.getDeploymentContext().isProductStatusInstalling(serverId,
        productId);
    final boolean isInstalled = BeaverServices.getDeploymentContext().isProductStatusInstalled(serverId,
        productId);
    final boolean updatingProcess = BeaverServices.getCurrentProductProcess().isUpdatingProcess();

    if (isInstalling == true)
    {
      processingNode(updatingProcess);
    }
    else if ((isInstalled == true))
    {
      if (updatingProcess == true)
      {
        processingNode(updatingProcess);
      }
      else
      {
        BeaverLogger.getFilelogger().info("Product already installed on this system");

      }
    }
    else if (BeaverServices.isResolved())
    {
      resolvedData();
    }
    else
    {
      BeaverLogger.getOutlogger().warn(
          "Product is unstable, please check it in persistence file. Is it existing on system? ");
    }

    if (updatingProcess)
    {
      if (BeaverServices.getCurrentProductProcess().isDeleteType())
      {
        BeaverServices.getDeploymentContext().removeProduct(serverId,
            BeaverServices.getCurrentProductProcess().getProductId());
      }
      else
      {
        final String artifactVersion = BeaverServices.getCurrentProductProcess().getArtifact().getVersion();
        final String productVersion = BeaverServices.getCurrentProductProcess().getProductVersion();
        BeaverServices.getDeploymentContext().setProductVersion(serverId, productId, productVersion);
        BeaverServices.getDeploymentContext().setProductUpdateVersion(serverId, productId, artifactVersion);
      }
    }
    BeaverServices.getDeploymentContext().setProductStatusInstalled(serverId, productId);
  }

  /**
   * Execute the install or update process for the current product
   * 
   * @throws BeaverException
   */
  private void processingNode(final boolean isUpdateProcess) throws BeaverException
  {
    BeaverServices.getCurrentProductProcess().browseProductProperty();
    final Path dataFile = resolvedData();

    if (BeaverServices.isResolved() == false)
    {
      if (isUpdateProcess)
      {
        BeaverLogger.getOutlogger().info("Updating the product");
      }
      else
      {
        BeaverLogger.getOutlogger().info("Installing the product");
      }

      final Artifact currentArtifact = BeaverServices.getCurrentProductProcess().getArtifact();
      final Path processTmpDir = BeaverServices.getMojoService().getFormattedOutputDirectory(false, false,
          true, false, BeaverServices.getTempDirectory(), currentArtifact);

      prepareProcess(currentArtifact, processTmpDir);
      executeMainGroovy(dataFile, processTmpDir);
      executeConfigurationGroovy(dataFile, processTmpDir);
      checkComponentToUpdate();
    }

  }

  /**
   * Resolve the data artefact if process has one
   * 
   * @throws BeaverException
   */
  private Path resolvedData() throws BeaverException
  {
    Path dataFile = null;
    if (BeaverServices.getCurrentProductProcess().getDataArtifact() != null)
    {
      BeaverLogger.getOutlogger().info("Retrieving data for the product");
      BeaverServices.getCurrentProductProcess().resolveDataArtifact();
      dataFile = BeaverServices.getCurrentProductProcess().getDataArtifact().getFile().toPath();
    }
    return dataFile;
  }

  /**
   * Execute a main groovy script.
   * 
   * @param pDataDir
   *          represents the path where the data artifact is located.
   * @param pProcessTmpDir
   *          represents the path where the descritor was unpacked.
   * @throws BeaverException
   */
  private void executeMainGroovy(final Path pDataDir, final Path pProcessTmpDir) throws BeaverException
  {

    final Path executeFile = pProcessTmpDir
        .resolve(BeaverServices.getCurrentProductProcess().getMainScript());
    executeGroovy(pDataDir, pProcessTmpDir, executeFile);
  }

  /**
   * Execute a configuration groovy script.
   * 
   * @param pDataDir
   *          represents the path where the data artifact is located.
   * @param pProcessTmpDir
   *          represents the path where the descritor was unpacked.
   * @throws BeaverException
   */
  private void executeConfigurationGroovy(final Path pDataDir, final Path pProcessTmpDir)
      throws BeaverException
  {
    if (StringUtils.isNotBlank(BeaverServices.getCurrentProductProcess().getConfigScript()))
    {
      final Path executeFile = pProcessTmpDir.resolve(BeaverServices.getCurrentProductProcess()
          .getConfigScript());

      executeGroovy(pDataDir, pProcessTmpDir, executeFile);
    }

  }

  /**
   * Check the artifacts which are impacted by the current installation and set them to installing in the
   * deployment
   * file
   * 
   * @throws BeaverException
   */
  private void checkComponentToUpdate() throws BeaverException
  {
    DependencyNode parentNode = BeaverServices.getCurrentProductProcess().getNode().getParent();
    while ((parentNode != BeaverServices.getLauncherService().getRootNode()) && (parentNode != null))
    {
      final Artifact parentArtifact = BeaverServices.getMojoService().resolveArtifact(
          parentNode.getArtifact());

      final BeaverMavenProject parentProject = BeaverServices.getMojoService().getProjectFromArtifact(
          parentArtifact);
      if (parentProject.hasProcessInfo())
      {
        final String parentId = parentProject.getProcessInfo().getProductId();
        final String serverId = BeaverServices.getLauncherService().getServerInfo().getServerId();
        if (BeaverServices.getDeploymentContext().existProduct(serverId, parentId))
        {
          BeaverServices.getDeploymentContext().setProductStatusInstalling(serverId, parentId);
        }
      }
      parentNode = parentNode.getParent();

    }

  }
}
