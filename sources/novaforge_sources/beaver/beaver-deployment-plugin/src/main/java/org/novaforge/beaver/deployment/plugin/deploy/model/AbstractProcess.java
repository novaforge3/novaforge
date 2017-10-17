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
package org.novaforge.beaver.deployment.plugin.deploy.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.novaforge.beaver.context.DeploymentContext;
import org.novaforge.beaver.deployment.plugin.deploy.constant.BeaverMavenProperty;
import org.novaforge.beaver.deployment.plugin.deploy.constant.BeaverProcessType;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Guillaume Lamirand
 */
public abstract class AbstractProcess
{

  /**
   * {@link DependencyNode} of the process
   */
  protected DependencyNode            node;
  /**
   * Beaver Process Artifact
   */
  protected Artifact                  artifact;
  /**
   * Product's Data Artifact
   */
  private Artifact                    dataArtifact;
  /**
   * Define if the current Beaver Process is valid
   */
  protected final boolean             validProductProcess;
  /**
   * Beaver process info
   */
  protected BeaverProcessInfo         processInfo;
  /**
   * Map of previous property
   */
  protected final Map<String, String> previousProperties = new HashMap<>();

  public AbstractProcess(final DependencyNode pProcessNode) throws BeaverException
  {
    this(pProcessNode.getArtifact());
    node = pProcessNode;
  }

  public AbstractProcess(final Artifact pProcessArtifact) throws BeaverException
  {
    node = null;

    resolvedProcessArtefact(pProcessArtifact);
    final BeaverMavenProject project = BeaverServices.getMojoService().getProjectFromArtifact(artifact);
    validProductProcess = project.hasProcessInfo()
        && BeaverMavenProperty.PROCESS_PACKAGING.getPropertyKey().equals(
            project.getMavenProject().getPackaging());

    if (validProductProcess)
    {
      processInfo = project.getProcessInfo();
      buildDataArtefact();
    }
    else if (BeaverMavenProperty.PROCESS_PACKAGING.getPropertyKey().equals(
        project.getMavenProject().getPackaging()))
    {
      throw new BeaverException(
          String
              .format(
                  "The maven project associated to the artefact %s/%s/%s is malformated and doesn't contain any process information",
                  artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion()));

    }
  }

  /**
   * Create the data artifact of the product
   * 
   * @throws BeaverException
   */
  private void buildDataArtefact() throws BeaverException
  {
    try
    {
      final PropertyArtifact dataProperty = processInfo.getDataProperty();
      if (dataProperty != null)
      {
        dataArtifact = BeaverServices.getMojoService().createArtifact(dataProperty);
      }
    }
    catch (final Exception e)
    {
      throw new BeaverException("Unable to create the  data artefact given", e);
    }
  }

  /**
   * Resolve the process artifact of the product
   * 
   * @throws BeaverException
   */
  private void resolvedProcessArtefact(final Artifact pUnresolvedArtifact) throws BeaverException
  {
    try
    {
      artifact = BeaverServices.getMojoService().resolveArtifact(pUnresolvedArtifact);
    }
    catch (final Exception e)
    {
      throw new BeaverException("Unable to resolve the process artefact", e);
    }
  }

  /**
   * Resolve the data artifact of the product
   * 
   * @throws BeaverException
   */
  public void resolveDataArtifact() throws BeaverException
  {
    try
    {
      if (dataArtifact != null)
      {
        dataArtifact = BeaverServices.getMojoService().resolveArtifact(dataArtifact);
      }
    }
    catch (final BeaverException e)
    {
      BeaverLogger.getOutlogger().warn("Unable to find the generic artifact.");
      dataArtifact = BeaverServices.getMojoService().resolvedArtifactWithEnvironment(dataArtifact,
          BeaverServices.getLauncherService().getEnvironment());
      BeaverLogger.getOutlogger().warn(
          "Otherwise it was found for your environment : "
              + BeaverServices.getLauncherService().getEnvironment());
    }
  }

  /**
   * Check if current process is updating or deleting type of process
   * 
   * @return <code>true</code> if the current process will update either process version or data
   * @throws BeaverException
   */
  public boolean isUpdatingProcess() throws BeaverException
  {
    boolean update = false;
    if (((isUpdateType() == true) && ((isDataUpdate() == true) || (isProcessUpdate() == true)))
        || (isDeleteType() == true))
    {
      update = true;
    }
    return update;
  }

  private boolean isDataUpdate() throws BeaverException
  {
    final DeploymentContext deployFileimpl = BeaverServices.getDeploymentContext();

    final String existingVersion = deployFileimpl.getProductVersion(BeaverServices.getLauncherService()
        .getServerInfo().getServerId(), getProductId());

    return compareVersion(existingVersion, getProductVersion());

  }

  private boolean isProcessUpdate() throws BeaverException
  {
    final DeploymentContext deployFileimpl = BeaverServices.getDeploymentContext();

    final String existingUpdateVersion = deployFileimpl.getProductUpdateVersion(BeaverServices
        .getLauncherService().getServerInfo().getServerId(), getProductId());
    final String newUpdateVersion = artifact.getVersion();

    return compareVersion(existingUpdateVersion, newUpdateVersion);

  }

  /**
   * Compare two version.
   * 
   * @param pOldVersion
   *          represents previous version
   * @param pNewVersion
   *          represents new version
   * @return true if pNewVersion>pOldVersion otherwise false
   * @throws BeaverException
   */
  private boolean compareVersion(final String pOldVersion, final String pNewVersion)
  {
    boolean isSup = false;
    final NaturalOrderComparator naturalComparator = new NaturalOrderComparator();
    if (naturalComparator.compare(pNewVersion, pOldVersion) > 0)
    {
      isSup = true;
    }
    return isSup;
  }

  public boolean isInstallType()
  {
    return BeaverProcessType.INSTALL.equals(getProcessType());

  }

  public boolean isUpdateType()
  {
    return BeaverProcessType.UPDATE.equals(getProcessType());

  }

  public boolean isDeleteType()
  {
    return BeaverProcessType.DELETE.equals(getProcessType());
  }

  /**
   * Returns <code>true</code> if the current process is a valid beaver-process project
   * 
   * @return <code>true</code> if the current process is a valid beaver-process project
   */
  public boolean isValid()
  {
    return validProductProcess;
  }

  /**
   * @return the node
   */
  public DependencyNode getNode()
  {
    return node;
  }

  /**
   * @return the artifact
   */
  public Artifact getArtifact()
  {
    return artifact;
  }

  /**
   * @return the ProductId
   */
  public String getProductId()
  {
    return processInfo.getProductId();
  }

  /**
   * @return the dataArtifact
   */
  public Artifact getDataArtifact()
  {
    return dataArtifact;
  }

  /**
   * @return the previousProperties
   */
  public Map<String, String> getPreviousProperties()
  {
    return previousProperties;
  }

  /**
   * @return the processType
   */
  public BeaverProcessType getProcessType()
  {
    return processInfo.getProcessType();
  }

  /**
   * @return the groovyScript
   */
  public String getMainScript()
  {
    return processInfo.getScript();
  }

  /**
   * @return the groovyScript
   */
  public String getConfigScript()
  {
    return processInfo.getConfig();
  }

  /**
   * @return the productVersion
   */
  public String getProductVersion()
  {
    return processInfo.getProductVersion();
  }
}
