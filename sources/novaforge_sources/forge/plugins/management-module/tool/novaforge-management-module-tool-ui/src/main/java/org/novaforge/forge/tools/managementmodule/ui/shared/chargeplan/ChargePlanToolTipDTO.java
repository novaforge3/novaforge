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
package org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan;

import java.io.Serializable;
import java.util.Date;

/**
 * Contains data for tooltip on a column for the chargePlanView
 * @author FALSQUELLE-E
 *
 */
public class ChargePlanToolTipDTO implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = -6459037180666195306L;

   private Date date;
   
   private String lotNames;
   private String childLotNames;
   private String phaseNames;
   private String iterationNames;
   
  

   public ChargePlanToolTipDTO() {
      super();
   }

   public Date getDate() {
      return date;
   }

   public void setDate(final Date date) {
      this.date = date;
   }
   
   
   /**
    * @return the lotNames
    */
   public String getLotNames()
   {
     return lotNames;
   }

   /**
    * @param lotNames 
    *    the lotNames to set
    */
   public void setLotNames(String lotNames)
   {
     this.lotNames = lotNames;
   }

   /**
    * @return the childLotNames
    */
   public String getChildLotNames()
   {
     return childLotNames;
   }

   /**
    * @param childLotNames 
    *    the childLotNames to set
    */
   public void setChildLotNames(String childLotNames)
   {
     this.childLotNames = childLotNames;
   }

   /**
    * @return the phaseNames
    */
   public String getPhaseNames()
   {
     return phaseNames;
   }

   /**
    * @param phaseNames 
    *    the phaseNames to set
    */
   public void setPhaseNames(String phaseNames)
   {
     this.phaseNames = phaseNames;
   }

   /**
    * @return the iterationNames
    */
   public String getIterationNames()
   {
     return iterationNames;
   }

   /**
    * @param iterationNames 
    *    the iterationNames to set
    */
   public void setIterationNames(String iterationNames)
   {
     this.iterationNames = iterationNames;
   }

 
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((date == null) ? 0 : date.hashCode());
      result = prime * result + ((childLotNames == null) ? 0 : childLotNames.hashCode());
      result = prime * result + ((iterationNames == null) ? 0 : iterationNames.hashCode());
      result = prime * result + ((lotNames == null) ? 0 : lotNames.hashCode());
      result = prime * result + ((phaseNames == null) ? 0 : phaseNames.hashCode());  
      return result;
   }

   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final ChargePlanToolTipDTO other = (ChargePlanToolTipDTO) obj;
      if (date == null) {
         if (other.date != null) {
            return false;
         }
      } else if (!date.equals(other.date)) {
         return false;
      }
      if (childLotNames == null) {
         if (other.childLotNames != null) {
            return false;
         }
      } else if (!childLotNames.equals(other.childLotNames)) {
         return false;
      }
     
      if (iterationNames == null) {
         if (other.iterationNames != null) {
            return false;
         }
      } else if (!iterationNames.equals(other.iterationNames)) {
         return false;
      }
      if (lotNames == null) {
         if (other.lotNames != null) {
            return false;
         }
      } else if (!lotNames.equals(other.lotNames)) {
         return false;
      }
      if (phaseNames == null) {
         if (other.phaseNames != null) {
            return false;
         }
      } else if (!phaseNames.equals(other.phaseNames)) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "ChargePlanToolTipDTO [date=" + date 
            + ", lotNames=" + lotNames
            + ", childLotNames=" + childLotNames 
            + ", phaseNames=" + phaseNames 
            + ", iterationNames="+ iterationNames  + "]";
   }

  /**
   * @return the serialversionuid
   */
  public static long getSerialversionuid()
  {
    return serialVersionUID;
  }

}
