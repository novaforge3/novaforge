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
package org.novaforge.forge.ui.user.management.internal.module;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.organization.services.LanguageService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;

/**
 * @author caseryj
 */
public class PublicModule implements PortalModule
{

  /**
   * The portal messages services
   */
  private static PortalMessages            PORTAL_MESSAGES;
  /**
   * The user presenter
   */
  private static UserPresenter             USER_PRESENTER;
  /**
   * The membership presenter
   */
  private static MembershipPresenter       MEMBERSHIP_PRESENTER;
  /**
   * The language service
   */
  private static LanguageService           LANGUAGE_PRESENTER;
  /**
   * The forge configuration service
   */
  private static ForgeConfigurationService FORGE_CONFIGURATION_SERVICE;
  /**
   * The authentification service
   */
  private static AuthentificationService   AUTHENTIFICATION_PRESENTER;
  /**
   * The project presenter
   */
  private static ProjectPresenter          PROJECT_PRESENTER;

  /**
   * Get the membership presenter
   *
   * @return the {@link MembershipPresenter}
   */
  public static MembershipPresenter getMembershipPresenter()
  {
    return MEMBERSHIP_PRESENTER;
  }

  /**
   * Use by container to inject {@link MembershipPresenter}
   *
   * @param pMembershipPresenter
   *          the membershipPresenter to set
   */
  public void setMembershipPresenter(final MembershipPresenter pMembershipPresenter)
  {
    MEMBERSHIP_PRESENTER = pMembershipPresenter;
  }

  /**
   * Get the authentification service
   *
   * @return the {@link AuthentificationService}
   */
  public static AuthentificationService getAuthentificationService()
  {
    return AUTHENTIFICATION_PRESENTER;
  }

  /**
   * Use by container to inject {@link AuthentificationService}
   *
   * @param pAuthentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    AUTHENTIFICATION_PRESENTER = pAuthentificationService;
  }

  /**
   * Get the portal messages service
   *
   * @return the {@link PortalMessages}
   */
  public static PortalMessages getPortalMessages()
  {
    return PORTAL_MESSAGES;
  }

  /**
   * Use by container to inject {@link PortalMessages}
   *
   * @param pPortalMessages
   *          the portalMessages to set
   */
  public void setPortalMessages(final PortalMessages pPortalMessages)
  {
    PORTAL_MESSAGES = pPortalMessages;
  }

  /**
   * Get the user presenter
   *
   * @return the {@link UserPresenter}
   */
  public static UserPresenter getUserPresenter()
  {
    return USER_PRESENTER;
  }

  /**
   * Use by container to inject {@link UserPresenter}
   *
   * @param pUserPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    USER_PRESENTER = pUserPresenter;
  }

  /**
   * Get the language service
   *
   * @return the {@link LanguageService}
   */
  public static LanguageService getLanguagePresenter()
  {
    return LANGUAGE_PRESENTER;
  }

  /**
   * Use by container to inject {@link LanguageService}
   *
   * @param pLanguagePresenter
   *          the languageService to set
   */
  public void setLanguagePresenter(final LanguageService pLanguagePresenter)
  {
    LANGUAGE_PRESENTER = pLanguagePresenter;
  }

  /**
   * Get the project presenter
   *
   * @return the {@link ProjectPresenter}
   */
  public static ProjectPresenter getProjectPresenter()
  {
    return PROJECT_PRESENTER;
  }

  /**
   * Use by container to inject {@link ProjectPresenter}
   *
   * @param pProjectPresenter
   *          the projectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    PROJECT_PRESENTER = pProjectPresenter;
  }

  /**
   * Get the forge configuration service
   *
   * @return the {@link ForgeConfigurationService}
   */
  public static ForgeConfigurationService getForgeConfigurationService()
  {
    return FORGE_CONFIGURATION_SERVICE;
  }

  /**
   * Use by container to inject {@link ForgeConfigurationService}
   *
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    FORGE_CONFIGURATION_SERVICE = pForgeConfigurationService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId()
  {
    return getPortalModuleId().getId();
  }

  /**
   * Return the {@link PortalModuleId} associated to the current module
   *
   * @return {@link PortalModuleId}
   */
  public static PortalModuleId getPortalModuleId()
  {
    return PortalModuleId.USERMANAGEMENT_PUBLIC;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalComponent createComponent(final PortalContext pPortalContext)
  {
    return new PublicComponent(pPortalContext);
  }

}
