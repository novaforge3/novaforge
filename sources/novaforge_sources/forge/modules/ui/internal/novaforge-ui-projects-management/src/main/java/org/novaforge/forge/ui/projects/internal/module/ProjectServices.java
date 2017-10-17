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
package org.novaforge.forge.ui.projects.internal.module;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.presenters.OrganizationPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.TemplatePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.portal.services.PortalMessages;

/**
 * @author Guillaume Lamirand
 */
public class ProjectServices
{

  private static ForgeConfigurationService FORGE_CONFIGURATION_SERVICE;
  private static ProjectPresenter          PROJECT_PRESENTER;
  private static OrganizationPresenter     ORGANIZATION_PRESENTER;
  private static TemplatePresenter         TEMPLATE_PRESENTER;
  private static PortalMessages            PORTAL_MESSAGES;
  private static UserPresenter             USER_PRESENTER;

  /**
   * @return the {@link ForgeCfgService}
   */
  public static ForgeConfigurationService getForgeConfigurationService()
  {
    return FORGE_CONFIGURATION_SERVICE;
  }

  /**
   * Use by container to inject {@link ForgeCfgService}
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    FORGE_CONFIGURATION_SERVICE = pForgeConfigurationService;
  }

  /**
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
   * @return the {@link OrganizationPresenter}
   */
  public static OrganizationPresenter getOrganizationPresenter()
  {
    return ORGANIZATION_PRESENTER;
  }

  /**
   * Use by container to inject {@link OrganizationPresenter}
   * 
   * @param pOrganizationPresenter
   *          the organizationPresenter to set
   */
  public void setOrganizationPresenter(final OrganizationPresenter pOrganizationPresenter)
  {
    ORGANIZATION_PRESENTER = pOrganizationPresenter;
  }

  /**
   * @return the {@link TemplatePresenter}
   */
  public static TemplatePresenter getTemplatePresenter()
  {
    return TEMPLATE_PRESENTER;
  }

  /**
   * Use by container to inject {@link TemplatePresenter}
   * 
   * @param pTemplatePresenter
   *          the templatePresenter to set
   */
  public void setTemplatePresenter(final TemplatePresenter pTemplatePresenter)
  {
    TEMPLATE_PRESENTER = pTemplatePresenter;
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
}
