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
package org.novaforge.forge.ui.requirements.internal.module;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.plugins.services.ApplicationRequestService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementConfigurationService;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementManagerFunctionalService;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.tools.requirements.common.connectors.ExternalRepositoryRequirementConnector;
import org.novaforge.forge.tools.requirements.common.facades.RequirementCodeService;
import org.novaforge.forge.tools.requirements.common.facades.RequirementFunctionalTestService;
import org.novaforge.forge.tools.requirements.common.factories.RequirementFactory;
import org.novaforge.forge.tools.requirements.common.services.RequirementManagerSchedulingService;
import org.novaforge.forge.tools.requirements.common.services.RequirementManagerService;
import org.novaforge.forge.tools.requirements.common.services.RequirementPermissionService;
import org.novaforge.forge.tools.requirements.common.services.RequirementRepositoryService;

import java.util.List;

/**
 * @author Jeremy Casery
 */
public class RequirementsModule implements PortalModule
{

  private static ForgeConfigurationService                    FORGE_CONFIGURATION_SERVICE;
  private static PortalMessages                               PORTAL_MESSAGES;
  private static RequirementManagerService                    REQUIREMENT_MANAGER_SERVICE;
  private static RequirementRepositoryService                 REQUIREMENT_REPOSITORY_SERVICE;
  private static RequirementCodeService                       REQUIREMENT_CODE_SERVICE;
  private static RequirementFunctionalTestService             REQUIREMENT_FUNCTIONAL_TEST_SERVICE;
  private static RequirementManagerFunctionalService          REQUIREMENT_MANAGER_FUNCTIONAL_SERVICE;
  private static RequirementManagerSchedulingService          REQUIREMENT_MANAGER_SCHEDULING_SERVICE;
  private static ApplicationRequestService                    APPLICATION_REQUEST_SERVICE;
  private static RequirementFactory                           REQUIREMENT_FACTORY_SERVICE;
  private static RequirementConfigurationService              REQUIREMENT_CONFIGURATION_SERVICE;
  private static List<ExternalRepositoryRequirementConnector> EXTERNAL_REPOSITORY_REQUIREMENT_CONNECTORS;
  private static RequirementPermissionService                 REQUIREMENT_PERMISSION_SERVICE;

  /** The authentification service */
  private static AuthentificationService                      AUTHENTIFICATION_PRESENTER;

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
   * @return the {@link RequirementManagerService}
   */
  public static RequirementManagerService getRequirementManagerService()
  {
    return REQUIREMENT_MANAGER_SERVICE;
  }

  /**
   * Use by container to inject {@link RequirementManagerService}
   *
   * @param pRequirementManagerService
   *          the requirementManagerService to set
   */
  public void setRequirementManagerService(final RequirementManagerService pRequirementManagerService)
  {
    REQUIREMENT_MANAGER_SERVICE = pRequirementManagerService;
  }

  /**
   * @return the {@link RequirementRepositoryService}
   */
  public static RequirementRepositoryService getRequirementRepositoryService()
  {
    return REQUIREMENT_REPOSITORY_SERVICE;
  }

  /**
   * Use by container to inject {@link RequirementRepositoryService}
   *
   * @param pRequirementRepositoryService
   *          the RequirementRepositoryService to set
   */
  public void setRequirementRepositoryService(final RequirementRepositoryService pRequirementRepositoryService)
  {
    REQUIREMENT_REPOSITORY_SERVICE = pRequirementRepositoryService;
  }

  /**
   * @return the {@link RequirementManagerFunctionalService}
   */
  public static RequirementManagerFunctionalService getRequirementManagerFunctionalService()
  {
    return REQUIREMENT_MANAGER_FUNCTIONAL_SERVICE;
  }

  /**
   * Use by container to inject {@link RequirementManagerFunctionalService}
   *
   * @param pRequirementManagerFunctionalService
   *          the requirementManagerFunctionalService to set
   */
  public void setRequirementManagerFunctionalService(
      final RequirementManagerFunctionalService pRequirementManagerFunctionalService)
  {
    REQUIREMENT_MANAGER_FUNCTIONAL_SERVICE = pRequirementManagerFunctionalService;
  }

  /**
   * @return the {@link RequirementManagerSchedulingService}
   */
  public static RequirementManagerSchedulingService getRequirementManagerSchedulingService()
  {
    return REQUIREMENT_MANAGER_SCHEDULING_SERVICE;
  }

  /**
   * Use by container to inject {@link RequirementManagerSchedulingService}
   *
   * @param pRequirementManagerSchedulingService
   *          the requirementManagerSchedulingService to set
   */
  public void setRequirementManagerSchedulingService(
      final RequirementManagerSchedulingService pRequirementManagerSchedulingService)
  {
    REQUIREMENT_MANAGER_SCHEDULING_SERVICE = pRequirementManagerSchedulingService;
  }

  /**
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
   * @return the {@link ApplicationRequestService}
   */
  public static ApplicationRequestService getApplicationRequestService()
  {
    return APPLICATION_REQUEST_SERVICE;
  }

  /**
   * Use by container to inject {@link ApplicationRequestService}
   *
   * @param pApplicationRequestService
   *          the applicationRequestService to set
   */
  public void setApplicationRequestService(final ApplicationRequestService pApplicationRequestService)
  {
    APPLICATION_REQUEST_SERVICE = pApplicationRequestService;
  }

  /**
   * @return the {@link RequirementFactory}
   */
  public static RequirementFactory getRequirementFactory()
  {
    return REQUIREMENT_FACTORY_SERVICE;
  }

  /**
   * Use by container to inject {@link RequirementFactory}
   *
   * @param pRequirementFactory
   *          the requirementFactory to set
   */
  public void setRequirementFactory(final RequirementFactory pRequirementFactory)
  {
    REQUIREMENT_FACTORY_SERVICE = pRequirementFactory;
  }

  public static RequirementCodeService getRequirementCodeService()
  {
    return REQUIREMENT_CODE_SERVICE;
  }

  public void setRequirementCodeService(final RequirementCodeService pRequirement_code_service)
  {
    REQUIREMENT_CODE_SERVICE = pRequirement_code_service;
  }

  /**
   * @return the {@link RequirementFunctionalTestService}
   */
  public static RequirementFunctionalTestService getRequirementFunctionalTestService()
  {
    return REQUIREMENT_FUNCTIONAL_TEST_SERVICE;
  }

  /**
   * Use by container to inject {@link RequirementFunctionalTestService}
   *
   * @param pRequirementFunctionalTestService
   *          the requirementFunctionalTestService to set
   */
  public void setRequirementFunctionalTestService(
      final RequirementFunctionalTestService pRequirementFunctionalTestService)
  {
    REQUIREMENT_FUNCTIONAL_TEST_SERVICE = pRequirementFunctionalTestService;
  }

  public static RequirementConfigurationService getRequirementConfigurationService()
  {
    return REQUIREMENT_CONFIGURATION_SERVICE;
  }

  public void setRequirementConfigurationService(
      final RequirementConfigurationService pRequirement_configuration_service)
  {
    REQUIREMENT_CONFIGURATION_SERVICE = pRequirement_configuration_service;
  }

  public static List<ExternalRepositoryRequirementConnector> getExternalRepositoryRequirementConnectors()
  {
    return EXTERNAL_REPOSITORY_REQUIREMENT_CONNECTORS;
  }

  public void setExternalRepositoryRequirementConnectors(
      final List<ExternalRepositoryRequirementConnector> pExternal_repository_requirement_connector)
  {
    EXTERNAL_REPOSITORY_REQUIREMENT_CONNECTORS = pExternal_repository_requirement_connector;
  }

  public static RequirementPermissionService getRequirementPermissionService()
  {
    return REQUIREMENT_PERMISSION_SERVICE;
  }

  public void setRequirementPermissionService(final RequirementPermissionService pRequirementPermissionService)
  {
    REQUIREMENT_PERMISSION_SERVICE = pRequirementPermissionService;
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
    return PortalModuleId.REQUIREMENTS_MANAGEMENT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MainComponent createComponent(final PortalContext pPortalContext)
  {
    return new MainComponent(pPortalContext);
  }

}
