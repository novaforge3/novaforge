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
package org.novaforge.forge.tools.managementmodule.ui.shared;

import java.io.Serializable;

/**
 * @author BILET-JC
 *
 */
public class ScopeUnitMonitoringDTO implements Serializable {

	/**
	 * Unique ID for serialization
	 */
	private static final long serialVersionUID = -874660639977801058L;
	/**
	 * The advancement of the work we have to done on the iteration
	 */
	float advancement;
	/**
	 * The re-estimate work on iteration
	 */
	float reestimate;
	private String unitId;
	private String scopeUnitName;
	private String parentScopeUnitName;
	private String parentScopeUnitId;
	private float  consumed;
	private float  remainingTasks;

	/**
	 * @return the unitId
	 */
	public String getUnitId()
	{
		return unitId;
	}

	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(String unitId)
	{
		this.unitId = unitId;
	}

	/**
	 * @return the scopeUnitName
	 */
	public String getScopeUnitName()
	{
		return scopeUnitName;
	}

	/**
	 * @param scopeUnitName the scopeUnitName to set
	 */
	public void setScopeUnitName(String scopeUnitName)
	{
		this.scopeUnitName = scopeUnitName;
	}

	/**
	 * @return the parentScopeUnitName
	 */
	public String getParentScopeUnitName()
	{
		if (parentScopeUnitName != null)
		{
			return parentScopeUnitName;
		}
		return "";
	}

	/**
	 * @param parentScopeUnitName the parentScopeUnitName to set
	 */
	public void setParentScopeUnitName(String parentScopeUnitName)
	{
		this.parentScopeUnitName = parentScopeUnitName;
	}

	/**
	 * @return the parentScopeUnitId
	 */
	public String getParentScopeUnitId()
	{
		return parentScopeUnitId;
	}

	/**
	 * @param parentScopeUnitId
	 *     the parentScopeUnitId to set
	 */
	public void setParentScopeUnitId(String parentScopeUnitId)
	{
		this.parentScopeUnitId = parentScopeUnitId;
	}

	/**
	 * @return the consumed
	 */
	public Float getConsumed()
	{
		return consumed;
	}

	/**
	 * @param consumed the consumed to set
	 */
	public void setConsumed(Float consumed)
	{
		this.consumed = consumed;
	}

	/**
	 * @return the remainingTasks
	 */
	public Float getRemainingTasks()
	{
		return remainingTasks;
	}

	/**
	 * @param remainingTasks the remainingTasks to set
	 */
	public void setRemainingTasks(Float remainingTasks) {
		this.remainingTasks = remainingTasks;
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

   /**
    * Get the reestimate
    * @return the reestimate
    */
   public float getReestimate() {
      return reestimate;
   }

   /**
    * Set the reestimate
    * @param reestimate the reestimate to set
    */
   public void setReestimate(float reestimate) {
      this.reestimate = reestimate;
   }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
         return true;
      }
		if (obj == null) {
         return false;
      }
		if (getClass() != obj.getClass()) {
         return false;
      }
		ScopeUnitMonitoringDTO other = (ScopeUnitMonitoringDTO) obj;
		if (unitId == null) {
			if (other.unitId != null) {
            return false;
         }
		} else if (!unitId.equals(other.unitId)) {
         return false;
      }
		return true;
	}

   @Override
   public String toString() {
      return "ScopeUnitMonitoringDTO [unitId=" + unitId + ", scopeUnitName=" + scopeUnitName
            + ", parentScopeUnitName=" + parentScopeUnitName + ", consumed=" + consumed + ", remainingTasks="
            + remainingTasks + "]";
   }
	
	
}
