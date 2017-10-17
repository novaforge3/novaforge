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
import org.novaforge.forge.core.organization.entity.TemplateEntity;
import org.novaforge.forge.core.organization.entity.TemplateInstanceEntity;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateInstance;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author blachonm
 */
public class TemplateDAOImplTest extends OrganizationJPATestCase
{

  static final TemplateProjectStatus TEMPLATE_STATUS_IN_PROGRESS = TemplateProjectStatus.IN_PROGRESS;
  static final TemplateProjectStatus TEMPLATE_STATUS_ENABLE      = TemplateProjectStatus.ENABLE;
  static final String                TEMPLATE_NAME1              = "template1";
  static final String                TEMPLATE_NAME2              = "template2";
  static final String                TEMPLATE_ELEMENT_ID1        = "template1";
  static final String                TEMPLATE_ELEMENT_ID2        = "template2";
  static final String                TEMPLATE_DESCRIPTION1       = "description";
  static final String                TEMPLATE_DESCRIPTION2       = "description";
  static final String                PROJECT_NAME1               = "project1";
  static final String                PROJECT_ID1                 = "project1";
  static final String                PROJECT_DESCRIPTION1        = "project description1";
  /*
   * constants declaration
   */
  private TemplateDAOImpl templateDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    templateDAOImpl = new TemplateDAOImpl();
    templateDAOImpl.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    templateDAOImpl = null;
  }

  /**
   * Test method for {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#newTemplate()}.
   */
  @Test
  public void testNewTemplate()
  {
    final Template newTemplate = templateDAOImpl.newTemplate();
    assertNotNull(newTemplate);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#getTemplatesByStatus(org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus)}
   * .
   */
  @Test
  public void testGetTemplatesByStatus()
  {
    final TemplateEntity entityToFound = buildTemplateEntity(TEMPLATE_NAME1, TEMPLATE_ELEMENT_ID1,
        TEMPLATE_STATUS_ENABLE);
    final TemplateEntity entity = buildTemplateEntity(TEMPLATE_NAME2, TEMPLATE_ELEMENT_ID2,
        TEMPLATE_STATUS_IN_PROGRESS);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkTemplateNummber(2);
    final List<Template> list = templateDAOImpl.getTemplatesByStatus(TEMPLATE_STATUS_ENABLE);
    assertThat(list.size(), is(1));
    assertThat(list.get(0).getName(), is(TEMPLATE_NAME1));
  }

  private TemplateEntity buildTemplateEntity(final String pName, final String pElementId,
                                             final TemplateProjectStatus pTemplateProjectStatus)
  {
    final TemplateEntity entity = new TemplateEntity();
    entity.setName(pName);
    entity.setDescription(TEMPLATE_DESCRIPTION1);
    entity.setStatus(pTemplateProjectStatus);
    entity.setElementId(pElementId);
    return entity;
  }

  /**
   * Check the number of {@link TemplateEntity} persisted
   *
   * @param pNumber
   *          the number to match
   */
  private void checkTemplateNummber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM TemplateEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#findByTemplateId(java.lang.String)}
   * .
   */
  @Test
  public void testFindByTemplateId()
  {
    final TemplateEntity entityToFound = buildTemplateEntity(TEMPLATE_NAME1, TEMPLATE_ELEMENT_ID1,
        TEMPLATE_STATUS_ENABLE);
    final TemplateEntity entity = buildTemplateEntity(TEMPLATE_NAME2, TEMPLATE_ELEMENT_ID2,
        TEMPLATE_STATUS_ENABLE);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();

    // Check if the previous event have been correctly persisted
    checkTemplateNummber(2);
    final Template template = templateDAOImpl.findByTemplateId(TEMPLATE_ELEMENT_ID1);
    assertNotNull(template);
    assertThat(template.getName(), is(TEMPLATE_NAME1));
  }

  /**
   * Test method for {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#findAll()}.
   */
  @Test
  public void testFindAll()
  {
    final TemplateEntity entityToFound = buildTemplateEntity(TEMPLATE_NAME1, TEMPLATE_ELEMENT_ID1,
        TEMPLATE_STATUS_ENABLE);
    final TemplateEntity entity = buildTemplateEntity(TEMPLATE_NAME2, TEMPLATE_ELEMENT_ID2,
        TEMPLATE_STATUS_ENABLE);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkTemplateNummber(2);
    final List<Template> list = templateDAOImpl.findAll();
    assertNotNull(list);
    assertThat(list.size(), is(2));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#persist(org.novaforge.forge.core.organization.model.Template)}
   * .
   */
  @Test
  public void testPersistTemplate()
  {
    final TemplateEntity entityToFound = buildTemplateEntity(TEMPLATE_NAME1, TEMPLATE_ELEMENT_ID1,
        TEMPLATE_STATUS_IN_PROGRESS);
    em.getTransaction().begin();
    templateDAOImpl.persist(entityToFound);
    em.getTransaction().commit();
    checkTemplateNummber(1);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#update(org.novaforge.forge.core.organization.model.Template)}
   * .
   */
  @Test
  public void testUpdate()
  {
    final TemplateEntity entityToFound = buildTemplateEntity(TEMPLATE_NAME1, TEMPLATE_ELEMENT_ID1,
        TEMPLATE_STATUS_ENABLE);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkTemplateNummber(1);
    final List<Template> list = templateDAOImpl.getTemplatesByStatus(TEMPLATE_STATUS_ENABLE);
    final Template template = list.get(0);
    assertNotNull(template);
    assertThat(template.getName(), is(TEMPLATE_NAME1));
    // updating
    template.setName(TEMPLATE_NAME2);
    template.setDescription(TEMPLATE_DESCRIPTION2);
    em.getTransaction().begin();
    templateDAOImpl.update(template);
    em.getTransaction().commit();
    // checking update
    checkTemplateNummber(1);
    final List<Template> updatedList = templateDAOImpl.getTemplatesByStatus(TEMPLATE_STATUS_ENABLE);
    final Template updatedTemplate = updatedList.get(0);
    assertNotNull(updatedTemplate);
    assertThat(updatedTemplate.getName(), is(TEMPLATE_NAME2));
    assertThat(updatedTemplate.getDescription(), is(TEMPLATE_DESCRIPTION2));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#delete(org.novaforge.forge.core.organization.model.Template)}
   * .
   */
  @Test
  public void testDeleteTemplate()
  {
    final TemplateEntity entityToFound = buildTemplateEntity(TEMPLATE_NAME1, TEMPLATE_ELEMENT_ID1,
        TEMPLATE_STATUS_ENABLE);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkTemplateNummber(1);
    final List<Template> list = templateDAOImpl.getTemplatesByStatus(TEMPLATE_STATUS_ENABLE);
    final Template template = list.get(0);
    assertNotNull(template);
    assertThat(template.getName(), is(TEMPLATE_NAME1));
    // deleting template
    em.getTransaction().begin();
    templateDAOImpl.delete(template);
    em.getTransaction().commit();
    // checking
    final List<Template> emptyList = templateDAOImpl.getTemplatesByStatus(TEMPLATE_STATUS_ENABLE);
    assertThat(emptyList.size(), is(0));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#newTemplateInstance()}.
   */
  @Test
  public void testNewTemplateInstance()
  {
    // creating template object
    final Template newTemplate = templateDAOImpl.newTemplate();
    assertNotNull(newTemplate);
    // creating project object
    final ProjectDAOImpl projectDAOImpl = new ProjectDAOImpl();
    final Project newProject = projectDAOImpl.newProject();
    assertNotNull(newProject);
    // creating new templateInstance object
    final TemplateInstance templateInstance = templateDAOImpl.newTemplateInstance();
    templateInstance.setProject(newProject);
    templateInstance.setTemplate(newTemplate);
    assertNotNull(templateInstance);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#findByProjectId(java.lang.String)}
   * .
   */
  @Test
  public void testFindByProjectId()
  {
    final TemplateInstance templateInstance = buildTemplateInstance();

    // persist templateInstance
    em.getTransaction().begin();
    templateDAOImpl.persist(templateInstance);
    em.getTransaction().commit();
    checkTemplateInstanceNummber(1);

    // find templateInstance with findByProjectId
    final TemplateInstance templateInstanceFound = templateDAOImpl.findByProjectId("project1");
    assertNotNull(templateInstanceFound);
  }

  private TemplateInstance buildTemplateInstance()
  {
    // persist template
    final TemplateEntity templateEntity = buildTemplateEntity(TEMPLATE_NAME1, TEMPLATE_ELEMENT_ID1,
                                                              TEMPLATE_STATUS_IN_PROGRESS);
    em.getTransaction().begin();
    templateDAOImpl.persist(templateEntity);
    em.getTransaction().commit();
    checkTemplateNummber(1);

    // persist project and persist it
    final ProjectEntity projectEntity = buildProjectEntity(PROJECT_NAME1, PROJECT_ID1, PROJECT_DESCRIPTION1,
                                                           ProjectStatus.VALIDATED);
    em.getTransaction().begin();
    final ProjectDAOImpl projectDAOImpl = new ProjectDAOImpl();
    projectDAOImpl.setEntityManager(em);
    projectDAOImpl.persist(projectEntity);
    em.getTransaction().commit();
    checkProjectNummber(1);

    // create templateInstance and persist it
    final Project        project  = projectDAOImpl.findByProjectId("project1");
    final List<Template> list     = templateDAOImpl.getTemplatesByStatus(TemplateProjectStatus.IN_PROGRESS);
    final Template       template = list.get(0);
    assertNotNull(template);
    final TemplateInstance templateInstance = templateDAOImpl.newTemplateInstance();
    assertNotNull(templateInstance);
    templateInstance.setProject(project);
    templateInstance.setTemplate(template);

    return templateInstance;
  }

  /**
   * Check the number of {@link TemplateInstanceEntity} persisted
   *
   * @param pNumber
   *     the number to match
   */
  private void checkTemplateInstanceNummber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM TemplateInstanceEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  private ProjectEntity buildProjectEntity(final String pProjectName, final String pProjectId,
                                           final String pDescription, final ProjectStatus pProjectStatus)
  {
    final ProjectEntity entity = new ProjectEntity();
    entity.setName(pProjectName);
    entity.setProjectId(pProjectId);
    entity.setDescription(pDescription);
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

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#persist(org.novaforge.forge.core.organization.model.TemplateInstance)}
   * .
   */
  @Test
  public void testPersistTemplateInstance()
  {
    final TemplateInstance templateInstance = buildTemplateInstance();

    // persist templateInstance
    em.getTransaction().begin();
    templateDAOImpl.persist(templateInstance);
    em.getTransaction().commit();
    checkTemplateInstanceNummber(1);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.TemplateDAOImpl#delete(org.novaforge.forge.core.organization.model.TemplateInstance)}
   * .
   */
  @Test
  public void testDeleteTemplateInstance()
  {
    final TemplateInstance templateInstance = buildTemplateInstance();

    // persist templateInstance
    em.getTransaction().begin();
    templateDAOImpl.persist(templateInstance);
    em.getTransaction().commit();
    checkTemplateInstanceNummber(1);

    // delete templateInstance
    em.getTransaction().begin();
    templateDAOImpl.delete(templateInstance);
    em.getTransaction().commit();
    checkTemplateInstanceNummber(0);
  }

}
