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
package org.novaforge.commons.route.domain;

import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginRoleInformation;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author sbenoist
 */
public class PluginRoleInformationImpl implements PluginRoleInformation
{

  private static final long      serialVersionUID = 5791195346476366708L;

  private List<PluginMembership> removeMemberships;
  private List<PluginMembership> updateMemberships;

  private Map<String, String>    rolesMapping;

  /**
   * Default constructor
   * 
   * @param pPreviousMemberhips
   *          the old membership used to build the removed list
   * @param pUpdateMemberships
   *          the membership to update
   * @param pRolesMapping
   *          the new role mapping
   */
  public PluginRoleInformationImpl(final List<Membership> pPreviousMemberhips,
      final List<Membership> pUpdateMemberships, final Map<String, String> pRolesMapping)
  {
    super();
    updateMemberships = toPluginMemberships(pUpdateMemberships);
    removeMemberships = buildRemovePluginMemberships(pPreviousMemberhips, pUpdateMemberships);
    rolesMapping = pRolesMapping;
  }

  private List<PluginMembership> toPluginMemberships(final List<Membership> pMemberships)
  {
    final List<PluginMembership> pluginMemberships = new ArrayList<PluginMembership>();
    for (final Membership membership : pMemberships)
    {
      pluginMemberships.add(toPluginMembership(membership));
    }
    return pluginMemberships;
  }

  private List<PluginMembership> buildRemovePluginMemberships(final List<Membership> pPreviousMemberhips,
      final List<Membership> pUpdateMemberships)
  {
    final List<PluginMembership> pluginMemberships = new ArrayList<PluginMembership>();
    for (final Membership previousMembership : pPreviousMemberhips)
    {
      final UUID previousUUID = previousMembership.getActor().getUuid();
      final boolean toUpdate = isUpdated(pUpdateMemberships, previousUUID);
      if (!toUpdate)
      {
        pluginMemberships.add(toPluginMembership(previousMembership));
      }
    }
    return pluginMemberships;

  }

  private PluginMembership toPluginMembership(final Membership membership)
  {
    PluginMembership pluginMembership = null;
    final Actor      actor            = membership.getActor();
    if (actor instanceof User)
    {
      final PluginUser pluginUser = new PluginUserImpl((User) actor);
      pluginMembership = new PluginMembershipImpl(pluginUser, membership.getRole().getName());
    }
    return pluginMembership;
  }

  private boolean isUpdated(final List<Membership> pUpdateMemberships, final UUID pPreviousUUID)
  {
    boolean isUpdated = false;
    if ((pPreviousUUID != null) && (pUpdateMemberships != null))
    {
      for (final Membership updatedMemberhsip : pUpdateMemberships)
      {
        final UUID uuid = updatedMemberhsip.getActor().getUuid();
        if (pPreviousUUID.equals(uuid))
        {
          isUpdated = true;
          break;
        }
      }
    }
    return isUpdated;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginMembership> getRemoveMemberships()
  {
    return removeMemberships;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRemoveMemberships(final List<PluginMembership> pMemberships)
  {
    removeMemberships = pMemberships;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginMembership> getUpdateMemberships()
  {
    return updateMemberships;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUpdateMemberships(final List<PluginMembership> pMemberships)
  {
    updateMemberships = pMemberships;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getRolesMapping()
  {
    return rolesMapping;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRolesMapping(final Map<String, String> pRolesMapping)
  {
    rolesMapping = pRolesMapping;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "PluginRoleInformationImpl [removeMemberships=" + removeMemberships + ", updateMemberships="
               + updateMemberships + ", rolesMapping=" + rolesMapping + "]";
  }
}
