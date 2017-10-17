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

import org.novaforge.forge.core.organization.dao.UserDAO;
import org.novaforge.forge.core.organization.entity.AttributeEntity;
import org.novaforge.forge.core.organization.entity.BinaryFileEntity;
import org.novaforge.forge.core.organization.entity.BlacklistedUserEntity;
import org.novaforge.forge.core.organization.entity.RecoveryPasswordEntity;
import org.novaforge.forge.core.organization.entity.UserEntity;
import org.novaforge.forge.core.organization.entity.UserEntity_;
import org.novaforge.forge.core.organization.entity.UserProfileContactEntity;
import org.novaforge.forge.core.organization.entity.UserProfileEntity;
import org.novaforge.forge.core.organization.entity.UserProfileWorkEntity;
import org.novaforge.forge.core.organization.model.Attribute;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.core.organization.model.RecoveryPassword;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.UserProfileContact;
import org.novaforge.forge.core.organization.model.UserProfileWork;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * JPA2 implementation of {@link UserDAO}
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

  /***************************************************
   * The following methods will manage User
   ***************************************************/
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
  public List<User> findAllUser()
  {
    final CriteriaBuilder     builder       = entityManager.getCriteriaBuilder();
    final CriteriaQuery<User> countCriteria = builder.createQuery(User.class);
    final Root<UserEntity>    entityRoot    = countCriteria.from(UserEntity.class);
    countCriteria.select(entityRoot);
    return entityManager.createQuery(countCriteria).getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UserProfile> findAllUserProfile()
  {
    final CriteriaBuilder            builder       = entityManager.getCriteriaBuilder();
    final CriteriaQuery<UserProfile> countCriteria = builder.createQuery(UserProfile.class);
    final Root<UserProfileEntity>    entityRoot    = countCriteria.from(UserProfileEntity.class);
    countCriteria.select(entityRoot);
    return entityManager.createQuery(countCriteria).getResultList();
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
  public User findByUUID(final UUID pUserUUID)
  {
    if (pUserUUID != null)
    {
      final TypedQuery<User> q = entityManager.createNamedQuery("UserEntity.findByUUID", User.class);
      q.setParameter("uuid", pUserUUID.toString());
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
  public String findPassword(final String pLogin)
  {
    final TypedQuery<String> q = entityManager.createNamedQuery("UserEntity.findCredential", String.class);
    q.setParameter("login", pLogin);
    return q.getSingleResult();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existEmail(final String pEmail)
  {
    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    final Root<UserEntity> entityRoot = countCriteria.from(UserEntity.class);
    countCriteria.select(builder.count(entityRoot));

    final Predicate emailPredicate = builder.equal(entityRoot.get(UserEntity_.email), pEmail);
    countCriteria.where(emailPredicate);
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existLogin(final String pLogin)
  {
    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
    final Root<UserEntity> entityRoot = countCriteria.from(UserEntity.class);
    countCriteria.select(builder.count(entityRoot));

    final Predicate loginPredicate = builder.equal(entityRoot.get(UserEntity_.login), pLogin);
    countCriteria.where(loginPredicate);
    final long count = entityManager.createQuery(countCriteria).getSingleResult();

    return (count > 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existUserWith(final UUID pUUID)
  {
    boolean existUser = false;
    if (pUUID != null)
    {
      final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      final Root<UserEntity> entityRoot = countCriteria.from(UserEntity.class);
      countCriteria.select(builder.count(entityRoot));

      final Predicate loginPredicate = builder.equal(entityRoot.get(UserEntity_.uuid), pUUID.toString());
      countCriteria.where(loginPredicate);
      final long count = entityManager.createQuery(countCriteria).getSingleResult();
      existUser = (count > 0);
    }
    return existUser;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public BlacklistedUser newBlacklistedUser()
  {
    return new BlacklistedUserEntity();
  }

  /***************************************************
   * The following methods will manage BlacklistedUser
   ***************************************************/

  /**
   * {@inheritDoc}
   */
  @Override
  public BlacklistedUser persist(final BlacklistedUser pBlacklistedUser)
  {
    entityManager.persist(pBlacklistedUser);
    entityManager.flush();
    return pBlacklistedUser;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlacklistedUser findBlacklistedByLogin(final String pLogin)
  {
    final TypedQuery<BlacklistedUser> q = entityManager.createNamedQuery("BlacklistedUserEntity.findByLogin",
        BlacklistedUser.class);
    q.setParameter("login", pLogin);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BlacklistedUser> findAllBlacklisted()
  {
    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<BlacklistedUser> allCriteria = builder.createQuery(BlacklistedUser.class);
    final Root<BlacklistedUserEntity> entityRoot = allCriteria.from(BlacklistedUserEntity.class);
    allCriteria.select(entityRoot);
    return entityManager.createQuery(allCriteria).getResultList();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RecoveryPassword findByToken(final UUID pToken)
  {
    final TypedQuery<RecoveryPassword> q = entityManager.createNamedQuery("RecoveryPasswordEntity.findByToken",
                                                                          RecoveryPassword.class);
    q.setParameter("token", pToken.toString());
    return q.getSingleResult();
  }

  /***************************************************
   * The following methods will manage RecoveryPassword
   ***************************************************/

/**
   * {@inheritDoc}
   */
  @Override
  public List <RecoveryPassword> findByUser(User pUser) 
  {
    final TypedQuery<RecoveryPassword> q = entityManager.createNamedQuery(
        "RecoveryPasswordEntity.findByUser", RecoveryPassword.class);
    q.setParameter("email", pUser.getEmail());
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RecoveryPassword newRecoveryPassword()
  {
    return new RecoveryPasswordEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RecoveryPassword persist(final RecoveryPassword pRecoveryPassword)
  {
    entityManager.persist(pRecoveryPassword);
    entityManager.flush();
    return pRecoveryPassword;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RecoveryPassword update(final RecoveryPassword pRecoveryPassword)
  {
    entityManager.merge(pRecoveryPassword);
    entityManager.flush();
    return pRecoveryPassword;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final RecoveryPassword pRecoveryPassword)
  {
    entityManager.remove(pRecoveryPassword);
    entityManager.flush();

  }

  /***************************************************
   * The following methods will manage UserProfile
   ***************************************************/

  @Override
  public List<User> searchUserByCriterias(final Map<String, Object> likeCriterias,
                                          final Map<String, Object> equalCriterias)
  {

    final CriteriaBuilder     criteriaBuilder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<User> criteriaQuery   = criteriaBuilder.createQuery(User.class);
    final Root<UserEntity>    from            = criteriaQuery.from(UserEntity.class);
    final CriteriaQuery<User> select          = criteriaQuery.select(from);

    final List<Predicate> predicates = new ArrayList<Predicate>();
    if (likeCriterias != null)
    {
      for (final Map.Entry<String, Object> entry : likeCriterias.entrySet())
      {
        predicates.add(criteriaBuilder.like(from.<String>get(entry.getKey()), "%" + (String) entry.getValue() + "%"));
      }
    }

    if (equalCriterias != null)
    {
      for (final Map.Entry<String, Object> entry : equalCriterias.entrySet())
      {
        predicates.add(criteriaBuilder.equal(from.<String>get(entry.getKey()), entry.getValue()));
      }
    }

    final Predicate[] tab = predicates.toArray(new Predicate[predicates.size()]);
    criteriaQuery.where(tab);

    final TypedQuery<User> typedQuery = entityManager.createQuery(select);
    return typedQuery.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile persist(final UserProfile pUserProfile)
  {
    entityManager.persist(pUserProfile);
    entityManager.flush();
    return pUserProfile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile update(final UserProfile pUserProfile)
  {
    entityManager.merge(pUserProfile);
    entityManager.flush();
    return pUserProfile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final UserProfile pUserProfile)
  {
    entityManager.remove(pUserProfile);
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final UserProfileContact pUserProfileContact)
  {
    entityManager.remove(pUserProfileContact);
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final UserProfileWork pUserProfileWork)
  {
    entityManager.remove(pUserProfileWork);
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final Attribute pAttribute)
  {
    entityManager.remove(pAttribute);
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfileWork newUserProfileWork()
  {
    return new UserProfileWorkEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfileContact newUserProfileContact()
  {
    return new UserProfileContactEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile newUserProfile()
  {
    return new UserProfileEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BinaryFile newUserIcon()
  {
    return new BinaryFileEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile findProfileByLogin(final String pUserLogin)
  {
    final TypedQuery<UserProfile> q = entityManager.createNamedQuery("UserProfileEntity.findProfileByLogin",
                                                                     UserProfile.class);
    q.setParameter("login", pUserLogin);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile findProfileByEmail(final String pUserEmail)
  {
    final TypedQuery<UserProfile> q = entityManager.createNamedQuery("UserProfileEntity.findProfileByEmail",
                                                                     UserProfile.class);
    q.setParameter("email", pUserEmail);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile newUserProfile(final User pUser)
  {
    return new UserProfileEntity(pUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute newUserProfileProjects()
  {
    return new AttributeEntity();
  }

}
