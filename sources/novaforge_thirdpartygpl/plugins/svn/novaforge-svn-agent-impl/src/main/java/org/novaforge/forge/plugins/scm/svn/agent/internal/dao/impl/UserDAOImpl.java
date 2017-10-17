/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.internal.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.novaforge.forge.plugins.scm.svn.agent.dao.UserDAO;
import org.novaforge.forge.plugins.scm.svn.domain.User;

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
  public User findByName(final String pName) throws NoResultException
  {
    TypedQuery<User> q = entityManager.createNamedQuery("UserEntity.findByName", User.class);
    q.setParameter("name", pName);
    return q.getSingleResult();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existUser(final String pName)
  {
    boolean exists = false;
    TypedQuery<User> q = entityManager.createNamedQuery("UserEntity.findByName", User.class);
    q.setParameter("name", pName);
    List<User> users = q.getResultList();
    if ((users != null) && (users.isEmpty() == false))
    {
      exists = true;
    }
    return exists;
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
