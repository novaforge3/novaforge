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
package org.novaforge.forge.core.configuration.entity;

import org.novaforge.forge.core.configuration.model.ForgeConfiguration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Jeremy Casery <>
 */
@Entity
@Table(name = "FORGE_CONFIGURATION")
@NamedQuery(name = "ForgeConfigurationEntity.findByKey",
    query = "SELECT c FROM ForgeConfigurationEntity c WHERE c.key = :key")
public class ForgeConfigurationEntity implements ForgeConfiguration
{

  /**
   * This represents the unique key which identify the forge configuration entity
   */
  @Id
  @Column(name = "key", nullable = false, updatable = false)
  private String key;

  /**
   * The value associated
   */
  @Column(name = "value")
  private String value;

  /**
   * Constructor public needed by JPA2
   */
  public ForgeConfigurationEntity()
  {
    // Use by JPA
  }

  /**
   * Default constructor
   * 
   * @param pKey
   *          the configuration key
   */
  public ForgeConfigurationEntity(final String pKey)
  {
    key = pKey;
  }

  /**
   * Default constructor
   * 
   * @param pKey
   *          the configuration key
   * @param pValue
   *          the configuration value
   */
  public ForgeConfigurationEntity(final String pKey, final String pValue)
  {
    key = pKey;
    value = pValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getKey()
  {
    return key;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setKey(final String key)
  {
    this.key = key;
  }

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
  public String toString()
  {
    return "ForgeConfigurationEntity [key=" + key + ", value=" + value + "]";
  }

}
