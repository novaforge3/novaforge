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

import org.novaforge.forge.core.organization.dao.GroupDAO;
import org.novaforge.forge.core.organization.entity.GroupEntity;
import org.novaforge.forge.core.organization.entity.GroupEntity_;
import org.novaforge.forge.core.organization.entity.ProjectElementEntity_;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.GroupInfo;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA2 implementation of {@link GroupDAO}
 * 
 * @author BILET-JC
 * @author Guillaume Lamirand
 */
public class GroupDAOImpl implements GroupDAO
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
  public Group newGroup()
  {
    return new GroupEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Group findByProjectIdAndName(final String pProjectId, final String pName)
  {
    final TypedQuery<Group> q = entityManager.createNamedQuery("GroupEntity.findByNameAndProjectId",
        Group.class);
    q.setParameter("name", pName);
    q.setParameter("projectId", pProjectId);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Group findByUUID(final UUID pGroupUUID) throws NoResultException
  {
    if (pGroupUUID != null)
    {
      final TypedQuery<Group> q = entityManager.createNamedQuery("GroupEntity.findByUUID", Group.class);
      q.setParameter("uuid", pGroupUUID.toString());
      return q.getSingleResult();
    }
    else
    {
      throw new IllegalArgumentException("The given uuid shouldn't be null.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Group> findByVisibility(final boolean pVisibility)
  {

    final TypedQuery<Group> q = entityManager.createNamedQuery("GroupEntity.findByVisibility", Group.class);
    q.setParameter("visibility", pVisibility);
    return q.getResultList();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existGroup(final String pProjectId, final String pName)
  {

    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    final Root<GroupEntity> entityRoot = countCriteria.from(GroupEntity.class);
    countCriteria.select(builder.count(entityRoot));

    final Predicate loginPredicate = builder.equal(entityRoot.get(GroupEntity_.name), pName);
    final Predicate projectPredicate = builder.equal(
        entityRoot.get(GroupEntity_.project).get(ProjectElementEntity_.elementId), pProjectId);
    countCriteria.where(builder.and(loginPredicate, projectPredicate));
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existGroup(final UUID pUUID)
  {

    boolean existGroup = false;
    if (pUUID != null)
    {
      final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      final Root<GroupEntity> entityRoot = countCriteria.from(GroupEntity.class);
      countCriteria.select(builder.count(entityRoot));

      final Predicate uuidPredicate = builder.equal(entityRoot.get(GroupEntity_.uuid), pUUID.toString());
      countCriteria.where(uuidPredicate);
      final long count = entityManager.createQuery(countCriteria).getSingleResult();
      existGroup = (count > 0);
    }
    return existGroup;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Group> findGroupsForUser(final UUID pUserUUID)
  {
    final List<Group> groups = new ArrayList<Group>();
    if (pUserUUID != null)
    {
      final TypedQuery<Group> q = entityManager
          .createNamedQuery("GroupEntity.findGroupsForUser", Group.class);
      q.setParameter("uuid", pUserUUID.toString());
      final List<Group> resultList = q.getResultList();
      if (resultList != null)
      {
        groups.addAll(resultList);
      }
    }
    return groups;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GroupInfo> findGroupsInfosForUser(final UUID pUserUUID)
  {
    final List<GroupInfo> groups = new ArrayList<GroupInfo>();
    if (pUserUUID != null)
    {
      final TypedQuery<GroupInfo> q = entityManager.createNamedQuery("GroupEntity.findGroupsInfosForUser",
          GroupInfo.class);
      q.setParameter("uuid", pUserUUID.toString());
      final List<GroupInfo> resultList = q.getResultList();
      if (resultList != null)
      {
        groups.addAll(resultList);
      }
    }
    return groups;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UUID> findGroupsUUIDForUser(final UUID pUserUUID)
  {
    final List<UUID> returnList = new ArrayList<UUID>();
    if (pUserUUID != null)
    {
      final TypedQuery<String> q = entityManager.createNamedQuery("GroupEntity.findGroupsUUIDForUser",
          String.class);
      q.setParameter("uuid", pUserUUID.toString());
      final List<String> resultList = q.getResultList();
      if (resultList != null)
      {
        for (final String uuidString : resultList)
        {
          returnList.add(UUID.fromString(uuidString));
        }
      }
    }
    return returnList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Group> findByProjectWithPublic(final String pProjectId)
  {
    final TypedQuery<Group> q = entityManager.createNamedQuery("GroupEntity.findByProjectWithPublic",
        Group.class);
    q.setParameter("projectId", pProjectId);
    return q.getResultList();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Group> findByProjectWithoutPublic(final String pProjectId)
  {
    final TypedQuery<Group> q = entityManager.createNamedQuery("GroupEntity.findByProject", Group.class);
    q.setParameter("projectId", pProjectId);
    return q.getResultList();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Group update(final Group pGroup)
  {
    entityManager.merge(pGroup);
    entityManager.flush();
    return pGroup;
  }

}
