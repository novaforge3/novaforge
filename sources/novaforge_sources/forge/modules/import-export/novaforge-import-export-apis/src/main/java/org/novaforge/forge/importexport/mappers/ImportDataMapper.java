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
package org.novaforge.forge.importexport.mappers;

import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.importexport.datas.model.Groups.GroupElement;
import org.novaforge.forge.importexport.datas.model.ProjectInfo;
import org.novaforge.forge.importexport.datas.model.Roles.RoleElement;
import org.novaforge.forge.importexport.datas.model.RolesMapping;
import org.novaforge.forge.importexport.datas.model.Users.UserElement;
import org.novaforge.forge.importexport.exceptions.ImportExportServiceException;

import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public interface ImportDataMapper
{

  /**
   * This method will update the {@link UserEntity} given with the {@link User}
   * 
   * @param pUser
   *          the user object read
   * @param pUserEntity
   *          the entity to update
   * @return the entity updated
   * @throws ImportExportServiceException
   */
  User updateEntity(final UserElement pUser, final User pUserEntity) throws ImportExportServiceException;

  /**
   * This method will update the {@link GroupEntity} given with the {@link Group}
   * 
   * @param pGroup
   *          the group object read
   * @param pGroupEntity
   *          the entity to update
   * @return the entity updated
   */
  Group updateEntity(final GroupElement pGroup, final Group pGroupEntity);

  /**
   * This method will update the {@link ProjectEntity} given with the {@link ProjectInfo}
   * 
   * @param pProjectId
   *          the project id read
   * @param pProjectInfo
   *          the project object read
   * @param pProjectGot
   *          the entity to update
   * @return the entity updated
   * @throws ImportExportServiceException
   */
  Project updateEntity(final String pProjectId, final ProjectInfo pProjectInfo, final Project pProjectGot)
      throws ImportExportServiceException;

  /**
   * This method will update the {@link ProjectRoleEntity} given with the {@link Role}
   * 
   * @param pRole
   *          the project object read
   * @param pRoleGot
   *          the entity to update
   * @return the entity updated
   */
  ProjectRole updateEntity(final RoleElement pRole, final ProjectRole pRoleGot);

  /**
   * This method will create a {@link Map} according to the {@link RolesMapping} given
   * 
   * @param pRolesMapping
   *          the RolesMapping object read
   * @return a map which contains the role mapping
   */
  Map<String, String> buildMapping(final RolesMapping pRolesMapping);

}