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
import java.util.Date;

/**
 * @author BILET-JC
 * 
 */
public class IterationTaskDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -499920311456054301L;

	private Long id;
	// private String responsible;
	private TaskDTO task;
	private float consumedTime;
	private float remainingTime;
	private float reestimate;
	private float advancement;
	private float errorEstimation;
	private Date lastUpdateTime;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	// /**
	// * @return the responsible
	// */
	// public String getResponsible() {
	// return responsible;
	// }
	// /**
	// * @param responsible the responsible to set
	// */
	// public void setResponsible(String responsible) {
	// this.responsible = responsible;
	// }
	/**
	 * @return the task
	 */
	public TaskDTO getTask() {
		return task;
	}

	/**
	 * @param task
	 *            the task to set
	 */
	public void setTask(TaskDTO task) {
		this.task = task;
	}

	/**
	 * @return the consumedTime
	 */
	public float getConsumedTime() {
		return consumedTime;
	}

	/**
	 * @param consumedTime
	 *            the consumedTime to set
	 */
	public void setConsumedTime(float consumedTime) {
		this.consumedTime = consumedTime;
	}

	/**
	 * @return the remainingTime
	 */
	public float getRemainingTime() {
		return remainingTime;
	}

	/**
	 * @param remainingTime
	 *            the remainingTime to set
	 */
	public void setRemainingTime(float remainingTime) {
		this.remainingTime = remainingTime;
	}

	/**
	 * @return the reestimate
	 */
	public float getReestimate() {
		return reestimate;
	}

	/**
	 * @param reestimate the reestimate to set
	 */
	public void setReestimate(float reestimate) {
		this.reestimate = reestimate;
	}

	/**
	 * @return the advancement
	 */
	public float getAdvancement() {
		return advancement;
	}

	/**
	 * @param advancement the advancement to set
	 */
	public void setAdvancement(float advancement) {
		this.advancement = advancement;
	}

	/**
	 * @return the errorEstimation
	 */
	public float getErrorEstimation() {
		return errorEstimation;
	}

	/**
	 * @param errorEstimation the errorEstimation to set
	 */
	public void setErrorEstimation(float errorEstimation) {
		this.errorEstimation = errorEstimation;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime
	 *            the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	// we can use id for equals/hashcode cause it always have value (no creation
	// in ihm)
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		IterationTaskDTO other = (IterationTaskDTO) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
