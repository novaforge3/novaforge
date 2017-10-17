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
package org.novaforge.commons.route.domain;

import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;

/**
 * @author lamirang
 */
public class PluginProjectImpl implements PluginProject
{

  private static final long serialVersionUID = 2820284842626765605L;
  private String            projectId;
  private String            name;
  private String            description;
  private String            license;
  private String            status;
  private String            author;

  public PluginProjectImpl(final Project pProject, final String pAuthor)
  {
    projectId = pProject.getProjectId();
    name = pProject.getName();
    description = pProject.getDescription();
    license = pProject.getLicenceType();
    status = pProject.getStatus().getLabel();
    author = pAuthor;
  }

  /**
   * @return the projectId
   */
  @Override
  public String getProjectId()
  {
    return projectId;
  }

  /**
   * @param pProjectId
   *          the projectId to set
   */
  @Override
  public void setProjectId(final String pProjectId)
  {
    projectId = pProjectId;
  }

  /**
   * @return the name
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * @param pName
   *          the name to set
   */
  @Override
  public void setName(final String pName)
  {
    name = pName;
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
   * @param pDescription
   *          the description to set
   */
  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  /**
   * @return the licence
   */
  @Override
  public String getLicense()
  {
    return license;
  }

  /**
   * @param pLicense
   *          the license to set
   */
  @Override
  public void setLicense(final String pLicense)
  {
    license = pLicense;
  }

  /**
   * @return the status
   */
  @Override
  public String getStatus()
  {
    return status;
  }

  /**
   * @param pStatus
   *          the status to set
   */
  @Override
  public void setStatus(final String pStatus)
  {
    status = pStatus;
  }

  /**
   * @inheritDoc
   */
  @Override
  public String getAuthor()
  {
    return author;
  }

  /**
   * @inheritDoc
   */
  @Override
  public void setAuthor(final String pAuthor)
  {
    author = pAuthor;
  }

}
