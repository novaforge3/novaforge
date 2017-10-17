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

public class ScopeUnitGlobalMonitoringDTO extends ScopeUnitMonitoringDTO {

	/** Unique ID for serialization */
	private static final long serialVersionUID = -8465803494837379894L;

	private String lotName;
	private String parentLotName;
	private Integer projectPlanEstimation;
	private Float remainingScopeUnit;
	private Integer weight;
	private Integer risk;
	private Integer benefit;
	private Integer injury;
	private ScopeUnitMonitoringStatusEnum status;
	private boolean hasAllTaskFinished;
	private boolean hasAllScopeUnitDisciplineFinished;
	private boolean hasAllChildFinished;

	/**
	 * @return the estimate
	 */
	public Integer getProjectPlanEstimation() {
		return projectPlanEstimation;
	}

	/**
	 * @param estimate
	 *            the estimate to set
	 */
	public void setProjectPlanEstimation(Integer estimate) {
		this.projectPlanEstimation = estimate;
	}

	/**
	 * @return the remainingScopeUnit
	 */
	public Float getRemainingScopeUnit() {
		return remainingScopeUnit;
	}

	/**
	 * @param remainingScopeUnit
	 *            the remainingScopeUnit to set
	 */
	public void setRemainingScopeUnit(Float remainingScopeUnit) {
		this.remainingScopeUnit = remainingScopeUnit;
	}

	/**
	 * @return the weight
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * @return the risk
	 */
	public Integer getRisk() {
		return risk;
	}

	/**
	 * @param risk
	 *            the risk to set
	 */
	public void setRisk(Integer risk) {
		this.risk = risk;
	}

	/**
	 * @return the benefit
	 */
	public Integer getBenefit() {
		return benefit;
	}

	/**
	 * @param benefit
	 *            the benefit to set
	 */
	public void setBenefit(Integer benefit) {
		this.benefit = benefit;
	}

	/**
	 * @return the injury
	 */
	public Integer getInjury() {
		return injury;
	}

	/**
	 * @param injury
	 *            the injury to set
	 */
	public void setInjury(Integer injury) {
		this.injury = injury;
	}

	/**
	 * @return the status
	 */
	public ScopeUnitMonitoringStatusEnum getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(ScopeUnitMonitoringStatusEnum status) {
		this.status = status;
	}

	/**
	 * @return the hasAllTaskFinished
	 */
	public boolean hasAllTaskFinished() {
		return hasAllTaskFinished;
	}

	/**
	 * @param hasAllTaskFinished
	 *            the hasAllTaskFinished to set
	 */
	public void setAllTaskFinished(boolean hasAllTaskFinished) {
		this.hasAllTaskFinished = hasAllTaskFinished;
	}

	/**
	 * @param hasAllScopeUnitDisciplineFinished
	 *            the hasAllScopeUnitDisciplineFinished to set
	 */
	public void setAllScopeUnitDisciplineFinished(boolean hasAllScopeUnitDisciplineFinished) {
		this.hasAllScopeUnitDisciplineFinished = hasAllScopeUnitDisciplineFinished;
	}

	/**
	 * @return the hasAllScopeUnitDisciplineFinished
	 */
	public boolean hasAllScopeUnitDisciplineFinished() {
		return hasAllScopeUnitDisciplineFinished;
	}

	/**
	 * @param hasAllChildFinished
	 *            the hasAllChildFinished to set
	 */
	public void setAllChildFinished(boolean hasAllChildFinished) {
		this.hasAllChildFinished = hasAllChildFinished;
	}

	/**
	 * @return the hasAllChildFinished
	 */
	public boolean hasAllChildFinished() {
		return hasAllChildFinished;
	}

	/**
	 * @return the lotName
	 */
	public String getLotName() {
		return lotName;
	}

	/**
	 * @param lotName
	 *            the lotName to set
	 */
	public void setLotName(String lotName) {
		this.lotName = lotName;
	}

	/**
	 * @return the parentLotName
	 */
	public String getParentLotName() {
		return parentLotName;
	}

	/**
	 * @param parentLotName
	 *            the parentLotName to set
	 */
	public void setParentLotName(String parentLotName) {
		this.parentLotName = parentLotName;
	}
}
