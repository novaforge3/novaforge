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
package org.novaforge.forge.core.plugins.categories.requirementsmanagement;

import java.util.ArrayList;
import java.util.List;

/**
 * This enum defines the list of requirement management send event which can come from a requirement plugin.
 * 
 * @author lamirang
 */
public enum RequirementsSend
{
  /**
   * Define a method which send new requirements
   */
  updateRequirements;

  private static final List<String> sends = new ArrayList<String>();

  static
  {
    for (final RequirementsSend notif : values())
    {
      sends.add(notif.toString());
    }
  }

  /**
   * @return list of string represented by enumeration names
   */
  public static List<String> toList()
  {
    return sends;
  }
}
