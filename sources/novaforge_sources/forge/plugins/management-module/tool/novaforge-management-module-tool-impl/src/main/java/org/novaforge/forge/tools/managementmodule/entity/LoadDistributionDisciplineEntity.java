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

import org.novaforge.forge.tools.managementmodule.domain.LoadDistributionDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ProjectDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author falsquelle-e
 */
@Entity
@Table(name = "loadDistributionDiscipline")
@NamedQueries({
		@NamedQuery(name = "LoadDistributionDisciplineEntity.findByProjectPlanId", query = "SELECT ldd FROM LoadDistributionDisciplineEntity ldd WHERE ldd.projectPlan.id = :projectPlanId"),
		@NamedQuery(name = "LoadDistributionDisciplineEntity.getLoadsByDateByProjectPlanId", query = "select ldd.date, sum(ldd.load) from LoadDistributionDisciplineEntity ldd where ldd.projectPlan.id = :projectPlanId group by ldd.date order by ldd.date"),
		@NamedQuery(name = "LoadDistributionDisciplineEntity.deleteLoadsByProjectPlanId", query = "DELETE FROM LoadDistributionDisciplineEntity ldd where ldd.projectPlan.id = :projectPlanId") })
public class LoadDistributionDisciplineEntity implements LoadDistributionDiscipline, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3448756519920734070L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	/**
	 * name of this column is different than entity's field because "load" is reserved in SQL
	 */
	@Column(name = "loadValue", nullable = false)
	private Float						 load;

	@Column(name = "date", nullable = false)
	private Date							date;

	@ManyToOne(targetEntity = ProjectDisciplineEntity.class)
	private ProjectDiscipline projectDiscipline;

	@ManyToOne(targetEntity = ProjectPlanEntity.class)
	private ProjectPlan			 projectPlan;

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public Float getLoad()
	{
		return load;
	}

	@Override
	public void setLoad(final float load)
	{
		this.load = load;
	}

	@Override
	public ProjectDiscipline getProjectDiscipline()
	{
		return projectDiscipline;
	}

	@Override
	public void setProjectDiscipline(final ProjectDiscipline projectDiscipline)
	{
		this.projectDiscipline = projectDiscipline;
	}

	@Override
	public ProjectPlan getProjectPlan()
	{
		return projectPlan;
	}

	@Override
	public void setProjectPlan(final ProjectPlan projectPlan)
	{
		this.projectPlan = projectPlan;
	}

	@Override
	public Date getDate()
	{
		return date;
	}

	@Override
	public void setDate(final Date date)
	{
		this.date = date;
	}

}
