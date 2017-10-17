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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.core.organization.entity.BlacklistedUserEntity;
import org.novaforge.forge.core.organization.entity.LanguageEntity;
import org.novaforge.forge.core.organization.entity.RecoveryPasswordEntity;
import org.novaforge.forge.core.organization.entity.UserEntity;
import org.novaforge.forge.core.organization.entity.UserProfileContactEntity;
import org.novaforge.forge.core.organization.entity.UserProfileEntity;
import org.novaforge.forge.core.organization.entity.UserProfileWorkEntity;
import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.core.organization.model.RecoveryPassword;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.UserProfileContact;
import org.novaforge.forge.core.organization.model.UserProfileWork;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.UserStatus;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Guillaume Lamirand
 */
public class UserDAOImplTest extends OrganizationJPATestCase
{

  /*
   * Constants declaration
   */
  private static final String     NAME      = "name";
  private static final String     FIRSTNAME = "firstname";
  private static final String     PWD       = "pwd";
  private static final RealmType  TYPE      = RealmType.USER;
  private static final UserStatus STATUS    = UserStatus.ACTIVATED;
  private static final String     FR        = "fr";
  private UserDAOImpl             userDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    userDAOImpl = new UserDAOImpl();
    userDAOImpl.setEntityManager(em);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    userDAOImpl = null;
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#findUserByLogin(java.lang.String)}.
   */
  @Test
  public final void testFindByLogin()
  {
    // Persist two metadata with different id
    final UserEntity entityToFound = buildEntity("user1", "user1@email.com");
    final UserEntity entity        = buildEntity("user2", "user2@email.com");
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(2);

    final User user = userDAOImpl.findByLogin("user1");
    assertNotNull(user);
    assertThat(user.getLogin(), is("user1"));
    assertThat(user.getEmail(), is("user1@email.com"));
    assertThat(user.getPassword(), is(PWD));
    assertThat(user.getLanguage().getName(), is(FR));
    assertThat(user.getStatus(), is(STATUS));
    assertThat(user.getRealmType(), is(TYPE));
  }

  private UserEntity buildEntity(final String pLogin, final String pEmail)
  {
    final TypedQuery<LanguageEntity> query = em.createQuery("SELECT l FROM LanguageEntity l",
        LanguageEntity.class);
    final List<LanguageEntity> resultList = query.getResultList();
    LanguageEntity language = null;
    if ((resultList == null) || (resultList.isEmpty()))
    {
      if (!em.getTransaction().isActive())
      {
        em.getTransaction().begin();
      }
      language = new LanguageEntity();
      language.setName(FR);
      em.persist(language);
      em.getTransaction().commit();
    }
    else
    {
      language = resultList.get(0);
    }

    final UserEntity entity = new UserEntity();
    entity.setLogin(pLogin);
    entity.setEmail(pEmail);
    entity.setName(NAME);
    entity.setFirstName(FIRSTNAME);
    entity.setPassword(PWD);
    entity.setCreated(new Date());
    entity.setRealmType(TYPE);
    entity.setStatus(STATUS);
    entity.setLanguage(language);
    return entity;
  }

  /**
   * Check the number of {@link UserEntity} persisted
   *
   * @param pNumber
   *          the number to match
   */
  private void checkUserNummber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM UserEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  /**
   * // * Test method for
   * // *
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#findPassword(java.lang.String)}.
   * //
   */
  @Test
  public final void testFindPassword()
  {
    // Persist two metadata with different id
    final UserEntity entityToFound = buildEntity("user1", "user1@email.com");
    final UserEntity entity = buildEntity("user2", "user2@email.com");
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(2);

    final String pwd = userDAOImpl.findPassword("user1");
    assertNotNull(pwd);
    assertThat(pwd, is(PWD));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#findByEmail(java.lang.String)}.
   */
  @Test
  public final void testFindByEmail()
  {

    // Persist two metadata with different id
    final UserEntity entityToFound = buildEntity("user1", "user1@email.com");
    final UserEntity entity = buildEntity("user2", "user2@email.com");
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(2);

    final User user = userDAOImpl.findByEmail("user1@email.com");
    assertNotNull(user);
    assertThat(user.getLogin(), is("user1"));
    assertThat(user.getEmail(), is("user1@email.com"));
    assertThat(user.getPassword(), is(PWD));
    assertThat(user.getLanguage().getName(), is(FR));
    assertThat(user.getStatus(), is(STATUS));
    assertThat(user.getRealmType(), is(TYPE));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#existEmail(java.lang.String)}.
   */
  @Test
  public final void testExistEmail()
  {

    // Persist two metadata with different id
    final UserEntity entityToFound = buildEntity("user1", "user1@email.com");
    final UserEntity entity = buildEntity("user2", "user2@email.com");
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(2);

    final boolean email1 = userDAOImpl.existEmail("user1@email.com");
    assertThat(email1, is(true));
    final boolean email2 = userDAOImpl.existEmail("user2@email.com");
    assertThat(email2, is(true));
    final boolean email3 = userDAOImpl.existEmail("user3@email.com");
    assertThat(email3, is(false));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#existLogin(java.lang.String)}.
   */
  @Test
  public final void testExistLogin()
  {
    // Persist two metadata with different id
    final UserEntity entityToFound = buildEntity("user1", "user1@email.com");
    final UserEntity entity = buildEntity("user2", "user2@email.com");
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(2);

    final boolean login1 = userDAOImpl.existLogin("user1");
    assertThat(login1, is(true));
    final boolean login2 = userDAOImpl.existLogin("user2");
    assertThat(login2, is(true));
    final boolean login3 = userDAOImpl.existLogin("user3");
    assertThat(login3, is(false));
  }

  /**
   * Test method for {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#findAllUser()}.
   */
  @Test
  public final void testFindAllUser()
  {
    // Persist two metadata with different id
    final UserEntity entityToFound = buildEntity("user1", "user1@email.com");
    final UserEntity entity = buildEntity("user2", "user2@email.com");
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(2);

    final List<User> user = userDAOImpl.findAllUser();
    assertNotNull(user);
    assertThat(user.size(), is(2));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#persist(org.novaforge.forge.core.organization.model.User)}
   * .
   */
  @Test
  public final void testPersistUser()
  {
    final UserEntity entity = buildEntity("user1", "user1@email.com");
    em.getTransaction().begin();
    userDAOImpl.persist(entity);
    em.getTransaction().commit();
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#update(org.novaforge.forge.core.organization.model.User)}
   * .
   */
  @Test
  public final void testUpdateUser()
  {
    // Persist two metadata with different id
    final UserEntity entityToFound = buildEntity("user1", "user1@email.com");
    final UserEntity entity = buildEntity("user2", "user2@email.com");
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(2);

    final User user = userDAOImpl.findByEmail("user1@email.com");
    assertNotNull(user);
    assertThat(user.getLogin(), is("user1"));
    assertThat(user.getEmail(), is("user1@email.com"));
    assertThat(user.getPassword(), is(PWD));
    assertThat(user.getLanguage().getName(), is(FR));
    assertThat(user.getStatus(), is(STATUS));
    assertThat(user.getRealmType(), is(TYPE));

    user.setEmail("user3@email.com");
    em.getTransaction().begin();
    userDAOImpl.update(user);
    em.getTransaction().commit();
    final User userUpdated = userDAOImpl.findByEmail("user3@email.com");
    assertNotNull(userUpdated);
    assertThat(userUpdated.getLogin(), is("user1"));
    assertThat(userUpdated.getEmail(), is("user3@email.com"));
    assertThat(userUpdated.getPassword(), is(PWD));
    assertThat(userUpdated.getLanguage().getName(), is(FR));
    assertThat(userUpdated.getStatus(), is(STATUS));
    assertThat(userUpdated.getRealmType(), is(TYPE));
    try
    {
      userDAOImpl.findByEmail("user1@email.com");
      fail("An user was found");
    }
    catch (final NoResultException e)
    {
      // Ok
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#delete(org.novaforge.forge.core.organization.model.User)}
   * .
   */
  @Test
  public final void testDeleteUser()
  {
    // Persist two metadata with different id
    final UserEntity entityToFound = buildEntity("user1", "user1@email.com");
    final UserEntity entity = buildEntity("user2", "user2@email.com");
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(2);

    final User user = userDAOImpl.findByEmail("user1@email.com");
    assertNotNull(user);
    assertThat(user.getLogin(), is("user1"));
    assertThat(user.getEmail(), is("user1@email.com"));
    assertThat(user.getPassword(), is(PWD));
    assertThat(user.getLanguage().getName(), is(FR));
    assertThat(user.getStatus(), is(STATUS));
    assertThat(user.getRealmType(), is(TYPE));

    em.getTransaction().begin();
    userDAOImpl.delete(user);
    em.getTransaction().commit();
    try
    {
      userDAOImpl.findByEmail("user1@email.com");
      fail("An user was found");
    }
    catch (final NoResultException e)
    {
      // Ok
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#findBlacklistedByLogin(java.lang.String)}
   * .
   */
  @Test
  public final void testFindBlacklistedByLogin()
  {
    // Persist two metadata with different id
    final BlacklistedUserEntity entityToFound = new BlacklistedUserEntity();
    entityToFound.setLogin("user1");
    entityToFound.setEmail("user1@email.com");
    entityToFound.setCreationDate(new Date());
    final BlacklistedUserEntity entity = new BlacklistedUserEntity();
    entity.setLogin("user2");
    entity.setEmail("user2@email.com");
    entity.setCreationDate(new Date());
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkBlacklistedUserNummber(2);

    final BlacklistedUser user = userDAOImpl.findBlacklistedByLogin("user1");
    assertNotNull(user);
    assertThat(user.getLogin(), is("user1"));
    assertThat(user.getEmail(), is("user1@email.com"));

  }

  /**
   * Check the number of {@link BlacklistedUserEntity} persisted
   *
   * @param pNumber
   *          the number to match
   */
  private void checkBlacklistedUserNummber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM BlacklistedUserEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#findAllBlacklisted()}.
   */
  @Test
  public final void testFindAllBlacklisted()
  {
    // Persist two metadata with different id
    final BlacklistedUserEntity entityToFound = new BlacklistedUserEntity();
    entityToFound.setLogin("user1");
    entityToFound.setEmail("user1@email.com");
    entityToFound.setCreationDate(new Date());
    final BlacklistedUserEntity entity = new BlacklistedUserEntity();
    entity.setLogin("user2");
    entity.setEmail("user2@email.com");
    entity.setCreationDate(new Date());
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkBlacklistedUserNummber(2);

    final List<BlacklistedUser> user = userDAOImpl.findAllBlacklisted();
    assertNotNull(user);
    assertThat(user.size(), is(2));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#newBlacklistedUser()}.
   */
  @Test
  public final void testNewBlacklistedUser()
  {
    final BlacklistedUser newBlacklistedUser = userDAOImpl.newBlacklistedUser();
    assertNotNull(newBlacklistedUser);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#persist(org.novaforge.forge.core.organization.model.BlacklistedUser)}
   * .
   */
  @Test
  public final void testPersistBlacklistedUser()
  {
    // Persist two metadata with different id
    final BlacklistedUserEntity entityToFound = new BlacklistedUserEntity();
    entityToFound.setLogin("user1");
    entityToFound.setEmail("user1@email.com");
    entityToFound.setCreationDate(new Date());
    em.getTransaction().begin();
    userDAOImpl.persist(entityToFound);
    em.getTransaction().commit();
    checkBlacklistedUserNummber(1);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#findByToken(java.lang.String)}.
   */
  @Test
  public final void testFindByToken()
  {
    // Persist two user with different id
    final UserEntity user1 = buildEntity("user1", "user1@email.com");
    final UserEntity user2 = buildEntity("user2", "user2@email.com");
    em.getTransaction().begin();
    em.persist(user1);
    em.persist(user2);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(2);

    // Persist two metadata with different id
    final RecoveryPasswordEntity entityToFound = new RecoveryPasswordEntity();
    entityToFound.setUser(user1);
    final UUID uuid = entityToFound.getToken();
    final RecoveryPasswordEntity entity = new RecoveryPasswordEntity();
    entity.setUser(user2);
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.persist(entity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkRecoveryNummber(2);

    final RecoveryPassword findByToken = userDAOImpl.findByToken(uuid);
    assertNotNull(findByToken);
    assertThat(findByToken.getUser(), is((User) user1));
    try
    {
      userDAOImpl.findByToken(UUID.randomUUID());
      fail("A NoResultExpception should be thrown");
    }
    catch (final NoResultException e)
    {
      // Ok
    }
  }

  /**
   * Check the number of {@link RecoveryPasswordEntity} persisted
   *
   * @param pNumber
   *     the number to match
   */
  private void checkRecoveryNummber(final int pNumber)
  {
    final TypedQuery<Long> query = em.createQuery("SELECT count(e) FROM RecoveryPasswordEntity e", Long.class);
    assertThat(query.getSingleResult(), is(new Long(pNumber)));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#newRecoveryPassword()}.
   */
  @Test
  public final void testNewRecoveryPassword()
  {
    final RecoveryPassword newRecoveryPassword = userDAOImpl.newRecoveryPassword();
    assertNotNull(newRecoveryPassword);
    assertNotNull(newRecoveryPassword.getToken());
    assertNotNull(newRecoveryPassword.getExpirationDate());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#persist(org.novaforge.forge.core.organization.model.RecoveryPassword)}
   * .
   */
  @Test
  public final void testPersistRecoveryPassword()
  {
    // Persist two user with different id
    final UserEntity user = buildEntity("user1", "user1@email.com");
    em.getTransaction().begin();
    em.persist(user);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(1);

    // Persist two metadata with different id
    final RecoveryPasswordEntity entityToFound = new RecoveryPasswordEntity();
    entityToFound.setUser(user);
    em.getTransaction().begin();
    userDAOImpl.persist(entityToFound);
    em.getTransaction().commit();
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#update(org.novaforge.forge.core.organization.model.RecoveryPassword)}
   * .
   */
  @Test
  public final void testUpdateRecoveryPassword()
  {

    // Persist two user with different id
    final UserEntity user1 = buildEntity("user1", "user1@email.com");
    em.getTransaction().begin();
    em.persist(user1);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(1);

    // Persist two metadata with different id
    final RecoveryPasswordEntity entityToFound = new RecoveryPasswordEntity();
    entityToFound.setUser(user1);
    final UUID uuid = entityToFound.getToken();
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkRecoveryNummber(1);

    final RecoveryPassword findByToken = userDAOImpl.findByToken(uuid);
    assertNotNull(findByToken);
    assertThat(findByToken.getUser(), is((User) user1));

    final UUID randomUUID = UUID.randomUUID();
    ((RecoveryPasswordEntity) findByToken).setToken(randomUUID);
    em.getTransaction().begin();
    userDAOImpl.update(findByToken);
    em.getTransaction().commit();
    final RecoveryPassword newReco = userDAOImpl.findByToken(randomUUID);
    assertNotNull(newReco);
    assertThat(newReco.getUser(), is((User) user1));

  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#delete(org.novaforge.forge.core.organization.model.RecoveryPassword)}
   * .
   */
  @Test
  public final void testDeleteRecoveryPassword()
  {
    // Persist two user with different id
    final UserEntity user1 = buildEntity("user1", "user1@email.com");
    em.getTransaction().begin();
    em.persist(user1);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(1);

    // Persist two metadata with different id
    final RecoveryPasswordEntity entityToFound = new RecoveryPasswordEntity();
    entityToFound.setUser(user1);
    final UUID uuid = entityToFound.getToken();
    em.getTransaction().begin();
    em.persist(entityToFound);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkRecoveryNummber(1);

    final RecoveryPassword findByToken = userDAOImpl.findByToken(uuid);
    assertNotNull(findByToken);
    assertThat(findByToken.getUser(), is((User) user1));

    em.getTransaction().begin();
    userDAOImpl.delete(findByToken);
    em.getTransaction().commit();
    try
    {
      userDAOImpl.findByToken(uuid);
      fail("A NoResultExpception should be thrown");
    }
    catch (final NoResultException e)
    {
      // Ok
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.dao.UserDAOImpl#findProfileByLogin(java.lang.String)}
   * .
   */
  @Test
  public final void testFindProfileByLogin()
  {
    // Persist two metadata with different id
    final UserEntity userToFindEntity = buildEntity("user1", "user1@email.com");
    final UserEntity userEntity = buildEntity("user2", "user2@email.com");

    em.getTransaction().begin();
    em.persist(userToFindEntity);
    em.persist(userEntity);
    em.getTransaction().commit();
    // Check if the previous event have been correctly persisted
    checkUserNummber(2);

    final User user1 = userDAOImpl.findByLogin("user1");
    final UserProfileEntity userProfileEntity = new UserProfileEntity();
    userProfileEntity.setUser(user1);

    final UserProfileContact userProfileContact = new UserProfileContactEntity();
    userProfileContact.getPhoneMobile().setValue("06 12 34 56 78");
    userProfileContact.getPhoneMobile().setIsPublic(true);
    userProfileContact.getPhoneWork().setValue("04 12 34 56 78");
    userProfileContact.getPhoneWork().setIsPublic(false);
    userProfileEntity.setUserProfileContact(userProfileContact);

    final UserProfileWork userProfileWork = new UserProfileWorkEntity();
    userProfileWork.getCompanyName().setValue("Bull SAS");
    userProfileWork.getCompanyName().setIsPublic(true);
    userProfileWork.getCompanyAddress().setValue("1 rue de provence, 38130 Echirolles");
    userProfileWork.getCompanyAddress().setIsPublic(true);
    userProfileWork.getCompanyOffice().setValue("B1-100");
    userProfileWork.getCompanyOffice().setIsPublic(true);
    userProfileEntity.setUserProfileWork(userProfileWork);

    em.getTransaction().begin();
    em.persist(userProfileEntity);
    em.getTransaction().commit();

    final UserProfile userProfile = userDAOImpl.findProfileByLogin("user1");
    assertNotNull(userProfile);
    assertThat(userProfile.getUser().getLogin(), is("user1"));
    assertThat(userProfile.getUser().getEmail(), is("user1@email.com"));
    assertThat(userProfile.getUser().getPassword(), is(PWD));
    assertThat(userProfile.getUser().getLanguage().getName(), is(FR));
    assertThat(userProfile.getUser().getStatus(), is(STATUS));
    assertThat(userProfile.getUser().getRealmType(), is(TYPE));
    assertThat(userProfile.getUserProfileContact().getPhoneMobile().getValue(), is(userProfileEntity
        .getUserProfileContact().getPhoneMobile().getValue()));
    assertThat(userProfile.getUserProfileContact().getPhoneMobile().getIsPublic(), is(userProfileEntity
        .getUserProfileContact().getPhoneMobile().getIsPublic()));
    assertThat(userProfile.getUserProfileContact().getPhoneWork().getValue(), is(userProfileEntity
        .getUserProfileContact().getPhoneWork().getValue()));
    assertThat(userProfile.getUserProfileContact().getPhoneWork().getIsPublic(), is(userProfileEntity
        .getUserProfileContact().getPhoneWork().getIsPublic()));
    assertThat(userProfile.getUserProfileWork().getCompanyName().getValue(), is(userProfileEntity
        .getUserProfileWork().getCompanyName().getValue()));
    assertThat(userProfile.getUserProfileWork().getCompanyName().getIsPublic(), is(userProfileEntity
        .getUserProfileWork().getCompanyName().getIsPublic()));
    assertThat(userProfile.getUserProfileWork().getCompanyAddress().getValue(), is(userProfileEntity
        .getUserProfileWork().getCompanyAddress().getValue()));
    assertThat(userProfile.getUserProfileWork().getCompanyAddress().getIsPublic(), is(userProfileEntity
        .getUserProfileWork().getCompanyAddress().getIsPublic()));
    assertThat(userProfile.getUserProfileWork().getCompanyOffice().getValue(), is(userProfileEntity
        .getUserProfileWork().getCompanyOffice().getValue()));
    assertThat(userProfile.getUserProfileWork().getCompanyOffice().getIsPublic(), is(userProfileEntity
        .getUserProfileWork().getCompanyOffice().getIsPublic()));
  }

}
