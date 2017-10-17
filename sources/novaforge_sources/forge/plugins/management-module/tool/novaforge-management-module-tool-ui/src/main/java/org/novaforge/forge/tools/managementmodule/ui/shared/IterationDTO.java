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
import java.util.Date;
import java.util.Set;


/**
 * The iteration DTO
 */
public class IterationDTO implements Serializable
{


	/** UID for serialization */
	private static final long serialVersionUID = 19861769018L;

	private long			  iterationId;
	private int			  	  numIteration;
	private String            label;
	private Date              startDate;
	private Date              endDate;
	private PhaseTypeDTO	   	  phaseType;
	private LotDTO			  lot;
	private boolean isFinished;	
	private Set<IterationTaskDTO> iterationTasks;
	private boolean isComplex;

	public IterationDTO(){
		super();
	}

	public long getIterationId() {
		return iterationId;
	}


	public void setIterationId(long iterationId) {
		this.iterationId = iterationId;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public PhaseTypeDTO getPhaseType() {
		return phaseType;
	}

	public void setPhaseType(PhaseTypeDTO phase) {
		this.phaseType = phase;
	}

	public int getNumIteration() {
		return numIteration;
	}

	public void setNumIteration(int numIteration) {
		this.numIteration = numIteration;
	}

	public LotDTO getLot() {
		return lot;
	}

	public void setLot(LotDTO lot) {
		this.lot = lot;
	}

	/**
	 * @return the iterationTasks
	 */
	public Set<IterationTaskDTO> getIterationTasks() {
		return iterationTasks;
	}

	/**
	 * @param iterationTasks the iterationTasks to set
	 */
	public void setIterationTasks(Set<IterationTaskDTO> iterationTasks) {
		this.iterationTasks = iterationTasks;
	}

	/**
	 * @return the isComplex
	 */
	public boolean isComplex() {
		return isComplex;
	}

	/**
	 * @param isComplex the isComplex to set
	 */
	public void setComplex(boolean isComplex) {
		this.isComplex = isComplex;
	}
	

	
}
