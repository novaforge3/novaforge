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
import org.novaforge.forge.core.organization.entity.OrganizationEntity;
import org.novaforge.forge.core.organization.model.Organization;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author blachonm
 */
public class OrganizationDAOImplTest extends OrganizationJPATestCase
{

  /*
   * constants declaration
   */
  private OrganizationDAOImpl organizationDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    organizationDAOImpl = new OrganizationDAOImpl();
    organizationDAOImpl.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    organizationDAOImpl = null;
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.OrganizationDAOImpl#findByName(java.lang.String)}
   * .
   */
  @Test
  public void testFindByName()
  {
    // Persist two metadata with different id
    final OrganizationEntity entityToFound = buildEntity("org1");
    final OrganizationEntity entity = buildEntity("org2");
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkOrgNummber(2);
    final Organization organization = organizationDAOImpl.findByName("org1");
    assertNotNull(organization);
    assertThat(organization.getName(), is("org1"));
  }

  private OrganizationEntity buildEntity(final String pName)
  {
    final OrganizationEntity entity = new OrganizationEntity();
    entity.setName(pName);
    return entity;
  }

  /**
   * Check the number of {@link OrganizationEntity} persisted
   *
   * @param pNumber
   *     the number to match
   */
  private void checkOrgNummber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM OrganizationEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.OrganizationDAOImpl#newOrganization()}.
   */
  @Test
  public void testNewOrganization()
  {
    final Organization newOrganization = organizationDAOImpl.newOrganization();
    assertNotNull(newOrganization);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.OrganizationDAOImpl#persist(org.novaforge.forge.core.organization.model.Organization)}
   * .
   */
  @Test
  public void testPersist()
  {
    final OrganizationEntity entityToFound = new OrganizationEntity();
    entityToFound.setName("org1");
    em.getTransaction().begin();
    organizationDAOImpl.persist(entityToFound);
    em.getTransaction().commit();
    checkOrgNummber(1);
  }

  /**
   * Test method for {@link org.novaforge.forge.core.organization.internal.dao.OrganizationDAOImpl#findAll()}.
   */
  @Test
  public void testFindAll()
  {
    final OrganizationEntity entityToFound = new OrganizationEntity();
    entityToFound.setName("org1");
    final OrganizationEntity entity = new OrganizationEntity();
    entity.setName("org2");
    em.getTransaction().begin();
    organizationDAOImpl.persist(entityToFound);
    organizationDAOImpl.persist(entity);
    em.getTransaction().commit();
    checkOrgNummber(2);
    final List<Organization> list = organizationDAOImpl.findAll();
    assertNotNull(list);
    assertThat(list.size(), is(2));
  }

}
