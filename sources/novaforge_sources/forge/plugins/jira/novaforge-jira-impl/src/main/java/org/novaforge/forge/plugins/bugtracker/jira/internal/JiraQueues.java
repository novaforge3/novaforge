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
package org.novaforge.forge.plugins.bugtracker.jira.internal;

import org.novaforge.forge.core.plugins.domain.route.PluginQueues;
import org.novaforge.forge.plugins.bugtracker.jira.route.JiraQueueName;

/**
 * @author Guillaume Lamirand
 */
public class JiraQueues implements PluginQueues
{

  /**
   * 
   */
  private static final long serialVersionUID = 3044545876910603907L;

  /**
   * @return the projectQueue
   */
  @Override
  public String getProjectQueue()
  {
    return JiraQueueName.PROJECT.getQueueName();
  }

  /**
   * @return the userQueue
   */
  @Override
  public String getUserQueue()
  {
    return JiraQueueName.USER.getQueueName();
  }

  /**
   * @return the membershipQueue
   */
  @Override
  public String getMembershipQueue()
  {
    return JiraQueueName.MEMBERSHIP.getQueueName();
  }

  /**
   * @return the rolesMappingQueue
   */
  @Override
  public String getRolesMappingQueue()
  {
    return JiraQueueName.ROLESMAPPING.getQueueName();
  }

}
