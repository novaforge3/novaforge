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

import org.novaforge.forge.tools.deliverymanager.model.TemplateCustomField;
import org.novaforge.forge.tools.deliverymanager.model.TemplateReport;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
import java.util.List;

/**
 * This entity describes the element stored for a report template. It has to contain a name, description and a
 * file name regarding a project id;
 * 
 * @see TemplateReport
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "TEMPLATE_REPORT", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "project_id",
    "file_name" }))
@NamedQueries({
    @NamedQuery(name = "TemplateReportEntity.findByProjectAndFileName", query = "SELECT t FROM TemplateReportEntity t WHERE t.projectId = :projectId AND t.fileName = :fileName"),
    @NamedQuery(name = "TemplateReportEntity.findByProjectAndName", query = "SELECT t FROM TemplateReportEntity t WHERE t.projectId = :projectId AND t.name = :name"),
    @NamedQuery(name = "TemplateReportEntity.findByProject", query = "SELECT t FROM TemplateReportEntity t WHERE t.projectId = :projectId") })
public class TemplateReportEntity implements Serializable, TemplateReport
{

	/**
	 * Serial Version Id used for serialization
	 */
	private static final long         serialVersionUID = -1201801366727680617L;
	/**
	 * @see TemplateReportEntity#getId()
	 * @see TemplateReportEntity#setId(Long)
	 */
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long                      id;
	/**
	 * The name of the template
	 * 
	 * @see TemplateReportEntity#getName()
	 * @see TemplateReportEntity#setName(String)
	 */
	@Column(name = "name", nullable = false)
	private String                    name;
	/**
	 * Some details about the template
	 * 
	 * @see TemplateReportEntity#getDescription()
	 * @see TemplateReportEntity#setDescription(String)
	 */
	@Column(name = "description", nullable = false)
	private String                    description;
	/**
	 * The file name of the template file
	 * 
	 * @see TemplateReportEntity#getFileName()
	 * @see TemplateReportEntity#setFileName(String)
	 */
	@Column(name = "file_name", nullable = false)
	private String                    fileName;

	/**
	 * This contains all custom field declared to the template
	 * 
	 * @see TemplateReportEntity#getFields()
	 * @see TemplateReportEntity#addField(TemplateCustomField)
	 * @see TemplateReportEntity#removeField(TemplateCustomField)
	 * @see TemplateCustomFieldEntity
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = TemplateCustomFieldEntity.class, orphanRemoval = true)
	@JoinTable(name = "TEMPLATE_FIELD", joinColumns = @JoinColumn(name = "template_id"), inverseJoinColumns = @JoinColumn(name = "field_id"))
	private List<TemplateCustomField> fields           = new ArrayList<TemplateCustomField>();

	/**
	 * @see TemplateReportEntity#getProjectId()
	 * @see TemplateReportEntity#setProjectId(String)
	 */
	@Column(name = "project_id", nullable = false)
	private String                    projectId;

	/**
	 * Default constructor
	 */
	public TemplateReportEntity()
	{
		// Used by JPA
	}

	/**
	 * @return persistence id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportEntity#name
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
	 * @see TemplateReportEntity#name
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportEntity#description
	 */
	@Override
	@NotNull
	@Size(min = 1)
	public String getDescription()
	{
		return description;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportEntity#description
	 */
	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportEntity#fileName
	 */
	@Override
	@NotNull
	@Size(min = 1)
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportEntity#fileName
	 */
	@Override
	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportEntity#fields
	 * @see TemplateReportEntity#addField(TemplateCustomField)
	 * @see TemplateReportEntity#removeField(TemplateCustomField)
	 */
	@Override
	public List<TemplateCustomField> getFields()
	{
		return Collections.unmodifiableList(fields);
	}

	/**
	 * @param fields
	 *          the fields to set
	 */
	public void setFields(final List<TemplateCustomField> fields)
	{
		this.fields = fields;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return <tt>true</tt> (as specified by {@link List#add})
	 * @see TemplateReportEntity#fields
	 */
	@Override
	public boolean addField(final TemplateCustomField pField)
	{
		return fields.add(pField);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return <tt>true</tt> (as specified by {@link List#remove})
	 * @see TemplateReportEntity#fields
	 */
	@Override
	public boolean removeField(final TemplateCustomField pField)
	{
		return fields.remove(pField);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TemplateReportEntity#projectId
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
	 * @see TemplateReportEntity#projectId
	 */
	@Override
	public void setProjectId(final String projectId)
	{
		this.projectId = projectId;
	}

	@Override
	public String toString()
	{
		return "TemplateReportEntity [name=" + name + ", description=" + description + ", fileName=" + fileName
							 + ", fields=" + getFields() + ", projectId=" + projectId + "]";
	}

}
