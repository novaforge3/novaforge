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
package org.novaforge.forge.core.organization.entity;

import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.Language;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;

/**
 * This describes a persisted {@link Composition}
 * 
 * @author BILET-JC
 */
@Entity
@Table(name = "LANGUAGE")
@NamedQueries({
    @NamedQuery(name = "LanguageEntity.findByName",
        query = "SELECT l FROM LanguageEntity l WHERE l.name = :name"),
    @NamedQuery(name = "LanguageEntity.findDefault",
        query = "SELECT l FROM LanguageEntity l WHERE l.isDefault = TRUE") })
public class LanguageEntity implements Language
{
  /**
   * Serial version id
   */
  private static final long serialVersionUID = 2589728672863638221L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "name", nullable = false, unique = true)
  private String            name;

  @Column(name = "is_default", nullable = false, unique = false)
  private boolean           isDefault;

  /**
   * @return technical id
   * @see LanguageEntity#id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   * 
   * @see LanguageEntity#name
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
   * @see LanguageEntity#name
   */
  @Override
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Locale getLocale()
  {
    Locale locale = Locale.ENGLISH;
    if (name != null)
    {
      locale = new Locale(name);
    }
    return locale;
  }

  /**
   * {@inheritDoc}
   *
   * @see LanguageEntity#isDefault
   */
  @Override
  @NotNull
  public boolean IsDefault()
  {
    return isDefault;
  }

  /**
   * {@inheritDoc}
   *
   * @see LanguageEntity#isDefault
   */
  @Override
  public void setIsDefault(final boolean pIsDefault)
  {
    this.isDefault = pIsDefault;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + (isDefault ? 1231 : 1237);
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LanguageEntity other = (LanguageEntity) obj;
    if (id == null)
    {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    if (isDefault != other.isDefault)
      return false;
    if (name == null)
    {
      if (other.name != null)
        return false;
    }
    else if (!name.equals(other.name))
      return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public String toString()
  {
    return "LanguageEntity [id=" + id + ", name=" + name + ", is default=" + isDefault + "]";
  }

}
