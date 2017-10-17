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
package org.novaforge.forge.commands.distribution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.services.MembershipService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This command (launched on LOCAL/ZONAL or CENTRAL forge) will list project id, project name, organization,
 * roles and number of users having this role.
 * 
 * @author Marc Blachon
 */
@Command(scope = "distribution", name = "reporting-check-project",
    description = "list project Id, name, organisation and the roles with users number within it")
public class ReportingCheckProjectCommand extends OsgiCommandSupport
{
  private static final Log log = LogFactory.getLog(ReportingCheckProjectCommand.class);
  private AuthentificationService   authentificationService;
  private ForgeConfigurationService forgeConfigurationService;
  private UserService               userService;
  private ProjectService            projectService;
  private MembershipService         membershipService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    // loop on project Id
    // TODO: get project name, projectId, organization
    try
    {
      final ProjectOptions projectOptions = projectService.newProjectOptions();
      projectOptions.setRetrievedImage(false);
      projectOptions.setRetrievedOrganization(true);
      projectOptions.setRetrievedSystem(false);
      final List<Project> validatedProjects = projectService.getAllProjectsByStatus(projectOptions,
          ProjectStatus.VALIDATED);

      for (final Project project : validatedProjects)
      {
        String roleUsersList = "";
        final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
        final User user = userService.getUser(superAdministratorLogin);
        authentificationService.login(superAdministratorLogin, user.getPassword());

        final HashMap<String, List<String>> rolesNbUsers = new HashMap<String, List<String>>();
        final List<Membership> memberships = membershipService
            .getAllEffectiveUserMembershipsForProject(project.getProjectId());
        // case membership actor not group.
        for (final Membership membership : memberships)
        {
          final ProjectRole role = membership.getRole();

          final Actor actor = membership.getActor();
          if (actor instanceof User)
          {
            final User userf = (User) actor;

            // if the key (role) does not exist
            if (!rolesNbUsers.keySet().contains(role.getName()))
            {
              final ArrayList<String> listForUsers = new ArrayList<String>();
              listForUsers.add(userf.getLogin());
              rolesNbUsers.put(role.getName(), listForUsers);
            }
            else
            {
              // key (role) already exists
              final List<String> prevListForUsers = rolesNbUsers.get(role.getName());
              // if the list does not contain this role
              if (!prevListForUsers.contains(userf.getLogin()))
              {
                prevListForUsers.add(userf.getLogin());
                rolesNbUsers.put(role.getName(), prevListForUsers);
              }
            }
          }
          // case actor is a group
          else if (actor instanceof Group)
          {
            final Group group = (Group) actor;
            final List<User> users = group.getUsers();
            // get all users of the group and check if there have already been added for the role.
            for (final User userInGroup : users)
            {

              // if the key (role) does not exist
              if (!rolesNbUsers.keySet().contains(role.getName()))
              {
                final ArrayList<String> listForUsers = new ArrayList<String>();
                listForUsers.add(userInGroup.getLogin());
                rolesNbUsers.put(role.getName(), listForUsers);
              }
              else
              {
                // key (role) already exists
                final List<String> prevListForUsers = rolesNbUsers.get(role.getName());
                // if the list does not contain this role
                if (!prevListForUsers.contains(userInGroup.getLogin()))
                {
                  prevListForUsers.add(userInGroup.getLogin());
                  rolesNbUsers.put(role.getName(), prevListForUsers);
                }
              }
            }

          }
        }

        // format the output to get a string like this ex: Administrator:1 testeur:0 developpeur:0
        // replace the users list by users number
        for (final String foundRole : rolesNbUsers.keySet())
        {
          final int usersNb = rolesNbUsers.get(foundRole).size();
          roleUsersList = roleUsersList + foundRole + ":" + usersNb + " ";
        }

        if (roleUsersList.length() > 0)
        {
          // remove last blank character
          roleUsersList = roleUsersList.substring(0, roleUsersList.length() - 1);
        }

        // add here projectId, ....roleUsersList
        final String projectId = project.getProjectId();
        final String projecName = project.getName();
        String organization = "";
        if (project.getOrganization() != null)
        {
          organization = project.getOrganization().getName();
        }
        // to avoid empty org
        else
        {
          organization = "NONE";
        }

        System.out.println(projectId + "|" + projecName + "|" + organization + "|" + roleUsersList);
      }
    }
    finally
    {
      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
    return null;
  }

  // ----------------- setter used by the container to inject services -------------------------------

  public void setAuthentificationService(final AuthentificationService authentificationService)
  {
    this.authentificationService = authentificationService;
  }

  public void setUserService(final UserService userService)
  {
    this.userService = userService;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }

  public void setProjectService(final ProjectService projectService)
  {
    this.projectService = projectService;
  }

  public void setMembershipService(final MembershipService membershipService)
  {
    this.membershipService = membershipService;
  }

}
