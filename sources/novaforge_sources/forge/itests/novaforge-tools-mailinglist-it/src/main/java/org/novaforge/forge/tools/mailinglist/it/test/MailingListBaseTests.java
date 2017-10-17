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
package org.novaforge.forge.tools.mailinglist.it.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.presenters.GroupPresenter;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListCategoryService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.tools.mailinglist.it.test.data.XmlData;
import org.osgi.framework.ServiceReference;

/**
 * @author blachonm
 */

public class MailingListBaseTests extends OSGiTestCase
{
  private static final Log LOG = LogFactory.getLog(MailingListBaseTests.class);
  protected MailingListCategoryService mailingListCategoryService;
  protected ForgeConfigurationService  forgeConfigurationService;
  protected ForgeIdentificationService forgeIdentificationService;
  protected ProjectPresenter           projectPresenter;
  protected UserPresenter              userPresenter;
  protected GroupPresenter             groupPresenter;
  protected PluginsManager             pluginsManager;
  protected AuthentificationService    authentificationService;
  protected ApplicationService         applicationService;
  protected MembershipPresenter        membershipPresenter;
  protected XmlData                    xmlData;

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    forgeConfigurationServiceInitial();
    projectPresenterInitial();
    applicationServiceInitial();
    pluginsManagerInitial();
    userPresenterInitial();
    groupPresenterInitial();
    membershipPresenterInitial();
    authentificationServiceInitial();
    forgeIdentificationServiceInitial();
    mailingListCategoryServiceInitial();
    xmlData = new XmlData();
  }

  @Override
  public void tearDown() throws Exception
  {
    super.tearDown();
  }

  private void forgeConfigurationServiceInitial()
  {
    final ServiceReference forgeConfigurationServiceRef = getServiceReference(ForgeConfigurationService.class
        .getName());
    assertNotNull(forgeConfigurationServiceRef);
    forgeConfigurationService = (ForgeConfigurationService) getServiceObject(forgeConfigurationServiceRef);
    assertNotNull(forgeConfigurationService);
  }

  private void projectPresenterInitial()
  {
    final ServiceReference projectReference = getServiceReference(ProjectPresenter.class.getName());
    assertNotNull(projectReference);
    projectPresenter = (ProjectPresenter) getServiceObject(projectReference);
    assertNotNull(projectPresenter);
  }

  private void applicationServiceInitial()
  {
    final ServiceReference aplicationServiceReference = getServiceReference(ApplicationService.class
        .getName());
    assertNotNull("ServiceReference for PluginsManager instance should not be null",
        aplicationServiceReference);
    applicationService = (ApplicationService) getServiceObject(aplicationServiceReference);
    assertNotNull("ApplicationService instance should not be null", applicationService);
  }

  private void pluginsManagerInitial()
  {
    final ServiceReference pluginManagerReference = getServiceReference(PluginsManager.class.getName());
    assertNotNull(pluginManagerReference);
    pluginsManager = (PluginsManager) getServiceObject(pluginManagerReference);
    assertNotNull(pluginsManager);
  }

  private void userPresenterInitial()
  {
    final ServiceReference userPresenterReference = getServiceReference(UserPresenter.class.getName());
    assertNotNull(userPresenterReference);
    userPresenter = (UserPresenter) getServiceObject(userPresenterReference);
    assertNotNull(userPresenter);
  }

  private void groupPresenterInitial()
  {
    final ServiceReference groupPresenterReference = getServiceReference(GroupPresenter.class.getName());
    assertNotNull(groupPresenterReference);
    groupPresenter = (GroupPresenter) getServiceObject(groupPresenterReference);
    assertNotNull(groupPresenter);
  }

  private void membershipPresenterInitial()
  {
    final ServiceReference membershipPresenterReference = getServiceReference(MembershipPresenter.class
        .getName());
    assertNotNull(membershipPresenterReference);
    membershipPresenter = (MembershipPresenter) getServiceObject(membershipPresenterReference);
    assertNotNull(membershipPresenter);
  }

  private void authentificationServiceInitial()
  {
    final ServiceReference authentificationServiceRef = getServiceReference(AuthentificationService.class
        .getName());
    assertNotNull(authentificationServiceRef);
    authentificationService = (AuthentificationService) getServiceObject(authentificationServiceRef);
    assertNotNull(authentificationService);
  }

  private void forgeIdentificationServiceInitial()
  {
    final ServiceReference serviceReference = getServiceReference(ForgeIdentificationService.class.getName());
    assertNotNull("ServiceReference for ForgeIdentificationManager instance should not be null", serviceReference);
    forgeIdentificationService = (ForgeIdentificationService) getServiceObject(serviceReference);
    assertNotNull("ForgeIdentificationManager instance should not be null", forgeIdentificationService);
  }

  private void mailingListCategoryServiceInitial()
  {
    final ServiceReference mailingListCategoryServiceRef = getServiceReference(MailingListCategoryService.class
        .getName());
    assertNotNull(mailingListCategoryServiceRef);
    mailingListCategoryService = (MailingListCategoryService) getServiceObject(mailingListCategoryServiceRef);
    assertNotNull(mailingListCategoryService);
  }

}
