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
package org.novaforge.forge.commands.upgrade.users;

import de.nlh.graphics2dimages.FixedWidthGraphics2DImage;
import org.apache.camel.converter.IOConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.dao.UserDAO;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.ui.portal.client.util.UserIconGenerator;

import java.util.List;

/**
 * @author Jeremy Casery
 */
@Command(scope = "upgrade", name = "userprofiles",
    description = "Create userprofile for all users, and geenrate icon for users without one")
public class UpdateUserProfilesCommand extends OsgiCommandSupport
{
  private static final Log LOGGER = LogFactory.getLog(UpdateUserProfilesCommand.class);
  private ForgeConfigurationService forgeConfigurationService;
  private UserPresenter             userPresenter;
  private AuthentificationService   authentificationService;
  private HistorizationService      historizationService;
  private UserDAO                   userDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    // Disactivate historization if implemented
    historizationService.setActivatedMode(false);

    // login
    login();

    // Get all the projects
    final List<User> users = userPresenter.getAllUsers(true);
    for (final User user : users)
    {
      final String userLogin = user.getLogin();
      try
      {
        LOGGER.info(String.format("*** Create, if needed, UserProfile for user:%s", userLogin));
        userPresenter.getUserProfile(userLogin);
      }
      catch (final Exception e)
      {
        LOGGER.error(String.format("*** Create UserProfile for user:%s has failed", userLogin));
      }
    }
    final List<UserProfile> userProfiles = userPresenter.getAllUserProfiles(true);
    for (final UserProfile userProfile : userProfiles)
    {
      try
      {
        if (userProfile.getImage() == null)
        {
          LOGGER.info(String.format("*** Create user icon for user:%s", userProfile.getUser().getLogin()));
          FixedWidthGraphics2DImage iconFileForUser = UserIconGenerator.getIconFileForUser(userProfile
              .getUser().getFirstName(), userProfile.getUser().getName());
          BinaryFile newUserIcon = userPresenter.newUserIcon();
          newUserIcon.setFile(IOConverter.toBytes(iconFileForUser.getStream()));
          newUserIcon.setMimeType(iconFileForUser.getResource().getMIMEType());
          newUserIcon.setName(iconFileForUser.getResource().getFilename());
          userProfile.setImage(newUserIcon);
          userDAO.update(userProfile);
        }
      }
      catch (final Exception e)
      {
        LOGGER.error(String.format("*** Create user icon for user:%s has failed", userProfile.getUser()
            .getLogin()));
      }
    }
    LOGGER.info("*** Upgrade UserProfiles finished");
    // Logout
    authentificationService.logout();

    // re-activate historization
    historizationService.setActivatedMode(true);

    return null;
  }

  private void login() throws UserServiceException
  {
    final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();

    final org.novaforge.forge.core.organization.model.User user = userPresenter
        .getUser(superAdministratorLogin);
    authentificationService.login(superAdministratorLogin, user.getPassword());
  }

  /**
   * @param forgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }

  /**
   * @param pUserPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

  /**
   * @param pUserDAO
   *          the userDao to set
   */
  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
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
   * @param pHistorizationService
   *          the historizationService to set
   */
  public void setHistorizationService(final HistorizationService pHistorizationService)
  {
    historizationService = pHistorizationService;
  }
}
