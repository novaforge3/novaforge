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
/**
 * 
 */
package org.novaforge.forge.importexport.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.GroupPresenter;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.importexport.datas.model.Domains;
import org.novaforge.forge.importexport.datas.model.Domains.DomainElement;
import org.novaforge.forge.importexport.datas.model.Forge;
import org.novaforge.forge.importexport.datas.model.Memberships;
import org.novaforge.forge.importexport.datas.model.MembershipsGroups;
import org.novaforge.forge.importexport.datas.model.MembershipsGroups.MembershipGroup;
import org.novaforge.forge.importexport.datas.model.MembershipsUsers;
import org.novaforge.forge.importexport.datas.model.MembershipsUsers.MembershipUser;
import org.novaforge.forge.importexport.datas.model.Projects;
import org.novaforge.forge.importexport.datas.model.Projects.ProjectElement;
import org.novaforge.forge.importexport.exceptions.ImportExportServiceException;
import org.novaforge.forge.importexport.mappers.ExportDataMapper;
import org.novaforge.forge.importexport.services.ExportService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public class ExportServiceImpl implements ExportService
{
  private static final Log LOG = LogFactory.getLog(ExportServiceImpl.class);
  private UserPresenter             userPresenter;
  private ProjectPresenter          projectPresenter;
  private ApplicationPresenter      applicationPresenter;
  private SpacePresenter            spacePresenter;
  private GroupPresenter            groupPresenter;
  private ProjectRolePresenter      projectRolePresenter;
  private ForgeConfigurationService forgeConfigurationService;
  private ExportDataMapper          exportDataMapper;
  private PluginsManager            pluginsManager;
  private MembershipPresenter       membershipPresenter;
  private AuthentificationService   authentificationService;
  private HistorizationService      historizationService;
  private Forge                     forge = new Forge();

  @Override
  public void exportDatas(final String pExportFilePath) throws ImportExportServiceException
  {
    LOG.info("Starting export forge process ...");
    this.export(pExportFilePath, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void exportProject(final String pExportFilePath, final String pProjectId)
      throws ImportExportServiceException
  {
    LOG.info("Starting export project process ...");
    this.export(pExportFilePath, pProjectId);
  }

  private void export(final String pExportFilePath, final String pProjectId)
      throws ImportExportServiceException
  {
    try
    {
      final File exportXML = new File(pExportFilePath);
      // Disactivate historization if implemented
      historizationService.setActivatedMode(false);

      login();

      // Do the job
      forgeToXML(exportXML, pProjectId);

      // Activate historization if implemented
      historizationService.setActivatedMode(true);

      // Logout
      authentificationService.logout();
    }
    catch (Exception e)
    {
      throw new ImportExportServiceException("an error occurred during export datas", e);
    }
  }

  private void login() throws ImportExportServiceException
  {
    try
    {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userPresenter.getUser(superAdministratorLogin);
      authentificationService.login(superAdministratorLogin, user.getPassword());
    }
    catch (final Exception e)
    {
      throw new ImportExportServiceException("Unable to authenticate super administrator", e);
    }
  }

  private void forgeToXML(final File pExportXML, final String pProjectId) throws Exception
  {
    if (pProjectId != null)
    {
      buildProject(pProjectId);
    }
    else
    {
      // fill the forge object to export
      buildForge();
    }
    LOG.info(String.format("END of the export and marshalling of the forge in the file : %s",
        pExportXML.getPath()));

    final JAXBContext jaxbContext = JAXBContext.newInstance(Forge.class);
    final Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    marshaller.marshal(forge, pExportXML);
  }

  private void buildProject(final String pProjectId) throws ExportException
  {
    try
    {
      // get the users who have any membership in the project
      List<User> users = new ArrayList<User>();
      List<MembershipInfo> memberships = membershipPresenter.getAllUserMemberships(pProjectId, false);
      for (MembershipInfo membership : memberships)
      {
        users.add((User) membership.getActor());
      }
      forge.setUsers(exportDataMapper.toUsers(users));

      // get the projects
      Projects projects = new Projects();
      Project project = projectPresenter.getProject(pProjectId, false);
      ProjectElement exportedProject = exportProject(project);
      projects.getProjectElement().add(exportedProject);
      forge.setProjects(projects);
    }
    catch (ProjectServiceException e)
    {
      LOG.error("exporting projects or memberships ON ERROR", e);
      throw new ExportException("exporting projects ON ERROR", e);
    }
    catch (Exception e)
    {
      throw new ExportException("another technical error", e);
    }
  }

  private void buildForge() throws ExportException
  {
    try
    {
      // get the users
      List<User> users = userPresenter.getAllUsers(true);
      forge.setUsers(exportDataMapper.toUsers(users));

      // get the projects
      List<Project> forgeProjects = projectPresenter.getAllProjects(true);

      // get the groups for the forge project
      LOG.info("Get the groups for forge project ...");
      List<Group> groups = groupPresenter.getAllGroups(forgeConfigurationService.getForgeProjectId(), true);

      if (groups != null && groups.size() > 0)
      {
        forge.setGroups(exportDataMapper.toGroups(groups));
      }
      LOG.info("Get the projects ...");
      Projects projects = new Projects();
      for (Project project : forgeProjects)
      {
        String projectId = project.getProjectId();
        // do not export the forge and referenciel projects
        if (!"forge".equals(projectId) && !"reference".equals(projectId))
        {
          ProjectElement exportedProject = exportProject(project);
          projects.getProjectElement().add(exportedProject);
        }
      }
      forge.setProjects(projects);
    }
    catch (UserServiceException e)
    {
      LOG.error("exporting users ON ERROR", e);
      throw new ExportException("exporting users ON ERROR", e);
    }
    catch (GroupServiceException e)
    {
      LOG.error("exporting groups ON ERROR", e);
      throw new ExportException("exporting groups ON ERROR", e);
    }
    catch (ProjectServiceException e)
    {
      LOG.error("exporting projects or memberships ON ERROR", e);
      throw new ExportException("exporting projects ON ERROR", e);
    }
    catch (Exception e)
    {
      throw new ExportException("another technical error", e);
    }
  }

  private ProjectElement exportProject(final Project pProject) throws ExportException
  {
    try
    {
      final String projectId = pProject.getProjectId();

      // build the project object
      LOG.info(String.format("build project datas for projectID=%s", projectId));
      ProjectElement exportedProject = exportDataMapper.toProject(pProject);

      // Get the roles of the project
      LOG.info(String.format("Get the roles for project [%s]", projectId));
      List<ProjectRole> roles = projectRolePresenter.getAllRoles(pProject.getProjectId());
      exportedProject = exportDataMapper.addRolesToProject(exportedProject, roles);

      // get the groups of the project
      LOG.info(String.format("Get the groups for project [%s]", projectId));
      List<Group> projectGroups = groupPresenter.getAllGroups(projectId, true);
      if (projectGroups != null && projectGroups.size() > 0)
      {
        exportedProject.setGroups(exportDataMapper.toGroups(projectGroups));
      }

      // Get the memberships to project
      Memberships memberships = exportMemberships(pProject.getProjectId());
      exportedProject.setMemberships(memberships);

      // Get the domains and applications
      Domains domains = exportDomains(pProject.getProjectId());
      exportedProject.setDomains(domains);

      return exportedProject;
    }
    catch (GroupServiceException e)
    {
      LOG.error("exporting groups ON ERROR", e);
      throw new ExportException("exporting groups ON ERROR", e);
    }
    catch (ProjectServiceException e)
    {
      LOG.error("exporting projects or memberships ON ERROR", e);
      throw new ExportException("exporting projects ON ERROR", e);
    }
    catch (PluginManagerException e)
    {
      LOG.error("exporting applications ON ERROR", e);
      throw new ExportException("exporting applications ON ERROR", e);
    }
    catch (Exception e)
    {
      throw new ExportException("another technical error", e);
    }
  }

  private Domains exportDomains(final String pProjectId) throws ProjectServiceException,
      PluginManagerException, SpaceServiceException, ApplicationServiceException
  {
    Domains domains = new Domains();

    List<Space> spaces = spacePresenter.getAllSpaces(pProjectId);
    LOG.info(String.format("Get the spaces for projectID=%s", pProjectId));
    for (Space space : spaces)
    {
      // Get the applications
      List<ProjectApplication> applications = applicationPresenter.getAllSpaceApplications(space.getUri(),
          pProjectId);
      DomainElement domain = exportDataMapper.toDomain(space.getName());

      LOG.info(String.format("Get the applications for projectID=%s", pProjectId));
      for (Application application : applications)
      {
        String uuid = application.getPluginUUID().toString();
        if (pluginsManager.isAvailablePlugin(uuid))
        {
          LOG.info(String.format("Add the application with the name=%s", application.getName()));
          // Get the role mapping
          Map<String, String> rolesMapping = applicationPresenter.getRoleMapping(pProjectId,
              application.getUri());
          // Get the category and the type
          PluginMetadata pluginMetadata = pluginsManager.getPluginMetadataByUUID(uuid);
          domain = exportDataMapper.addApplicationToDomain(domain, pluginMetadata.getCategory(),
              pluginMetadata.getType(), application.getName(), application.getDescription(), rolesMapping);
        }
      }
      domains.getDomainElement().add(domain);
    }

    return domains;
  }

  private Memberships exportMemberships(final String pProjectId) throws ProjectServiceException
  {
    LOG.info(String.format("add membership project for projectID=%s", pProjectId));
    Memberships memberships = new Memberships();

    List<MembershipInfo> userMemberships = membershipPresenter.getAllUserMemberships(pProjectId, false);
    MembershipsUsers membershipsUsers = new MembershipsUsers();
    if (userMemberships.size() > 0)
    {
      for (MembershipInfo membership : userMemberships)
      {
        User user = (User) membership.getActor();
        LOG.info(String.format("add user membership with login=%s and role=%s", user.getLogin(), membership
            .getRole().getName()));
        MembershipUser membershipUser = exportDataMapper.toMembershipUser(membership);
        membershipsUsers.getMembershipUser().add(membershipUser);
      }

      memberships.setMembershipsUsers(membershipsUsers);
    }

    List<MembershipInfo> groupMemberships = membershipPresenter.getAllGroupMemberships(pProjectId);
    MembershipsGroups membershipsGroups = new MembershipsGroups();
    if (groupMemberships.size() > 0)
    {
      for (MembershipInfo membership : groupMemberships)
      {
        Group group = (Group) membership.getActor();
        LOG.info((String.format("add group membership with login=%s and role=%s", group.getName(), membership
            .getRole().getName())));
        MembershipGroup membershipGroup = exportDataMapper.toMembershipGroup(membership);
        membershipsGroups.getMembershipGroup().add(membershipGroup);
      }

      memberships.setMembershipsGroups(membershipsGroups);
    }

    return memberships;
  }

  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  public void setApplicationPresenter(final ApplicationPresenter pApplicationPresenter)
  {
    applicationPresenter = pApplicationPresenter;
  }

  public void setSpacePresenter(final SpacePresenter pSpacePresenter)
  {
    spacePresenter = pSpacePresenter;
  }

  public void setGroupPresenter(final GroupPresenter pGroupPresenter)
  {
    groupPresenter = pGroupPresenter;
  }

  public void setProjectRolePresenter(final ProjectRolePresenter pProjectRolePresenter)
  {
    projectRolePresenter = pProjectRolePresenter;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  public void setExportDataMapper(final ExportDataMapper pExportDataMapper)
  {
    exportDataMapper = pExportDataMapper;
  }

  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  public void setMembershipPresenter(final MembershipPresenter pMembershipPresenter)
  {
    membershipPresenter = pMembershipPresenter;
  }

  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  public void setHistorizationService(final HistorizationService pHistorizationService)
  {
    historizationService = pHistorizationService;
  }
}
