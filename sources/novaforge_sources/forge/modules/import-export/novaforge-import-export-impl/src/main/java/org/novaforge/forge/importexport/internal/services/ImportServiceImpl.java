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
package org.novaforge.forge.importexport.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.importexport.datas.model.Applications.ApplicationElement;
import org.novaforge.forge.importexport.datas.model.Domains.DomainElement;
import org.novaforge.forge.importexport.datas.model.Forge;
import org.novaforge.forge.importexport.datas.model.Groups;
import org.novaforge.forge.importexport.datas.model.Groups.GroupElement;
import org.novaforge.forge.importexport.datas.model.Memberships;
import org.novaforge.forge.importexport.datas.model.MembershipsGroups.MembershipGroup;
import org.novaforge.forge.importexport.datas.model.MembershipsUsers.MembershipUser;
import org.novaforge.forge.importexport.datas.model.Projects.ProjectElement;
import org.novaforge.forge.importexport.datas.model.Roles.RoleElement;
import org.novaforge.forge.importexport.datas.model.Status;
import org.novaforge.forge.importexport.datas.model.Users;
import org.novaforge.forge.importexport.datas.model.Users.UserElement;
import org.novaforge.forge.importexport.exceptions.ImportExportServiceException;
import org.novaforge.forge.importexport.handlers.ImportHandler;
import org.novaforge.forge.importexport.services.ImportService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class ImportServiceImpl implements ImportService
{
  private static final Log LOG = LogFactory.getLog(ImportServiceImpl.class);
  private ForgeConfigurationService forgeConfigurationService;
  private AuthentificationService   authentificationService;
  private HistorizationService      historizationService;
  private ImportHandler             importHandler;
  private UserPresenter             userPresenter;
  private Forge                     forge;

  /**
   * {@inheritDoc}
   */
  @Override
  public void importDatas(final String pImportFilePath) throws ImportExportServiceException
  {
    LOG.info("Starting import process ...");
    final File importXML = new File(pImportFilePath);

    // Read import xml file
    readXML(importXML);

    // Disactivate historization
    historizationService.setActivatedMode(false);

    login();

    // Handle forge data
    handleForge();

    // Handle project data
    handleProjects();

    // Logout
    authentificationService.logout();

    // Activate historization
    historizationService.setActivatedMode(true);

    // Write xml report
    writeXML(importXML);
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

  /**
   * This method will read the xml file and unmarshall it into {@link ImportServiceImpl#forgeCore}
   * 
   * @throws ImportExportServiceException
   */
  private void readXML(final File pImportXML) throws ImportExportServiceException
  {
    LOG.info("Reading import data ...");
    try
    {
      // Build JAXB component
      final JAXBContext jaxbContext = JAXBContext.newInstance(Forge.class);
      final Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

      if (pImportXML.exists())
      {
        final InputStream is = new FileInputStream(pImportXML);
        forge = (Forge) unMarshaller.unmarshal(is);
      }
      else
      {
        throw new FileNotFoundException(String.format("Unable to read the configuration file [file=%s]",
            pImportXML.getPath()));
      }
    }
    catch (final JAXBException e)
    {
      LOG.error("Reading import data ON ERROR", e);
      throw new ImportExportServiceException(String.format("Unable to read the import xml file [file=%s]",
          pImportXML.getPath()), e);
    }
    catch (final FileNotFoundException e)
    {
      LOG.error("Reading import data ON ERROR", e);
      throw new ImportExportServiceException(String.format("Unable to read the configuration file [file=%s]",
          pImportXML.getPath()), e);
    }
    LOG.info("Reading import data SUCCESSFUL");
  }

  /**
   * This method will read the xml file and unmarshall it into {@link ImportServiceImpl#forgeCore}
   * 
   * @throws ImportExportServiceException
   */
  private void writeXML(final File pImportXML) throws ImportExportServiceException
  {
    LOG.info("Writing import data report ...");
    try
    {
      // Build JAXB component
      final JAXBContext jaxbContext = JAXBContext.newInstance(Forge.class);
      final Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.marshal(forge, pImportXML);
    }
    catch (final JAXBException e)
    {
      LOG.error("Writing import data report ON ERROR", e);
      throw new ImportExportServiceException(String.format("Unable to read the configuration file [file=%s]",
          pImportXML.getPath()), e);
    }
    LOG.info("Writing import data report SUCCESSFUL");
  }

  /**
	 */
  private void handleForge()
  {
    if (forge != null)
    {
      LOG.info("Import forge data in progress ...");
      final Users users = forge.getUsers();
      if ((users != null) && (users.getUserElement() != null))
      {
        LOG.info("Import users in progress ...");
        for (final UserElement user : users.getUserElement())
        {
          LOG.info(String.format("Import user : %s", user.getLogin()));
          if (!Status.DONE.equals(user.getStatus()))
          {
            final boolean importUser = importHandler.importUser(user);
            user.setStatus(getStatus(importUser));
            LOG.info(String.format("Import user finish with status: %s", user.getStatus()));
          }
          else
          {
            LOG.info("Import user already done.");
          }
        }
      }

      // Import groups of the forge project
      handleGroups(forge.getGroups(), forgeConfigurationService.getForgeProjectId());
    }
  }

  private void handleGroups(final Groups pGroups, final String pProjectId)
  {
    if ((pGroups != null) && (pGroups.getGroupElement() != null))
    {
      LOG.info(String.format("Import groups in progress for [project=%s]", pProjectId));
      for (final GroupElement group : pGroups.getGroupElement())
      {
        LOG.info(String.format("Import group : %s", group.getId()));
        if (!Status.DONE.equals(group.getStatus()))
        {
          final boolean importGroup = importHandler.importGroup(pProjectId, group);
          group.setStatus(getStatus(importGroup));
          LOG.info(String.format("Import group finish with status: %s", group.getStatus()));
        }
        else
        {
          LOG.info("Import group already done.");
        }
      }
    }
  }

  /**
	 */
  private void handleProjects()
  {
    if ((forge != null) && (forge.getProjects() != null))
    {
      LOG.info("Import projects data in progress ...");
      final List<ProjectElement> projects = forge.getProjects().getProjectElement();
      if (projects != null)
      {
        for (final ProjectElement project : projects)
        {
          boolean isSuccess = true;
          final String projectId = project.getId();
          LOG.info(String.format("Import project : %s", projectId));
          if (!Status.DONE.equals(project.getStatus()))
          {
            // Import project information
            if ((project.getProjectInfo() != null) && (!Status.DONE.equals(project.getProjectInfo().getStatus())))
            {
              LOG.info(String.format("Import project information: %s", projectId));
              isSuccess = importHandler.importProjectInfo(projectId, project.getProjectInfo());
              project.getProjectInfo().setStatus(getStatus(isSuccess));
              LOG.info(String.format("Import project information finish with status: %s", project
                  .getProjectInfo().getStatus()));
            }
            else
            {
              LOG.info("Import project information already done.");
            }

            // Import project roles
            if ((isSuccess) && (project.getRoles() != null))
            {
              final List<RoleElement> roles = project.getRoles().getRoleElement();
              if (roles != null)
              {
                for (final RoleElement role : roles)
                {
                  if (!Status.DONE.equals(role.getStatus()))
                  {
                    LOG.info(String.format("Import role : %s", role.getName()));
                    // we need to temporize the role creation for the following tasks
                    try
                    {
                      Thread.sleep(5000);
                    }
                    catch (final InterruptedException e)
                    {
                      LOG.error("unable to sleep current thread", e);
                    }
                    final boolean importStatus = importHandler.importRole(projectId, role);
                    role.setStatus(getStatus(importStatus));
                    if (isSuccess)
                    {
                      isSuccess = importStatus;
                    }
                    LOG.info(String.format("Import role finish with status: %s", role.getStatus()));
                  }
                  else
                  {
                    LOG.info("Import role already done.");
                  }
                }
              }
            }

            // Import Groups
            handleGroups(project.getGroups(), projectId);

            // Import Memberships
            handleMemberships(projectId, project.getMemberships());

            // Import project domains and apps
            if ((isSuccess) && (project.getDomains() != null))
            {
              final List<DomainElement> domains = project.getDomains().getDomainElement();
              if (domains != null)
              {
                for (final DomainElement domain : domains)
                {
                  if (!Status.DONE.equals(domain.getStatus()))
                  {
                    LOG.info(String.format("Import domain : %s", domain.getName()));
                    final boolean statusDomain = importHandler.importDomain(projectId, domain);
                    if ((statusDomain) && (domain.getApplications() != null))
                    {
                      final List<ApplicationElement> apps = domain.getApplications().getApplicationElement();
                      if (apps != null)
                      {
                        for (final ApplicationElement app : apps)
                        {
                          if (!Status.DONE.equals(app.getStatus()))
                          {
                            LOG.info(String.format("Import app : %s", app.getName()));
                            final boolean importStatusApp = importHandler.importApp(projectId,
                                domain.getName(), app);
                            app.setStatus(getStatus(importStatusApp));
                            if (isSuccess)
                            {
                              isSuccess = importStatusApp;
                            }
                            LOG.info(String.format("Import app finish with status: %s", domain.getStatus()));
                          }
                          else
                          {
                            LOG.info("Import app already done.");
                          }
                        }
                      }
                    }
                    if (isSuccess)
                    {
                      isSuccess = statusDomain;
                    }
                    domain.setStatus(getStatus(isSuccess));
                    LOG.info(String.format("Import domain finish with status: %s", domain.getStatus()));

                  }
                  else
                  {
                    LOG.info("Import domain already done.");
                  }
                }
              }
            }
            project.setStatus(getStatus(isSuccess));
            LOG.info(String.format("Import project finish with status: %s", project.getStatus()));
          }
          else
          {
            LOG.info("Import project already done.");
          }
        }
      }
    }
  }

  /**
	 */
  private void handleMemberships(final String pProjectId, final Memberships pMemeberships)
  {
    if (pMemeberships != null)
    {
      LOG.info(String.format("Import memberships for project : %s", pProjectId));
      if ((pMemeberships.getMembershipsUsers() != null)
          && (pMemeberships.getMembershipsUsers().getMembershipUser() != null))
      {
        for (final MembershipUser membershipUser : pMemeberships.getMembershipsUsers().getMembershipUser())
        {
          LOG.info(String.format("Import user membership : %s", membershipUser.getLogin()));

          if (!Status.DONE.equals(membershipUser.getStatus()))
          {
            final boolean importUser = importHandler.importUserMembership(pProjectId, membershipUser);
            membershipUser.setStatus(getStatus(importUser));
            LOG.info(String.format("Import user finish with status: %s", membershipUser.getStatus()));
          }
          else
          {
            LOG.info("Import user membership already done.");
          }
        }
      }
      if ((pMemeberships.getMembershipsGroups() != null)
          && (pMemeberships.getMembershipsGroups().getMembershipGroup() != null))
      {

        for (final MembershipGroup membershipGroup : pMemeberships.getMembershipsGroups()
            .getMembershipGroup())
        {
          LOG.info(String.format("Import group membership : %s", membershipGroup.getId()));
          if (!Status.DONE.equals(membershipGroup.getStatus()))
          {
            final boolean importGroup = importHandler.importGroupMembership(pProjectId, membershipGroup);
            membershipGroup.setStatus(getStatus(importGroup));
            LOG.info(String.format("Import group membership finish with status: %s",
                membershipGroup.getStatus()));
          }
          else
          {
            LOG.info("Import group membership already done.");
          }
        }
      }
    }
  }

  /**
   * It will return the status according to the parameter
   * 
   * @param pOnSuccess
   *          the parameter used to find the status
   * @return the {@link Status} found
   */
  private Status getStatus(final boolean pOnSuccess)
  {
    Status returnStatus = Status.DONE;
    if (!pOnSuccess)
    {
      returnStatus = Status.ON_ERROR;
    }
    return returnStatus;

  }

  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  public void setHistorizationService(final HistorizationService pHistorizationService)
  {
    historizationService = pHistorizationService;
  }

  public void setImportHandler(final ImportHandler pImportHandler)
  {
    importHandler = pImportHandler;
  }

  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }
}
