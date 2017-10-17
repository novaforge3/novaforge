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

import org.novaforge.forge.plugins.mailinglist.sympa.dao.SubscriptionDAO;
import org.novaforge.forge.plugins.mailinglist.sympa.entity.GroupSubscriptionEntity;
import org.novaforge.forge.plugins.mailinglist.sympa.entity.UserSubscriptionEntity;
import org.novaforge.forge.plugins.mailinglist.sympa.model.GroupSubscription;
import org.novaforge.forge.plugins.mailinglist.sympa.model.User;
import org.novaforge.forge.plugins.mailinglist.sympa.model.UserSubscription;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sbenoist
 */
public class SubscriptionDAOImpl implements SubscriptionDAO
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

  /**
   * {@inheritDoc}
   */
  @Override
  public GroupSubscription newGroupSubscription(final UUID pGroupUUID, final String pGroupName,
      final String pListName)
  {
    return new GroupSubscriptionEntity(pGroupUUID, pGroupName, pListName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GroupSubscription> findAllByGroup(final UUID pGroupUUID)
  {
    final TypedQuery<GroupSubscription> q = entityManager.createNamedQuery("GroupSubscriptionEntity.findByGroupUUID",
                                                                           GroupSubscription.class);
    q.setParameter("groupUUID", pGroupUUID.toString());
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GroupSubscription> findAllByList(final String pListName)
  {
    final TypedQuery<GroupSubscription> q = entityManager.createNamedQuery(
        "GroupSubscriptionEntity.findByListName", GroupSubscription.class);
    q.setParameter("listname", pListName);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GroupSubscription> findAllByListAndUser(final String pListName, final String pLogin)
  {
    final List<GroupSubscription> returnedList = new ArrayList<GroupSubscription>();
    try
    {
      final TypedQuery<User> q = entityManager.createNamedQuery("UserEntity.findByLogin", User.class);
      q.setParameter("login", pLogin);
      final User user = q.getSingleResult();

      final TypedQuery<GroupSubscription> q2 = entityManager
                                                   .createNamedQuery("GroupSubscriptionEntity.findByListNameAndUser",
                                                                     GroupSubscription.class);
      q2.setParameter("listname", pListName);
      q2.setParameter("user", user);
      returnedList.addAll(q2.getResultList());
    }
    catch (final NoResultException e)
    {
      // Nothing to do
    }

    return returnedList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GroupSubscription persist(final GroupSubscription pGroupSubscription)
  {
    entityManager.persist(pGroupSubscription);
    entityManager.flush();
    return pGroupSubscription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final GroupSubscription pGroupSubscription)
  {
    entityManager.remove(entityManager.merge(pGroupSubscription));
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GroupSubscription update(final GroupSubscription pGroupSubscription)
  {
    entityManager.merge(pGroupSubscription);
    entityManager.flush();
    return pGroupSubscription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GroupSubscription findByListAndGroup(final String pListName, final UUID pGroupUUID)
  {
    final TypedQuery<GroupSubscription> q = entityManager
                                                .createNamedQuery("GroupSubscriptionEntity.findByListNameAndGroupUUID",
                                                                  GroupSubscription.class);
    q.setParameter("listname", pListName);
    q.setParameter("groupUUID", pGroupUUID.toString());
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existGroupSubscription(final String pListName, final UUID pGroupUUID)
  {
    boolean exists = false;
    try
    {
      final GroupSubscription subscription = findByListAndGroup(pListName, pGroupUUID);
      if (subscription != null)
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
  public UserSubscription newUserSubscription(final String pLogin, final String pListName)
  {
    return new UserSubscriptionEntity(pLogin, pListName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserSubscription persist(final UserSubscription pUserSubscription)
  {
    entityManager.persist(pUserSubscription);
    entityManager.flush();
    return pUserSubscription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final UserSubscription pUserSubscription)
  {
    entityManager.remove(entityManager.merge(pUserSubscription));
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserSubscription findByListAndUser(final String pListName, final String pLogin)
  {
    final TypedQuery<UserSubscription> q = entityManager.createNamedQuery(
        "UserSubscriptionEntity.findByListNameAndLogin", UserSubscription.class);
    q.setParameter("listname", pListName);
    q.setParameter("login", pLogin);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existUserSubscription(final String pListName, final String pLogin)
  {
    boolean exists = false;
    try
    {
      final UserSubscription subscription = findByListAndUser(pListName, pLogin);
      if (subscription != null)
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

}
