/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.core.organization.internal.services.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Attribute;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.UserProfileContact;
import org.novaforge.forge.core.organization.model.UserProfileWork;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.UserStatus;
import org.novaforge.forge.core.organization.services.CommonUserService;
import org.novaforge.forge.core.organization.services.LanguageService;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

/**
 * @author Marc Blachon
 */

@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class CommonUserServiceImplTest extends BaseUtil
{
  @Inject
  private CommonUserService    commonUserService;

  @Inject
  private LanguageService      languageService;

  private static final String  LANGUAGE_FR_NAME        = "FR";
  private static final String  LANGUAGE_EN_NAME        = "EN";

  // user1
  private static final String  NAME1                   = "name1";
  private static final String  FIRSTNAME1              = "firstname1";
  private static final String  LOGIN1                  = "name1-f";
  private static final String  EMAIL1                  = "mailUser1@toto.fr";
  private static final String  PASSWORD1               = "password1";

  // user2
  private static final String  NAME2                   = "name2";
  private static final String  FIRSTNAME2              = "firstname2";
  private static final String  LOGIN2                  = "name2-f";
  private static final String  EMAIL2                  = "mailUser2@toto.fr";
  private static final String  PASSWORD2               = "password2";

  // super user1
  private static final String  SUPERNAME1              = "supername1";
  private static final String  SUPERFIRSTNAME1         = "superfirstname1";
  private static final String  SUPERLOGIN1             = "superlogin1";
  private static final String  SUPEREMAIL1             = "";
  private static final String  SUPERPASSWORD1          = "superpassword1";

  // user profile contacts
  private static final boolean IS_PHONE_MOBILE_PUBLIC  = false;

  private static final String  PHONE_MOBILE            = "06 55 77 88 99";

  // user profile projects

  private static final boolean IS_PROJECTS_PUBLIC      = false;

  private static final String  PROJECTS_PUBLIC         = "projects for all ....";

  private static final boolean IS_COMPAGNY_ADDR_PUBLIC = false;

  private static final String  COMPAGNY_ADDR           = "11 rue de la moutarde";

  private static final boolean IS_NAME_PUBLIC          = false;

  private static final String  MY_NAME                 = "toto";

  private static final boolean IS_OFFICE_PUBLIC        = false;

  private static final String  OFFICE                  = "yakato";

  @Test
  // @Ignore
  public void testNewUser()
  {
    assertNotNull(commonUserService);
    final User user = commonUserService.newUser();
    assertNotNull(user);
  }

  @Test
  // @Ignore
  public void testExistUser() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);
    final boolean existUser1 = commonUserService.existUser(LOGIN1);
    assertTrue(existUser1);
  }

  @Test
  // @Ignore
  public void testCreateUser() throws UserServiceException, LanguageServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);

    commonUserService.createUser(user1);

    final User gotUser1 = commonUserService.getUser(LOGIN1);
    assertEquals(NAME1, gotUser1.getName());
    assertEquals(FIRSTNAME1, gotUser1.getFirstName());
    assertEquals(LOGIN1, gotUser1.getLogin());
    assertEquals(EMAIL1, gotUser1.getEmail());
  }

  @Test
  // @Ignore
  public void testCreateSuperUser() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User superUser1 = createUser(SUPERNAME1, SUPERFIRSTNAME1, SUPEREMAIL1, SUPERPASSWORD1);
    superUser1.setLogin(SUPERLOGIN1); // login must be set

    commonUserService.createSuperUser(superUser1);

    final User gotUser1 = commonUserService.getUser(SUPERLOGIN1);
    assertEquals(SUPERNAME1, gotUser1.getName());
    assertEquals(SUPERFIRSTNAME1, gotUser1.getFirstName());
    assertEquals(SUPERLOGIN1, gotUser1.getLogin());
    assertEquals(SUPEREMAIL1, gotUser1.getEmail());
  }

  @Test
  // @Ignore
  public void testGetUser() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);

    commonUserService.createUser(user1);
    commonUserService.createUser(user2);

    final User gotUser1 = commonUserService.getUser(LOGIN1);
    assertNotNull(gotUser1);
    assertEquals(NAME1, gotUser1.getName());
    assertEquals(FIRSTNAME1, gotUser1.getFirstName());
    assertEquals(LOGIN1, gotUser1.getLogin());
    assertEquals(EMAIL1, gotUser1.getEmail());

    final User gotUser2 = commonUserService.getUser(LOGIN2);
    assertNotNull(gotUser2);
    assertEquals(NAME2, gotUser2.getName());
    assertEquals(FIRSTNAME2, gotUser2.getFirstName());
    assertEquals(LOGIN2, gotUser2.getLogin());
    assertEquals(EMAIL1, gotUser1.getEmail());
  }

  @Test
  // @Ignore
  public void testIsBlacklistedUser() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    boolean result = commonUserService.isBlacklistedUser(LOGIN1);
    assertFalse(result);
    commonUserService.createUser(user1);
    commonUserService.deleteUser(LOGIN1);
    result = commonUserService.isBlacklistedUser(LOGIN1);
    assertTrue(result);
  }

  @Test
  // @Ignore
  public void testGetUserCredential() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);
    final String credential = commonUserService.getUserCredential(user1.getLogin());
    assertNotNull(credential);
  }

  @Test
  // @Ignore
  public void testUpdateUserProfile() throws UserServiceException, LanguageServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);

    // create user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    // initializing profile beans
    UserProfileContact userProfileContact = commonUserService.newUserProfileContact();
    Attribute userProfileProjects = commonUserService.newUserProfileProjects();
    UserProfileWork userProfileWork = commonUserService.newUserProfileWork();

    UserProfile userProfile = commonUserService.getUserProfile(LOGIN1);
    assertNotNull(userProfile);

    userProfile.setUserProfileContact(userProfileContact);
    userProfile.setUserProfileProjects(userProfileProjects);
    userProfile.setUserProfileWork(userProfileWork);

    // contacts
    userProfile.getUserProfileContact().getPhoneMobile().setIsPublic(IS_PHONE_MOBILE_PUBLIC);
    userProfile.getUserProfileContact().getPhoneMobile().setValue(PHONE_MOBILE);

    // projects
    userProfile.getUserProfileProjects().setIsPublic(IS_PROJECTS_PUBLIC);
    userProfile.getUserProfileProjects().setValue(PROJECTS_PUBLIC);

    // work
    userProfile.getUserProfileWork().getCompanyAddress().setIsPublic(IS_COMPAGNY_ADDR_PUBLIC);
    userProfile.getUserProfileWork().getCompanyAddress().setValue(COMPAGNY_ADDR);
    userProfile.getUserProfileWork().getCompanyName().setIsPublic(IS_NAME_PUBLIC);
    userProfile.getUserProfileWork().getCompanyName().setValue(MY_NAME);
    userProfile.getUserProfileWork().getCompanyOffice().setIsPublic(IS_OFFICE_PUBLIC);
    userProfile.getUserProfileWork().getCompanyOffice().setValue(OFFICE);

    commonUserService.updateUserProfile(userProfile);

    // check updated user profile
    UserProfile updatedUserProfile = commonUserService.getUserProfile(LOGIN1);
    assertNotNull(updatedUserProfile);

    assertEquals(IS_PHONE_MOBILE_PUBLIC, updatedUserProfile.getUserProfileContact().getPhoneMobile()
        .getIsPublic());
    assertEquals(PHONE_MOBILE, updatedUserProfile.getUserProfileContact().getPhoneMobile().getValue());

    assertEquals(IS_PROJECTS_PUBLIC, updatedUserProfile.getUserProfileProjects().getIsPublic());
    assertEquals(PROJECTS_PUBLIC, updatedUserProfile.getUserProfileProjects().getValue());

    assertEquals(IS_COMPAGNY_ADDR_PUBLIC, updatedUserProfile.getUserProfileWork().getCompanyAddress()
        .getIsPublic());
    assertEquals(COMPAGNY_ADDR, updatedUserProfile.getUserProfileWork().getCompanyAddress().getValue());

    assertEquals(IS_NAME_PUBLIC, updatedUserProfile.getUserProfileWork().getCompanyName().getIsPublic());
    assertEquals(MY_NAME, updatedUserProfile.getUserProfileWork().getCompanyName().getValue());

    assertEquals(IS_OFFICE_PUBLIC, updatedUserProfile.getUserProfileWork().getCompanyOffice().getIsPublic());
    assertEquals(OFFICE, updatedUserProfile.getUserProfileWork().getCompanyOffice().getValue());
  }

  @Test
  // @Ignore
  public void testDeleteUser() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);

    commonUserService.createUser(user1);
    final User gotUser1 = commonUserService.getUser(LOGIN1);
    assertNotNull(gotUser1);
    commonUserService.deleteUser(LOGIN1);

    try
    {
      commonUserService.getUser(LOGIN1);
      fail("should has raised an exception");
    }
    catch (final UserServiceException e)
    {
      System.out.println("expected exception!");
    }
  }

  @Test
  // @Ignore
  public void testDeleteUserProfile()
  {
    // idem testUpdateUserProfile()
  }

  @Test
  // @Ignore
  public void testGetBlacklistedUsers() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    final boolean result = commonUserService.isBlacklistedUser(LOGIN1);
    assertFalse(result);
    commonUserService.createUser(user1);
    commonUserService.deleteUser(LOGIN1);
    final List<BlacklistedUser> users = commonUserService.getBlacklistedUsers();
    assertNotNull(users);
    assertTrue(users.size() == 1);
    assertEquals(LOGIN1, users.get(0).getLogin());
  }

  @Test
  // @Ignore
  public void testActivateUser() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);
    final User gotUser1 = commonUserService.getUser(LOGIN1);
    final UserStatus userStatus = gotUser1.getStatus();
    assertEquals("to be activated", userStatus.getLabel());
    commonUserService.activateUser(gotUser1.getLogin(), "toto");
    final User gotUser1AfterActivate = commonUserService.getUser(LOGIN1);
    final UserStatus userStatusAfterActivate = gotUser1AfterActivate.getStatus();
    assertEquals("activated", userStatusAfterActivate.getLabel());
  }

  @Test
  // @Ignore
  public void testDesactivateUser() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);
    // initial status
    final User gotUser1 = commonUserService.getUser(LOGIN1);
    final UserStatus userStatus = gotUser1.getStatus();
    assertEquals("to be activated", userStatus.getLabel());
    // activate
    commonUserService.activateUser(gotUser1.getLogin(), "toto");
    final User gotUser1AfterActivate = commonUserService.getUser(LOGIN1);
    final UserStatus userStatusAfterActivate = gotUser1AfterActivate.getStatus();
    assertEquals("activated", userStatusAfterActivate.getLabel());
    // de-activate
    commonUserService.desactivateUser(gotUser1.getLogin(), "toto");
    final User gotUser1AfterDesActivate = commonUserService.getUser(LOGIN1);
    final UserStatus userStatusAfterDesActivate = gotUser1AfterDesActivate.getStatus();
    assertEquals("desactivated", userStatusAfterDesActivate.getLabel());
  }

  @Test
  // @Ignore
  public void testGetAllUsers() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);

    commonUserService.createUser(user1);
    commonUserService.createUser(user2);

    // with super user
    final List<User> usersT = commonUserService.getAllUsers(true);
    assertNotNull(usersT);
    assertTrue(usersT.size() == 3);

    // without super user
    final List<User> usersF = commonUserService.getAllUsers(false);
    assertNotNull(usersF);
    assertTrue(usersF.size() == 2);
  }

  @Test
  // @Ignore
  public void testSearchUsersByCriterias() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);

    commonUserService.createUser(user1);
    commonUserService.createUser(user2);

    commonUserService.getUser(LOGIN1);

    // equal criterias
    final Map<String, Object> equalCriterias = new HashMap<String, Object>();
    equalCriterias.put("realmType", RealmType.USER);
    final List<User> searched1users = commonUserService.searchUsersByCriterias(null, equalCriterias);
    assertNotNull(searched1users);
    assertEquals(2, searched1users.size());

    // like criterias
    final Map<String, Object> likeCriterias = new HashMap<String, Object>();
    likeCriterias.put("name", "name");
    final List<User> searched2users = commonUserService.searchUsersByCriterias(likeCriterias, null);
    assertNotNull(searched2users);
    assertEquals(2, searched2users.size());

  }

  @Test
  // @Ignore
  public void testRecoverUserPassword() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);

    commonUserService.createUser(user1);
    final User gotUser1 = commonUserService.getUser(LOGIN1);
    commonUserService.recoverUserPassword(EMAIL1);
    final User gotUser1Recovered = commonUserService.getUser(LOGIN1);
    assertTrue(gotUser1.getPassword().equals(gotUser1Recovered.getPassword()));
  }

  @Test
  // @Ignore
  public void testIsCandidateForUpdatingPassword() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);
    final boolean isCandidate = commonUserService.isCandidateForUpdatingPassword(LOGIN1);
    assertFalse(isCandidate);
    // TODO: see how programatically to force expiration time ??
  }

  @Test
  // @Ignore
  public void testCheckUser() throws LanguageServiceException
  {
    // check that the login is unique
    // check that the email is unique
    // constraints on bean: name, firstName, not empty and minimum 1; password not empty
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);

    // test unique email
    final User userNUEmail = createUser(NAME2, FIRSTNAME2, EMAIL1, PASSWORD2);
    try
    {
      commonUserService.createUser(user1);
      commonUserService.createUser(userNUEmail);
      fail("An exception ought to be raised");
    }
    catch (final UserServiceException e)
    {
      System.out.println("expected exception!");
    }

    // test unique login for super user
    final User superUser1 = createUser(SUPERNAME1, SUPERFIRSTNAME1, SUPEREMAIL1, SUPERPASSWORD1);
    superUser1.setLogin(SUPERLOGIN1); // login must be set
    final User superUserNULogin = createUser(SUPERNAME1 + "2", SUPERFIRSTNAME1 + "2", EMAIL2, SUPERPASSWORD1
        + "2");
    superUser1.setLogin(SUPERLOGIN1); // login must be set
    try
    {
      commonUserService.createSuperUser(superUser1);
      commonUserService.createSuperUser(superUserNULogin);
      fail("An exception ought to be raised");
    }
    catch (final UserServiceException e)
    {
      System.out.println("expected exception!");
    }

    // without password
    final User userWOPass = commonUserService.newUser();
    assertNotNull(userWOPass);
    userWOPass.setFirstName(FIRSTNAME2);
    userWOPass.setName(NAME2);
    userWOPass.setEmail(EMAIL2);
    Language langFR = languageService.getLanguage(LANGUAGE_FR_NAME);
    userWOPass.setLanguage(langFR);
    try
    {
      commonUserService.createUser(userWOPass);
      fail("An exception ought to be raised");
    }
    catch (final UserServiceException e)
    {
      System.out.println("expected exception!");
    }

    // without NAME
    final User userWOName = commonUserService.newUser();
    assertNotNull(userWOName);
    userWOName.setFirstName(FIRSTNAME2);
    userWOName.setEmail(EMAIL2);
    langFR = languageService.getLanguage(LANGUAGE_FR_NAME);
    userWOName.setLanguage(langFR);
    userWOName.setPassword(PASSWORD2);
    try
    {
      commonUserService.createUser(userWOName);
      fail("An exception ought to be raised");
    }
    catch (final UserServiceException e)
    {
      System.out.println("expected exception!");
    }

    // without FIRSTNAME
    final User userWOFirstName = commonUserService.newUser();
    assertNotNull(userWOFirstName);
    userWOFirstName.setName(NAME2);
    userWOFirstName.setEmail(EMAIL2);
    langFR = languageService.getLanguage(LANGUAGE_FR_NAME);
    userWOFirstName.setLanguage(langFR);
    userWOFirstName.setPassword(PASSWORD2);
    try
    {
      commonUserService.createUser(userWOFirstName);
      fail("An exception ought to be raised");
    }
    catch (final UserServiceException e)
    {
      System.out.println("expected exception!");
    }

  }

  @Test
  // @Ignore
  public void testGetUserFromRecoveryPasswordToken() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);

    commonUserService.createUser(user1);
    commonUserService.getUser(LOGIN1);
    // commonUserService.getUserFromRecoveryPasswordToken(pToken);
    fail("ask S.B. for method to get the token after password recovery");
  }

  @Test
  // @Ignore
  public void testUpdatePasswordFromRecovery() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);

    commonUserService.createUser(user1);
    commonUserService.getUser(LOGIN1);
    // commonUserService.updatePasswordFromRecovery(pToken, pPassword)
    fail("ask S.B. for method to get the token after password recovery");
  }

  @Test
  // @Ignore
  public void testGetUserProfile() throws LanguageServiceException, UserServiceException
  {
    assertNotNull(commonUserService);
    assertNotNull(languageService);

    // create user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    // initializing profile beans
    UserProfileContact userProfileContact = commonUserService.newUserProfileContact();
    Attribute userProfileProjects = commonUserService.newUserProfileProjects();
    UserProfileWork userProfileWork = commonUserService.newUserProfileWork();

    UserProfile userProfile = commonUserService.getUserProfile(LOGIN1);
    assertNotNull(userProfile);

    userProfile.setUserProfileContact(userProfileContact);
    userProfile.setUserProfileProjects(userProfileProjects);
    userProfile.setUserProfileWork(userProfileWork);

    // contacts
    userProfile.getUserProfileContact().getPhoneMobile().setIsPublic(IS_PHONE_MOBILE_PUBLIC);
    userProfile.getUserProfileContact().getPhoneMobile().setValue(PHONE_MOBILE);

    // projects
    userProfile.getUserProfileProjects().setIsPublic(IS_PROJECTS_PUBLIC);
    userProfile.getUserProfileProjects().setValue(PROJECTS_PUBLIC);

    // work
    userProfile.getUserProfileWork().getCompanyAddress().setIsPublic(IS_COMPAGNY_ADDR_PUBLIC);
    userProfile.getUserProfileWork().getCompanyAddress().setValue(COMPAGNY_ADDR);
    userProfile.getUserProfileWork().getCompanyName().setIsPublic(IS_NAME_PUBLIC);
    userProfile.getUserProfileWork().getCompanyName().setValue(MY_NAME);
    userProfile.getUserProfileWork().getCompanyOffice().setIsPublic(IS_OFFICE_PUBLIC);
    userProfile.getUserProfileWork().getCompanyOffice().setValue(OFFICE);

    commonUserService.updateUserProfile(userProfile);

    UserProfile updatedUserProfile = commonUserService.getUserProfile(LOGIN1);

    // check get user profile
    assertNotNull(updatedUserProfile);

    assertEquals(IS_PHONE_MOBILE_PUBLIC, updatedUserProfile.getUserProfileContact().getPhoneMobile()
        .getIsPublic());
    assertEquals(PHONE_MOBILE, updatedUserProfile.getUserProfileContact().getPhoneMobile().getValue());

    assertEquals(IS_PROJECTS_PUBLIC, updatedUserProfile.getUserProfileProjects().getIsPublic());
    assertEquals(PROJECTS_PUBLIC, updatedUserProfile.getUserProfileProjects().getValue());

    assertEquals(IS_COMPAGNY_ADDR_PUBLIC, updatedUserProfile.getUserProfileWork().getCompanyAddress()
        .getIsPublic());
    assertEquals(COMPAGNY_ADDR, updatedUserProfile.getUserProfileWork().getCompanyAddress().getValue());

    assertEquals(IS_NAME_PUBLIC, updatedUserProfile.getUserProfileWork().getCompanyName().getIsPublic());
    assertEquals(MY_NAME, updatedUserProfile.getUserProfileWork().getCompanyName().getValue());

    assertEquals(IS_OFFICE_PUBLIC, updatedUserProfile.getUserProfileWork().getCompanyOffice().getIsPublic());
    assertEquals(OFFICE, updatedUserProfile.getUserProfileWork().getCompanyOffice().getValue());
  }

  // private methods:
  /**
   * @return
   * @throws LanguageServiceException
   */
  private User createUser(final String name, final String firstName, final String eMail, final String password)
      throws LanguageServiceException
  {
    final User user = commonUserService.newUser();
    assertNotNull(user);
    user.setFirstName(firstName);
    user.setName(name);
    user.setEmail(eMail);
    final Language langFR = languageService.getLanguage(LANGUAGE_FR_NAME);
    user.setLanguage(langFR);
    user.setPassword(password);
    return user;
  }

}
