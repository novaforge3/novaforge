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
package org.novaforge.forge.plugins.delivery.it.test.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.novaforge.forge.importexport.datas.model.Applications.ApplicationElement;
import org.novaforge.forge.importexport.datas.model.Domains.DomainElement;
import org.novaforge.forge.importexport.datas.model.Forge;
import org.novaforge.forge.importexport.datas.model.GroupUsers;
import org.novaforge.forge.importexport.datas.model.Groups;
import org.novaforge.forge.importexport.datas.model.Groups.GroupElement;
import org.novaforge.forge.importexport.datas.model.Memberships;
import org.novaforge.forge.importexport.datas.model.MembershipsGroups.MembershipGroup;
import org.novaforge.forge.importexport.datas.model.MembershipsUsers.MembershipUser;
import org.novaforge.forge.importexport.datas.model.Projects;
import org.novaforge.forge.importexport.datas.model.Projects.ProjectElement;
import org.novaforge.forge.importexport.datas.model.RolesMapping;
import org.novaforge.forge.importexport.datas.model.RolesMapping.RoleMapped;
import org.novaforge.forge.importexport.datas.model.Users;
import org.novaforge.forge.importexport.datas.model.Users.UserElement;
import org.novaforge.forge.plugins.delivery.it.test.HelperTest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlData extends OSGiTestCase
{
  private static final Log LOG = LogFactory.getLog(XmlData.class);
  private final Map<String, String> projects;
  private final Map<String, String> users;
  private Forge forge;
  private Forge                     forge2;

  public XmlData() throws Exception
  {
    projects = new HashMap<>();
    users = new HashMap<>();
    final File importXML = new File(HelperTest.DIRECTORY_XML + "/" + HelperTest.IMPORT_ITESTS_VALIDATION_XML);
    try
    {
      // Read import xml files (initial and changed file for testing changes propagation).
      readXML(importXML, false);

      listProjects();
      listUsers();    
    }
    catch (final Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      LOG.info("Exception raised with message:" + e.getMessage());
      throw new Exception("Error when reading the XML file, probably not available");
    }

  }

  /**
   * This method will read the xml file and unmarshall it into {@link ImportServiceImpl#forgeCore}
   *
   * @throws ImportExportServiceException
   */
  private void readXML(final File pImportXML, final boolean changedXML) throws Exception
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
        if (changedXML)
        {
          forge2 = (Forge) unMarshaller.unmarshal(is);
        }
        else
        {
          forge = (Forge) unMarshaller.unmarshal(is);
        }

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
      throw new Exception(String.format("Unable to read the import xml file [file=%s]", pImportXML.getPath()), e);
    }
    catch (final FileNotFoundException e)
    {
      LOG.error("Reading import data ON ERROR", e);
      throw new Exception(String.format("Unable to read the configuration file [file=%s]", pImportXML.getPath()), e);
    }
    LOG.info("Reading import data SUCCESSFUL");
  }

  /*
   * get imported projects from the XML file
   */
  private void listProjects() throws Exception
  {
    LOG.info("get projects imported from xml file ...");
    handleProjects();
  }

  /*
   * list imported users from XML file
   */
  private void listUsers() throws Exception
  {
    LOG.info("get users imported from xml file ...");
    handleUsers();
  }

  private void handleProjects()
  {
    if (forge != null)
    {
      final Projects forgeProjects = forge.getProjects();
      if ((forgeProjects != null) && (forgeProjects.getProjectElement() != null))
      {
        LOG.info("Getting projects in progress ...");
        for (final ProjectElement project : forgeProjects.getProjectElement())
        {
          LOG.info(String.format("Imported project: %s", project.getId()));
          projects.put(project.getId(), project.getProjectInfo().getName());
        }
      }
      else
      {
        LOG.info("ERROR: forgeProjects or forgeProjects.getProjectElement() are null");
      }
    }
    else
    {
      LOG.info("ERROR: forge is null");
    }
  }

  private void handleUsers()
  {
    if (forge != null)
    {
      final Users forgeUsers = forge.getUsers();
      if ((forgeUsers != null) && (forgeUsers.getUserElement() != null))
      {
        LOG.info("Getting users into Xml in progress ...");
        for (final UserElement user : forgeUsers.getUserElement())
        {
          LOG.info(String.format("Found user: %s with email: %s", user.getLogin(), user.getEmail()));
          users.put(user.getLogin(), user.getEmail());
        }
      }
      else
      {
        LOG.info("ERROR: forgeUsers or forgeUsers.getUserElement() are null");
      }
    }
    else
    {
      LOG.info("ERROR: forge is null");
    }
  }

  /*
   * get user memberships for a given project
   */
  public Map<String, String> getUsersMembership(final String pProjectId) throws Exception
  {
    return listUsersMembership(pProjectId, forge);
  }

  /*
   * list user memberships for a given project into either XML et XML2 according on pForge parameter
   */
  private Map<String, String> listUsersMembership(final String pProjectId, final Forge pForge) throws Exception
  {
    final Map<String, String> map = new HashMap<String, String>();
    if (pForge != null)
    {
      final Projects forgeProjects = pForge.getProjects();
      if ((forgeProjects != null) && (forgeProjects.getProjectElement() != null))
      {
        for (final ProjectElement project : forgeProjects.getProjectElement())
        {
          if (project.getId().equals(pProjectId))
          {
            final Memberships memberships = project.getMemberships();
            if (memberships != null)
            {
              LOG.info(String.format("Get user memberships for project : %s", pProjectId));
              if ((memberships.getMembershipsUsers() != null) && (memberships.getMembershipsUsers().getMembershipUser()
                                                                      != null))
              {
                for (final MembershipUser membershipUser : memberships.getMembershipsUsers().getMembershipUser())
                {
                  LOG.info(String.format("Get user membership : %s", membershipUser.getLogin()));
                  if (membershipUser.getRole().size() == 1)
                  {
                    map.put(membershipUser.getLogin(), membershipUser.getRole().get(0));
                  }
                  else
                  {
                    throw new Exception(String.format("User has more than one role [user=%s]",
                                                      membershipUser.getLogin()));
                  }
                }
              }
            }
          }
        }
      }
      else
      {
        LOG.info("ERROR: No projects or project elements into XLM");
      }
    }
    else
    {
      LOG.info("ERROR: forge is null");
    }
    return map;
  }

  /*
   * get a map with user and role for all users of the froup.
   */
  public Map<String, String> getGroupsUsersMembership(final String pProjectId) throws Exception
  {
    Map<String, String> groupsroles = new HashMap<String, String>();
    final Map<String, String> usersFromGroupeRoles = new HashMap<String, String>();
    groupsroles = getGroupsMembership(pProjectId);
    final Map<String, String> map = new HashMap<String, String>();
    if (forge != null)
    {
      final Projects forgeProjects = forge.getProjects();
      if ((forgeProjects != null) && (forgeProjects.getProjectElement() != null))
      {
        for (final ProjectElement project : forgeProjects.getProjectElement())
        {
          if (project.getId().equals(pProjectId))
          {
            Groups groups = project.getGroups();
            if (groups != null)
            {
              LOG.info(String.format("Getting project groups for project : %s", pProjectId));
              if ((groups != null) && (groups.getGroupElement() != null)
                  && (groups.getGroupElement().size() == 1))
              {
                LOG.info(String.format("get groups for project=%s", pProjectId));
                final List<GroupElement> groupElements = groups.getGroupElement();
                for (final GroupElement groupElement : groupElements)
                {
                  final GroupUsers groupUsers = groupElement.getUsers();
                  for (final String userId : groupUsers.getLogin())
                  {
                    String role = groupsroles.get(groupElement.getId());
                    usersFromGroupeRoles.put(userId, role);
                  }
                }
              }
              else
              {
                throw new Exception(
                    String
                        .format(
                            "ERROR: either groups is null or no element exists or there's more than 1 group with project id = %s",
                            pProjectId));
              }

            }
          }
        }
      }
    }
    return usersFromGroupeRoles;
  }

  // ***************************************************************************
  // *********************** private function **********************************
  // *************************************************************************

  /*
   * get group memberships for a given project
   */
  public Map<String, String> getGroupsMembership(final String pProjectId) throws Exception, IOException
  {
    return listGroupsMembership(pProjectId, forge);
  }

  /*
   * list groups memberships for a given project into either XML et XML2 according on pForge parameter
   */
  private Map<String, String> listGroupsMembership(final String pProjectId, final Forge pForge) throws Exception
  {
    final Map<String, String> groupsroles = new HashMap<String, String>();
    if (pForge != null)
    {
      final Projects forgeProjects = pForge.getProjects();
      if ((forgeProjects != null) && (forgeProjects.getProjectElement() != null))
      {
        for (final ProjectElement project : forgeProjects.getProjectElement())
        {
          if (project.getId().equals(pProjectId))
          {
            final Memberships memberships = project.getMemberships();
            if (memberships != null)
            {
              LOG.info(String.format("Get group memberships for project : %s", pProjectId));
              if ((memberships.getMembershipsGroups() != null) && (memberships.getMembershipsGroups()
                                                                              .getMembershipGroup() != null))
              {
                for (final MembershipGroup membershipGroup : memberships.getMembershipsGroups().getMembershipGroup())
                {
                  LOG.info(String.format("Get group membership : %s", membershipGroup.getId()));
                  if (membershipGroup.getRole().size() == 1)
                  {
                    groupsroles.put(membershipGroup.getId(), membershipGroup.getRole().get(0));
                  }
                  else
                  {
                    throw new Exception(String.format("Group has more than one role [user=%s]",
                                                      membershipGroup.getId()));
                  }
                }
              }
            }
          }
        }
      }
      else
      {
        LOG.info("ERROR: No projects or project elements into XLM");
      }
    }
    else
    {
      LOG.info("ERROR: forge is null");
    }
    return groupsroles;
  }

  /*
   * get application name for a given project and application type
   */
  public String getApplicationName(final String pProjectId, final String pType) throws Exception
  {
    String returned = null;
    final Projects forgeProjects = forge.getProjects();
    if ((forgeProjects != null) && (forgeProjects.getProjectElement() != null))
    {
      for (final ProjectElement project : forgeProjects.getProjectElement())
      {
        if (project.getId().equals(pProjectId))
        {
          if (project.getDomains() != null)
          {
            final List<DomainElement> domains = project.getDomains().getDomainElement();
            if ((domains != null) && (domains.size() == 1))
            {
              for (final DomainElement domain : domains)
              {
                LOG.info(String.format("domain : %s has been found", domain.getName()));

                if (domain.getApplications() != null)
                {
                  final List<ApplicationElement> apps = domain.getApplications().getApplicationElement();
                  if (apps != null)
                  {
                    for (final ApplicationElement app : apps)
                    {
                      // pour s'affranchir des cas ou le app type commene par une majuscule ou minuscule .....
                      if (pType.equalsIgnoreCase(app.getType().value()))
                      {
                        LOG.info(String.format("Found app : %s with type: %s", app.getName(), app.getType()
                            .value()));
                        returned = app.getName();
                        break;
                      }
                    }
                  }
                }
              }
            }
            else
            {
              throw new Exception("ERROR: No domain has been defined or more than one domain exists");
            }
          }
        }

      }
    }

    return returned;
  }
  
  public Map<String, String> getAppMapping(final String pProjectId, final String pType) throws Exception
  {
    {
      Map<String, String> maps = new HashMap<String, String>();
      // extract the number at the end of the type (ex.: Mantis/Mantis2)
      String type = null;
      final char c = pType.charAt(pType.length() - 1);
      if ('2' == c)
      {
        type = pType.substring(0, pType.length() - 1);
        maps = listAppMapping(pProjectId, type, forge2);
      }
      else
      {
        type = pType;
        maps = listAppMapping(pProjectId, type, forge);
      }

      return maps;
    }
  }

  /*
   * get role mapping (role forge, role application into either XML et XML2 according on pForge parameter
   */
  private Map<String, String> listAppMapping(final String pProjectId, final String pType, final Forge pForge)
      throws Exception
  {
    final Map<String, String> maps = new HashMap<String, String>();
    final Projects forgeProjects = pForge.getProjects();
    if ((forgeProjects != null) && (forgeProjects.getProjectElement() != null))
    {
      for (final ProjectElement project : forgeProjects.getProjectElement())
      {
        if (project.getId().equals(pProjectId))
        {
          if (project.getDomains() != null)
          {
            final List<DomainElement> domains = project.getDomains().getDomainElement();
            if ((domains != null) && (domains.size() == 1))
            {
              for (final DomainElement domain : domains)
              {
                LOG.info(String.format("domain : %s has been found", domain.getName()));

                if (domain.getApplications() != null)
                {
                  final List<ApplicationElement> apps = domain.getApplications().getApplicationElement();
                  if (apps != null)
                  {
                    for (final ApplicationElement app : apps)
                    {
                      if (pType.equals(app.getType().value()))
                      {
                        LOG.info(String.format("Found app : %s with type: %s", app.getName(), app.getType()
                            .value()));
                        final RolesMapping importExportrolesMapping = app.getRolesMapping();
                        final List<RoleMapped> roles = importExportrolesMapping.getRoleMapped();

                        if (roles != null)
                        {
                          for (final RoleMapped role : roles)
                          {
                            LOG.info(String.format("Role mapping: roleForge= %s  roleApp=%s",
                                role.getForgeId(), role.getApplicationId()));
                            // case no role (ie. for sympa 2 possibility: no role or subscriber
                            if (!"".equals(role.getApplicationId()))
                            {
                              maps.put(role.getForgeId(), role.getApplicationId());
                            }
                          }
                        }
                        else
                        {
                          throw new Exception(String.format("ERROR: No roles mapping for application: %s",
                              app.getName()));
                        }
                      }
                    }
                  }
                }
              }
            }
            else
            {
              throw new Exception("ERROR: No domain has been defined or more than one domain exists");
            }
          }
        }

      }
    }
    return maps;
  }

  public List<String> getProjectToDelete() throws Exception
  {
    // TODO if
    return new ArrayList<String>();
  }

  public Map<String, String> getProjects()
  {
    return projects;
  }

  public Map<String, String> getUsers() throws Exception
  {
    return users;
  }




}
