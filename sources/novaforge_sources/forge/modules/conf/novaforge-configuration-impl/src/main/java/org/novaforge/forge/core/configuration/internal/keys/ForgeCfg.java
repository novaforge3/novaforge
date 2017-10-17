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
package org.novaforge.forge.core.configuration.internal.keys;

/**
 * This enum lists all property key used in database
 * 
 * @author Guillaume Lamirand
 */
public enum ForgeCfg
{
  INITIALIZATION
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "initialization";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue()
    {
      return "false";
    }
  },
  FORGE_PROJECT_ID
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "forgeProjectId";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue()
    {
      return "forge";
    }
  },
  FORGE_SUPERADMINISTRATOR_LOGIN
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "forgeSuperAdministratorLogin";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue()
    {
      return "admin1";
    }
  },
  FORGE_SUPERADMINISTRATOR_PASSWORD
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "forgeSuperAdministratorPassword";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue()
    {
      return "c1dc19afe59631de1e5f5408d083cc55e9dcdf65";
    }
  },
  FORGE_SUPERADMINISTRATOR_ROLE_NAME
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "forgeSuperAdministratorRoleName";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue()
    {
      return "Super Administrator";
    }
  },
  FORGE_ADMINISTRATOR_ROLE_NAME
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "forgeAdministratorRoleName";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue()
    {
      return "Administrator";
    }
  },
  FORGE_MEMBER_ROLE_NAME
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "forgeMemberRoleName";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue()
    {
      return "Member";
    }
  },
  REFERENTIEL_CREATED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "referentielCreated";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue()
    {
      return "false";
    }
  },
  REFERENTIEL_PROJECT_ID
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "referentielProjectId";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue()
    {
      return "reference";
    }
  },
  REFERENTIEL_MEMBER_ROLE_NAME
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
      return "referentielMemberRoleName";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue()
    {
      return "Member";
    }
  };
  /**
   * Returns the property key used in configuration file
   * 
   * @return the property key used in configuration file
   */
  public abstract String getKey();

  /**
   * Returns the default value
   * 
   * @return the default value
   */
  public abstract String getDefaultValue();
}
