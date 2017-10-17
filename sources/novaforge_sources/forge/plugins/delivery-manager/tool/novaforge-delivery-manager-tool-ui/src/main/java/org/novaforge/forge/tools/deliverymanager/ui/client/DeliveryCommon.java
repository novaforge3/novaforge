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
/**
 * 
 */
package org.novaforge.forge.tools.deliverymanager.ui.client;

import com.google.gwt.core.client.GWT;
import org.novaforge.forge.tools.deliverymanager.ui.client.properties.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.ressources.DeliveryManagementRessources;
import org.novaforge.forge.tools.deliverymanager.ui.shared.exceptions.DeliveryManagementServiceException;
import org.novaforge.forge.tools.deliverymanager.ui.shared.exceptions.ExceptionCode;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;

/**
 * @author Guillaume Lamirand
 */
public final class DeliveryCommon
{
   private static final DeliveryManagement           MESSAGES = GWT.create(DeliveryManagement.class);
   private static final DeliveryManagementRessources RESOURCE = GWT.create(DeliveryManagementRessources.class);

   public static void displayErrorMessage(final Throwable caught)
   {
      if ((caught instanceof DeliveryManagementServiceException)
            && (((DeliveryManagementServiceException) caught).getCode() != null))
      {
         final ExceptionCode code = ((DeliveryManagementServiceException) caught).getCode();

         final InfoDialogBox info = new InfoDialogBox(code.getLocalizedMessage(), InfoTypeEnum.WARNING);
         info.show();
      }
      else
      {
         final InfoDialogBox info = new InfoDialogBox(ExceptionCode.TECHNICAL_ERROR.getLocalizedMessage(),
               InfoTypeEnum.WARNING);
         info.show();
      }
   }

   /**
    * @return the messages
    */
   public static DeliveryManagement getMessages()
   {
      return MESSAGES;
   }

   /**
    * @return the resource
    */
   public static DeliveryManagementRessources getResources()
   {
      return RESOURCE;
   }

}
