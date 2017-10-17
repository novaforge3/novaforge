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
import org.novaforge.forge.core.configuration.entity.ForgeConfigurationEntity;
import org.novaforge.forge.core.configuration.model.ForgeConfiguration;
import org.novaforge.forge.tests.jpa.tcase.JPATestCase;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Guillaume Lamirand
 */
public class ForgeConfigurationDAOImplTest extends JPATestCase
{

  private ForgeConfigurationDAOImpl forgeConfigurationDAO;

  /**
   * Default constructor
   */
  public ForgeConfigurationDAOImplTest()
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
    forgeConfigurationDAO = new ForgeConfigurationDAOImpl();
    forgeConfigurationDAO.setEntityManagerFactory(emf);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    forgeConfigurationDAO = null;
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.configuration.internal.dao.ForgeConfigurationDAOImpl#findByKey(String)} .
   */
  @Test
  public final void testFindByUUID()
  {
    // Persist two id with different uuid
    em.getTransaction().begin();
    final ForgeConfigurationEntity entityToFound = new ForgeConfigurationEntity("test1", "value1");
    final ForgeConfigurationEntity entity = new ForgeConfigurationEntity("test2", "value2");
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkNummber(2);

    // Check method
    final ForgeConfiguration findByKey = forgeConfigurationDAO.findByKey("test1");
    assertNotNull(findByKey);
    assertThat(findByKey.getKey(), is("test1"));
    assertThat(findByKey.getValue(), is("value1"));
  }

  /**
   * Check the number of {@link ForgeConfigurationEntity} persisted
   *
   * @param pNumber
   *     the number to match
   */
  private void checkNummber(final int pNumber)
  {
    final TypedQuery<ForgeConfigurationEntity> query = em.createQuery("SELECT e FROM ForgeConfigurationEntity e",
                                                                      ForgeConfigurationEntity.class);
    final List<ForgeConfigurationEntity> resultList = query.getResultList();
    assertThat(resultList.size(), is(pNumber));
  }
}
