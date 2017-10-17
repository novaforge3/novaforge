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

package org.novaforge.forge.ui.distribution.shared.diffusion;

import java.io.Serializable;

public class SynchDifferedDTO implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 5956282952605319919L;
   private boolean           isActive;
   private String            hours;

   private String            minutes;

   private String            period;

   /**
    * 
    */
   public SynchDifferedDTO()
   {
      super();
   }

   /**
    * @param isActive
    * @param forgeId
    * @param time
    * @param description
    * @param period
    */
   public SynchDifferedDTO(boolean isActive, String pHours, String pMinutes, String pPeriod)
   {
      super();
      this.isActive = isActive;
      this.hours = pHours;
      this.minutes = pMinutes;
      this.period = pPeriod;
   }

   public boolean isActive()
   {
      return isActive;
   }

   public void setActive(boolean isActive)
   {
      this.isActive = isActive;
   }

   public String getHours()
   {
      return hours;
   }

   public void setHours(String hours)
   {
      this.hours = hours;
   }

   public String getMinutes()
   {
      return minutes;
   }

   public void setMinutes(String minutes)
   {
      this.minutes = minutes;
   }

   public String getPeriod()
   {
      return period;
   }

   public void setPeriod(String period)
   {
      this.period = period;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((hours == null) ? 0 : hours.hashCode());
      result = prime * result + (isActive ? 1231 : 1237);
      result = prime * result + ((minutes == null) ? 0 : minutes.hashCode());
      result = prime * result + ((period == null) ? 0 : period.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      SynchDifferedDTO other = (SynchDifferedDTO) obj;
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

   @Override
   public String toString()
   {
      return "SynchDifferedDTO [isActive=" + isActive + ", hours=" + hours + ", minutes=" + minutes + ", period="
                 + period + "]";
   }

}
