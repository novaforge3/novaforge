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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons;

import org.novaforge.forge.tools.managementmodule.ui.client.properties.ErrorCodeMapping;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ErrorEnumeration;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.List;

/**
 * Display the errors messages in dialog box
 */
public class ErrorManagement
{
  /**
   * private constructor
   */
  private ErrorManagement()
  {
  }

   public static void displayErrorMessage(final Throwable caught)
   {
      if ((caught instanceof ManagementModuleException)
            && (((ManagementModuleException) caught).getCode() != null))
      {
         ErrorEnumeration code = ((ManagementModuleException) caught).getCode();
         displayErrorMessage(code);
      }
      else
      {
         InfoDialogBox info = new InfoDialogBox(ErrorCodeMapping.getLocalizedMessage(ErrorEnumeration.TECHNICAL_ERROR), InfoTypeEnum.KO);
         info.getDialogPanel().center();
         info.getDialogPanel().show();
      }
   }

   /**
    * Display an error message using an error code
    *
    * @param code
    *     the code to use to look for the message
    */
   public static void displayErrorMessage(final ErrorEnumeration code)
   {
      InfoDialogBox info = new InfoDialogBox(ErrorCodeMapping.getLocalizedMessage(code), InfoTypeEnum.KO);
      info.getDialogPanel().center();
      info.getDialogPanel().show();
   }
   
   public static void displayMultipleErrorMessage(final Throwable caught, final String pGlobalMessage)
   {
      if ((caught instanceof ManagementModuleException) && (!((ManagementModuleException) caught).getErrors()
                                                                                                 .isEmpty()))
      {
         List<ErrorEnumeration> codes = ((ManagementModuleException) caught).getErrors();
         StringBuilder errorMessage = new StringBuilder();
         errorMessage.append(pGlobalMessage);
         for (ErrorEnumeration error : codes)
         {
            errorMessage.append("<br/> - ");
            errorMessage.append(ErrorCodeMapping.getLocalizedMessage(error));
         }
         InfoDialogBox info = new InfoDialogBox(errorMessage.toString(), InfoTypeEnum.KO);
         info.getDialogPanel().center();
         info.getDialogPanel().show();
      }
      else
      {
         InfoDialogBox info = new InfoDialogBox(ErrorCodeMapping.getLocalizedMessage(ErrorEnumeration.TECHNICAL_ERROR), InfoTypeEnum.KO);
         info.getDialogPanel().center();
         info.getDialogPanel().show();
      }
   }
   
   /**
    * Display an error message
    * @param message the message to display
    */
   public static void displayErrorMessage(final String message){
      InfoDialogBox info = new InfoDialogBox(message, InfoTypeEnum.KO);
      info.getDialogPanel().center();
      info.getDialogPanel().show();
   }
}
