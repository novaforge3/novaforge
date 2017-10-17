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
package org.novaforge.forge.core.organization.internal.dao;

import org.novaforge.forge.core.organization.dao.RoleDAO;
import org.novaforge.forge.core.organization.entity.PermissionEntity;
import org.novaforge.forge.core.organization.entity.PermissionEntity_;
import org.novaforge.forge.core.organization.entity.ProjectElementEntity_;
import org.novaforge.forge.core.organization.entity.ProjectRoleEntity;
import org.novaforge.forge.core.organization.entity.RoleEntity;
import org.novaforge.forge.core.organization.entity.RoleEntity_;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * JPA2 implementation of {@link RoleDAO}
 * 
 * @author Guillaume Lamirand
 */
public class RoleDAOImpl implements RoleDAO
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

  /****************************************
   * The following methods will manage Role
   ****************************************/

  /**
   * {@inheritDoc}
   */
  @Override
  public Role newRole()
  {
    return new RoleEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectRole newProjectRole()
  {
    return new ProjectRoleEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Role> findAllRole(final String pElementId)
  {
    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Role> countCriteria = builder.createQuery(Role.class);
    final Root<RoleEntity> entityRoot = countCriteria.from(RoleEntity.class);
    countCriteria.select(entityRoot);
    countCriteria.where(builder.equal(entityRoot.get(RoleEntity_.element)
        .get(ProjectElementEntity_.elementId), pElementId));
    return entityManager.createQuery(countCriteria).getResultList();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role findByNameAndElement(final String pElementId, final String pRoleName)
  {
    final TypedQuery<Role> q = entityManager.createNamedQuery("RoleEntity.findByNameAndProjectElement",
        Role.class);
    q.setParameter("elementId", pElementId);
    q.setParameter("name", pRoleName);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existRole(final String pElementId, final String pRoleName)
  {

    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    final Root<RoleEntity> entityRoot = countCriteria.from(RoleEntity.class);
    countCriteria.select(builder.count(entityRoot));

    final Predicate loginPredicate = builder.equal(entityRoot.get(RoleEntity_.name), pRoleName);
    // Cannot be done using jpa metamodel
    final Predicate projectPredicate = builder.equal(
        entityRoot.get(RoleEntity_.element).get(ProjectElementEntity_.elementId), pElementId);
    countCriteria.where(builder.and(loginPredicate, projectPredicate));
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getMaxOrder(final String pTemplateId)
  {
    final TypedQuery<Integer> q = entityManager.createNamedQuery("RoleEntity.getMaxOrder", Integer.class);
    q.setParameter("elementId", pTemplateId);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role changeOrder(final String pElementId, final String pRoleName, final boolean pIncrease)
  {
    // Get current role object
    final Role role = findByNameAndElement(pElementId, pRoleName);
    final Integer lastOrder = role.getOrder();
    Integer newOrder = 0;
    if (pIncrease)
    {
      newOrder = lastOrder + 1;
    }
    else
    {
      newOrder = lastOrder - 1;
    }

    // Get other role object regarding new order
    final Role otherRole = findByElementAndOrder(pElementId, newOrder);

    // Update role order
    if (otherRole != null)
    {
      role.setOrder(newOrder);
      otherRole.setOrder(lastOrder);
      update(otherRole);
    }
    return update(role);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int updateOrdersBiggerThan(final String pTemplateId, final Integer pOrder)
  {
    final Query q = entityManager.createNamedQuery("RoleEntity.updateOrder");
    q.setParameter("order", pOrder);
    q.setParameter("elementId", pTemplateId);
    return q.executeUpdate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role findByElementAndOrder(final String pTemplateId, final Integer pOrder)
  {
    final TypedQuery<Role> q = entityManager.createNamedQuery("RoleEntity.findByOrderAndProjectElement", Role.class);
    q.setParameter("elementId", pTemplateId);
    q.setParameter("order", pOrder);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role update(final Role pRole)
  {
    entityManager.merge(pRole);
    entityManager.flush();
    return pRole;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final Role pRole)
  {
    entityManager.remove(pRole);
    entityManager.flush();

  }

  /****************************************
   * The following methods will manage Permission
   ****************************************/
  /**
   * {@inheritDoc}
   */
  @Override
  public Permission findByName(final String pName)
  {
    final TypedQuery<Permission> q = entityManager.createNamedQuery("PermissionEntity.findByName",
        Permission.class);
    q.setParameter("name", pName);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Permission newPermission(final String pName)
  {
    return new PermissionEntity(pName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existPermission(final String pName)
  {

    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    final Root<PermissionEntity> entityRoot = countCriteria.from(PermissionEntity.class);
    countCriteria.select(builder.count(entityRoot));

    final Predicate namePredicate = builder.equal(entityRoot.get(PermissionEntity_.name), pName);
    countCriteria.where(namePredicate);
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Permission persist(final Permission pPermission)
  {
    entityManager.persist(pPermission);
    entityManager.flush();
    return pPermission;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Permission update(final Permission pPermission)
  {
    entityManager.merge(pPermission);
    entityManager.flush();
    return pPermission;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final Permission pPermission)
  {
    final Permission merge = entityManager.merge(pPermission);
    entityManager.remove(merge);
    entityManager.flush();

  }

}
