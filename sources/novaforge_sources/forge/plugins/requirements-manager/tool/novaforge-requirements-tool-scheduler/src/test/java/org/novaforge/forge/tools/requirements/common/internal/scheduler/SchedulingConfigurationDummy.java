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
package org.novaforge.forge.tools.requirements.common.internal.scheduler;

import org.novaforge.forge.tools.requirements.common.model.scheduling.SchedulingConfiguration;

/**
 * @author Guillaume Morin
 */
public class SchedulingConfigurationDummy implements SchedulingConfiguration
{

  private String  fprojectID;
  private String  fUserID;
  private boolean fActive;
  private Integer fLaunchHour;
  private Integer fLaunchMinute;
  private Integer fPeriod;

  @Override
  public String getProjectId()
  {
    return fprojectID;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectId(final String pProjectId)
  {
    fprojectID = pProjectId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUserId()
  {
    return fUserID;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserId(final String pUserId)
  {
    fUserID = pUserId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isActive()
  {
    return fActive;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActive(final boolean pActive)
  {
    fActive = pActive;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getLaunchHour()
  {
    return fLaunchHour;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLaunchHour(final Integer pLaunchHour)
  {
    fLaunchHour = pLaunchHour;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getLaunchMinute()
  {
    return fLaunchMinute;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLaunchMinute(final Integer pLaunchMinute)
  {
    fLaunchMinute = pLaunchMinute;
  }

  @Override
  public Integer getLaunchPeriod()
  {
    return fPeriod;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLaunchPeriod(final Integer pPeriod)
  {
    fPeriod = pPeriod;
  }
}
