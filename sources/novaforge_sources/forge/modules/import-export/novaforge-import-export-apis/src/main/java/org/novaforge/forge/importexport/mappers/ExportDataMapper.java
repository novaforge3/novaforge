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
package org.novaforge.forge.importexport.mappers;

import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.importexport.datas.model.Domains.DomainElement;
import org.novaforge.forge.importexport.datas.model.Groups;
import org.novaforge.forge.importexport.datas.model.Groups.GroupElement;
import org.novaforge.forge.importexport.datas.model.MembershipsGroups.MembershipGroup;
import org.novaforge.forge.importexport.datas.model.MembershipsUsers.MembershipUser;
import org.novaforge.forge.importexport.datas.model.Projects.ProjectElement;
import org.novaforge.forge.importexport.datas.model.Users;
import org.novaforge.forge.importexport.datas.model.Users.UserElement;

import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public interface ExportDataMapper
{
  /**
   * This method allows to converts a forge model user to data model user
   * 
   * @param pUser
   * @return a user in data model
   */
  UserElement toUser(User pUser);

  /**
   * This method allows to converts a list of forge model users to a list of data model users
   * 
   * @param pUsers
   * @return Users
   */
  Users toUsers(List<User> pUsers);

  /**
   * This method allows to converts a forge model group to data model group
   * 
   * @param pGroup
   * @return a group in data model
   */
  GroupElement toGroup(Group pGroup);

  /**
   * This method allows to converts a list of forge model groups to a list of data model groups
   * 
   * @param pGroups
   * @return Groups
   */
  Groups toGroups(List<Group> pGroups);

  /**
   * This method allows to converts a forge model group to data model group
   * 
   * @param pProject
   * @return the project
   */
  ProjectElement toProject(Project pProject);

  /**
   * This method allows to add a list of roles to a project passed in argument
   * 
   * @param pProject
   * @param pRoles
   * @return
   */
  ProjectElement addRolesToProject(ProjectElement pProject, List<ProjectRole> pRoles);

  /**
   * This method allows to create a domain object
   * 
   * @param pName
   * @return
   */
  DomainElement toDomain(String pName);

  /**
   * This method allows to add an application to the domain passed in argument
   * 
   * @param pDomain
   * @param pCategory
   * @param pType
   * @param pName
   * @param pDescription
   * @param pRolesMapping
   * @return Domain
   */
  DomainElement addApplicationToDomain(DomainElement pDomain, String pCategory, String pType, String pName,
      String pDescription, Map<String, String> pRolesMapping);

  /**
   * This method allows to convert a Membership into a MembershipGroup
   * 
   * @param pMembership
   * @return MembershipGroup
   */
  MembershipGroup toMembershipGroup(MembershipInfo pMembership);

  /**
   * This method allows to convert a Membership into a MembershipUser
   * 
   * @param pMembership
   * @return MembershipUser
   */
  MembershipUser toMembershipUser(MembershipInfo pMembership);
}
