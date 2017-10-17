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
package org.novaforge.forge.tools.requirements.common.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.model.IResourceOOCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

@Entity
@Table(name = "RESOURCE")
public class ResourceOOEntity implements IResourceOOCode, Serializable
{
  private static final long   serialVersionUID = -5236633270951186828L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long                id;

  @Column(name = "name", nullable = true, updatable = false)
  private String              name;

  @Column(name = "location", nullable = false, updatable = false)
  private String              location;

  @Column(name = "component", nullable = true, updatable = false)
  private String              compomentName;

  @ManyToOne(targetEntity = RequirementVersionEntity.class)
  @JoinColumn(name = "version_id", nullable = false, insertable = true, updatable = false)
  private IRequirementVersion version;

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void setName(final String pName)
  {
    name = pName;
  }

  @Override
  public IRequirementVersion getVersion()
  {
    return version;
  }

  @Override
  public URL getLocation()
  {
    try
    {
      return new URL(location);
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void setVersion(final IRequirementVersion pVersion)
  {
    version = pVersion;
  }

  @Override
  public void setLocation(final URL pLocation)
  {
    location = pLocation.toString();
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(getId()).toHashCode();
  }

  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof DirectoryEntity))
    {
      return false;
    }
    final ResourceOOEntity castOther = (ResourceOOEntity) other;
    return new EqualsBuilder().append(getId(), castOther.getId()).isEquals();
  }

  public Long getId()
  {
    return id;
  }

  @Override
  public String getCompomentName()
  {
    return compomentName;
  }

  @Override
  public void setCompomentName(final String pComponentName)
  {
    compomentName = pComponentName;
  }







}
