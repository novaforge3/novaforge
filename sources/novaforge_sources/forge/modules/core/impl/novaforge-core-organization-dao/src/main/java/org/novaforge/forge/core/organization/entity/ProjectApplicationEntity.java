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
package org.novaforge.forge.core.organization.entity;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.ProjectApplication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * @author sbenoist
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "NODE")
@NamedQueries({
    @NamedQuery(name = "ProjectApplicationEntity.findByInstance",
        query = "SELECT p FROM ProjectApplicationEntity p WHERE p.pluginInstanceUUID = :instance"),
    @NamedQuery(
        name = "ProjectApplicationEntity.updateApplicationStatus",
        query = "UPDATE ProjectApplicationEntity a SET a.status = :status WHERE a.pluginInstanceUUID = :instance") })
public class ProjectApplicationEntity extends ApplicationEntity implements ProjectApplication
{

  /**
    * 
    */
  private static final long serialVersionUID = -6729867778159576316L;
  @Column(name = "plugin_instance_uuid", nullable = true)
  private String            pluginInstanceUUID;

  @Column(name = "status", nullable = true)
  private String            status;

  @Override
  @NotNull
  public UUID getPluginInstanceUUID()
  {
    return UUID.fromString(pluginInstanceUUID);
  }

  @Override
  public void setPluginInstanceUUID(final UUID pluginInstanceUUID)
  {
    this.pluginInstanceUUID = pluginInstanceUUID.toString();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ApplicationStatus getStatus()
  {
    return ApplicationStatus.valueOf(status);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatus(final ApplicationStatus pStatus)
  {
    status = pStatus.toString();
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(getId()).append(getUri()).toHashCode();
  }

  @Override
  public boolean equals(final Object pOther)
  {
    if (this == pOther)
    {
      return true;
    }
    if (pOther == null || getClass() != pOther.getClass())
    {
      return false;
    }
    if (!super.equals(pOther))
    {
      return false;
    }
    final ProjectApplicationEntity that = (ProjectApplicationEntity) pOther;
    return Objects.equals(pluginInstanceUUID, that.pluginInstanceUUID) && Objects.equals(status, that.status);
  }
}