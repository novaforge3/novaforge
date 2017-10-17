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
package org.novaforge.forge.commons.technical.historization.internal.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.commons.technical.historization.entity.EventEntity;
import org.novaforge.forge.commons.technical.historization.model.Event;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.tests.jpa.tcase.JPATestCase;

import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Guillaume Lamirand
 */
public class EventDAOImplTest extends JPATestCase
{

  /**
   * Constant for admin actor
   */
  private static final String DEFAULT_ACTOR = "admin";
  /**
   * Instance of {@link EventDAOImpl} to test
   */
  private static EventDAOImpl eventDAO;

  /**
   * Default contructor.
   */
  public EventDAOImplTest()
  {
    super("jdbc/historization", "historizationTest");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();

    eventDAO = new EventDAOImpl();
    eventDAO.setEntityManager(em);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    eventDAO = null;
  }

  /**
   * Test method {@link EventDAOImpl#findEventsByActor(String)}
   */
  @Test
  public void testFindEventsByActor()
  {
    // Persist two event with different actor
    em.getTransaction().begin();
    final String uuid         = UUID.randomUUID().toString();
    final Date   date         = new Date();
    final String actorTofound = DEFAULT_ACTOR;
    final String actor        = "user1";
    final EventEntity entityToFound = buildEntity(uuid, actorTofound, date, EventLevel.ENTRY, EventType.GET_ALL_ROLES);
    final EventEntity entity = buildEntity(UUID.randomUUID().toString(), actor, date, EventLevel.ENTRY,
                                           EventType.GET_ALL_ROLES);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final List<Event> findEventsByActor = eventDAO.findEventsByActor(actorTofound);
    assertNotNull(findEventsByActor);
    assertThat(findEventsByActor.size(), is(1));
    assertThat(findEventsByActor.get(0).getUuid(), is(uuid));
    assertThat(findEventsByActor.get(0).getDate(), is(date));
    assertThat(findEventsByActor.get(0).getActor(), is(actorTofound));

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
  private EventEntity buildEntity(final String pUUID, final String pActor, final Date pDate,
      final EventLevel pLevel, final EventType pType)
  {

    final EventEntity entity = new EventEntity(pUUID);
    entity.setActor(pActor);
    entity.setDate(pDate);
    entity.setLevel(pLevel);
    entity.setType(pType);
    return entity;
  }

  /**
   * Check the number of {@link EventEntity} persisted
   *
   * @param pNumber
   *          the number to match
   */
  private void checkEventNummber(final int pNumber)
  {
    final TypedQuery<Event> query = em.createQuery("SELECT e FROM EventEntity e", Event.class);
    final List<Event> resultList = query.getResultList();
    assertThat(resultList.size(), is(pNumber));
  }

  /**
   * Test method {@link EventDAOImpl#findEventsByDate(Date, Date)}
   */
  @Test
  public void testFindEventsByDate()
  {

    // Persist two event with different date
    em.getTransaction().begin();
    final String uuid = UUID.randomUUID().toString();
    final Calendar instance = Calendar.getInstance();
    instance.set(2013, 2, 4);
    final Date dateTofound = instance.getTime();
    final Date date = new Date();
    final String actor = DEFAULT_ACTOR;
    final EventEntity entityToFound = buildEntity(uuid, actor, dateTofound, EventLevel.ENTRY,
        EventType.GET_ALL_ROLES);
    final EventEntity entity = buildEntity(UUID.randomUUID().toString(), actor, date, EventLevel.ENTRY,
        EventType.GET_ALL_ROLES);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    instance.set(2013, 2, 3);
    final Date begin = instance.getTime();
    instance.add(Calendar.DATE, 2);
    final Date end = instance.getTime();
    final List<Event> findEventsByDate = eventDAO.findEventsByDate(begin, end);
    assertNotNull(findEventsByDate);
    assertThat(findEventsByDate.size(), is(1));
    assertThat(findEventsByDate.get(0).getUuid(), is(uuid));
    assertThat(findEventsByDate.get(0).getDate(), is(dateTofound));
    assertThat(findEventsByDate.get(0).getActor(), is(actor));
  }

  /**
   * Test method {@link EventDAOImpl#findEventsByKeyword(String)}
   */
  @Test
  public void testFindEventsByKeyword()
  {
    // Persist two event with different actor
    em.getTransaction().begin();
    final String uuid = UUID.randomUUID().toString();
    final Date date = new Date();
    final EventEntity entityToFound = buildEntity(uuid, DEFAULT_ACTOR, date, EventLevel.ENTRY,
        EventType.GET_ALL_ROLES);
    final String details = "test keyword";
    entityToFound.setDetails(details);
    final EventEntity entity = buildEntity(UUID.randomUUID().toString(), DEFAULT_ACTOR, date,
        EventLevel.ENTRY, EventType.GET_ALL_ROLES);
    entity.setDetails("test");
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    final List<Event> findEventsByKeyword = eventDAO.findEventsByKeyword("keyword");
    assertNotNull(findEventsByKeyword);
    assertThat(findEventsByKeyword.size(), is(1));
    assertThat(findEventsByKeyword.get(0).getUuid(), is(uuid));
    assertThat(findEventsByKeyword.get(0).getDate(), is(date));
    assertThat(findEventsByKeyword.get(0).getActor(), is(DEFAULT_ACTOR));
    assertThat(findEventsByKeyword.get(0).getDetails(), is(details));
  }

  /**
   * Test method {@link EventDAOImpl#findEventsByLevel(EventLevel)}
   */
  @Test
  public void testFindEventsByLevel()
  {
    // Persist two event with different level
    em.getTransaction().begin();
    final String uuid = UUID.randomUUID().toString();
    final Date date = new Date();
    final String actor = DEFAULT_ACTOR;
    final EventEntity entityToFound = buildEntity(uuid, actor, date, EventLevel.ERROR,
        EventType.GET_ALL_ROLES);
    final EventEntity entity = buildEntity(UUID.randomUUID().toString(), actor, date, EventLevel.EXIT,
        EventType.GET_ALL_ROLES);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();

    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    final List<Event> findEventsByLevel = eventDAO.findEventsByLevel(EventLevel.ERROR);
    assertNotNull(findEventsByLevel);
    assertThat(findEventsByLevel.size(), is(1));
    assertThat(findEventsByLevel.get(0).getUuid(), is(uuid));
    assertThat(findEventsByLevel.get(0).getDate(), is(date));
    assertThat(findEventsByLevel.get(0).getActor(), is(actor));
    assertThat(findEventsByLevel.get(0).getLevel(), is(EventLevel.ERROR));
  }

  /**
   * Test method {@link EventDAOImpl#findEventsByType(EventType)}
   */
  @Test
  public void testFindEventsByType()
  {

    // Persist two event with different type
    em.getTransaction().begin();
    final String uuid = UUID.randomUUID().toString();
    final Date date = new Date();
    final String actor = DEFAULT_ACTOR;
    final EventEntity entityToFound = buildEntity(uuid, actor, date, EventLevel.ERROR,
        EventType.GET_ALL_ROLES);
    final EventEntity entity = buildEntity(UUID.randomUUID().toString(), actor, date, EventLevel.EXIT,
        EventType.CREATE_GROUP);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    checkEventNummber(2);

    final List<Event> findEventsByType = eventDAO.findEventsByType(EventType.GET_ALL_ROLES);
    assertNotNull(findEventsByType);
    assertThat(findEventsByType.size(), is(1));
    assertThat(findEventsByType.get(0).getUuid(), is(uuid));
    assertThat(findEventsByType.get(0).getDate(), is(date));
    assertThat(findEventsByType.get(0).getActor(), is(actor));
    assertThat(findEventsByType.get(0).getType(), is(EventType.GET_ALL_ROLES));
    assertThat(findEventsByType.get(0).getLevel(), is(EventLevel.ERROR));
  }

  /**
   * Test method {@link EventDAOImpl#findEventsByType(EventType)}
   */
  @Test
  public void testSave()
  {

    // Check save method
    em.getTransaction().begin();
    final String uuid = UUID.randomUUID().toString();
    final Date date = new Date();
    final String actor = DEFAULT_ACTOR;
    final EventEntity entity = buildEntity(uuid, DEFAULT_ACTOR, date, EventLevel.ERROR,
        EventType.GET_ALL_ROLES);
    eventDAO.save(entity);
    em.getTransaction().commit();

    // Check if the previous event has been correctly persisted
    checkEventNummber(1);

    // Check event retrieve
    final TypedQuery<Event> query = em.createQuery("SELECT e FROM EventEntity e", Event.class);
    final List<Event> resultList = query.getResultList();
    assertNotNull(resultList);
    assertThat(resultList.size(), is(1));
    assertThat(resultList.get(0).getUuid(), is(uuid));
    assertThat(resultList.get(0).getDate(), is(date));
    assertThat(resultList.get(0).getActor(), is(actor));
    assertThat(resultList.get(0).getType(), is(EventType.GET_ALL_ROLES));
    assertThat(resultList.get(0).getLevel(), is(EventLevel.ERROR));
  }

  /**
   * Test method {@link EventDAOImpl#findEventsByType(EventType)}
   */
  @Test
  public void testCreeteEvent()
  {
    final Event createEvent = eventDAO.createEvent(DEFAULT_ACTOR, EventType.GET_ALL_ROLES, EventLevel.ERROR,
        null);
    assertNotNull(createEvent);
    assertNotNull(createEvent.getUuid());
    assertNotNull(createEvent.getDate());
    assertThat(createEvent.getActor(), is(DEFAULT_ACTOR));
    assertThat(createEvent.getType(), is(EventType.GET_ALL_ROLES));
    assertThat(createEvent.getLevel(), is(EventLevel.ERROR));
  }

  /**
   * Test method {@link EventDAOImpl#deleteEventsBeforeDate(Date)}
   */
  @Test
  public void testDeleteEventsBeforeDate()
  {
    // Persist two event with different date
    em.getTransaction().begin();
    final String uuid = UUID.randomUUID().toString();
    final Calendar instance = Calendar.getInstance();
    instance.set(2013, 2, 4);
    final Date dateTofound = instance.getTime();
    final Date date = new Date();
    final String actor = DEFAULT_ACTOR;
    final EventEntity entityToFound = buildEntity(uuid, actor, dateTofound, EventLevel.ENTRY,
        EventType.GET_ALL_ROLES);
    final EventEntity entity = buildEntity(UUID.randomUUID().toString(), actor, date, EventLevel.ENTRY,
        EventType.GET_ALL_ROLES);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    // Check method
    em.getTransaction().begin();
    instance.set(2013, 2, 5);
    final Date begin = instance.getTime();
    final int deleted = eventDAO.deleteEventsBeforeDate(begin);
    em.getTransaction().commit();
    assertNotNull(deleted);
    assertThat(1, is(deleted));

    // Check if the previous event have been correctly deleted
    checkEventNummber(1);
  }

  /**
   * Test method {@link EventDAOImpl#findAllEvents(int, int, Date)}
   */
  @Test
  public void testFindAllEvents()
  {
    // Persist two event with different type
    em.getTransaction().begin();
    final String event1Uuid = UUID.randomUUID().toString();
    final String event2Uuid = UUID.randomUUID().toString();
    final Date date = new Date();
    final EventEntity entityToFound = buildEntity(event1Uuid, DEFAULT_ACTOR, date, EventLevel.ERROR,
        EventType.GET_ALL_ROLES);
    final EventEntity entity = buildEntity(event2Uuid, DEFAULT_ACTOR, date, EventLevel.EXIT,
        EventType.CREATE_GROUP);
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkEventNummber(2);

    final List<Event> findAll = eventDAO.findAllEvents(0, 5, new Date());
    assertNotNull(findAll);
    assertThat(findAll.size(), is(2));
    for (final Event event : findAll)
    {
      assertThat(event.getDate(), is(date));
      assertThat(event.getActor(), is(DEFAULT_ACTOR));
      if (event1Uuid.equals(event.getUuid()))
      {
        assertThat(event.getType(), is(EventType.GET_ALL_ROLES));
        assertThat(event.getLevel(), is(EventLevel.ERROR));
      }
      else if (event1Uuid.equals(event.getUuid()))
      {
        assertThat(event.getType(), is(EventType.CREATE_GROUP));
        assertThat(event.getLevel(), is(EventLevel.EXIT));

      }
    }
    final List<Event> findAllbis = eventDAO.findAllEvents(0, 1, new Date());
    assertNotNull(findAllbis);
    assertThat(1, is(findAllbis.size()));
    for (final Event event : findAllbis)
    {
      assertThat(event.getDate(), is(date));
      assertThat(event.getActor(), is(DEFAULT_ACTOR));
      if (event1Uuid.equals(event.getUuid()))
      {
        assertThat(event.getType(), is(EventType.GET_ALL_ROLES));
        assertThat(event.getLevel(), is(EventLevel.ERROR));
      }
      else if (event1Uuid.equals(event.getUuid()))
      {
        assertThat(event.getType(), is(EventType.CREATE_GROUP));
        assertThat(event.getLevel(), is(EventLevel.EXIT));

      }
    }
  }

  /**
   * Test method
   * {@link EventDAOImpl#findEventsByCriterias(java.util.Map, java.util.Map, Date, Date, int, int)}
   */
  @Test
  public void testFindEventsByCriterias()
  {
    // TODO
  }

  /**
   * Test method {@link EventDAOImpl#countEventsByCriterias(java.util.Map, java.util.Map, Date, Date)}
   */
  @Test
  public void testCountEventsByCriterias()
  {
    // TODO
  }

}
