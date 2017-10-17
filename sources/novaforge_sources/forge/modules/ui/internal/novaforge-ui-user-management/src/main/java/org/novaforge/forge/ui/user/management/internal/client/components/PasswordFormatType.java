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
package org.novaforge.forge.ui.user.management.internal.client.components;

/**
 * @author caseryj
 */
public enum PasswordFormatType
{

  /**
   * Define the NovaForge password format
   */
  NOVAFORGE
  {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
      return "novaforge";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue()
    {
      return "((?=.*\\d)(?=.*[a-zA-Z])(?=.*[!\\\\\\\"#$%&'<>\\()*=^+\\]\\[,\\./:;?@_`|{}~-])(?=\\S+$).{8,20})";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getI18NName()
    {
      return "usermanagement.pwd.novaforge";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getI18NDescription()
    {
      return "usermanagement.pwd.novaforge.description";
    }

  },
  /**
   * Define a custom password format
   */
  CUSTOM
  {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
      return "custom";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue()
    {
      return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getI18NName()
    {
      return "usermanagement.pwd.custom";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getI18NDescription()
    {
      return "usermanagement.pwd.custom.description";
    }

  };
  /**
   * Get the Password Format name
   * 
   * @return the password format name
   */
  public abstract String getName();

  /**
   * Get the Password Format I18N name
   * 
   * @return the password format I18N name
   */
  public abstract String getI18NName();

  /**
   * Get the Password Format value
   * 
   * @return the password format value
   */
  public abstract String getValue();

  /**
   * Get the Password Format I18N description
   * 
   * @return the password format I18N description
   */
  public abstract String getI18NDescription();

}
