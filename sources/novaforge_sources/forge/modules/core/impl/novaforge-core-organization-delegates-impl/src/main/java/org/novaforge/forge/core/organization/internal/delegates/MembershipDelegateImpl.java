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
package org.novaforge.forge.core.organization.internal.delegates;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.delegates.MembershipDelegate;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class MembershipDelegateImpl implements MembershipDelegate
{
  /**
   * Reference to {@link MembershipDAO} service injected by the container
   */
  private MembershipDAO             membershipDAO;
  /**
   * Reference to {@link ForgeCfgService} service injected by the container
   */
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * Use by container to inject {@link MembershipDAO}
   *
   * @param pMembershipDAO
   *          the membershipDAO to set
   */
  public void setMembershipDAO(final MembershipDAO pMembershipDAO)
  {
    membershipDAO = pMembershipDAO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<User> getAllSuperAdmin()
  {
    final List<User> returnList = new ArrayList<User>();

    final List<Actor> actors = membershipDAO.findActorsByProjectAndRole(
        forgeConfigurationService.getForgeProjectId(),
        forgeConfigurationService.getForgeSuperAdministratorRoleName());
    for (final Actor actor : actors)
    {
      if (actor instanceof User)
      {
        final User user = (User) actor;
        returnList.add(user);
      }
      else
      {
        if (actor instanceof Group)
        {
          final Group group = (Group) actor;
          returnList.addAll(group.getUsers());
        }

      }
    }
    return returnList;
  }

  /**
   * Use by container to inject {@link ForgeConfigurationService}
   *
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<User> getAllProjectAdministrators()
  {
    final List<User> administrators = new ArrayList<User>();

    final List<Actor> actors = membershipDAO.findActorsByRole(forgeConfigurationService
        .getForgeAdministratorRoleName());
    for (final Actor actor : actors)
    {
      if (actor instanceof Group)
      {
        final Group group = (Group) actor;
        for (final User user : group.getUsers())
        {
          administrators.add(user);
        }
      }
      else
      {
        administrators.add((User) actor);
      }
    }
    return administrators;
  }

  @Override
  public boolean isSuperAdmin(final String pUserLogin)
  {
    boolean returnValue = false;
    if (pUserLogin != null)
    {
      final List<User> allSuperAdmin = getAllSuperAdmin();
      for (final User user : allSuperAdmin)
      {
        if (user.getLogin().equals(pUserLogin))
        {
          returnValue = true;
          break;
        }
      }
    }
    return returnValue;
  }

}
