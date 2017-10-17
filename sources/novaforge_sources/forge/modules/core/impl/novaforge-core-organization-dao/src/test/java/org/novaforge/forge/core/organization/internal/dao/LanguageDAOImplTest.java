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
import org.novaforge.forge.core.organization.entity.LanguageEntity;
import org.novaforge.forge.core.organization.model.Language;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author blachonm
 */
public class LanguageDAOImplTest extends OrganizationJPATestCase
{

  private static final String FR = "fr";
  private static final String EN = "en";
  /*
   * constants declaration
   */
  private LanguageDAOImpl     languageDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    languageDAOImpl = new LanguageDAOImpl();
    languageDAOImpl.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    languageDAOImpl = null;
  }

  /**
   * Test method for {@link org.novaforge.forge.core.organization.internal.dao.LanguageDAOImpl#findAll()}.
   */
  @Test
  public void testFindAll()
  {
    final LanguageEntity entityToFound = buildEntity(FR);
    final LanguageEntity entity        = buildEntity(EN);
    em.getTransaction().begin();
    languageDAOImpl.persist(entityToFound);
    languageDAOImpl.persist(entity);
    em.getTransaction().commit();
    checkLanguageNummber(2);
    final List<Language> list = languageDAOImpl.findAll();
    assertNotNull(list);
    assertThat(list.size(), is(2));
  }

  private LanguageEntity buildEntity(final String pName)
  {
    final LanguageEntity entity = new LanguageEntity();
    entity.setName(pName);
    entity.setIsDefault(false);
    return entity;
  }

  /**
   * Check the number of {@link LanguageEntity} persisted
   *
   * @param pNumber
   *          the number to match
   */
  private void checkLanguageNummber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM LanguageEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.LanguageDAOImpl#findByName(java.lang.String)}.
   */
  @Test
  public void testFindByName()
  {
    // Persist two metadata with different id
    final LanguageEntity entityToFound = buildEntity(FR);
    final LanguageEntity entity = buildEntity(EN);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkLanguageNummber(2);
    final Language language = languageDAOImpl.findByName(FR);
    assertNotNull(language);
    assertThat(language.getName(), is(FR));

  }

  /**
   * Test method for {@link org.novaforge.forge.core.organization.internal.dao.LanguageDAOImpl#newLanguage()}.
   */
  @Test
  public void testNewLanguage()
  {
    final Language newLanguage = languageDAOImpl.newLanguage();
    assertNotNull(newLanguage);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.LanguageDAOImpl#persist(org.novaforge.forge.core.organization.model.Language)}
   * .
   */
  @Test
  public void testPersist()
  {
    final LanguageEntity entityToFound = new LanguageEntity();
    final Language newLanguage = languageDAOImpl.newLanguage();
    entityToFound.setName(FR);
    newLanguage.setName(EN);
    em.getTransaction().begin();
    languageDAOImpl.persist(entityToFound);
    languageDAOImpl.persist(newLanguage);
    em.getTransaction().commit();
    checkLanguageNummber(2);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.LanguageDAOImpl#findByName(java.lang.String)}.
   */
  @Test
  public void testIsDefault()
  {
    // Persist two metadata with different id
    final LanguageEntity entity = buildEntity(FR);
    final LanguageEntity entityDefault = buildEntity(EN);
    entityDefault.setIsDefault(true);
    em.getTransaction().begin();
    em.persist(entityDefault);
    em.persist(entity);
    em.getTransaction().commit();
    final Language language = languageDAOImpl.findDefault();
    assertNotNull(language);
    assertThat(language.getName(), is(EN));
  }
}
