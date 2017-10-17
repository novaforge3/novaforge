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
package org.novaforge.forge.core.organization.delegates;

import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author lamirang
 */
public interface MessageDelegate
{
  void sendProjectMessage(String pPluginUUID, String pInstanceUUID, String instanceLabel, String pProjectId,
      Serializable pObject, String pToolInstanceName, String pAction, String pLogin)
      throws MessageServiceException;

  void sendProjectMessage(String pPluginUUID, String pInstanceUUID, String instanceLabel, String pProjectId,
      Serializable pObject, String pToolInstanceName, String pAction, String pLogin, boolean pIgnore)
      throws MessageServiceException;

  void sendMembershipMessage(String pPluginUUID, String pInstanceUUID, String pProjectId,
      Serializable pMemberships, String userName, String action) throws MessageServiceException;

  void sendRolesMappingMessage(String pPluginUUID, String pInstanceUUID, String pProjectId,
      Serializable pRolesMapping, String userName, String action) throws MessageServiceException;

  void sendUserMessage(String pluginUUID, String instanceUUID, String projectId, User user, String action)
      throws MessageServiceException;

  void sendSharedProjectMessage(final String pProjectId, final UUID pUserUUID, final RealmType pRealmType)
      throws MessageServiceException;

  void sendGroupMessage(String pPluginUUID, String pInstanceUUID, String pProjectId, Group pGroup,
      String pAction, String pUsername) throws MessageServiceException;

}