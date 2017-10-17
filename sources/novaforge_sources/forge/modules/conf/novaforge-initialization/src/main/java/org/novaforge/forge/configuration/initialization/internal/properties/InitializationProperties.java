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
package org.novaforge.forge.configuration.initialization.internal.properties;

/**
 * Bean used to read initialization configuration
 * 
 * @author Guillaume Lamirand
 */
public class InitializationProperties
{

  /*
   * Reference to InitializationPropertiesFile bean
   */
  private InitializationPropertiesFile initializationPropertiesFile;

  /**
   * Returns the Forge projectId
   * 
   * @return forge projectId
   */
  public String getForgeProjectId()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_PROJECTID.getKey());
  }

  /**
   * Returns the Forge Project Name
   * 
   * @return Forge Project Name
   */
  public String getForgeProjectName()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_PROJECT_NAME.getKey());
  }

  /**
   * Returns the Forge Project Description
   * 
   * @return Forge Project Description
   */
  public String getForgeProjectDescription()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_PROJECT_DESCRIPTION.getKey());
  }

  /**
   * Returns the Forge Project Licence
   * 
   * @return Forge Project Licence
   */
  public String getForgeProjectLicence()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_PROJECT_LICENCE.getKey());
  }

  public String getForgeMemberRoleName()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_MEMBER_ROLENAME.getKey());
  }

  public String getForgeSuperAdministratorRoleName()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_SUPERADMINISTRATOR_ROLENAME.getKey());
  }

  public String getForgeAdministratorRoleName()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_ADMINISTRATOR_ROLENAME.getKey());
  }

  /**
   * Returns the Super Administrator Login
   * 
   * @return Super Administrator Login
   */
  public String getSuperAdministratorLogin()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_SUPERADMINISTRATOR_LOGIN.getKey());
  }

  /**
   * Returns the Super Administrator password
   * 
   * @return Super Administrator password
   */
  public String getSuperAdministratorPassword()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_SUPERADMINISTRATOR_PASSWORD.getKey());
  }

  /**
   * Returns the Super Administrator Email
   * 
   * @return Super Administrator Email
   */
  public String getSuperAdministratorEmail()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_SUPERADMINISTRATOR_EMAIL.getKey());
  }

  /**
   * Returns the Super Administrator firstname
   * 
   * @return Super Administrator firstname
   */
  public String getSuperAdministratorFirstName()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_SUPERADMINISTRATOR_FIRSTNAME.getKey());
  }

  /**
   * Returns the Super Administrator name
   * 
   * @return Super Administrator name
   */
  public String getSuperAdministratorName()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_SUPERADMINISTRATOR_NAME.getKey());
  }

  /**
   * Returns the Super Administrator language
   * 
   * @return Super Administrator language
   */
  public String getSuperAdministratorLanguage()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.FORGE_SUPERADMINISTRATOR_LANGUAGE.getKey());
  }

  /**
   * Returns true if referentiel project is created
   * 
   * @return true if referentiel project is created, false otherwise
   */
  public boolean isReferentielCreated()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getBoolean(
        InitProperty.REFERENTIEL_CREATED.getKey());
  }

  /**
   * Returns the referentiel project id
   * 
   * @return referentiel project projectId
   */
  public String getReferentielProjectName()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.REFERENTIEL_PROJECT_NAME.getKey());
  }

  /**
   * Returns the referentiel project description
   * 
   * @return referentiel project projectId
   */
  public String getReferentielProjectDescription()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.REFERENTIEL_PROJECT_DESCRIPTION.getKey());
  }

  /**
   * Returns the referentiel project license
   * 
   * @return referentiel project license
   */
  public String getReferentielProjectLicence()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.REFERENTIEL_PROJECT_LICENCE.getKey());
  }

  /**
   * Returns the name of role member for referentiel project
   * 
   * @return name of role member for referentiel project
   */
  public String getReferentielMemberRoleName()
  {
    return initializationPropertiesFile.getPropertiesConfiguration().getString(
        InitProperty.REFERENTIEL_MEMBER_ROLENAME.getKey());
  }

  /**
   * Bind method used by the container to inject {@link InitializationPropertiesFile} service.
   * 
   * @param pInitializationPropertiesFile
   *          the InitializationPropertiesFile to set
   */
  public void setInitializationPropertiesFile(final InitializationPropertiesFile pInitializationPropertiesFile)
  {
    initializationPropertiesFile = pInitializationPropertiesFile;
  }
}
