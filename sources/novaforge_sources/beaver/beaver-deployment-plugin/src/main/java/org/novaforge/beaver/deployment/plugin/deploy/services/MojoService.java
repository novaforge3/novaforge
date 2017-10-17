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
package org.novaforge.beaver.deployment.plugin.deploy.services;

import java.nio.file.Path;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.novaforge.beaver.deployment.plugin.deploy.model.BeaverMavenProject;
import org.novaforge.beaver.deployment.plugin.deploy.model.PropertyArtifact;
import org.novaforge.beaver.exception.BeaverException;

public interface MojoService
{
  /**
   * Create an artifact from the property given. It will find the correct groupId, an artifactId, a version,a
   * type and classifier from it.
   * 
   * @param pPropertyArtefact
   *          property defining artefact.
   * @return Artifact artifact build from parameters
   * @throws BeaverException
   *           thrown if property is malformed
   */
  Artifact createArtifact(final PropertyArtifact pPropertyArtefact) throws BeaverException;

  /**
   * Resolve an artifact from remote repository.
   * 
   * @param pArtifact
   *          artifact to be resolved.
   * @return Artifact resolved and present in local repository.
   * @throws BeaverException
   */
  Artifact resolveArtifact(final Artifact pArtifact) throws BeaverException;

  /**
   * Resolve artifact with classifier, in our case the classifier contain the current environnment (Linux or
   * Windows)
   * 
   * @param pArtifact
   *          artifact to be resolved.
   * @return Artifact resolved and present in local repository.
   * @throws BeaverException
   */
  Artifact resolvedArtifactWithEnvironment(final Artifact pArtifact, final String pEnvironment)
      throws BeaverException;

  /**
   * Build Maven Project object from an artifact.
   * 
   * @param pArtifact
   *          represents the artifact's project.
   * @return {@link BeaverMavenProject} built.
   * @throws BeaverException
   */
  BeaverMavenProject getProjectFromArtifact(final Artifact pArtifact) throws BeaverException;

  /**
   * Build dependency tree from Maven Project Object.
   * 
   * @param pProject
   *          represents the project from which we want to build a dependency tree
   * @return DependencyNode resulting from Mave Project
   * @throws BeaverException
   */
  DependencyNode buildDependencyTree(final MavenProject pProject) throws BeaverException;

  /**
   * Formats the outputDirectory regarding a specific artifact. (ex; org/apache/maven/1.0/)
   * 
   * @param pUseSubdirsPerType
   *          if a new sub directory should be used for each type.
   * @param pUseSubdirPerArtifact
   *          if a new sub directory should be used for each artifact.
   * @param pUseRepositoryLayout
   *          if dependendies must be moved into a Maven repository layout, if set, other settings will be
   *          ignored.
   * @param pRemoveVersion
   *          if the version must not be mentioned in the filename
   * @param pOutputDirectory
   *          base outputDirectory.
   * @param pArtifact
   *          information about the artifact.
   * @return a formatted File object to use for output.
   */
  Path getFormattedOutputDirectory(final boolean pUseSubdirsPerType, final boolean pUseSubdirPerArtifact,
      final boolean pUseRepositoryLayout, final boolean pRemoveVersion, final Path pOutputDirectory,
      final Artifact pArtifact);

  /**
   * Return the used Maven Project
   * 
   * @return the Maven Project
   */
  MavenProject getMavenProject();
}
