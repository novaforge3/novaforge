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
package org.novaforge.forge.tools.deliverymanager.ui.shared;

/**
 * @author caseryj
 */
public interface NodeDTO
{

   /**
    * @return the name
    */
   String getName();

   /**
    * @param pName
    *           the name to set
    */
   void setName(final String pName);

   /**
    * @return the path
    */
   String getPath();

   /**
    * @param pPath
    *           the path to set
    */
   void setPath(final String pPath);

   /**
    * Get if a node is still existing
    * 
    * @return true if still exist, false otherwise
    */
   boolean isExist();

   /**
    * Set if the original node still exist
    * 
    * @param pIsExist
    *           true if original node still exist, false otherwise
    */
   void setExist(final boolean pIsExist);

   /**
    * Same as equals, but don't compare the field path
    */
   boolean equalsWithoutPath(final Object obj);

   /**
    * {@inheritDoc}
    */
   @Override
   int hashCode();

   /**
    * {@inheritDoc}
    */
   @Override
   boolean equals(final Object obj);

}
