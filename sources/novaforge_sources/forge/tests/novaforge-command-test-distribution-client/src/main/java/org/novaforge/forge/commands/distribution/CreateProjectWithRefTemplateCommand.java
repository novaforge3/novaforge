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
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.model.Organization;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.services.OrganizationService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import java.util.Random;

/**
 * This command (launched on LOCAL/ZONAL or CENTRAL forge) will create a project with template for
 * Distribution itests.
 * 
 * @author Marc Blachon
 */
@Command(scope = "distribution", name = "create-project-template-ref",
    description = "Create and validate a project with template ID: template_itests_project")
public class CreateProjectWithRefTemplateCommand extends OsgiCommandSupport
{

  private static final Log           log                              = LogFactory
                                                                          .getLog(CreateProjectWithRefTemplateCommand.class);
  private static final String        TEMPLATE_DISTRIBUTION_ITESTS_ID  = "template_itests_project";
  private static final String        PROJECT_DISTRIBUTION_ITESTS_ID   = "distributionproject";
  private static final String        PROJECT_DISTRIBUTION_ITESTS_NAME = "distribution itests project";
  private static final String        PROJECT_DISTRIBUTION_ITESTS_DESC = "distribution itests project description";
  private ForgeIdentificationService forgeIdentificationService;
  private AuthentificationService    authentificationService;
  private UserService                userService;
  private ForgeConfigurationService  forgeConfigurationService;
  private ProjectService             projectService;
  private OrganizationService        organizationService;
  private String                     projectId;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    try
    {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userService.getUser(superAdministratorLogin);
      authentificationService.login(superAdministratorLogin, user.getPassword());

      forgeIdentificationService.getForgeId().toString();

      createValidateProject();

      System.out.println("project with ID: " + projectId + " has been created with template ID: "
          + TEMPLATE_DISTRIBUTION_ITESTS_ID);

    }
    finally
    {
      authentificationService.logout();
    }

    return null;
  }

  /*
   * creating project and validating it. Throws exception if not created or not validated.
   */
  private void createValidateProject() throws Exception
  {
    projectId = generateProjectID();
    Project project = projectService.newProject();
    project.setAuthor("admin1");
    project.setElementId(projectId);
    project.setName(projectId);
    project.setDescription(PROJECT_DISTRIBUTION_ITESTS_DESC);
    project.setLicenceType("GPL");
    // org to be used for distitribution indicators
    Organization org = organizationService.newOrganization();
    org.setName("MyOrg1");
    project.setOrganization(org);

    projectService.createProject(project, "admin1", TEMPLATE_DISTRIBUTION_ITESTS_ID);
    projectService.validateProject(projectId);

    ProjectOptions projectOptions = projectService.newProjectOptions();
    projectOptions.setRetrievedImage(false);
    projectOptions.setRetrievedOrganization(false);
    projectOptions.setRetrievedSystem(false);

    Project gotProject = projectService.getProject(projectId, projectOptions);
    if (gotProject != null)
    {
      ProjectStatus projectStatus = gotProject.getStatus();
      if (!ProjectStatus.VALIDATED.equals(projectStatus))
      {
        throw new Exception("ERROR: the project staus with ID " + projectId + " is not created.");
      }
    }
    else
    {
      throw new Exception("Error: the project with ID: " + projectId + " is null");
    }
  }

  private String generateProjectID()
  {
    String projectId;
    Random randomGenerator = new Random();
    int randomInt = randomGenerator.nextInt(100);
    projectId = PROJECT_DISTRIBUTION_ITESTS_ID + randomInt;
    return projectId;
  }

  // ----------------- setter used by the container to inject services -------------------------------

  public void setForgeIdentificationService(final ForgeIdentificationService pForgeIdentificationService)
  {
    forgeIdentificationService = pForgeIdentificationService;
  }

  public void setAuthentificationService(final AuthentificationService authentificationService)
  {
    this.authentificationService = authentificationService;
  }

  public void setUserService(UserService userService)
  {
    this.userService = userService;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }

  public void setProjectService(ProjectService projectService)
  {
    this.projectService = projectService;
  }

  public void setOrganizationService(OrganizationService organizationService)
  {
    this.organizationService = organizationService;
  }

}
