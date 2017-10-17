/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.core.organization.internal.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.core.organization.entity.ProjectApplicationEntity;
import org.novaforge.forge.core.organization.entity.SpaceEntity;
import org.novaforge.forge.core.organization.entity.TemplateApplicationEntity;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectElement;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.enumerations.NodeType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NodeDAOImplTest extends OrganizationJPATestCase
{

  private NodeDAOImpl nodeDAO;

  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    nodeDAO = new NodeDAOImpl();
    nodeDAO.setEntityManager(em);
  }

  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    nodeDAO = null;
  }

  @Test
  public void testFindByUri()
  {
    final String uri = "space1";
    final String name = "space1";
    final SpaceEntity node = DataCreator.createSpaceNode(uri, name);
    em.getTransaction().begin();
    em.persist(node);
    em.getTransaction().commit();
    try
    {
      final Node result = nodeDAO.findByUri(uri);
      assertNotNull(result);
      assertThat(result.getUri(), is(uri));
      assertThat(result.getName(), is(name));

    }
    catch (final Exception e)
    {
      e.printStackTrace();
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  @Test
  public void testFindByProjectElement()
  {
    final String uri = "space1";
    final String name = "space1";
    final String projectElementId = "element1";
    final SpaceEntity node = DataCreator.createSpaceNode(uri, name);
    final ProjectElement project = DataCreator.createProjectElement(projectElementId);

    em.getTransaction().begin();
    final ProjectElement merged = em.merge(project);
    node.setProjectElement(merged);
    em.persist(node);
    em.getTransaction().commit();
    try
    {
      final List<Space> result = nodeDAO.findByProjectElement(projectElementId);
      assertNotNull(result);
      assertThat(result.size(), is(1));
      final Space space = result.get(0);
      assertThat(space.getUri(), is(uri));
      assertThat(space.getName(), is(name));

    }
    catch (final Exception e)
    {
      e.printStackTrace();
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  @Test
  public void testFindSpacesWithAppsForElementId()
  {
    final String uri = "space1";
    final String name = "space1";
    final String projectElementId = "element1";

    final String uri1 = "application1";
    final String name1 = "application1";
    final UUID pluginInstanceUUID1 = UUID.randomUUID();
    final String uri2 = "application2";
    final String name2 = "application2";
    final UUID pluginInstanceUUID2 = UUID.randomUUID();

    final SpaceEntity node = DataCreator.createSpaceNode(uri, name);
    final ProjectElement project = DataCreator.createProjectElement(projectElementId);

    final Application application1 = DataCreator.createProjectApplication(uri1, name1, pluginInstanceUUID1);

    final Application application2 = DataCreator.createProjectApplication(uri2, name2, pluginInstanceUUID2);

    em.getTransaction().begin();
    // Save Project Element
    final ProjectElement createdProj = em.merge(project);
    // Save App 1
    final Application createdApp1 = em.merge(application1);
    // Save App 2
    final Application createdApp2 = em.merge(application2);
    // Save Node
    node.setProjectElement(createdProj);
    node.addApplication(createdApp1);
    node.addApplication(createdApp2);
    em.persist(node);
    em.getTransaction().commit();
    try
    {
      final Map<Space, List<Application>> result = nodeDAO.findSpacesWithAppsForElementId(projectElementId);
      assertNotNull(result);
      assertThat(result.size(), is(1));
      // Check space.
      final Space space = result.keySet().iterator().next();
      assertThat(space.getUri(), is(uri));
      assertThat(space.getName(), is(name));

      // Check Applications
      final List<Application> apps = result.get(space);
      assertNotNull(apps);
      assertThat(apps.size(), is(2));

    }
    catch (final Exception e)
    {
      e.printStackTrace();
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  @Test
  public void testUpdateSpace()
  {
    final String uri = "space1";
    final String uri2 = "space2";
    final String name = "space1";
    final String name2 = "space2";
    final SpaceEntity node = DataCreator.createSpaceNode(uri, name);
    em.getTransaction().begin();
    em.persist(node);
    em.getTransaction().commit();
    final Node created = nodeDAO.findByUri(uri);
    // Update the created Space

    created.setUri(uri2);
    created.setName(name2);
    em.getTransaction().begin();
    final Node result = nodeDAO.update((Space) created);
    em.getTransaction().commit();
    assertNotNull(result);
    assertThat(result.getUri(), is(uri2));
    assertThat(result.getName(), is(name2));

  }

  @Test
  public void findSpaceForApp() throws SpaceServiceException
  {
    final String spaceUri = "space1";
    final String spaceName = "space1";
    final SpaceEntity space = DataCreator.createSpaceNode(spaceUri, spaceName);
    em.getTransaction().begin();
    em.persist(space);
    em.getTransaction().commit();

    final String uri = "uri";
    final String name = "name";
    final UUID pluginInstanceUUID = UUID.randomUUID();
    final ProjectApplicationEntity app = DataCreator.createProjectApplication(uri, name, pluginInstanceUUID);
    em.getTransaction().begin();
    nodeDAO.addApplicationToSpace(spaceUri, app);
    em.getTransaction().commit();
    final Node parentSpace = nodeDAO.findSpaceForApp(uri);

    assertNotNull(parentSpace);
    assertThat(parentSpace.getUri(), is(spaceUri));
    assertThat(parentSpace.getName(), is(spaceName));

  }

  @Test
  public void testFindByInstanceId()
  {
    final String uri = "uri";
    final String name = "name";
    final UUID pluginInstanceUUID = UUID.randomUUID();
    final ProjectApplicationEntity node = DataCreator.createProjectApplication(uri, name, pluginInstanceUUID);
    em.getTransaction().begin();
    em.persist(node);
    em.getTransaction().commit();

    final ProjectApplication result = nodeDAO.findByInstanceId(pluginInstanceUUID);
    assertNotNull(result);
    assertThat(result.getUri(), is(uri));
    assertThat(result.getName(), is(name));

  }

  @Test
  public void testUpdateApplicationStatus()
  {
    final String uri = "uri";
    final String name = "name";
    final UUID pluginInstanceUUID = UUID.randomUUID();
    final ProjectApplicationEntity node = DataCreator.createProjectApplication(uri, name, pluginInstanceUUID);
    node.setStatus(ApplicationStatus.CREATE_IN_PROGRESS);
    em.getTransaction().begin();
    em.persist(node);
    em.flush();
    em.getTransaction().commit();
    final ProjectApplication created = nodeDAO.findByInstanceId(pluginInstanceUUID);
    assertNotNull(created);
    assertThat(created.getUri(), is(uri));
    assertThat(created.getName(), is(name));
    assertThat(created.getStatus(), is(ApplicationStatus.CREATE_IN_PROGRESS));
    em.getTransaction().begin();
    nodeDAO.updateApplicationStatus(ApplicationStatus.ACTIVE, pluginInstanceUUID);

    em.getTransaction().commit();
    final ProjectApplication updated = nodeDAO.findByInstanceId(pluginInstanceUUID);
    assertNotNull(updated);
    assertThat(updated.getUri(), is(uri));
    assertThat(updated.getName(), is(name));
    assertThat(updated.getStatus(), is(ApplicationStatus.ACTIVE));

  }

  @Test
  public void testUpdateProjectApplication()
  {
    final String uri = "uri";
    final String uri2 = "uri2";
    final String name = "name";
    final String name2 = "name2";
    final UUID pluginInstanceUUID = UUID.randomUUID();
    final ProjectApplicationEntity node = DataCreator.createProjectApplication(uri, name, pluginInstanceUUID);
    em.getTransaction().begin();
    em.persist(node);
    em.flush();
    em.getTransaction().commit();
    final Node created = nodeDAO.findByUri(uri);
    created.setUri(uri2);
    created.setName(name2);
    em.getTransaction().begin();
    final Node result = nodeDAO.update((ProjectApplicationEntity) created);
    em.flush();
    em.getTransaction().commit();
    assertNotNull(result);
    assertThat(result.getUri(), is(uri2));
    assertThat(result.getName(), is(name2));
  }

  @Test
  public void testNewNode()
  {
    Node result = nodeDAO.newNode(NodeType.Space);
    assertNotNull(result);
    assertTrue(result instanceof SpaceEntity);

    result = nodeDAO.newNode(NodeType.ProjectApplication);
    assertNotNull(result);
    assertTrue(result instanceof ProjectApplicationEntity);

    result = nodeDAO.newNode(NodeType.TemplateApplication);
    assertNotNull(result);
    assertTrue(result instanceof TemplateApplicationEntity);

  }

  @Test
  public void testFindAppsForSpace()
  {
    final String uri = "space1";
    final String name = "space1";

    final String uri1 = "application1";
    final String name1 = "application1";
    final UUID pluginInstanceUUID1 = UUID.randomUUID();
    final String uri2 = "application2";
    final String name2 = "application2";
    final UUID pluginInstanceUUID2 = UUID.randomUUID();

    final SpaceEntity node = DataCreator.createSpaceNode(uri, name);

    final Application application1 = DataCreator.createProjectApplication(uri1, name1, pluginInstanceUUID1);

    final Application application2 = DataCreator.createProjectApplication(uri2, name2, pluginInstanceUUID2);

    em.getTransaction().begin();
    node.addApplication(application1);
    node.addApplication(application2);
    em.persist(node);
    em.flush();
    em.getTransaction().commit();
    final List<Application> apps = nodeDAO.findAppsForSpace(uri);

    assertNotNull(apps);
    assertThat(apps.size(), is(2));
    final Application app1 = apps.get(0);
    assertNotNull(app1.getUri());
    assertTrue(app1.getUri().equals(uri1) || app1.getUri().equals(uri2));

    final Application app2 = apps.get(1);
    assertNotNull(app2.getUri());
    assertTrue(app2.getUri().equals(uri1) || app2.getUri().equals(uri2));

  }

  @Test
  public void testAddApplicationToSpace() throws SpaceServiceException
  {
    final String uri = "space1";
    final String name = "space1";

    final String name1 = "application1";
    final UUID pluginInstanceUUID1 = UUID.randomUUID();
    final String name2 = "application2";
    final UUID pluginInstanceUUID2 = UUID.randomUUID();

    final SpaceEntity node = DataCreator.createSpaceNode(uri, name);

    final Application application1 = DataCreator.createProjectApplication("", name1, pluginInstanceUUID1);

    final Application application2 = DataCreator.createProjectApplication("", name2, pluginInstanceUUID2);

    em.getTransaction().begin();
    em.persist(node);
    em.flush();
    em.getTransaction().commit();

    em.getTransaction().begin();
    nodeDAO.addApplicationToSpace(uri, application1);
    nodeDAO.addApplicationToSpace(uri, application2);
    em.flush();
    em.getTransaction().commit();

    final SpaceEntity result = (SpaceEntity) nodeDAO.findByUri(uri);
    assertNotNull(result);
    assertThat(result.getUri(), is(uri));
    assertThat(result.getName(), is(name));
    // Check space.
    final List<Application> apps = result.getApplications();
    assertNotNull(apps);
    assertThat(apps.size(), is(2));
    final Application app1 = apps.get(0);
    assertNotNull(app1.getUri());
    assertTrue(app1.getUri().equals(uri + "/" + app1.getName()));
    final Application app2 = apps.get(1);
    assertNotNull(app2.getUri());
    assertTrue(app2.getUri().equals(uri + "/" + app2.getName()));

  }

  @Test
  public void testDeleteApplication() throws SpaceServiceException, ApplicationServiceException
  {
    final String uri = "space1";
    final String name = "space1";

    final String uri1 = "application1";
    final String name1 = "application1";
    final UUID pluginInstanceUUID1 = UUID.randomUUID();
    final String uri2 = "application2";
    final String name2 = "application2";
    final UUID pluginInstanceUUID2 = UUID.randomUUID();

    final SpaceEntity node = DataCreator.createSpaceNode(uri, name);

    final Application application1 = DataCreator.createProjectApplication(uri1, name1, pluginInstanceUUID1);

    final Application application2 = DataCreator.createProjectApplication(uri2, name2, pluginInstanceUUID2);

    em.getTransaction().begin();
    node.addApplication(application1);
    node.addApplication(application2);
    em.persist(node);
    em.flush();
    em.getTransaction().commit();

    em.getTransaction().begin();
    nodeDAO.deleteApplication(uri, uri1);
    em.getTransaction().commit();

    final SpaceEntity result = (SpaceEntity) nodeDAO.findByUri(uri);
    assertNotNull(result);
    assertThat(result.getUri(), is(uri));
    assertThat(result.getName(), is(name));
    // Check space.
    final List<Application> apps = result.getApplications();
    assertNotNull(apps);
    assertThat(apps.size(), is(1));
    final Application app = apps.get(0);
    assertNotNull(app.getUri());
    assertTrue(app.getUri().equals(uri2));

  }

  @Test
  public void testBuildUri()
  {
    try
    {
      final String uri = nodeDAO.buildUri("", "name");
      assertThat(uri, is("/name"));
    }
    catch (final Exception e)
    {
      e.printStackTrace();
      fail("Should not throw exception: " + e.getMessage());
    }
  }

}
