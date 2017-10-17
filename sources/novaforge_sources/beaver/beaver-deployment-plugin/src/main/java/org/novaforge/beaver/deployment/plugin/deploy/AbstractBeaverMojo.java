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

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Profile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.apache.maven.repository.RepositorySystem;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.deploy.model.BeaverMavenProject;
import org.novaforge.beaver.deployment.plugin.deploy.model.PropertyArtifact;
import org.novaforge.beaver.deployment.plugin.deploy.services.MojoService;
import org.novaforge.beaver.exception.BeaverException;
import org.sonatype.aether.RepositorySystemSession;

/**
 * AbstractBeaverMojo is a common abstract class which includes common components used by Mojo
 * 
 * @author Guillaume Lamirand
 * @version 3.0
 * @since 0.1
 */
public abstract class AbstractBeaverMojo extends AbstractMojo implements MojoService
{
  /**
   * Used to look up Artifacts in the remote repository.
   */
  @Component()
  private RepositorySystem         repositorySystem;
  @Parameter(property = "repositorySystemSession")
  private RepositorySystemSession  repositorySystemSession;

  /**
   * Location of the local repository.
   */
  @Parameter(property = "localRepository", required = true, readonly = true)
  private ArtifactRepository       localRepository;

  /**
   * List of remote repositories define by Maven settings, it is used by the resolver.
   */
  @Parameter(property = "project.remoteArtifactRepositories", required = true, readonly = true)
  private List<ArtifactRepository> remoteRepos;

  /**
   * List of plugin repositories define by Maven settings, it is used by the resolver.
   */
  @Parameter(property = "project.pluginArtifactRepositories", required = true, readonly = true)
  private List<ArtifactRepository> pluginArtifactRepositories;

  /**
   * The dependency tree builder uses to build dependency node.
   */
  @Component(hint = "default")
  private DependencyGraphBuilder   dependencyGraphBuilder;

  /**
   * To manage Maven project and build on from specific artifact.
   */
  @Component(role = org.apache.maven.project.ProjectBuilder.class)
  private ProjectBuilder           projectBuilder;

  /**
   * To manage Maven session details
   */
  @Parameter(property = "session", readonly = true)
  private MavenSession             mavenSession;

  /**
   * Represents the current Maven project, in our case our launcher pom.xml.
   */
  @Parameter(property = "project", readonly = true)
  private MavenProject             project;
  /**
   * Represents the current Maven project wrap as a {@link BeaverMavenProject}
   */
  private BeaverMavenProject       beaverMavenProject;
  /**
   * Output absolute filename for resolved artifacts; Used to define the path of artifact
   */
  @Parameter(property = "outputAbsoluteArtifactFilename", defaultValue = "true")
  private boolean                  outputAbsoluteArtifactFilename;
  /**
   * Keep version in path directory. Used to define the path of artifact.
   */
  @Parameter(property = "remoteVersion", defaultValue = "false")
  private boolean                  remoteVersion;

  /**
   * Place each type of file in a separate subdirectory. (example /outputDirectory/jars /outputDirectory/wars
   * etc)
   */
  @Parameter(property = "useSubDirectoryPerType", defaultValue = "false")
  private boolean                  useSubDirectoryPerType;

  /**
   * Place each file in a separate subdirectory. (example /outputDirectory/junit-3.8.1-jar)
   */
  @Parameter(property = "useSubDirectoryPerArtifact", defaultValue = "false")
  private boolean                  useSubDirectoryPerArtifact;
  /**
   * Place each artifact in the same directory layout as a default repository. <br/>
   * example: /outputDirectory/junit/junit/3.8.1/junit-3.8.1.jar
   */
  @Parameter(property = "useRepositoryLayout", defaultValue = "true")
  private boolean                  useRepositoryLayout;

  /**
   * {@inheritDoc}
   * 
   * @throws BeaverException
   */
  @Override
  public Artifact createArtifact(final PropertyArtifact pPropertyArtefact) throws BeaverException
  {

    return repositorySystem.createArtifactWithClassifier(pPropertyArtefact.getGroupId(),
        pPropertyArtefact.getArtifactId(), pPropertyArtefact.getVersion(), pPropertyArtefact.getType(),
        pPropertyArtefact.getClassifier());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Artifact resolveArtifact(final Artifact pArtifact) throws BeaverException
  {
    final Artifact artifactResolved = pArtifact;

    final ArtifactResolutionRequest request = new ArtifactResolutionRequest().setArtifact(artifactResolved)
        .setResolveRoot(true).setResolveTransitively(false).setLocalRepository(getLocalRepository())
        .setRemoteRepositories(remoteRepos);
    final ArtifactResolutionResult resolutionResult = repositorySystem.resolve(request);
    if (resolutionResult.hasExceptions())
    {
      throw new BeaverException(String.format("Could not resolve artifact [%s]", pArtifact), resolutionResult
          .getExceptions().get(0));
    }
    else if (resolutionResult.hasMissingArtifacts())
    {
      throw new BeaverException(String.format("Some of artifacts requested %s are not found.",
          resolutionResult.getMissingArtifacts().toString()));
    }
    else
    {
      return artifactResolved;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Artifact resolvedArtifactWithEnvironment(final Artifact pArtifact, final String pEnvironment)
      throws BeaverException
  {
    final Artifact dataArtifact = repositorySystem.createArtifactWithClassifier(pArtifact.getGroupId(),
        pArtifact.getArtifactId(), pArtifact.getVersion(), pArtifact.getType(), pEnvironment);

    resolveArtifact(dataArtifact);

    return dataArtifact;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BeaverMavenProject getProjectFromArtifact(final Artifact pArtifact) throws BeaverException
  {

    BeaverMavenProject artefactProject = null;
    try
    {
      // Create a project building request
      final ProjectBuildingRequest request = new DefaultProjectBuildingRequest();

      // Add active profile to requrst builder
      final List<String> activeProfileIds = new ArrayList<String>();
      for (final Profile profile : project.getActiveProfiles())
      {
        request.addProfile(profile);
        activeProfileIds.add(profile.getId());
      }
      request.setActiveProfileIds(activeProfileIds);

      // Add user ans system properties to request builder
      request.setUserProperties(mavenSession.getUserProperties());
      request.setSystemProperties(mavenSession.getSystemProperties());
      // Add repositories information
      request.setLocalRepository(getLocalRepository());
      request.setRemoteRepositories(remoteRepos);
      request.setRepositorySession(repositorySystemSession);
      request.setPluginArtifactRepositories(pluginArtifactRepositories);
      final ProjectBuildingResult projectBuildingResult = projectBuilder.build(pArtifact, request);
      artefactProject = new BeaverMavenProject(projectBuildingResult.getProject());
    }
    catch (final ProjectBuildingException e)
    {
      throw new BeaverException("Unable to create a Maven project with the artifact: " + pArtifact, e);
    }
    return artefactProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DependencyNode buildDependencyTree(final MavenProject pProject) throws BeaverException
  {
    DependencyNode nodeWithTree = null;
    try
    {
      nodeWithTree = dependencyGraphBuilder.buildDependencyGraph(pProject, null);
    }
    catch (final DependencyGraphBuilderException e)
    {
      throw new BeaverException("Unable to create a Dependency Tree from the Maven project: " + pProject, e);
    }
    return nodeWithTree;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Path getFormattedOutputDirectory(final boolean useSubdirsPerType,
      final boolean useSubdirPerArtifact, final boolean useRepositoryLayout, final boolean removeVersion,
      final Path pOutputDirectory, final Artifact pArtifact)
  {
    final StringBuilder formatedDirectory = new StringBuilder();
    if (useRepositoryLayout)
    {
      // group id
      formatedDirectory.append(pArtifact.getGroupId().replace('.', File.separatorChar)).append(
          File.separatorChar);
      // artifact id
      formatedDirectory.append(pArtifact.getArtifactId()).append(File.separatorChar);
      // version
      formatedDirectory.append(pArtifact.getBaseVersion()).append(File.separatorChar);
    }
    else
    {
      if (useSubdirsPerType)
      {
        formatedDirectory.append(pArtifact.getType()).append("s").append(File.separatorChar);
      }
      if (useSubdirPerArtifact)
      {
        final String artifactString = getDependencyId(pArtifact, removeVersion);
        formatedDirectory.append(artifactString).append(File.separatorChar);
      }
    }
    return pOutputDirectory.resolve(formatedDirectory.toString());
  }

  /**
   * Build a name from an artifact, used to store it in repository(ex: log4j-1.2.16-jar)
   * 
   * @param artifact
   *          artifact object used to get its name
   * @param removeVersion
   *          if the version must not be mentioned in the filename
   * @return name built
   */
  private String getDependencyId(final Artifact pArtifact, final boolean pRemoveVersion)
  {
    final StringBuilder dependencyId = new StringBuilder();

    dependencyId.append(pArtifact.getArtifactId());

    if (StringUtils.isNotEmpty(pArtifact.getClassifier()))
    {
      dependencyId.append("-");
      dependencyId.append(pArtifact.getClassifier());
    }

    if (!pRemoveVersion)
    {
      dependencyId.append("-");
      dependencyId.append(pArtifact.getVersion());
      dependencyId.append("-");
      dependencyId.append(pArtifact.getType());
    }
    else
    {
      // if the classifier and type are the same (sources), then don't
      // repeat.
      // avoids names like foo-sources-sources
      if (!StringUtils.equals(pArtifact.getClassifier(), pArtifact.getType()))
      {
        dependencyId.append("-");
        dependencyId.append(pArtifact.getType());
      }
    }
    return dependencyId.toString();
  }

  /**
   * @return Returns the project.
   * @throws BeaverException
   */
  public BeaverMavenProject getProject() throws BeaverException
  {
    if (beaverMavenProject == null)
    {
      beaverMavenProject = new BeaverMavenProject(project);
    }
    return beaverMavenProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MavenProject getMavenProject()
  {
    return project;
  }

  /**
   * @return the repository
   */
  public RepositorySystem getRepositorySystem()
  {
    return repositorySystem;
  }

  /**
   * @return Returns the local.
   */
  public ArtifactRepository getLocalRepository()
  {
    return localRepository;
  }

  /**
   * @return Returns the remoteRepos.
   */
  public List<ArtifactRepository> getRemoteRepos()
  {
    return remoteRepos;
  }

  /**
   * @return the dependencyTreeBuilder
   */
  public DependencyGraphBuilder getDependencyGraphBuilder()
  {
    return dependencyGraphBuilder;
  }

  /**
   * @return the projectBuilder
   */
  public ProjectBuilder getProjectBuilder()
  {
    return projectBuilder;
  }

  /**
   * @return the outputAbsoluteArtifactFilename
   */
  public boolean isOutputAbsoluteArtifactFilename()
  {
    return outputAbsoluteArtifactFilename;
  }

  /**
   * @return the remoteVersion
   */
  public boolean isRemoteVersion()
  {
    return remoteVersion;
  }

  /**
   * @return the useSubDirectoryPerType
   */
  public boolean isUseSubDirectoryPerType()
  {
    return useSubDirectoryPerType;
  }

  /**
   * @return the useSubDirectoryPerArtifact
   */
  public boolean isUseSubDirectoryPerArtifact()
  {
    return useSubDirectoryPerArtifact;
  }

  /**
   * @return the useRepositoryLayout
   */
  public boolean isUseRepositoryLayout()
  {
    return useRepositoryLayout;
  }

}
