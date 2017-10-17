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
import java.nio.file.Paths;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.novaforge.beaver.deployment.plugin.deploy.model.PropertyArtifact;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;

public class NovaForgeDeployMojoTest extends AbstractMojoTestCase
{

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setUp() throws Exception
  {
    // required
    super.setUp();

  }

  /** {@inheritDoc} */
  /**
   * {@inheritDoc}
   */
  @Override
  protected void tearDown() throws Exception
  {
    // required
    super.tearDown();

  }

  /**
   * @throws Exception
   */
  public void testNovaForgeDeployMojo() throws Exception
  {
    final File pom = getTestFile("src/test/ressources/plugin-config.xml");
    assertNotNull(pom);
    assertTrue(pom.exists());

    final DeployBeaverMojo fMojo = (DeployBeaverMojo) lookupMojo("deploy", pom);
    assertNotNull(fMojo);

    assertNotNull(fMojo.getRepositorySystem());
    assertNotNull(fMojo.getDependencyGraphBuilder());
    assertNotNull(fMojo.getProjectBuilder());

  }

  public void testSettingMojoVariables() throws Exception
  {
    final File pom = getTestFile("src/test/ressources/plugin-config.xml");
    assertNotNull(pom);
    assertTrue(pom.exists());

    final DeployBeaverMojo fMojo = (DeployBeaverMojo) lookupMojo("deploy", pom);

    assertEquals("temp", (String) getVariableValueFromObject(fMojo, "tempDirectory"));
    assertEquals("logs", (String) getVariableValueFromObject(fMojo, "logsDirectory"));
    assertEquals("deployment.xml", (String) getVariableValueFromObject(fMojo, "deploymentFile"));
    assertEquals("org.novaforge.novacic.novadeployimpl.DeploymentPersistenceImpl",
        (String) getVariableValueFromObject(fMojo, "deploymentImplClass"));
  }

  public void testcreateArtifact() throws Exception
  {
    final File pom = getTestFile("src/test/ressources/plugin-config.xml");
    assertNotNull(pom);
    assertTrue(pom.exists());

    final DeployBeaverMojo fMojo = (DeployBeaverMojo) lookupMojo("deploy", pom);
    assertNotNull(fMojo);
    BeaverServices.init(fMojo, null, false, false, null);
    final String groupId = "org.test";
    final String artifactId = "test-artifact";
    final String version = "1.0";
    final String type = "zip";
    final String artifactProperty = new StringBuilder(groupId).append("/").append(artifactId).append("/")
        .append(version).append("/").append(type).toString();
    final PropertyArtifact propertyArtefact = new PropertyArtifact(artifactProperty);
    final Artifact artifact = BeaverServices.getMojoService().createArtifact(propertyArtefact);
    assertNotNull(artifact);
    assertEquals(artifact.getGroupId(), groupId);
    assertEquals(artifact.getArtifactId(), artifactId);
    assertEquals(artifact.getVersion(), version);
    assertEquals(artifact.getType(), type);
  }

  public void testgetFormattedOutputDirectory() throws Exception
  {
    final File pom = getTestFile("src/test/ressources/plugin-config.xml");
    assertNotNull(pom);
    assertTrue(pom.exists());

    final DeployBeaverMojo fMojo = (DeployBeaverMojo) lookupMojo("deploy", pom);
    assertNotNull(fMojo);
    BeaverServices.init(fMojo, null, false, false, null);
    final String groupId = "org.test";
    final String artifactId = "test-artifact";
    final String version = "1.0";
    final String type = "zip";
    final String artifactProperty = new StringBuilder(groupId).append("/").append(artifactId).append("/")
        .append(version).append("/").append(type).toString();
    final PropertyArtifact propertyArtefact = new PropertyArtifact(artifactProperty);
    final Artifact artifact = BeaverServices.getMojoService().createArtifact(propertyArtefact);
    assertNotNull(artifact);

    final Path descriptorDir = BeaverServices.getMojoService().getFormattedOutputDirectory(false, false,
        true, false, Paths.get("src/test/ressources/"), artifact);
    assertNotNull(descriptorDir);
    final boolean orgContain = descriptorDir.toString().contains("org");
    final boolean testContain = descriptorDir.toString().contains("test");
    final boolean artifactIdContain = descriptorDir.toString().contains(artifactId);
    final boolean versionContain = descriptorDir.toString().contains(version);
    assertTrue(orgContain);
    assertTrue(testContain);
    assertTrue(artifactIdContain);
    assertTrue(versionContain);
  }
}
