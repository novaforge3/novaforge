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
package org.novaforge.forge.ui.distribution.client.view.commons.dialogbox;

import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.distribution.shared.enumeration.TypeDistributionRequestEnum;

/**
 * @author BILET-JC
 */
public class ForgeValidateDialogBox extends ValidateDialogBox
{
   private String                      objectId;
   private TypeDistributionRequestEnum type;

   public ForgeValidateDialogBox(final String pContent)
   {
      super(pContent);
   }

   /**
    * @return the objectId
    */
   public String getObjectId()
   {
      return this.objectId;
   }

   /**
    * @param objectId
    *           the objectId to set
    */
   public void setObjectId(final String objectId)
   {
      this.objectId = objectId;
   }

   /**
    * @return the type
    */
   public TypeDistributionRequestEnum getType()
   {
      return this.type;
   }

   /**
    * @param type
    *           the type to set
    */
   public void setType(final TypeDistributionRequestEnum type)
   {
      this.type = type;
   }

}
