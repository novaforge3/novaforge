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
 * This enum lists all property key used in initialization properties file
 * 
 * @author Guillaume Lamirand
 */
public enum InitProperty
{
  FORGE_PROJECTID
  {
    @Override
    public String getKey()
    {
      return "forgeProjectId";
    }
  },
  FORGE_PROJECT_NAME
  {
    @Override
    public String getKey()
    {
      return "forgeProjectName";
    }
  },
  FORGE_PROJECT_DESCRIPTION
  {
    @Override
    public String getKey()
    {
      return "forgeProjectDescription";
    }
  },
  FORGE_PROJECT_LICENCE
  {
    @Override
    public String getKey()
    {
      return "forgeProjectLicence";
    }
  },
  FORGE_MEMBER_ROLENAME
  {
    @Override
    public String getKey()
    {
      return "forgeMemberRoleName";
    }
  },
  FORGE_SUPERADMINISTRATOR_ROLENAME
  {
    @Override
    public String getKey()
    {
      return "forgeSuperAdministratorRoleName";
    }
  },
  FORGE_ADMINISTRATOR_ROLENAME
  {
    @Override
    public String getKey()
    {
      return "forgeAdministratorRoleName";
    }
  },
  FORGE_SUPERADMINISTRATOR_LOGIN
  {
    @Override
    public String getKey()
    {
      return "forgeSuperAdministratorLogin";
    }
  },
  FORGE_SUPERADMINISTRATOR_PASSWORD
  {
    @Override
    public String getKey()
    {
      return "forgeSuperAdministratorPassword";
    }
  },
  FORGE_SUPERADMINISTRATOR_EMAIL
  {
    @Override
    public String getKey()
    {
      return "forgeSuperAdministratorEmail";
    }
  },
  FORGE_SUPERADMINISTRATOR_FIRSTNAME
  {
    @Override
    public String getKey()
    {
      return "forgeSuperAdministratorFirstName";
    }
  },
  FORGE_SUPERADMINISTRATOR_NAME
  {
    @Override
    public String getKey()
    {
      return "forgeSuperAdministratorName";
    }
  },
  FORGE_SUPERADMINISTRATOR_LANGUAGE
  {
    @Override
    public String getKey()
    {
      return "forgeSuperAdministratorLanguage";
    }
  },
  REFERENTIEL_CREATED
  {
    @Override
    public String getKey()
    {
      return "referentielCreated";
    }
  },
  REFERENTIEL_PROJECT_NAME
  {
    @Override
    public String getKey()
    {
      return "referentielProjectName";
    }
  },
  REFERENTIEL_PROJECT_DESCRIPTION
  {
    @Override
    public String getKey()
    {
      return "referentielProjectDescription";
    }
  },
  REFERENTIEL_PROJECT_LICENCE
  {
    @Override
    public String getKey()
    {
      return "referentielProjectLicence";
    }
  },
  REFERENTIEL_MEMBER_ROLENAME
  {
    @Override
    public String getKey()
    {
      return "referentielMemberRoleName";
    }
  };
  /**
   * Returns the property key used in configuration file
   * 
   * @return the property key used in configuration file
   */
  public abstract String getKey();
}
