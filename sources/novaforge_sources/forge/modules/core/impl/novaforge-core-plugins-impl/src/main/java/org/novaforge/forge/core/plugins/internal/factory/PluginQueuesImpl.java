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
package org.novaforge.forge.core.plugins.internal.factory;

import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceQueues;
import org.novaforge.forge.core.plugins.domain.route.PluginQueues;

/**
 * @author lamirang
 */
public class PluginQueuesImpl implements PluginQueues
{

  /**
	 * 
	 */
  private static final long serialVersionUID = 4110191205435043066L;
  private final String      projectQueue;
  private final String      membershipQueue;
  private final String      userQueue;
  private final String      rolesMappingQueue;

  /**
   * @param pProjectQueue
   * @param pMembershipQueue
   * @param pUserQueue
   * @param pRolesMappingQueue
   */
  public PluginQueuesImpl(final String pProjectQueue, final String pMembershipQueue, final String pUserQueue,
      final String pRolesMappingQueue)
  {
    projectQueue = pProjectQueue;
    membershipQueue = pMembershipQueue;
    userQueue = pUserQueue;
    rolesMappingQueue = pRolesMappingQueue;
  }

  /**
   * @param pJmsQueues
   */
  public PluginQueuesImpl(final PluginPersistenceQueues pJmsQueues)
  {
    projectQueue = pJmsQueues.getProjectQueue();
    membershipQueue = pJmsQueues.getMembershipQueue();
    userQueue = pJmsQueues.getUserQueue();
    rolesMappingQueue = pJmsQueues.getRolesMappingQueue();
  }

  /**
   * @return the projectQueue
   */
  @Override
  public String getProjectQueue()
  {
    return projectQueue;
  }

  /**
   * @return the userQueue
   */
  @Override
  public String getUserQueue()
  {
    return userQueue;
  }

  /**
   * @return the membershipQueue
   */
  @Override
  public String getMembershipQueue()
  {
    return membershipQueue;
  }

  /**
   * @return the rolesMappingQueue
   */
  @Override
  public String getRolesMappingQueue()
  {
    return rolesMappingQueue;
  }

}
