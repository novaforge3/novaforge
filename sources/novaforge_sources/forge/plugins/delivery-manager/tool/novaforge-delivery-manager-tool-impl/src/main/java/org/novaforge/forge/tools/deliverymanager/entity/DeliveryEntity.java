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
package org.novaforge.forge.tools.deliverymanager.entity;

import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Concret implementation of delivery persistence object
 * 
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "DELIVERY", uniqueConstraints = @UniqueConstraint(columnNames = { "reference", "project_id" }))
@NamedQueries({
    @NamedQuery(name = "DeliveryEntity.findByProjectAndReference", query = "SELECT d FROM DeliveryEntity d WHERE d.projectId = :projectId AND d.reference = :reference"),
    @NamedQuery(name = "DeliveryEntity.findByProject", query = "SELECT d FROM DeliveryEntity d WHERE d.projectId = :projectId"),
    @NamedQuery(name = "DeliveryEntity.findByProjectAndStatus", query = "SELECT d FROM DeliveryEntity d WHERE d.projectId = :projectId AND d.status = :status") })
public class DeliveryEntity implements Delivery, Serializable
{
	private static final long serialVersionUID = -4945615531290197742L;

	/**
	 * @see DeliveryEntity#getId()
	 */
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long              id;

	/**
	 * @see DeliveryEntity#getReference()
	 * @see DeliveryEntity#setReference(String)
	 */
	@Column(name = "reference", nullable = false, unique = true)
	private String            reference;
	/**
	 * @see DeliveryEntity#getName()
	 * @see DeliveryEntity#setName(String)
	 */
	@Column(name = "name", nullable = false)
	private String            name;
	/**
	 * @see DeliveryEntity#getVersion()
	 * @see DeliveryEntity#setVersion(String)
	 */
	@Column(name = "version", nullable = false)
	private String            version;
	/**
	 * @see DeliveryEntity#getProjectId()
	 * @see DeliveryEntity#setProjectId(String)
	 */
	@Column(name = "project_id", nullable = false)
	private String            projectId;
	/**
	 * @see DeliveryEntity#getStatus()
	 * @see DeliveryEntity#setStatus(DeliveryStatus)
	 */
	@Column(name = "status", nullable = false)
	private String            status;
	/**
	 * @see DeliveryEntity#getDate()
	 * @see DeliveryEntity#setDate(Date)
	 */
	@Column(name = "date", nullable = true)
	private Date              date;
	/**
	 * @see DeliveryEntity#getType()
	 * @see DeliveryEntity#setType(DeliveryType)
	 */
	@ManyToOne(targetEntity = DeliveryTypeEntity.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "type", nullable = true)
	private DeliveryType      type;
	/**
	 * @see DeliveryEntity#getContents()
	 * @see DeliveryEntity#removeContent(Content)
	 * @see DeliveryEntity#addContent(Content)
	 */
	@OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
	    CascadeType.PERSIST }, targetEntity = ContentEntity.class, orphanRemoval = true)
	private List<Content>     contents         = new ArrayList<Content>();

	public DeliveryEntity()
	{
		// Used by JPA
	}

	/**
	 * @return id
	 * @see DeliveryEntity#id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DeliveryEntity#reference
	 */
	@NotNull
	@Size(min = 1)
	@Override
	public String getReference()
	{
		return reference;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DeliveryEntity#reference
	 */
	@Override
	public void setReference(final String pReference)
	{
		reference = pReference;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DeliveryEntity#name
	 */
	@Override
	@NotNull
	@Size(min = 1)
	public String getName()
	{
		return name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DeliveryEntity#name
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see DeliveryEntity#type
	 */
	@Override
	public DeliveryType getType()
	{
		return type;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see DeliveryEntity#version
	 */
	@Override
	@NotNull
	@Size(min = 1)
	public String getVersion()
	{
		return version;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see DeliveryEntity#version
	 */
	@Override
	public void setVersion(final String pVersion)
	{
		version = pVersion;

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see DeliveryEntity#status
	 */
	@Override
	public DeliveryStatus getStatus()
	{
		return DeliveryStatus.valueOf(status);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DeliveryEntity#projectId
	 */
	@Override
	@NotNull
	@Size(min = 1)
	public String getProjectId()
	{
		return projectId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DeliveryEntity#projectId
	 */
	@Override
	public void setProjectId(final String pProjectId)
	{
		projectId = pProjectId;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see DeliveryEntity#date
	 */
	@Override
	public Date getDate()
	{
		return date;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see DeliveryEntity#version
	 */
	public void setDate(final Date pDate)
	{
		date = pDate;

	}

	/**
	 * @param pStatus
	 * @see DeliveryEntity#status
	 */
	public void setStatus(final DeliveryStatus pStatus)
	{
		status = pStatus.name();

	}

	/**
	 * @param pType
	 * @see DeliveryEntity#type
	 */
	public void setType(final DeliveryType pType)
	{
		type = pType;
	}

	/**
	 * This method has to be called in a JPA context because the list is lazy one
	 * 
	 * @return lit of contents
	 * @see DeliveryEntity#contents
	 */
	public List<Content> getContents()
	{
		return Collections.unmodifiableList(contents);
	}

	/**
	 * @param pContents
	 *          the contents to set
	 */
	public void setContents(final List<Content> pContents)
	{
		this.contents = pContents;
	}

	/**
	 * @param pContent
	 * @see DeliveryEntity#contents
	 */
	public void addContent(final Content pContent)
	{
		contents.add(pContent);
	}

	/**
	 * @param pContent
	 * @see DeliveryEntity#contents
	 */
	public void removeContent(final Content pContent)
	{
		contents.remove(pContent);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, reference, name, version, projectId, status, date, type, contents);
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
		final DeliveryEntity that = (DeliveryEntity) o;
		return Objects.equals(id, that.id) &&
							 Objects.equals(reference, that.reference) &&
							 Objects.equals(name, that.name) &&
							 Objects.equals(version, that.version) &&
							 Objects.equals(projectId, that.projectId) &&
							 Objects.equals(status, that.status) &&
							 Objects.equals(date, that.date) &&
							 Objects.equals(type, that.type) &&
							 Objects.equals(contents, that.contents);
	}

	@Override
	public String toString()
	{
		return "DeliveryEntity [id=" + id + ", reference=" + reference + ", name=" + name + ", version=" + version
							 + ", projectId=" + projectId + ", status=" + status + ", type=" + type + "]";
	}

}