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
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.context.ServerType;
import org.novaforge.beaver.deployment.plugin.deploy.constant.BeaverMavenProperty;
import org.novaforge.beaver.deployment.plugin.deploy.constant.SystemEnvironment;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.resources.ResourceImpl;
import org.novaforge.beaver.deployment.plugin.deploy.model.BeaverMavenProject;
import org.novaforge.beaver.deployment.plugin.deploy.model.BeaverServerInfo;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.deploy.services.LauncherService;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;
import org.novaforge.beaver.resource.Resource;

/**
 * Default implementation of {@link LauncherService}
 * 
 * @author Guillaume Lamirand
 * @see LauncherService
 */
public class LauncherServiceImpl extends AbstractService implements LauncherService
{
  /**
   * Reference to the main maven project
   */
  private BeaverMavenProject         launcherMavenProject;
  /**
   * The computed dependency tree root node of the Maven project.
   */
  private DependencyNode             rootNode;
  private BeaverServerInfo           beaverServerInfo;
  private final List<DependencyNode> processNodes = new ArrayList<DependencyNode>();
  private SystemEnvironment          systemEnvironment;

  @Override
  public void init(final BeaverMavenProject pLauncherProject) throws BeaverException
  {
    // Set environment
    if (Os.isFamily(Os.FAMILY_UNIX))
    {
      systemEnvironment = SystemEnvironment.LINUX;
    }
    else if (Os.isFamily(Os.FAMILY_WINDOWS))
    {
      systemEnvironment = SystemEnvironment.WINDOWS;
    }

    launcherMavenProject = pLauncherProject;
    if (launcherMavenProject != null)
    {
      final MavenProject mavenProject = launcherMavenProject.getMavenProject();

      // Build dependency graph for launcher maven projet
      rootNode = BeaverServices.getMojoService().buildDependencyTree(mavenProject);

      final List<DependencyNode> children = rootNode.getChildren();
      for (final DependencyNode dependencyNode : children)
      {
        final Artifact artifact = dependencyNode.getArtifact();
        final String artifactType = artifact.getType();
        if (BeaverMavenProperty.SERVER_PACKAGING.getPropertyKey().equals(artifactType))
        {
          if (beaverServerInfo == null)
          {
            BeaverLogger.getOutlogger().info(BeaverLogger.SEPARATOR_STAR);
            BeaverLogger.getOutlogger().info(
                String.format("Processing server <%s>", artifact.getArtifactId()));
            BeaverLogger.getOutlogger().info(BeaverLogger.SEPARATOR_TIRET);

            // Resolved server artifact
            final Artifact resolveArtifact = BeaverServices.getMojoService().resolveArtifact(artifact);

            // Retrieve server info from artifact
            beaverServerInfo = retrieveServerInfo(artifact);

            // Update server properties from launcher properties
            updateProperties();

            // Store values to deployment context
            final boolean isNew = storeServerInfo();

            if (BeaverServices.isResolved() == false)
            {
              // Process server
              processingServer(resolveArtifact, isNew);
            }
          }
          else
          {
            throw new BeaverException("More than one beaver-server is defined in your launcher.");
          }
        }
        else if ((BeaverMavenProperty.PROCESS_PACKAGING.getPropertyKey().equals(artifactType))
            && (processNodes.contains(dependencyNode) == false))
        {
          processNodes.add(dependencyNode);
        }

      }
    }
  }

  private BeaverServerInfo retrieveServerInfo(final Artifact artifact) throws BeaverException
  {
    BeaverLogger.getOutlogger().info("Retrieve server info");
    BeaverServerInfo returnServerInfo = null;
    final BeaverMavenProject serverProject = BeaverServices.getMojoService().getProjectFromArtifact(artifact);
    if ((serverProject != null) && (serverProject.hasServerInfo()))
    {
      returnServerInfo = serverProject.getServerInfo();
      validServer(returnServerInfo);

    }
    else if ((serverProject == null) || (serverProject.hasServerInfo()))
    {
      throw new BeaverException(
          "Server information cannot be retrieve from beaver-server defined in your launcher.");
    }
    return returnServerInfo;
  }

  private void updateProperties() throws BeaverException
  {
    BeaverLogger.getOutlogger().info("Update server properties");

    final Properties properties = launcherMavenProject.getMavenProject().getProperties();
    for (final Entry<Object, Object> property : properties.entrySet())
    {
      final String resourceKey = property.getKey().toString();
      if (BeaverMavenProperty.isBeaverMavenProperty(resourceKey) == false)
      {
        final Resource resource = new ResourceImpl(resourceKey);
        if ((resource.isCorrectFormat())
            && (resource.isServerRelated())
            && ((beaverServerInfo.getServerId().equals(resource.getServerId())) || (Resource.LOCAL_SERVER
                .equals(resource.getServerId()))))
        {
          final String key = resource.getKey();
          final String value = property.getValue().toString();
          beaverServerInfo.putProperty(key, value);
          if (BeaverLogger.getOutlogger().isDebugEnabled())
          {
            BeaverLogger.getOutlogger()
                .debug(String.format("Update property [key=%s, value=%s]", key, value));

          }
        }
      }
    }
  }

  private void validServer(final BeaverServerInfo returnServerInfo) throws BeaverException
  {
    BeaverLogger.getOutlogger().info("Validate server");
    final String serverId = returnServerInfo.getServerId();
    final String mainServerId = BeaverServices.getDeploymentContext().getMainServerId();
    if ((StringUtils.isNotBlank(mainServerId)) && (ServerType.MAIN.equals(returnServerInfo.getServerType()))
        && (mainServerId.equals(serverId) == false))
    {
      throw new BeaverException(
          "A main server is already defined in the deployment context, you cannot set another one");
    }
    else if ((StringUtils.isBlank(mainServerId))
        && (ServerType.SIMPLE.equals(returnServerInfo.getServerType())))
    {
      throw new BeaverException(
          "There is not main server in the deployment context, you have to set the current as main or deploy the main server before the current one.");
    }
  }

  /**
   * Save the current server info to deployment context
   * 
   * @return if server was previously existing
   * @throws BeaverException
   */
  private boolean storeServerInfo() throws BeaverException
  {
    BeaverLogger.getOutlogger().info("Store server context");
    boolean wasExisting = false;
    if (beaverServerInfo != null)
    {
      final String serverId = beaverServerInfo.getServerId();
      wasExisting = BeaverServices.getDeploymentContext().existServer(serverId);
      if (wasExisting == false)
      {
        BeaverServices.getDeploymentContext().addServer(serverId, beaverServerInfo.getServerType());
      }
      for (final Entry<String, String> entry : beaverServerInfo.propertyEntrySet())
      {
        BeaverServices.getDeploymentContext().addServerProperty(serverId, entry.getKey(), entry.getValue());
      }
    }
    else
    {
      throw new BeaverException(
          "There is not server defined, unable to store its information to deployment context.");
    }
    return wasExisting;
  }

  /**
   * Execute the install or update process for the current server
   * 
   * @throws BeaverException
   */
  private void processingServer(final Artifact pServerArtifact, final boolean pWasExisting)
      throws BeaverException
  {
    if (pWasExisting)
    {
      BeaverLogger.getOutlogger().info("Updating the server");
    }
    else
    {
      BeaverLogger.getOutlogger().info("Installing the server");
    }

    final Path processTmpDir = BeaverServices.getMojoService().getFormattedOutputDirectory(false, false,
        true, false, BeaverServices.getTempDirectory(), pServerArtifact);

    prepareProcess(pServerArtifact, processTmpDir);
    Path executeGroovy = null;
    if ((pWasExisting == false) && (StringUtils.isNotBlank(beaverServerInfo.getInstallScript())))
    {
      executeGroovy = processTmpDir.resolve(beaverServerInfo.getInstallScript());
    }
    else if (StringUtils.isNotBlank(beaverServerInfo.getUpdateScript()))
    {
      executeGroovy = processTmpDir.resolve(beaverServerInfo.getUpdateScript());
    }
    if (executeGroovy != null)
    {
      executeGroovy(null, processTmpDir, executeGroovy);
    }
    else
    {
      BeaverLogger.getOutlogger().warn("Server doesn't define groovy script, does that make sense?");
    }

  }

  /**
   * Returns the value of the property given as parameter
   * 
   * @return value found in laucnher properties
   */
  @Override
  public String getProperty(final String pKey)
  {
    return launcherMavenProject.getMavenProject().getProperties().getProperty(pKey);
  }

  /**
   * Returns the value of the property given as parameter
   * 
   * @return value found in laucnher properties
   */
  @Override
  public boolean containsProperty(final String pKey)
  {
    return launcherMavenProject.getMavenProject().getProperties().containsKey(pKey);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getEnvironment()
  {
    return systemEnvironment.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onLinux()
  {
    return systemEnvironment.equals(SystemEnvironment.LINUX);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onWindows()
  {
    return systemEnvironment.equals(SystemEnvironment.WINDOWS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BeaverServerInfo getServerInfo()
  {
    return beaverServerInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BeaverMavenProject getLauncherProject()
  {
    return launcherMavenProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DependencyNode getRootNode()
  {
    return rootNode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DependencyNode> getProcessNodes()
  {
    return processNodes;
  }

}
