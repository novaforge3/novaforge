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
package org.novaforge.forge.distribution.register.client.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.distribution.register.client.DistributionNotificationService;
import org.novaforge.forge.distribution.register.domain.ForgeDTO;
import org.novaforge.forge.distribution.register.exceptions.ExceptionCode;
import org.novaforge.forge.distribution.register.exceptions.ForgeDistributionException;
import org.novaforge.forge.reference.tool.ReferenceToolService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service called when a forge is added or removed from the distribution tree.
 * 
 * @author rols-p
 */
public class DistributionNotificationServiceImpl implements DistributionNotificationService
{

  private static final Log          LOGGER = LogFactory.getLog(DistributionNotificationServiceImpl.class);

  private ForgeConfigurationService forgeConfigurationService;
  private MembershipPresenter       membershipPresenter;
  private AuthentificationService   authentificationService;
  private UserPresenter             userPresenter;
  private ReferenceToolService      referenceToolService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void subscriptionApproved(final ForgeDTO forge, final String centralForgePublicKey)
      throws ForgeDistributionException
  {
    LOGGER.info(String.format("Distribution: subscription has been approved for the forge %s, level=%s",
        forge.getLabel(), forge.getForgeLevel()));

    login();

    final Set<String> roles = new HashSet<String>();
    roles.add(forgeConfigurationService.getReferentielMemberRoleName());

    updateSuperAdminMembership(roles);

    storeCentralForgePublicKey(centralForgePublicKey);

    authentificationService.logout();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unsubscriptionApproved(final ForgeDTO forge, final String centralForgePublicKey)
      throws ForgeDistributionException
  {
    LOGGER.info(String.format("Distribution: Unsubscription has been approved for the forge %s, level=%s",
        forge.getLabel(), forge.getForgeLevel()));
    login();
    final Set<String> roles = new HashSet<String>();
    roles.add(forgeConfigurationService.getForgeAdministratorRoleName());

    updateSuperAdminMembership(roles);

    removeCentralForgePublicKey(centralForgePublicKey);

    authentificationService.logout();
  }

  private void removeCentralForgePublicKey(final String centralPublicKey) throws ForgeDistributionException
  {
    try
    {
      LOGGER.info("Removing central forge public key from authorized_keys file...");

      if (centralPublicKey != null)
      {
        LOGGER.info(String.format("Trying to remove the central forge public key: %s", centralPublicKey));
        final File inputFile = new File(referenceToolService.getAuthorizedCertificatePublicKeysFile());
        final File tempFile = new File(referenceToolService.getAuthorizedCertificatePublicKeysFile() + ".tmp");

        final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        final PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

        String currentLine;
        while ((currentLine = reader.readLine()) != null)
        {
          final String trimmedLine = currentLine.trim();
          if (trimmedLine.equals(centralPublicKey))
          {
            LOGGER.info("Removing the central forge public key...");
            continue;
          }
          writer.println(currentLine);
          writer.flush();
        }
        writer.close();
        reader.close();

        final boolean successful = tempFile.renameTo(inputFile);
        if (!successful)
        {
          LOGGER.error("Could not rename the temporary authorized certificate public keys file");
        }

        // Set chmod 600 on the new authorized_keys file to make sure it complies with ssh rules
        LOGGER.info("Setting the rights on the new authorized_keys file...");
        final StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append(referenceToolService.getChmodCommand()).append(" ");
        commandBuilder.append(referenceToolService.getAuthorizedCertificatePublicKeysFileAccess())
            .append(" ");
        commandBuilder.append(referenceToolService.getAuthorizedCertificatePublicKeysFile());
        Runtime.getRuntime().exec(commandBuilder.toString());
        LOGGER.info(String.format("Run command %s", commandBuilder.toString()));
      }
      else
      {
        LOGGER.warn("No central public key found, nothing removed from the authorized public keys file.");
      }
    }
    catch (final IOException e)
    {
      throw new ForgeDistributionException(
          "Error when trying to remove the public key from the authorized keys file", e);
    }
  }

  private void login() throws ForgeDistributionException
  {
    try
    {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userPresenter.getUser(superAdministratorLogin);
      authentificationService.login(superAdministratorLogin, user.getPassword());
    }
    catch (final UserServiceException e)
    {
      throw new ForgeDistributionException("Unable to authenticate super administrator", e);
    }
  }

  /**
   * @param roles
   *
   * @throws ForgeDistributionException
   */
  private void updateSuperAdminMembership(final Set<String> roles) throws ForgeDistributionException
  {
    final Map<String, Object> criterias = new HashMap<String, Object>();
    criterias.put("realmType", RealmType.SYSTEM);
    List<User> users;
    try
    {
      // FIXME we must manage the user rigths differently:
      // 1- for subscription: get all the users with the Admin membership on the ref project and set to
      // them the "member" Membership
      // 2- for unsubscription: get all the users with Super Admin memebership on the forge project and set
      // to them the Admin membership to the ref project.
      // Do that when there wiil be a specific service to manage the forge Project and the "Super Admin"
      // role en the forge.

      users = userPresenter.searchUsersByCriterias(null, criterias);

      if ((users != null) && !users.isEmpty())
      {
        for (final User user : users)
        {
          LOGGER.info(String.format("Update membership, user=%s, projectid=%s, roles=%s", user.getLogin(),
                                    forgeConfigurationService.getReferentielProjectId(), roles));
          membershipPresenter.updateUserMembership(forgeConfigurationService.getReferentielProjectId(), user.getUuid(),
                                                   roles, null, false);
        }
      }
    }
    catch (final UserServiceException e)
    {
      final String msg = "Unable to retrieve the forge administrators!";
      LOGGER.error(msg, e);
      throw new ForgeDistributionException(ExceptionCode.TECHNICAL_ERROR, msg, e);
    }
    catch (final ProjectServiceException e)
    {
      final String msg = "Unable to update the forge administrators membership!";
      LOGGER.error(msg, e);
      throw new ForgeDistributionException(ExceptionCode.TECHNICAL_ERROR, msg, e);
    }
  }

  private void storeCentralForgePublicKey(final String centralPublicKey) throws ForgeDistributionException
  {
    try
    {
      LOGGER.info("Storing central forge public key into authorized_keys file...");
      if (centralPublicKey != null)
      {
        LOGGER.info(String.format("Storing central public key: %s", centralPublicKey));
        final FileWriter fileWriter = new FileWriter(referenceToolService.getAuthorizedCertificatePublicKeysFile(),
                                                     true);
        final PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(centralPublicKey);
        printWriter.close();
      }
      else
      {
        LOGGER.warn("No central public key found, nothing added to the authorized public keys file.");
      }
    }
    catch (final IOException e)
    {
      throw new ForgeDistributionException("Error when trying to write the public key into the authorized keys file",
                                           e);
    }
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
   * @param pMembershipPresenter
   *          the membershipPresenter to set
   */
  public void setMembershipPresenter(final MembershipPresenter pMembershipPresenter)
  {
    membershipPresenter = pMembershipPresenter;
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
   * @param pUserPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

  /**
   * @param pReferenceToolService
   *          the referenceToolService to set
   */
  public void setReferenceToolService(final ReferenceToolService pReferenceToolService)
  {
    referenceToolService = pReferenceToolService;
  }
}
