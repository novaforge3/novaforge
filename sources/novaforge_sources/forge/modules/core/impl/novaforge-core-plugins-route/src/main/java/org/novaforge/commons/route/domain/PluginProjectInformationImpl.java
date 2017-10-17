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
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProjectInformation;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lamirang
 */
public class PluginProjectInformationImpl implements PluginProjectInformation
{

  private static final long      serialVersionUID = 1071780587937183937L;
  private PluginProject          project;
  private List<PluginMembership> memberships;
  private Map<String, String>    rolesMapping;

  public PluginProjectInformationImpl(final Project pProject, final List<Membership> pMemberships,
      final Map<String, String> pRolesMapping, final String pAuthor)
  {
    project = new PluginProjectImpl(pProject, pAuthor);
    memberships = toPluginMemberships(pMemberships);
    rolesMapping = pRolesMapping;
  }

  private List<PluginMembership> toPluginMemberships(final List<Membership> pMemberships)
  {
    final List<PluginMembership> pluginMemberships = new ArrayList<PluginMembership>();
    for (final Membership membership : pMemberships)
    {
      final Actor actor = membership.getActor();
      if (actor instanceof User)
      {
        final PluginUser pluginUser = new PluginUserImpl((User) actor);
        final PluginMembership pluginMembershipImpl = new PluginMembershipImpl(pluginUser, membership
            .getRole().getName());
        pluginMemberships.add(pluginMembershipImpl);
      }
    }
    return pluginMemberships;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginProject getProject()
  {
    return project;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProject(final PluginProject pProject)
  {
    project = pProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginMembership> getMemberships()
  {
    return memberships;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMemberships(final List<PluginMembership> pMemberships)
  {
    memberships = pMemberships;
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

  @Override
  public String toString()
  {
    return "PluginProjectInformationImpl [memberships=" + memberships + ", project=" + project
        + ", rolesMapping=" + rolesMapping + "]";
  }

}
