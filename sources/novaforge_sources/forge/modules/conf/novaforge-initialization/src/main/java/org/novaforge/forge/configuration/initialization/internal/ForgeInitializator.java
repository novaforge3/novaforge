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
package org.novaforge.forge.configuration.initialization.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.configuration.initialization.exceptions.ForgeInitializationException;
import org.novaforge.forge.configuration.initialization.internal.creator.LanguageCreator;
import org.novaforge.forge.configuration.initialization.internal.creator.ProjectCreator;
import org.novaforge.forge.configuration.initialization.internal.creator.ReferentielCreator;
import org.novaforge.forge.configuration.initialization.internal.creator.UserCreator;
import org.novaforge.forge.configuration.initialization.internal.properties.InitializationProperties;
import org.novaforge.forge.configuration.initialization.internal.properties.InitializationPropertiesFile;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.initialization.services.ForgeInitializationService;

/**
 * @author lamirang
 */
public class ForgeInitializator
{
  private static final Log             LOGGER = LogFactory.getLog(ForgeInitializator.class);
  private InitializationPropertiesFile initializationPropertiesFile;
  private InitializationProperties     initializationProperties;
  private ForgeConfigurationService    forgeConfigurationService;
  private ForgeInitializationService   forgeInitializationService;
  private HistorizationService         historizationService;

  private LanguageCreator              languageCreator;
  private ProjectCreator               projectCreator;
  private UserCreator                  userCreator;
  private ReferentielCreator           referentielCreator;

  public void init()
  {
    historizationService.setActivatedMode(false);
    try
    {
      if ((!initializationPropertiesFile.isLocked()) && (!forgeInitializationService.isInitialized()))
      {
        LOGGER.info("Novaforge Initialization: START.");
        storeConfiguration();
        handleInitialization();
        initializationPropertiesFile.lockProperties();
        LOGGER.info("Novaforge Initialization: FINISHED SUCCESSFULLY.");

      }
      else if ((!initializationPropertiesFile.isLocked())
          && (forgeInitializationService.isInitialized()))
      {
        initializationPropertiesFile.lockProperties();
        LOGGER.info("Novaforge already initialized: READY TO BE USED.");
      }
      else
      {
        LOGGER.info("Novaforge already initialized: READY TO BE USED.");
      }
      forgeInitializationService.setInitializationSuccessfull(true);
    }
    catch (final ForgeInitializationException e)
    {
      LOGGER.error("Unable to initialize forge datas", e);
    }
    finally
    {
      historizationService.setActivatedMode(true);
    }
  }

  private void storeConfiguration()
  {
    final String forgeProjectId = initializationProperties.getForgeProjectId();
    forgeConfigurationService.setForgeProjectId(forgeProjectId);
    final String superAdminLogin = initializationProperties.getSuperAdministratorLogin();
    forgeConfigurationService.setSuperAdministratorLogin(superAdminLogin);
    final String forgeAdministratorRoleName = initializationProperties.getForgeAdministratorRoleName();
    forgeConfigurationService.setForgeAdministratorRoleName(forgeAdministratorRoleName);
    final String forgeSuperAdministratorRoleName = initializationProperties
        .getForgeSuperAdministratorRoleName();
    forgeConfigurationService.setForgeSuperAdministratorRoleName(forgeSuperAdministratorRoleName);
    final String forgeMemberRoleName = initializationProperties.getForgeMemberRoleName();
    forgeConfigurationService.setForgeMemberRoleName(forgeMemberRoleName);
    forgeConfigurationService.setReferentielProjectId(ReferentielCreator.REFERENCE_PROJECT_ID);
    final String referentielMemberRoleName = initializationProperties.getReferentielMemberRoleName();
    forgeConfigurationService.setReferentielMemberRoleName(referentielMemberRoleName);
    final boolean referentielCreated = initializationProperties.isReferentielCreated();
    forgeConfigurationService.setReferentielCreated(referentielCreated);

  }

  private void handleInitialization() throws ForgeInitializationException
  {
    languageCreator.createLanguages();
    userCreator.createSuperAdmin();
    projectCreator.createForgeProject();
    referentielCreator.createReferentiel();
  }

  public void setInitializationProperties(final InitializationProperties pInitializationProperties)
  {
    initializationProperties = pInitializationProperties;
  }

  public void setInitializationPropertiesFile(final InitializationPropertiesFile pInitializationPropertiesFile)
  {
    initializationPropertiesFile = pInitializationPropertiesFile;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  public void setForgeInitializationService(final ForgeInitializationService pForgeInitializationService)
  {
    forgeInitializationService = pForgeInitializationService;
  }

  public void setLanguageCreator(final LanguageCreator pLanguageCreator)
  {
    languageCreator = pLanguageCreator;
  }

  public void setProjectCreator(final ProjectCreator pProjectCreator)
  {
    projectCreator = pProjectCreator;
  }

  public void setUserCreator(final UserCreator pUserCreator)
  {
    userCreator = pUserCreator;
  }

  public void setReferentielCreator(final ReferentielCreator pReferentielCreator)
  {
    referentielCreator = pReferentielCreator;
  }

  public void setHistorizationService(final HistorizationService pHistorizationService)
  {
    historizationService = pHistorizationService;
  }

}
