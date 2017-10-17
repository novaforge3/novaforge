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
package org.novaforge.forge.core.organization.internal.dao;

import org.novaforge.forge.core.organization.entity.GroupEntity;
import org.novaforge.forge.core.organization.entity.OrganizationEntity;
import org.novaforge.forge.core.organization.entity.ProjectApplicationEntity;
import org.novaforge.forge.core.organization.entity.ProjectEntity;
import org.novaforge.forge.core.organization.entity.ProjectRoleEntity;
import org.novaforge.forge.core.organization.entity.SpaceEntity;
import org.novaforge.forge.core.organization.entity.UserEntity;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;

import java.util.UUID;

/**
 * @author ibn-m
 */
public class DataCreator
{

  public static SpaceEntity createSpaceNode(final String uri, final String name)
  {
    final SpaceEntity result = new SpaceEntity();
    result.setUri(uri);
    result.setName(name);
    return result;
  }

  public static ProjectEntity createProjectElement(final String pElementId)
  {
    return createProjectElement(pElementId, pElementId);
  }

  public static ProjectEntity createProjectElement(final String pElementId, final String pName)
  {
    final ProjectEntity result = new ProjectEntity();
    result.setElementId(pElementId);
    result.setName(pName);
    result.setAuthor("");
    result.setLicenceType("");
    final OrganizationEntity organization = new OrganizationEntity();
    organization.setName(pName);
    result.setOrganization(organization);
    result.setPrivateVisibility(true);

    result.setRealmType(RealmType.ANONYMOUS);
    result.setStatus(ProjectStatus.VALIDATED);
    result.setDescription("");
    return result;
  }

  public static ProjectApplicationEntity createProjectApplication(final String uri, final String name,
      final UUID pluginInstanceUUID)
  {
    final ProjectApplicationEntity result = new ProjectApplicationEntity();
    result.setUri(uri);
    result.setName(name);
    result.setPluginInstanceUUID(pluginInstanceUUID);
    return result;
  }

  public static GroupEntity createGroup(final String name, final boolean visibility)
  {
    final GroupEntity result = new GroupEntity();
    result.setName(name);
    result.setVisible(visibility);
    return result;
  }

  public static User createUser(final String userLogin)
  {
    final UserEntity result = new UserEntity();
    result.setLogin(userLogin);
    return result;
  }

  public static ProjectRoleEntity createRole(final String name, final int order)
  {
    final ProjectRoleEntity result = new ProjectRoleEntity();
    result.setName(name);
    result.setOrder(new Integer(order));
    return result;
  }

}
