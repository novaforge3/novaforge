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
package org.novaforge.forge.core.organization.internal.route;

import org.apache.camel.Handler;
import org.apache.camel.Properties;
import org.novaforge.forge.core.organization.route.OrganizationQueueHeader;
import org.novaforge.forge.core.organization.route.OrganizationQueueName;

import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class ApplicationsDynamicRouter
{

  /**
   * This method is used to route message on each application
   * 
   * @param pProperties
   *          the exchange properties
   * @return target route
   */
  @Handler
  public String route(@Properties final Map<String, Object> pProperties)
  {
    String targetRoute = null;
    // get the state from the exchange properties and keep track how many times
    // we have been invoked
    int invoked = 0;
    final Object current = pProperties.get(OrganizationQueueHeader.APPLICATIONS_NUMBER);
    if (current != null)
    {
      invoked = Integer.valueOf(current.toString());
      invoked--;
    }
    // and store the state back on the properties
    pProperties.put(OrganizationQueueHeader.APPLICATIONS_NUMBER, invoked);
    if (invoked != -1)
    {
      targetRoute = OrganizationQueueName.PROJECT_DELETE_APPLICATION_QUEUE;
    }

    // no more so return null
    return targetRoute;

  }
}
