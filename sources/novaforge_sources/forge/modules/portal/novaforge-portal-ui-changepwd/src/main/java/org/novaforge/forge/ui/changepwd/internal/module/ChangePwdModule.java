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
package org.novaforge.forge.ui.changepwd.internal.module;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.changepwd.internal.client.ChangePwdPresenter;
import org.novaforge.forge.ui.changepwd.internal.client.ChangePwdView;
import org.novaforge.forge.ui.changepwd.internal.client.ChangePwdViewImpl;

/**
 * @author Guillaume Lamirand
 */
public class ChangePwdModule implements PortalModule
{

  /**
   * Contains reference to {@link PortalMessages} implementation
   */
  private static PortalMessages            PORTAL_MESSAGES;
  /**
   * Contains reference to {@link UserPresenter} implementation
   */
  private static UserPresenter             USER_PRESENTER;
  /**
   * Contains reference to {@link AuthentificationService} implementation
   */
  private static AuthentificationService   AUTHENTIFICATION_SERVICE;
  /**
   * Contains reference to {@link ForgeConfigurationService} implementation
   */
  private static ForgeConfigurationService FORGECONFIGURATION_SERVICE;

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
   * @return the {@link ForgeCfgService}
   */
  public static ForgeConfigurationService getForgeConfigurationService()
  {
    return FORGECONFIGURATION_SERVICE;
  }

  /**
   * Use by container to inject {@link ForgeCfgService}
   *
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    FORGECONFIGURATION_SERVICE = pForgeConfigurationService;
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
    return PortalModuleId.CHANGE_PWD;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalComponent createComponent(final PortalContext pPortalContext)
  {
    final boolean sub = (pPortalContext.getAttributes() != null) && (pPortalContext.getAttributes()
                                                                                   .containsKey(PortalContext.KEY.IN_SUBWINDOW));
    final ChangePwdView      view      = new ChangePwdViewImpl(sub);
    final ChangePwdPresenter presenter = new ChangePwdPresenter(view, pPortalContext);
    presenter.init();
    return presenter;
  }

}
