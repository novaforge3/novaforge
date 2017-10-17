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
package org.novaforge.forge.tools.managementmodule.entity;

import org.novaforge.forge.tools.managementmodule.domain.AdjustFactor;
import org.novaforge.forge.tools.managementmodule.domain.AdjustFactorJointure;
import org.novaforge.forge.tools.managementmodule.domain.AdjustWeight;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author ppr
 */
@Entity
@Table(name = "adjust_factor_jointure")
@NamedQuery(name = "AdjustFactorJointureEntity.findByFunctionalId", query = "SELECT p FROM AdjustFactorJointureEntity p WHERE p.adjustFactor.functionalId = :functionalId")
public class AdjustFactorJointureEntity implements AdjustFactorJointure, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7503001284542692562L;
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectPlanEntity.class)
	private ProjectPlan			 projectPlan;

	@OneToOne(targetEntity = AdjustFactorEntity.class)
	private AdjustFactor			adjustFactor;

	@OneToOne(targetEntity = AdjustWeightEntity.class)
	private AdjustWeight			adjustWeight;

	/**
	 * @return the id
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id
	 *          the id to set
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * @return the projectPlan
	 */
	@Override
	public ProjectPlan getProjectPlan()
	{
		return projectPlan;
	}

	/**
	 * @param projectPlan
	 *          the projectPlan to set
	 */
	@Override
	public void setProjectPlan(final ProjectPlan projectPlan)
	{
		this.projectPlan = projectPlan;
	}

	/**
	 * @return the adjustFactor
	 */
	@Override
	public AdjustFactor getAdjustFactor()
	{
		return adjustFactor;
	}

	/**
	 * @param adjustFactor
	 *          the adjustFactor to set
	 */
	@Override
	public void setAdjustFactor(final AdjustFactor adjustFactor)
	{
		this.adjustFactor = adjustFactor;
	}

	/**
	 * @return the adjustWeight
	 */
	@Override
	public AdjustWeight getAdjustWeight()
	{
		return adjustWeight;
	}

	/**
	 * @param adjustWeight
	 *          the adjustWeight to set
	 */
	@Override
	public void setAdjustWeight(final AdjustWeight adjustWeight)
	{
		this.adjustWeight = adjustWeight;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, projectPlan, adjustFactor, adjustWeight);
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		final AdjustFactorJointureEntity that = (AdjustFactorJointureEntity) o;
		return Objects.equals(id, that.id) &&
							 Objects.equals(projectPlan, that.projectPlan) &&
							 Objects.equals(adjustFactor, that.adjustFactor) &&
							 Objects.equals(adjustWeight, that.adjustWeight);
	}

	/*
		 * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
	@Override
	public String toString()
	{
		return "AdjustFactorJointureEntity [id=" + id + ", projectPlan=" + projectPlan.getId() + ", adjustFactor="
							 + adjustFactor.getFunctionalId() + ", adjustWeight=" + adjustWeight.getFunctionalId() + "]";
	}

}
