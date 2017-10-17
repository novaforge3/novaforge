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
 * This DTO represents a plugin item.
 * 
 * @author salvat-a
 */
public class ItemDTO
{
  /**
   * Item reference.
   */
  private ItemReferenceDTO reference;

  /**
   * Action needed on the item.
   */
  private ActionType       action;

  public ItemDTO()
  {
  }

  public ItemDTO(final ItemReferenceDTO reference, final ActionType action)
  {
    this.reference = reference;
    this.action = action;
  }

  public ItemReferenceDTO getReference()
  {
    return reference;
  }

  public void setReference(final ItemReferenceDTO reference)
  {
    this.reference = reference;
  }

  public ActionType getAction()
  {
    return action;
  }

  public void setAction(final ActionType action)
  {
    this.action = action;
  }

  @Override
  public int hashCode()
  {
    int result = reference != null ? reference.hashCode() : 0;
    result = (31 * result) + (action != null ? action.hashCode() : 0);
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

    final ItemDTO itemDTO = (ItemDTO) o;

    if (action != itemDTO.action)
    {
      return false;
    }
    return !(reference != null ? !reference.equals(itemDTO.reference) : itemDTO.reference != null);

  }

  @Override
  public String toString()
  {
    return "ItemDTO{" + "reference=" + reference + ", action=" + action + '}';
  }
}
