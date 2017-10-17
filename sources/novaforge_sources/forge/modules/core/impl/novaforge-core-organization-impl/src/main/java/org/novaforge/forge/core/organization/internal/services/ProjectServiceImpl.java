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
package org.novaforge.forge.core.organization.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.jms.MessageService;
import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.commons.technical.validation.ValidationService;
import org.novaforge.forge.commons.technical.validation.ValidatorResponse;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.organization.dao.GroupDAO;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.dao.ProjectElementDAO;
import org.novaforge.forge.core.organization.dao.RoleDAO;
import org.novaforge.forge.core.organization.dao.TemplateDAO;
import org.novaforge.forge.core.organization.dao.UserDAO;
import org.novaforge.forge.core.organization.delegates.MailDelegate;
import org.novaforge.forge.core.organization.delegates.MessageDelegate;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.MailDelegateException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.handlers.SysApplicationHandler;
import org.novaforge.forge.core.organization.internal.model.ProjectInfoImpl;
import org.novaforge.forge.core.organization.internal.model.ProjectOptionsImpl;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectInfo;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateInstance;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.route.OrganizationQueueHeader;
import org.novaforge.forge.core.organization.route.OrganizationQueueName;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.novaforge.forge.core.organization.services.TemplateInitializationHandler;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.core.security.authorization.PermissionHandler;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is an implementation of {@link ProjectService}
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see ProjectService
 */
public class ProjectServiceImpl implements ProjectService
{
  private static final Log LOG = LogFactory.getLog(ProjectServiceImpl.class);
  /**
   * Reference to {@link PermissionHandler} service injected by the container
   */
  private PermissionHandler             permissionHandler;
  /**
   * Reference to {@link ProjectDAO} service injected by the container
   */
  private ProjectDAO                    projectDAO;
  /**
   * Reference to {@link ProjectElementDAO} service injected by the container
   */
  private ProjectElementDAO             projectElementDAO;
  /**
   * Reference to {@link TemplateDAO} service injected by the container
   */
  private TemplateDAO                   templateDAO;
  /**
   * Reference to {@link RoleDAO} service injected by the container
   */
  private RoleDAO                       roleDAO;
  /**
   * Reference to {@link UserDAO} service injected by the container
   */
  private UserDAO                       userDAO;
  /**
   * Reference to {@link ApplicationService} service injected by the container
   */
  private ApplicationService            applicationService;
  /**
   * Reference to {@link TemplateInitializationHandler} service injected by the container
   */
  private TemplateInitializationHandler templateInitializationHandler;
  /**
   * Reference to {@link MailDelegate} service injected by the container
   */
  private MailDelegate                  mailDelegate;
  /**
   * Reference to {@link ValidationService} service injected by the container
   */
  private ValidationService             validationService;
  /**
   * Reference to {@link MessageDelegate} service injected by the container
   */
  private MessageDelegate               messageDelegate;
  /**
   * Reference to {@link ForgeCfgService} service injected by the container
   */
  private ForgeConfigurationService     forgeConfigurationService;
  /**
   * Reference to {@link MembershipDAO} service injected by the container
   */
  private MembershipDAO                 membershipDAO;
  /**
   * Reference to {@link GroupDAO} service injected by the container
   */
  private GroupDAO                      groupDAO;
  /**
   * Reference to {@link MessageService} service injected by the container
   */
  private MessageService                messageService;

  /**
   * Reference to {@link SysApplicationHandler} service injected by the container
   */
  private SysApplicationHandler         sysApplicationHandler;

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectOptions newProjectOptions()
  {
    return new ProjectOptionsImpl();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project newProject()
  {
    return projectDAO.newProject();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createSystemProject(final Project project, final String pAuthor) throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input - project : " + project.toString());
    }

    try
    {
      checkProject("", project, pAuthor);
      createProject(project, pAuthor, RealmType.SYSTEM, null);
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createProject(final Project pProject, final String pAuthor, final String pTemplateId)
      throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input - project : " + pProject.toString());
      LOG.debug("Input - author : " + pAuthor);
    }
    checkProject("", pProject, pAuthor);
    final User owner = createProject(pProject, pAuthor, RealmType.USER, pTemplateId);
    try
    {
      final Locale userLocale = owner.getLanguage().getLocale();
      mailDelegate.sendProjectCreationEmails(pProject, owner.getEmail(), userLocale);
    }
    catch (final MailDelegateException e)
    {
      throw new ProjectServiceException(String.format(
          "unable to send email for project creation to user with login=%s", pAuthor), e);
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> getAllProjects(final ProjectOptions pProjectOptions) throws ProjectServiceException
  {
    try
    {
      return getAllProjectsByStatus(pProjectOptions, ProjectStatus.VALIDATED);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws ProjectServiceException
   */
  @Override
  public List<Project> getValidatedProjects(final String pLogin, final ProjectOptions pProjectOptions)
      throws ProjectServiceException
  {
    final List<Project> projects = new ArrayList<Project>();
    try
    {
      final Set<Project> projectSet = new HashSet<Project>();
      final User user = userDAO.findByLogin(pLogin);
      final UUID userUUID = user.getUuid();
      projectSet.addAll(membershipDAO.findValidatedProjectsForActors(pProjectOptions, userUUID));
      final List<UUID> groups = groupDAO.findGroupsUUIDForUser(userUUID);
      projectSet.addAll(membershipDAO.findValidatedProjectsForActors(pProjectOptions,
          groups.toArray(new UUID[groups.size()])));
      projects.addAll(projectSet);
      return projects;
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws ProjectServiceException
   */
  @Override
  public List<Project> getPublicProjects(final String pLogin, final ProjectOptions pProjectOptions)
      throws ProjectServiceException
  {
    try
    {
      // Use a set to filter duplicated projects
      final Set<Project> projects = new HashSet<Project>();

      final User user = userDAO.findByLogin(pLogin);
      final UUID userUUID = user.getUuid();

      // Get public projects for user
      final List<Project> userProjects = membershipDAO.findPublicProjectsForActors(pProjectOptions, userUUID);
      projects.addAll(userProjects);

      // Get public projects for user
      final List<UUID> groups = groupDAO.findGroupsUUIDForUser(userUUID);
      final List<Project> groupProjects = membershipDAO.findPublicProjectsForActors(pProjectOptions,
          groups.toArray(new UUID[groups.size()]));
      if ((groupProjects != null) && (!groupProjects.isEmpty()))
      {
        // Get the intersection between group and direct membership
        projects.retainAll(groupProjects);
      }

      return new ArrayList<Project>(projects);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws ProjectServiceException
   */
  @Override
  public Set<String> getProjectsId(final String pLogin, final ProjectOptions pProjectOptions)
      throws ProjectServiceException
  {
    final Set<String> projects = new HashSet<String>();
    try
    {
      final User user = userDAO.findByLogin(pLogin);
      final UUID userUUID = user.getUuid();
      projects.addAll(membershipDAO.findProjectsIdForActors(userUUID));
      final List<UUID> groups = groupDAO.findGroupsUUIDForUser(userUUID);
      projects.addAll(membershipDAO.findProjectsIdForActors(groups.toArray(new UUID[groups.size()])));
      return projects;
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project getProject(final String pProjectId, final ProjectOptions pProjectOptions)
      throws ProjectServiceException
  {
    try
    {
      return projectDAO.findById(pProjectId, pProjectOptions);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectInfo getProjectInfo(final String projectId) throws ProjectServiceException
  {
    try
    {
      final Project project = projectDAO.findByProjectId(projectId);
      return new ProjectInfoImpl(project.getDescription(), project.getLicenceType(),
                                                          project.getName(), project.getProjectId(),
                                                          project.getLastModified());
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateProject(final String pOldName, final Project pProject, final String pAuthor)
      throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input - project : " + pProject.toString());
    }

    try
    {
      checkProject(pOldName, pProject, pAuthor);
      projectDAO.update(pProject);

      // propagate the modification for all applications
      final List<ProjectApplication> applications = applicationService.getAllProjectApplications(pProject
          .getProjectId());
      for (final ProjectApplication application : applications)
      {
        if (ApplicationStatus.ACTIVE.equals(application.getStatus()))
        {
          // send the message
          messageDelegate.sendProjectMessage(application.getPluginUUID().toString(), application
              .getPluginInstanceUUID().toString(), application.getName(), pProject.getProjectId(), pProject,
              null, PluginQueueAction.UPDATE.getLabel(), pAuthor, false);
        }
      }
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteProject(final String pProjectId, final String pAuthor) throws ProjectServiceException
  {
    try
    {
      // Change status to delete in progress
      changeProjectStatus(ProjectStatus.DELETE_IN_PROGRESS, pProjectId);

      final Map<String, String> headers = new HashMap<String, String>();
      headers.put(OrganizationQueueHeader.PROJECT_ID_HEADER, pProjectId);
      headers.put(OrganizationQueueHeader.USER_NAME_HEADER, pAuthor);
      // send the message
      messageService.sendMessage(OrganizationQueueName.PROJECT_DELETE_ROUTE_NAME, pProjectId, headers);
    }
    catch (final MessageServiceException e)
    {
      throw new ProjectServiceException(String.format("Unable to send delete project message for [project_id=%s]",
                                                      pProjectId), e);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void validateProject(final String pProjectId) throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input - projectId : " + pProjectId);
    }

    // instantiate the projet using an template if one is existing
    boolean instantiateError = false;
    Project project = null;
    try
    {
      // Change the effective status
      changeProjectStatus(ProjectStatus.VALIDATED, pProjectId);
      templateInitializationHandler.instantiateProjectFromTemplate(pProjectId);

      project = projectDAO.findByProjectId(pProjectId);
      final User owner = userDAO.findByLogin(project.getAuthor());
      final Locale userLocale = owner.getLanguage().getLocale();

      // Handle system apps
      handleSysApps(project);

      if (!instantiateError)
      {
        mailDelegate.sendProjectValidationEmail(project, owner.getEmail(), userLocale);
      }
      else
      {
        mailDelegate.sendTemplateInitializationErrorEmail(project, owner.getEmail(), userLocale);
      }
    }
    catch (final TemplateServiceException e1)
    {
      instantiateError = true;
      LOG.error(String.format(
          "Unable to instantiate the projet using the declared template with [project_id=%s]", pProjectId),
          e1);
    }
    catch (final NoResultException e)
    {
      throw new ProjectServiceException(String.format("Unable to find user with [login=%s]",
          project.getAuthor()), e);
    }
    catch (final MailDelegateException e)
    {
      throw new ProjectServiceException(String.format(
          "unable to send email for project validation to user with login=%s", project.getAuthor()), e);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  private void handleSysApps(final Project pProject)
  {
    // execute the system applications management on a new thread because it can takes more than transaction
    // timeout and it doesn't need to be rolledback if errors occurs
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          LOG.info("Handle System applications is starting....");
          sysApplicationHandler.handleSysApplications(pProject);
        }
        catch (final Exception e)
        {
          LOG.error(
              String.format("Unable to handle system applications with [project_id=%s]",
                  pProject.getProjectId()), e);
        }
      }
    });
    executor.shutdown();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rejectProject(final String pProjectId, final String pReason) throws ProjectServiceException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Input - projectId : " + pProjectId);
    }

    Project project = null;
    try
    {
      changeProjectStatus(ProjectStatus.REJECTED, pProjectId);
      project = projectDAO.findByProjectId(pProjectId);
      final User owner = userDAO.findByLogin(project.getAuthor());
      final Locale userLocale = owner.getLanguage().getLocale();
      mailDelegate.sendProjectRejectEmail(project, owner.getEmail(), pReason, userLocale);
    }
    catch (final NoResultException e)
    {
      throw new ProjectServiceException(String.format("Unable to find user with login=%s",
          project.getAuthor()), e);
    }
    catch (final MailDelegateException e)
    {
      throw new ProjectServiceException(String.format(
          "unable to send email for rejecting project to user with login=%s", project.getAuthor()), e);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> getAllProjectsByStatus(final ProjectOptions pProjectOptions,
                                              final ProjectStatus... pProjectStatus) throws ProjectServiceException
  {
    try
    {
      return projectDAO.findAllByStatus(pProjectOptions, pProjectStatus);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isForgeProject(final String pProjectId)
  {
    boolean      isForgeProject = false;
    final String forgeProjectId = forgeConfigurationService.getForgeProjectId();
    if (pProjectId != null)
    {
      isForgeProject = pProjectId.equals(forgeProjectId);
    }
    return isForgeProject;
  }

  private void changeProjectStatus(final ProjectStatus pNewStatus, final String pProjectId)
      throws ProjectServiceException
  {
    final Project project = projectDAO.findByProjectId(pProjectId);
    project.setStatus(pNewStatus);
    projectDAO.update(project);
  }

  private void checkProject(final String pOldName, final Project pProject, final String pAuthor)
      throws ProjectServiceException
  {
    final String projectId = pProject.getProjectId();

    // validate the bean
    final ValidatorResponse response = validationService.validate(Project.class, pProject);
    if (!response.isValid())
    {
      LOG.error(ExceptionCode.ERR_VALIDATION_BEAN.toString() + " : " + response.getMessage());
      throw new ProjectServiceException(ExceptionCode.ERR_VALIDATION_BEAN, response.getMessage());
    }

    // check another project with the same project id doesn't already exist
    if (("".equals(pOldName)) && (projectElementDAO.existId(projectId)))
    {
      LOG.error(ExceptionCode.ERR_CREATE_PROJECT_PROJECTID_ALREADY_EXIST.toString());
      throw new ProjectServiceException(ExceptionCode.ERR_CREATE_PROJECT_PROJECTID_ALREADY_EXIST,
                                        String.format("projectId=%s", projectId));
    }

    // check another project with the same project name doesn't already
    // exist
    if ((!pOldName.equals(pProject.getName())) && (projectElementDAO.existName(pProject.getName())))
    {
      LOG.error(ExceptionCode.ERR_CREATE_PROJECT_PROJECT_NAME_ALREADY_EXIST.toString());
      throw new ProjectServiceException(ExceptionCode.ERR_CREATE_PROJECT_PROJECT_NAME_ALREADY_EXIST,
                                        String.format("projectId=%s", projectId));
    }

    // check the author username is existing
    if (!userDAO.existLogin(pAuthor))
    {
      LOG.error(ExceptionCode.ERR_CREATE_PROJ_AUTHOR_NOT_EXIST.toString());
      throw new ProjectServiceException(ExceptionCode.ERR_CREATE_PROJ_AUTHOR_NOT_EXIST, String.format("projectId=%s",
                                                                                                      projectId));
    }
  }

  private User createProject(final Project project, final String pAuthor, final RealmType pType,
                             final String pTemplateId) throws ProjectServiceException
  {
    final Set<String> permissions = permissionHandler.buildProjectPermissions(project.getProjectId(),
                                                                              PermissionAction.values());

    if (project.getImage().getFile() == null)
    {
      final byte[] defaultIcon = forgeConfigurationService.getDefaultIcon();
      if (defaultIcon != null)
      {
        project.getImage().setFile(defaultIcon);
        project.getImage().setMimeType("image/png");
        project.getImage().setName("defaultIcon");
      }
    }
    final String projectId = project.getProjectId();

    // Set the author of the project : it's by priority the author's project attribute and next the pAuthor
    // argument accordint to the origin
    // of the project's creation
    if (project.getAuthor() == null)
    {
      project.setAuthor(pAuthor);
    }

    // set the specified realm type
    project.setRealmType(pType);

    // create a set of permission for project
    for (final String name : permissions)
    {
      final Permission perm = roleDAO.newPermission(name);
      roleDAO.persist(perm);
    }

    // create the administrator role
    final Set<String> permAdmin = permissionHandler.buildProjectPermissions(project.getProjectId(), Project.class,
                                                                            PermissionAction.ALL);
    final String      administratorRoleName = forgeConfigurationService.getForgeAdministratorRoleName();
    final ProjectRole administrator         = roleDAO.newProjectRole();
    administrator.addPermission(roleDAO.findByName(permAdmin.iterator().next()));

    administrator.setName(administratorRoleName);
    administrator.setRealmType(RealmType.SYSTEM);
    administrator.setOrder(1);
    project.addRole(administrator);

    final Project projectPersistent = projectDAO.persist(project);

    // Create the template instance is required
    if ((pTemplateId != null) && (!"".equals(pTemplateId)))
    {
      final Template template = templateDAO.findByTemplateId(pTemplateId);
      final TemplateInstance instance = templateDAO.newTemplateInstance();
      instance.setProject(projectPersistent);
      instance.setTemplate(template);
      templateDAO.persist(instance);

    }
    // create a membership for the author with an administrator role
    final User user = userDAO.findByLogin(pAuthor);
    final ProjectRole rolePersistent = (ProjectRole) roleDAO.findByNameAndElement(projectId, administratorRoleName);

    final Membership membership = membershipDAO.newMembership(user, projectPersistent, rolePersistent);
    membershipDAO.persist(membership);

    // create a membership for the super-administrator with an administrator role
    final String superadmin = forgeConfigurationService.getSuperAdministratorLogin();
    if (!superadmin.equals(pAuthor))
    {
      final User admin = userDAO.findByLogin(superadmin);
      final Membership membership2 = membershipDAO.newMembership(admin, projectPersistent, rolePersistent);
      membershipDAO.persist(membership2);
    }
    return user;
  }

  /**
   * Use by container to inject {@link PermissionHandler} implementation
   * 
   * @param pPermissionHandler
   *          the authorizationHandler to set
   */
  public void setPermissionHandler(final PermissionHandler pPermissionHandler)
  {
    permissionHandler = pPermissionHandler;
  }

  /**
   * Use by container to inject {@link ProjectDAO} implementation
   * 
   * @param pProjectDAO
   *          the projectDAO to set
   */
  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }

  /**
   * Use by container to inject {@link ProjectElementDAO} implementation
   * 
   * @param pProjectElementDAO
   *          the projectElementDAO to set
   */
  public void setProjectElementDAO(final ProjectElementDAO pProjectElementDAO)
  {
    projectElementDAO = pProjectElementDAO;
  }

  /**
   * Use by container to inject {@link TemplateDAO} implementation
   * 
   * @param pTemplateDAO
   *          the templateDAO to set
   */
  public void setTemplateDAO(final TemplateDAO pTemplateDAO)
  {
    templateDAO = pTemplateDAO;
  }

  /**
   * Use by container to inject {@link RoleDAO} implementation
   * 
   * @param pRoleDAO
   *          the roleDAO to set
   */
  public void setRoleDAO(final RoleDAO pRoleDAO)
  {
    roleDAO = pRoleDAO;
  }

  /**
   * Use by container to inject {@link UserDAO} implementation
   * 
   * @param pUserDAO
   *          the userDAO to set
   */
  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
  }

  /**
   * Use by container to inject {@link ApplicationService} implementation
   * 
   * @param pApplicationService
   *          the applicationService to set
   */
  public void setApplicationService(final ApplicationService pApplicationService)
  {
    applicationService = pApplicationService;
  }

  /**
   * Use by container to inject {@link TemplateInitializationHandler} implementation
   * 
   * @param pTemplateInitializationHandler
   *          the templateInitializationService to set
   */
  public void setTemplateInitializationHandler(
      final TemplateInitializationHandler pTemplateInitializationHandler)
  {
    templateInitializationHandler = pTemplateInitializationHandler;
  }

  /**
   * Use by container to inject {@link MailDelegate} implementation
   * 
   * @param pMailDelegate
   *          the mailDelegate to set
   */
  public void setMailDelegate(final MailDelegate pMailDelegate)
  {
    mailDelegate = pMailDelegate;
  }

  /**
   * Use by container to inject {@link ValidationService} implementation
   * 
   * @param pValidationService
   *          the validationService to set
   */
  public void setValidationService(final ValidationService pValidationService)
  {
    validationService = pValidationService;
  }

  /**
   * Use by container to inject {@link MessageDelegate} implementation
   * 
   * @param pMessageDelegate
   *          the messageDelegate to set
   */
  public void setMessageDelegate(final MessageDelegate pMessageDelegate)
  {
    messageDelegate = pMessageDelegate;
  }

  /**
   * Use by container to inject {@link ForgeCfgService} implementation
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * Use by container to inject {@link MembershipDAO} implementation
   * 
   * @param pMembershipDAO
   *          the membershipDAO to set
   */
  public void setMembershipDAO(final MembershipDAO pMembershipDAO)
  {
    membershipDAO = pMembershipDAO;
  }

  /**
   * Use by container to inject {@link GroupDAO} implementation
   * 
   * @param pGroupDAO
   *          the groupDAO to set
   */
  public void setGroupDAO(final GroupDAO pGroupDAO)
  {
    groupDAO = pGroupDAO;
  }

  /**
   * Use by container to inject {@link MessageService} implementation
   * 
   * @param pMessageService
   *          the messageService to set
   */
  public void setMessageService(final MessageService pMessageService)
  {
    messageService = pMessageService;
  }

  /**
   * Use by container to inject {@link SysApplicationHandler} implementation
   * 
   * @param pSysApplicationHandler
   *          the sysApplicationHandler to set
   */
  public void setSysApplicationHandler(final SysApplicationHandler pSysApplicationHandler)
  {
    sysApplicationHandler = pSysApplicationHandler;
  }

}
