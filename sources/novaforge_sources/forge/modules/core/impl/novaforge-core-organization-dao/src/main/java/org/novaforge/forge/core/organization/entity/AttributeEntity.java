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

import org.novaforge.forge.core.organization.model.Attribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jeremy Casery
 */
@Entity
@Table(name = "USER_PROFILE_ATTRIBUTE")
public class AttributeEntity implements Attribute
{

  /**
	 * 
	 */
  private static final long serialVersionUID = 7279966990655550135L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "value")
  private String            value            = "";

  @Column(name = "ispublic")
  private boolean           isPublic;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getValue()
  {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(final String value)
  {
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getIsPublic()
  {
    return isPublic;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setIsPublic(final boolean isPublic)
  {
    this.isPublic = isPublic;
  }

}
