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

import org.novaforge.forge.commons.technical.historization.dao.EventDAO;
import org.novaforge.forge.commons.technical.historization.entity.EventEntity;
import org.novaforge.forge.commons.technical.historization.model.Event;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author sbenoist
 */
public class EventDAOImpl implements EventDAO
{

  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager entityManager;

  /**
   * Use by container to inject {@link EntityManager}
   * 
   * @param pEntityManager
   *          the entityManager to set
   */
  public void setEntityManager(final EntityManager pEntityManager)
  {
    entityManager = pEntityManager;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByDate(final Date pBeginDate, final Date pEndDate)

  {
    final TypedQuery<Event> q = entityManager.createNamedQuery("EventEntity.findEventsByDates", Event.class);
    q.setParameter("begin", pBeginDate, TemporalType.TIMESTAMP);
    q.setParameter("end", pEndDate, TemporalType.TIMESTAMP);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByActor(final String pLogin)
  {
    final TypedQuery<Event> q = entityManager.createNamedQuery("EventEntity.findEventsByActor", Event.class);
    q.setParameter("actor", pLogin);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByLevel(final EventLevel pLevel)
  {
    final TypedQuery<Event> q = entityManager.createNamedQuery("EventEntity.findEventsByLevel", Event.class);
    q.setParameter("level", pLevel);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByType(final EventType pType)
  {
    final TypedQuery<Event> q = entityManager.createNamedQuery("EventEntity.findEventsByType", Event.class);
    q.setParameter("type", pType);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByKeyword(final String pKeyWord)
  {
    final TypedQuery<Event> q = entityManager.createNamedQuery("EventEntity.findEventsByKeyword", Event.class);
    q.setParameter("keyword", "%" + pKeyWord + "%");
    return q.getResultList();
  }

  @Override
  public List<Event> findEventsByCriterias(final Map<String, Object> pLikeCriterias,
      final Map<String, Object> pEqualCriterias, final Date pBeginDate, final Date pEndDate,
      final int pFirstResult, final int pMaxResults)
  {
    final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
    final Root<EventEntity> from = criteriaQuery.from(EventEntity.class);
    criteriaQuery.select(from);

    final List<Predicate> predicates = new ArrayList<Predicate>();
    if (pLikeCriterias != null)
    {
      for (final Map.Entry<String, Object> entry : pLikeCriterias.entrySet())
      {
        predicates.add(criteriaBuilder.like(from.<String> get(entry.getKey()),
            "%" + (String) entry.getValue() + "%"));
      }
    }

    if (pEqualCriterias != null)
    {
      for (final Map.Entry<String, Object> entry : pEqualCriterias.entrySet())
      {
        predicates.add(criteriaBuilder.equal(from.<String> get(entry.getKey()), entry.getValue()));
      }
    }

    if ((pBeginDate != null) && (pEndDate != null))
    {
      predicates.add(criteriaBuilder.between(from.<Date> get("date"), pBeginDate, pEndDate));
    }
    else if (pBeginDate != null)
    {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(from.<Date> get("date"), pBeginDate));
    }
    else if (pEndDate != null)
    {
      predicates.add(criteriaBuilder.lessThanOrEqualTo(from.<Date> get("date"), pEndDate));
    }

    final Predicate[] tab = predicates.toArray(new Predicate[predicates.size()]);
    criteriaQuery.where(tab);

    // add order by clause
    criteriaQuery.orderBy(criteriaBuilder.desc(from.<Date> get("date")));

    final TypedQuery<Event> typedQuery = entityManager.createQuery(criteriaQuery);
    // add pagination result
    if (pMaxResults > 0)
    {
      typedQuery.setFirstResult(pFirstResult);
      typedQuery.setMaxResults(pMaxResults);
    }

    return typedQuery.getResultList();
  }

  @Override
  public int countEventsByCriterias(final Map<String, Object> pLikeCriterias,
      final Map<String, Object> pEqualCriterias, final Date pBeginDate, final Date pEndDate)
  {
    final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
    final Root<EventEntity> from = criteriaQuery.from(EventEntity.class);
    criteriaQuery.select(criteriaBuilder.count(from));

    final List<Predicate> predicates = new ArrayList<Predicate>();
    if (pLikeCriterias != null)
    {
      for (final Map.Entry<String, Object> entry : pLikeCriterias.entrySet())
      {
        predicates.add(criteriaBuilder.like(from.<String> get(entry.getKey()),
            "%" + (String) entry.getValue() + "%"));
      }
    }

    if (pEqualCriterias != null)
    {
      for (final Map.Entry<String, Object> entry : pEqualCriterias.entrySet())
      {
        predicates.add(criteriaBuilder.equal(from.<String> get(entry.getKey()), entry.getValue()));
      }
    }

    if ((pBeginDate != null) && (pEndDate != null))
    {
      predicates.add(criteriaBuilder.between(from.<Date> get("date"), pBeginDate, pEndDate));
    }
    else if (pBeginDate != null)
    {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(from.<Date> get("date"), pBeginDate));
    }
    else if (pEndDate != null)
    {
      predicates.add(criteriaBuilder.lessThanOrEqualTo(from.<Date> get("date"), pEndDate));
    }

    final Predicate[] tab = predicates.toArray(new Predicate[predicates.size()]);
    criteriaQuery.where(tab);

    return entityManager.createQuery(criteriaQuery).getSingleResult().intValue();

  }

  @Override
  public Event save(final Event pEvent)
  {
    entityManager.persist(pEvent);
    entityManager.flush();
    return pEvent;

  }

  @Override
  public List<Event> findAllEvents(final int pFirstResult, final int pMaxResults, final Date pDateMax)

  {
    TypedQuery<Event> q;

    if (pDateMax != null)
    {
      q = entityManager.createNamedQuery("EventEntity.findAllEventsBeforeDate", Event.class);
      q.setParameter("date", pDateMax);
    }
    else
    {
      q = entityManager.createNamedQuery("EventEntity.findAllEvents", Event.class);
    }

    if (pMaxResults > 0)
    {
      q.setFirstResult(pFirstResult).setMaxResults(pMaxResults);
    }

    return q.getResultList();
  }

  @Override
  public Event createEvent(final String pActor, final EventType pType, final EventLevel pStatus, final String pDetails)
  {
    // set the uuid of the event
    final String uuid = UUID.randomUUID().toString();

    final EventEntity event = new EventEntity(uuid);
    event.setActor(pActor);
    event.setLevel(pStatus);
    event.setType(pType);
    event.setDetails(pDetails);
    event.setDate(new Date());
    return event;
  }

  @Override
  public int deleteEventsBeforeDate(final Date pDate)
  {
    final Query q = entityManager.createNamedQuery("EventEntity.deleteEventsFromDate");
    q.setParameter("date", pDate);
    return q.executeUpdate();
  }

}
