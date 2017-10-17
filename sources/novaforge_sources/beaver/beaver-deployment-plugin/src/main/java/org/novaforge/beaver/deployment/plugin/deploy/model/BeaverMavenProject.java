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
import java.util.Map.Entry;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.novaforge.beaver.context.ServerType;
import org.novaforge.beaver.deployment.plugin.deploy.constant.BeaverMavenProperty;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.resources.ResourceImpl;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.exception.BeaverException;
import org.novaforge.beaver.resource.Resource;

/**
 * This object is used to wrap a {@link MavenProject} in order to retrieve Beaver properties from it
 * 
 * @author Guillaume Lamirand
 */
public class BeaverMavenProject
{

  /**
   * Separator used in process artefactid to retrieve product version
   */
  private static final String PRODUCT_VERSION_SEPARATOR = "-";
  /**
   * The original {@link MavenProject} wrapped
   */
  private final MavenProject  mavenProject;

  private Artifact            requisiteArtifact;

  private BeaverProcessInfo   processInfo;
  private BeaverServerInfo    serverInfo;

  /**
   * Default private contructor. Wrap the {@link MavenProject} given to retrieve meta information
   * 
   * @param pMavenProject
   *          the original {@link MavenProject} to wrap
   * @throws BeaverException
   *           thrown on any errors when finding metainformation
   */
  public BeaverMavenProject(final MavenProject pMavenProject) throws BeaverException
  {
    mavenProject = pMavenProject;
    if (mavenProject != null)
    {
      retrieveServerInfo();
      retrieveRequisite();
      retrieveProcessInfo();
    }
  }

  private void retrieveServerInfo() throws BeaverException
  {
    if (mavenProject.getProperties().containsKey(BeaverMavenProperty.SERVER_ID.getPropertyKey()))
    {
      String serverId = BeaverServices.getServerId();
      if(serverId==null || serverId.isEmpty())
      {
        serverId = mavenProject.getProperties().getProperty(
                BeaverMavenProperty.SERVER_ID.getPropertyKey());
      }

      BeaverServerInfo.validateId(serverId);

      final Map<String, String> serverProperties = new HashMap<>();
      for (final Entry<Object, Object> property : mavenProject.getProperties().entrySet())
      {
        final String resourceKey = property.getKey().toString();
        if (BeaverMavenProperty.isBeaverMavenProperty(resourceKey) == false)
        {
          final Resource resource = new ResourceImpl(resourceKey);
          if ((resource.isCorrectFormat())
              && (resource.isServerRelated())
              && ((serverId.equals(resource.getServerId())) || (Resource.LOCAL_SERVER.equals(resource
                  .getServerId()))))
          {
            serverProperties.put(resource.getKey(), property.getValue().toString());
          }
        }
      }

      final String type = mavenProject.getProperties().getProperty(
          BeaverMavenProperty.SERVER_TYPE.getPropertyKey());
      final ServerType serverType = ServerType.get(type);

      final String setup = mavenProject.getProperties().getProperty(
          BeaverMavenProperty.SERVER_INSTALL.getPropertyKey());
      final String update = mavenProject.getProperties().getProperty(
          BeaverMavenProperty.SERVER_UPDATE.getPropertyKey());

      serverInfo = new BeaverServerInfo(serverId, serverType, setup, update, serverProperties);
    }

  }

  private void retrieveRequisite() throws BeaverException
  {
    if (mavenProject.getProperties().containsKey(BeaverMavenProperty.REQUISITE.getPropertyKey()))
    {
      final String requisiteProperty = mavenProject.getProperties().getProperty(
          BeaverMavenProperty.REQUISITE.getPropertyKey());
      final PropertyArtifact propertyArtefact = new PropertyArtifact(requisiteProperty);
      requisiteArtifact = BeaverServices.getMojoService().createArtifact(propertyArtefact);
    }
  }

  private void retrieveProcessInfo() throws BeaverException
  {
    final String artifactId = mavenProject.getArtifactId();
    final int index = artifactId.indexOf(PRODUCT_VERSION_SEPARATOR);
    if ((index != -1)
        && (mavenProject.getProperties().containsKey(BeaverMavenProperty.PROCESS.getPropertyKey()))
        && (mavenProject.getProperties().containsKey(BeaverMavenProperty.PRODUCT_ID.getPropertyKey()))
        && (mavenProject.getProperties().containsKey(BeaverMavenProperty.SCRIPT.getPropertyKey())))
    {
      final String productVersion = artifactId.substring(index + 1);
      final String process = mavenProject.getProperties().getProperty(
          BeaverMavenProperty.PROCESS.getPropertyKey());
      final String productId = mavenProject.getProperties().getProperty(
          BeaverMavenProperty.PRODUCT_ID.getPropertyKey());
      final String script = mavenProject.getProperties().getProperty(
          BeaverMavenProperty.SCRIPT.getPropertyKey());
      final String config = mavenProject.getProperties().getProperty(
          BeaverMavenProperty.CONFIG.getPropertyKey());
      final String data = mavenProject.getProperties().getProperty(BeaverMavenProperty.DATA.getPropertyKey());

      final Map<String, String> productProperties = new HashMap<>();
      for (final Entry<Object, Object> property : mavenProject.getProperties().entrySet())
      {
        final String resourceKey = property.getKey().toString();
        if (BeaverMavenProperty.isBeaverMavenProperty(resourceKey) == false)
        {
          final Resource resource = new ResourceImpl(resourceKey);
          if ((resource.isCorrectFormat()) && (resource.isCurrentProductRelated()))
          {
            productProperties.put(resource.getKey(), property.getValue().toString());
          }
        }
      }

      processInfo = new BeaverProcessInfo(productVersion, process, productId, script, config, data,
          productProperties);
    }
  }

  /**
   * @return the mavenProject
   */
  public MavenProject getMavenProject()
  {
    return mavenProject;
  }

  /**
   * @return <code>true</code> if the {@link MavenProject} wrapped contains a requisite artifact
   */
  public boolean hasRequisiteArtifact()
  {
    return requisiteArtifact != null;
  }

  /**
   * @return the requisiteArtifact
   */
  public Artifact getRequisiteArtifact()
  {
    return requisiteArtifact;
  }

  /**
   * @return the processInfo
   */
  public BeaverProcessInfo getProcessInfo()
  {
    return processInfo;
  }

  /**
   * @return <code>true</code> if the {@link MavenProject} wrapped contains a process information
   */
  public boolean hasProcessInfo()
  {
    return processInfo != null;
  }

  /**
   * @return the serverInfo
   */
  public BeaverServerInfo getServerInfo()
  {
    return serverInfo;
  }

  /**
   * @return <code>true</code> if the {@link MavenProject} wrapped contains a server information
   */
  public boolean hasServerInfo()
  {
    return serverInfo != null;
  }

}
