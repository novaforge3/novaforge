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

import org.novaforge.forge.core.configuration.model.ForgeIdentification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.UUID;

/**
 * This entity is used to store forge identification
 * 
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "FORGE_IDENTIFICATION")
@NamedQuery(name = "ForgeIdentificationEntity.findByUUID",
    query = "SELECT i FROM ForgeIdentificationEntity i WHERE i.forgeUUID = :uuid")
public class ForgeIdentificationEntity implements ForgeIdentification
{

  /**
   * This represents the unique UUID which identify the forge
   */
  @Id
  @Column(name = "uuid", nullable = false, updatable = false)
  private String forgeUUID;

  /**
   * Constructor public needed by JPA2
   */
  public ForgeIdentificationEntity()
  {
    // Use by JPA
  }

  /**
   * Default constructor
   * 
   * @param pUUID
   *          the forgeUUID to set
   */
  public ForgeIdentificationEntity(final UUID pUUID)
  {
    forgeUUID = pUUID.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getIdentifiant()
  {
    return UUID.fromString(forgeUUID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setIdentifiant(final UUID pForgeUUID)
  {
    forgeUUID = pForgeUUID.toString();

  }

}
