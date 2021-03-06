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
package org.novaforge.forge.ui.memberships.internal.module;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.GroupPresenter;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.MembershipRequestPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;

/**
 * @author Guillaume Lamirand
 */
public class MembershipsModule implements PortalModule
{

  private static PortalMessages             PORTAL_MESSAGES;
  private static AuthentificationService    AUTHENTIFICATION_SERVICE;
  private static MembershipPresenter        MEMBERSHIP_PRESENTER;
  private static MembershipRequestPresenter MEMBERSHIP_REQUEST_PRESENTER;
  private static UserPresenter              USER_PRESENTER;
  private static GroupPresenter             GROUP_PRESENTER;
  private static ProjectRolePresenter       PROJECT_ROLE_PRESENTER;
  private static PluginsManager             PLUGINS_MANAGER;
  private static ProjectPresenter           PROJECT_PRESENTER;
  private static SpacePresenter             SPACE_PRESENTER;
  private static ApplicationPresenter       APPLICATION_PRESENTER;
  private static ForgeConfigurationService  FORGE_CONFIGURATION_SERVICE;

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
   * @return the {@link MembershipRequestPresenter}
   */
  public static MembershipRequestPresenter getMembershipRequestPresenter()
  {
    return MEMBERSHIP_REQUEST_PRESENTER;
  }

  /**
   * Use by container to inject {@link MembershipRequestPresenter}
   *
   * @param pMembershipRequestPresenter
   *          the membershipRequestPresenter to set
   */
  public void setMembershipRequestPresenter(final MembershipRequestPresenter pMembershipRequestPresenter)
  {
    MEMBERSHIP_REQUEST_PRESENTER = pMembershipRequestPresenter;
  }

  /**
   * @return the {@link PluginsManager}
   */
  public static PluginsManager getPluginsManager()
  {
    return PLUGINS_MANAGER;
  }

  /**
   * Use by container to inject {@link PluginsManager}
   *
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    PLUGINS_MANAGER = pPluginsManager;
  }

  /**
   * @return the {@link ProjectRolePresenter}
   */
  public static ProjectRolePresenter getProjectRolePresenter()
  {
    return PROJECT_ROLE_PRESENTER;
  }

  /**
   * Use by container to inject {@link ProjectRolePresenter}
   *
   * @param pProjectRolePresenter
   *          the projectRolePresenter to set
   */
  public void setProjectRolePresenter(final ProjectRolePresenter pProjectRolePresenter)
  {
    PROJECT_ROLE_PRESENTER = pProjectRolePresenter;
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
   * @return the {@link GroupPresenter}
   */
  public static GroupPresenter getGroupPresenter()
  {
    return GROUP_PRESENTER;
  }

  /**
   * Use by container to inject {@link GroupPresenter}
   *
   * @param pGroupPresenter
   *          the groupPresenter to set
   */
  public void setGroupPresenter(final GroupPresenter pGroupPresenter)
  {
    GROUP_PRESENTER = pGroupPresenter;
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
   * @return the {@link SpacePresenter}
   */
  public static SpacePresenter getSpacePresenter()
  {
    return SPACE_PRESENTER;
  }

  /**
   * Use by container to inject {@link SpacePresenter}
   *
   * @param pSpacePresenter
   *          the spacePresenter to set
   */
  public void setSpacePresenter(final SpacePresenter pSpacePresenter)
  {
    SPACE_PRESENTER = pSpacePresenter;
  }

  /**
   * @return the {@link ApplicationPresenter}
   */
  public static ApplicationPresenter getApplicationPresenter()
  {
    return APPLICATION_PRESENTER;
  }

  /**
   * Use by container to inject {@link ApplicationPresenter}
   *
   * @param pApplicationPresenter
   *          the applicationPresenter to set
   */
  public void setApplicationPresenter(final ApplicationPresenter pApplicationPresenter)
  {
    APPLICATION_PRESENTER = pApplicationPresenter;
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
   * {@inheritDoc}
   */
  @Override
  public String getId()
  {
    return getPortalModuleId().getId();
  }

  /**
   * Return {@link PortalModuleId} defined for this module
   *
   * @return {@link PortalModuleId}
   */
  public static PortalModuleId getPortalModuleId()
  {
    return PortalModuleId.MEMBERSHIPS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalComponent createComponent(final PortalContext pPortalContext)
  {
    return new MainComponent(pPortalContext);
  }

}
