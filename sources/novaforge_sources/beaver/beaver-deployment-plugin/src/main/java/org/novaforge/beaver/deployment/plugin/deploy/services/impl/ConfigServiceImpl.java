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
import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.deploy.services.ConfigService;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * Default implementation of {@link ConfigService}
 * 
 * @author Guillaume Lamirand
 * @see ConfigService
 */
public class ConfigServiceImpl extends AbstractService implements ConfigService
{

  /**
   * {@inheritDoc}
   */
  @Override
  public void config() throws BeaverException
  {
    final String productId = BeaverServices.getCurrentProductProcess().getProductId();
    final String serverId = BeaverServices.getLauncherService().getServerInfo().getServerId();
    final boolean isInstalled = BeaverServices.getDeploymentContext().isProductStatusInstalled(serverId,
        productId);

    if (isInstalled)
    {
      configNode();
    }
    else
    {
      BeaverLogger
          .getOutlogger()
          .debug(
              BeaverServices.getCurrentProductProcess().getArtifact().getArtifactId()
                  + " have not been not correctly installed on this system, please launcher install process before.");
    }
  }

  private void configNode() throws BeaverException
  {

    BeaverServices.getCurrentProductProcess().browseProductProperty();

    BeaverLogger.getOutlogger().info(BeaverLogger.SEPARATOR_STAR);
    BeaverLogger.getOutlogger().info(
        "Configuring " + BeaverServices.getCurrentProductProcess().getArtifact().getArtifactId());

    final Artifact currentArtifact = BeaverServices.getCurrentProductProcess().getArtifact();
    final Path processTmpDir = BeaverServices.getMojoService().getFormattedOutputDirectory(false, false,
        true, false, BeaverServices.getTempDirectory(), currentArtifact);

    prepareProcess(currentArtifact, processTmpDir);
    if (StringUtils.isNotBlank(BeaverServices.getCurrentProductProcess().getConfigScript()))
    {
      final Path executeFile = processTmpDir.resolve(BeaverServices.getCurrentProductProcess()
          .getConfigScript());

      final Path dataFile = resolvedData();

      executeGroovy(dataFile, processTmpDir, executeFile);
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

}
