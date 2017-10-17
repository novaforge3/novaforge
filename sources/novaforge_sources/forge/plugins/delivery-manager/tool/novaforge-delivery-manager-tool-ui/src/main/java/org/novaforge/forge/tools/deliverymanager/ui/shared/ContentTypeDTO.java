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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This enumeration describe the status available for a delivery
 * 
 * @author Guillaume Lamirand
 */
public enum ContentTypeDTO
{
   ECM
   {
      @Override
      public String getId()
      {
         return ECM_ID;
      }
   },
   SCM
   {
      @Override
      public String getId()
      {
         return SCM_ID;
      }
   },
   BUG
   {
      @Override
      public String getId()
      {
         return BUG_ID;
      }
   },
   FILE
   {
      @Override
      public String getId()
      {
         return FILE_ID;
      }
   },
   NOTE
   {
      @Override
      public String getId()
      {
         return NOTE_ID;
      }
   };

   private static final String SCM_ID  = "scm";
   private static final String ECM_ID  = "ecm";
   private static final String BUG_ID  = "bug";
   private static final String FILE_ID = "file";
   private static final String NOTE_ID = "note";

   public static ContentTypeDTO getById(final String pId)
   {
      ContentTypeDTO returnType = null;
      final ContentTypeDTO[] values = ContentTypeDTO.values();
      for (final ContentTypeDTO type : values)
      {
         if (type.getId().equals(pId))
         {
            returnType = type;
            break;
         }
      }
      return returnType;
   }

   public abstract String getId();

   public static List<ContentTypeDTO> list()
   {
      final List<ContentTypeDTO> typeList = new ArrayList<ContentTypeDTO>();
      final ContentTypeDTO[] values = ContentTypeDTO.values();
      Collections.addAll(typeList, values);
      return typeList;
   }
}
