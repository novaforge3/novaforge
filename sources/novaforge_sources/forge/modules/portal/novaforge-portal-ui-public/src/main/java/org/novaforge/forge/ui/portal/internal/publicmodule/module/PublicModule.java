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
package org.novaforge.forge.ui.portal.internal.publicmodule.module;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.presenters.LanguagePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.portal.services.PortalModuleService;
import org.novaforge.forge.portal.services.PortalService;
import org.novaforge.forge.ui.portal.internal.publicmodule.client.HomePresenter;
import org.novaforge.forge.ui.portal.internal.publicmodule.client.HomeView;
import org.novaforge.forge.ui.portal.internal.publicmodule.client.HomeViewImpl;

/**
 * @author Guillaume Lamirand
 */
public class PublicModule implements PortalModule
{

  /**
   * Contains reference to {@link PortalService} implementation
   */
  private static PortalService             PORTAL_SERVICE;
  /**
   * Contains reference to {@link PortalMessages} implementation
   */
  private static PortalMessages            PORTAL_MESSAGES;
  /**
   * Contains reference to {@link ForgeConfigurationService} implementation
   */
  private static ForgeConfigurationService FORGE_CONFIGURATION_SERVICE;
  /**
   * Contains reference to {@link PortalModuleService} implementation
   */
  private static PortalModuleService       PORTAL_MODULE_SERVICE;
  /**
   * Contains reference to {@link AuthentificationService} implementation
   */
  private static AuthentificationService   AUTHENTIFICATION_SERVICE;
  /**
   * Contains reference to {@link UserPresenter} implementation
   */
  private static UserPresenter             USER_PRESENTER;
  /**
   * Contains reference to {@link LanguagePresenter} implementation
   */
  private static LanguagePresenter         LANGUAGE_PRESENTER;

  /**
   * @return the {@link PortalService}
   */
  public static PortalService getPortalService()
  {
    return PORTAL_SERVICE;
  }

  /**
   * Use by container to inject {@link PortalService}
   *
   * @param pPortalService
   *          the portalService to set
   */
  public void setPortalService(final PortalService pPortalService)
  {
    PORTAL_SERVICE = pPortalService;
  }

  /**
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
   * @return the {@link PortalModuleService}
   */
  public static PortalModuleService getPortalModuleService()
  {
    return PORTAL_MODULE_SERVICE;
  }

  /**
   * Use by container to inject {@link PortalModuleService}
   *
   * @param pPortalModuleService
   *          the portalModuleService to set
   */
  public void setPortalModuleService(final PortalModuleService pPortalModuleService)
  {
    PORTAL_MODULE_SERVICE = pPortalModuleService;
  }

  /**
   * @return the {@link AuthentificationService}
   */
  public static AuthentificationService getAuthentificationService()
  {
    return AUTHENTIFICATION_SERVICE;
  }

  /**
   * Use by container to inject {@link AuthentificationService}
   *
   * @param pAuthentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    AUTHENTIFICATION_SERVICE = pAuthentificationService;
  }

  /**
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
   * @return the {@link LanguagePresenter}
   */
  public static LanguagePresenter getLanguagePresenter()
  {
    return LANGUAGE_PRESENTER;
  }

  /**
   * Use by container to inject {@link LanguagePresenter}
   *
   * @param pLanguagePresenter
   *          the languagePresenter to set
   */
  public void setLanguagePresenter(final LanguagePresenter pLanguagePresenter)
  {
    LANGUAGE_PRESENTER = pLanguagePresenter;
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
    return PortalModuleId.PUBLIC;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalComponent createComponent(final PortalContext pPortalContext)
  {
    final HomeView      view      = new HomeViewImpl();
    return new HomePresenter(view, pPortalContext);
  }

}
