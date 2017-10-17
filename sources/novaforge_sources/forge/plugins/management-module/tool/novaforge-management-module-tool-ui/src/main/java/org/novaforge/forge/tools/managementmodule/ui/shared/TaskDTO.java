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
 * The DTO used to transmit tasks to UI
 */
public class TaskDTO implements Serializable {

	/** UID for serialization */
	private static final long serialVersionUID = -7004518383732094674L;

	/** The task id */
	private Long id;

	/** The task label */
	private String label;

	/** The initial estimation in days */
	private float initialEstimation;

	/** The start date */
	private Date startDate;

	/** The end date */
	private Date endDate;

	/** The estimated remaing time (in days) to finish the task */
	private float remainingTime;

	/** The time (in days) of work on this task */
	private float consumedTime;

	/** A commentary on the task */
	private String commentary;

	/** The assigned user of the this task */
	private UserDTO user;

	/** The iteration of the task */
	private IterationDTO iteration;

	/** The discipline of the task */
	private DisciplineDTO discipline;

	/** The category of the task */
	private TaskCategoryDTO category;

	/** The scope unit or null if TYPE bug */
	private ScopeUnitDTO scopeUnit;

	/** The TYPE of the task (bug or work) */
	private TaskTypeEnum type;

	/** The status of the task */
	private TaskStatusDTO status;

	/** The task description */
	private String description;

	/** The task bug */
	private BugDTO bug;

	/**
	 * Get the id
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the initialEstimation
	 */
	public float getInitialEstimation() {
		return initialEstimation;
	}

	/**
	 * @param initialEstimation
	 *            the initialEstimation to set
	 */
	public void setInitialEstimation(float initialEstimation) {
		this.initialEstimation = initialEstimation;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Get the remainingTime
	 * 
	 * @return the remainingTime
	 */
	public float getRemainingTime() {
		return remainingTime;
	}

	/**
	 * Set the remainingTime
	 * 
	 * @param remainingTime
	 *            the remainingTime to set
	 */
	public void setRemainingTime(float remainingTime) {
		this.remainingTime = remainingTime;
	}

	/**
	 * Get the consumedTime
	 * 
	 * @return the consumedTime
	 */
	public float getConsumedTime() {
		return consumedTime;
	}

	/**
	 * Set the consumedTime
	 * 
	 * @param consumedTime
	 *            the consumedTime to set
	 */
	public void setConsumedTime(float consumedTime) {
		this.consumedTime = consumedTime;
	}

	/**
	 * Get the commentary
	 * 
	 * @return the commentary
	 */
	public String getCommentary() {
		return commentary;
	}

	/**
	 * Set the commentary
	 * 
	 * @param commentary
	 *            the commentary to set
	 */
	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	/**
	 * Get the user
	 * 
	 * @return the user
	 */
	public UserDTO getUser() {
		return user;
	}

	/**
	 * Set the user
	 * 
	 * @param user
	 *            the user to set
	 */
	public void setUser(UserDTO user) {
		this.user = user;
	}

	/**
	 * Get the iteration
	 * 
	 * @return the iteration
	 */
	public IterationDTO getIteration() {
		return iteration;
	}

	/**
	 * Set the iteration
	 * 
	 * @param iteration
	 *            the iteration to set
	 */
	public void setIteration(IterationDTO iteration) {
		this.iteration = iteration;
	}

	/**
	 * Get the discipline
	 * 
	 * @return the discipline
	 */
	public DisciplineDTO getDiscipline() {
		return discipline;
	}

	/**
	 * Set the discipline
	 * 
	 * @param discipline
	 *            the discipline to set
	 */
	public void setDiscipline(DisciplineDTO discipline) {
		this.discipline = discipline;
	}

	/**
	 * Get the category
	 * 
	 * @return the category
	 */
	public TaskCategoryDTO getCategory() {
		return category;
	}

	/**
	 * Set the category
	 * 
	 * @param category
	 *            the category to set
	 */
	public void setCategory(TaskCategoryDTO category) {
		this.category = category;
	}

	/**
	 * Get the scopeUnit
	 * 
	 * @return the scopeUnit
	 */
	public ScopeUnitDTO getScopeUnit() {
		return scopeUnit;
	}

	/**
	 * Set the scopeUnit
	 * 
	 * @param scopeUnit
	 *            the scopeUnit to set
	 */
	public void setScopeUnit(ScopeUnitDTO scopeUnit) {
		this.scopeUnit = scopeUnit;
	}

	/**
	 * Get the TYPE
	 * 
	 * @return the TYPE
	 */
	public TaskTypeEnum getType() {
		return type;
	}

	/**
	 * Set the TYPE
	 * 
	 * @param TYPE
	 *            the TYPE to set
	 */
	public void setType(TaskTypeEnum type) {
		this.type = type;
	}

	/**
	 * Get the status
	 * 
	 * @return the status
	 */
	public TaskStatusDTO getStatus() {
		return status;
	}

	/**
	 * Set the status
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(TaskStatusDTO status) {
		this.status = status;
	}

	/**
	 * Get the description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the parentScopeUnitName or "" if the scope unit has no parent
	 * 
	 * @return
	 */
	public String getParentScopeUnitName() {
		if (scopeUnit != null && scopeUnit.getParentScopeUnit() != null) {
			return scopeUnit.getParentScopeUnit().getName();
		}
		return "";
	}

	/**
	 * Get the lot name of the task
	 * 
	 * @return the lot name
	 */
	public String getLotName() {
		return iteration.getLot().getName();
	}

	/**
	 * Get the parent of the lot's task or "" if the lot has no parent
	 * 
	 * @return the parent lot name or ""
	 */
	public String getParentLotName() {
		if (iteration.getLot().getParentLotName() != null) {
			return iteration.getLot().getParentLotName();
		}
		return "";
	}

	/**
	 * Get the bug
	 * 
	 * @return the bug
	 */
	public BugDTO getBug() {
		return bug;
	}

	/**
	 * Set the bug
	 * 
	 * @param bug
	 *            the bug to set
	 */
	public void setBug(BugDTO bug) {
		this.bug = bug;
	}

}
