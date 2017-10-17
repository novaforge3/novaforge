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
/**
 * 
 */
package org.novaforge.forge.core.organization.internal.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.core.organization.entity.ProjectApplicationEntity;
import org.novaforge.forge.core.organization.entity.ProjectApplicationRequestEntity;
import org.novaforge.forge.core.organization.entity.ProjectEntity;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectApplicationRequest;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author blachonm
 */
public class ProjectApplicationRequestDAOImplTest extends OrganizationJPATestCase
{

  // project
  static final String              PROJECT_NAME1        = "project1";
  static final String              PROJECT_ID1          = "project1";
  static final String              PROJECT_DESCRIPTION1 = "project description1";

  // application
  static final String              APPLICATION_NAME1    = "application1";
  static final String              APPLICATION_PATH1    = "project1/application1";
  static final String              APPLICATION_NAME2    = "application2";
  static final String              APPLICATION_PATH2    = "project1/application2";

  // projectApplicationRequest
  static final String              LOGIN1               = "login1";
  UUID                             pluginInstanceUUID1;
  UUID                             pluginUUID1;
  UUID                             pluginInstanceUUID2;
  UUID                             pluginUUID2;
  ProjectApplicationRequestDAOImpl projectApplicationRequestDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    projectApplicationRequestDAOImpl = new ProjectApplicationRequestDAOImpl();
    projectApplicationRequestDAOImpl.setEntityManager(em);
    pluginInstanceUUID1 = UUID.randomUUID();
    pluginUUID1 = UUID.randomUUID();
    pluginInstanceUUID2 = UUID.randomUUID();
    pluginUUID2 = UUID.randomUUID();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    projectApplicationRequestDAOImpl = null;
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectApplicationRequestDAOImpl#findByProjectAndApp(java.lang.String, java.util.UUID)}
   * .
   */
  @Test
  public void testFindByProjectAndApp()
  {
    final HashMap<String, String> roleMapping = createRoleMapping();
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
                                                           ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.getTransaction().commit();
    checkProjectNummber(1);

    final ProjectApplicationRequestEntity ProjectApplicationRequestEntity = buildProjectApplicationRequestEntity(project,
                                                                                                                 LOGIN1,
                                                                                                                 APPLICATION_NAME1,
                                                                                                                 APPLICATION_PATH1,
                                                                                                                 pluginUUID1,
                                                                                                                 pluginInstanceUUID1,
                                                                                                                 roleMapping);
    checkProjectApplicationNummber(1);
    em.getTransaction().begin();
    projectApplicationRequestDAOImpl.persist(ProjectApplicationRequestEntity);
    em.getTransaction().commit();
    checkProjectApplicationRequestNumber(1);
    final ProjectApplicationRequest projectApplicationRequest = projectApplicationRequestDAOImpl
                                                                    .findByProjectAndApp(PROJECT_ID1,
                                                                                         pluginInstanceUUID1);
    assertNotNull(projectApplicationRequest);
  }

  /**
   * @return roleMapping
   */
  private HashMap<String, String> createRoleMapping()
  {
    final HashMap<String, String> roleMapping = new HashMap<String, String>();
    roleMapping.put("roleA1", "roleP1");
    roleMapping.put("roleA2", "roleP2");
    return roleMapping;
  }

  private ProjectEntity buildProjectEntity(final String pProjectName, final String pProjectId,
      final String pDescription, final ProjectStatus pProjectStatus)
  {
    final ProjectEntity entity = new ProjectEntity();
    entity.setName(pProjectName);
    entity.setProjectId(pProjectId);
    entity.setDescription(pDescription);
    entity.setCreated(new Date());
    entity.setStatus(pProjectStatus);
    return entity;
  }

  /**
   * Check the number of {@link ProjectEntity} persisted
   *
   * @param pNumber
   *     the number to match
   */
  private void checkProjectNummber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM ProjectEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  private ProjectApplicationRequestEntity buildProjectApplicationRequestEntity(final Project project,
      final String pLogin, final String pApplicationName, final String pApplicationPath,
      final UUID pPluginUUID, final UUID pPluginInstanceUUID, final Map<String, String> pRoleMapping)
  {
    // persist application
    final ProjectApplicationEntity projectApplication = buildProjectApplicationEntity(pApplicationName,
        pApplicationPath, pPluginUUID, pPluginInstanceUUID);
    em.getTransaction().begin();
    final ProjectApplication application = em.merge(projectApplication);
    em.getTransaction().commit();

    // projectApplicationRequestEntity
    final ProjectApplicationRequestEntity entity = new ProjectApplicationRequestEntity();
    entity.setApplication(application);
    entity.setLogin(pLogin);
    entity.setProject(project);
    entity.setRolesMapping(pRoleMapping);
    return entity;
  }

  /**
   * Check the number of {@link ProjectApplicationEntity} persisted
   *
   * @param pNumber
   *          the number to match
   */
  private void checkProjectApplicationNummber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM ProjectApplicationEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  /**
   * Check the number of {@link ProjectApplicationRequestEntity} persisted
   *
   * @param pNumber
   *          the number to match
   */
  private void checkProjectApplicationRequestNumber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM ProjectApplicationRequestEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  private ProjectApplicationEntity buildProjectApplicationEntity(final String pApplicationName,
                                                                 final String pApplicationPath, final UUID pPluginUUID,
                                                                 final UUID pPluginInstanceUUID)
  {
    final ProjectApplicationEntity projectApplicationEntity = new ProjectApplicationEntity();
    projectApplicationEntity.setName(pApplicationName);
    projectApplicationEntity.setPluginInstanceUUID(pPluginInstanceUUID);
    projectApplicationEntity.setPluginUUID(pPluginUUID);
    projectApplicationEntity.setStatus(ApplicationStatus.ACTIVE);
    projectApplicationEntity.setUri(pApplicationPath);
    return projectApplicationEntity;
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectApplicationRequestDAOImpl#findByProjectAndPlugin(java.lang.String, java.util.UUID)}
   * .
   */
  @Test
  public void testFindByProjectAndPlugin()
  {
    final HashMap<String, String> roleMapping = createRoleMapping();
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
        ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.getTransaction().commit();
    checkProjectNummber(1);

    final ProjectApplicationRequestEntity ProjectApplicationRequestEntity = buildProjectApplicationRequestEntity(
        project, LOGIN1, APPLICATION_NAME1, APPLICATION_PATH1, pluginUUID1, pluginInstanceUUID1, roleMapping);
    checkProjectApplicationNummber(1);
    em.getTransaction().begin();
    projectApplicationRequestDAOImpl.persist(ProjectApplicationRequestEntity);
    em.getTransaction().commit();
    checkProjectApplicationRequestNumber(1);
    final List<ProjectApplicationRequest> list = projectApplicationRequestDAOImpl.findByProjectAndPlugin(
        PROJECT_ID1, pluginUUID1);
    assertThat(list.size(), is(1));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectApplicationRequestDAOImpl#findByPlugin(java.util.UUID)}
   * .
   */
  @Test
  public void testFindByPlugin()
  {
    final HashMap<String, String> roleMapping = createRoleMapping();
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
        ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.getTransaction().commit();
    checkProjectNummber(1);

    final ProjectApplicationRequestEntity ProjectApplicationRequestEntity = buildProjectApplicationRequestEntity(
        project, LOGIN1, APPLICATION_NAME1, APPLICATION_PATH1, pluginUUID1, pluginInstanceUUID1, roleMapping);
    checkProjectApplicationNummber(1);
    em.getTransaction().begin();
    projectApplicationRequestDAOImpl.persist(ProjectApplicationRequestEntity);
    em.getTransaction().commit();
    checkProjectApplicationRequestNumber(1);
    final List<ProjectApplicationRequest> list = projectApplicationRequestDAOImpl.findByPlugin(pluginUUID1);
    assertThat(list.size(), is(1));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectApplicationRequestDAOImpl#findOldestByPlugin(java.util.UUID)}
   * .
   */
  @Test
  public void testFindOldestByPlugin()
  {
    final HashMap<String, String> roleMapping = createRoleMapping();
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
        ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.getTransaction().commit();
    checkProjectNummber(1);

    final ProjectApplicationRequestEntity ProjectApplicationRequestEntity1 = buildProjectApplicationRequestEntity(
        project, LOGIN1, APPLICATION_NAME1, APPLICATION_PATH1, pluginUUID1, pluginInstanceUUID1, roleMapping);
    final ProjectApplicationRequestEntity ProjectApplicationRequestEntity2 = buildProjectApplicationRequestEntity(
        project, LOGIN1, APPLICATION_NAME2, APPLICATION_PATH2, pluginUUID1, pluginInstanceUUID2, roleMapping);
    checkProjectApplicationNummber(2);

    em.getTransaction().begin();
    projectApplicationRequestDAOImpl.persist(ProjectApplicationRequestEntity1);
    projectApplicationRequestDAOImpl.persist(ProjectApplicationRequestEntity2);
    em.getTransaction().commit();
    checkProjectApplicationRequestNumber(2);
    final ProjectApplicationRequest projectApplicationRequest = projectApplicationRequestDAOImpl
        .findOldestByPlugin(pluginUUID1);
    assertNotNull(projectApplicationRequest);
    final UUID pluginInstanceUUID = projectApplicationRequest.getApplication().getPluginInstanceUUID();
    assertThat(pluginInstanceUUID, is(pluginInstanceUUID1));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectApplicationRequestDAOImpl#findByProject(java.lang.String)}
   * .
   */
  @Test
  public void testFindByProject()
  {
    final HashMap<String, String> roleMapping = createRoleMapping();
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
        ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.getTransaction().commit();
    checkProjectNummber(1);

    final ProjectApplicationRequestEntity ProjectApplicationRequestEntity = buildProjectApplicationRequestEntity(
        project, LOGIN1, APPLICATION_NAME1, APPLICATION_PATH1, pluginUUID1, pluginInstanceUUID1, roleMapping);
    checkProjectApplicationNummber(1);
    em.getTransaction().begin();
    projectApplicationRequestDAOImpl.persist(ProjectApplicationRequestEntity);
    em.getTransaction().commit();
    checkProjectApplicationRequestNumber(1);
    final List<ProjectApplicationRequest> list = projectApplicationRequestDAOImpl.findByProject(PROJECT_ID1);
    assertThat(list.size(), is(1));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectApplicationRequestDAOImpl#persist(org.novaforge.forge.core.organization.model.ProjectApplicationRequest)}
   * .
   */
  @Test
  public void testPersist()
  {
    final HashMap<String, String> roleMapping = createRoleMapping();
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
        ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.getTransaction().commit();
    checkProjectNummber(1);

    final ProjectApplicationRequestEntity ProjectApplicationRequestEntity = buildProjectApplicationRequestEntity(
        project, LOGIN1, APPLICATION_NAME1, APPLICATION_PATH1, pluginUUID1, pluginInstanceUUID1, roleMapping);
    checkProjectApplicationNummber(1);
    em.getTransaction().begin();
    projectApplicationRequestDAOImpl.persist(ProjectApplicationRequestEntity);
    em.getTransaction().commit();
    checkProjectApplicationRequestNumber(1);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectApplicationRequestDAOImpl#delete(org.novaforge.forge.core.organization.model.ProjectApplicationRequest)}
   * .
   */
  @Test
  public void testDelete()
  {
    final HashMap<String, String> roleMapping = createRoleMapping();
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
        ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.flush();
    em.getTransaction().commit();
    checkProjectNummber(1);

    final ProjectApplicationRequestEntity projectApplicationRequestEntity = buildProjectApplicationRequestEntity(
        project, LOGIN1, APPLICATION_NAME1, APPLICATION_PATH1, pluginUUID1, pluginInstanceUUID1, roleMapping);
    em.getTransaction().begin();
    projectApplicationRequestDAOImpl.persist(projectApplicationRequestEntity);
    em.getTransaction().commit();
    checkProjectApplicationRequestNumber(1);
    em.getTransaction().begin();
    final ProjectApplicationRequest persist = projectApplicationRequestDAOImpl
        .findOldestByPlugin(pluginUUID1);
    projectApplicationRequestDAOImpl.delete(persist);
    em.getTransaction().commit();
    checkProjectApplicationRequestNumber(0);
  }

}
