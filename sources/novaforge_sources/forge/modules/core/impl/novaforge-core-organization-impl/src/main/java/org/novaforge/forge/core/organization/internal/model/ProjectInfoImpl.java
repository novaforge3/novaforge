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
package org.novaforge.forge.core.organization.internal.model;

import org.novaforge.forge.core.organization.model.ProjectInfo;

import java.util.Date;

/**
 * @author sbenoist
 */
public class ProjectInfoImpl implements ProjectInfo
{
  private static final long serialVersionUID = -1588994578050317418L;

  private String            description;

  private String            licenceType;

  private String            name;

  private String            projectId;

  private Date              lastModified;

  /**
   * @param description
   * @param licenceType
   * @param name
   * @param projectId
   * @param lastModified
   */
  public ProjectInfoImpl(final String description, final String licenceType, final String name,
      final String projectId, final Date lastModified)
  {
    super();
    this.description = description;
    this.licenceType = licenceType;
    this.name = name;
    this.projectId = projectId;
    this.lastModified = lastModified;
  }

  /**
   * @inheritDoc
   */
  @Override
  public String getProjectId()
  {
    return projectId;
  }

  /**
   * @inheritDoc
   */
  @Override
  public void setProjectId(final String pProjectId)
  {
    projectId = pProjectId;
  }

  /**
   * @inheritDoc
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * @inheritDoc
   */
  @Override
  public void setName(final String pName)
  {
    name = pName;
  }

  /**
   * @inheritDoc
   */
  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * @inheritDoc
   */
  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  /**
   * @inheritDoc
   */
  @Override
  public String getLicenceType()
  {
    return licenceType;
  }

  /**
   * @inheritDoc
   */
  @Override
  public void setLicenceType(final String pLicenceType)
  {
    licenceType = pLicenceType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getLastModified()
  {

    return lastModified;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLastModified(final Date date)
  {
    lastModified = date;
  }

}
