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
package org.novaforge.forge.distribution.reference.model;

import java.util.UUID;

/**
 * @author rols-p
 */
public class TargetForge
{
  private UUID   forgeId;

  private String label;

  private String description;

  private int    forgeLevel;

  /**
    * 
    */
  public TargetForge()
  {
    super();
  }

  /**
   * @param forgeId
   * @param label
   * @param description
   * @param forgeLevel
   */
  public TargetForge(final UUID forgeId, final String label, final String description, final int forgeLevel)
  {
    super();
    this.forgeId = forgeId;
    this.label = label;
    this.description = description;
    this.forgeLevel = forgeLevel;
  }

  /**
   * @return the forgeId
   */
  public UUID getForgeId()
  {
    return forgeId;
  }

  /**
   * @param forgeId
   *          the forgeId to set
   */
  public void setForgeId(final UUID forgeId)
  {
    this.forgeId = forgeId;
  }

  /**
   * @return the label
   */
  public String getLabel()
  {
    return label;
  }

  /**
   * @param label
   *          the label to set
   */
  public void setLabel(final String label)
  {
    this.label = label;
  }

  /**
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription(final String description)
  {
    this.description = description;
  }

  /**
   * @return the forgeLevel
   */
  public int getForgeLevel()
  {
    return forgeLevel;
  }

  /**
   * @param forgeLevel
   *          the forgeLevel to set
   */
  public void setForgeLevel(final int forgeLevel)
  {
    this.forgeLevel = forgeLevel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((forgeId == null) ? 0 : forgeId.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    final TargetForge other = (TargetForge) obj;
    if (forgeId == null)
    {
      if (other.forgeId != null)
      {
        return false;
      }
    }
    else if (!forgeId.equals(other.forgeId))
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "TargetForge [forgeId=" + forgeId + ", label=" + label + ", description=" + description
        + ", forgeLevel=" + forgeLevel + "]";
  }

}
