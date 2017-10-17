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
package org.novaforge.forge.configuration.initialization.internal.creator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.configuration.initialization.exceptions.ForgeInitializationException;
import org.novaforge.forge.configuration.initialization.internal.properties.InitializationProperties;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.LanguagePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;

/**
 * @author Guillaume Lamirand
 */
public class UserCreator
{
  private static final Log         LOGGER = LogFactory.getLog(UserCreator.class);
  private UserPresenter            userPresenter;
  private LanguagePresenter        languagePresenter;
  private InitializationProperties initializationProperties;

  public void createSuperAdmin() throws ForgeInitializationException
  {
    LOGGER.info("User 'super administration' is being created.");
    try
    {
      final User administrator = userPresenter.newUser();
      administrator.setLogin(initializationProperties.getSuperAdministratorLogin());
      administrator.setName(initializationProperties.getSuperAdministratorName());
      administrator.setFirstName(initializationProperties.getSuperAdministratorFirstName());
      administrator.setEmail(initializationProperties.getSuperAdministratorEmail());
      administrator.setPassword(initializationProperties.getSuperAdministratorPassword());
      administrator.setLanguage(languagePresenter.getLanguage(initializationProperties
          .getSuperAdministratorLanguage()));
      userPresenter.createSuperUser(administrator);
    }
    catch (final UserServiceException | LanguageServiceException e)
    {
      throw new ForgeInitializationException("Unable to create super administrator", e);
    }

    LOGGER.info("User 'super administration' created.");
  }

  public void setLanguagePresenter(final LanguagePresenter pLanguagePresenter)
  {
    languagePresenter = pLanguagePresenter;
  }

  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

  public void setInitializationProperties(final InitializationProperties pInitializationProperties)
  {
    initializationProperties = pInitializationProperties;
  }

}
