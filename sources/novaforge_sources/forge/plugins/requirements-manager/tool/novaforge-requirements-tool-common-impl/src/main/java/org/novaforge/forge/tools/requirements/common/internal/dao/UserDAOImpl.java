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
package org.novaforge.forge.tools.requirements.common.internal.dao;

import org.novaforge.forge.tools.requirements.common.dao.UserDAO;
import org.novaforge.forge.tools.requirements.common.entity.UserEntity;
import org.novaforge.forge.tools.requirements.common.entity.UserEntity_;
import org.novaforge.forge.tools.requirements.common.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author sbenoist
 */
public class UserDAOImpl implements UserDAO
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
  public User newUser()
  {
    return new UserEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean exist(final String pLogin)
  {
    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    final Root<UserEntity> entityRoot = countCriteria.from(UserEntity.class);
    countCriteria.select(builder.count(entityRoot));

    final Predicate predicate = builder.equal(entityRoot.get(UserEntity_.login), pLogin);
    countCriteria.where(predicate);
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User findByLogin(final String pLogin)
  {
    final TypedQuery<User> q = entityManager.createNamedQuery("UserEntity.findByLogin", User.class);
    q.setParameter("login", pLogin);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User persist(final User pUser)
  {
    entityManager.persist(pUser);
    entityManager.flush();
    return pUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User update(final User pUser)
  {
    entityManager.merge(pUser);
    entityManager.flush();
    return pUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final User pUser)
  {
    entityManager.remove(pUser);
    entityManager.flush();
  }

}
