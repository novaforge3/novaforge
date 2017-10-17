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
package org.novaforge.forge.importexport.handlers;

import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.importexport.datas.model.Applications.ApplicationElement;
import org.novaforge.forge.importexport.datas.model.Domains.DomainElement;
import org.novaforge.forge.importexport.datas.model.Groups.GroupElement;
import org.novaforge.forge.importexport.datas.model.MembershipsGroups.MembershipGroup;
import org.novaforge.forge.importexport.datas.model.MembershipsUsers.MembershipUser;
import org.novaforge.forge.importexport.datas.model.ProjectInfo;
import org.novaforge.forge.importexport.datas.model.Roles.RoleElement;
import org.novaforge.forge.importexport.datas.model.Users.UserElement;
import org.novaforge.forge.importexport.exceptions.ImportExportServiceException;

/**
 * This handler will import data from {@link ForgeImport} into the forge.
 * 
 * @author Guillaume Lamirand
 */
public interface ImportHandler
{

  /**
   * This method will import the {@link User} given in parameter. It will catch all exception and return
   * false if any errors occured.
   * 
   * @param pUser
   *          the user to import
   * @return true if success false otherwise
   */
  boolean importUser(final UserElement pUser);

  /**
   * This method will import the {@link Group} given in parameter. It will catch all exception and return
   * false if any errors occured.
   * 
   * @param pProjectId
   *          the project id to import group
   * @param pGroup
   *          the group to import
   * @return true if success false otherwise
   */
  boolean importGroup(final String pProjectId, final GroupElement pGroup);

  /**
   * This method will import the {@link ProjectInfo} given in parameter. It will catch all exception and
   * return * false if any errors occured.
   * 
   * @param pProjectId
   *          the project id
   * @param pProject
   *          the project info to import
   * @return true if success false otherwise
   */
  boolean importProjectInfo(final String pProjectId, final ProjectInfo pProject);

  /**
   * This method will import the {@link Role} given in parameter. It will catch all exception and return
   * false if any errors occured.
   * 
   * @param pProjectId
   *          the project id
   * @param pRole
   *          the role to import
   * @return true if success false otherwise
   */
  boolean importRole(final String pProjectId, final RoleElement pRole);

  /**
   * This method will import the {@link Role} given in parameter. It will catch all exception and return
   * false if any errors occured.
   * 
   * @param pProjectId
   *          the project id
   * @param pDomain
   *          the domaine to import
   * @return true if success false otherwise
   * @throws ImportExportServiceException
   */
  boolean importDomain(final String pProjectId, final DomainElement pDomain);

  /**
   * This method will import the {@link Role} given in parameter. It will catch all exception and return
   * false if any errors occured.
   * 
   * @param pProjectId
   *          the project id
   * @param pSpaceName
   *          the parent space name used to retrieve its URI
   * @param pApp
   *          the domaine to import
   * @return true if success false otherwise
   */
  boolean importApp(final String pProjectId, final String pSpaceName, final ApplicationElement pApp);

  /**
   * This method will import the {@link MembershipUser} given in parameter. It will catch all exception and
   * return false if any errors occured.
   * 
   * @param pProjectId
   *          the project id
   * @param pMembershipUser
   *          the membership to import
   * @return true if success false otherwise
   */
  boolean importUserMembership(final String pProjectId, final MembershipUser pMembershipUser);

  /**
   * This method will import the {@link MembershipGroup} given in parameter. It will catch all exception and
   * return false if any errors occured.
   * 
   * @param pProjectId
   *          the project id
   * @param pMembershipGroup
   *          the membership to import
   * @return true if success false otherwise
   */
  boolean importGroupMembership(final String pProjectId, final MembershipGroup pMembershipGroup);

}