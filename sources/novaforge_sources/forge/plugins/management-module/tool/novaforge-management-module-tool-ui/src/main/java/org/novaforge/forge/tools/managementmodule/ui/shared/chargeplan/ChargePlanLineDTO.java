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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author Falsquelle-e
 * 
 */
public class ChargePlanLineDTO implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = -6364706124808512492L;

   private Long idChargePlanLine;
   private Long idProjectPlan;
   private String disciplineFunctionalId;
   private String disciplineName;
   private int disciplineOrder;
   private float totalLoad;
   private float verifiedLoad;
   private float remainingLoad;

   private Map<Date, Float> loadsByDate;

   public ChargePlanLineDTO() {
      super();
   }

   public Long getIdChargePlanLine() {
      return idChargePlanLine;
   }

   public void setIdChargePlanLine(Long idChargePlanLine) {
      this.idChargePlanLine = idChargePlanLine;
   }

   public Long getIdProjectPlan() {
      return idProjectPlan;
   }

   public void setIdProjectPlan(Long idProjectPlan) {
      this.idProjectPlan = idProjectPlan;
   }

   public String getIdDiscipline() {
      return disciplineFunctionalId;
   }

   public void setIdDiscipline(String functionalId) {
      this.disciplineFunctionalId = functionalId;
   }

   public String getDisciplineName() {
      return disciplineName;
   }

   public void setDisciplineName(String disciplineName) {
      this.disciplineName = disciplineName;
   }

   public Map<Date, Float> getLoadsByDate() {
      return loadsByDate;
   }

   public void setLoadsByDate(Map<Date, Float> loadsByDate) {
      this.loadsByDate = loadsByDate;
   }

   public float getTotalLoad() {
      return totalLoad;
   }

   public void setTotalLoad(float totalLoad) {
      this.totalLoad = totalLoad;
   }

   public float getVerifiedLoad() {
      return verifiedLoad;
   }

   public void setVerifiedLoad(float verifiedLoad) {
      this.verifiedLoad = verifiedLoad;
   }

   public float getRemainnigLoad() {
      return remainingLoad;
   }

   public void setRemainingLoad(float remainnigLoad) {
      this.remainingLoad = remainnigLoad;
   }

   public int getDisciplineOrder() {
      return disciplineOrder;
   }

   public void setDisciplineOrder(int disciplineOrder) {
      this.disciplineOrder = disciplineOrder;
   }
}
