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
package org.novaforge.forge.distribution.reporting.internal.scheduler;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.distribution.reporting.exceptions.ForgeReportingException;
import org.novaforge.forge.distribution.reporting.services.ForgeExtractionService;

/**
 * @author sbenoist
 */
public class ExtractionProcessor implements Processor
{

  private static final Log          LOGGER = LogFactory.getLog(ExtractionProcessor.class);

  private ForgeExtractionService    forgeExtractionService;
  private AuthentificationService   authentificationService;
  private ForgeConfigurationService forgeConfigurationService;
  private UserPresenter             userPresenter;

  @Override
  public void process(final Exchange exchange) throws Exception
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Starting Extraction.");
    }
    login();
    try
    {
      forgeExtractionService.startExtraction();
    }
    finally
    {
      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
  }

  private void login() throws ForgeReportingException
  {
    try
    {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userPresenter.getUser(superAdministratorLogin);
      authentificationService.login(superAdministratorLogin, user.getPassword());
    }
    catch (final UserServiceException e)
    {
      throw new ForgeReportingException("Unable to authenticate super administrator", e);
    }
  }

  /**
   * @param pForgeExtractionService
   *          the forgeExtractionService to set
   */
  public void setForgeExtractionService(final ForgeExtractionService pForgeExtractionService)
  {
    forgeExtractionService = pForgeExtractionService;
  }

  /**
   * @param pAuthentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  /**
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * @param pUserPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

}
