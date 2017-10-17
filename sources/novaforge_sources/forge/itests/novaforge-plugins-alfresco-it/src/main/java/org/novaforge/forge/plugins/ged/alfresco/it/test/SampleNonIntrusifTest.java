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
package org.novaforge.forge.plugins.ged.alfresco.it.test;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.LanguagePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.it.commons.test.NonIntrusiveDBTest;
import org.osgi.framework.ServiceReference;

import java.util.Arrays;
import java.util.List;

public class SampleNonIntrusifTest extends NonIntrusiveDBTest
{
  public static final String        USER1_EMAIL                  = "";
  public static final String        USER1_FIRSTNAME              = "user1_firstname";
  public static final String        USER1_NAME                   = "user1_name";
  public static final String        USER1_LOGIN_GENERATED        = "user1_name-u";
  public static final String        USER1_PASSWORD               = "7084d7d35a39bac9cd7888734e4c666e8632ca41";
  private static final String FR_LANG = "FR";
  private static final String       NF_DB_OSGI_JNDI_SERVICE_NAME = "osgi:service/javax.sql.DataSource/(osgi.jndi.service.name=jdbc/novaforge)";
  private AuthentificationService   authentificationService;
  private ForgeConfigurationService forgeConfigurationService;
  private UserPresenter             userPresenter;
  private LanguagePresenter         languagePresenter;

  /*
   * because of inheritance of NonIntrusiveDBTest for this test class following test can be ran again and again. 
   */
  public void testNonIntrusifSample01() throws Exception
  {
    ServiceReference userPresenterReference = getServiceReference(UserPresenter.class.getName());
    assertNotNull(userPresenterReference);
    this.userPresenter = (UserPresenter) getServiceObject(userPresenterReference);
    assertNotNull(this.userPresenter);
    
    ServiceReference languagePresenterReference = getServiceReference(LanguagePresenter.class.getName());
    assertNotNull(languagePresenterReference);
    this.languagePresenter = (LanguagePresenter) getServiceObject(languagePresenterReference);
    assertNotNull(this.languagePresenter);    
    
    Language language = this.languagePresenter.getLanguage(FR_LANG);
    User user1 = this.userPresenter.newUser();
    user1.setEmail(USER1_EMAIL);
    user1.setFirstName(USER1_FIRSTNAME);
    user1.setName(USER1_NAME);
    user1.setPassword(USER1_PASSWORD);
    user1.setLanguage(language);

    this.userPresenter.createUser(user1);
    User user = this.userPresenter.getUser(USER1_LOGIN_GENERATED);
    assertNotNull(user);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected List<String> getDatabasesJndiName()
  {
    return Arrays.asList(NF_DB_OSGI_JNDI_SERVICE_NAME);
  }

  private void login() throws Exception
  {
    ServiceReference serviceReference = getServiceReference(ForgeIdentificationService.class.getName());
    assertNotNull("ServiceReference for ForgeConfigurationManager instance should not be null",
        serviceReference);

    final ForgeIdentificationService forgeIdentificationService = (ForgeIdentificationService) getServiceObject(serviceReference);
    assertNotNull("ForgeConfigurationManager instance should not be null", forgeIdentificationService);

    serviceReference = getServiceReference(AuthentificationService.class.getName());
    assertNotNull("ServiceReference for AuthentificationService instance should not be null",
        serviceReference);

    final AuthentificationService authentificationService = (AuthentificationService) getServiceObject(serviceReference);
    assertNotNull("AuthentificationService instance should not be null", authentificationService);

    this.authentificationService = authentificationService;

    // forgeConfigurationService
    serviceReference = getServiceReference(ForgeConfigurationService.class.getName());
    assertNotNull("ServiceReference for AuthentificationService instance should not be null",
        serviceReference);
    final ForgeConfigurationService forgeConfigurationService = (ForgeConfigurationService) getServiceObject(serviceReference);
    assertNotNull("AuthentificationService instance should not be null", forgeConfigurationService);

    this.forgeConfigurationService = forgeConfigurationService;

    ServiceReference userPresenterReference = getServiceReference(UserPresenter.class.getName());
    assertNotNull(userPresenterReference);
    UserPresenter userPresenter = (UserPresenter) getServiceObject(userPresenterReference);
    assertNotNull(userPresenter);

    // authentication
    final String adminLogin = forgeConfigurationService.getSuperAdministratorLogin();
    final User user = userPresenter.getUser(adminLogin);
    authentificationService.login(adminLogin, user.getPassword());
  }

  private void logout() throws Exception
  {
    assertNotNull("The authentication service should not be null", authentificationService);
    if (authentificationService.checkLogin())
    {
      authentificationService.logout();
    }
  }

}
