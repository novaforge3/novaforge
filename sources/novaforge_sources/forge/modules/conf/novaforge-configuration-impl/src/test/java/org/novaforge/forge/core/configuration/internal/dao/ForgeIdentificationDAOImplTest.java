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
package org.novaforge.forge.core.configuration.internal.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.core.configuration.entity.ForgeIdentificationEntity;
import org.novaforge.forge.core.configuration.model.ForgeIdentification;
import org.novaforge.forge.tests.jpa.tcase.JPATestCase;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Guillaume Lamirand
 */
public class ForgeIdentificationDAOImplTest extends JPATestCase
{

  private ForgeIdentificationDAOImpl forgeIdentificationDAO;

  /**
   * Default constructor
   */
  public ForgeIdentificationDAOImplTest()
  {
    super("jdbc/novaforge", "core.configuration.test");
  }

  /**
   * @throws java.lang.Exception
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    forgeIdentificationDAO = new ForgeIdentificationDAOImpl();
    forgeIdentificationDAO.setEntityManagerFactory(emf);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    forgeIdentificationDAO = null;
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.configuration.internal.dao.ForgeIdentificationDAOImpl#findByUUID(java.util.UUID)}
   * .
   */
  @Test
  public final void testFindByUUID()
  {
    // Persist two id with different uuid
    em.getTransaction().begin();
    final UUID uuid = UUID.randomUUID();
    final ForgeIdentificationEntity entityToFound = new ForgeIdentificationEntity(uuid);
    final ForgeIdentificationEntity entity = new ForgeIdentificationEntity(UUID.randomUUID());
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final ForgeIdentification findByUUID = forgeIdentificationDAO.findByUUID(uuid);
    assertNotNull(findByUUID);
    assertThat(findByUUID.getIdentifiant(), is(uuid));
  }

  /**
   * Check the number of {@link EventEntity} persisted
   *
   * @param pNumber
   *     the number to match
   */
  private void checkEventNummber(final int pNumber)
  {
    final TypedQuery<ForgeIdentification> query = em.createQuery("SELECT e FROM ForgeIdentificationEntity e",
                                                                 ForgeIdentification.class);
    final List<ForgeIdentification> resultList = query.getResultList();
    assertThat(resultList.size(), is(pNumber));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.configuration.internal.dao.ForgeIdentificationDAOImpl#existIdentifiant()}
   * .
   */
  @Test
  public final void testExistIdentifiant()
  {
    // Persist two id with different uuid
    em.getTransaction().begin();
    final ForgeIdentificationEntity entity = new ForgeIdentificationEntity(UUID.randomUUID());
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(1);

    // Check method
    final boolean exists = forgeIdentificationDAO.existIdentifiant();
    assertNotNull(exists);
    assertTrue(exists);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.configuration.internal.dao.ForgeIdentificationDAOImpl#get()}.
   */
  @Test
  public final void testGet()
  {
    // Persist two id with different uuid
    em.getTransaction().begin();
    final UUID uuid = UUID.randomUUID();
    final ForgeIdentificationEntity entity = new ForgeIdentificationEntity(uuid);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(1);

    // Check method
    final ForgeIdentification id = forgeIdentificationDAO.get();
    assertNotNull(id);
    assertThat(id.getIdentifiant(), is(uuid));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.configuration.internal.dao.ForgeIdentificationDAOImpl#create(java.util.UUID)}
   * .
   */
  @Test
  public final void testCreate()
  {
    // Persist two id with different uuid
    final UUID uuid = UUID.randomUUID();
    em.getTransaction().begin();
    final ForgeIdentification create = forgeIdentificationDAO.create(uuid);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(1);

    // Check method
    assertNotNull(create);
    assertThat(create.getIdentifiant(), is(uuid));
  }

}
