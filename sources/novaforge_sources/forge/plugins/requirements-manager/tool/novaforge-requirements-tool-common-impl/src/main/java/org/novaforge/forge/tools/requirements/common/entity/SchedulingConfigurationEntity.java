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
package org.novaforge.forge.tools.requirements.common.entity;

import org.novaforge.forge.tools.requirements.common.model.scheduling.SchedulingConfiguration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Guillaume Morin
 */

@Entity
@Table(name = "SCHEDULE")
@NamedQueries({
    @NamedQuery(name = "SchedulingConfigurationEntity.findScheduleConfigurationByProjectID",
        query = "SELECT s FROM  SchedulingConfigurationEntity s  WHERE s.projectId=:REF"),
    @NamedQuery(name = "SchedulingConfigurationEntity.findAllScheduleConfiguration",
        query = "SELECT s FROM  SchedulingConfigurationEntity s") })
public class SchedulingConfigurationEntity implements SchedulingConfiguration, Serializable
{

  private static final long serialVersionUID = -4231855851645066265L;

  @SuppressWarnings("unused")
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "project_id", nullable = false)
  private String            projectId;

  @Column(name = "user_id", nullable = false)
  private String            userId;

  @Column(name = "hour", nullable = false)
  private Integer           hour;

  @Column(name = "minute", nullable = false)
  private Integer           minute;

  @Column(name = "period", nullable = false)
  private Integer           period;

  @Column(name = "active_status", nullable = false)
  private boolean           activate;

  public SchedulingConfigurationEntity()
  {

  }

  @Override
  public String getProjectId()
  {
    return projectId;
  }

  @Override
  public void setProjectId(final String pProjectId)
  {
    projectId = pProjectId;
  }

  @Override
  public String getUserId()
  {
    return userId;
  }

  @Override
  public void setUserId(final String pUserId)
  {
    userId = pUserId;
  }

  @Override
  public boolean isActive()
  {
    return activate;
  }

  @Override
  public void setActive(final boolean pActivate)
  {
    activate = pActivate;
  }

  @Override
  public Integer getLaunchHour()
  {
    return hour;
  }

  @Override
  public void setLaunchHour(final Integer pHour)
  {
    hour = pHour;
  }

  @Override
  public Integer getLaunchMinute()
  {
    return minute;
  }

  @Override
  public void setLaunchMinute(final Integer pMinute)
  {
    minute = pMinute;
  }

  @Override
  public Integer getLaunchPeriod()
  {
    return period;
  }

  @Override
  public void setLaunchPeriod(final Integer pPeriod)
  {
    period = pPeriod;
  }

  @Override
  public String toString()
  {
    return "[ project= " + projectId + ", user=" + userId + " (" + hour + "," + minute + " :period=" + period
        + " activate= " + activate + ") ]";
  }

}
