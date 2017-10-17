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
package org.novaforge.forge.configuration.csv.internal;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.GroupPresenter;
import org.novaforge.forge.core.organization.presenters.LanguagePresenter;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.business.ReferentielManager;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.osgi.framework.BundleContext;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ImportNovaforge
{
  public static final  String FORGE_GOD        = "*";
  public static final  String LANGUAGE_FR_NAME = "FR";
  public static final  String LANGUAGE_EN_NAME = "EN";
  private static final Log    LOG              = LogFactory.getLog(ImportNovaforge.class);
  private static final String LOC_FILE         = "importdata.lock";
  // TODO voir avec philippe pour parametriser ca
  private final        String directoryNfConf  = "/datas/novaforge3/datas/importcsv/";
  private ProjectPresenter          projectPresenter;
  private SpacePresenter            spacePresenter;
  private ApplicationPresenter      applicationPresenter;
  private UserPresenter             userPresenter;
  private LanguagePresenter         languagePresenter;
  private ManagementModuleManager   managementModuleManager;
  private ReferentielManager        referentielManager;
  private ProjectPlanManager        projectPlanManager;
  private IterationManager          iterationManager;
  private ProjectRolePresenter      rolePresenter;
  private GroupPresenter            groupPresenter;
  private MembershipPresenter       membershipPresenter;
  private AuthentificationService   authentificationService;
  private PluginsManager            pluginsManager;
  private ForgeConfigurationService forgeConfigurationService;
  private BundleContext             bundleContext;

  // private final String directoryNfConf =
  // "C:/datas/novaforge3/datas/importcsv/";

  public void starting()
  {
    final File workingDir = bundleContext.getDataFile(LOC_FILE);
    LOG.info("Starting ImportNovaforge");
    try
    {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userPresenter.getUser(superAdministratorLogin);
      authentificationService.login(superAdministratorLogin, user.getPassword());
      if (workingDir.exists())
      {
        init();

        authentificationService.logout();

        final ImportManagementModule imp = new ImportManagementModuleImpl();

        imp.init(projectPlanManager, managementModuleManager, referentielManager, iterationManager,
            forgeConfigurationService, authentificationService, userPresenter, directoryNfConf);

        LOG.info("ImportCSV done.");
      }
      workingDir.createNewFile();
    }
    catch (final Exception e)
    {
      LOG.error("Unable to initialize forge datas", e);
    }
    finally
    {
      authentificationService.logout();
    }
  }

  public void init() throws Exception
  {
    reinitBase();
    importUsers();
    importProjects();
    importRoles();
    importGroups();
    importMemberships();
    importSpaces();
    importApps();
  }

  private void reinitBase() throws Exception
  {
    LOG.info("Reinit Base");
    final String filename = directoryNfConf + "reinit_base.bat";
    final File f = createFile(filename);
    if (f.exists())
    {
      try
      {
        final Process p = Runtime.getRuntime().exec("cmd /c " + filename);
        p.waitFor();
      }
      catch (final IOException e)
      {
        throw new Exception(e);
      }
      catch (final InterruptedException e)
      {
        throw new Exception(e);
      }
    }
  }

  private void importUsers() throws UserServiceException, LanguageServiceException, IOException
  {
    LOG.info("Import users");
    final String filename = directoryNfConf + "users.csv";
    final File f = createFile(filename);
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(f));
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null)
      {
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {
          LOG.info(Arrays.toString(nextLine));
          try
          {
            final UserProfile userProfile = userPresenter.getUserProfile(nextLine[0]);
            LOG.info("Import user " + nextLine[0] + " is already exist");
            userProfile.getUser().setName(nextLine[1]);
            userProfile.getUser().setFirstName(nextLine[2]);
            userProfile.getUser().setEmail(nextLine[3]);
            userProfile.getUser().setPassword(nextLine[4]);
            userProfile.getUser().setLanguage(languagePresenter.getLanguage(LANGUAGE_EN_NAME));
            userPresenter.updateUserProfile(userProfile);
          }
          catch (final Exception e)
          {

            final User user = userPresenter.newUser();
            user.setLogin(nextLine[0]);
            user.setName(nextLine[1]);
            user.setFirstName(nextLine[2]);
            user.setEmail(nextLine[3]);
            user.setPassword(nextLine[4]);
            user.setLanguage(languagePresenter.getLanguage(LANGUAGE_EN_NAME));
            userPresenter.createUser(user);
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine));
        }
      }
    }
  }

  private void importProjects() throws ProjectServiceException, IOException
  {
    LOG.info("Import projects");

    final String filename = directoryNfConf + "projects.csv";
    final File f = createFile(filename);
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(f));
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null)
      {
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {

          LOG.info(Arrays.toString(nextLine));
          final Project project = projectPresenter.newProject();
          project.setProjectId(nextLine[0]);
          project.setName(nextLine[1]);
          project.setDescription(nextLine[2]);
          project.setLicenceType(nextLine[3]);
          try
          {
            projectPresenter.deleteProject(nextLine[0]);
          }

          catch (final ProjectServiceException e)
          {
            LOG.info("nouveau projet - pas de projet ancien", e);
          }
          projectPresenter.createProject(project);
          projectPresenter.validateProject(nextLine[0]);
        }
      }
    }
  }

  private void importRoles() throws ProjectServiceException, IOException
  {

    LOG.info("Import roles");

    // This is needed in order to authorize role creating
    final String filename = directoryNfConf + "roles.csv";
    final File f = createFile(filename);
    LOG.info("Import roles begin");
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(f));
      String[] nextLine;
      LOG.info("Import roles before while");
      while ((nextLine = reader.readNext()) != null)
      {
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {
          LOG.info("Import roles " + Arrays.toString(nextLine));
          final ProjectRole role = rolePresenter.newRole();
          role.setName(nextLine[1]);
          role.setDescription(nextLine[2]);
          try
          {
            rolePresenter.getRole(nextLine[1], nextLine[0]);
          }
          catch (final ProjectServiceException e)
          {
            rolePresenter.createRole(role, nextLine[0]);
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine));
        }
      }
    }
    else
    {
      LOG.error("Impossible to read " + filename);
    }
  }

  private void importGroups() throws IOException, GroupServiceException, UserServiceException
  {

    LOG.info("Import groups");

    // Add all group
    String filename = directoryNfConf + "groups.csv";
    final File f = createFile(filename);
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(f));

      String[] nextLine;
      while ((nextLine = reader.readNext()) != null)
      {
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {
          LOG.info(Arrays.toString(nextLine));
          final String projectId = nextLine[0];
          final String groupName = nextLine[1];
          final String groupDes = nextLine[2];
          final Group group = groupPresenter.newGroup();
          group.setName(groupName);
          group.setDescription(groupDes);
          try
          {
            groupPresenter.createGroup(group, projectId);
          }
          catch (final Exception e)
          {
            LOG.error(e,e);
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine));
        }
      }
    }
    else
    {
      LOG.info("Impossible to read " + filename);
    }

    // Add group relation
    filename = directoryNfConf + "groups_users.csv";
    final File f2 = createFile(filename);
    if (f2.exists())
    {
      final CSVReader reader2 = new CSVReader(new FileReader(f2));
      String[] nextLine2;
      while ((nextLine2 = reader2.readNext()) != null)
      {
        if ((nextLine2.length > 1) && !nextLine2[0].startsWith("#"))
        {

          final String projectId = nextLine2[0];
          final String groupId = nextLine2[1];
          final String userId = nextLine2[2];
          final Group group = groupPresenter.getGroup(projectId, groupId);

          final User user = userPresenter.getUser(userId);
          group.addUser(user);
          try
          {
            groupPresenter.updateGroup(projectId, groupId, group);
          }
          catch (final Exception e)
          {
            LOG.error(e,e);
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine2));
        }
      }
    }
    else
    {
      LOG.info("Impossible to read " + filename);
    }
  }

  private void importMemberships() throws ProjectServiceException, IOException, GroupServiceException
  {

    LOG.info("Import membership");

    final String filename = directoryNfConf + "memberships_users.csv";
    final File f = createFile(filename);
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(f));
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null)
      {
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {
          LOG.info(Arrays.toString(nextLine));
          final String projectId = nextLine[0];
          final String userLogin = nextLine[1];
          final String roleName = nextLine[2];
          final Set<String> newRoles = new HashSet<String>();
          newRoles.add(roleName);
          final List<Membership> membershipsUser = membershipPresenter
              .getAllEffectiveUserMembershipsForUserAndProject(projectId, userLogin);
          if ((membershipsUser == null) || membershipsUser.isEmpty())
          {
            try
            {
              final User user = userPresenter.getUser(userLogin);
              membershipPresenter.addUserMembership(projectId, user.getUuid(), newRoles, null, false);
            }
            catch (final Exception e)
            {
              LOG.error(e,e);
            }
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine));
        }
      }
    }
    else
    {
      LOG.info("Impossible to read " + filename);
    }
    final String filename2 = directoryNfConf + "memberships_groups.csv";
    final File f2 = createFile(filename2);
    if (f2.exists())
    {
      final CSVReader reader2 = new CSVReader(new FileReader(f2));
      String[] nextLine2;
      while ((nextLine2 = reader2.readNext()) != null)
      {
        if ((nextLine2.length > 1) && !nextLine2[0].startsWith("#"))
        {
          LOG.info(Arrays.toString(nextLine2));
          final String projectId = nextLine2[0];
          final String userLogin = nextLine2[1];
          final String roleName = nextLine2[2];
          final Set<String> newRoles = new HashSet<String>();
          newRoles.add(roleName);

          try
          {
            final Group group = groupPresenter.getGroup(projectId, userLogin);
            membershipPresenter.addGroupMembership(projectId, group.getUuid(), newRoles, null);
          }
          catch (final ProjectServiceException e)
          {
            if (!ExceptionCode.ERR_UPDATE_PROJECT_MEMBERSHIP_SENDING_MAIL.equals(e.getCode()))
            {
              throw e;
            }
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine2));
        }
      }
    }
    else
    {
      LOG.info("Impossible to read " + filename);
    }
  }

  private void importSpaces() throws IOException, ProjectServiceException
  {

    LOG.info("Import spaces");

    final String filename = directoryNfConf + "spaces.csv";
    final File f = createFile(filename);
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(f));
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null)
      {
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {
          LOG.info(Arrays.toString(nextLine));
          final String projectId = nextLine[0];
          final String spaceName = nextLine[1];
          final Space space = spacePresenter.newSpace();
          space.setName(spaceName);
          try
          {
            spacePresenter.addSpace(projectId, space);
          }
          catch (final Exception e)
          {
            LOG.error(e,e);
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine));
        }
      }
    }
    else
    {
      LOG.info("Impossible to read " + filename);
    }
  }

  private void importApps() throws IOException, ProjectServiceException, PluginManagerException,
      NumberFormatException, ManagementModuleException
  {

    LOG.info("Import apps");

    final String filename = directoryNfConf + "apps.csv";
    final File f = createFile(filename);
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(f));
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null)
      {
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {
          LOG.info(Arrays.toString(nextLine));
          final String projectId = nextLine[0];
          final String spaceUri = nextLine[1];
          final String appName = nextLine[2];
          final String catName = nextLine[3];
          final String typeName = nextLine[4];
          try
          {
            addApplication(projectId, spaceUri, getAppMapping(typeName, projectId, appName), catName,
                typeName, appName);
          }
          catch (final Exception e)
          {
            LOG.error(e,e);
          }
        }
        else
        {
          LOG.info("la ligne commencant par " + nextLine[0] + " est ignorée");
        }

      }
    }
    else
    {
      LOG.info("Impossible to read " + filename);
    }
  }

  private File createFile(final String filename)
  {
    return new File(filename);
  }

  /**
   * @param projectId
   * @param spaceUri
   * @param roleMappings
   *
   * @throws PluginManagerException
   * @throws ApplicationServiceException
   */
  private void addApplication(final String projectId, final String spaceUri, final Map<String, String> roleMappings,
                              final String appCategory, final String appType, final String appName)
      throws ApplicationServiceException, PluginManagerException
  {
    final List<PluginMetadata> plugins = pluginsManager.getPluginsMetadataByCategory(appCategory);
    if ((plugins != null) && !plugins.isEmpty())
    {
      for (final PluginMetadata plugin : plugins)
      {
        if (appType.equals(plugin.getType()))
        {
          final String pluginUUID = plugin.getUUID();

          applicationPresenter.addApplication(projectId, spaceUri, appName, appName, UUID.fromString(pluginUUID),
                                              roleMappings);
          LOG.info(String.format("Application %s created.", appType));
        }
      }
    }
    else
    {
      LOG.info(String.format("No application exists for the category %s", appCategory));
    }
  }

  private Map<String, String> getAppMapping(final String pType, final String pProjectId, final String pUri)
      throws ProjectServiceException, IOException, PluginManagerException
  {
    final Map<String, String> maps = new HashMap<String, String>();
    final String filename = String.format(directoryNfConf + "%s.csv", pType);
    final File f = createFile(filename);
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(f));
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null)
      {
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {
          LOG.info(Arrays.toString(nextLine));
          final String projectId = nextLine[0];
          final String appName = nextLine[1];
          final String forgeRole = nextLine[2];
          final String appRole = nextLine[3];
          if ((pProjectId.equals(projectId)) && (pUri.equals(appName)))
          {
            maps.put(forgeRole, appRole);
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine));
        }
      }
    }
    else
    {
      LOG.info("Impossible to read " + filename);
    }
    return maps;
  }

  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  public void setSpacePresenter(final SpacePresenter pSpacePresenter)
  {
    spacePresenter = pSpacePresenter;
  }

  public void setApplicationPresenter(final ApplicationPresenter pApplicationPresenter)
  {
    applicationPresenter = pApplicationPresenter;
  }

  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

  public void setLanguagePresenter(final LanguagePresenter pLanguagePresenter)
  {
    languagePresenter = pLanguagePresenter;
  }

  public void setManagementModuleManager(final ManagementModuleManager pManagementModuleManager)
  {
    managementModuleManager = pManagementModuleManager;
  }

  public void setReferentielManager(final ReferentielManager pReferentielManager)
  {
    referentielManager = pReferentielManager;
  }

  public void setProjectPlanManager(final ProjectPlanManager pProjectPlanManager)
  {
    projectPlanManager = pProjectPlanManager;
  }

  public void setIterationManager(final IterationManager pIterationManager)
  {
    iterationManager = pIterationManager;
  }

  public void setRolePresenter(final ProjectRolePresenter pRolePresenter)
  {
    rolePresenter = pRolePresenter;
  }

  public void setGroupPresenter(final GroupPresenter pGroupPresenter)
  {
    groupPresenter = pGroupPresenter;
  }

  public void setMembershipPresenter(final MembershipPresenter pMembershipPresenter)
  {
    membershipPresenter = pMembershipPresenter;
  }

  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  public void setBundleContext(final BundleContext pBundleContext)
  {
    bundleContext = pBundleContext;
  }

}
