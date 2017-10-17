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

import java.util.LinkedList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * Requisites build and stores the pile of requisites for the whole process.
 * 
 * @author Guillaume Lamirand
 * @version 3.0
 */
public class RequisitesBuilder
{
  /**
   * The list of pre-requisites built
   */
  private final List<DependencyNode> requisiteNodes;
  /**
   * Defined if prerequisite defined in children has to be ignored
   */
  private final boolean              ignorePrerequisite;

  public RequisitesBuilder(final boolean pIgnorePrerequisite)
  {
    ignorePrerequisite = pIgnorePrerequisite;
    requisiteNodes = new LinkedList<DependencyNode>();
  }

  /**
   * Build the pre-requisite list from main node given
   * 
   * @param pMainNode
   *          the main node to add
   * @throws BeaverException
   */
  public void buildRequisites(final DependencyNode pMainNode) throws BeaverException
  {
    addNode(pMainNode);
    buildSubRequisites(pMainNode);
  }

  /**
   * Add node as pre-requisite to the list
   */
  private void addNode(final DependencyNode pNode) throws BeaverException
  {
    ((LinkedList<DependencyNode>) requisiteNodes).addFirst(pNode);

  }

  /**
   * Build the pre-requisite list from previous pre-requisite
   * 
   * @param pNode
   *          represents the current requisite
   * @param pMainNode
   *          <code>true</code> if the node given is the main one
   * @throws BeaverException
   */
  private void buildSubRequisites(final DependencyNode pNode) throws BeaverException
  {
    BeaverLogger.getFilelogger().debug("------------------------------------------------------------------");
    BeaverLogger.getFilelogger().debug(
        "Build dependency tree for " + pNode.getArtifact().getArtifactId() + " :");

    final Artifact mainArtifact = BeaverServices.getMojoService().resolveArtifact(pNode.getArtifact());
    final BeaverMavenProject mainProject = BeaverServices.getMojoService().getProjectFromArtifact(
        mainArtifact);

    if (mainProject.hasRequisiteArtifact())
    {
      final Artifact requisiteArtifact = BeaverServices.getMojoService().resolveArtifact(
          mainProject.getRequisiteArtifact());
      final RequisiteProcess requisiteProcess = new RequisiteProcess(requisiteArtifact);
      if (requisiteProcess.isValid())
      {
        final String productId = requisiteProcess.getProductId();
        final String serverId = BeaverServices.getLauncherService().getServerInfo().getServerId();
        final boolean existProduct = BeaverServices.getDeploymentContext().existProduct(serverId, productId);
        final boolean updatingProcess = requisiteProcess.isUpdatingProcess();

        if ((existProduct == false) || (updatingProcess))
        {
          final BeaverMavenProject requisiteProject = BeaverServices.getMojoService().getProjectFromArtifact(
              requisiteArtifact);
          final DependencyNode requisiteNode = BeaverServices.getMojoService().buildDependencyTree(
              requisiteProject.getMavenProject());

          if ((requisiteNode != null) && (ignorePrerequisite == false))
          {
            addNode(requisiteNode);
            buildSubRequisites(requisiteNode);
          }
          else if (requisiteNode == null)
          {
            throw new BeaverException("Cannot build requisites tree because of null node");
          }
        }
      }
    }
  }

  /**
   * @return the requisiteNodes
   */
  public List<DependencyNode> getRequisiteNodes()
  {
    return requisiteNodes;
  }
}
