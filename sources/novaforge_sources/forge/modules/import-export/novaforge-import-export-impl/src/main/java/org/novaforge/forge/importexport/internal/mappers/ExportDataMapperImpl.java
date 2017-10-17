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
package org.novaforge.forge.importexport.internal.mappers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.importexport.datas.model.AppCategory;
import org.novaforge.forge.importexport.datas.model.AppType;
import org.novaforge.forge.importexport.datas.model.Applications;
import org.novaforge.forge.importexport.datas.model.Applications.ApplicationElement;
import org.novaforge.forge.importexport.datas.model.Domains;
import org.novaforge.forge.importexport.datas.model.Domains.DomainElement;
import org.novaforge.forge.importexport.datas.model.GroupUsers;
import org.novaforge.forge.importexport.datas.model.Groups;
import org.novaforge.forge.importexport.datas.model.Groups.GroupElement;
import org.novaforge.forge.importexport.datas.model.MembershipsGroups.MembershipGroup;
import org.novaforge.forge.importexport.datas.model.MembershipsUsers.MembershipUser;
import org.novaforge.forge.importexport.datas.model.ProjectInfo;
import org.novaforge.forge.importexport.datas.model.Projects.ProjectElement;
import org.novaforge.forge.importexport.datas.model.Roles;
import org.novaforge.forge.importexport.datas.model.Roles.RoleElement;
import org.novaforge.forge.importexport.datas.model.RolesMapping;
import org.novaforge.forge.importexport.datas.model.RolesMapping.RoleMapped;
import org.novaforge.forge.importexport.datas.model.Users;
import org.novaforge.forge.importexport.datas.model.Users.UserElement;
import org.novaforge.forge.importexport.mappers.ExportDataMapper;

import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public class ExportDataMapperImpl implements ExportDataMapper
{
  private static final Log LOG = LogFactory.getLog(ExportDataMapperImpl.class);

  @Override
  public UserElement toUser(final User pUser)
  {
    LOG.info(String.format("Add user with login=%s", pUser.getLogin()));
    UserElement user = new UserElement();
    user.setFirstname(pUser.getFirstName());
    user.setLastname(pUser.getName());
    user.setEmail(pUser.getEmail().trim());
    user.setLogin(pUser.getLogin());
    user.setPasswd(pUser.getPassword());
    return user;
  }

  @Override
  public Users toUsers(final List<User> pUsers)
  {
    Users users = new Users();
    for (org.novaforge.forge.core.organization.model.User user : pUsers)
    {
      users.getUserElement().add(toUser(user));
    }
    return users;
  }

  @Override
  public GroupElement toGroup(final Group pGroup)
  {
    LOG.info(String.format("Add group with login=%s", pGroup.getName()));
    GroupElement group = new GroupElement();
    group.setId(pGroup.getName());
    group.setDescription(pGroup.getDescription());
    group.setPublic(pGroup.isVisible());

    GroupUsers groupUsers = new GroupUsers();
    for (org.novaforge.forge.core.organization.model.User user : pGroup.getUsers())
    {
      LOG.info(String.format("Add user in group with login=%s", user.getLogin()));
      groupUsers.getLogin().add(user.getLogin());
    }

    group.setUsers(groupUsers);
    return group;
  }

  @Override
  public Groups toGroups(final List<Group> pGroups)
  {
    Groups groups = new Groups();
    for (org.novaforge.forge.core.organization.model.Group group : pGroups)
    {
      groups.getGroupElement().add(toGroup(group));
    }
    return groups;
  }

  @Override
  public ProjectElement toProject(final Project pProject)
  {
    LOG.info(String.format("Add project with projectID=%s", pProject.getProjectId()));
    ProjectElement project = new ProjectElement();
    project.setId(pProject.getProjectId());

    // fill the project informations
    ProjectInfo projectInfo = new ProjectInfo();
    projectInfo.setDescription(pProject.getDescription());
    projectInfo.setLicenceType(pProject.getLicenceType());
    projectInfo.setName(pProject.getName());
    projectInfo.setAuthor(pProject.getAuthor());
    projectInfo.setPrivate(pProject.isPrivateVisibility());
    project.setProjectInfo(projectInfo);

    // set a default domains oject
    Domains domains = new Domains();
    project.setDomains(domains);
    return project;
  }

  @Override
  public ProjectElement addRolesToProject(final ProjectElement pProject, final List<ProjectRole> pRoles)
  {
    Roles roles = new Roles();
    for (ProjectRole projectRole : pRoles)
    {
      RoleElement role = new RoleElement();
      LOG.info(String.format("Add role with name=%s", projectRole.getName()));
      role.setDescription(projectRole.getDescription());
      role.setName(projectRole.getName());
      roles.getRoleElement().add(role);
    }
    pProject.setRoles(roles);
    return pProject;
  }

  @Override
  public DomainElement toDomain(final String pName)
  {
    DomainElement domain = new DomainElement();
    domain.setName(pName);

    LOG.info(String.format("Add domain with name=%s", pName));
    // set a default applications
    Applications applications = new Applications();
    domain.setApplications(applications);
    return domain;
  }

  @Override
  public DomainElement addApplicationToDomain(final DomainElement pDomain, final String pCategory,
      final String pType, final String pName, final String pDescription,
      final Map<String, String> pRolesMapping)
  {
    ApplicationElement application = new ApplicationElement();
    LOG.info(String.format("Add application with name=%s", pName));
    application.setRolesMapping(toRolesMapping(pRolesMapping));
    application.setCategory(AppCategory.fromValue(pCategory));
    application.setName(pName);
    application.setDescription(pDescription);
    application.setType(AppType.fromValue(pType));
    pDomain.getApplications().getApplicationElement().add(application);
    return pDomain;
  }

  @Override
  public MembershipUser toMembershipUser(final MembershipInfo pMembership)
  {
    MembershipUser membershipUser = new MembershipUser();
    User user = (User) pMembership.getActor();
    membershipUser.setLogin(user.getLogin());
    membershipUser.getRole().add(pMembership.getRole().getName());
    return membershipUser;
  }

  @Override
  public MembershipGroup toMembershipGroup(final MembershipInfo pMembership)
  {
    MembershipGroup membershipGroup = new MembershipGroup();
    Group group = (Group)pMembership.getActor();
    membershipGroup.setId(group.getName());
    membershipGroup.getRole().add(pMembership.getRole().getName());
    return membershipGroup;
  }

  private RolesMapping toRolesMapping(final Map<String, String> pRolesMapping)
  {
    RolesMapping rolesMapping = new RolesMapping();
    for (Map.Entry<String, String> entry : pRolesMapping.entrySet())
    {
      RoleMapped role = new RoleMapped();
      role.setForgeId(entry.getKey());
      role.setApplicationId(entry.getValue());

      rolesMapping.getRoleMapped().add(role);
    }

    return rolesMapping;

  }
}
