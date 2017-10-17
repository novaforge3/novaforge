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

package org.novaforge.forge.ui.distribution.client.view.commons;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ProfilViewDTO;

import java.util.Iterator;
import java.util.Set;

/**
 * @author BILET-JC
 *
 */
public class IndexedColumn extends Column<ProfilViewDTO, String>
{
   private final String index;

   public IndexedColumn(String index)
   {
      super(new TextCell());
      this.index = index;
   }

   @Override
   public String getValue(ProfilViewDTO object)
   {
      String result = "0";
      Set<String> key = object.getRoles().keySet();
      Iterator<String> it = key.iterator();
      // for each role of the object
      while (it.hasNext())
      {
         String objectRole = it.next();
         if (objectRole.equals(index))
         {
            result = object.getRoles().get(objectRole).toString();
         }
      }
      return result;
   }

}
