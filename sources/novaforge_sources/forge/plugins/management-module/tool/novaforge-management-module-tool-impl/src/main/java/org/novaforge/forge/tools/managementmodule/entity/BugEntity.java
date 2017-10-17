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

import org.novaforge.forge.tools.managementmodule.domain.Bug;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * JPA implementation of a Bug
 */
@Entity
@Table(name = "bug")
@NamedQueries(@NamedQuery(name = "BugEntity.findByBugTrackerId", query = "SELECT bug FROM BugEntity bug WHERE bug.bugTrackerId = :bugTrackerId"))
public class BugEntity implements Serializable, Bug
{

	/** UID for serialization */
	private static final long serialVersionUID = -7914871977769245006L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "bug_tracker_id", nullable = false, unique = true)
	private String						bugTrackerId;

	/** The title of the bug */
	@Column(name = "title", nullable = false, unique = false)
	private String						title;

	/** The description of the bug */
	@Transient
	private String						description;

	/** The additionals informations of the bug */
	@Transient
	private String						additionalInfo;

	/** The category of the bug */
	@Transient
	private String						category;

	/** The reporter of the bug */
	@Transient
	private String						reporter;

	/** The severity of the bug */
	@Transient
	private String						severity;

	/** The priority of the bug */
	@Transient
	private String						priority;

	/** The resolution of the bug */
	@Transient
	private String						resolution;

	/** The status of the bug */
	@Transient
	private String						status;

	/** The product version of the bug */
	@Transient
	private String						productVersion;

	/** The user assigned to the bug */
	@Transient
	private String						assignedTo;

	/** The reproducibility of the bug */
	@Transient
	private String						reproducibility;

	/** The target version of the bug */
	@Transient
	private String						targetVersion;

	/** The version of the fix of the bug */
	@Transient
	private String						fixedInVersion;

	/**
	 * @return the bugTrackerId
	 */
	@Override
	public String getBugTrackerId()
	{
		return bugTrackerId;
	}

	/**
	 * @param bugTrackerId
	 *          the bugTrackerId to set
	 */
	@Override
	public void setBugTrackerId(final String bugTrackerId)
	{
		this.bugTrackerId = bugTrackerId;
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *          the description to set
	 */
	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * Get the title
	 * 
	 * @return the title
	 */
	@Override
	public String getTitle()
	{
		return title;
	}

	/**
	 * Set the title
	 * 
	 * @param title
	 *          the title to set
	 */
	@Override
	public void setTitle(final String title)
	{
		this.title = title;
	}

	/**
	 * Get the additionalInfo
	 * 
	 * @return the additionalInfo
	 */
	@Override
	public String getAdditionalInfo()
	{
		return additionalInfo;
	}

	/**
	 * Set the additionalInfo
	 * 
	 * @param additionalInfo
	 *          the additionalInfo to set
	 */
	@Override
	public void setAdditionalInfo(final String additionalInfo)
	{
		this.additionalInfo = additionalInfo;
	}

	/**
	 * Get the category
	 * 
	 * @return the category
	 */
	@Override
	public String getCategory()
	{
		return category;
	}

	/**
	 * Set the category
	 * 
	 * @param category
	 *          the category to set
	 */
	@Override
	public void setCategory(final String category)
	{
		this.category = category;
	}

	/**
	 * Get the reporter
	 * 
	 * @return the reporter
	 */
	@Override
	public String getReporter()
	{
		return reporter;
	}

	/**
	 * Set the reporter
	 * 
	 * @param reporter
	 *          the reporter to set
	 */
	@Override
	public void setReporter(final String reporter)
	{
		this.reporter = reporter;
	}

	/**
	 * Get the severity
	 * 
	 * @return the severity
	 */
	@Override
	public String getSeverity()
	{
		return severity;
	}

	/**
	 * Set the severity
	 * 
	 * @param severity
	 *          the severity to set
	 */
	@Override
	public void setSeverity(final String severity)
	{
		this.severity = severity;
	}

	/**
	 * Get the priority
	 * 
	 * @return the priority
	 */
	@Override
	public String getPriority()
	{
		return priority;
	}

	/**
	 * Set the priority
	 * 
	 * @param priority
	 *          the priority to set
	 */
	@Override
	public void setPriority(final String priority)
	{
		this.priority = priority;
	}

	/**
	 * Get the resolution
	 * 
	 * @return the resolution
	 */
	@Override
	public String getResolution()
	{
		return resolution;
	}

	/**
	 * Set the resolution
	 * 
	 * @param resolution
	 *          the resolution to set
	 */
	@Override
	public void setResolution(final String resolution)
	{
		this.resolution = resolution;
	}

	/**
	 * Get the status
	 * 
	 * @return the status
	 */
	@Override
	public String getStatus()
	{
		return status;
	}

	/**
	 * Set the status
	 * 
	 * @param status
	 *          the status to set
	 */
	@Override
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * Get the productVersion
	 * 
	 * @return the productVersion
	 */
	@Override
	public String getProductVersion()
	{
		return productVersion;
	}

	/**
	 * Set the productVersion
	 * 
	 * @param productVersion
	 *          the productVersion to set
	 */
	@Override
	public void setProductVersion(final String productVersion)
	{
		this.productVersion = productVersion;
	}

	/**
	 * Get the assignedTo
	 * 
	 * @return the assignedTo
	 */
	@Override
	public String getAssignedTo()
	{
		return assignedTo;
	}

	/**
	 * Set the assignedTo
	 * 
	 * @param assignedTo
	 *          the assignedTo to set
	 */
	@Override
	public void setAssignedTo(final String assignedTo)
	{
		this.assignedTo = assignedTo;
	}

	/**
	 * Get the reproducibility
	 * 
	 * @return the reproducibility
	 */
	@Override
	public String getReproducibility()
	{
		return reproducibility;
	}

	/**
	 * Set the reproducibility
	 * 
	 * @param reproducibility
	 *          the reproducibility to set
	 */
	@Override
	public void setReproducibility(final String reproducibility)
	{
		this.reproducibility = reproducibility;
	}

	/**
	 * Get the targetVersion
	 * 
	 * @return the targetVersion
	 */
	@Override
	public String getTargetVersion()
	{
		return targetVersion;
	}

	/**
	 * Set the targetVersion
	 * 
	 * @param targetVersion
	 *          the targetVersion to set
	 */
	@Override
	public void setTargetVersion(final String targetVersion)
	{
		this.targetVersion = targetVersion;
	}

	/**
	 * Get the fixedInVersion
	 * 
	 * @return the fixedInVersion
	 */
	@Override
	public String getFixedInVersion()
	{
		return fixedInVersion;
	}

	/**
	 * Set the fixedInVersion
	 * 
	 * @param fixedInVersion
	 *          the fixedInVersion to set
	 */
	@Override
	public void setFixedInVersion(final String fixedInVersion)
	{
		this.fixedInVersion = fixedInVersion;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bugTrackerId == null) ? 0 : bugTrackerId.hashCode());
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
		BugEntity other = (BugEntity) obj;
		if (bugTrackerId == null)
		{
			if (other.bugTrackerId != null)
			{
				return false;
			}
		}
		else if (!bugTrackerId.equals(other.bugTrackerId))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "BugEntity [id=" + id + ", bugTrackerId=" + bugTrackerId + ", title=" + title + "]";
	}

}
