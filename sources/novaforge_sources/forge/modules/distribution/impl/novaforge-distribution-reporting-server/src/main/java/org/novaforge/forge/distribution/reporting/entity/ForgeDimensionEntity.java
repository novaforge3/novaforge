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
package org.novaforge.forge.distribution.reporting.entity;

import org.novaforge.forge.distribution.reporting.domain.ForgeDimension;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.UUID;

/**
 * @author Bilet-jc
 */
@Entity
@Table(name = "FORGE_DIMENSION")
@NamedQuery(name = "ForgeDimensionEntity.findByForgeId",
    query = "SELECT fd FROM ForgeDimensionEntity fd WHERE fd.forgeId = :forgeId")
public class ForgeDimensionEntity implements ForgeDimension
{

	/**
    * 
    */
	private static final long serialVersionUID = -8521194866266096699L;
	/**
	 * Added a date to change with the last update
	 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_updated",nullable = false, updatable = false)
    private java.util.Date lastUpdated;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private long   id;
	@Column(name = "forge_id", updatable = false, unique = true)
	private String forgeId;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "forge_level", nullable = false)
	private int    forgeLevel;

	public ForgeDimensionEntity()
	{
		super();
	}

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	@Override
	public UUID getForgeId()
	{
		return UUID.fromString(forgeId);
	}

	@Override
	public void setForgeId(final UUID forgeId)
	{
		this.forgeId = forgeId.toString();
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(final String name)
	{
		this.name = name;

	}

	@Override
	public int getForgeLevel()
	{
		return forgeLevel;
	}

	@Override
	public void setForgeLevel(final int forgeLevel)
	{
		this.forgeLevel = forgeLevel;

	}

	@Override
	public Date getLastUpdated()
	{
		return this.lastUpdated;
	}

	@Override
	public void setLastUpdated(Date lastUpdated) 
	{
		this.lastUpdated = lastUpdated; 
	}

	@Override
	public String toString()
	{
		return "ForgeDimensionEntity [id=" + id + ", forgeId=" + forgeId + ", forgeLevel=" + forgeLevel + ", name=" + name
							 + "]";
	}

}
