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
package org.novaforge.forge.core.organization.internal.presenter;

import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.internal.model.AuthorizationResourceImpl;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.presenters.GroupPresenter;
import org.novaforge.forge.core.organization.services.GroupService;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.List;
import java.util.UUID;

/**
 * This is an implementation of {@link GroupPresenter}
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see GroupPresenter
 */
public class GroupPresenterImpl implements GroupPresenter
{

  /**
   * Reference to {@link GroupService} service injected by the container
   */
  private GroupService     groupService;
  /**
   * Reference to {@link SecurityDelegate} service injected by the container
   */
  private SecurityDelegate securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public Group newGroup()
  {
    return groupService.newGroup();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.CREATE_GROUP)
  public void createGroup(@HistorizableParam(label = "group") final Group pGroup, final String pProjectId)
      throws GroupServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Group.class,
        PermissionAction.READ));
    groupService.createGroup(pGroup, pProjectId);
    securityDelegate.clearPermissionCached(pGroup.getUsers());

  }

  /**
   * {@inheritDoc}
   *
   * @throws GroupServiceException
   */
  @Override
  public Group getGroup(final String pProjectId, final String pLogin) throws GroupServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Group.class,
        PermissionAction.READ));
    return groupService.getGroup(pProjectId, pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.UPDATE_GROUP)
  public void updateGroup(@HistorizableParam(label = "projectId") final String pProjectId,
      final String pGroupOldName, @HistorizableParam(label = "group") final Group pGroup)
      throws GroupServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Group.class,
        PermissionAction.UPDATE));

    final String currentUser = securityDelegate.getCurrentUser();

    groupService.updateGroup(pProjectId, pGroupOldName, pGroup, currentUser);

    securityDelegate.clearPermissionCached(pGroup.getUsers());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.DELETE_GROUP)
  public void deleteGroup(@HistorizableParam(label = "projectId") final String pProjectId,
                          @HistorizableParam(label = "uuid") final UUID pGroupUUID) throws GroupServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Group.class, PermissionAction.READ,
                                                                             PermissionAction.DELETE));
    final Group group = groupService.deleteGroup(pProjectId, pGroupUUID);
    securityDelegate.clearPermissionCached(group.getUsers());
  }

  /**
   * {@inheritDoc}
   *
   * @throws GroupServiceException
   */
  @Override
  public List<Group> getAllGroups(final String pProjectId, final boolean pWithPublic) throws GroupServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Group.class, PermissionAction.READ));
    return groupService.getAllGroups(pProjectId, pWithPublic);
  }

}
