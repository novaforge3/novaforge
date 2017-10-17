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

import org.novaforge.forge.tools.managementmodule.domain.CDOParameters;
import org.novaforge.forge.tools.managementmodule.domain.Project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "cdo_parameters")
@NamedQueries({
		@NamedQuery(name = "CDOParametersEntity.findByProject", query = "SELECT p FROM CDOParametersEntity p WHERE p.project.projectId = :projectId"),
		@NamedQuery(name = "CDOParametersEntity.findById", query = "SELECT p FROM CDOParametersEntity p WHERE p.id = :id") })
public class CDOParametersEntity implements CDOParameters, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1988425604751674256L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "host", nullable = false)
	private String						host;

	@Column(name = "port", nullable = false)
	private Integer					 port;

	@Column(name = "repository", nullable = false)
	private String						repository;

	@Column(name = "projetCdo", nullable = false)
	private String						projetCdo;

	@Column(name = "systemGraal", nullable = false)
	private String						systemGraal;

	@OneToOne(targetEntity = ProjectEntity.class)
	private Project					 project;

	@Column(name = "cronExpression", nullable = true)
	private String						cronExpression;

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public String getSystemGraal()
	{
		return systemGraal;
	}

	@Override
	public void setSystemGraal(final String systemGraal)
	{
		this.systemGraal = systemGraal;
	}

	@Override
	public String getProjetCdo()
	{
		return projetCdo;
	}

	@Override
	public void setProjetCdo(final String projetCdo)
	{
		this.projetCdo = projetCdo;
	}

	@Override
	public String getRepository()
	{
		return repository;
	}

	@Override
	public void setRepository(final String repository)
	{
		this.repository = repository;
	}

	@Override
	public Integer getPort()
	{
		return port;
	}

	@Override
	public void setPort(final Integer port)
	{
		this.port = port;
	}

	@Override
	public String getHost()
	{
		return host;
	}

	@Override
	public void setHost(final String host)
	{
		this.host = host;
	}

	@Override
	public Project getProject()
	{
		return project;
	}

	@Override
	public void setProject(final Project project)
	{
		this.project = project;
	}

	@Override
	public String getCronExpression()
	{
		return cronExpression;
	}

	@Override
	public void setCronExpression(final String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((repository == null) ? 0 : repository.hashCode());
		result = prime * result + ((systemGraal == null) ? 0 : systemGraal.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		CDOParametersEntity other = (CDOParametersEntity) obj;
		if (host == null)
		{
			if (other.host != null)
			{
				return false;
			}
		}
		else if (!host.equals(other.host))
		{
			return false;
		}
		if (port == null)
		{
			if (other.port != null)
			{
				return false;
			}
		}
		else if (!port.equals(other.port))
		{
			return false;
		}
		if (repository == null)
		{
			if (other.repository != null)
			{
				return false;
			}
		}
		else if (!repository.equals(other.repository))
		{
			return false;
		}
		if (systemGraal == null)
		{
			if (other.systemGraal != null)
			{
				return false;
			}
		}
		else if (!systemGraal.equals(other.systemGraal))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "CDOParametersEntity [id=" + id + ", host=" + host + ", port=" + port + ", repository=" + repository
							 + ", projetCdo=" + projetCdo + ", systemGraal=" + systemGraal + ", project=" + project
							 + ", cronExpression=" + cronExpression + "]";
	}

}
