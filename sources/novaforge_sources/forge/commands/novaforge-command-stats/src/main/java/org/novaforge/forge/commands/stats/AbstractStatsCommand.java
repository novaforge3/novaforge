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
package org.novaforge.forge.commands.stats;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.commands.stats.internal.model.Constants;
import org.novaforge.forge.commands.stats.internal.model.StatsProject;
import org.novaforge.forge.commands.stats.internal.model.StatsUsers;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public abstract class AbstractStatsCommand extends OsgiCommandSupport
{
  private static final Log LOGGER = LogFactory.getLog(AbstractStatsCommand.class);
  protected ForgeConfigurationService forgeConfigurationService;
  protected AuthentificationService   authentificationService;
  protected HistorizationService      historizationService;
  protected UserPresenter             userPresenter;
  protected ProjectPresenter          projectPresenter;
  protected MembershipPresenter       membershipPresenter;
  protected PluginsManager            pluginsManager;
  protected ApplicationPresenter      applicationPresenter;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    // Disactivate historization if implemented
    historizationService.setActivatedMode(false);

    // login
    login();

    // Build stats
    final List<StatsProject> buildStats = buildStats();
    process(buildStats);

    // Logout
    authentificationService.logout();

    // re-activate historization
    historizationService.setActivatedMode(true);
    return null;
  }

  private void login() throws UserServiceException
  {
    final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();

    final org.novaforge.forge.core.organization.model.User user = userPresenter
        .getUser(superAdministratorLogin);
    authentificationService.login(superAdministratorLogin, user.getPassword());
  }

  private List<StatsProject> buildStats() throws Exception
  {
    final List<StatsProject> stats = new ArrayList<>();

    // Retrieve all validated project and sort them by name
    final List<Project> projects = getProjects();

    // Browse projects
    for (final Project project : projects)
    {
      final String projectId = project.getElementId();
      if (!Arrays.asList(Constants.IGNORED_PROJECT).contains(projectId))
      {
        final StatsProject statsProject = new StatsProject(project.getName(), project.getCreated(),
            getAuthorEmail(project.getAuthor()));

        try
        {
          // Retrieve effective members
          final List<Membership> members = membershipPresenter
              .getAllEffectiveUserMembershipsForProject(projectId);

          // Add members column
          final Set<Actor> actors = getMembers(members);
          final StatsUsers statsUsers = new StatsUsers(actors.size(), getExtActorNumber(actors));
          statsProject.setUsers(statsUsers);

          // Process apps
          for (final String type : Constants.APPS)
          {
            final StatsUsers statsApp = buildStatsApp(projectId, members, type);
            if (statsApp != null)
            {
              statsProject.getApplications().put(type, statsApp);
            }
          }
        }
        catch (final Exception e)
        {
          // Ignore this
          LOGGER.warn(String.format("Errors when processing project [%s]", projectId), e);
        }
        stats.add(statsProject);
      }

    }
    return stats;

  }

  protected abstract void process(final List<StatsProject> pStatsProjects) throws Exception;

  private List<Project> getProjects() throws ProjectServiceException
  {

    // Retrieve all validated project and sort them by name
    final List<Project> projects = projectPresenter.getAllProjects(projectPresenter.newProjectOptions(false,
        false, false));
    final List<Project> sorted = new ArrayList<>(projects);
    Collections.sort(sorted, new Comparator<Project>()
    {

      @Override
      public int compare(final Project pO1, final Project pO2)
      {
        final String p1Name = pO1.getName().toLowerCase();
        final String p2Name = pO2.getName().toLowerCase();
        return p1Name.compareTo(p2Name);
      }
    });
    return sorted;
  }

  private String getAuthorEmail(final String pLogin)
  {
    String email = "unknown";
    try
    {
      final User user = userPresenter.getUser(pLogin);
      email = user.getEmail();
    }
    catch (final UserServiceException e)
    {
      // Ignore this
    }
    return email.toLowerCase();
  }

  private Set<Actor> getMembers(final List<Membership> pMembers)
  {
    final Set<Actor> members = new HashSet<>();
    for (final Membership membership : pMembers)
    {
      final Actor actor = membership.getActor();
      if ((actor instanceof User) && (RealmType.USER.equals(((User) actor).getRealmType())))
      {
        members.add(actor);
      }
    }
    return members;
  }

  private int getExtActorNumber(final Set<Actor> pMembers)
  {
    int externalMembers = 0;
    if (pMembers != null)
    {
      for (final Actor actor : pMembers)
      {
        if (actor instanceof User)
        {
          final User user = (User) actor;

          if (isExternalEmail(user.getEmail()))
          {
            externalMembers++;
          }
        }

      }
    }
    return externalMembers;
  }

  private StatsUsers buildStatsApp(final String projectId, final List<Membership> members, final String type)
      throws Exception, ApplicationServiceException
  {
    StatsUsers        statsApplication = null;
    boolean           hasApp           = false;
    int               actorsForApp     = 0;
    int               actorsExtForApp  = 0;
    final Set<String> pluginUUIDs      = getPluginUUIDs(type);
    for (final String pluginUUID : pluginUUIDs)
    {
      final List<ProjectApplication> applications = applicationPresenter.getAllProjectApplications(projectId,
                                                                                                   pluginUUID);

      if (applications != null)
      {
        final Set<Actor> actorsForPlugin = new HashSet<>();
        for (final ProjectApplication application : applications)
        {
          hasApp = true;
          final String applicationURI = application.getUri();
          final Map<String, String> roleMapping = applicationPresenter.getRoleMapping(projectId, applicationURI);
          if (roleMapping != null)
          {
            final Set<Entry<String, String>> entrySet = roleMapping.entrySet();
            for (final Entry<String, String> entry : entrySet)
            {
              final Set<Actor> actorForRole = getActorForRole(members, entry.getKey());
              actorsForPlugin.addAll(actorForRole);
            }
          }
        }
        actorsForApp += actorsForPlugin.size();
        actorsExtForApp += getExtActorNumber(actorsForPlugin);
      }
    }
    if (hasApp)
    {
      statsApplication = new StatsUsers(actorsForApp, actorsExtForApp);
    }
    return statsApplication;
  }

  private boolean isExternalEmail(final String pEmail)
  {
    boolean isExternal = true;
    if (!"".equals(pEmail))
    {
      for (final String internalMail : Constants.INTERNAL_EMAIL)
      {
        if (pEmail.endsWith(internalMail))
        {
          isExternal = false;
          break;
        }
      }
    }
    return isExternal;
  }

  private Set<String> getPluginUUIDs(final String pType) throws Exception
  {
    final Set<String> uuids = new HashSet<>();
    final List<PluginMetadata> plugins = pluginsManager.getPluginsMetadataByType(pType.toLowerCase());
    if (plugins.size() == 0)
    {
      throw new Exception(String.format("Cannot find any %s plugin.", pType));
    }
    for (final PluginMetadata pluginMetadata : plugins)
    {
      uuids.add(pluginMetadata.getUUID());
    }
    return uuids;
  }

  private Set<Actor> getActorForRole(final List<Membership> pMembers, final String pRole)
  {
    final Set<Actor> actors = new HashSet<>();

    if (pMembers != null)
    {
      for (final Membership membership : pMembers)
      {
        final Actor actor = membership.getActor();
        final Role role = membership.getRole();
        if ((actor instanceof User) && (RealmType.USER.equals(((User) actor).getRealmType())) && (role.getName()
                                                                                                      .equals(pRole)))
        {
          actors.add(actor);
        }
      }
    }
    return actors;

  }

  /**
   * @param forgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }

  /**
   * @param userPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter userPresenter)
  {
    this.userPresenter = userPresenter;
  }

  /**
   * @param authentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService authentificationService)
  {
    this.authentificationService = authentificationService;
  }

  /**
   * @param historizationService
   *          the historizationService to set
   */
  public void setHistorizationService(final HistorizationService historizationService)
  {
    this.historizationService = historizationService;
  }

  /**
   * @param pMembershipPresenter
   *          the membershipPresenter to set
   */
  public void setMembershipPresenter(final MembershipPresenter pMembershipPresenter)
  {
    membershipPresenter = pMembershipPresenter;
  }

  /**
   * @param projectPresenter
   *          the projectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter projectPresenter)
  {
    this.projectPresenter = projectPresenter;
  }

  /**
   * @param pluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pluginsManager)
  {
    this.pluginsManager = pluginsManager;
  }

  /**
   * @param applicationPresenter
   *          the applicationPresenter to set
   */
  public void setApplicationPresenter(final ApplicationPresenter applicationPresenter)
  {
    this.applicationPresenter = applicationPresenter;
  }
}
