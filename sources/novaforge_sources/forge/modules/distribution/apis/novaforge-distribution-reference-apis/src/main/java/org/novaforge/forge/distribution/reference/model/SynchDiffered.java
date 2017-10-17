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
package org.novaforge.forge.distribution.reference.model;

import org.novaforge.forge.commons.technical.historization.annotations.Historizable;

public class SynchDiffered
{
  private boolean isActive;
  private String  hours;

  private String  minutes;

  private String  period;

  /**
    * 
    */
  public SynchDiffered()
  {
    super();
  }

  public SynchDiffered(final boolean isActive, final String hours, final String minutes, final String period)
  {
    super();
    this.isActive = isActive;
    this.hours = hours;
    this.minutes = minutes;
    this.period = period;
  }

  public boolean isActive()
  {
    return isActive;
  }

  public void setActive(final boolean isActive)
  {
    this.isActive = isActive;
  }

  @Historizable(label = "hours")
  public String getHours()
  {
    return hours;
  }

  public void setHours(final String hours)
  {
    this.hours = hours;
  }

  @Historizable(label = "minutes")
  public String getMinutes()
  {
    return minutes;
  }

  public void setMinutes(final String minutes)
  {
    this.minutes = minutes;
  }

  @Historizable(label = "period")
  public String getPeriod()
  {
    return period;
  }

  public void setPeriod(final String period)
  {
    this.period = period;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((hours == null) ? 0 : hours.hashCode());
    result = (prime * result) + (isActive ? 1231 : 1237);
    result = (prime * result) + ((minutes == null) ? 0 : minutes.hashCode());
    result = (prime * result) + ((period == null) ? 0 : period.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof SynchDiffered))
    {
      return false;
    }
    final SynchDiffered other = (SynchDiffered) obj;
    if (hours == null)
    {
      if (other.hours != null)
      {
        return false;
      }
    }
    else if (!hours.equals(other.hours))
    {
      return false;
    }
    if (isActive != other.isActive)
    {
      return false;
    }
    if (minutes == null)
    {
      if (other.minutes != null)
      {
        return false;
      }
    }
    else if (!minutes.equals(other.minutes))
    {
      return false;
    }
    if (period == null)
    {
      if (other.period != null)
      {
        return false;
      }
    }
    else if (!period.equals(other.period))
    {
      return false;
    }
    return true;
  }

}
