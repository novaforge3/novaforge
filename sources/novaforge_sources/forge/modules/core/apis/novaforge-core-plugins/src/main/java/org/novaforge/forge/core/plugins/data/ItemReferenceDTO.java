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
package org.novaforge.forge.core.plugins.data;

/**
 * This DTO represents a plugin item reference.
 * 
 * @author salvat-a
 */
public class ItemReferenceDTO
{
  /**
   * The reference ID.
   */
  private String referenceId;

  /**
   * The comparator element used to know if an item has been modified.
   */
  private String modificationComparator;

  /**
   * The item type (useful when a plugin handles different types of items).
   */
  private String itemType;

  public ItemReferenceDTO()
  {
  }

  public ItemReferenceDTO(final String referenceId, final String modificationComparator, final String itemType)
  {
    this.referenceId = referenceId;
    this.modificationComparator = modificationComparator;
    this.itemType = itemType;
  }

  public String getReferenceId()
  {
    return referenceId;
  }

  public void setReferenceId(final String referenceId)
  {
    this.referenceId = referenceId;
  }

  public String getModificationComparator()
  {
    return modificationComparator;
  }

  public void setModificationComparator(final String modificationComparator)
  {
    this.modificationComparator = modificationComparator;
  }

  public String getItemType()
  {
    return itemType;
  }

  public void setItemType(final String itemType)
  {
    this.itemType = itemType;
  }

  @Override
  public int hashCode()
  {
    int result = referenceId != null ? referenceId.hashCode() : 0;
    result = (31 * result) + (modificationComparator != null ? modificationComparator.hashCode() : 0);
    result = (31 * result) + (itemType != null ? itemType.hashCode() : 0);
    return result;
  }

  @Override
  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass()))
    {
      return false;
    }

    final ItemReferenceDTO that = (ItemReferenceDTO) o;

    if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null)
    {
      return false;
    }
    if (modificationComparator != null ? !modificationComparator.equals(that.modificationComparator)
        : that.modificationComparator != null)
    {
      return false;
    }
    return !(referenceId != null ? !referenceId.equals(that.referenceId) : that.referenceId != null);

  }

  @Override
  public String toString()
  {
    return "ItemReferenceDTO{" + "referenceId='" + referenceId + '\'' + ", modificationComparator='"
        + modificationComparator + '\'' + ", itemType='" + itemType + '\'' + '}';
  }
}
