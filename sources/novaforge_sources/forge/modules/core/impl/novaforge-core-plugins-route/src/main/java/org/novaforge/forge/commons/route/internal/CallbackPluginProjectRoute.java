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
package org.novaforge.forge.commons.route.internal;

import org.novaforge.commons.route.util.RouteUtil;
import org.novaforge.forge.commons.processor.CallbackPluginProcessor;
import org.novaforge.forge.commons.processor.CallbackPluginProjectProcessor;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.core.organization.services.ApplicationService;

/**
 * @author sbenoist
 * @date Mar 2, 2011 This class is responsible for handling responses from plugin project propagations
 */
public class CallbackPluginProjectRoute extends AbstractCallbackPluginRoute
{
  private ApplicationService applicationService;

  @Override
  protected String getCallbackQueueName()
  {
    return RouteUtil.FORGE_CALLBACK_PROJECT_QUEUE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getCallbackRouteName()
  {
    return RouteUtil.FORGE_CALLBACK_PROJECT_ROUTE_NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected EventType getEventType()
  {
    return EventType.PROPAGATION_FOR_PROJECT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected CallbackPluginProcessor getProcessor()
  {
    return new CallbackPluginProjectProcessor(historizationService, getEventType(), applicationService);
  }

  public void setApplicationService(final ApplicationService pApplicationService)
  {
    applicationService = pApplicationService;
  }

}
