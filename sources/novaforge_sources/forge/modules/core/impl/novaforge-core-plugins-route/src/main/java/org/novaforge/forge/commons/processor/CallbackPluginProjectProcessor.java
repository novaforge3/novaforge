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
package org.novaforge.forge.commons.processor;

import org.apache.camel.Exchange;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;

/**
 * @author sbenoist
 */
public class CallbackPluginProjectProcessor extends CallbackPluginProcessor
{
  /**
   * Reference to service implementation of {@link ApplicationService}
   */
  private final ApplicationService applicationService;

  /**
   * @param pHistorizationService
   * @param pEventType
   * @param pApplicationService
   */
  public CallbackPluginProjectProcessor(final HistorizationService pHistorizationService,
      final EventType pEventType, final ApplicationService pApplicationService)
  {
    super(pHistorizationService, pEventType);
    applicationService = pApplicationService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void process(final Exchange pExchange) throws Exception
  {
    super.process(pExchange);
    final String ignore = (String) pExchange.getIn().getHeader(PluginQueueHeader.IGNORE_ERRORS_HEADER);
    final String response = (String) pExchange.getIn().getHeader(PluginQueueHeader.STATUS_RESPONSE_HEADER);
    final String action = (String) pExchange.getIn().getHeader(PluginQueueHeader.ACTION_HEADER);
    final String instanceId = (String) pExchange.getIn().getHeader(PluginQueueHeader.INSTANCE_UUID_HEADER);
    final PluginQueueAction queueAction = PluginQueueAction.fromLabel(action);
    // change the application status in case of creation
    if (PluginQueueAction.CREATE.equals(queueAction))
    {
      if (PluginQueueHeader.STATUS_KO.equals(response))
      {
        applicationService.changeApplicationStatus(ApplicationStatus.CREATE_ON_ERROR, instanceId);
      }
      else if (PluginQueueHeader.STATUS_OK.equals(response))
      {
        applicationService.changeApplicationStatus(ApplicationStatus.ACTIVE, instanceId);
      }
    }
    else if (PluginQueueAction.DELETE.equals(queueAction))
    {
      if ((PluginQueueHeader.STATUS_KO.equals(response)) && (!Boolean.parseBoolean(ignore)))
      {
        applicationService.changeApplicationStatus(ApplicationStatus.DELETE_ON_ERROR, instanceId);

      }
      else if ((PluginQueueHeader.STATUS_OK.equals(response))
          || ((PluginQueueHeader.STATUS_KO.equals(response)) && (Boolean.parseBoolean(ignore))))
      {
        final String projectId = (String) pExchange.getIn().getHeader(PluginQueueHeader.PROJECT_ID_HEADER);
        final String toolInstanceId = (String) pExchange.getIn().getHeader(
            PluginQueueHeader.TOOL_INSTANCE_UUID_HEADER);
        applicationService.finalizeDeleteApplication(projectId, instanceId, toolInstanceId);
      }
    }
  }
}
