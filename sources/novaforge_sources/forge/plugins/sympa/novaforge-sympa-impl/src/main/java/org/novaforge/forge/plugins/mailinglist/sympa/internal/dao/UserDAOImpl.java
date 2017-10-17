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
package org.novaforge.forge.plugins.mailinglist.sympa.internal.dao;

import org.novaforge.forge.plugins.mailinglist.sympa.dao.UserDAO;
import org.novaforge.forge.plugins.mailinglist.sympa.entity.UserEntity;
import org.novaforge.forge.plugins.mailinglist.sympa.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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
    this.entityManager = pEntityManager;
  }

  /***************************************************
   * The following methods will manage User
   ***************************************************/

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existUser(final String pLogin)
  {
    boolean exists = false;
    try
    {
      final User user = findByLogin(pLogin);
      if (user != null)
      {
        exists = true;
      }
    }
    catch (final NoResultException e)
    {
      // we don't want to throw exception in case of the user doesn't exist
    }
    return exists;
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
  public User newUser(final String pLogin, final String pEmail)
  {
    return new UserEntity(pLogin, pEmail);
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
  public User findByEmail(final String pEmail)
  {
    final TypedQuery<User> q = entityManager.createNamedQuery("UserEntity.findByEmail", User.class);
    q.setParameter("email", pEmail);
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
  public void delete(final User pUser)
  {
    entityManager.remove(entityManager.merge(pUser));
    entityManager.flush();

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

}
