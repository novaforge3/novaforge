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
package org.novaforge.forge.core.plugins.domain.plugin;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This object is used as DTO between forge and plugin
 * 
 * @author sbenoist
 */
public interface PluginRoleInformation extends Serializable
{
  /**
   * Return the {@link PluginMembership} which has to be removed
   * 
   * @return the memberships to remove
   */
  List<PluginMembership> getRemoveMemberships();

  /**
   * Set the {@link PluginMembership} which has to be removed
   * 
   * @param pMemberships
   *          the memberships w to set
   */
  void setRemoveMemberships(List<PluginMembership> pMemberships);

  /**
   * Return the {@link PluginMembership} which has to be updated
   * 
   * @return the memberships
   */
  List<PluginMembership> getUpdateMemberships();

  /**
   * Set the {@link PluginMembership} which has to be updated
   * 
   * @param pMemberships
   *          the memberships to set
   */
  void setUpdateMemberships(List<PluginMembership> pMemberships);

  /**
   * Return the new role mapping to use
   *
   * @return the rolesMapping
   */
  Map<String, String> getRolesMapping();

  /**
   * Set the new role mapping to use
   *
   * @param pRolesMapping
   *          the rolesMapping to set
   */
  void setRolesMapping(Map<String, String> pRolesMapping);
}
