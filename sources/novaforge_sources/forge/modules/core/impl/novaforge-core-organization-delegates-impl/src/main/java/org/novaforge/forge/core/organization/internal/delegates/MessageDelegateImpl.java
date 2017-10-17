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
package org.novaforge.forge.core.organization.internal.delegates;

import org.novaforge.forge.commons.technical.jms.MessageService;
import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.delegates.MessageDelegate;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.route.OrganizationQueueHeader;
import org.novaforge.forge.core.organization.route.OrganizationQueueName;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class MessageDelegateImpl implements MessageDelegate
{

  private MessageService             messageService;
  private ForgeIdentificationService forgeIdentificationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendProjectMessage(final String pPluginUUID, final String pInstanceUUID,
      final String instanceLabel, final String pProjectId, final Serializable pObject,
      final String pToolInstanceUUID, final String pAction, final String pLogin)
      throws MessageServiceException
  {
    sendProjectMessage(pPluginUUID, pInstanceUUID, instanceLabel, pProjectId, pObject, pToolInstanceUUID,
        pAction, pLogin, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendProjectMessage(final String pPluginUUID, final String pInstanceUUID,
      final String instanceLabel, final String pProjectId, final Serializable pObject,
      final String pToolInstanceUUID, final String pAction, final String pLogin, final boolean pIgnore)
      throws MessageServiceException
  {
    // set the header
    final Map<String, String> headers = buildHeaders(pPluginUUID, pInstanceUUID, instanceLabel, pProjectId,
        pLogin, pToolInstanceUUID, pAction, pIgnore);

    // send the message
    messageService.sendMessage(PluginQueueName.PROJECT_QUEUE_NAME, pObject, headers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendMembershipMessage(final String pPluginUUID, final String pInstanceUUID,
      final String pProjectId, final Serializable pMemberships, final String userName, final String action)
      throws MessageServiceException
  {
    // set the header
    final Map<String, String> headers = buildHeaders(pPluginUUID, pInstanceUUID, "", pProjectId, userName,
        null, action, false);

    // send the message
    messageService.sendMessage(PluginQueueName.MEMBERSHIP_QUEUE_NAME, pMemberships, headers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendRolesMappingMessage(final String pPluginUUID, final String pInstanceUUID,
      final String pProjectId, final Serializable pRolesMapping, final String userName, final String action)
      throws MessageServiceException
  {
    // set the header
    final Map<String, String> headers = buildHeaders(pPluginUUID, pInstanceUUID, "", pProjectId, userName,
        null, action, false);

    // send the message
    messageService.sendMessage(PluginQueueName.ROLES_MAPPING_QUEUE_NAME, pRolesMapping, headers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendUserMessage(final String pPluginUUID, final String pInstanceUUID, final String pProjectId,
      final User pUser, final String pAction) throws MessageServiceException
  {
    // set the header
    final Map<String, String> headers = buildHeaders(pPluginUUID, pInstanceUUID, "",
        pProjectId, pUser.getLogin(), null, pAction, false);

    // send the message
    messageService.sendMessage(PluginQueueName.USER_QUEUE_NAME, pUser, headers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendSharedProjectMessage(final String pProjectId, final UUID pUserUUID, final RealmType pRealmType)
      throws MessageServiceException
  {
    // set the header
    final Map<String, String> headers = new HashMap<String, String>();
    headers.put(OrganizationQueueHeader.PROJECT_ID_HEADER, pProjectId);
    headers.put(OrganizationQueueHeader.USER_UUID_HEADER, pUserUUID.toString());
    headers.put(OrganizationQueueHeader.TYPE_HEADER, pRealmType.getLabel());

    // send the message
    messageService.sendMessage(OrganizationQueueName.SHARED_PROJECT_QUEUE_SHORT, "addMembership", headers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendGroupMessage(final String pPluginUUID, final String pInstanceUUID, final String pProjectId,
                               final Group pGroup, final String pAction, final String pUsername)
      throws MessageServiceException
  {
    // set the header
    final Map<String, String> headers = buildHeaders(pPluginUUID, pInstanceUUID, "", pProjectId,
                                                     pUsername, null, pAction, false);

    // send the message
    messageService.sendMessage(PluginQueueName.GROUP_QUEUE_NAME, pGroup, headers);

  }

  private Map<String, String> buildHeaders(final String pluginUUID, final String instanceUUID,
      final String instanceLabel, final String projectId, final String userName,
      final String pToolInstanceUUID, final String action, final boolean pIgnore)
  {
    final Map<String, String> headers = new HashMap<String, String>();
    headers.put(PluginQueueHeader.FORGE_ID_HEADER, forgeIdentificationService.getForgeId().toString());
    headers.put(PluginQueueHeader.PLUGIN_UUID_HEADER, pluginUUID);
    headers.put(PluginQueueHeader.INSTANCE_UUID_HEADER, instanceUUID);
    headers.put(PluginQueueHeader.INSTANCE_LABEL_HEADER, instanceLabel);
    headers.put(PluginQueueHeader.PROJECT_ID_HEADER, projectId);
    headers.put(PluginQueueHeader.IGNORE_ERRORS_HEADER, Boolean.toString(pIgnore));
    if (userName != null)
    {
      headers.put(PluginQueueHeader.USER_NAME_HEADER, userName);
    }

    if (pToolInstanceUUID != null)
    {
      headers.put(PluginQueueHeader.TOOL_INSTANCE_UUID_HEADER, pToolInstanceUUID);
    }
    headers.put(PluginQueueHeader.ACTION_HEADER, action);
    return headers;
  }

  /**
   * Use by container to inject {@link MessageService} implementation
   *
   * @param pMessageService
   *          the messageService to set
   */
  public void setMessageService(final MessageService pMessageService)
  {
    messageService = pMessageService;
  }

  /**
   * Use by container to inject {@link ForgeIdentificationService} implementation
   *
   * @param pForgeIdentificationService
   *          the forgeIdentificationService to set
   */
  public void setForgeIdentificationService(final ForgeIdentificationService pForgeIdentificationService)
  {
    forgeIdentificationService = pForgeIdentificationService;
  }
}
