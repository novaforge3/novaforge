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
package org.novaforge.forge.core.plugins.entity;

import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceQueues;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lamirang
 */
@Entity
@Table(name = "PLUGIN_QUEUE")
public class PluginQueuesEntity implements PluginPersistenceQueues
{

  /**
	 * 
	 */
  private static final long serialVersionUID = -633178982651052925L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private long              id;

  @Column(name = "project_queue", nullable = false)
  private String            projectQueue;

  @Column(name = "user_queue", nullable = false)
  private String            userQueue;

  @Column(name = "membership_queue", nullable = false)
  private String            membershipQueue;

  @Column(name = "roles_mapping_queue", nullable = false)
  private String            rolesMappingQueue;

  /**
   * Optional group plugin queue
   */
  @Column(name = "group_queue", nullable = true)
  private String            groupQueue;

  /**
   * Empty constructor needed by JPA2
   */
  public PluginQueuesEntity()
  {
    // Used by JPA
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProjectQueue()
  {
    return projectQueue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectQueue(final String pProjectQueue)
  {
    projectQueue = pProjectQueue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUserQueue()
  {
    return userQueue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserQueue(final String pUserQueue)
  {
    userQueue = pUserQueue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMembershipQueue()
  {
    return membershipQueue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMembershipQueue(final String pMembershipQueue)
  {
    membershipQueue = pMembershipQueue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRolesMappingQueue()
  {

    return rolesMappingQueue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRolesMappingQueue(final String pRolesMappingQueue)
  {

    rolesMappingQueue = pRolesMappingQueue;
  }

  /**
   * @return the groupQueue
   */
  @Override
  public String getGroupQueue()
  {
    return groupQueue;
  }

  /**
   * @param groupQueue
   *          the groupQueue to set
   */
  @Override
  public void setGroupQueue(final String groupQueue)
  {
    this.groupQueue = groupQueue;
  }

}
