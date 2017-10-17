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
package org.novaforge.forge.core.plugins.internal.delegates;

import org.novaforge.forge.commons.technical.jms.MessageService;
import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.core.plugins.categories.AssociationType;
import org.novaforge.forge.core.plugins.delegates.PluginMessageDelegate;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Service implementation of {@link PluginMessageDelegate}
 * 
 * @author Guillaume Lamirand
 */
public class PluginMessageDelegateImpl implements PluginMessageDelegate
{
  /**
   * {@link MessageService} injected by container
   */
  private MessageService messageService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendNotificationMessage(final String pForgeId, final String pProjectId,
      final String pInstanceUUID, final String pNotification, final String pUserName, final Serializable pBean)
      throws MessageServiceException
  {
    // set the header
    final Map<String, String> headers = buildHeaders(pForgeId, pProjectId, pInstanceUUID, pNotification,
        AssociationType.NOTIFICATION, pUserName);

    // send the message
    messageService.sendMessage(PluginQueueName.COMPOSITION_QUEUE_NAME, pBean, headers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendDataMessage(final String pForgeId, final String pProjectId, final String pInstanceUUID,
      final String pEvent, final String pUserName, final Serializable pBean) throws MessageServiceException
  {
    // set the header
    final Map<String, String> headers = buildHeaders(pForgeId, pProjectId, pInstanceUUID, pEvent,
        AssociationType.SEND, pUserName);

    // send the message
    messageService.sendMessage(PluginQueueName.COMPOSITION_QUEUE_NAME, pBean, headers);

  }

  private Map<String, String> buildHeaders(final String pForgeid, final String pProjectId,
      final String pInstanceUUID, final String pEvent, final AssociationType pType, final String pUserName)
  {
    final Map<String, String> headers = new HashMap<String, String>();
    headers.put(PluginQueueHeader.FORGE_ID_HEADER, pForgeid);
    headers.put(PluginQueueHeader.PROJECT_ID_HEADER, pProjectId);
    headers.put(PluginQueueHeader.INSTANCE_UUID_HEADER, pInstanceUUID);
    headers.put(PluginQueueHeader.EVENT_SOURCE_HEADER, pEvent);
    headers.put(PluginQueueHeader.TYPE_SOURCE_HEADER, pType.name());
    headers.put(PluginQueueHeader.USER_NAME_HEADER, pUserName);
    return headers;
  }

  /**
   * Use by container to inject {@link MessageService}
   * 
   * @param messageService
   *          the messageService to set
   */
  public void setMessageService(final MessageService messageService)
  {
    this.messageService = messageService;
  }

}
