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
import org.novaforge.forge.core.organization.entity.ProjectEntity;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.ProjectOptionsImpl;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

/**
 * @author blachonm
 */
public class ProjectDAOImplTest extends OrganizationJPATestCase
{

  static final String    PROJECT_NAME1       = "project1";
  static final String    PROJECT_ID1         = "project1";
  static final String    PROJECT_NAME2       = "project2";
  static final String    PROJECT_ID2         = "project2";
  static final String    PROJECT_NAME3       = "project3";
  static final String    PROJECT_ID3         = "project3";
  static final String    DESCRIPTION         = "description";
  static final String    UPDATED_DESCRIPTION = "description updated";
  static final String    LICENCE_TYPE        = "no licence";
  /*
   * constants declaration
   */
  private ProjectDAOImpl projectDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    projectDAOImpl = new ProjectDAOImpl();
    projectDAOImpl.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    projectDAOImpl = null;
  }

  /**
   * Test method for {@link org.novaforge.forge.core.organization.internal.dao.ProjectDAOImpl#newProject()}.
   */
  @Test
  public void testNewProject()
  {
    final Project newProject = projectDAOImpl.newProject();
    assertNotNull(newProject);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectDAOImpl#findByProjectId(java.lang.String)}
   * .
   */
  @Test
  public void testFindByProjectId()
  {
    final ProjectEntity entityToFound = buildEntity(PROJECT_NAME1, PROJECT_ID1, DESCRIPTION, LICENCE_TYPE,
        ProjectStatus.TO_BE_VALIDATED);
    final ProjectEntity entity = buildEntity(PROJECT_NAME2, PROJECT_ID2, DESCRIPTION, LICENCE_TYPE,
        ProjectStatus.TO_BE_VALIDATED);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkProjectNummber(2);
    final Project project = projectDAOImpl.findByProjectId(PROJECT_ID1);
    assertNotNull(project);
    assertThat(project.getName(), is(PROJECT_NAME1));
  }

  private ProjectEntity buildEntity(final String pProjectName, final String pProjectId, final String pDescription,
                                    final String pLicenceType, final ProjectStatus pProjectStatus)
  {
    final ProjectEntity entity = new ProjectEntity();
    entity.setName(pProjectName);
    entity.setProjectId(pProjectId);
    entity.setDescription(pDescription);
    entity.setLicenceType(pLicenceType);
    entity.setRealmType(RealmType.USER);
    entity.setStatus(pProjectStatus);
    entity.setPrivateVisibility(true);
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

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectDAOImpl#findAllByStatus(org.novaforge.forge.core.organization.model.enumerations.ProjectStatus)}
   * .
   */
  @Test
  public void testFindAllByStatus()
  {
    final ProjectEntity entityToFound = buildEntity(PROJECT_NAME1, PROJECT_ID1, DESCRIPTION, LICENCE_TYPE,
        ProjectStatus.TO_BE_VALIDATED);
    final ProjectEntity entityToFound2 = buildEntity(PROJECT_NAME3, PROJECT_ID3, DESCRIPTION, LICENCE_TYPE,
        ProjectStatus.TO_BE_VALIDATED);
    entityToFound2.setRealmType(RealmType.SYSTEM);
    entityToFound2.setStatus(ProjectStatus.TO_BE_VALIDATED);
    final ProjectEntity entity = buildEntity(PROJECT_NAME2, PROJECT_ID2, DESCRIPTION, LICENCE_TYPE,
        ProjectStatus.TO_BE_VALIDATED);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entityToFound2);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkProjectNummber(3);
    final List<Project> list = projectDAOImpl.findAllByStatus(ProjectStatus.TO_BE_VALIDATED);
    assertNotNull(list);
    assertThat(list.size(), is(2));

    // Check if the previous event have been correctly persisted
    checkProjectNummber(3);
    final List<Project> list2 = projectDAOImpl.findAllByStatus((ProjectOptions) null,
        ProjectStatus.TO_BE_VALIDATED);
    assertNotNull(list2);
    assertThat(list2.size(), is(2));

    // Check if the previous event have been correctly persisted
    checkProjectNummber(3);
    final ProjectOptions opts = new ProjectOptionsImpl();
    opts.setRetrievedSystem(false);
    final List<Project> list3 = projectDAOImpl.findAllByStatus(opts, ProjectStatus.TO_BE_VALIDATED);
    assertNotNull(list3);
    assertThat(list3.size(), is(2));

    // Check if the previous event have been correctly persisted
    checkProjectNummber(3);
    opts.setRetrievedSystem(true);
    final List<Project> list4 = projectDAOImpl.findAllByStatus(opts, ProjectStatus.TO_BE_VALIDATED);
    assertNotNull(list4);
    assertThat(list4.size(), is(3));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectDAOImpl#persist(org.novaforge.forge.core.organization.model.Project)}
   * .
   */
  @Test
  public void testPersist()
  {
    final ProjectEntity entityToFound = buildEntity(PROJECT_NAME1, PROJECT_ID1, DESCRIPTION, LICENCE_TYPE,
        ProjectStatus.TO_BE_VALIDATED);
    em.getTransaction().begin();
    projectDAOImpl.persist(entityToFound);
    em.getTransaction().commit();
    checkProjectNummber(1);
    // get project1 and update it
    final Project project1 = projectDAOImpl.findByProjectId(PROJECT_ID1);
    assertNotNull(project1);
    assertNotNull(project1.getCreated());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectDAOImpl#update(org.novaforge.forge.core.organization.model.Project)}
   * .
   * 
   * @throws InterruptedException
   */
  @Test
  public void testUpdate() throws InterruptedException
  {
    final ProjectEntity entityToFound = buildEntity(PROJECT_NAME1, PROJECT_ID1, DESCRIPTION, LICENCE_TYPE,
        ProjectStatus.TO_BE_VALIDATED);
    final ProjectEntity entity = buildEntity(PROJECT_NAME2, PROJECT_ID2, DESCRIPTION, LICENCE_TYPE,
        ProjectStatus.TO_BE_VALIDATED);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.flush();
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkProjectNummber(2);
    // get project1 and update it
    Project project1 = projectDAOImpl.findByProjectId(PROJECT_ID1);
    // updating 1
    Date dateSource = project1.getLastModified();
    project1.setDescription(UPDATED_DESCRIPTION);
    em.getTransaction().begin();
    projectDAOImpl.update(project1);
    em.getTransaction().commit();
    Project updatedProject1 = projectDAOImpl.findByProjectId(PROJECT_ID1);
    assertThat(updatedProject1.getDescription(), is(UPDATED_DESCRIPTION));
    assertNotSame(dateSource, updatedProject1.getLastModified());

    // Update 2
    Thread.sleep(2000);
    // get project1 and update it
    project1 = projectDAOImpl.findByProjectId(PROJECT_ID1);
    dateSource = project1.getLastModified();
    project1.setDescription("2");
    em.getTransaction().begin();
    projectDAOImpl.update(project1);
    em.getTransaction().commit();
    updatedProject1 = projectDAOImpl.findByProjectId(PROJECT_ID1);
    assertThat(updatedProject1.getDescription(), is("2"));
    assertNotSame(dateSource, updatedProject1.getLastModified());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.ProjectDAOImpl#delete(org.novaforge.forge.core.organization.model.Project)}
   * .
   */
  @Test
  public void testDelete()
  {
    final ProjectEntity entityToFound = buildEntity(PROJECT_NAME1, PROJECT_ID1, DESCRIPTION, LICENCE_TYPE,
        ProjectStatus.TO_BE_VALIDATED);
    final ProjectEntity entity = buildEntity(PROJECT_NAME2, PROJECT_ID2, DESCRIPTION, LICENCE_TYPE,
        ProjectStatus.TO_BE_VALIDATED);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkProjectNummber(2);
    // get project1 and update it
    final Project project1 = projectDAOImpl.findByProjectId(PROJECT_ID1);
    // updating
    project1.setDescription(UPDATED_DESCRIPTION);
    em.getTransaction().begin();
    projectDAOImpl.delete(project1);
    em.getTransaction().commit();
    checkProjectNummber(1);
  }

}
