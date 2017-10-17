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
import org.novaforge.forge.core.organization.entity.CompositionEntity;
import org.novaforge.forge.core.organization.entity.ProjectApplicationEntity;
import org.novaforge.forge.core.organization.entity.ProjectEntity;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author blachonm
 */
public class CompositionDAOImplTest extends OrganizationJPATestCase
{

  // project
  static final String            PROJECT_NAME1             = "project1";
  static final String            PROJECT_ID1               = "project1";
  static final String            PROJECT_DESCRIPTION1      = "project description1";
  // application
  static final String            APPLICATION_NAME1         = "application1";
  static final String            APPLICATION_PATH1         = "project1/application1";
  static final String            APPLICATION_NAME2         = "application2";
  static final String            APPLICATION_PATH2         = "project1/application2";
  static final ApplicationStatus APPLICATION_STATUS_ACTIVE = ApplicationStatus.ACTIVE;
  // projectApplicationRequest
  static final String            LOGIN1                    = "login1";
  // composition
  private static final String    SOURCE_APPLICATION_NAME_1 = "source_application_name_1";
  private static final String    TARGET_APPLICATION_NAME_1 = "target_application_name_1";
  private static final UUID      COMPOSITION_UUID_1        = UUID.randomUUID();
  private static final String    SOURCE_APPLICATION_NAME_2 = "source_application_name_2";
  private static final String    TARGET_APPLICATION_NAME_2 = "target_application_name_2";
  private static final UUID      COMPOSITION_UUID_2        = UUID.randomUUID();
  private static final UUID      COMPOSITION_UUID_3        = UUID.randomUUID();
  UUID pluginInstanceUUID1;
  UUID pluginUUID1;
  UUID pluginInstanceUUID2;
  UUID pluginUUID2;
  private CompositionDAOImpl compositionDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    compositionDAOImpl = new CompositionDAOImpl();
    compositionDAOImpl.setEntityManager(em);
    // initialize global variables
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
    compositionDAOImpl = null;
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.CompositionDAOImpl#findBySource(java.lang.String)}
   * .
   */
  @Test
  public void testFindBySource()
  {
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
                                                           ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.flush();
    em.getTransaction().commit();
    checkProjectNumber(1);

    // project application source
    final ProjectApplicationEntity projectApplicationEntitySource = buildProjectApplicationEntity(APPLICATION_NAME1,
                                                                                                  APPLICATION_PATH1,
                                                                                                  pluginUUID1,
                                                                                                  pluginInstanceUUID1);
    // project application target
    final ProjectApplicationEntity projectApplicationEntityTarget = buildProjectApplicationEntity(APPLICATION_NAME2,
                                                                                                  APPLICATION_PATH2,
                                                                                                  pluginUUID1,
                                                                                                  pluginInstanceUUID2);
    em.getTransaction().begin();
    final ProjectApplication projectApplicationSource = em.merge(projectApplicationEntitySource);
    final ProjectApplication projectApplicationTarget = em.merge(projectApplicationEntityTarget);
    em.flush();
    em.getTransaction().commit();
    checkProjectApplicationNumber(2);

    // composition1
    final CompositionEntity compositionEntity1 = buildCompositionEntity(project, projectApplicationSource,
                                                                        projectApplicationTarget,
                                                                        SOURCE_APPLICATION_NAME_1,
                                                                        TARGET_APPLICATION_NAME_1,
                                                                        CompositionType.NOTIFICATION,
                                                                        COMPOSITION_UUID_1);

    // composition2
    final CompositionEntity compositionEntity2 = buildCompositionEntity(project, projectApplicationSource,
                                                                        projectApplicationTarget,
                                                                        SOURCE_APPLICATION_NAME_2,
                                                                        TARGET_APPLICATION_NAME_2,
                                                                        CompositionType.NOTIFICATION,
                                                                        COMPOSITION_UUID_2);
    em.getTransaction().begin();
    final CompositionEntity composition1 = em.merge(compositionEntity1);
    em.persist(compositionEntity2);
    em.flush();
    em.getTransaction().commit();
    checkCompositionNumber(2);

    // find and check
    final String            compositionPluginInstanceId1 = composition1.getSource().getPluginInstanceUUID().toString();
    final List<Composition> list                         = compositionDAOImpl
                                                               .findBySource(compositionPluginInstanceId1);
    assertThat(list.size(), is(2));
  }

  /*
   * buiding project entity
   */
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
  private void checkProjectNumber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM ProjectEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  /*
   * building application entity
   */
  private ProjectApplicationEntity buildProjectApplicationEntity(final String pApplicationName,
      final String pApplicationPath, final UUID pPluginUUID, final UUID pPluginInstanceUUID)
  {
    final ProjectApplicationEntity projectApplicationEntity = new ProjectApplicationEntity();
    projectApplicationEntity.setName(pApplicationName);
    projectApplicationEntity.setPluginInstanceUUID(pPluginInstanceUUID);
    projectApplicationEntity.setPluginUUID(pPluginUUID);
    projectApplicationEntity.setStatus(APPLICATION_STATUS_ACTIVE);
    projectApplicationEntity.setUri(pApplicationPath);
    return projectApplicationEntity;
  }

  /**
   * Check the number of {@link ProjectApplicationEntity} persisted
   *
   * @param pNumber
   *          the number to match
   */
  private void checkProjectApplicationNumber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM ProjectApplicationEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  /*
   * building APPLICATION
   */
  private CompositionEntity buildCompositionEntity(final Project pProject,
      final ProjectApplication pSourceProjectApplication, final ProjectApplication pTargetProjectApplication,
      final String pSourceApplicationName, final String pTargetApplicationName,
      final CompositionType pCompositionType, final UUID pCompositionUUID)
  {
    final CompositionEntity compositionEntity = new CompositionEntity();
    compositionEntity.setActivated(true);
    compositionEntity.setProject(pProject);
    compositionEntity.setSource(pSourceProjectApplication);
    compositionEntity.setSourceName(pSourceApplicationName);
    compositionEntity.setTarget(pTargetProjectApplication);
    compositionEntity.setTargetName(pTargetApplicationName);
    // compositionEntity.setTemplate(template)
    compositionEntity.setType(CompositionType.NOTIFICATION);
    compositionEntity.setUUID(pCompositionUUID);

    return compositionEntity;
  }

  /**
   * Check the number of {@link CompositionEntity} persisted
   *
   * @param pNumber
   *          the number to match
   */
  private void checkCompositionNumber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM CompositionEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.CompositionDAOImpl#findBySourceAndAssociation(java.lang.String, org.novaforge.forge.core.organization.model.CompositionType, java.lang.String)}
   * .
   */
  @Test
  public void testFindBySourceAndAssociation()
  {
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
        ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.flush();
    em.getTransaction().commit();
    checkProjectNumber(1);

    // project application source
    final ProjectApplicationEntity projectApplicationEntitySource = buildProjectApplicationEntity(
        APPLICATION_NAME1, APPLICATION_PATH1, pluginUUID1, pluginInstanceUUID1);
    // project application target
    final ProjectApplicationEntity projectApplicationEntityTarget = buildProjectApplicationEntity(
        APPLICATION_NAME2, APPLICATION_PATH2, pluginUUID1, pluginInstanceUUID2);
    em.getTransaction().begin();
    final ProjectApplication projectApplicationSource = em.merge(projectApplicationEntitySource);
    final ProjectApplication projectApplicationTarget = em.merge(projectApplicationEntityTarget);
    em.flush();
    em.getTransaction().commit();
    checkProjectApplicationNumber(2);

    // composition1
    final CompositionEntity compositionEntity1 = buildCompositionEntity(project, projectApplicationSource,
        projectApplicationTarget, SOURCE_APPLICATION_NAME_1, TARGET_APPLICATION_NAME_1,
        CompositionType.NOTIFICATION, COMPOSITION_UUID_1);
    final CompositionEntity compositionEntity2 = buildCompositionEntity(project, projectApplicationSource,
        projectApplicationTarget, SOURCE_APPLICATION_NAME_2, TARGET_APPLICATION_NAME_2,
        CompositionType.NOTIFICATION, COMPOSITION_UUID_2);
    final CompositionEntity compositionEntity3 = buildCompositionEntity(project, projectApplicationSource,
        projectApplicationTarget, SOURCE_APPLICATION_NAME_1, TARGET_APPLICATION_NAME_2,
        CompositionType.NOTIFICATION, COMPOSITION_UUID_3);

    em.getTransaction().begin();
    em.persist(compositionEntity1);
    em.persist(compositionEntity2);
    em.persist(compositionEntity3);
    em.flush();
    em.getTransaction().commit();
    checkCompositionNumber(3);

    // find and check
    final String instanceId = projectApplicationSource.getPluginInstanceUUID().toString();
    final List<Composition> list = compositionDAOImpl.findBySourceAndAssociation(instanceId,
        CompositionType.NOTIFICATION, SOURCE_APPLICATION_NAME_1);
    assertThat(list.size(), is(2));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.CompositionDAOImpl#findByUUID(java.lang.String)}
   * .
   */
  @Test
  public void testFindByUUID()
  {
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
        ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.flush();
    em.getTransaction().commit();
    checkProjectNumber(1);

    // project application source
    final ProjectApplicationEntity projectApplicationEntitySource = buildProjectApplicationEntity(
        APPLICATION_NAME1, APPLICATION_PATH1, pluginUUID1, pluginInstanceUUID1);
    // project application target
    final ProjectApplicationEntity projectApplicationEntityTarget = buildProjectApplicationEntity(
        APPLICATION_NAME2, APPLICATION_PATH2, pluginUUID1, pluginInstanceUUID2);
    em.getTransaction().begin();
    final ProjectApplication projectApplicationSource = em.merge(projectApplicationEntitySource);
    final ProjectApplication projectApplicationTarget = em.merge(projectApplicationEntityTarget);
    em.flush();
    em.getTransaction().commit();
    checkProjectApplicationNumber(2);

    // composition1
    final CompositionEntity compositionEntity1 = buildCompositionEntity(project, projectApplicationSource,
        projectApplicationTarget, SOURCE_APPLICATION_NAME_1, TARGET_APPLICATION_NAME_1,
        CompositionType.NOTIFICATION, COMPOSITION_UUID_1);
    // composition2
    final CompositionEntity compositionEntity2 = buildCompositionEntity(project, projectApplicationSource,
        projectApplicationTarget, SOURCE_APPLICATION_NAME_2, TARGET_APPLICATION_NAME_2,
        CompositionType.NOTIFICATION, COMPOSITION_UUID_2);

    em.getTransaction().begin();
    em.persist(compositionEntity1);
    em.persist(compositionEntity2);
    em.flush();
    em.getTransaction().commit();
    checkCompositionNumber(2);

    final Composition compositionFound = compositionDAOImpl.findByUUID(COMPOSITION_UUID_1.toString());
    assertNotNull(compositionFound);
    assertThat(compositionFound.getSourceName(), is(SOURCE_APPLICATION_NAME_1));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.CompositionDAOImpl#update(org.novaforge.forge.core.organization.model.Composition)}
   * .
   */
  @Test
  public void testUpdate()
  {
    // project
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
        ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final Project project = em.merge(projectEntity);
    em.flush();
    em.getTransaction().commit();
    checkProjectNumber(1);

    // project application source
    final ProjectApplicationEntity projectApplicationEntitySource = buildProjectApplicationEntity(
        APPLICATION_NAME1, APPLICATION_PATH1, pluginUUID1, pluginInstanceUUID1);
    // project application target
    final ProjectApplicationEntity projectApplicationEntityTarget = buildProjectApplicationEntity(
        APPLICATION_NAME2, APPLICATION_PATH2, pluginUUID1, pluginInstanceUUID2);
    em.getTransaction().begin();
    final ProjectApplication projectApplicationSource = em.merge(projectApplicationEntitySource);
    final ProjectApplication projectApplicationTarget = em.merge(projectApplicationEntityTarget);
    em.flush();
    em.getTransaction().commit();
    checkProjectApplicationNumber(2);

    // composition1
    final CompositionEntity compositionEntity1 = buildCompositionEntity(project, projectApplicationSource,
        projectApplicationTarget, SOURCE_APPLICATION_NAME_1, TARGET_APPLICATION_NAME_1,
        CompositionType.NOTIFICATION, COMPOSITION_UUID_1);
    // composition2
    final CompositionEntity compositionEntity2 = buildCompositionEntity(project, projectApplicationSource,
        projectApplicationTarget, SOURCE_APPLICATION_NAME_2, TARGET_APPLICATION_NAME_2,
        CompositionType.NOTIFICATION, COMPOSITION_UUID_2);

    em.getTransaction().begin();
    final CompositionEntity composition1 = em.merge(compositionEntity1);
    em.persist(compositionEntity2);
    em.flush();
    em.getTransaction().commit();
    checkCompositionNumber(2);

    assertNotNull(composition1);
    composition1.setType(CompositionType.REQUEST_DATA);
    em.getTransaction().begin();
    compositionDAOImpl.update(composition1);
    em.getTransaction().commit();
    final Composition compositionFound = compositionDAOImpl.findByUUID(COMPOSITION_UUID_1.toString());
    assertNotNull(compositionFound);
    assertThat(compositionFound.getSource().getName(), is(APPLICATION_NAME1));
    assertThat(compositionFound.getTarget().getName(), is(APPLICATION_NAME2));
    assertThat(compositionFound.getSourceName(), is(SOURCE_APPLICATION_NAME_1));
    assertThat(compositionFound.getTargetName(), is(TARGET_APPLICATION_NAME_1));
    assertThat(compositionFound.getType(), is(CompositionType.REQUEST_DATA));
  }

}
