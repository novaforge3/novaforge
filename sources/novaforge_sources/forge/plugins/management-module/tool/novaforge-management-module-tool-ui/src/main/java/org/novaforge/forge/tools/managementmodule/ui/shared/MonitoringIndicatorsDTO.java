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
package org.novaforge.forge.tools.managementmodule.ui.shared;

import java.io.Serializable;

public class MonitoringIndicatorsDTO implements Serializable{

   /** Unique ID for serialization */
   private static final long serialVersionUID = -5954213508110208781L;

   /** The number of people which have worked */
   private int activeUsersNumber;
   
   /** The total consumed time in days */
   private float consumed;
   
   /** The average focalisation factor  */
   private float focalisation;
   
   /** The average velocity */
   private float velocity;
   
   /** The average error estimation */
   private float averageEstimationError;
   
   /** The advancement in % */
   private float advancement;

   /**
    * Get the activeUsersNumber
    * @return the activeUsersNumber
    */
   public int getActiveUsersNumber() {
      return activeUsersNumber;
   }

   /**
    * Set the activeUsersNumber
    * @param activeUsersNumber the activeUsersNumber to set
    */
   public void setActiveUsersNumber(int activeUsersNumber) {
      this.activeUsersNumber = activeUsersNumber;
   }

   /**
    * Get the consumed
    * @return the consumed
    */
   public float getConsumed() {
      return consumed;
   }

   /**
    * Set the consumed
    * @param consumed the consumed to set
    */
   public void setConsumed(float consumed) {
      this.consumed = consumed;
   }

   /**
    * Get the focalisation
    * @return the focalisation
    */
   public float getFocalisation() {
      return focalisation;
   }

   /**
    * Set the focalisation
    * @param focalisation the focalisation to set
    */
   public void setFocalisation(float focalisation) {
      this.focalisation = focalisation;
   }

   /**
    * Get the velocity
    * @return the velocity
    */
   public float getVelocity() {
      return velocity;
   }

   /**
    * Set the velocity
    * @param velocity the velocity to set
    */
   public void setVelocity(float velocity) {
      this.velocity = velocity;
   }

   /**
    * Get the averageEstimationError
    * @return the averageEstimationError
    */
   public float getAverageEstimationError() {
      return averageEstimationError;
   }

   /**
    * Set the averageEstimationError
    * @param averageEstimationError the averageEstimationError to set
    */
   public void setAverageEstimationError(float averageEstimationError) {
      this.averageEstimationError = averageEstimationError;
   }

   /**
    * Get the advancement
    * @return the advancement
    */
   public float getAdvancement() {
      return advancement;
   }

   /**
    * Set the advancement
    * @param advancement the advancement to set
    */
   public void setAdvancement(float advancement) {
      this.advancement = advancement;
   }
   
   
}
