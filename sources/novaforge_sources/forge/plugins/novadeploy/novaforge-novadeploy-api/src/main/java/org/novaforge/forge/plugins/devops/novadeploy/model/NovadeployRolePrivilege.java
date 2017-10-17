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
package org.novaforge.forge.plugins.devops.novadeploy.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This enumeration declares default roles which are used on Novadeploy
 * 
 * @author dekimpea
 */
public enum NovadeployRolePrivilege
{

  /**
   * Represents manager role
   */

  PROJECT_MANAGER
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return NovadeployEnumConstant.PROJECT_MANAGER_LABEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId()
    {
      return 1;
    }

  },
  /**
   * Represents architect role. It can do actions with environment descriptor mostly @see api doc.
   */
  ARCHITECT
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return NovadeployEnumConstant.ARCHITECT_LABEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId()
    {
      return 8;
    }

  },
  /**
   *  Represents tester role. It can do enviroment deployement mainly @see api doc for details.
   */
  TESTER
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return NovadeployEnumConstant.TESTER_LABEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId()
    {
      return 4;
    }
  };

  /**
   * Contains map which link a label to a specific role element
   */
  private static final Map<String, NovadeployRolePrivilege> roles = new HashMap<String, NovadeployRolePrivilege>();

  static
  {
    for (final NovadeployRolePrivilege role : values())
    {
      roles.put(role.getLabel(), role);
    }
  }

  /**
   * Return element from enumeration regarding a specific label.
   *
   * @param pLabel
   *          represents the label used to search a element in the enumeration
   * @return specific element
   */
  public static NovadeployRolePrivilege fromLabel(final String pLabel)
  {
    return roles.get(pLabel);
  }

  /**
   * Return the label of a role
   *
   * @return label of a role
   */
  public abstract String getLabel();

  /**
   * Return the id of a privilege
   *
   * @return id of a role
   */
  public abstract int getId();
}
