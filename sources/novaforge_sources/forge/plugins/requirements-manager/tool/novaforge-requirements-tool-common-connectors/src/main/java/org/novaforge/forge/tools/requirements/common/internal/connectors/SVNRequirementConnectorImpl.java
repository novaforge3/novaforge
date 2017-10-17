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
package org.novaforge.forge.tools.requirements.common.internal.connectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.commons.technical.jms.MessageService;
import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.plugins.requirements.requirementmanager.route.RequirementManagerQueueName;
import org.novaforge.forge.tools.requirements.common.connectors.SCMRequirementConnector;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementConnectorException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sbenoist
 */
public class SVNRequirementConnectorImpl extends AbstractRequirementConnector implements
    SCMRequirementConnector
{
  private static final Log LOGGER = LogFactory.getLog(SVNRequirementConnectorImpl.class);
  private String               requirementEnumClassName;
  private HistorizationService historizationService;
  private MessageService       messageService;

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.SYNCHRONIZATION_REQUIREMENTS_AND_SOURCES)
  public void synchronizeWithCode(@HistorizableParam(label = "ProjectId") final String pProjectId,
      final String pCodeRepositoryPath, final String pCurrentUser) throws RequirementConnectorException
  {
    Map<String, Object> maps = new HashMap<String, Object>();
    maps.put("ProjectId", pProjectId);
    historizationService.registerEvent(pCurrentUser, EventType.SYNCHRONIZATION_REQUIREMENTS_AND_SOURCES, EventLevel.ENTRY, maps);

    final Map<String, String> map = new HashMap<String, String>();
    map.put("projectId", pProjectId);
    map.put("codeRepositoryPath", pCodeRepositoryPath);
    map.put("currentUser", pCurrentUser);
    map.put("enumClassName", requirementEnumClassName);

    try
    {
      messageService.publish(RequirementManagerQueueName.REQUIREMENT_SOURCES_SYNCHRO_TOPIC_SHORT_NAME, map);
    }
    catch (final MessageServiceException e)
    {
      throw new RequirementConnectorException(
          "an error occured during sending message from tool to plugin in order to make synchronization with sources application",
          e);
    }

    LOGGER.info(String.format(
        "a message requesting getting code resources from svn has been sent for projectID=%s and login=%s",
        pProjectId, pCurrentUser));

    historizationService.registerEvent(pCurrentUser, EventType.SYNCHRONIZATION_REQUIREMENTS_AND_SOURCES, EventLevel.EXIT, new HashMap<String, Object>());
  }

  public void setRequirementEnumClassName(final String pRequirementEnumClassName)
  {
    requirementEnumClassName = pRequirementEnumClassName;
  }

  @Override
  protected EventType getEventType()
  {
    return EventType.SYNCHRONIZATION_REQUIREMENTS_AND_SOURCES;
  }

  @Override
  protected HistorizationService getHistorizationService()
  {
    return historizationService;
  }

  public void setHistorizationService(final HistorizationService historizationService)
  {
    this.historizationService = historizationService;
  }

  public void setMessageService(final MessageService pMessageService)
  {
    messageService = pMessageService;
  }

}
