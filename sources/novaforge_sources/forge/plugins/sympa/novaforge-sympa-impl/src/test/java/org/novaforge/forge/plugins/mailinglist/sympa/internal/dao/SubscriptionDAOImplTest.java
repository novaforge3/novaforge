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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.plugins.mailinglist.sympa.entity.GroupSubscriptionEntity;
import org.novaforge.forge.plugins.mailinglist.sympa.entity.UserEntity;
import org.novaforge.forge.plugins.mailinglist.sympa.entity.UserSubscriptionEntity;
import org.novaforge.forge.plugins.mailinglist.sympa.model.GroupSubscription;
import org.novaforge.forge.plugins.mailinglist.sympa.model.User;
import org.novaforge.forge.plugins.mailinglist.sympa.model.UserSubscription;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author sbenoist
 */
public class SubscriptionDAOImplTest extends PluginSympaJPATestCase
{
  private static final String      MAILING_LIST_NAME_ONE = "my-list-one";
  private static final String      MAILING_LIST_NAME_TWO = "my-list-two";
  private static final String      GROUP_NAME_ONE        = "groupOne";
  private static final String      GROUP_NAME_TWO        = "groupTwo";
  private static final String      USER_ONE_LOGIN        = "toto";
  private static final String      USER_ONE_EMAIL        = "toto@bull.net";
  private static final String      USER_TWO_LOGIN        = "tutu";
  private static final String      USER_TWO_EMAIL        = "tutu@bull.net";
  /*
   * constants declaration
   */
  private SubscriptionDAOImpl subscriptionDAOImpl;
  private UserDAOImpl         userDAOImpl;

  /**
   * 
   */
  public SubscriptionDAOImplTest()
  {
    super();
    // we need to change the table USER in another name in UserEntity.java to launch successfully thes tests
    final String property = System.getProperty("sympa.profile");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {

    super.setUp();
    subscriptionDAOImpl = new SubscriptionDAOImpl();
    subscriptionDAOImpl.setEntityManager(em);

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
    subscriptionDAOImpl = null;
    userDAOImpl = null;
  }

  @Test
  public void testFindGroupSubscriptions()
  {
    final UUID uuidOne = UUID.randomUUID();
    final GroupSubscriptionEntity groupSubscriptionOne = buildGroupSubscription(uuidOne,
        MAILING_LIST_NAME_ONE, GROUP_NAME_ONE);
    groupSubscriptionOne.addMember(createUser(USER_ONE_LOGIN, USER_ONE_EMAIL));
    groupSubscriptionOne.addMember(createUser(USER_TWO_LOGIN, USER_TWO_EMAIL));
    final UUID uuidTwo = UUID.randomUUID();
    final GroupSubscriptionEntity groupSubscriptionTwo = buildGroupSubscription(uuidTwo,
        MAILING_LIST_NAME_TWO, GROUP_NAME_TWO);
    final GroupSubscriptionEntity groupSubscriptionThree = buildGroupSubscription(uuidOne,
        MAILING_LIST_NAME_TWO, GROUP_NAME_ONE);
    em.getTransaction().begin();
    subscriptionDAOImpl.persist(groupSubscriptionOne);
    subscriptionDAOImpl.persist(groupSubscriptionTwo);
    subscriptionDAOImpl.persist(groupSubscriptionThree);
    em.getTransaction().commit();
    List<GroupSubscription> groupSubscriptions = subscriptionDAOImpl.findAllByGroup(uuidOne);
    assertNotNull(groupSubscriptions);
    assertThat(groupSubscriptions.size(), is(2));
    groupSubscriptions = subscriptionDAOImpl.findAllByList(MAILING_LIST_NAME_TWO);
    assertNotNull(groupSubscriptions);
    assertThat(groupSubscriptions.size(), is(2));
    final GroupSubscription groupSubOne = subscriptionDAOImpl.findByListAndGroup(MAILING_LIST_NAME_ONE,
        uuidOne);
    assertNotNull(groupSubOne);
    assertNotNull(groupSubOne.getMembers());
    assertThat(groupSubOne.getMembers().size(), is(2));
    groupSubscriptions = subscriptionDAOImpl.findAllByListAndUser(MAILING_LIST_NAME_ONE, USER_ONE_LOGIN);
    assertNotNull(groupSubscriptions);
    assertThat(groupSubscriptions.size(), is(1));
    groupSubscriptions = subscriptionDAOImpl.findAllByListAndUser(MAILING_LIST_NAME_TWO, USER_ONE_LOGIN);
    assertNotNull(groupSubscriptions);
    assertThat(groupSubscriptions.size(), is(0));
  }

  private GroupSubscriptionEntity buildGroupSubscription(final UUID pUUID, final String pListName,
                                                         final String pGroupName)
  {
    return (GroupSubscriptionEntity) subscriptionDAOImpl.newGroupSubscription(pUUID, pGroupName, pListName);
  }

  private UserEntity createUser(final String pLogin, final String pEmail)
  {
    final UserEntity user = new UserEntity(pLogin, pEmail);
    em.getTransaction().begin();
    final UserEntity persistedUser = (UserEntity) userDAOImpl.persist(user);
    em.getTransaction().commit();

    return persistedUser;
  }

  @Test
  public void testUpdateGroupSubscription()
  {
    final UUID uuidOne = UUID.randomUUID();
    final GroupSubscriptionEntity groupSubscriptionOne = buildGroupSubscription(uuidOne,
        MAILING_LIST_NAME_ONE, GROUP_NAME_ONE);
    groupSubscriptionOne.addMember(createUser(USER_ONE_LOGIN, USER_ONE_EMAIL));
    groupSubscriptionOne.addMember(createUser(USER_TWO_LOGIN, USER_TWO_EMAIL));
    em.getTransaction().begin();
    subscriptionDAOImpl.persist(groupSubscriptionOne);
    em.getTransaction().commit();
    final GroupSubscription groupSubOne = subscriptionDAOImpl.findByListAndGroup(MAILING_LIST_NAME_ONE,
        uuidOne);
    assertNotNull(groupSubOne);
    assertNotNull(groupSubOne.getMembers());
    assertThat(groupSubOne.getMembers().size(), is(2));
    final User userOne = userDAOImpl.findByLogin(USER_ONE_LOGIN);
    assertNotNull(userOne);
    groupSubOne.removeMember(userOne);
    em.getTransaction().begin();
    final GroupSubscription returnedSubscription = subscriptionDAOImpl.update(groupSubOne);
    em.getTransaction().commit();
    assertNotNull(returnedSubscription);
    assertNotNull(returnedSubscription.getMembers());
    assertThat(groupSubOne.getMembers().size(), is(1));
  }

  @Test
  public void testDeleteGroupSubscription()
  {
    final UUID uuidOne = UUID.randomUUID();
    final GroupSubscriptionEntity groupSubscriptionOne = buildGroupSubscription(uuidOne,
        MAILING_LIST_NAME_ONE, GROUP_NAME_ONE);
    groupSubscriptionOne.addMember(createUser(USER_ONE_LOGIN, USER_ONE_EMAIL));
    groupSubscriptionOne.addMember(createUser(USER_TWO_LOGIN, USER_TWO_EMAIL));
    em.getTransaction().begin();
    subscriptionDAOImpl.persist(groupSubscriptionOne);
    em.getTransaction().commit();
    final GroupSubscription groupSubOne = subscriptionDAOImpl.findByListAndGroup(MAILING_LIST_NAME_ONE,
        uuidOne);
    assertNotNull(groupSubOne);
    assertNotNull(groupSubOne.getMembers());
    assertThat(groupSubOne.getMembers().size(), is(2));
    em.getTransaction().begin();
    subscriptionDAOImpl.delete(groupSubOne);
    em.getTransaction().commit();
    final boolean exist = subscriptionDAOImpl.existGroupSubscription(MAILING_LIST_NAME_ONE, uuidOne);
    assertNotNull(exist);
    assertThat(exist, is(false));
    final User userOne = userDAOImpl.findByLogin(USER_ONE_LOGIN);
    assertNotNull(userOne);
  }

  @Test
  public void testFindUserSubscription()
  {
    final UserSubscriptionEntity userSubscriptionOne = buildUserSubscription(USER_ONE_LOGIN,
        MAILING_LIST_NAME_ONE);
    em.getTransaction().begin();
    subscriptionDAOImpl.persist(userSubscriptionOne);
    em.getTransaction().commit();
    final UserSubscription userSubscriptionReturned = subscriptionDAOImpl.findByListAndUser(
        MAILING_LIST_NAME_ONE, USER_ONE_LOGIN);
    assertNotNull(userSubscriptionReturned);
    final boolean exist = subscriptionDAOImpl.existUserSubscription(MAILING_LIST_NAME_ONE,
        USER_ONE_LOGIN);
    assertNotNull(exist);
    assertThat(exist, is(true));
  }

  private UserSubscriptionEntity buildUserSubscription(final String pLogin, final String pListName)
  {
    return (UserSubscriptionEntity) subscriptionDAOImpl.newUserSubscription(pLogin, pListName);
  }

  @Test
  public void testDeleteUserSubscription()
  {
    final UserSubscriptionEntity userSubscriptionOne = buildUserSubscription(USER_ONE_LOGIN,
        MAILING_LIST_NAME_ONE);
    em.getTransaction().begin();
    subscriptionDAOImpl.persist(userSubscriptionOne);
    em.getTransaction().commit();
    final UserSubscription userSubscriptionReturned = subscriptionDAOImpl.findByListAndUser(
        MAILING_LIST_NAME_ONE, USER_ONE_LOGIN);
    assertNotNull(userSubscriptionReturned);
    em.getTransaction().begin();
    subscriptionDAOImpl.delete(userSubscriptionReturned);
    em.getTransaction().commit();
    assertNotNull(userSubscriptionReturned);
    final boolean exist = subscriptionDAOImpl.existUserSubscription(MAILING_LIST_NAME_ONE,
        USER_ONE_LOGIN);
    assertNotNull(exist);
    assertThat(exist, is(false));
  }
}
