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
package org.novaforge.forge.core.plugins.internal.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.entity.PluginMetadataEntity;
import org.novaforge.forge.core.plugins.entity.PluginQueuesEntity;
import org.novaforge.forge.tests.jpa.tcase.JPATestCase;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Guillaume Lamirand
 */
public class PluginMetadataDAOImplTest extends JPATestCase
{

  private final static String   CATEGORY             = "TEST";
  private final static String   TYPE                 = "test";
  private final static String   TYPE_FIND            = "testfind";
  private static final String   DESC_NOT             = "entity not to find";
  private static final String   DESC_FIND            = "entity to find";
  private static final String   MEMBERSHIP_QUEUE     = "membership";
  private static final String   PROJECT_QUEUE        = "project";
  private static final String   ROLESMAPPING_QUEUE   = "rolesmapping";
  private static final String   USER_QUEUE           = "user";
  private static final String   MEMBERSHIP_QUEUE_2   = "membership2";
  private static final String   PROJECT_QUEUE_2      = "project2";
  private static final String   ROLESMAPPING_QUEUE_2 = "rolesmapping2";
  private static final String   USER_QUEUE_2         = "user2";
  private PluginMetadataDAOImpl pluginMetadataDAO;

  /**
   * Default constructor
   */
  public PluginMetadataDAOImplTest()
  {
    super("jdbc/novaforge", "core.plugins.test");
  }

  /**
   * @throws java.lang.Exception
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();

    pluginMetadataDAO = new PluginMetadataDAOImpl();
    pluginMetadataDAO.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    pluginMetadataDAO = null;
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#exist(java.util.UUID)}.
   */
  @Test
  public final void testExist()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID                 uuidToFind    = UUID.randomUUID();
    final UUID                 uuid          = UUID.randomUUID();
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_FIND);
    final PluginMetadataEntity entity        = buildEntity(2, uuid, DESC_NOT);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final boolean exists = pluginMetadataDAO.exist(uuidToFind);
    assertThat(exists, is(true));
    final boolean notexists = pluginMetadataDAO.exist(UUID.randomUUID());
    assertThat(notexists, is(false));
  }

  /**
   * Build an entity which the following parameter
   * 
   * @param pUUID
   *          the uuid
   * @param pActor
   *          the actor
   * @param pDate
   *          the date
   * @param pLevel
   *          the level
   * @param pType
   *          the type
   * @return {@link EventEntity} instance
   */
  private PluginMetadataEntity buildEntity(final int pIndex, final UUID pUUID, final String pDesc)
  {

    final PluginQueuesEntity persistenceQueues = new PluginQueuesEntity();
    if (pIndex == 1)
    {
      persistenceQueues.setProjectQueue(PROJECT_QUEUE);
      persistenceQueues.setMembershipQueue(MEMBERSHIP_QUEUE);
      persistenceQueues.setRolesMappingQueue(ROLESMAPPING_QUEUE);
      persistenceQueues.setUserQueue(USER_QUEUE);
    }
    else
    {
      persistenceQueues.setProjectQueue(PROJECT_QUEUE_2);
      persistenceQueues.setMembershipQueue(MEMBERSHIP_QUEUE_2);
      persistenceQueues.setRolesMappingQueue(ROLESMAPPING_QUEUE_2);
      persistenceQueues.setUserQueue(USER_QUEUE_2);
    }

    final PluginMetadataEntity entity = new PluginMetadataEntity();
    entity.setUUID(pUUID);
    entity.setDescription(pDesc);
    entity.setType(TYPE);
    entity.setCategory(CATEGORY);
    entity.setJMSQueues(persistenceQueues);
    entity.setStatus(PluginStatus.INSTALLED);
    return entity;
  }

  /**
   * Check the number of {@link PluginPersistenceMetadata} persisted
   *
   * @param pNumber
   *          the number to match
   */
  private void checkEventNummber(final int pNumber)
  {
    final TypedQuery<PluginPersistenceMetadata> query = em.createQuery("SELECT e FROM PluginMetadataEntity e",
                                                                       PluginPersistenceMetadata.class);
    final List<PluginPersistenceMetadata> resultList = query.getResultList();
    assertThat(resultList.size(), is(pNumber));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#findByCategory(java.lang.String)}
   * .
   */
  @Test
  public final void testFindByCategory()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuidToFind = UUID.randomUUID();
    final UUID uuid = UUID.randomUUID();
    final String category = "CATEGORY TO FIND";
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_FIND);
    entityToFound.setStatus(PluginStatus.DEPRECATED);
    entityToFound.setCategory(category);
    final PluginMetadataEntity entity = buildEntity(2, uuid, DESC_NOT);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();

    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final List<PluginPersistenceMetadata> find = pluginMetadataDAO.findByCategory(category);
    assertNotNull(find);
    assertThat(find.size(), is(1));
    assertThat(find.get(0).getUUID(), is(uuidToFind));
    assertThat(find.get(0).getStatus(), is(PluginStatus.DEPRECATED));
    assertThat(find.get(0).getType(), is(TYPE));
    assertThat(find.get(0).getDescription(), is(DESC_FIND));
    assertThat(find.get(0).getCategory(), is(category));
    final List<PluginPersistenceMetadata> notfind = pluginMetadataDAO.findByCategory("wrongcat");
    assertNotNull(notfind);
    assertThat(notfind.size(), is(0));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#findByCategoryAndStatusAndAvailability(java.lang.String, org.novaforge.forge.core.plugins.domain.core.PluginStatus, boolean)}
   * .
   */
  @Test
  public final void testFindByCategoryAndStatusAndAvailability()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuidToFind = UUID.randomUUID();
    final UUID uuid = UUID.randomUUID();
    final String category = "CATEGORY TO FIND";
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_FIND);
    entityToFound.setStatus(PluginStatus.DEPRECATED);
    entityToFound.setCategory(category);
    final PluginMetadataEntity entity = buildEntity(2, uuid, DESC_NOT);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();

    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final List<PluginPersistenceMetadata> find = pluginMetadataDAO.findByCategoryAndStatusAndAvailability(
        category, PluginStatus.DEPRECATED, false);
    assertNotNull(find);
    assertThat(find.size(), is(1));
    assertThat(find.get(0).getUUID(), is(uuidToFind));
    assertThat(find.get(0).getStatus(), is(PluginStatus.DEPRECATED));
    assertThat(find.get(0).getType(), is(TYPE));
    assertThat(find.get(0).getDescription(), is(DESC_FIND));
    assertThat(find.get(0).getCategory(), is(category));
    final List<PluginPersistenceMetadata> notfind = pluginMetadataDAO.findByCategoryAndStatusAndAvailability(
        "wrongcat", PluginStatus.DEPRECATED, false);
    assertNotNull(notfind);
    assertThat(notfind.size(), is(0));
    final List<PluginPersistenceMetadata> notfind1 = pluginMetadataDAO
        .findByCategoryAndStatusAndAvailability(category, PluginStatus.DESACTIVATED, false);
    assertNotNull(notfind1);
    assertThat(notfind1.size(), is(0));
    final List<PluginPersistenceMetadata> notfind2 = pluginMetadataDAO
        .findByCategoryAndStatusAndAvailability(category, PluginStatus.DEPRECATED, true);
    assertNotNull(notfind2);
    assertThat(notfind2.size(), is(0));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#findByType(java.lang.String)}.
   */
  @Test
  public final void testFindByType()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuidToFind = UUID.randomUUID();
    final UUID uuid = UUID.randomUUID();
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_FIND);
    entityToFound.setStatus(PluginStatus.DEPRECATED);
    entityToFound.setType(TYPE_FIND);
    final PluginMetadataEntity entity = buildEntity(2, uuid, DESC_NOT);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();

    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final List<PluginPersistenceMetadata> find = pluginMetadataDAO.findByType(TYPE_FIND);
    assertNotNull(find);
    assertThat(find.size(), is(1));
    assertThat(find.get(0).getUUID(), is(uuidToFind));
    assertThat(find.get(0).getStatus(), is(PluginStatus.DEPRECATED));
    assertThat(find.get(0).getType(), is(TYPE_FIND));
    assertThat(find.get(0).getDescription(), is(DESC_FIND));
    final List<PluginPersistenceMetadata> notfind = pluginMetadataDAO.findByType("wrongtype");
    assertNotNull(notfind);
    assertThat(notfind.size(), is(0));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#findByStatus(org.novaforge.forge.core.plugins.domain.core.PluginStatus)}
   * .
   */
  @Test
  public final void testFindByStatus()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuidToFind = UUID.randomUUID();
    final UUID uuid = UUID.randomUUID();
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_FIND);
    entityToFound.setStatus(PluginStatus.DEPRECATED);
    final PluginMetadataEntity entity = buildEntity(2, uuid, DESC_NOT);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();

    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final List<PluginPersistenceMetadata> find = pluginMetadataDAO.findByStatus(PluginStatus.DEPRECATED);
    assertNotNull(find);
    assertThat(find.size(), is(1));
    assertThat(find.get(0).getUUID(), is(uuidToFind));
    assertThat(find.get(0).getStatus(), is(PluginStatus.DEPRECATED));
    assertThat(find.get(0).getDescription(), is(DESC_FIND));
    final List<PluginPersistenceMetadata> findnot = pluginMetadataDAO.findByStatus(PluginStatus.UNINSTALLED);
    assertNotNull(findnot);
    assertThat(findnot.size(), is(0));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#findByStatusAndAvailabitly(org.novaforge.forge.core.plugins.domain.core.PluginStatus, boolean)}
   * .
   */
  @Test
  public final void testFindByStatusAndAvailabitly()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuidToFind = UUID.randomUUID();
    final UUID uuid = UUID.randomUUID();
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_FIND);
    entityToFound.setStatus(PluginStatus.DEPRECATED);
    entityToFound.setAvailable(true);
    final PluginMetadataEntity entity = buildEntity(2, uuid, DESC_NOT);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();

    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final List<PluginPersistenceMetadata> find = pluginMetadataDAO.findByStatusAndAvailabitly(
        PluginStatus.DEPRECATED, true);
    assertNotNull(find);
    assertThat(find.size(), is(1));
    assertThat(find.get(0).getUUID(), is(uuidToFind));
    assertThat(find.get(0).getStatus(), is(PluginStatus.DEPRECATED));
    assertThat(find.get(0).getType(), is(TYPE));
    assertThat(find.get(0).getDescription(), is(DESC_FIND));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#findByUUID(java.util.UUID)}.
   */
  @Test
  public final void testFindByUUID()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuidToFind = UUID.randomUUID();
    final UUID uuid = UUID.randomUUID();
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_FIND);
    entityToFound.setStatus(PluginStatus.DEPRECATED);
    final PluginMetadataEntity entity = buildEntity(2, uuid, DESC_NOT);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final PluginPersistenceMetadata plugin = pluginMetadataDAO.findByUUID(uuidToFind);
    assertThat(plugin, notNullValue());
    assertThat(plugin.getUUID(), is(uuidToFind));
    assertThat(plugin.getStatus(), is(PluginStatus.DEPRECATED));
    assertThat(plugin.getType(), is(TYPE));
    assertThat(plugin.getDescription(), is(DESC_FIND));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#findViewsByUUID(java.util.UUID)}
   * .
   */
  @Test
  public final void testFindViewsByUUID()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuidToFind = UUID.randomUUID();
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_NOT);
    final List<PluginViewEnum> enums = new ArrayList<PluginViewEnum>();
    enums.add(PluginViewEnum.APPLICATION);
    enums.add(PluginViewEnum.DEFAULT);
    enums.add(PluginViewEnum.ADMINISTRATION);
    entityToFound.setViews(enums);
    em.persist(entityToFound);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(1);

    // Check method
    final List<PluginViewEnum> views = pluginMetadataDAO.findViewsByUUID(uuidToFind);
    assertThat(views, notNullValue());
    assertThat(views.size(), is(3));
    boolean isOk = false;
    for (final PluginViewEnum view : views)
    {
      isOk = ((view.equals(PluginViewEnum.APPLICATION)) && (!view.equals(PluginViewEnum.ADMINISTRATION))
                  && ((!view.equals(PluginViewEnum.DEFAULT)))) || ((!view.equals(PluginViewEnum.APPLICATION))
                                                                       && (view.equals(PluginViewEnum.ADMINISTRATION))
                                                                       && ((!view.equals(PluginViewEnum.DEFAULT))))
                 || ((!view.equals(PluginViewEnum.APPLICATION)) && (!view.equals(PluginViewEnum.ADMINISTRATION))
                         && ((view.equals(PluginViewEnum.DEFAULT))));
    }
    assertThat(isOk, is(true));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#findByViewId(org.novaforge.forge.core.plugins.domain.core.PluginViewEnum)}
   * .
   */
  @Test
  public final void testFindByViewId()
  {

    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuidToFind = UUID.randomUUID();
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_FIND);
    entityToFound.setStatus(PluginStatus.DEPRECATED);

    final List<PluginViewEnum> enums = new ArrayList<PluginViewEnum>();
    enums.add(PluginViewEnum.ADMINISTRATION);
    entityToFound.setViews(enums);
    final PluginMetadataEntity entity = buildEntity(2, uuidToFind, DESC_NOT);
    final List<PluginViewEnum> enums2 = new ArrayList<PluginViewEnum>();
    enums2.add(PluginViewEnum.APPLICATION);
    enums2.add(PluginViewEnum.DEFAULT);
    entity.setViews(enums2);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final List<PluginPersistenceMetadata> plugins = pluginMetadataDAO
        .findByViewId(PluginViewEnum.ADMINISTRATION);
    assertThat(plugins, notNullValue());
    assertThat(plugins.size(), is(1));
    assertThat(plugins.get(0).getUUID(), is(uuidToFind));
    assertThat(plugins.get(0).getStatus(), is(PluginStatus.DEPRECATED));
    assertThat(plugins.get(0).getType(), is(TYPE));
    assertThat(plugins.get(0).getDescription(), is(DESC_FIND));
  }

  /**
   * Test method for {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#findAll()}.
   */
  @Test
  public final void testFindAll()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuidToFind = UUID.randomUUID();
    final UUID uuid = UUID.randomUUID();
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_NOT);
    final PluginMetadataEntity entity = buildEntity(2, uuid, DESC_NOT);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final List<PluginPersistenceMetadata> plugins = pluginMetadataDAO.findAll();
    assertThat(plugins, notNullValue());
    assertThat(plugins.size(), is(2));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#findCategories()}.
   */
  @Test
  public final void testFindCategories()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuidToFind = UUID.randomUUID();
    final UUID uuid = UUID.randomUUID();
    final PluginMetadataEntity entityToFound = buildEntity(1, uuidToFind, DESC_NOT);
    final String category2 = "CATE2";
    entityToFound.setCategory(category2);
    final PluginMetadataEntity entity = buildEntity(2, uuid, DESC_NOT);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final List<String> cats = pluginMetadataDAO.findCategories();
    assertThat(cats, notNullValue());
    assertThat(cats.size(), is(2));
    boolean isOk = false;
    for (final String string : cats)
    {
      isOk = ((!string.equals(CATEGORY)) && ((string.equals(category2)))) || ((string.equals(CATEGORY)) && ((!string
                                                                                                                  .equals(category2))));
    }
    assertThat(isOk, is(true));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#create(org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata)}
   * .
   */
  @Test
  public final void testCreate()
  {
    // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuid1 = UUID.randomUUID();
    final PluginMetadataEntity entity1 = buildEntity(1, uuid1, DESC_NOT);
    em.persist(entity1);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(1);

    // Check method
    em.getTransaction().begin();
    final UUID uuid2 = UUID.randomUUID();
    final PluginMetadataEntity entity2 = buildEntity(2, uuid2, DESC_FIND);
    pluginMetadataDAO.create(entity2);
    em.getTransaction().commit();
    final PluginPersistenceMetadata plugin = pluginMetadataDAO.findByUUID(uuid2);
    assertThat(plugin, notNullValue());
    assertThat(plugin.getUUID(), is(uuid2));
    assertThat(plugin.getDescription(), is(DESC_FIND));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#update(org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata)}
   * .
   */
  @Test
  public final void testUpdate()
  { // Persist two metadata with different id
    em.getTransaction().begin();
    final UUID uuid1 = UUID.randomUUID();
    final PluginMetadataEntity entity1 = buildEntity(1, uuid1, DESC_FIND);
    em.persist(entity1);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(1);

    // Check method
    em.getTransaction().begin();
    final PluginPersistenceMetadata plugin = pluginMetadataDAO.findByUUID(uuid1);
    plugin.setStatus(PluginStatus.STOPPED);
    plugin.setDescription(DESC_NOT);
    plugin.setAvailable(true);
    pluginMetadataDAO.update(plugin);
    em.getTransaction().commit();
    final PluginPersistenceMetadata plugin2 = pluginMetadataDAO.findByUUID(uuid1);
    assertThat(plugin2, notNullValue());
    assertThat(plugin2.getUUID(), is(uuid1));
    assertThat(plugin2.getStatus(), is(PluginStatus.STOPPED));
    assertThat(plugin2.isAvailable(), is(true));
    assertThat(plugin2.getDescription(), is(DESC_NOT));
  }

  /**
   * Test method for {@link org.novaforge.forge.core.plugins.internal.dao.PluginMetadataDAOImpl#newEntity()}.
   */
  @Test
  public final void testNewEntity()
  {
    final PluginPersistenceMetadata newEntity = pluginMetadataDAO.newEntity();
    assertThat(newEntity, notNullValue());
    assertThat(newEntity.getJMSQueues(), notNullValue());
    assertThat(newEntity.getViews(), notNullValue());
  }

}
