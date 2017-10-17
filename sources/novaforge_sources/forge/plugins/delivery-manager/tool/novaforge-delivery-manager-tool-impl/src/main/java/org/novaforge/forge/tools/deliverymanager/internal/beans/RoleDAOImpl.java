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
package org.novaforge.forge.tools.deliverymanager.internal.beans;

import org.novaforge.forge.tools.deliverymanager.dao.RoleDAO;
import org.novaforge.forge.tools.deliverymanager.entity.PermissionEntity;
import org.novaforge.forge.tools.deliverymanager.entity.PermissionEntity_;
import org.novaforge.forge.tools.deliverymanager.entity.RoleEntity;
import org.novaforge.forge.tools.deliverymanager.entity.RoleEntity_;
import org.novaforge.forge.tools.deliverymanager.model.Permission;
import org.novaforge.forge.tools.deliverymanager.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;

/**
 * @author sbenoist
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
  public Permission newPermission()
  {
    return new PermissionEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role findRoleByName(final String pName)
  {
    final TypedQuery<Role> q = entityManager.createNamedQuery("RoleEntity.findByName", Role.class);
    q.setParameter("name", pName);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Role> findAllRoles()
  {
    final CriteriaBuilder     builder     = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Role> allCriteria = builder.createQuery(Role.class);
    final Root<RoleEntity>    entityRoot  = allCriteria.from(RoleEntity.class);
    allCriteria.orderBy(builder.asc(entityRoot.get(RoleEntity_.name)));
    allCriteria.select(entityRoot);
    return new HashSet<Role>(entityManager.createQuery(allCriteria).getResultList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Permission findPermissionByName(final String pName)
  {
    final TypedQuery<Permission> q = entityManager.createNamedQuery("PermissionEntity.findByName", Permission.class);
    q.setParameter("name", pName);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existRole(final String pName)
  {
    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    final Root<RoleEntity> entityRoot = countCriteria.from(RoleEntity.class);
    countCriteria.select(builder.count(entityRoot));

    final Predicate predicate = builder.equal(entityRoot.get(RoleEntity_.name), pName);
    countCriteria.where(predicate);
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role persist(final Role pRole)
  {
    entityManager.persist(pRole);
    entityManager.flush();
    return pRole;
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
  public void delete(final Role pRole)
  {
    entityManager.remove(pRole);
    entityManager.flush();
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

    final Predicate predicate = builder.equal(entityRoot.get(PermissionEntity_.name), pName);
    countCriteria.where(predicate);
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }

}
